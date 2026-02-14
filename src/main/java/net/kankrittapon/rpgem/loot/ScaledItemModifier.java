package net.kankrittapon.rpgem.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class ScaledItemModifier extends LootModifier {
    public static final MapCodec<ScaledItemModifier> CODEC = RecordCodecBuilder
            .mapCodec(instance -> codecStart(instance)
                    .and(instance.group(
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(m -> m.item),
                            Codec.FLOAT.fieldOf("base_chance").forGetter(m -> m.baseChance),
                            Codec.FLOAT.fieldOf("level_multiplier").forGetter(m -> m.levelMultiplier),
                            Codec.FLOAT.fieldOf("looting_multiplier").forGetter(m -> m.lootingMultiplier)))
                    .apply(instance, ScaledItemModifier::new));

    private final Item item;
    private final float baseChance;
    private final float levelMultiplier;
    private final float lootingMultiplier;

    public ScaledItemModifier(LootItemCondition[] conditionsIn, Item item, float baseChance, float levelMultiplier,
            float lootingMultiplier) {
        super(conditionsIn);
        this.item = item;
        this.baseChance = baseChance;
        this.levelMultiplier = levelMultiplier;
        this.lootingMultiplier = lootingMultiplier;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // 1. Get Looting Level
        int looting = 0;
        Entity killer = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        if (killer instanceof LivingEntity living) {
            looting = EnchantmentHelper.getEnchantmentLevel(
                    living.level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                            .getOrThrow(net.minecraft.world.item.enchantment.Enchantments.LOOTING),
                    living);
        }

        // 2. Get Mob Level (L2 Hostility Integration)
        int mobLevel = 0;
        Entity victim = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (victim instanceof LivingEntity) {
            // Check NBT as fallback for L2 Hostility Level
            if (victim.getPersistentData().contains("hostility:level")) {
                mobLevel = victim.getPersistentData().getInt("hostility:level");
            } else if (victim.getPersistentData().contains("hostility:difficulty")) {
                mobLevel = victim.getPersistentData().getInt("hostility:difficulty");
            }
        }

        // 3. Apotheosis Boss Detection (Extra luck for bosses)
        float bossBonus = 0;
        if (victim instanceof LivingEntity) {
            // Apotheosis bosses often have much higher health or specific attributes
            // We can also check for certain NBT tags known to be used by Apotheosis
            if (victim.getPersistentData().contains("apotheosis:boss") ||
                    victim.getPersistentData().contains("apotheosis:is_boss")) {
                bossBonus = 0.05f; // Extra 5% base chance for Apotheosis Bosses
            }
        }

        // 4. Calculate Chance
        // Chance = (Base + BossBonus) + (Level * LevelMult) + (Looting * LootingMult)
        float currentChance = (this.baseChance + bossBonus) + (mobLevel * this.levelMultiplier)
                + (looting * this.lootingMultiplier);

        // 5. Roll
        if (context.getRandom().nextFloat() < currentChance) {
            generatedLoot.add(new ItemStack(this.item));
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
