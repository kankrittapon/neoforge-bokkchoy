package net.kankrittapon.rpgem;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        // ==========================================
        // Alchemy Table Settings
        // ==========================================
        public static final ModConfigSpec.IntValue ALCHEMY_TABLE_TICK_RATE = BUILDER
                        .comment("Frequency of recipe verification (in ticks). Lower = Faster but more lag.")
                        .defineInRange("alchemyTableTickRate", 10, 1, 100);

        public static final ModConfigSpec.IntValue ALCHEMY_TABLE_BASE_TIME = BUILDER
                        .comment("Base time taken to brew a potion (in ticks). 20 ticks = 1 second.")
                        .defineInRange("alchemyTableBaseTime", 100, 1, 10000);

        // ==========================================
        // RPG Potion Effects Settings
        // ==========================================
        public static final ModConfigSpec.DoubleValue DODGE_CHANCE = BUILDER
                        .comment("Chance to dodge an attack (0.0 - 1.0). Default: 0.3 (30%)")
                        .defineInRange("dodgeChance", 0.3, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue THORNS_CHANCE = BUILDER
                        .comment("Chance to reflect damage (0.0 - 1.0). Default: 0.1 (10%)")
                        .defineInRange("thornsChance", 0.1, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue REFLECTION_MULTIPLIER = BUILDER
                        .comment("Damage multiplier for Thorns reflection. Default: 2.0 (200%)")
                        .defineInRange("reflectionMultiplier", 2.0, 0.0, 100.0);

        public static final ModConfigSpec.DoubleValue SAVIOR_REFLECTION_CHANCE = BUILDER
                        .comment("Chance for The Savior to reflect damage (0.0 - 1.0). Default: 0.25 (25%)")
                        .defineInRange("saviorReflectionChance", 0.25, 0.0, 1.0);

        // ==========================================
        // Upgrade System Settings (Ancient Forge)
        // ==========================================
        public static final ModConfigSpec.DoubleValue UPGRADE_SUCCESS_RATE_TIER_1 = BUILDER
                        .comment("Base success rate for Tier 1 upgrades (+1 to +15). Default: 0.7 (70%)")
                        .defineInRange("upgradeSuccessRateTier1", 0.7, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue UPGRADE_SUCCESS_RATE_TIER_2 = BUILDER
                        .comment("Base success rate for Tier 2 upgrades (I to X). Default: 0.4 (40%)")
                        .defineInRange("upgradeSuccessRateTier2", 0.4, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue UPGRADE_SUCCESS_RATE_TIER_3 = BUILDER
                        .comment("Base success rate for Tier 3 upgrades (Final). Default: 0.1 (10%)")
                        .defineInRange("upgradeSuccessRateTier3", 0.1, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue ARMOR_PENETRATION_CAP = BUILDER
                        .comment("Max damage dealt by Armor Penetration per hit. Default: 1000.0")
                        .defineInRange("armorPenetrationCap", 1000.0, 0.0, 1000000.0);

        public static final ModConfigSpec.DoubleValue FATE_SEAL_CHANCE = BUILDER
                        .comment("Chance for Fate Seal to trigger when conditions are met (0.0 - 1.0). Default: 0.5 (50%)")
                        .defineInRange("fateSealChance", 0.5, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue FATE_SEAL_THRESHOLD = BUILDER
                        .comment("HP threshold ratio for Fate Seal to trigger (0.0 - 1.0). Default: 0.15 (15%)")
                        .defineInRange("fateSealThreshold", 0.15, 0.0, 1.0);

        // ==========================================
        // Infinite Potion Drop Rates
        // ==========================================
        public static final ModConfigSpec.DoubleValue ZOMBIE_KING_HEART_CHANCE = BUILDER
                        .comment("Chance for Zombie King to drop Zombie Heart (Big Item). Default: 0.125%")
                        .defineInRange("zombieKingHeartChance", 0.00125, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue ZOMBIE_HEART_CHANCE = BUILDER
                        .comment("Chance for Zombie to drop Zombie Heart (Big Item). Default: 0.05%")
                        .defineInRange("zombieHeartChance", 0.0005, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue SKELETON_LORD_BONE_CHANCE = BUILDER
                        .comment("Chance for Skeleton Lord to drop Bone of Maze (Big Item). Default: 0.125%")
                        .defineInRange("skeletonLordBoneChance", 0.00125, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue SKELETON_BONE_CHANCE = BUILDER
                        .comment("Chance for Skeleton to drop Bone of Maze (Big Item). Default: 0.05%")
                        .defineInRange("skeletonBoneChance", 0.0005, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue WARDEN_COSMIC_CHANCE = BUILDER
                        .comment("Chance for Warden to drop Cosmic Emerald (Big Item). Default: 0.125%")
                        .defineInRange("wardenCosmicChance", 0.00125, 0.0, 1.0);

        public static final ModConfigSpec.DoubleValue MOB_COSMIC_CHANCE = BUILDER
                        .comment("Chance for Enderman/Witch to drop Cosmic Emerald (Big Item). Default: 0.05%")
                        .defineInRange("mobCosmicChance", 0.0005, 0.0, 1.0);

        // ==========================================
        // Integration Settings (Mod Compatibility)
        // ==========================================
        public static final ModConfigSpec.BooleanValue ENABLE_APOTHEOSIS_INTEGRATION = BUILDER
                        .comment("Enable integration with Apotheosis (Boss Loot, Elite Drops). Default: true")
                        .define("enableApotheosisIntegration", true);

        public static final ModConfigSpec.BooleanValue ENABLE_L2_HOSTILITY_INTEGRATION = BUILDER
                        .comment("Enable integration with L2 Hostility (Level-based Drop Scaling). Default: true")
                        .define("enableL2HostilityIntegration", true);

        public static final ModConfigSpec.BooleanValue ENABLE_L2_COMPLEMENTS_BALANCING = BUILDER
                        .comment("Enable balancing logic for L2 Complements (Evasion/Seal Resist). Default: true")
                        .define("enableL2ComplementsBalancing", true);

        // ==========================================
        // Inventory Weight System Settings
        // ==========================================
        public static final ModConfigSpec.DoubleValue WEIGHT_LIMIT_BASE = BUILDER
                        .comment("Base weight limit for players. Default: 100.0")
                        .defineInRange("weightLimitBase", 100.0, 1.0, 10000.0);

        public static final ModConfigSpec.DoubleValue WEIGHT_PENALTY_1_THRESHOLD = BUILDER
                        .comment("Threshold for usage penalty Level 1 (Slowness). Default: 1.0 (100% of Max Weight)")
                        .defineInRange("weightPenalty1Threshold", 1.0, 0.0, 10.0);

        public static final ModConfigSpec.DoubleValue WEIGHT_PENALTY_2_THRESHOLD = BUILDER
                        .comment("Threshold for usage penalty Level 2 (No Jump/High Slowness). Default: 1.5 (150% of Max Weight)")
                        .defineInRange("weightPenalty2Threshold", 1.5, 0.0, 10.0);

        public static final ModConfigSpec.DoubleValue WEIGHT_PENALTY_3_THRESHOLD = BUILDER
                        .comment("Threshold for usage penalty Level 3 (Immobile). Default: 2.0 (200% of Max Weight)")
                        .defineInRange("weightPenalty3Threshold", 2.0, 0.0, 10.0);

        static final ModConfigSpec SPEC = BUILDER.build();
}
