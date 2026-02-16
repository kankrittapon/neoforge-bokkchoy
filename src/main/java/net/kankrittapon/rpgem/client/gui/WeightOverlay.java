package net.kankrittapon.rpgem.client.gui;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.attachment.WeightData;
import net.kankrittapon.rpgem.init.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = RPGEasyMode.MODID, value = Dist.CLIENT)
public class WeightOverlay {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        // Only render if HUD is enabled and player exists
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.isSpectator())
            return;

        // Render in Inventory Screen? Plan said "Overlay UI: แถบน้ำหนักปัจจุบันในหน้าจอ
        // Inventory"
        // If it means "Inside Inventory Screen", we should use
        // ContainerScreenEvent.Render.Foreground/Background
        // If it means "HUD", use RenderGuiEvent.
        // Task 122: "Overlay UI: แถบน้ำหนักปัจจุบันในหน้าจอ Inventory"
        // Usually "Inventory Screen" implies inside the container GUI.
        // But "Overlay" usually implies HUD.
        // Let's implement BOTH or check user intent.
        // "Weight Bar in Inventory Screen" -> sounds like BDO where weight is shown in
        // the invo.

        // Changing strategy: Hook into ContainerScreenEvent.Render.Foreground
        // But let's verify if user meant HUD (always visible) or Inventory Only.
        // "หน้าจอ Inventory" = "Inventory Screen".
    }

    @SubscribeEvent
    public static void onRenderContainerScreen(
            net.neoforged.neoforge.client.event.ContainerScreenEvent.Render.Foreground event) {
        if (event.getContainerScreen() instanceof net.minecraft.client.gui.screens.inventory.InventoryScreen ||
                event.getContainerScreen() instanceof net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen) {

            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null)
                return;

            WeightData data = mc.player.getData(ModAttachments.WEIGHT_DATA);
            float current = data.getCurrentWeight();
            float max = data.getMaxWeight();
            if (max <= 0)
                max = 100;

            // Position: Bottom Right of the specific GUI window? Or fixed?
            // Inventory Screen usually puts it near the player model or slots.

            GuiGraphics guiGraphics = event.getGuiGraphics();
            int x = 120; // Generic relative offset
            int y = 60;

            // Drawing Logic...
            // Let's use a simple text for now: "Weight: 50.5 / 100.0 LT"
            String text = String.format("%.1f / %.1f LT", current, max);
            int color = 0xFFFFFF;

            if (current > max * 2.0)
                color = 0xFF0000; // Red
            else if (current > max * 1.5)
                color = 0xFFA500; // Orange
            else if (current > max)
                color = 0xFFFF00; // Yellow

            // Draw Text
            // Note: event.getMouseX/Y are passed, but we need relative coordinates to the
            // container.
            // event.getGuiGraphics().drawString...
            // ContainerScreen has 'leftPos' and 'topPos'.
            // We can access them via reflection or access transformer?
            // Or assumes (0,0) is top-left of the SCREEN, not the GUI.
            // Wait, Render.Foreground usually translates matrix to GUI top-left?
            // "This event is fired after the container screen has rendered its background
            // and foreground layers."
            // It might be in screen coordinates or gui coordinates.
            // Documentation says: "The matrix stack is pushed to the top left of the GUI."

            // Let's verify by drawing at 0,0
            guiGraphics.drawString(mc.font, text, 100, 10, color, false);
        }
    }
}
