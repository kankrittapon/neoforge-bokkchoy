package net.kankrittapon.rpgem.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlchemyTableScreen extends AbstractContainerScreen<AlchemyTableMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID,
            "textures/gui/alchemy_table.png");
    private static final ResourceLocation PROGRESS_TEXTURE = ResourceLocation
            .withDefaultNamespace("textures/gui/container/brewing_stand.png");

    public AlchemyTableScreen(AlchemyTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 240;
        this.imageHeight = 160;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 1000;
        this.inventoryLabelY = 1000;
        // Shift up to make room for inventory
        this.topPos = (this.height - 245) / 2; // 160 (GUI) + 85 (Inv) space
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = this.topPos;

        // Draw Background
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Draw Inventory Background (Dark Box)
        // Slots start at x+40, y+162
        int invX = x + 38;
        int invY = y + 160;
        int invW = 166;
        int invH = 86;
        guiGraphics.fill(invX, invY, invX + invW, invY + invH, 0xAA000000);

        // Render Progress Bar
        renderProgressBar(guiGraphics, x, y);
    }

    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            // Vanilla Brewing Stand Texture:
            // Bubbles are at u=185, v=29 (full 29 px height) in some versions, or 0..29.
            // Let's assume standard 1.21 texture layout where bubbles are at 185, 0
            // (filling down?) or 185, 29 (filling up?).
            // Actually, usually bubbles appear.
            // Let's try drawing the 12x29 bubble sprite.

            RenderSystem.setShaderColor(0.8F, 0.4F, 1.0F, 1.0F); // Purple Tint matches alchemy theme

            int barX = x + 95;
            int barY = y + 45; // Sits slightly lower, near bottle center

            int totalHeight = 29;
            int progress = (int) (menu.getScaledProgress()); // 0..50
            int scaledHeight = (progress * totalHeight) / 50;

            // Draw Bubbles (Filling Up)
            // Destination: barX, barY + (29 - scaled)
            // Source: u=185, v=29 - scaled
            // Width: 12
            // Height: scaled

            guiGraphics.blit(PROGRESS_TEXTURE,
                    barX, barY + (totalHeight - scaledHeight),
                    185, 29 - scaledHeight,
                    12, scaledHeight);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
