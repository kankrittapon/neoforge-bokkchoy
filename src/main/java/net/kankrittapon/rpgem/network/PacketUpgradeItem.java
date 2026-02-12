package net.kankrittapon.rpgem.network;

import io.netty.buffer.ByteBuf;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.block.entity.AncientForgeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketUpgradeItem(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PacketUpgradeItem> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "upgrade_item"));

    public static final StreamCodec<ByteBuf, PacketUpgradeItem> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PacketUpgradeItem::pos,
            PacketUpgradeItem::new);

    public static void handle(final PacketUpgradeItem payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level() instanceof Level level) {
                // Validate distance
                if (context.player().distanceToSqr(payload.pos.getX() + 0.5, payload.pos.getY() + 0.5,
                        payload.pos.getZ() + 0.5) > 64) {
                    return;
                }

                BlockEntity blockEntity = level.getBlockEntity(payload.pos);
                if (blockEntity instanceof AncientForgeBlockEntity forge) {
                    forge.tryUpgradeItem(level, context.player());
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
