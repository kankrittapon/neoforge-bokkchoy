package net.kankrittapon.rpgem.block.entity;

import net.kankrittapon.rpgem.init.ModBlockEntities;
import net.kankrittapon.rpgem.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.kankrittapon.rpgem.menu.AlchemyTableMenu;

public class AlchemyTableBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() == ModItems.ETHERNAL_BOTTLE.get() ||
                        stack.getItem() == ModItems.INFINITE_POTION_TIER_1.get() ||
                        stack.getItem() == ModItems.INFINITE_POTION_TIER_2.get();
                case 1, 2, 3 -> stack.getItem() == ModItems.ZOMBIE_HEART.get() ||
                        stack.getItem() == ModItems.BONE_OF_MAZE.get() ||
                        stack.getItem() == ModItems.COSMIC_EMERALD.get();
                case 4 -> false; // Output slot
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;

    public AlchemyTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ALCHEMY_TABLE_BE.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AlchemyTableBlockEntity.this.progress;
                    case 1 -> AlchemyTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AlchemyTableBlockEntity.this.progress = value;
                    case 1 -> AlchemyTableBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        net.minecraft.world.Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("alchemy_table.progress", progress);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("alchemy_table.progress");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        // Optimize: Check recipe only every 10 ticks or if we are already crafting
        boolean isCrafting = progress > 0;
        boolean shouldCheckRecipe = level.getGameTime() % 10 == 0 || !isCrafting;

        if (shouldCheckRecipe) {
            if (hasRecipe()) {
                increaseCraftingProgress();
                setChanged(level, pos, state);

                if (hasProgressFinished()) {
                    craftItem();
                    resetProgress();
                }
            } else {
                resetProgress();
            }
        } else if (isCrafting) {
            // Already confirmed recipe exists, just increment progress
            increaseCraftingProgress();
            setChanged(level, pos, state);
            if (hasProgressFinished()) {
                // Double check before finishing
                if (hasRecipe()) {
                    craftItem();
                }
                resetProgress();
            }
        }
    }

    private boolean hasRecipe() {
        ItemStack resultSlot = itemHandler.getStackInSlot(4);
        if (resultSlot.getCount() >= resultSlot.getMaxStackSize()) {
            return false; // Result slot full
        }

        ItemStack input = itemHandler.getStackInSlot(0);
        if (input.isEmpty())
            return false;

        List<ItemStack> ingredients = List.of(
                itemHandler.getStackInSlot(1),
                itemHandler.getStackInSlot(2),
                itemHandler.getStackInSlot(3));

        // Check if any ingredient slots are occupied
        boolean hasIngredients = false;
        for (ItemStack ing : ingredients) {
            if (!ing.isEmpty()) {
                hasIngredients = true;
                break;
            }
        }
        if (!hasIngredients)
            return false;

        CraftingContext ctx = getCraftingContext(input, ingredients);
        if (ctx != null) {
            this.maxProgress = ctx.processTime;
            // Check if result slot is valid for output
            return resultSlot.isEmpty()
                    || (resultSlot.getItem() == ctx.outputItem && resultSlot.getCount() < resultSlot.getMaxStackSize());
        }

        return false;
    }

    private void craftItem() {
        ItemStack input = itemHandler.getStackInSlot(0);
        List<ItemStack> ingredients = List.of(
                itemHandler.getStackInSlot(1),
                itemHandler.getStackInSlot(2),
                itemHandler.getStackInSlot(3));

        CraftingContext ctx = getCraftingContext(input, ingredients);
        if (ctx != null) {
            String ingredientName = getIngredientName(ctx.ingredientUsed);

            // Consume Input
            input.shrink(1);

            // Consume Ingredient
            ctx.ingredientUsed.shrink(1);

            // Produce Result
            ItemStack resultStack = itemHandler.getStackInSlot(4);
            if (resultStack.isEmpty()) {
                ItemStack newResult = new ItemStack(ctx.outputItem);
                applyUsedIngredients(newResult, ctx.previousUsed, ingredientName);
                itemHandler.setStackInSlot(4, newResult);
            } else {
                // Should technically invoke same logic for checking NBT but assuming slot 4 is
                // cleared or compatible
                resultStack.grow(1);
            }
        }
    }

    private void applyUsedIngredients(ItemStack stack, List<String> previousUsed, String newIngredient) {
        List<String> used = new ArrayList<>(previousUsed);
        used.add(newIngredient);

        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (String s : used) {
            listTag.add(StringTag.valueOf(s));
        }
        tag.put("IngredientHistory", listTag);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private String getIngredientName(ItemStack stack) {
        if (stack.is(ModItems.ZOMBIE_HEART.get()))
            return "H";
        if (stack.is(ModItems.BONE_OF_MAZE.get()))
            return "B";
        if (stack.is(ModItems.COSMIC_EMERALD.get()))
            return "C";
        return stack.getItem().toString();
    }

    private record CraftingContext(Item outputItem, int processTime, List<String> previousUsed,
            ItemStack ingredientUsed) {
    }

    private CraftingContext getCraftingContext(ItemStack input, List<ItemStack> availableIngredients) {
        if (input.isEmpty())
            return null;

        List<String> usedIngredients = new ArrayList<>();
        if (input.has(DataComponents.CUSTOM_DATA)) {
            CustomData customData = input.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag = customData.copyTag();
            if (tag.contains("IngredientHistory")) {
                ListTag list = tag.getList("IngredientHistory", 8); // 8 = String
                for (int i = 0; i < list.size(); i++) {
                    usedIngredients.add(list.getString(i));
                }
            }
        }

        // TIER DETERMINATION
        Item output = null;
        int time = 0;

        if (input.is(ModItems.ETHERNAL_BOTTLE.get())) {
            output = ModItems.INFINITE_POTION_TIER_1.get();
            time = 200;
        } else if (input.is(ModItems.INFINITE_POTION_TIER_1.get())) {
            output = ModItems.INFINITE_POTION_TIER_2.get();
            time = 300;
        } else if (input.is(ModItems.INFINITE_POTION_TIER_2.get())) {
            output = ModItems.INFINITE_POTION_TIER_3.get();
            time = 400;
        } else {
            return null;
        }

        // Find FIRST valid unique ingredient
        for (ItemStack ing : availableIngredients) {
            if (!ing.isEmpty()) {
                String name = getIngredientName(ing);
                if (!usedIngredients.contains(name)) {
                    // Found valid ingredient
                    return new CraftingContext(output, time, usedIngredients, ing);
                }
            }
        }

        return null;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.rpgem.alchemy_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory,
            net.minecraft.world.entity.player.Player player) {
        return new AlchemyTableMenu(containerId, playerInventory, this, this.data);
    }
}
