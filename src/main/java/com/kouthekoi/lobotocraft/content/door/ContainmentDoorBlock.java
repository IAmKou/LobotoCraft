package com.kouthekoi.lobotocraft.content.door;

import com.kouthekoi.lobotocraft.content.containmentcontroller.ContainmentControllerBlockEntity;
import com.kouthekoi.lobotocraft.content.lever.ContainmentLeverBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.ArrayList;
import java.util.List;

public class ContainmentDoorBlock extends DoorBlock {
    public static final int LEVER_RADIUS = 4;

    public ContainmentDoorBlock(Properties props) {
        super(BlockSetType.IRON, props);   // iron type: no opening by hand
    }

    // Ignore redstone, so only the lever (and the controller) can move it
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   BlockPos neighborPos, boolean movedByPiston) {
    }

    /** Opens or closes the door and puts every nearby containment lever in the same state. */
    public static void setDoorAndLevers(Level level, BlockPos doorPos, boolean open) {
        BlockState s = level.getBlockState(doorPos);
        if (!(s.getBlock() instanceof ContainmentDoorBlock door)) return;

        if (s.getValue(HALF) == DoubleBlockHalf.UPPER) {
            doorPos = doorPos.below();
            s = level.getBlockState(doorPos);
        }
        door.setOpen(null, level, s, doorPos, open);

        for (BlockPos p : findLeversNear(level, doorPos)) {
            BlockState l = level.getBlockState(p);
            if (l.getValue(LeverBlock.POWERED) != open) {
                level.setBlock(p, l.setValue(LeverBlock.POWERED, open), Block.UPDATE_ALL);
                level.updateNeighborsAt(p, l.getBlock());
                level.playSound(null, p, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, open ? 0.6f : 0.5f);
            }
        }
    }

    public static List<BlockPos> findLeversNear(Level level, BlockPos origin) {
        List<BlockPos> result = new ArrayList<>();
        int r = LEVER_RADIUS;
        for (BlockPos p : BlockPos.betweenClosed(origin.offset(-r, -r, -r), origin.offset(r, r, r))) {
            if (level.getBlockState(p).getBlock() instanceof ContainmentLeverBlock) result.add(p.immutable());
        }
        return result;
    }

    /** Nearest door (lower half) within the lever radius, or null. */
    public static BlockPos findDoorNear(Level level, BlockPos origin) {
        BlockPos best = null;
        int r = LEVER_RADIUS;
        for (BlockPos p : BlockPos.betweenClosed(origin.offset(-r, -r, -r), origin.offset(r, r, r))) {
            BlockState s = level.getBlockState(p);
            if (s.getBlock() instanceof ContainmentDoorBlock && s.getValue(HALF) == DoubleBlockHalf.LOWER
                    && (best == null || p.distSqr(origin) < best.distSqr(origin))) {
                best = p.immutable();
            }
        }
        return best;
    }

    /** The controller touching either half of the door, or null. */
    public static ContainmentControllerBlockEntity findController(Level level, BlockPos doorLower) {
        for (BlockPos half : new BlockPos[]{doorLower, doorLower.above()}) {
            for (Direction d : Direction.values()) {
                if (level.getBlockEntity(half.relative(d)) instanceof ContainmentControllerBlockEntity c) return c;
            }
        }
        return null;
    }
}
