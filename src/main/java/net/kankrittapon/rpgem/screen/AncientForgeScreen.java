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
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft",
            "textures/gui/container/dispenser.png");

    // --- GUI POSITIONS ---
    private static final int BG_WIDTH = 176;
    private static final int BG_HEIGHT = 166;

    private static final int BUTTON_UPGRADE_X = 60;
    private static final int BUTTON_UPGRADE_Y = 75;
    private static final int BUTTON_UPGRADE_W = 56;
    private static final int BUTTON_UPGRADE_H = 20;

    private static final int TEXT_MODE_X = 8;
    private static final int TEXT_MODE_Y = 20;
    private static final int TEXT_FS_X = 28;
    private static final int TEXT_FS_Y = 63;

    public AncientForgeScreen(AncientForgeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = BG_WIDTH;
        this.imageHeight = BG_HEIGHT;
    }

    private net.minecraft.client.gui.components.Button upgradeButton;

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 10000;
        this.inventoryLabelX = 10000;

        this.upgradeButton = this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(Component.literal("Upgrade"), button -> {
                    net.neoforged.neoforge.network.PacketDistributor.sendToServer(
                            new net.kankrittapon.rpgem.network.PacketUpgradeItem(this.menu.blockEntity.getBlockPos()));
                }).bounds(leftPos + BUTTON_UPGRADE_X, topPos + BUTTON_UPGRADE_Y, BUTTON_UPGRADE_W, BUTTON_UPGRADE_H)
                        .build());
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (this.menu.getSlot(1).getItem().is(net.kankrittapon.rpgem.init.ModItems.MEMORY_FRAGMENT.get())) {
            this.upgradeButton.setMessage(Component.literal("Repair"));
        } else {
            this.upgradeButton.setMessage(Component.literal("Upgrade"));
        }
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

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        // Mode
        boolean isRepair = this.menu.getSlot(1).getItem()
                .is(net.kankrittapon.rpgem.init.ModItems.MEMORY_FRAGMENT.get());
        String modeText = isRepair ? "REPAIR" : "UPGRADE";
        int modeColor = isRepair ? 0x55FF55 : 0xFFAA00;
        guiGraphics.drawString(this.font, modeText, TEXT_MODE_X, TEXT_MODE_Y, modeColor, false);

        // Fail Stack
        int failStack = 0;
        if (this.minecraft != null && this.minecraft.player != null) {
            failStack = this.minecraft.player.getData(net.kankrittapon.rpgem.init.ModAttachments.FAIL_STACK);
        }
        guiGraphics.drawString(this.font, "FS: " + failStack, TEXT_FS_X, TEXT_FS_Y, 0xFF5555, false);
    }
}
