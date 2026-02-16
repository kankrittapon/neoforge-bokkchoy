package net.kankrittapon.rpgem.event;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.client.ModKeyMappings;
import net.kankrittapon.rpgem.network.PacketOpenFairyGUI;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = RPGEasyMode.MODID, value = Dist.CLIENT)
public class ClientModEvents {

    @EventBusSubscriber(modid = RPGEasyMode.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(ModKeyMappings.OPEN_FAIRY_GUI_KEY);
        }

        @SubscribeEvent
        public static void onRegisterScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
            event.register(net.kankrittapon.rpgem.init.ModMenuTypes.ALCHEMY_TABLE_MENU.get(),
                    net.kankrittapon.rpgem.screen.AlchemyTableScreen::new);
            event.register(net.kankrittapon.rpgem.init.ModMenuTypes.ANCIENT_FORGE_MENU.get(),
                    net.kankrittapon.rpgem.screen.AncientForgeScreen::new);
            event.register(net.kankrittapon.rpgem.init.ModMenuTypes.FAIRY_MENU.get(),
                    net.kankrittapon.rpgem.screen.FairyScreen::new);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ModKeyMappings.OPEN_FAIRY_GUI_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new PacketOpenFairyGUI());
        }
    }
}
