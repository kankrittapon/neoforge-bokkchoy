package net.kankrittapon.rpgem.menu;

import net.kankrittapon.rpgem.init.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FairyMenu extends AbstractContainerMenu {
    // We can use a simple ItemStackHandler for now, or sync with Entity
    public final ItemStackHandler inventory;

    public final net.kankrittapon.rpgem.entity.custom.FairyEntity fairy;

    public FairyMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, new ItemStackHandler(6),
                createEntityFromPacket(inv.player.level(), extraData));
    }

    private static net.kankrittapon.rpgem.entity.custom.FairyEntity createEntityFromPacket(
            net.minecraft.world.level.Level level, FriendlyByteBuf data) {
        int entityId = data.readInt();
        if (entityId == -1) {
            // Item Mode: Create Dummy Entity
            net.minecraft.nbt.CompoundTag tag = data.readNbt();
            net.kankrittapon.rpgem.entity.custom.FairyEntity dummy = new net.kankrittapon.rpgem.entity.custom.FairyEntity(
                    net.kankrittapon.rpgem.init.ModEntities.FAIRY.get(), level);
            if (tag != null)
                dummy.readAdditionalSaveData(tag);
            return dummy;
        }
        return (net.kankrittapon.rpgem.entity.custom.FairyEntity) level.getEntity(entityId);
    }

    public FairyMenu(int containerId, Inventory inv, ItemStackHandler handler,
            net.kankrittapon.rpgem.entity.custom.FairyEntity entity) {
        super(ModMenuTypes.FAIRY_MENU.get(), containerId);
        this.inventory = handler;
        this.fairy = entity;
        // Don't check container size if dummy, or ensure dummy has inventory
        if (entity != null && entity.getId() != -1) {
            checkContainerSize(inv, 6);
        }

        // Slot 0: HP Potion (Center-Right in GUI)
        // Adjusted relative to guiLeft/Top. Let's assume placement around x=140, y=80
        // based on flow
        this.addSlot(new SlotItemHandler(handler, 0, 150, 90));

        // Slot 1: MP Potion (Next to HP)
        this.addSlot(new SlotItemHandler(handler, 1, 170, 90));

        // Slot 2: Growth/Sprout Input
        this.addSlot(new SlotItemHandler(handler, 2, 200, 70));

        // Slots 3-5: Hide
        for (int i = 0; i < 3; i++) {
            this.addSlot(new SlotItemHandler(handler, 3 + i, -1000, -1000));
        }

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + 36;
    private static final int TE_INVENTORY_SLOT_COUNT = 6;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem())
            return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < TE_INVENTORY_FIRST_SLOT_INDEX) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX,
                    false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // Virtual inventory for now
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public ItemStack boundItem = ItemStack.EMPTY;

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide && !boundItem.isEmpty() && fairy != null) {
            net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
            fairy.addAdditionalSaveData(tag);
            boundItem.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                    net.minecraft.world.item.component.CustomData.of(tag));
        }
    }
}
