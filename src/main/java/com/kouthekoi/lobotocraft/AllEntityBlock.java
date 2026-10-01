package com.kouthekoi.lobotocraft;

import com.kouthekoi.lobotocraft.content.containmentcontroller.ContainmentControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AllEntityBlock extends Block {
    public AllEntityBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {

        if (!level.isClientSide()) {

            BlockEntity blockEntity =
                    level.getBlockEntity(pos);

            if (blockEntity instanceof ContainmentControllerBlockEntity controller) {

                controller.validateRoom();

                player.displayClientMessage(
                        Component.literal(
                                controller.getValidationReason()
                        ),
                        true
                );
            }
        }

        return InteractionResult.SUCCESS;
    }
}
