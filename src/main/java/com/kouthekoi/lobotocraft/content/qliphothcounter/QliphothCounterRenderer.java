package com.kouthekoi.lobotocraft.content.qliphothcounter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class QliphothCounterRenderer implements BlockEntityRenderer<QliphothCounterBlockEntity> {
    private final Font font;

    public QliphothCounterRenderer(BlockEntityRendererProvider.Context ctx) {
        this.font = ctx.getFont();
    }

    @Override
    public void render(QliphothCounterBlockEntity be, float partialTick, PoseStack pose,
                       MultiBufferSource buffer, int light, int overlay) {
        int value = be.getDisplay();
        Level level = be.getLevel();
        if (value < 0 || level == null) return;

        String text = String.valueOf(value);
        int color = value == 0 ? 0xFF2020 : 0xFFB030;
        float scale = Math.min(0.1f, 0.8f / font.width(text));   // shrink for 2+ digits

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (!level.getBlockState(be.getBlockPos().relative(dir)).isAir()) continue;

            pose.pushPose();
            pose.translate(0.5, 0.5, 0.5);
            pose.mulPose(Axis.YP.rotationDegrees(-dir.toYRot()));
            pose.translate(0, 0, 0.505);
            pose.scale(scale, -scale, scale);
            font.drawInBatch(text, -font.width(text) / 2f, -font.lineHeight / 2f, color, false,
                    pose.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
            pose.popPose();
        }
    }
}
