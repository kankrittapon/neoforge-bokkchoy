package net.kankrittapon.rpgem.util;

import net.kankrittapon.rpgem.Config;
import net.kankrittapon.rpgem.attachment.WeightData;
import net.kankrittapon.rpgem.entity.custom.FairyEntity;
import net.kankrittapon.rpgem.init.ModAttachments;
import net.kankrittapon.rpgem.network.PacketSyncWeight;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class WeightHandler {

    public static void tick(Player player) {
        if (player.level().isClientSide)
            return;

        double current = calculateCurrentWeight(player);
        double max = calculateEffectiveMaxWeight(player);

        WeightData data = player.getData(ModAttachments.WEIGHT_DATA);

        // Sync only if changed significantly (e.g. > 0.1 difference) to save bandwidth
        if (Math.abs(data.getCurrentWeight() - current) > 0.05 || Math.abs(data.getMaxWeight() - max) > 0.05) {
            data.setCurrentWeight((float) current);
            data.setMaxWeight((float) max);

            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new PacketSyncWeight((float) current, (float) max));
            }
        }
    }

    public static double calculateCurrentWeight(Player player) {
        double totalWeight = 0.0;

        // Inventory
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty()) {
                totalWeight += getItemWeight(stack);
            }
        }
        // Armor
        for (ItemStack stack : player.getInventory().armor) {
            if (!stack.isEmpty()) {
                totalWeight += getItemWeight(stack);
            }
        }
        // Offhand
        for (ItemStack stack : player.getInventory().offhand) {
            if (!stack.isEmpty()) {
                totalWeight += getItemWeight(stack);
            }
        }

        return totalWeight;
    }

    private static double getItemWeight(ItemStack stack) {
        // TODO: Future - Use Registry/Tags for specific weights
        if (stack.getItem() instanceof net.minecraft.world.item.ArmorItem ||
                stack.getItem() instanceof net.minecraft.world.item.TieredItem ||
                stack.getItem() instanceof net.minecraft.world.item.ShieldItem) {
            return 5.0; // Heavy equipment
        }
        // Specific exception for Fairy Wing/Bottle? Maybe light?

        if (stack.getMaxStackSize() == 1) {
            return 1.0; // Unstackables (Potions, etc.) - Reduced from 2.0
        }
        return 0.1 * stack.getCount(); // Standard items
    }

    public static double calculateEffectiveMaxWeight(Player player) {
        double base = Config.WEIGHT_LIMIT_BASE.get(); // Default 100.0

        // --- FAIRY SKILL 4.3: Feathery Steps ---
        int fairyTier = getNearbyFairyTier(player);
        double bonusMultiplier = 0.0;

        if (fairyTier > 0) {
            if (fairyTier >= 4)
                bonusMultiplier = 1.0; // +100% (200.0)
            else if (fairyTier >= 3)
                bonusMultiplier = 0.75; // +75% (175.0)
            else if (fairyTier >= 2)
                bonusMultiplier = 0.5; // +50% (150.0)
            else if (fairyTier >= 1)
                bonusMultiplier = 0.2; // +20% (120.0)
        }

        return base * (1.0 + bonusMultiplier);
    }

    public static int getPenaltyLevel(Player player) {
        // Use cached data from Attachment (synced)
        WeightData data = player.getData(ModAttachments.WEIGHT_DATA);
        float current = data.getCurrentWeight();
        float max = data.getMaxWeight();

        if (max <= 0)
            max = 100.0f; // Safety

        double ratio = current / max;

        if (ratio >= Config.WEIGHT_PENALTY_3_THRESHOLD.get()) // 2.0 (200%)
            return 3;
        if (ratio >= Config.WEIGHT_PENALTY_2_THRESHOLD.get()) // 1.5 (150%)
            return 2;
        if (ratio >= Config.WEIGHT_PENALTY_1_THRESHOLD.get()) // 1.0 (100%)
            return 1;

        return 0;
    }

    private static int getNearbyFairyTier(Player player) {
        if (player.level().isClientSide)
            return 0;

        List<FairyEntity> fairies = player.level().getEntitiesOfClass(
                FairyEntity.class,
                player.getBoundingBox().inflate(10.0),
                entity -> entity.getOwnerUUID().isPresent() && entity.getOwnerUUID().get().equals(player.getUUID()));

        if (!fairies.isEmpty()) {
            return fairies.get(0).getTier();
        }
        return 0;
    }
}
