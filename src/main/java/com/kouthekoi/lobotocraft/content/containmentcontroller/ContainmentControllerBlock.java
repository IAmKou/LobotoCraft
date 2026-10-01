package com.kouthekoi.lobotocraft.content.containmentcontroller;

import com.kouthekoi.lobotocraft.AllBlocksEntity;
import com.kouthekoi.lobotocraft.foundation.utility.NetHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ContainmentControllerBlock extends Block implements EntityBlock {
    public ContainmentControllerBlock(Properties p) {
        super(p);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AllBlocksEntity.CONTAINMENT_CONTROLLER.create(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide || type != AllBlocksEntity.CONTAINMENT_CONTROLLER.get()) return null;
        return (l, p, s, be) -> ((ContainmentControllerBlockEntity) be).serverTick();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            NetHelper.ClientHooks.openControllerScreen(pos);
        }
        return InteractionResult.SUCCESS;
    }

    // 1.21.1 signature. On 1.21.2+ use affectNeighborsAfterRemoval instead.
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof ContainmentControllerBlockEntity c
                && level instanceof ServerLevel sl) {
            c.unform(sl);
        }
        super.onRemove(state, level, pos, newState, moved);
    }
}
