package com.kouthekoi.lobotocraft;

import com.kouthekoi.lobotocraft.content.containmentcontroller.ContainmentControllerBlockEntity;
import com.kouthekoi.lobotocraft.content.qliphothcounter.QliphothCounterBlockEntity;
import com.kouthekoi.lobotocraft.content.qliphothcounter.QliphothCounterRenderer;
import com.kouthekoi.lobotocraft.foundation.data.LobotoRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;


public class AllBlocksEntity {
    private static final LobotoRegistrate REGISTRATE = LobotoCraft.registrate();

    public static final BlockEntityEntry<ContainmentControllerBlockEntity> CONTAINMENT_CONTROLLER =
            REGISTRATE.blockEntity("containment_controller", ContainmentControllerBlockEntity::new)
                    .validBlocks(AllBlocks.CONTAINMENT_CONTROLLER)
                    .register();

    public static final BlockEntityEntry<QliphothCounterBlockEntity> QLIPHOTH_COUNTER =
            REGISTRATE.blockEntity("qliphoth_counter", QliphothCounterBlockEntity::new)
                    .validBlocks(AllBlocks.QLIPHOTH_COUNTER)
                    .renderer(() -> QliphothCounterRenderer::new)
                    .register();
    public static void register() {
    }
}
