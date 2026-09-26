package com.lobotocraft.foundation.data.recipe;

import com.lobotocraft.LobotoCraft;
import com.lobotocraft.api.data.recipe.DatagenMod;

import java.util.function.Consumer;

public enum Mods implements DatagenMod {
    VANILLA("minecraft"),
    LOBOTO(LobotoCraft.ID);
    private final String id;

    private boolean reversedMetalPrefix;
    private boolean strippedIsSuffix;
    private boolean omitWoodSuffix;

    private Mods(String id) {
        this(id, b -> {
        });
    }

    private Mods(String id, Consumer<Builder> props) {
        props.accept(new Builder());
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean reversedMetalPrefix() {
        return reversedMetalPrefix;
    }

    @Override
    public boolean strippedIsSuffix() {
        return strippedIsSuffix;
    }

    @Override
    public boolean omitWoodSuffix() {
        return omitWoodSuffix;
    }

    class Builder {

        Builder reverseMetalPrefix() {
            reversedMetalPrefix = true;
            return this;
        }

        Builder strippedWoodIsSuffix() {
            strippedIsSuffix = true;
            return this;
        }

        Builder omitWoodSuffix() {
            omitWoodSuffix = true;
            return this;
        }

    }


}
