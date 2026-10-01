package com.kouthekoi.lobotocraft.content.containmentcontroller;

import com.kouthekoi.lobotocraft.foundation.networking.ControllerActionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class ContainmentControllerScreen extends Screen {

    private enum Tab { ROOM, IMPLANT }

    private final BlockPos pos;
    private Tab tab = Tab.ROOM;
    private boolean awaitingAssemble = false;

    private Button roomTabButton;
    private Button implantTabButton;
    private Button assembleButton;
    private Button disassembleButton;

    public ContainmentControllerScreen(BlockPos pos) {
        super(Component.literal("Containment Controller"));
        this.pos = pos;
    }

    @Override
    protected void init() {
        int cx = width / 2;
        int top = height / 2 - 50;

        roomTabButton = addRenderableWidget(Button.builder(Component.literal("Room"),
                b -> setTab(Tab.ROOM)).bounds(cx - 100, top - 30, 98, 20).build());

        implantTabButton = addRenderableWidget(Button.builder(Component.literal("Implant"),
                b -> setTab(Tab.IMPLANT)).bounds(cx + 2, top - 30, 98, 20).build());

        int y = height / 2 + 10;

        assembleButton = addRenderableWidget(Button.builder(Component.literal("Assemble"),
                b -> send(true)).bounds(cx - 100, y, 95, 20).build());

        disassembleButton = addRenderableWidget(Button.builder(Component.literal("Disassemble"),
                b -> send(false)).bounds(cx + 5, y, 95, 20).build());

        updateWidgets();
    }

    private void setTab(Tab newTab) {
        tab = newTab;
        updateWidgets();
    }

    private void send(boolean assemble) {
        awaitingAssemble = assemble;
        PacketDistributor.sendToServer(new ControllerActionPacket(pos, assemble));
    }

    private ContainmentControllerBlockEntity controller() {
        Level level = Minecraft.getInstance().level;
        return level != null && level.getBlockEntity(pos) instanceof ContainmentControllerBlockEntity c ? c : null;
    }

    /** Syncs button states and visibility with the controller's current state. */
    private void updateWidgets() {
        ContainmentControllerBlockEntity c = controller();
        boolean active = c != null && c.isActive();

        if (!active && tab == Tab.IMPLANT) tab = Tab.ROOM;   // room was disassembled or broken

        roomTabButton.active = tab != Tab.ROOM;
        implantTabButton.active = active && tab != Tab.IMPLANT;

        boolean onRoomTab = tab == Tab.ROOM;
        assembleButton.visible = onRoomTab;
        disassembleButton.visible = onRoomTab;
        assembleButton.active = !active;
        disassembleButton.active = active;
    }

    @Override
    public void tick() {
        ContainmentControllerBlockEntity c = controller();
        if (c == null) {            // block was removed
            onClose();
            return;
        }

        if (awaitingAssemble && c.isActive()) {   // assemble succeeded: jump to the new tab
            awaitingAssemble = false;
            tab = Tab.IMPLANT;
        }
        updateWidgets();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);   // draws background + buttons

        ContainmentControllerBlockEntity c = controller();
        int cx = width / 2;
        int top = height / 2 - 50;

        g.drawCenteredString(font, title, cx, top, 0xFFFFFF);

        if (tab == Tab.ROOM) {
            if (c != null) {
                g.drawCenteredString(font, c.isActive() ? "Assembled" : "Not assembled",
                        cx, top + 20, c.isActive() ? 0x55FF55 : 0xFF5555);
                g.drawCenteredString(font, c.getValidationReason(), cx, top + 36, 0xAAAAAA);
            }
        } else {
            g.drawCenteredString(font, "Implant next", cx, top + 40, 0xFFFFFF);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

