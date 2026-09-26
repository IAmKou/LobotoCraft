package com.lobotocraft;

import com.lobotocraft.foundation.data.LobotoRegistrate;
import com.lobotocraft.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;

public class AllItems {
    private static final LobotoRegistrate REGISTRATE = LobotoCraft.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }
    public static final ItemEntry<Item> RAW_LEAD =
            taggedIngredient("raw_lead", CommonMetal.LEAD.rawOres, Tags.Items.RAW_MATERIALS);
    public static final ItemEntry<Item> RAW_SILVER =
            taggedIngredient("raw_silver", CommonMetal.SILVER.rawOres, Tags.Items.RAW_MATERIALS);
    public static final ItemEntry<Item> RAW_MERCURY =
            taggedIngredient("raw_mercury", CommonMetal.MERCURY.rawOres, Tags.Items.RAW_MATERIALS);
    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new)
                .tag(tags)
                .register();
    }
    public static void register() {
    }
}
