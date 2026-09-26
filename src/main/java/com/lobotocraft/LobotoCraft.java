package com.lobotocraft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lobotocraft.foundation.data.LobotoRegistrate;
import com.lobotocraft.foundation.item.TooltipModifier;
import com.lobotocraft.infrastructure.data.CreateDatagen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import static javax.swing.Action.NAME;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(LobotoCraft.ID)
public class LobotoCraft {
    public static final String ID = "lobotocraft";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    private static final LobotoRegistrate REGISTRATE = LobotoRegistrate.create(ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    public LobotoCraft(IEventBus modEventBus, ModContainer modContainer) {
        onCtor(modEventBus, modContainer);
    }

    public static void onCtor(IEventBus modEventBus, ModContainer modContainer) {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        REGISTRATE.registerEventListeners(modEventBus);
        AllCreativeModeTabs.register(modEventBus);
        AllBlocks.register();
        AllItems.register();

        modEventBus.addListener(EventPriority.HIGHEST, CreateDatagen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, CreateDatagen::gatherData);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static LobotoRegistrate registrate() {
        if (!STACK_WALKER.getCallerClass().getPackageName().startsWith("com.lobotocraft"))
            throw new UnsupportedOperationException("Other mods are not permitted to use create's registrate instance.");
        return REGISTRATE;
    }

}
