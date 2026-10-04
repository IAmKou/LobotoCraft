package com.kouthekoi.lobotocraft.content.qliphothcounter;

import com.kouthekoi.lobotocraft.AllBlocksEntity;
import com.kouthekoi.lobotocraft.content.containmentcontroller.FormableWallBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class QliphothCounterBlock extends FormableWallBlock implements EntityBlock {
    public QliphothCounterBlock(Properties props) { super(props); }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AllBlocksEntity.QLIPHOTH_COUNTER.create(pos, state);
    }
}
