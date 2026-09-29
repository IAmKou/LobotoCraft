package com.kouthekoi.lobotocraft.foundation.data.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public final class LobotoRecipeProvider extends RecipeProvider {

    public LobotoRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
    }


    protected static class I {

        static TagKey<Item> redstone() {
            return Tags.Items.DUSTS_REDSTONE;
        }

        static TagKey<Item> planks() {
            return ItemTags.PLANKS;
        }

        static TagKey<Item> woodSlab() {
            return ItemTags.WOODEN_SLABS;
        }

        static TagKey<Item> gold() {
            return Tags.Items.INGOTS_GOLD;
        }

        static TagKey<Item> goldSheet() {
            return CommonMetal.GOLD.plates;
        }

        static TagKey<Item> stone() {
            return Tags.Items.STONES;
        }

        static TagKey<Item> iron() {
            return Tags.Items.INGOTS_IRON;
        }

        static TagKey<Item> ironNugget() {
            return Tags.Items.NUGGETS_IRON;
        }

        static TagKey<Item> leads() {
            return CommonMetal.LEAD.ingots;
        }

        static TagKey<Item> ironSheet() {
            return CommonMetal.IRON.plates;
        }

        static TagKey<Item> leadBlock() {
            return CommonMetal.LEAD.storageBlocks.items();
        }

        static Ingredient netherite() {
            return Ingredient.of(Tags.Items.INGOTS_NETHERITE);
        }

    }
}
