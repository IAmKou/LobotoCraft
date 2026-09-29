package com.kouthekoi.lobotocraft.api.data.recipe;

import com.kouthekoi.lobotocraft.AllBlocks;
import com.kouthekoi.lobotocraft.api.validate.ContainmentValidationResult;
import com.kouthekoi.lobotocraft.api.validate.FormableWallBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ContainmentRoomValidator {

    public static final int MIN_WIDTH = 3;
    public static final int MIN_HEIGHT = 3;
    public static final int MIN_LENGTH = 3;

    public static final int MAX_WIDTH = 32;
    public static final int MAX_HEIGHT = 16;
    public static final int MAX_LENGTH = 32;

    private static final int MAX_BLOCKS =
            MAX_WIDTH * MAX_HEIGHT * MAX_LENGTH;

    private ContainmentRoomValidator() {
    }

    private static final String NOT_ENCLOSED = "Room is not enclosed (or is too big)";

    public static ContainmentValidationResult validate(ServerLevel level, BlockPos controllerPos) {
        BlockPos doorPos = findIronDoor(level, controllerPos);
        if (doorPos == null) {
            return ContainmentValidationResult.invalid("Controller must be next to an iron door");
        }

        BlockState doorState = level.getBlockState(doorPos);
        if (doorState.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
            doorPos = doorPos.below();
        }
        Direction passage = doorState.getValue(DoorBlock.FACING);
        Set<BlockPos> doorParts = Set.of(doorPos, doorPos.above());

        // The room is on one of the two sides of the door. Try both.
        String error = "Cannot find the inside of the containment room";
        for (Direction side : new Direction[]{passage, passage.getOpposite()}) {
            BlockPos start = doorPos.relative(side);
            if (!isInteriorSpace(level.getBlockState(start))) continue;

            ContainmentValidationResult r = validateFrom(level, start, controllerPos, doorParts);
            if (r.valid()) return r;
            // prefer a specific error over the generic "not enclosed"
            if (!r.reason().equals(NOT_ENCLOSED) || error.startsWith("Cannot")) error = r.reason();
        }
        return ContainmentValidationResult.invalid(error);
    }

    private static ContainmentValidationResult validateFrom(
            ServerLevel level, BlockPos start, BlockPos controllerPos, Set<BlockPos> doorParts) {

        Set<BlockPos> interior = floodInterior(level, start, doorParts);
        if (interior == null) return ContainmentValidationResult.invalid(NOT_ENCLOSED);

        RoomBounds b = RoomBounds.from(interior);
        if (!b.isValidSize()) {
            return ContainmentValidationResult.invalid(
                    "Room dimensions are invalid: " + b.width() + "x" + b.height() + "x" + b.length());
        }

        Set<BlockPos> walls = new HashSet<>();
        for (BlockPos pos : interior) {
            for (Direction dir : Direction.values()) {
                BlockPos n = pos.relative(dir);
                if (interior.contains(n) || doorParts.contains(n) || n.equals(controllerPos)) continue;

                BlockState s = level.getBlockState(n);
                if (isContainmentWall(s)) {
                    walls.add(n.immutable());
                } else if (dir == Direction.DOWN) {
                    if (!s.is(Blocks.STONE)) {
                        return ContainmentValidationResult.invalid(
                                "Floor must be stone or containment wall at " + n.toShortString());
                    }
                } else {
                    return ContainmentValidationResult.invalid(
                            "Invalid wall/ceiling at " + n.toShortString());
                }
            }
        }
        return ContainmentValidationResult.valid("Valid", walls);
    }
    /*
     * ---------------------------------------------------------
     * FIND DOOR
     * ---------------------------------------------------------
     */
    private static BlockPos findIronDoor(ServerLevel level, BlockPos controllerPos) {
        for (Direction d : Direction.values()) {
            BlockPos n = controllerPos.relative(d);
            if (level.getBlockState(n).is(Blocks.IRON_DOOR)) return n;
        }
        return null;
    }
    private static boolean isInteriorSpace(BlockState s) { return s.isAir(); }

    private static boolean isContainmentWall(BlockState s) {
        return s.getBlock() instanceof FormableWallBlock;
    }

    /*
     * ---------------------------------------------------------
     * FIND ROOM SIDE OF DOOR
     * ---------------------------------------------------------
     */

    private static BlockPos findInteriorSide(
            ServerLevel level,
            BlockPos controllerPos,
            BlockPos doorPos
    ) {

        /*
         * Direction from controller -> door.
         */
        Direction controllerToDoor =
                directionBetween(controllerPos, doorPos);

        if (controllerToDoor == null) {
            return null;
        }

        /*
         * The side opposite the controller
         * should be the inside of the room.
         */
        BlockPos inside =
                doorPos.relative(controllerToDoor);

        BlockState state =
                level.getBlockState(inside);

        if (isInteriorSpace(state)) {
            return inside;
        }

        /*
         * Iron doors occupy two vertical blocks.
         * Try the block one block above the lower door.
         */
        BlockPos above =
                inside.above();

        if (isInteriorSpace(
                level.getBlockState(above))) {

            return above;
        }

        return null;
    }

    /*
     * Determine direction between two adjacent positions.
     */
    private static Direction directionBetween(
            BlockPos from,
            BlockPos to
    ) {

        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        int dz = to.getZ() - from.getZ();

        if (dx == 1 && dy == 0 && dz == 0)
            return Direction.EAST;

        if (dx == -1 && dy == 0 && dz == 0)
            return Direction.WEST;

        if (dx == 0 && dy == 1 && dz == 0)
            return Direction.UP;

        if (dx == 0 && dy == -1 && dz == 0)
            return Direction.DOWN;

        if (dx == 0 && dy == 0 && dz == 1)
            return Direction.SOUTH;

        if (dx == 0 && dy == 0 && dz == -1)
            return Direction.NORTH;

        return null;
    }

    /*
     * ---------------------------------------------------------
     * FLOOD FILL
     * ---------------------------------------------------------
     */

    private static Set<BlockPos> floodInterior(ServerLevel level, BlockPos start, Set<BlockPos> doorParts) {
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            BlockPos cur = queue.poll();
            for (Direction d : Direction.values()) {
                BlockPos n = cur.relative(d);
                if (visited.contains(n) || doorParts.contains(n)) continue;

                if (Math.abs(n.getX() - start.getX()) > MAX_WIDTH
                        || Math.abs(n.getY() - start.getY()) > MAX_HEIGHT
                        || Math.abs(n.getZ() - start.getZ()) > MAX_LENGTH
                        || !level.isLoaded(n)) {
                    return null;
                }
                if (!isInteriorSpace(level.getBlockState(n))) continue;

                visited.add(n);
                if (visited.size() > MAX_BLOCKS) return null;
                queue.add(n);
            }
        }
        return visited;
    }


    /*
     * ---------------------------------------------------------
     * FLOOR
     * ---------------------------------------------------------
     */

    private static ContainmentValidationResult validateFloor(
            ServerLevel level,
            Set<BlockPos> interior
    ) {

        for (BlockPos pos : interior) {

            BlockPos below =
                    pos.below();

            BlockState belowState =
                    level.getBlockState(below);

            /*
             * Only check blocks directly below
             * interior air.
             */
            if (!belowState.is(Blocks.STONE)) {

                /*
                 * Don't complain about walls/ceiling here.
                 */
                if (isPotentialFloorPosition(
                        pos,
                        interior)) {

                    return ContainmentValidationResult.invalid(
                            "Floor must be stone at "
                                    + below
                    );
                }
            }
        }

        return ContainmentValidationResult.valid(
                "Valid",
                List.of()
        );
    }

    private static boolean isPotentialFloorPosition(
            BlockPos pos,
            Set<BlockPos> interior
    ) {

        /*
         * A block is considered floor if there is
         * no interior block underneath it.
         */
        return !interior.contains(pos.below());
    }

    /*
     * ---------------------------------------------------------
     * BOUNDARY
     * ---------------------------------------------------------
     */

    private static ContainmentValidationResult validateBoundary(
            ServerLevel level,
            Set<BlockPos> interior,
            BlockPos doorPos
    ) {

        for (BlockPos pos : interior) {

            for (Direction direction :
                    Direction.values()) {

                BlockPos neighbor =
                        pos.relative(direction);

                /*
                 * Another interior block.
                 */
                if (interior.contains(neighbor)) {
                    continue;
                }

                /*
                 * Door is allowed.
                 */
                if (neighbor.equals(doorPos)) {
                    continue;
                }

                BlockState boundary =
                        level.getBlockState(neighbor);

                /*
                 * Floor.
                 */
                if (direction == Direction.DOWN
                        && boundary.is(Blocks.STONE)) {

                    continue;
                }

                /*
                 * Containment walls.
                 */
                if (isContainmentWall(boundary)) {
                    continue;
                }

                /*
                 * Anything else means the room
                 * has an invalid opening.
                 */
                return ContainmentValidationResult.invalid(
                        "Invalid room boundary at "
                                + neighbor
                );
            }
        }

        return ContainmentValidationResult.valid(
                "Valid",
                List.of()
        );
    }


    /*
     * ---------------------------------------------------------
     * RESULT
     * ---------------------------------------------------------
     */

    private record FloodResult(
            Set<BlockPos> positions,
            boolean escaped
    ) {
    }

    /*
     * ---------------------------------------------------------
     * ROOM BOUNDS
     * ---------------------------------------------------------
     */

    public record RoomBounds(
            int minX,
            int maxX,
            int minY,
            int maxY,
            int minZ,
            int maxZ
    ) {

        public static RoomBounds from(
                Set<BlockPos> positions
        ) {

            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int minZ = Integer.MAX_VALUE;

            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int maxZ = Integer.MIN_VALUE;

            for (BlockPos pos : positions) {

                minX = Math.min(
                        minX,
                        pos.getX()
                );

                minY = Math.min(
                        minY,
                        pos.getY()
                );

                minZ = Math.min(
                        minZ,
                        pos.getZ()
                );

                maxX = Math.max(
                        maxX,
                        pos.getX()
                );

                maxY = Math.max(
                        maxY,
                        pos.getY()
                );

                maxZ = Math.max(
                        maxZ,
                        pos.getZ()
                );
            }

            return new RoomBounds(
                    minX,
                    maxX,
                    minY,
                    maxY,
                    minZ,
                    maxZ
            );
        }

        public int width() {
            return maxX - minX + 1;
        }

        public int height() {
            return maxY - minY + 1;
        }

        public int length() {
            return maxZ - minZ + 1;
        }

        public boolean isValidSize() {

            return width() >= MIN_WIDTH
                    && width() <= MAX_WIDTH
                    && height() >= MIN_HEIGHT
                    && height() <= MAX_HEIGHT
                    && length() >= MIN_LENGTH
                    && length() <= MAX_LENGTH;
        }
    }
}