package net.kankrittapon.rpgem.network;

import io.netty.buffer.ByteBuf;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketOpenFairyGUI() implements CustomPacketPayload {
    public PacketOpenFairyGUI() {
    }

    public static final CustomPacketPayload.Type<PacketOpenFairyGUI> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "open_fairy_gui"));

    public static final StreamCodec<ByteBuf, PacketOpenFairyGUI> STREAM_CODEC = StreamCodec
            .unit(new PacketOpenFairyGUI());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketOpenFairyGUI message, IPayloadContext context) {
        context.enqueueWork(() -> {
            net.minecraft.world.entity.player.Player player = context.player();
            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                net.minecraft.server.level.ServerLevel level = serverPlayer.serverLevel();

                // Find Fairy owned by player
                net.kankrittapon.rpgem.entity.custom.FairyEntity foundFairy = null;

                for (net.minecraft.world.entity.Entity entity : level.getAllEntities()) {
                    if (entity instanceof net.kankrittapon.rpgem.entity.custom.FairyEntity fairy) {
                        if (fairy.getOwnerUUID().isPresent()
                                && fairy.getOwnerUUID().get().equals(serverPlayer.getUUID())) {
                            foundFairy = fairy;
                            break;
                        }
                    }
                }

                if (foundFairy != null && foundFairy.isAlive()) {
                    final net.kankrittapon.rpgem.entity.custom.FairyEntity finalFairy = foundFairy;
                    serverPlayer.openMenu(foundFairy, buf -> {
                        buf.writeInt(finalFairy.getId());
                    });
                } else {
                    // Try to find Fairy Item in Curios or Inventory
                    java.util.concurrent.atomic.AtomicReference<net.minecraft.world.item.ItemStack> fairyStack = new java.util.concurrent.atomic.AtomicReference<>(
                            net.minecraft.world.item.ItemStack.EMPTY);

                    // Check Curios first
                    // Check Curios first
                    top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(serverPlayer).ifPresent(handler -> {
                        var curios = handler.getCurios();
                        for (var entry : curios.entrySet()) {
                            var stacksHandler = entry.getValue();
                            var itemHandler = stacksHandler.getStacks();
                            for (int i = 0; i < itemHandler.getSlots(); i++) {
                                if (itemHandler.getStackInSlot(i)
                                        .getItem() == net.kankrittapon.rpgem.init.ModItems.FAIRY_WING.get()) {
                                    fairyStack.set(itemHandler.getStackInSlot(i));
                                    return;
                                }
                            }
                        }
                    });

                    // Fallback to Main Inventory if not found in Curios (optional as per design,
                    // but good UX)
                    if (fairyStack.get().isEmpty()) {
                        for (int i = 0; i < serverPlayer.getInventory().getContainerSize(); i++) {
                            net.minecraft.world.item.ItemStack stack = serverPlayer.getInventory().getItem(i);
                            if (stack.getItem() == net.kankrittapon.rpgem.init.ModItems.FAIRY_WING.get()) {
                                fairyStack.set(stack);
                                break;
                            }
                        }
                    }

                    if (!fairyStack.get().isEmpty()) {
                        // Open Menu with Item NBT
                        net.minecraft.world.item.ItemStack stack = fairyStack.get();
                        net.minecraft.nbt.CompoundTag tag = stack
                                .getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                                        net.minecraft.world.item.component.CustomData.EMPTY)
                                .copyTag();

                        if (!tag.contains("Inventory")) {
                            // Initialize empty inventory if missing
                            net.neoforged.neoforge.items.ItemStackHandler tempHandler = new net.neoforged.neoforge.items.ItemStackHandler(
                                    13);
                            tag.put("Inventory", tempHandler.serializeNBT(serverPlayer.registryAccess()));
                        }

                        final net.minecraft.nbt.CompoundTag finalTag = tag;
                        final net.minecraft.world.item.ItemStack finalStack = stack;

                        serverPlayer.openMenu(new net.minecraft.world.SimpleMenuProvider(
                                (id, inv, p) -> {
                                    net.kankrittapon.rpgem.menu.FairyMenu menu = new net.kankrittapon.rpgem.menu.FairyMenu(
                                            id, inv, new net.neoforged.neoforge.items.ItemStackHandler(13),
                                            net.kankrittapon.rpgem.entity.custom.FairyEntity.createDummy(p.level(),
                                                    finalTag));
                                    menu.boundItem = finalStack;
                                    return menu;
                                },
                                net.minecraft.network.chat.Component.translatable("gui.rpgem.fairy_info")), buf -> {
                                    buf.writeInt(-1); // ID -1
                                    buf.writeNbt(finalTag);
                                });
                    } else {
                        serverPlayer.sendSystemMessage(
                                net.minecraft.network.chat.Component.translatable("message.rpgem.fairy_not_found_gui"));
                    }
                }
            }
        });
    }
}
