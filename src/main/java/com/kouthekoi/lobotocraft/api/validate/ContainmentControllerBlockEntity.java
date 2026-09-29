package com.kouthekoi.lobotocraft.api.validate;

import com.kouthekoi.lobotocraft.AllBlocks;
import com.kouthekoi.lobotocraft.AllBlocksEntity;
import com.kouthekoi.lobotocraft.api.data.recipe.ContainmentRoomValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
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

        unform(server);
        ContainmentValidationResult r = ContainmentRoomValidator.validate(server, worldPosition);
        active = r.valid();
        validationReason = r.reason();
        if (r.valid()) {
            structure.addAll(r.walls());
            setFormed(server, true);
        }
        setChanged();
    }

    public void unform(ServerLevel server) {
        setFormed(server, false);
        structure.clear();
        active = false;
        setChanged();
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

    /**
     * Cheap integrity check: only looks at the stored wall positions, once a second.
     */
    public void serverTick() {
        if (!active || !(level instanceof ServerLevel server) || server.getGameTime() % 20 != 0) return;
        for (BlockPos p : structure) {
            if (!server.isLoaded(p)) return;
            BlockState s = server.getBlockState(p);
            if (!(s.getBlock() instanceof FormableWallBlock) || !s.getValue(FormableWallBlock.FORMED)) {
                unform(server);
                validationReason = "Structure was broken";
                return;
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
}
