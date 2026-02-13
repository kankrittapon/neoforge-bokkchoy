package net.kankrittapon.rpgem.block.entity;

import net.kankrittapon.rpgem.Config;
import net.kankrittapon.rpgem.init.ModBlockEntities;
import net.kankrittapon.rpgem.init.ModDataComponents;
import net.kankrittapon.rpgem.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;

public class AncientForgeBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected final ContainerData data;

    public AncientForgeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ANCIENT_FORGE_BE.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.rpgem.ancient_forge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new net.kankrittapon.rpgem.menu.AncientForgeMenu(containerId, playerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
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

    // ========== Upgrade Logic ==========

    /**
     * Determines if an item is an armor piece.
     */
    private boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof net.minecraft.world.item.ArmorItem;
    }

    /**
     * Determines if an item is a weapon (sword, axe, etc.)
     */
    private boolean isWeapon(ItemStack stack) {
        return stack.getItem() instanceof net.minecraft.world.item.SwordItem
                || stack.getItem() instanceof net.minecraft.world.item.AxeItem;
    }

    /**
     * Returns the Forged Stone type: "fortitude", "agility", "destruction", or ""
     * if not a forged stone.
     * Works for both normal and ultimate variants.
     */
    private String getForgedStoneType(ItemStack stone) {
        if (stone.is(ModItems.FORGED_STONE_FORTITUDE.get()) || stone.is(ModItems.FORGED_STONE_ULTIMATE_FORTITUDE.get()))
            return "fortitude";
        if (stone.is(ModItems.FORGED_STONE_AGILITY.get()) || stone.is(ModItems.FORGED_STONE_ULTIMATE_AGILITY.get()))
            return "agility";
        if (stone.is(ModItems.FORGED_STONE_DESTRUCTION.get())
                || stone.is(ModItems.FORGED_STONE_ULTIMATE_DESTRUCTION.get()))
            return "destruction";
        return "";
    }

    /**
     * Returns true if the forged stone is an Ultimate variant.
     */
    private boolean isUltimateForgedStone(ItemStack stone) {
        return stone.is(ModItems.FORGED_STONE_ULTIMATE_FORTITUDE.get())
                || stone.is(ModItems.FORGED_STONE_ULTIMATE_AGILITY.get())
                || stone.is(ModItems.FORGED_STONE_ULTIMATE_DESTRUCTION.get());
    }

    public void tryUpgradeItem(Level level, Player player) {
        ItemStack equipment = this.itemHandler.getStackInSlot(0);
        ItemStack stone = this.itemHandler.getStackInSlot(1);

        if (equipment.isEmpty() || stone.isEmpty())
            return;

        int currentLevel = equipment.getOrDefault(ModDataComponents.UPGRADE_LEVEL.get(), 0);
        int nextLevel = currentLevel + 1;

        // ===== Tier Determination =====
        int tier = 0;
        boolean isForgedStone = false;
        String forgedType = "";

        if (stone.is(ModItems.UPGRADE_STONE_TIER_1.get()))
            tier = 1;
        else if (stone.is(ModItems.UPGRADE_STONE_TIER_2.get()))
            tier = 2;
        else if (stone.is(ModItems.UPGRADE_STONE_TIER_3.get()))
            tier = 3;
        else {
            // Check Forged Stones
            forgedType = getForgedStoneType(stone);
            if (!forgedType.isEmpty()) {
                isForgedStone = true;
                tier = isUltimateForgedStone(stone) ? 3 : 2;
            }
        }

        if (tier == 0) {
            player.sendSystemMessage(Component.literal("§cInvalid upgrade material!"));
            return;
        }

        // ===== Weapon/Armor Type Validation for Forged Stones =====
        if (isForgedStone) {
            boolean equipIsArmor = isArmor(equipment);
            boolean equipIsWeapon = isWeapon(equipment);

            if (forgedType.equals("destruction") && !equipIsWeapon) {
                player.sendSystemMessage(Component.literal("§cForged Stone: Destruction can only be used on weapons!"));
                return;
            }
            if ((forgedType.equals("fortitude") || forgedType.equals("agility")) && !equipIsArmor) {
                player.sendSystemMessage(Component.literal("§cThis Forged Stone can only be used on armor!"));
                return;
            }

            // ===== Armor Path Validation =====
            if (equipIsArmor) {
                String currentPath = equipment.getOrDefault(ModDataComponents.ARMOR_PATH.get(), "none");

                if (currentPath.equals("none")) {
                    // First Forged Stone → set the path
                    String newPath = forgedType.equals("fortitude") ? "reduction" : "evasion";
                    equipment.set(ModDataComponents.ARMOR_PATH.get(), newPath);
                    player.sendSystemMessage(Component.literal(
                            "§6⚔ Armor path set: "
                                    + (newPath.equals("reduction") ? "§b🧱 Damage Reduction" : "§a💨 Damage Evasion")));
                } else {
                    // Path already set → validate match
                    String expectedPath = forgedType.equals("fortitude") ? "reduction" : "evasion";
                    if (!currentPath.equals(expectedPath)) {
                        player.sendSystemMessage(Component.literal(
                                "§cThis armor is on the "
                                        + (currentPath.equals("reduction") ? "🧱 Damage Reduction"
                                                : "💨 Damage Evasion")
                                        + " path! Use the matching Forged Stone."));
                        return;
                    }
                }
            }
        }

        // ===== Level Range Validation =====
        if (tier == 1 && currentLevel >= 15) {
            player.sendSystemMessage(Component.literal("§cThis item needs a Tier 2 stone or Forged Stone!"));
            return;
        }
        if (tier == 2 && (currentLevel < 15 || currentLevel >= 25)) {
            player.sendSystemMessage(Component.literal(
                    currentLevel < 15 ? "§cItem level too low for Tier 2!"
                            : "§cItem needs a Tier 3 / Ultimate stone!"));
            return;
        }
        if (tier == 3 && currentLevel < 25) {
            player.sendSystemMessage(Component.literal("§cItem level too low for Tier 3!"));
            return;
        }
        if (tier == 3 && currentLevel >= 28) { // Max level
            player.sendSystemMessage(Component.literal("§6Item is already at Max Level!"));
            return;
        }

        // ===== RNG Calculation =====
        double successRate = 0.0;
        if (tier == 1)
            successRate = Config.UPGRADE_SUCCESS_RATE_TIER_1.get();
        else if (tier == 2)
            successRate = Config.UPGRADE_SUCCESS_RATE_TIER_2.get();
        else if (tier == 3)
            successRate = Config.UPGRADE_SUCCESS_RATE_TIER_3.get();

        boolean success = level.random.nextDouble() < successRate;

        // Consume Stone
        stone.shrink(1);

        if (success) {
            equipment.set(ModDataComponents.UPGRADE_LEVEL.get(), nextLevel);
            player.sendSystemMessage(Component.literal(
                    "§a✦ Upgrade Successful! New Level: " + getFormattedLevel(nextLevel)));
            level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ANVIL_USE,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
        } else {
            player.sendSystemMessage(Component.literal("§c✦ Upgrade Failed!"));
            level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ANVIL_BREAK,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);

            // Downgrade Logic (Tier 2 & 3 only): 50% chance to lose 1 level
            if (tier > 1 && level.random.nextBoolean()) {
                int floorLevel = tier == 2 ? 15 : 25;
                int newLevel = Math.max(floorLevel, currentLevel - 1);
                equipment.set(ModDataComponents.UPGRADE_LEVEL.get(), newLevel);
                player.sendSystemMessage(Component.literal(
                        "§4Ouch! Item level decreased to: " + getFormattedLevel(newLevel)));
            }
        }
    }

    private String getFormattedLevel(int level) {
        if (level <= 15)
            return "+" + level;
        if (level <= 25)
            return romanWithOffset(level - 15);
        return "Ultimate " + (level - 25);
    }

    private String romanWithOffset(int val) {
        String[] romans = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
        if (val > 0 && val <= 10)
            return romans[val - 1];
        return String.valueOf(val);
    }

}
