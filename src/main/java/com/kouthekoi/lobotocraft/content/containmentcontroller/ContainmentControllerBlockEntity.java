package com.kouthekoi.lobotocraft.content.containmentcontroller;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class  ContainmentControllerBlockEntity extends BlockEntity {
    private boolean active = false;
    private String validationReason = "Not validated";
    private final List<BlockPos> structure = new ArrayList<>();

    public ContainmentControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean isActive() {
        return active;
    }

    public String getValidationReason() {
        return validationReason;
    }

    public void validateRoom() {
        if (!(level instanceof ServerLevel server)) return;

        setFormed(server, false);   // un-form old walls, no sync yet
        structure.clear();

        ContainmentValidationResult r = ContainmentRoomValidator.validate(server, worldPosition);
        active = r.valid();
        validationReason = r.reason();
        if (r.valid()) {
            structure.addAll(r.walls());
            setFormed(server, true);
        }
        sync();   // was setChanged()
    }

    public void unform(ServerLevel server) {
        setFormed(server, false);
        structure.clear();
        active = false;
        sync();   // was setChanged()
    }

    public void serverTick() {
        if (!active || !(level instanceof ServerLevel server) || server.getGameTime() % 20 != 0) return;
        for (BlockPos p : structure) {
            if (!server.isLoaded(p)) return;
            BlockState s = server.getBlockState(p);

            boolean stillWall = ContainmentRoomValidator.isContainmentWall(s);
            boolean notFormed = s.getBlock() instanceof FormableWallBlock
                    && !s.getValue(FormableWallBlock.FORMED);

            if (!stillWall || notFormed) {
                unform(server);
                validationReason = "Structure was broken";
                sync();
                return;
            }
        }
    }

    private void setFormed(ServerLevel server, boolean formed) {
        for (BlockPos p : structure) {
            if (!server.isLoaded(p)) continue;
            BlockState s = server.getBlockState(p);
            if (s.getBlock() instanceof FormableWallBlock && s.getValue(FormableWallBlock.FORMED) != formed) {
                server.setBlock(p, s.setValue(FormableWallBlock.FORMED, formed), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("Active", active);
        tag.putString("ValidationReason", validationReason);
        tag.putLongArray("Structure", structure.stream().mapToLong(BlockPos::asLong).toArray());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        active = tag.getBoolean("Active");
        validationReason = tag.getString("ValidationReason");
        structure.clear();
        for (long l : tag.getLongArray("Structure")) structure.add(BlockPos.of(l));
    }

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void disassemble() {
        if (!(level instanceof ServerLevel server)) return;
        unform(server);
        validationReason = "Disassembled";
        sync();
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
}
