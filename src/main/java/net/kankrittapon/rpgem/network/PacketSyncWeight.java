package net.kankrittapon.rpgem.network;

import io.netty.buffer.ByteBuf;
import net.kankrittapon.rpgem.attachment.WeightData;
import net.kankrittapon.rpgem.init.ModAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.kankrittapon.rpgem.RPGEasyMode;

public record PacketSyncWeight(float currentWeight, float maxWeight) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PacketSyncWeight> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "sync_weight"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSyncWeight> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, PacketSyncWeight::currentWeight,
            ByteBufCodecs.FLOAT, PacketSyncWeight::maxWeight,
            PacketSyncWeight::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketSyncWeight message, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Client side handling
            Player player = context.player();
            if (player != null) {
                WeightData data = player.getData(ModAttachments.WEIGHT_DATA);
                data.setCurrentWeight(message.currentWeight());
                data.setMaxWeight(message.maxWeight());
            }
        });
    }
}
