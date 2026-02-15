package net.kankrittapon.rpgem.network;

import io.netty.buffer.ByteBuf;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.init.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSyncFailStack(int failStack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PacketSyncFailStack> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "sync_fail_stack"));

    public static final StreamCodec<ByteBuf, PacketSyncFailStack> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PacketSyncFailStack::failStack,
            PacketSyncFailStack::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final PacketSyncFailStack message, final IPayloadContext context) {
        context.enqueueWork(() -> {
            // Client Side Handling
            if (context.flow().isClientbound()) {
                Minecraft.getInstance().player.setData(ModAttachments.FAIL_STACK, message.failStack());
            }
        });
    }
}
