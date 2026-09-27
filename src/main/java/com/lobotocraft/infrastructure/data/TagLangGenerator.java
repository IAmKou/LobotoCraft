package com.lobotocraft.infrastructure.data;

import com.lobotocraft.AllTags.AllBlockTags;
import com.lobotocraft.AllTags.AllItemTags;
import com.lobotocraft.foundation.data.recipe.CommonMetal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Locale;
import java.util.function.BiConsumer;

public class TagLangGenerator {
    private final BiConsumer<String, String> output;

    public TagLangGenerator(BiConsumer<String, String> output) {
        this.output = output;
    }

    protected void translate(String key, String translation) {
        this.output.accept(key, translation);
    }

    protected void translate(TagKey<?> tag, String translation) {
        this.translate(keyFor(tag), translation);
    }

    private void translate(AllBlockTags tag, String translation) {
        this.translate(tag.tag, translation);
    }

    private void translate(AllItemTags tag, String translation) {
        this.translate(tag.tag, translation);
    }


    private void translate(AllBlockTags block, AllItemTags item, String translation) {
        this.translate(block, translation);
        this.translate(item, translation);
    }


    private void translate(CommonMetal.ItemLikeTag tags, String translated) {
        this.translate(tags.blocks(), translated);
        this.translate(tags.items(), translated);
    }

    public void generate() {
        // blocks and block items
        translate(AllBlockTags.NON_MOVABLE, "Non-movable");
        translate(AllBlockTags.NON_BREAKABLE, "Non-breakable");

        translate(AllItemTags.LOBOTO_INGOTS, "Loboto's Ingots");

        // metals
        for (CommonMetal metal : CommonMetal.values()) {
            String name = toWord(metal.name);

            if (metal.isNatural) {
                translate(metal.ores, name + " Ores");
                translate(metal.rawOres, "Raw " + name + " Ores");
                translate(metal.rawStorageBlocks, "Raw " + name + " Storage Blocks");
            }

            translate(metal.ingots, name + " Ingots");
            translate(metal.storageBlocks, name + " Storage Blocks");
            translate(metal.nuggets, name + " Nuggets");
            translate(metal.plates, name + " Plates");
        }
    }

    protected static String keyFor(TagKey<?> tag) {
        ResourceLocation registryId = tag.registry().location();
        String registry = sanitize(
                registryId.getNamespace().equals("minecraft") ? registryId.getPath() : registryId.toLanguageKey()
        );

        return "tag." + registry + '.' + sanitize(tag.location().toLanguageKey());
    }

    private static String sanitize(String string) {
        return string.replace('/', '.');
    }

    /**
     * Sets the first character to uppercase and all others to lowercase.
     */
    protected static String toWord(String string) {
        if (string.isBlank())
            return string;

        String lower = string.toLowerCase(Locale.ROOT);
        char first = Character.toUpperCase(lower.charAt(0));
        String rest = lower.substring(1);
        return first + rest;
    }
}
