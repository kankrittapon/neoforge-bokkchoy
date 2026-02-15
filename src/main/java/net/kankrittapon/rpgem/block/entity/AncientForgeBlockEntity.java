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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;

public class AncientForgeBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(3) {
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
                return 0; // No internal data sync needed, handled by Player Capability
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
        if (tag.contains("inventory")) {
            CompoundTag invTag = tag.getCompound("inventory");
            // Force size to 3 to support new slots (Protection Stone)
            invTag.putInt("Size", 3);
            itemHandler.deserializeNBT(registries, invTag);
        }
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

    public void handleForgeAction(Level level, Player player) {
        ItemStack stone = this.itemHandler.getStackInSlot(1);
        if (stone.is(ModItems.MEMORY_FRAGMENT.get())) {
            performRepair(level, player);
        } else {
            performUpgrade(level, player);
        }
    }

    private void performRepair(Level level, Player player) {
        ItemStack equipment = this.itemHandler.getStackInSlot(0);
        ItemStack material = this.itemHandler.getStackInSlot(1);
        ItemStack supportItem = this.itemHandler.getStackInSlot(2);

        if (equipment.isEmpty() || material.isEmpty())
            return;

        if (!equipment.isDamageableItem()) {
            player.sendSystemMessage(Component.literal("§cThis item cannot be repaired!"));
            return;
        }

        if (equipment.getDamageValue() == 0) {
            player.sendSystemMessage(Component.literal("§aItem is already at full durability!"));
            return;
        }

        int repairAmount = 1;
        int upgradeLevel = equipment.getOrDefault(ModDataComponents.UPGRADE_LEVEL.get(), 0);

        if (upgradeLevel > 0) {
            repairAmount = 1;
        } else {
            net.minecraft.world.item.Rarity rarity = equipment.getRarity();
            if (rarity == net.minecraft.world.item.Rarity.COMMON)
                repairAmount = 10;
            else if (rarity == net.minecraft.world.item.Rarity.UNCOMMON)
                repairAmount = 5;
            else if (rarity == net.minecraft.world.item.Rarity.RARE)
                repairAmount = 2;
            else
                repairAmount = 1;
        }

        // Artisan's Memory Boost (x5)
        boolean useArtisan = false;
        if (!supportItem.isEmpty() && supportItem.is(ModItems.ARTISANS_MEMORY.get())) {
            repairAmount *= 5;
            useArtisan = true;
        }

        // Apply Repair
        int newDamage = Math.max(0, equipment.getDamageValue() - repairAmount);
        equipment.setDamageValue(newDamage);

        // Consume Material
        material.shrink(1);
        if (useArtisan) {
            supportItem.shrink(1);
            player.sendSystemMessage(Component.literal("§6[Artisan's Memory] Repair Boost Activated! (x5)"));
        }

        // Feedback
        level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ANVIL_USE,
                net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
        player.sendSystemMessage(Component.literal("§aItem repaired! (+" + repairAmount + " Durability)"));
    }

