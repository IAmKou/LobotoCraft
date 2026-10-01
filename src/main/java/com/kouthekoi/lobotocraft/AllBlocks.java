package com.kouthekoi.lobotocraft;

import com.kouthekoi.lobotocraft.content.containmentcontroller.FormableWallBlock;
import com.kouthekoi.lobotocraft.foundation.data.LobotoRegistrate;
import com.kouthekoi.lobotocraft.foundation.data.recipe.CommonMetal;
import com.kouthekoi.lobotocraft.content.containmentcontroller.ContainmentControllerBlock;
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
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.Tags;

import java.util.Map;

import static com.kouthekoi.lobotocraft.foundation.data.TagGen.pickaxeOnly;
import static com.kouthekoi.lobotocraft.foundation.data.TagGen.tagBlockAndItem;

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

    public static final BlockEntry<Block> RAW_LEAD_BLOCK = REGISTRATE.block("raw_lead_block", Block::new)
            .initialProperties(() -> Blocks.RAW_GOLD_BLOCK)
            .properties(p -> p.mapColor(MapColor.GLOW_LICHEN)
                    .requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .lang("Block of Raw Lead")
            .transform(tagBlockAndItem(CommonMetal.LEAD.rawStorageBlocks))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .register();

    public static final BlockEntry<Block> LEAD_BLOCK = REGISTRATE.block("lead_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.mapColor(MapColor.GLOW_LICHEN)
                    .requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(CommonMetal.LEAD.storageBlocks))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Lead")
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
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(4.0F, 9.0F)
                                        ))
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

    public static final BlockEntry<FormableWallBlock> CONTAINMENT_WALL =
            REGISTRATE.block("containment_wall", FormableWallBlock::new)
                    .initialProperties(() -> Blocks.DEEPSLATE)
                    .properties(p -> p.strength(20.0F, 6.0F)
                            .mapColor(MapColor.TERRACOTTA_PURPLE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.DEEPSLATE))
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_IRON_TOOL, AllTags.AllBlockTags.BUILDING_BLOCK.tag)
                    .blockstate((ctx, prov) -> prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
                        String name = state.getValue(FormableWallBlock.FORMED)
                                ? ctx.getName() + "_formed" : ctx.getName();
                        return ConfiguredModel.builder()
                                .modelFile(prov.models().cubeAll(name, prov.modLoc("block/" + name)))
                                .build();
                    }))
                    .lang("Containment wall")
                    .simpleItem()
                    .register();

    public static final BlockEntry<FormableWallBlock> HARD_CONTAINMENT_WALL =
            REGISTRATE.block("hard_containment_wall", FormableWallBlock::new)
                    .initialProperties(() -> Blocks.OBSIDIAN)
                    .properties(p -> p.strength(40.0F)
                            .mapColor(MapColor.TERRACOTTA_PURPLE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.DEEPSLATE))
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL, AllTags.AllBlockTags.BUILDING_BLOCK.tag)
                    .blockstate((ctx, prov) -> prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
                        String name = state.getValue(FormableWallBlock.FORMED)
                                ? ctx.getName() + "_formed" : ctx.getName();
                        return ConfiguredModel.builder()
                                .modelFile(prov.models().cubeAll(name, prov.modLoc("block/" + name)))
                                .build();
                    }))
                    .lang("Hard containment wall")
                    .simpleItem()
                    .register();


    public static final BlockEntry<Block> QLIPHOTH_COUNTER = REGISTRATE.block("qliphoth_counter", Block::new)
            .initialProperties(() -> Blocks.GLASS)
            .properties(p -> p.mapColor(MapColor.CRIMSON_HYPHAE)
                    .sound(SoundType.GLASS))
            .tag(AllTags.AllBlockTags.BUILDING_BLOCK.tag)
            .lang("Qliphoth counter")
            .simpleItem()
            .register();

    public static final BlockEntry<ContainmentControllerBlock> CONTAINMENT_CONTROLLER =
            REGISTRATE.block("containment_controller", ContainmentControllerBlock::new)
                    .initialProperties(() -> Blocks.GLASS_PANE)
                    .properties(p -> p.mapColor(MapColor.COLOR_BLACK)
                            .sound(SoundType.GLASS)
                            .noOcclusion())
                    .tag(AllTags.AllBlockTags.BUILDING_BLOCK.tag)
                    .lang("Containment controller")
                    .simpleItem()
                    .register();

    public static void register() {
    }
}
