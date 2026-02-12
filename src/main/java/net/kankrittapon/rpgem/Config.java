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

        static final ModConfigSpec SPEC = BUILDER.build();
}