    private void performUpgrade(Level level, Player player) {
        ItemStack equipment = this.itemHandler.getStackInSlot(0);
        ItemStack stone = this.itemHandler.getStackInSlot(1);
        ItemStack supportItem = this.itemHandler.getStackInSlot(2);

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

        // ===== Durability Validation (New) =====
        if (equipment.isDamageableItem()) {
            int maxDamage = equipment.getMaxDamage();
            int currentDamage = equipment.getDamageValue();
            int durability = maxDamage - currentDamage;

            // Levels 1-15: Cannot upgrade if Durability <= 20
            if (currentLevel <= 15 && durability <= 20) {
                player.sendSystemMessage(Component.literal("§cDurability too low! Repair required (>20)."));
                player.playNotifySound(net.minecraft.sounds.SoundEvents.ANVIL_HIT,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
            }
            // Levels 16+: Cannot upgrade if Broken (Durability <= 0)
            // Note: Minecraft breaks item at durability 0 effectively, but if we have
            // custom logic keeping it alive:
            if (currentLevel > 15 && durability <= 0) {
                player.sendSystemMessage(Component.literal("§cItem is broken! Repair required."));
                player.playNotifySound(net.minecraft.sounds.SoundEvents.ANVIL_HIT,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
                return;
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
        if (tier == 3 && currentLevel >= 28) {
            player.sendSystemMessage(Component.literal("§6Item is already at Max Level!"));
            return;
        }

        // ===== Fail Stack & Success Rate =====
        double baseChance = 0.0;
        if (tier == 1)
            baseChance = Config.UPGRADE_SUCCESS_RATE_TIER_1.get();
        else if (tier == 2)
            baseChance = Config.UPGRADE_SUCCESS_RATE_TIER_2.get();
        else if (tier == 3)
            baseChance = Config.UPGRADE_SUCCESS_RATE_TIER_3.get();

        // Get Fail Stack from Player
        int failStack = player.getData(net.kankrittapon.rpgem.init.ModAttachments.FAIL_STACK);
        double failStackBonus = failStack * 0.01; // 1% per stack
        double successRate = baseChance + failStackBonus;

        // Cap success rate at 100%
        if (successRate > 1.0)
            successRate = 1.0;

        // Display Probability
        player.displayClientMessage(Component.literal("§eChance: " + (int) (successRate * 100) + "% (Base: "
                + (int) (baseChance * 100) + "% + FS: " + failStack + ")"), true);

        boolean success = level.random.nextDouble() < successRate;

        // Consume Stone
        stone.shrink(1);

        if (success) {
            // SUCCESS
            equipment.set(ModDataComponents.UPGRADE_LEVEL.get(), nextLevel);
            updateItemName(equipment, nextLevel);

            // Validate/Set Armor Path if needed (from original code)
            if (isForgedStone && isArmor(equipment)) {
                String currentPath = equipment.getOrDefault(ModDataComponents.ARMOR_PATH.get(), "none");
                if (currentPath.equals("none")) {
                    String newPath = forgedType.equals("fortitude") ? "reduction" : "evasion";
                    equipment.set(ModDataComponents.ARMOR_PATH.get(), newPath);
                }
            }

            // Reset Fail Stack
            player.setData(net.kankrittapon.rpgem.init.ModAttachments.FAIL_STACK, 0);

            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer,
                        new net.kankrittapon.rpgem.network.PacketSyncFailStack(0));
            }

            player.sendSystemMessage(Component.literal(
                    "§a✦ Upgrade Successful! New Level: " + getFormattedLevel(nextLevel)));
            level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ANVIL_USE,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
        } else {
            // FAIL
            // Increment Fail Stack
            player.setData(net.kankrittapon.rpgem.init.ModAttachments.FAIL_STACK, failStack + 1);

            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer,
                        new net.kankrittapon.rpgem.network.PacketSyncFailStack(failStack + 1));
            }

            // Durability Penalty (-10 Durability = +10 Damage)
            if (equipment.isDamageableItem()) {
                int newDamage = Math.min(equipment.getMaxDamage(), equipment.getDamageValue() + 10);
                equipment.setDamageValue(newDamage);
            }

            // Downgrade Logic (Tier 2/3)
            if (tier >= 2 || currentLevel >= 15) {
                boolean hasProtection = !supportItem.isEmpty() && supportItem.is(ModItems.PROTECTION_STONE.get());

                if (hasProtection) {
                    player.sendSystemMessage(Component.literal("§b[Protection Stone] Item saved from Downgrade!"));
                    supportItem.shrink(1);
                } else {
                    // Downgrade
                    int downgradedLevel = Math.max(15, currentLevel - 1); // Min +15/PRI
                    if (downgradedLevel < currentLevel) {
                        equipment.set(ModDataComponents.UPGRADE_LEVEL.get(), downgradedLevel);
                        updateItemName(equipment, downgradedLevel);
                        player.sendSystemMessage(Component.literal(
                                "§c⚠ Upgrade Failed! Item downgraded to " + getFormattedLevel(downgradedLevel)));
                    } else {
                        player.sendSystemMessage(
                                Component.literal("§c✦ Upgrade Failed! (Fail Stack: " + (failStack + 1) + ")"));
                    }
                }
            } else {
                player.sendSystemMessage(
                        Component.literal("§c✦ Upgrade Failed! (Fail Stack: " + (failStack + 1) + ")"));
            }

            level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ANVIL_BREAK,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }

    private void updateItemName(ItemStack stack, int upgradeLevel) {
        // 1. Get/Save Original Name
        Component originalNameComponent;
        String originalNameJson;

        if (stack.has(ModDataComponents.ORIGINAL_NAME.get())) {
            originalNameJson = stack.get(ModDataComponents.ORIGINAL_NAME.get());
            originalNameComponent = Component.Serializer.fromJson(originalNameJson,
                    this.level != null ? this.level.registryAccess() : null);
            if (originalNameComponent == null)
                originalNameComponent = stack.getHoverName();
        } else {
            // First time: Save current name as original
            originalNameComponent = stack.getHoverName();

            // Serialize and Save
            net.minecraft.core.RegistryAccess registryAccess = this.level != null ? this.level.registryAccess() : null;
            originalNameJson = Component.Serializer.toJson(originalNameComponent, registryAccess);
            stack.set(ModDataComponents.ORIGINAL_NAME.get(), originalNameJson);
        }

        // 2. Get Prefix
        String prefix = getUpgradePrefix(upgradeLevel);

        // 3. Set Name
        if (!prefix.isEmpty()) {
            Component newName = Component.literal(prefix + ": ").append(originalNameComponent);
            stack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, newName);
        }
    }

    private String getUpgradePrefix(int level) {
        if (level <= 0)
            return "";
        if (level <= 15)
            return "§7[+" + level + "]"; // Gray/White

        // PRI - DEC (16 - 25)
        switch (level) {
            case 16:
                return "§a[PRI]"; // Green
            case 17:
                return "§b[DUO]"; // Blue
            case 18:
                return "§e[TRI]"; // Yellow
            case 19:
                return "§6[TET]"; // Gold
            case 20:
                return "§c[PEN]"; // Red (Orange-ish in MC code usually handled by Gold/Red mix, using Red here)
            case 21:
                return "§4[HEX]"; // Dark Red
            case 22:
                return "§5[SEP]"; // Dark Purple
            case 23:
                return "§d[OCT]"; // Light Purple
            case 24:
                return "§9[NOV]"; // Blueish Purple (Indigo)
            case 25:
                return "§5§k[DEC]"; // Obfuscated/Rainbow (Magic?) - Using Dark Purple + Magic for now
            default:
                return "§5[Ultimate]"; // > 25
        }
    }

    private String getFormattedLevel(int level) {
        return getUpgradePrefix(level);
    }

}
