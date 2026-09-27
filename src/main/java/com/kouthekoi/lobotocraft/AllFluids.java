package com.kouthekoi.lobotocraft;

import com.kouthekoi.lobotocraft.foundation.data.LobotoRegistrate;

public class AllFluids {
    private static final LobotoRegistrate REGISTRATE = LobotoCraft.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }



    public static void register() {
    }
}

