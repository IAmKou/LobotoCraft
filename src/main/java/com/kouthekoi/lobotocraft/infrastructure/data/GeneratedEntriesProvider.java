package com.kouthekoi.lobotocraft.infrastructure.data;

import com.kouthekoi.lobotocraft.LobotoCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder();
//            .add(Registries.CONFIGURED_FEATURE, AllConfiguredFeatures::bootstrap)
//            .add(Registries.PLACED_FEATURE, AllPlacedFeatures::bootstrap)
//            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, AllBiomeModifiers::bootstrap);

    public GeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(LobotoCraft.ID));
    }

    @Override
    public String getName() {
        return "Loboto's Generated Registry Entries";
    }
}
