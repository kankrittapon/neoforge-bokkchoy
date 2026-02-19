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

    // --- GUI POSITIONS (Exported from Designer) ---
    private static final int BG_WIDTH = 256;
    private static final int BG_HEIGHT = 256;

    // Bars
    private static final int HP_BAR_X = 37;
    private static final int HP_BAR_Y = 68;
    private static final int HP_BAR_W = 13;
    private static final int HP_BAR_H = 105;

    private static final int EXP_BAR_X = 63;
    private static final int EXP_BAR_Y = 59;
    private static final int EXP_BAR_W = 12;
    private static final int EXP_BAR_H = 115;

    // Buttons
    private static final int BUTTON_GROWTH_X = 10;
    private static final int BUTTON_GROWTH_Y = 70;
    private static final int BUTTON_GROWTH_W = 10;
    private static final int BUTTON_GROWTH_H = 113;

    private static final int BUTTON_RELEASE_X = 61;
    private static final int BUTTON_RELEASE_Y = 238;
    private static final int BUTTON_RELEASE_W = 50;
    private static final int BUTTON_RELEASE_H = 10;

    private static final int BUTTON_SUMMON_X = 150;
    private static final int BUTTON_SUMMON_Y = 238;
    private static final int BUTTON_SUMMON_W = 50;
    private static final int BUTTON_SUMMON_H = 10;

    private static final int BUTTON_SKILL_X = 119;
    private static final int BUTTON_SKILL_Y = 234;
    private static final int BUTTON_SKILL_W = 18;
    private static final int BUTTON_SKILL_H = 18;

    // Model placeholder (Need to pick a good spot, maybe center-ish)
    private static final int FAIRY_MODEL_X = 128; // Center of 256
    private static final int FAIRY_MODEL_Y = 160;

    private float xMouse;
    private float yMouse;

    public FairyScreen(FairyMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = BG_WIDTH;
        this.imageHeight = BG_HEIGHT;
        this.titleLabelX = 10000;
        this.inventoryLabelX = 10000;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        // Growth (B2)
        this.addRenderableWidget(Button.builder(Component.literal(""), button -> {
            PacketDistributor.sendToServer(new PacketFairyAction(PacketFairyAction.ACTION_GROWTH));
        }).bounds(leftPos + BUTTON_GROWTH_X, topPos + BUTTON_GROWTH_Y, BUTTON_GROWTH_W, BUTTON_GROWTH_H)
                .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("เติบโต/วิวัฒนา")))
                .build());

        // Change Skill (B13)
        this.addRenderableWidget(Button.builder(Component.literal(""), button -> {
            PacketDistributor.sendToServer(new PacketFairyAction(PacketFairyAction.ACTION_CHANGE_SKILL));
        }).bounds(leftPos + BUTTON_SKILL_X, topPos + BUTTON_SKILL_Y, BUTTON_SKILL_W, BUTTON_SKILL_H)
                .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("เปลี่ยนทักษะ")))
                .build());

        // Release (B3)
        this.addRenderableWidget(Button.builder(Component.literal(""), button -> {
            PacketDistributor.sendToServer(new PacketFairyAction(PacketFairyAction.ACTION_RELEASE));
        }).bounds(leftPos + BUTTON_RELEASE_X, topPos + BUTTON_RELEASE_Y, BUTTON_RELEASE_W, BUTTON_RELEASE_H)
                .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("ปล่อย"))).build());

        // Summon/Unsummon (B4)
        this.addRenderableWidget(Button.builder(Component.literal(""), button -> {
            PacketDistributor.sendToServer(new PacketFairyAction(PacketFairyAction.ACTION_UNSUMMON));
        }).bounds(leftPos + BUTTON_SUMMON_X, topPos + BUTTON_SUMMON_Y, BUTTON_SUMMON_W, BUTTON_SUMMON_H)
                .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("อัญเชิญ"))).build());
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

        // Draw the main background (Full 256x256)
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        // Render Fairy Entity
        if (this.menu.fairy != null) {
            renderEntity(
                    guiGraphics,
                    x + FAIRY_MODEL_X, y + FAIRY_MODEL_Y,
                    40,
                    (float) (x + FAIRY_MODEL_X) - xMouse,
                    (float) (y + FAIRY_MODEL_Y - 50) - yMouse,
                    this.menu.fairy);
        }

        // B0: HP Bar
        if (this.menu.fairy != null) {
            float hpProgress = this.menu.fairy.getHealth() / this.menu.fairy.getMaxHealth();
            int hpHeight = (int) (HP_BAR_H * hpProgress);
            // Draw HP from Bottom Up
            guiGraphics.fill(x + HP_BAR_X, y + HP_BAR_Y + (HP_BAR_H - hpHeight), x + HP_BAR_X + HP_BAR_W,
                    y + HP_BAR_Y + HP_BAR_H, 0xFFFF0000);
        }

        // B1: EXP Bar
        if (this.menu.fairy != null) {
            float expProgress = (float) this.menu.fairy.getExp()
                    / this.menu.fairy.getMaxExpForLevel(this.menu.fairy.getLevel());
            int expHeight = (int) (EXP_BAR_H * expProgress);
            // Draw EXP from Bottom Up
            guiGraphics.fill(x + EXP_BAR_X, y + EXP_BAR_Y + (EXP_BAR_H - expHeight), x + EXP_BAR_X + EXP_BAR_W,
                    y + EXP_BAR_Y + EXP_BAR_H, 0xFF00FFF2);
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

        // NeoForge 1.21.1 method name might differ, checking standard
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
            // Tier Label
            guiGraphics.drawString(this.font, "Tier: " + this.menu.fairy.getTier(), 40, 16, 0xFFFFFFFF, false);

            // Name Label
            guiGraphics.drawString(this.font, this.menu.fairy.getDisplayName(), 40, 32, 0xFFFFFF00, false);

            // Level Label
            guiGraphics.drawString(this.font, "Lv." + this.menu.fairy.getLevel(), 40, 48, 0xFFFFFFFF, false);

            // EXP Text (Right aligned)
            String expText = this.menu.fairy.getExp() + " / "
                    + this.menu.fairy.getMaxExpForLevel(this.menu.fairy.getLevel());
            guiGraphics.drawString(this.font, expText, 160 - this.font.width(expText), 48, 0xFFFFFFFF, false);
        } else {
            guiGraphics.drawString(this.font, "No Fairy Data", 80, 50, 0xFFFF0000, false);
        }
    }
}
