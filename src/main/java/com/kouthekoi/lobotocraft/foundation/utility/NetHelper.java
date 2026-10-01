package com.kouthekoi.lobotocraft.foundation.utility;

import com.kouthekoi.lobotocraft.content.containmentcontroller.ContainmentControllerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class NetHelper {
    public final class ClientHooks {
        public static void openControllerScreen(BlockPos pos) {
            Minecraft.getInstance().setScreen(new ContainmentControllerScreen(pos));
        }
    }
}
