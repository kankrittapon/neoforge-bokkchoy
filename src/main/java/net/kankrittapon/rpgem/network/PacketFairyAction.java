package net.kankrittapon.rpgem.network;

import io.netty.buffer.ByteBuf;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.init.ModItems;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public record PacketFairyAction(int actionType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PacketFairyAction> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID, "fairy_action"));

    public static final StreamCodec<ByteBuf, PacketFairyAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PacketFairyAction::actionType,
            PacketFairyAction::new);

    public static final int ACTION_SPROUT = 0;
    public static final int ACTION_GROWTH = 1;
    public static final int ACTION_TOGGLE_AUTO_BUFF = 2;
    public static final int ACTION_RELEASE = 3;
    public static final int ACTION_REVIVE = 4; // Not used yet? Tear?
    public static final int ACTION_UNSUMMON = 5;
    public static final int ACTION_CHANGE_SKILL = 6;

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketFairyAction message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                net.minecraft.server.level.ServerLevel level = serverPlayer.serverLevel();

                // 1. Find Fairy (Entity or Item)
                net.kankrittapon.rpgem.entity.custom.FairyEntity foundFairy = null;
                ItemStack foundItem = ItemStack.EMPTY;

                // Check Entity
                for (net.minecraft.world.entity.Entity entity : level.getAllEntities()) {
                    if (entity instanceof net.kankrittapon.rpgem.entity.custom.FairyEntity fairy) {
                        if (fairy.getOwnerUUID().isPresent()
                                && fairy.getOwnerUUID().get().equals(serverPlayer.getUUID()) && fairy.isAlive()) {
                            foundFairy = fairy;
                            break;
                        }
                    }
                }

                // Check Item if no Entity
                if (foundFairy == null) {
                    // Check Curios
                    final java.util.concurrent.atomic.AtomicReference<ItemStack> itemRef = new java.util.concurrent.atomic.AtomicReference<>(
                            ItemStack.EMPTY);
                    top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(serverPlayer).ifPresent(handler -> {
                        handler.findFirstCurio(ModItems.FAIRY_WING.get())
                                .ifPresent(slotResult -> itemRef.set(slotResult.stack()));
                    });
                    if (itemRef.get().isEmpty()) {
                        // Check Inventory
                        for (int i = 0; i < serverPlayer.getInventory().getContainerSize(); i++) {
                            if (serverPlayer.getInventory().getItem(i).getItem() == ModItems.FAIRY_WING.get()) {
                                itemRef.set(serverPlayer.getInventory().getItem(i));
                                break;
                            }
                        }
                    }
                    foundItem = itemRef.get();
                }

                if (foundFairy == null && foundItem.isEmpty()) {
                    return; // No fairy found
                }

                // 2. Perform Action
                switch (message.actionType) {
                    case ACTION_SPROUT:
                        handleSprout(serverPlayer, foundFairy, foundItem);
                        break;
                    case ACTION_GROWTH:
                        handleGrowth(serverPlayer, foundFairy, foundItem); // Feeding logic
                        break;
                    case ACTION_UNSUMMON:
                        if (foundFairy != null) {
                            foundFairy.discard(); // Unsummon
                            // Might need to give item back or update item cooldown?
                            // Item handles recall usually.
                            serverPlayer.sendSystemMessage(net.minecraft.network.chat.Component
                                    .translatable("message.rpgem.fairy_unsummoned"));
                        }
                        break;
                    // TODO: Other actions
                }
            }
        });
    }

    private static void handleSprout(net.minecraft.server.level.ServerPlayer player,
            net.kankrittapon.rpgem.entity.custom.FairyEntity fairy, ItemStack item) {
        // Logic: Check materials -> Sprout (Materials checks TODO: Config/Inventory)
        if (fairy != null) {
            if (fairy.canSprout()) {
                fairy.sprout();
            }
        } else if (!item.isEmpty()) {
            // Item Mode
            CompoundTag originalTag = item.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                    net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
            net.kankrittapon.rpgem.entity.custom.FairyEntity dummy = net.kankrittapon.rpgem.entity.custom.FairyEntity
                    .createDummy(player.serverLevel(), originalTag);

            if (dummy.canSprout()) {
                dummy.sprout();
                // Save back
                CompoundTag tag = new CompoundTag();
                dummy.addAdditionalSaveData(tag);
                // Preserve Inventory if dummy didn't load it correctly or if
                // addAdditionalSaveData overwrites it
                // FairyEntity.addAdditionalSaveData DOES save inventory.
                item.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                        net.minecraft.world.item.component.CustomData.of(tag));

                // Send feedback (Sprout logic inside dummy.sprout() sends message if owner
                // present,
                // but dummy might not have owner set if we didn't set it)
                // FairyEntity.createDummy reads NBT. If NBT has Owner, it sets it.
                // We should ensure dummy has owner.
                if (!dummy.getOwnerUUID().isPresent()) {
                    dummy.setOwnerUUID(player.getUUID());
                }
            }
        }
    }

    private static void handleGrowth(net.minecraft.server.level.ServerPlayer player,
            net.kankrittapon.rpgem.entity.custom.FairyEntity fairy, ItemStack item) {
        if (player.containerMenu instanceof net.kankrittapon.rpgem.menu.FairyMenu menu) {
            ItemStack food = menu.getSlot(2).getItem(); // Slot index 2 is Growth Input
            if (!food.isEmpty()) {
                boolean success = false;

                if (fairy != null) {
                    success = fairy.feed(food);
                } else if (!item.isEmpty()) {
                    CompoundTag originalTag = item.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                            net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
                    net.kankrittapon.rpgem.entity.custom.FairyEntity dummy = net.kankrittapon.rpgem.entity.custom.FairyEntity
                            .createDummy(player.serverLevel(), originalTag);

                    success = dummy.feed(food);

                    if (success) {
                        CompoundTag tag = new CompoundTag();
                        dummy.addAdditionalSaveData(tag);
                        item.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                                net.minecraft.world.item.component.CustomData.of(tag));
                    }
                }

                if (success) {
                    // Feeding successful, item stack shrunk by feed()
                    // But we are modifying the stack in the SLOT.
                    // FairyEntity.feed() shrinks the stack passed to it.
                    // Since we passed 'food' which is menu.getSlot(2).getItem(), modifying 'food'
                    // modifies the stack.
                    // We need to notify the slot that it changed?
                    menu.getSlot(2).setChanged();
                    // Or send packets to client to update inventory?
                    // Validating slot changes usually automatic in Container.
                    player.containerMenu.broadcastChanges();
                }
            }
        }
    }
}
