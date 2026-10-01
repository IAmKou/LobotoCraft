package com.kouthekoi.lobotocraft.foundation.networking;

import com.kouthekoi.lobotocraft.content.containmentcontroller.ContainmentControllerBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ControllerActionPacket(BlockPos pos, boolean assemble) implements CustomPacketPayload {

    public static final Type<ControllerActionPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("lobotocraft", "controller_action"));

    public static final StreamCodec<ByteBuf, ControllerActionPacket> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ControllerActionPacket::pos,
            ByteBufCodecs.BOOL, ControllerActionPacket::assemble,
            ControllerActionPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ControllerActionPacket packet, IPayloadContext ctx) {
        // Runs on the server main thread by default in 1.21.1
        if (!(ctx.player() instanceof ServerPlayer player)) return;
        ServerLevel level = player.serverLevel();

        // Never trust the client: check distance and that the chunk is loaded
        if (!level.isLoaded(packet.pos()) || player.distanceToSqr(packet.pos().getCenter()) > 64) return;

        if (level.getBlockEntity(packet.pos()) instanceof ContainmentControllerBlockEntity controller) {
            if (packet.assemble()) controller.validateRoom();
            else controller.disassemble();
        }
    }
}
