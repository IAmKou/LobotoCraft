package com.kouthekoi.lobotocraft;

import com.kouthekoi.lobotocraft.foundation.entity.TestAbnoEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(
                    Registries.ENTITY_TYPE,
                    LobotoCraft.ID
            );

    public static final DeferredHolder<
                EntityType<?>,
                EntityType<TestAbnoEntity>
                > TEST_ABNO = ENTITY_TYPES.register(
            "test_abonor",
            () -> EntityType.Builder.of(
                            TestAbnoEntity::new,
                            MobCategory.MONSTER
                    )
                    .sized(1.0F, 2.0F)
                    .build("test_abonor")
    );

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
