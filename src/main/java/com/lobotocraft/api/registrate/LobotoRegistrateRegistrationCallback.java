package com.lobotocraft.api.registrate;

import com.lobotocraft.foundation.data.LobotoRegistrate;
import com.lobotocraft.impl.registrate.LobotoRegistrateRegistrationCallbackImpl;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class LobotoRegistrateRegistrationCallback {
    public static <R, T extends R> void register(ResourceKey<? extends Registry<R>> registry, ResourceLocation id, NonNullConsumer<? super T> callback) {
        LobotoRegistrateRegistrationCallbackImpl.<R, T>register(registry, id, callback);
    }


    public static void provideRegistrate(LobotoRegistrate registrate) {
        LobotoRegistrateRegistrationCallbackImpl.provideRegistrate(registrate);
    }

    private LobotoRegistrateRegistrationCallback() {
        throw new AssertionError("This class should not be instantiated");
    }
}
