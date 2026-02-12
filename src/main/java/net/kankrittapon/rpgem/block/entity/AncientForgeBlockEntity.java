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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.nbt.CompoundTag;
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

    // Upgrade Logic
    public void tryUpgradeItem(Level level, Player player) {
        ItemStack equipment = this.itemHandler.getStackInSlot(0);
        ItemStack stone = this.itemHandler.getStackInSlot(1);

        if (equipment.isEmpty() || stone.isEmpty())
            return;

        int currentLevel = equipment.getOrDefault(ModDataComponents.UPGRADE_LEVEL.get(), 0);
        int nextLevel = currentLevel + 1;

        // Tier Determination
        int tier = 0;
        if (stone.is(ModItems.UPGRADE_STONE_TIER_1.get()))
            tier = 1;
        else if (stone.is(ModItems.UPGRADE_STONE_TIER_2.get()))
            tier = 2;
        else if (stone.is(ModItems.UPGRADE_STONE_TIER_3.get()))
            tier = 3;

        if (tier == 0)
            return; // Invalid stone

        // Validation: Check if stone matches current level range
        if (tier == 1 && currentLevel >= 15) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("This item needs a Tier 2 stone!"));
            return;
        }
        if (tier == 2 && (currentLevel < 15 || currentLevel >= 25)) { // 15 + 10 = 25 (XV -> XXV in internal logic, or
                                                                      // I-X visual)
            player.sendSystemMessage(net.minecraft.network.chat.Component
                    .literal(currentLevel < 15 ? "Item level too low for Tier 2!" : "Item needs a Tier 3 stone!"));
            return;
        }
        if (tier == 3 && currentLevel < 25) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Item level too low for Tier 3!"));
            return;
        }
        if (tier == 3 && currentLevel >= 28) { // Max level (Final 3)
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Item is already at Max Level!"));
            return;
        }

        // RNG Calculation
        double successRate = 0.0;
        if (tier == 1)
            successRate = Config.UPGRADE_SUCCESS_RATE_TIER_1.get();
        else if (tier == 2)
            successRate = Config.UPGRADE_SUCCESS_RATE_TIER_2.get();
        else if (tier == 3)
            successRate = Config.UPGRADE_SUCCESS_RATE_TIER_3.get();

        // Roll
        boolean success = level.random.nextDouble() < successRate;

        // Consume Stone
        stone.shrink(1);

        if (success) {
            equipment.set(ModDataComponents.UPGRADE_LEVEL.get(), nextLevel);
            player.sendSystemMessage(net.minecraft.network.chat.Component
                    .literal("§aUpgrade Successful! New Level: " + getFormattedLevel(nextLevel)));
            level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ANVIL_USE,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
        } else {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§cUpgrade Failed!"));
            level.playSound(null, this.getBlockPos(), net.minecraft.sounds.SoundEvents.ANVIL_BREAK,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);

            // Downgrade Logic (Tier 2 & 3 only)
            if (tier > 1) {
                // simple logic: lose 1 level on fail for high tiers, or keep same.
                // Creating a risk config is recommended, for now let's just keep it safe or 50%
                // chance to drop.
                if (level.random.nextBoolean()) {
                    int newLevel = Math.max(tier == 2 ? 15 : 25, currentLevel - 1); // Don't drop below tier floor
                    equipment.set(ModDataComponents.UPGRADE_LEVEL.get(), newLevel);
                    player.sendSystemMessage(net.minecraft.network.chat.Component
                            .literal("§4Ouch! Item level decreased to: " + getFormattedLevel(newLevel)));
                }
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
