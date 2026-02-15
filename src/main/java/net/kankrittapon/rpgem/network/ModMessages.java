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
    }
}
