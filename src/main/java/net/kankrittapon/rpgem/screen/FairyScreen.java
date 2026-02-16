package net.kankrittapon.rpgem.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kankrittapon.rpgem.menu.FairyMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FairyScreen extends AbstractContainerScreen<FairyMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("rpgem",
            "textures/gui/container/fairy_gui.png");

    private float xMouse;
    private float yMouse;

    public FairyScreen(FairyMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 280;
        this.imageHeight = 200;
        this.titleLabelX = 10000;
        this.inventoryLabelX = 10000;
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // --- Top Right Buttons ---
        // Sprout (Vong Jorn Tid Peek)
        this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Sprout"), button -> {
                    net.neoforged.neoforge.network.PacketDistributor
                            .sendToServer(new net.kankrittapon.rpgem.network.PacketFairyAction(
                                    net.kankrittapon.rpgem.network.PacketFairyAction.ACTION_SPROUT));
                }).bounds(x + 200, y + 20, 70, 20).build());

        // Growth (Terb Toh)
        this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Growth"), button -> {
                    net.neoforged.neoforge.network.PacketDistributor
                            .sendToServer(new net.kankrittapon.rpgem.network.PacketFairyAction(
                                    net.kankrittapon.rpgem.network.PacketFairyAction.ACTION_GROWTH));
                }).bounds(x + 200, y + 45, 70, 20).build());

        // --- Middle Buttons ---
        // Auto-Potion Settings (Tung Kha Nam Ya)
        this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Auto Potion"), button -> {
                    net.neoforged.neoforge.network.PacketDistributor
                            .sendToServer(new net.kankrittapon.rpgem.network.PacketFairyAction(
                                    net.kankrittapon.rpgem.network.PacketFairyAction.ACTION_TOGGLE_AUTO_BUFF));
                }).bounds(x + 200, y + 85, 70, 20).build());

        // Change Skill (Plien Tuk Sa)
        this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Change Skill"), button -> {
                    net.neoforged.neoforge.network.PacketDistributor
                            .sendToServer(new net.kankrittapon.rpgem.network.PacketFairyAction(
                                    net.kankrittapon.rpgem.network.PacketFairyAction.ACTION_CHANGE_SKILL));
                }).bounds(x + 200, y + 120, 70, 20).build());

        // --- Bottom Right Buttons ---
        // Release (Ploy)
        this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Release"), button -> {
                    net.neoforged.neoforge.network.PacketDistributor
                            .sendToServer(new net.kankrittapon.rpgem.network.PacketFairyAction(
                                    net.kankrittapon.rpgem.network.PacketFairyAction.ACTION_RELEASE));
                    this.onClose();
                }).bounds(x + 120, y + 175, 50, 20).build());

        // Revive (Kuen Cheep)
        this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Revive"), button -> {
                    net.neoforged.neoforge.network.PacketDistributor
                            .sendToServer(new net.kankrittapon.rpgem.network.PacketFairyAction(
                                    net.kankrittapon.rpgem.network.PacketFairyAction.ACTION_REVIVE));
                }).bounds(x + 175, y + 175, 50, 20).build());

        // Unsummon (Yok Lerk)
        this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Recall"), button -> {
                    net.neoforged.neoforge.network.PacketDistributor
                            .sendToServer(new net.kankrittapon.rpgem.network.PacketFairyAction(
                                    net.kankrittapon.rpgem.network.PacketFairyAction.ACTION_UNSUMMON));
                    this.onClose();
                }).bounds(x + 230, y + 175, 50, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        this.xMouse = mouseX;
        this.yMouse = mouseY;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Render Fairy Entity
        if (this.menu.fairy != null) {
            renderEntity(
                    guiGraphics,
                    x + 52, y + 150,
                    30,
                    (float) (x + 52) - xMouse,
                    (float) (y + 150 - 50) - yMouse,
                    this.menu.fairy);
        }
    }

    public static void renderEntity(GuiGraphics guiGraphics, int x, int y, int scale, float mouseX, float mouseY,
            net.minecraft.world.entity.LivingEntity entity) {
        float f = (float) Math.atan((double) (mouseX / 40.0F));
        float f1 = (float) Math.atan((double) (mouseY / 40.0F));
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(f1 * 20.0F * ((float) Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        float f2 = entity.yBodyRot;
        float f3 = entity.getYRot();
        float f4 = entity.getXRot();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0F + f * 20.0F;
        entity.setYRot(180.0F + f * 40.0F);
        entity.setXRot(-f1 * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventory(guiGraphics, (float) x,
                (float) y, (float) scale, new Vector3f(), quaternionf, quaternionf1, entity);
        entity.yBodyRot = f2;
        entity.setYRot(f3);
        entity.setXRot(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        // Draw Title
        guiGraphics.drawString(this.font, "Fairy Info", 8, 6, 0xFFFFFFFF, false);

        if (this.menu.fairy != null) {
            // Stats (Right Side)
            int dataX = 110;
            // Tier
            guiGraphics.drawString(this.font, "Tier: " + this.menu.fairy.getTier(), dataX, 25, 0xFFFFFFFF, false);
            // Name (Yellow)
            guiGraphics.drawString(this.font, this.menu.fairy.getDisplayName(), dataX, 35, 0xFFFFFF00, false);
            // Level & XP
            guiGraphics.drawString(this.font,
                    "Lv." + this.menu.fairy.getLevel() + "  XP: " + this.menu.fairy.getExp() + "%", dataX, 45,
                    0xFFFFFFFF, false);

            // Description Placeholder
            guiGraphics.drawString(this.font, "Radiant Fairy...", dataX, 60, 0xFFAAAAAA, false);

            // Potion Slot Labels
            guiGraphics.drawString(this.font, "HP", 145, 95, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, "MP", 175, 95, 0xFFFFFFFF, false);

            // Skills List Placeholders
            // guiGraphics.drawString(this.font, "Skill 1: [Locked]", dataX, 110,
            // 0xFFAAAAAA, false);
        } else {
            guiGraphics.drawString(this.font, "No Fairy Data", 110, 50, 0xFFFF0000, false);
        }
    }
}
