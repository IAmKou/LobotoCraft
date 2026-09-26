package com.lobotocraft;

import com.lobotocraft.foundation.data.LobotoRegistrate;
import com.lobotocraft.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.neoforged.neoforge.common.Tags;

import java.util.Map;

import static com.lobotocraft.foundation.data.TagGen.pickaxeOnly;
import static com.lobotocraft.foundation.data.TagGen.tagBlockAndItem;

public class AllBlocks {
    private static final LobotoRegistrate REGISTRATE = LobotoCraft.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }
    public static final BlockEntry<Block> LEAD_ORE = REGISTRATE.block("lead_ore", Block::new)
            .initialProperties(() -> Blocks.GOLD_ORE)
            .properties(p -> p.mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) ->  {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(AllItems.RAW_LEAD.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem(Map.of(
                    CommonMetal.LEAD.ores.blocks(), CommonMetal.LEAD.ores.items(),
                    Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE
            )))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_LEAD_ORE = REGISTRATE.block("deepslate_lead_ore", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p.mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(AllItems.RAW_LEAD.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem(Map.of(
                    CommonMetal.LEAD.ores.blocks(), CommonMetal.LEAD.ores.items(),
                    Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE
            )))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_SILVER_ORE = REGISTRATE.block("deepslate_silver_ore", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE_IRON_ORE)
            .properties(p -> p.mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(AllItems.RAW_SILVER.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem(Map.of(
                    CommonMetal.SILVER.ores.blocks(), CommonMetal.SILVER.ores.items(),
                    Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE
            )))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> SILVER_ORE = REGISTRATE.block("silver_ore", Block::new)
            .initialProperties(() -> Blocks.IRON_ORE)
            .properties(p -> p.mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(AllItems.RAW_SILVER.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem(Map.of(
                    CommonMetal.SILVER.ores.blocks(), CommonMetal.SILVER.ores.items(),
                    Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE
            )))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> MERCURY_ORE = REGISTRATE.block("mercury_ore", Block::new)
            .initialProperties(() -> Blocks.LAPIS_ORE)
            .properties(p -> p.mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(AllItems.RAW_MERCURY.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem(Map.of(
                    CommonMetal.MERCURY.ores.blocks(), CommonMetal.MERCURY.ores.items(),
                    Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE
            )))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static void register() {
    }
}
