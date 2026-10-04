package com.kouthekoi.lobotocraft.content.lever;

import com.kouthekoi.lobotocraft.content.containmentcontroller.ContainmentControllerBlockEntity;
import com.kouthekoi.lobotocraft.content.door.ContainmentDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Properties;

public class ContainmentLeverBlock extends LeverBlock {
    public ContainmentLeverBlock(Properties props) { super(props); }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockPos door = ContainmentDoorBlock.findDoorNear(level, pos);
        if (door == null) {
            player.displayClientMessage(Component.literal("No containment door nearby"), true);
            return InteractionResult.CONSUME;
        }

        boolean open = !state.getValue(POWERED);
        ContainmentControllerBlockEntity controller = ContainmentDoorBlock.findController(level, door);

        if (!open && controller != null && level instanceof ServerLevel server) {
            controller.resetAndCloseDoor(server);          // close + refill the counter
        } else {
            ContainmentDoorBlock.setDoorAndLevers(level, door, open);
        }
        return InteractionResult.CONSUME;
    }
}
