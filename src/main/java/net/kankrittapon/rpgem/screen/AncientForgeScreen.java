package net.kankrittapon.rpgem.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.menu.AncientForgeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AncientForgeScreen extends AbstractContainerScreen<AncientForgeMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID,
            "textures/gui/alchemy_table.png"); // Placeholder for now

    public AncientForgeScreen(AncientForgeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 10000; // Hide default title
        this.inventoryLabelX = 10000; // Hide default inventory title

        this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Upgrade"), button -> {
                    net.neoforged.neoforge.network.PacketDistributor.sendToServer(
                            new net.kankrittapon.rpgem.network.PacketUpgradeItem(this.menu.blockEntity.getBlockPos()));
                }).bounds(leftPos + 60, topPos + 75, 56, 20).build());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
