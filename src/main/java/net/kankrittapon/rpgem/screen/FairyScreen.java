package net.kankrittapon.rpgem.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kankrittapon.rpgem.entity.custom.FairyEntity;
import net.kankrittapon.rpgem.menu.FairyMenu;
import net.kankrittapon.rpgem.network.PacketFairyAction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FairyScreen extends AbstractContainerScreen<FairyMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("rpgem",
            "textures/entity/fairy/fairy_gui.png");

    private float xMouse;
    private float yMouse;

    // Grid Size (1 Unit = 8 Pixels)
    private static final int S = 8;

    public FairyScreen(FairyMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 208; // 26 Units * 8px
        this.imageHeight = 208; // 26 Units * 8px
        this.titleLabelX = 10000;
        this.inventoryLabelX = 10000;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        // --- Action Buttons ---
        // Growth (Wine Icon) - Pos: 21, 3 (Grid) -> 168, 24
        this.addRenderableWidget(Button.builder(Component.literal("G"), button -> {
            PacketDistributor.sendToServer(
                    new PacketFairyAction(PacketFairyAction.ACTION_GROWTH));
        }).bounds(leftPos + 21 * S, topPos + 3 * S, 4 * S, 2 * S).build());

        // Change Skill (Orb Icon) - Pos: 22, 14 -> 176, 112
        this.addRenderableWidget(Button.builder(Component.literal("CS"), button -> {
            PacketDistributor.sendToServer(
                    new PacketFairyAction(PacketFairyAction.ACTION_CHANGE_SKILL));
        }).bounds(leftPos + 22 * S, topPos + 14 * S, 3 * S, 1 * S).build());

        // --- Footer Buttons (Row 24) ---
        // Release - Pos: 6, 24 (Grid) -> 48, 192 (11 units wide)
        this.addRenderableWidget(Button.builder(Component.literal("R"), button -> {
            PacketDistributor.sendToServer(
                    new PacketFairyAction(PacketFairyAction.ACTION_RELEASE));
        }).bounds(leftPos + 6 * S, topPos + 24 * S, 11 * S, 1 * S + 8).build());

        // Revive - Pos: 18, 24 -> 144, 192 (3 units wide)
        this.addRenderableWidget(Button.builder(Component.literal("REV"), button -> {
            PacketDistributor.sendToServer(
                    new PacketFairyAction(PacketFairyAction.ACTION_REVIVE));
        }).bounds(leftPos + 18 * S, topPos + 24 * S, 3 * S, 1 * S + 8).build());

        // Summon - Pos: 22, 24 -> 176, 192 (3 units wide)
        this.addRenderableWidget(Button.builder(Component.literal("SUM"), button -> {
            PacketDistributor.sendToServer(
                    new PacketFairyAction(PacketFairyAction.ACTION_UNSUMMON));
        }).bounds(leftPos + 22 * S, topPos + 24 * S, 3 * S, 1 * S + 8).build());

        // --- Conditional Buttons ---
        // Auto-Potion (Miraculous Cheer) - Pos: 5, 9 (Grid) -> 40, 72
        if (menu.fairy.hasSkill(FairyEntity.Skill.CHEER)) {
            this.addRenderableWidget(Button.builder(Component.literal("P"), b -> {
                PacketDistributor.sendToServer(
                        new PacketFairyAction(PacketFairyAction.ACTION_SETUP_POTION));
            }).bounds(leftPos + 5 * S, topPos + 9 * S, 6 * S, 1 * S + 8).build());
        }

        // Auto-Buff (Continuous Care) - Pos: 5, 12 (Grid) -> 40, 96
        if (menu.fairy.hasSkill(FairyEntity.Skill.CARE)) {
            this.addRenderableWidget(Button.builder(Component.literal("B"), b -> {
                // Open setup GUI logic
            }).bounds(leftPos + 5 * S, topPos + 12 * S, 6 * S, 1 * S + 8).build());
        }
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

        // Draw the main background (Top 208x208 part of 256x256 texture)
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        // Render Fairy Entity (Left Panel Area: 0, 2 -> 4, 25 Grid)
        // Box is 40px wide, centered at x=20. Anchor at bottom y=200px.
        if (this.menu.fairy != null) {
            renderEntity(
                    guiGraphics,
                    x + 2 * S + 4, y + 23 * S, // Centered in the 5-unit wide area
                    40, // Scale
                    (float) (x + 20) - xMouse,
                    (float) (y + 112) - yMouse,
                    this.menu.fairy);
        }

        // EXP Bar (5, 7 Grid) -> 40, 56
        if (this.menu.fairy != null) {
            // Placeholder: Green fill for now, should use texture blit from icons
            int barMax = 15 * S; // 120px
            int barWidth = (int) (barMax * (this.menu.fairy.getExp() / 100.0f));
            guiGraphics.fill(x + 5 * S, y + 7 * S, x + 5 * S + barWidth, y + 7 * S + 4, 0xFF00FF00);
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
        if (this.menu.fairy != null) {
            // Tier Label (5, 2 Grid) -> 40, 16
            guiGraphics.drawString(this.font, "Tier: " + this.menu.fairy.getTier(), 5 * S, 2 * S, 0xFFFFFFFF, false);

            // Name Label (5, 4 Grid) -> 40, 32
            guiGraphics.drawString(this.font, this.menu.fairy.getDisplayName(), 5 * S, 4 * S, 0xFFFFFF00, false);

            // Level Label (5, 6 Grid) -> 40, 48
            guiGraphics.drawString(this.font, "Lv." + this.menu.fairy.getLevel(), 5 * S, 6 * S, 0xFFFFFFFF, false);

            // EXP Text (18, 6 Grid) -> 144, 48
            guiGraphics.drawString(this.font, this.menu.fairy.getExp() + "%", 18 * S, 6 * S, 0xFFFFFFFF, false);
        } else {
            guiGraphics.drawString(this.font, "No Fairy Data", 110, 50, 0xFFFF0000, false);
        }
    }
}
