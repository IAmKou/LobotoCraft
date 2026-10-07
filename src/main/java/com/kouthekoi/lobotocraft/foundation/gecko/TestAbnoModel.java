package com.kouthekoi.lobotocraft.foundation.gecko;

import com.kouthekoi.lobotocraft.LobotoCraft;
import com.kouthekoi.lobotocraft.foundation.entity.TestAbnoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TestAbnoModel extends GeoModel<TestAbnoEntity> {

    @Override
    public ResourceLocation getModelResource(TestAbnoEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(
                LobotoCraft.ID,
                "geo/test_abonor.geo.json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(TestAbnoEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(
                LobotoCraft.ID,
                "geckolib/textures/entity/test_abonor.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(TestAbnoEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(
                LobotoCraft.ID,
                "animations/test_abonor.animation.json"
        );
    }
}
