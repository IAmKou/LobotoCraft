package com.kouthekoi.lobotocraft.foundation.gecko;

import com.kouthekoi.lobotocraft.foundation.entity.TestAbnoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Renderer extends GeoEntityRenderer<TestAbnoEntity> {

    public Renderer(
            EntityRendererProvider.Context context
    ) {
        super(
                context,
                new TestAbnoModel()
        );
    }
}
