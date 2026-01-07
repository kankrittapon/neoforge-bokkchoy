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
    }

    @SubscribeEvent
    public static void registerScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
        event.register(net.kankrittapon.rpgem.init.ModMenuTypes.ALCHEMY_TABLE_MENU.get(),
                net.kankrittapon.rpgem.screen.AlchemyTableScreen::new);
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
                            return switch (first) {
                                case "H" -> 0xCC3333; // Red (Heart)
                                case "B" -> 0xF0F0F0; // White (Bone)
                                case "C" -> 0x33CC33; // Green (Emerald)
                                default -> -1;
                            };
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
}
