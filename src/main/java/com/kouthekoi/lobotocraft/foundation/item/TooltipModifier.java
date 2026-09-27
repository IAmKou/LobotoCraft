package com.kouthekoi.lobotocraft.foundation.item;

import com.kouthekoi.lobotocraft.api.registry.registrate.SimpleRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;

public interface TooltipModifier {
    SimpleRegistry<Item, TooltipModifier> REGISTRY = SimpleRegistry.create();

    TooltipModifier EMPTY = new TooltipModifier() {
        @Override
        public void modify(ItemTooltipEvent context) {
        }

        @Override
        public TooltipModifier andThen(TooltipModifier after) {
            return after;
        }
    };

    void modify(ItemTooltipEvent context);

    default TooltipModifier andThen(TooltipModifier after) {
        if (after == EMPTY) {
            return this;
        }
        return tooltip -> {
            modify(tooltip);
            after.modify(tooltip);
        };
    }

    static TooltipModifier mapNull(@Nullable TooltipModifier modifier) {
        if (modifier == null) {
            return EMPTY;
        }
        return modifier;
    }
}
