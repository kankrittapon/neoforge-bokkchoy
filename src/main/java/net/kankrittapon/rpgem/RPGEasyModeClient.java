package net.kankrittapon.rpgem;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = RPGEasyMode.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods
// in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = RPGEasyMode.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RPGEasyModeClient {
    public RPGEasyModeClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your
        // mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json
        // file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        RPGEasyMode.LOGGER.info("HELLO FROM CLIENT SETUP");
        RPGEasyMode.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        event.enqueueWork(() -> {
            net.kankrittapon.rpgem.client.ModItemProperties.addCustomItemProperties();
        });
    }

    @SubscribeEvent
    public static void registerScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
        event.register(net.kankrittapon.rpgem.init.ModMenuTypes.ALCHEMY_TABLE_MENU.get(),
                net.kankrittapon.rpgem.screen.AlchemyTableScreen::new);
        event.register(net.kankrittapon.rpgem.init.ModMenuTypes.ANCIENT_FORGE_MENU.get(),
                net.kankrittapon.rpgem.screen.AncientForgeScreen::new);
    }

    @SubscribeEvent
    public static void registerItemColors(net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex == 0) {
                if (stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)) {
                    net.minecraft.nbt.CompoundTag tag = stack
                            .get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).copyTag();
                    if (tag.contains("IngredientHistory")) {
                        net.minecraft.nbt.ListTag list = tag.getList("IngredientHistory", 8);
                        if (!list.isEmpty()) {
                            String first = list.getString(0);

                            // Tier 1 Logic
                            if (list.size() == 1) {
                                return switch (first) {
                                    case "H" -> 0xCC3333; // Red
                                    case "B" -> 0xF0F0F0; // White
                                    case "C" -> 0x33CC33; // Green
                                    default -> -1;
                                };
                            }

                            // Tier 2 Logic
                            if (list.size() == 2) {
                                String second = list.getString(1);
                                String combo = first + second;
                                // Check for existence of specific types regardless of order if needed,
                                // but here we check specific combinations as per ModItemProperties logic
                                // However, the item logic uses specific combos (HB, HC, BH, BC, etc.)
                                // But visual request was grouped.

                                if (combo.contains("H") && combo.contains("B"))
                                    return 0xFF69B4; // Pink (HB/BH)
                                if (combo.contains("H") && combo.contains("C"))
                                    return 0xFFFF00; // Yellow (HC/CH)
                                if (combo.contains("B") && combo.contains("C"))
                                    return 0x00FFFF; // Cyan (BC/CB)
                                return -1;
                            }

                            // Tier 3 Logic
                            if (list.size() >= 3) {
                                return 0x800080; // Purple/Cosmic
                            }
                        }
                    }
                }
            }
            return -1;
        },
                net.kankrittapon.rpgem.init.ModItems.INFINITE_POTION_TIER_1.get(),
                net.kankrittapon.rpgem.init.ModItems.INFINITE_POTION_TIER_2.get(),
                net.kankrittapon.rpgem.init.ModItems.INFINITE_POTION_TIER_3.get());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(
            net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(net.kankrittapon.rpgem.init.ModEntities.ZOMBIE_KING.get(),
                net.minecraft.client.renderer.entity.ZombieRenderer::new);
        event.registerEntityRenderer(net.kankrittapon.rpgem.init.ModEntities.SKELETON_LORD.get(),
                net.minecraft.client.renderer.entity.SkeletonRenderer::new);
    }
}
