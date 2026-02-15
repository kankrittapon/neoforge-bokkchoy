package net.kankrittapon.rpgem.client.gui;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = RPGEasyMode.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class RPGOverlay {

        @SubscribeEvent
        public static void onRenderGui(RenderGuiEvent.Post event) {
                if (!net.kankrittapon.rpgem.ClientConfig.INSTANCE.HUD_ENABLED.get())
                        return;

                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.player == null || minecraft.options.hideGui) {
                        return;
                }

                GuiGraphics guiGraphics = event.getGuiGraphics();

                int width = minecraft.getWindow().getGuiScaledWidth();
                int height = minecraft.getWindow().getGuiScaledHeight();

                // Dynamic position from Config
                int xOffset = net.kankrittapon.rpgem.ClientConfig.INSTANCE.HUD_X_OFFSET.get();
                int yOffset = net.kankrittapon.rpgem.ClientConfig.INSTANCE.HUD_Y_OFFSET.get();

                int x = width - xOffset;
                int y = height - yOffset;
                int lineHeight = 10;

                net.minecraft.world.entity.player.Player player = minecraft.player;

                // Helper to render stats (rendered from bottom up)
                y = renderStat(guiGraphics, minecraft.font, player, net.kankrittapon.rpgem.init.ModAttributes.EVASION,
                                "⚡ Evasion", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player,
                                net.kankrittapon.rpgem.init.ModAttributes.ARMOR_PENETRATION,
                                "🛡️ Armor Pen", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player,
                                net.kankrittapon.rpgem.init.ModAttributes.CRIT_CHANCE,
                                "💥 Crit Chance", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player,
                                net.kankrittapon.rpgem.init.ModAttributes.LIFE_STEAL,
                                "🩸 Life Steal", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player, net.kankrittapon.rpgem.init.ModAttributes.ANTI_HEAL,
                                "☠️ Anti-Heal", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player, net.kankrittapon.rpgem.init.ModAttributes.ACCURACY,
                                "🎯 Accuracy", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player,
                                net.kankrittapon.rpgem.init.ModAttributes.SEAL_RESIST,
                                "🧿 Seal Resist", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player,
                                net.kankrittapon.rpgem.init.ModAttributes.REFLECT_RESIST,
                                "🪞 Reflect Res", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player,
                                net.kankrittapon.rpgem.init.ModAttributes.DAMAGE_REDUCTION,
                                "🛡️ Dmg Reduct", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player,
                                net.kankrittapon.rpgem.init.ModAttributes.ELEMENT_DAMAGE,
                                "🔥 Ele Dmg", x, y, lineHeight);
                y = renderStat(guiGraphics, minecraft.font, player,
                                net.kankrittapon.rpgem.init.ModAttributes.REFLECT_CHANCE,
                                "💢 Reflect %", x, y, lineHeight);
        }

        private static int renderStat(GuiGraphics guiGraphics, net.minecraft.client.gui.Font font,
                        net.minecraft.world.entity.player.Player player,
                        net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
                        String label,
                        int x, int y, int lineHeight) {
                double value = player.getAttributeValue(attribute);
                if (value > 0.001) { // Only show if value is significant (> 0.1%)
                        String text = String.format("%s: %.1f%%", label, value * 100);
                        int textWidth = font.width(text);

                        // Draw text aligned to right
                        // Draw shadow for visibility
                        guiGraphics.drawString(font, text, x - textWidth, y - lineHeight, 0xFFFFFF, true);

                        return y - lineHeight; // Move up for next line
                }
                return y;
        }
}
