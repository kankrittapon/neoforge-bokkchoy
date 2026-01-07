package net.kankrittapon.rpgem.screen;

import net.kankrittapon.rpgem.block.AlchemyTableBlock;
import net.kankrittapon.rpgem.block.entity.AlchemyTableBlockEntity;
import net.kankrittapon.rpgem.init.ModBlocks;
import net.kankrittapon.rpgem.init.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AlchemyTableMenu extends AbstractContainerMenu {
    public final AlchemyTableBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final ContainerData data;

    public AlchemyTableMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public AlchemyTableMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.ALCHEMY_TABLE_MENU.get(), containerId);
        checkContainerSize(inv, 5); // 5 slots total
        blockEntity = ((AlchemyTableBlockEntity) entity);
        this.levelAccess = ContainerLevelAccess.create(inv.player.level(), entity.getBlockPos());
        this.data = data;

        // Slot 0 (Input) @ [42, 70] (Shifted for art alignment)
        addSlot(new SlotItemHandler(blockEntity.itemHandler, 0, 42, 70));

        // Slot 1 (Top Ingredient) @ [192, 45] (Shifted X)
        addSlot(new SlotItemHandler(blockEntity.itemHandler, 1, 192, 45));

        // Slot 2 (Mid Ingredient) @ [192, 75]
        addSlot(new SlotItemHandler(blockEntity.itemHandler, 2, 192, 75));

        // Slot 3 (Bot Ingredient) @ [192, 105]
        addSlot(new SlotItemHandler(blockEntity.itemHandler, 3, 192, 105));

        // Slot 4 (Result) @ [120, 110] (Shifted Y for bottle belly)
        addSlot(new SlotItemHandler(blockEntity.itemHandler, 4, 120, 110));

        addDataSlots(data);

        // Player Inventory
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1); // Max Progress
        int progressArrowSize = 50; // Requested Height

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 5;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,
                    false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
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
        return stillValid(ContainerLevelAccess.create(player.level(), blockEntity.getBlockPos()),
                player, ModBlocks.ALCHEMY_TABLE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 40 + l * 18, 162 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 40 + i * 18, 220));
        }
    }
}
