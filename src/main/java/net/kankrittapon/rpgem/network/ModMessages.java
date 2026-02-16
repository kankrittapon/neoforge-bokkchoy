package net.kankrittapon.rpgem.network;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModMessages {
        public static void register(final RegisterPayloadHandlersEvent event) {
                final PayloadRegistrar registrar = event.registrar(RPGEasyMode.MODID);

                registrar.playToServer(
                                PacketUpgradeItem.TYPE,
                                PacketUpgradeItem.STREAM_CODEC,
                                PacketUpgradeItem::handle);

                registrar.playToClient(
                                PacketSyncFailStack.TYPE,
                                PacketSyncFailStack.STREAM_CODEC,
                                PacketSyncFailStack::handle);

                registrar.playToClient(
                                PacketSyncWeight.TYPE,
                                PacketSyncWeight.STREAM_CODEC,
                                PacketSyncWeight::handle);

                registrar.playToServer(
                                PacketOpenFairyGUI.TYPE,
                                PacketOpenFairyGUI.STREAM_CODEC,
                                PacketOpenFairyGUI::handle);

                registrar.playToServer(
                                PacketFairyAction.TYPE,
                                PacketFairyAction.STREAM_CODEC,
                                PacketFairyAction::handle);
        }
}
