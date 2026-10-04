package com.kouthekoi.lobotocraft.content.containmentcontroller;

import com.kouthekoi.lobotocraft.content.door.ContainmentDoorBlock;
import com.kouthekoi.lobotocraft.content.qliphothcounter.QliphothCounterBlock;
import com.kouthekoi.lobotocraft.content.qliphothcounter.QliphothCounterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class  ContainmentControllerBlockEntity extends BlockEntity {
    private boolean active = false;
    private String validationReason = "Not validated";
    private final List<BlockPos> structure = new ArrayList<>();

    private int maxCount = 10;
    private int count = 10;
    private AABB room = null;
    private final Set<UUID> inside = new HashSet<>();
    private boolean insideReady = false;
    private Boolean lastSwitch = null;   // last seen switch state, null until first scan

    public ContainmentControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean isActive() { return active; }
    public String getValidationReason() { return validationReason; }
    public int getMaxCount() { return maxCount; }
    public int getCount() { return count; }

    // ---------- countdown API ----------

    public void setMaxCount(int max) {
        maxCount = Mth.clamp(max, 1, 99);
        count = maxCount;
        if (level instanceof ServerLevel server) {
            refreshDisplays(server);
            sync();
        }
    }

    /** One trigger = one step down. At 0 the door is forced open. */
    public void triggerCountdown(ServerLevel server) {
        if (!active || count <= 0) return;
        count--;
        if (count == 0) setDoorOpen(server, true);
        refreshDisplays(server);
        sync();
    }

    /** Closes the door and refills the counter to its max. */
    public void resetAndCloseDoor(ServerLevel server) {
        count = maxCount;
        setDoorOpen(server, false);
        refreshDisplays(server);
        sync();
    }

    private void setDoorOpen(ServerLevel server, boolean open) {
        BlockPos door = ContainmentRoomValidator.findIronDoor(server, worldPosition);
        if (door != null) ContainmentDoorBlock.setDoorAndLevers(server, door, open);
    }
    // ---------- assemble / disassemble ----------

    public void validateRoom() {
        if (!(level instanceof ServerLevel server)) return;

        setFormed(server, false);
        structure.clear();
        room = null;
        inside.clear();
        insideReady = false;
        lastSwitch = null;
        count = maxCount;

        ContainmentValidationResult r = ContainmentRoomValidator.validate(server, worldPosition);
        active = r.valid();
        validationReason = r.reason();
        if (r.valid()) {
            structure.addAll(r.walls());
            room = r.room();
            setFormed(server, true);
        }
        sync();
    }

    public void disassemble() {
        if (!(level instanceof ServerLevel server)) return;
        unform(server);
        validationReason = "Disassembled";
        sync();
    }

    public void unform(ServerLevel server) {
        setFormed(server, false);
        structure.clear();
        active = false;
        room = null;
        inside.clear();
        insideReady = false;
        lastSwitch = null;
        count = maxCount;
        sync();
    }

    private void refreshDisplays(ServerLevel server) {
        setFormed(server, true);
    }

    /** Sets FORMED on formable walls and pushes the number to the counters. */
    private void setFormed(ServerLevel server, boolean formed) {
        for (BlockPos p : structure) {
            if (!server.isLoaded(p)) continue;
            BlockState s = server.getBlockState(p);
            if (!(s.getBlock() instanceof FormableWallBlock)) continue;

            if (s.getValue(FormableWallBlock.FORMED) != formed) {
                server.setBlock(p, s.setValue(FormableWallBlock.FORMED, formed), Block.UPDATE_ALL);
            }
            if (server.getBlockEntity(p) instanceof QliphothCounterBlockEntity display) {
                display.setDisplay(formed ? count : -1);
            }
        }
    }

    // ---------- ticking ----------

    public void serverTick() {
        if (!active || !(level instanceof ServerLevel server)) return;
        long time = server.getGameTime();

        if (time % 20 == 0 && !structureIntact(server)) return;
        if (time % 5 == 0) {
            trackPlayers(server);
            checkSwitch(server);
        }
    }

    private boolean structureIntact(ServerLevel server) {
        for (BlockPos p : structure) {
            if (!server.isLoaded(p)) return true;
            BlockState s = server.getBlockState(p);

            boolean stillWall = ContainmentRoomValidator.isContainmentWall(s);
            boolean notFormed = s.getBlock() instanceof FormableWallBlock
                    && !s.getValue(FormableWallBlock.FORMED);

            if (!stillWall || notFormed) {
                unform(server);
                validationReason = "Structure was broken";
                sync();
                return false;
            }
        }
        return true;
    }

    private void trackPlayers(ServerLevel server) {
        if (room == null) return;

        Set<UUID> now = new HashSet<>();
        int entered = 0;

        for (ServerPlayer p : server.getEntitiesOfClass(ServerPlayer.class, room)) {
            if (p.isSpectator() || !room.contains(p.position())) continue;
            now.add(p.getUUID());
            if (insideReady && !inside.contains(p.getUUID())) entered++;
        }

        inside.clear();
        inside.addAll(now);
        insideReady = true;

        for (int i = 0; i < entered; i++) triggerCountdown(server);
    }

    /** A lever or button touching the controller resets the room when its state changes. */
    private void checkSwitch(ServerLevel server) {
        boolean on = false;
        for (Direction d : Direction.values()) {
            BlockState s = server.getBlockState(worldPosition.relative(d));
            if ((s.getBlock() instanceof LeverBlock || s.getBlock() instanceof ButtonBlock)
                    && s.getValue(BlockStateProperties.POWERED)) {
                on = true;
                break;
            }
        }
        if (lastSwitch != null && lastSwitch != on) resetAndCloseDoor(server);
        lastSwitch = on;
    }

    // ---------- sync / save ----------

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("Active", active);
        tag.putString("ValidationReason", validationReason);
        tag.putLongArray("Structure", structure.stream().mapToLong(BlockPos::asLong).toArray());
        tag.putInt("MaxCount", maxCount);
        tag.putInt("Count", count);
        if (room != null) {
            tag.putIntArray("Room", new int[]{
                    (int) room.minX, (int) room.minY, (int) room.minZ,
                    (int) room.maxX, (int) room.maxY, (int) room.maxZ});
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        active = tag.getBoolean("Active");
        validationReason = tag.getString("ValidationReason");
        structure.clear();
        for (long l : tag.getLongArray("Structure")) structure.add(BlockPos.of(l));
        maxCount = tag.contains("MaxCount") ? tag.getInt("MaxCount") : 10;
        count = tag.contains("Count") ? tag.getInt("Count") : maxCount;
        int[] r = tag.getIntArray("Room");
        room = r.length == 6 ? new AABB(r[0], r[1], r[2], r[3], r[4], r[5]) : null;
        insideReady = false;
        lastSwitch = null;
    }
}
