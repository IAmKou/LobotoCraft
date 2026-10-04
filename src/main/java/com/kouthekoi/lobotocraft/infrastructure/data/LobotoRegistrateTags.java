package com.kouthekoi.lobotocraft.infrastructure.data;

import com.kouthekoi.lobotocraft.AllTags;
import com.kouthekoi.lobotocraft.LobotoCraft;
import com.kouthekoi.lobotocraft.foundation.data.LobotoRegistrate;
import com.kouthekoi.lobotocraft.foundation.data.TagGen.CreateTagsProvider;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

public class LobotoRegistrateTags  {
private static final LobotoRegistrate REGISTRATE = LobotoCraft.registrate();

public static void addGenerators() {
    REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, LobotoRegistrateTags::genBlockTags);
    REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, LobotoRegistrateTags::genItemTags);
//    REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, CreateRegistrateTags::genFluidTags);
//    REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, CreateRegistrateTags::genEntityTags);
}

private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
    CreateTagsProvider<Block> prov = new CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);

    prov.tag(Tags.Blocks.CONCRETES)
            .addTag(AllTags.AllBlockTags.CONTAINMENT_BUILDING_BLOCK.tag);


}

private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
    CreateTagsProvider<Item> prov = new CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

    prov.tag(Tags.Items.INGOTS)
            .addTag(AllTags.AllItemTags.LOBOTO_INGOTS.tag);

}
}
