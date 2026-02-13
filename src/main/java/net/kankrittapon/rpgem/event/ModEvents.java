package net.kankrittapon.rpgem.event;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.init.ModMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.List;

@EventBusSubscriber(modid = RPGEasyMode.MODID)
public class ModEvents {

        @SubscribeEvent
        public static void onLivingIncomingDamage(
                        net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent event) {
                if (event.getEntity() instanceof Player player) {
                        // === EVASION ===
                        double evasionChance = player
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.EVASION);

                        // Hardcoded check for potion effect if attribute is 0?
                        // Better to let attribute handle it.
                        // If the Potion gives the attribute, this works.
                        // If Potion uses old logic, we might miss it.
                        // Let's add Potion check fallback for now to be safe.
                        if (player.hasEffect(ModMobEffects.EVASION)) {
                                evasionChance = Math.max(evasionChance,
                                                net.kankrittapon.rpgem.Config.DODGE_CHANCE.get());
                        }

                        if (evasionChance > 0 && player.getRandom().nextFloat() < evasionChance) {
                                event.setCanceled(true); // Negate all damage
                                player.level().playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_FLAP,
                                                SoundSource.PLAYERS, 1.0f, 2.0f);
                                player.displayClientMessage(net.minecraft.network.chat.Component
                                                .literal("§b<< Dodged! (" + (int) (evasionChance * 100) + "%) >>"),
                                                true);
                        }
                }
        }

        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent.Pre event) {
                // === PLAYER ATTACKING (Crit, Element Damage) ===
                if (event.getSource().getEntity() instanceof Player attacker) {
                        // 1. Critical Chance
                        double critChance = attacker
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.CRIT_CHANCE);
                        if (critChance > 0 && attacker.getRandom().nextFloat() < critChance) {
                                event.setNewDamage(event.getOriginalDamage() * 2.0f); // Double Damage
                                attacker.level().playSound(null, attacker.blockPosition(),
                                                SoundEvents.PLAYER_ATTACK_CRIT,
                                                SoundSource.PLAYERS, 1.0f, 1.0f);
                                attacker.displayClientMessage(
                                                net.minecraft.network.chat.Component.literal("§c<< CRITICAL HIT! >>"),
                                                true);
                        }

                        // 2. Element Damage
                        double elementDmg = attacker
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.ELEMENT_DAMAGE);
                        if (elementDmg > 0) {
                                float damage = event.getNewDamage(); // Get current damage (potentially crit)
                                event.setNewDamage(damage * (1.0f + (float) elementDmg));
                                // Optional: Particle effect for magic damage?
                        }
                }

                // === PLAYER DEFENDING (Damage Reduction, Reflect Resist) ===
                if (event.getEntity() instanceof Player victim) {
                        // 1. Damage Reduction
                        double dr = victim
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.DAMAGE_REDUCTION);
                        if (dr > 0) {
                                float damage = event.getNewDamage();
                                // Cap DR at 80% to prevent invincibility (though attribute max is 1.0)
                                dr = Math.min(dr, 0.8);
                                event.setNewDamage(damage * (1.0f - (float) dr));
                        }

                        // 2. Reflect Resist (Only if source is Thorns)
                        if (event.getSource().is(net.minecraft.world.damagesource.DamageTypes.THORNS)) {
                                double reflectResist = victim
                                                .getAttributeValue(
                                                                net.kankrittapon.rpgem.init.ModAttributes.REFLECT_RESIST);
                                if (reflectResist > 0) {
                                        float damage = event.getNewDamage();
                                        event.setNewDamage(damage * (1.0f - (float) reflectResist));
                                }
                        }
                }
        }

        @SubscribeEvent
        public static void onLivingDamagePost(LivingDamageEvent.Post event) {
                // === LIFE STEAL ===
                if (event.getSource().getEntity() instanceof Player attacker) {
                        double lifeSteal = attacker
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.LIFE_STEAL);
                        if (lifeSteal > 0) {
                                float damage = event.getNewDamage();
                                float heal = damage * (float) lifeSteal;
                                if (heal > 0)
                                        attacker.heal(heal);
                        }
                }

                if (event.getEntity() instanceof Player player) {
                        Entity attacker = event.getSource().getEntity();
                        float amount = event.getOriginalDamage();

                        // REFLECTION LOGIC (Iron Thorns & Savior)
                        if (attacker instanceof LivingEntity livingAttacker) {
                                boolean hasThorns = player.hasEffect(ModMobEffects.IRON_THORNS);
                                boolean hasSavior = player.hasEffect(ModMobEffects.BOUNDLESS_GRACE);

                                double reflectChance = 0.0;
                                if (hasThorns)
                                        reflectChance = net.kankrittapon.rpgem.Config.THORNS_CHANCE.get(); // 10%
                                if (hasSavior)
                                        reflectChance = Math.max(reflectChance,
                                                        net.kankrittapon.rpgem.Config.SAVIOR_REFLECTION_CHANCE.get()); // 25%
                                                                                                                       // overrides
                                                                                                                       // if
                                                                                                                       // higher

                                if (reflectChance > 0 && player.getRandom().nextFloat() < reflectChance) {
                                        float reflectDamage = amount
                                                        * net.kankrittapon.rpgem.Config.REFLECTION_MULTIPLIER.get()
                                                                        .floatValue();
                                        livingAttacker.hurt(player.damageSources().thorns(player), reflectDamage);

                                        player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND,
                                                        SoundSource.PLAYERS,
                                                        0.5f, 0.5f);
                                        if (hasSavior) {
                                                player.displayClientMessage(
                                                                net.minecraft.network.chat.Component.literal(
                                                                                "§6<< DIVINE REFLECTION! >>"),
                                                                true);
                                        } else {
                                                player.displayClientMessage(
                                                                net.minecraft.network.chat.Component.literal(
                                                                                "§7<< Thorns Reflected! >>"),
                                                                true);
                                        }
                                }
                        }

                        // DIVINE REACTION (Savior Cleanse)
                        if (player.hasEffect(ModMobEffects.BOUNDLESS_GRACE)) {
                                // Remove only harmful effects
                                java.util.List<net.minecraft.world.effect.MobEffectInstance> active = new java.util.ArrayList<>(
                                                player.getActiveEffects());
                                for (net.minecraft.world.effect.MobEffectInstance instance : active) {
                                        if (instance.getEffect().value()
                                                        .getCategory() == net.minecraft.world.effect.MobEffectCategory.HARMFUL) {
                                                player.removeEffect(instance.getEffect());
                                        }
                                }

                                // Re-apply buffs
                                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 3)); // Regen IV
                                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1)); // Speed II
                        }
                }
        }

        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
                LivingEntity entity = event.getEntity();

                if (entity instanceof Player player) {
                        if (player.hasEffect(ModMobEffects.BOUNDLESS_GRACE)) {
                                // Prevent Death
                                event.setCanceled(true);

                                // 2. Set HP to 50%
                                player.setHealth(player.getMaxHealth() * 0.5f);

                                // 3. Remove Effect
                                player.removeEffect(ModMobEffects.BOUNDLESS_GRACE);

                                // 4. Spawn Particles & Sound
                                if (player.level() instanceof ServerLevel serverLevel) {
                                        serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                                                        player.getX(), player.getY() + 1.0, player.getZ(),
                                                        1, 0, 0, 0, 0);
                                        serverLevel.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE,
                                                        SoundSource.PLAYERS,
                                                        1.0f, 1.0f);
                                }

                                // 5. Apply Buffs
                                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0)); // 3s
                                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 4)); // 3s, Amp
                                                                                                              // 4
                                                                                                              // (Resistance
                                                                                                              // V) =
                                                                                                              // Invulnerable

                                // 6. Knockback Mobs
                                List<Entity> nearbyEntities = player.level().getEntities(player,
                                                player.getBoundingBox().inflate(5.0));
                                for (Entity e : nearbyEntities) {
                                        if (e instanceof LivingEntity living && !(e instanceof Player)) {
                                                double dx = e.getX() - player.getX();
                                                double dz = e.getZ() - player.getZ();
                                                living.knockback(2.0, -dx, -dz);
                                        }
                                }
                        }
                }
        }

        @SubscribeEvent
        public static void onItemAttributeModifier(net.neoforged.neoforge.event.ItemAttributeModifierEvent event) {
                net.minecraft.world.item.ItemStack stack = event.getItemStack();
                if (stack.has(net.kankrittapon.rpgem.init.ModDataComponents.UPGRADE_LEVEL.get())) {
                        int level = stack.get(net.kankrittapon.rpgem.init.ModDataComponents.UPGRADE_LEVEL.get());
                        if (level > 0) {
                                boolean isWeapon = stack.getItem() instanceof net.minecraft.world.item.SwordItem
                                                || stack.getItem() instanceof net.minecraft.world.item.AxeItem;
                                boolean isArmor = stack.getItem() instanceof net.minecraft.world.item.ArmorItem;

                                // === WEAPON STATS ===
                                if (isWeapon) {
                                        // 1. Attack Damage
                                        double damageBonus = 0;
                                        if (level <= 15)
                                                damageBonus = level * 1.0;
                                        else if (level <= 25)
                                                damageBonus = 15 + ((level - 15) * 2.0);
                                        else
                                                damageBonus = 15 + 20 + ((level - 25) * 5.0);

                                        event.addModifier(
                                                        net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE,
                                                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                        net.minecraft.resources.ResourceLocation
                                                                                        .fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "upgrade_damage"),
                                                                        damageBonus,
                                                                        net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);

                                        // 2. Life Steal (0.5% per level)
                                        event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.LIFE_STEAL,
                                                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                        net.minecraft.resources.ResourceLocation
                                                                                        .fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "upgrade_life_steal"),
                                                                        level * 0.005,
                                                                        net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);

                                        // 3. Crit Chance (1% per level)
                                        event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.CRIT_CHANCE,
                                                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                        net.minecraft.resources.ResourceLocation
                                                                                        .fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "upgrade_crit_chance"),
                                                                        level * 0.01,
                                                                        net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);

                                        // 4. Element Damage (2% per level)
                                        event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.ELEMENT_DAMAGE,
                                                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                        net.minecraft.resources.ResourceLocation
                                                                                        .fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "upgrade_element_damage"),
                                                                        level * 0.02,
                                                                        net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);
                                }

                                // === ARMOR STATS ===
                                if (isArmor) {
                                        // 1. Defense (0.5 per level)
                                        double armorBonus = level * 0.5;
                                        event.addModifier(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR,
                                                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                        net.minecraft.resources.ResourceLocation
                                                                                        .fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "upgrade_armor"),
                                                                        armorBonus,
                                                                        net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                        net.minecraft.world.entity.EquipmentSlotGroup.ARMOR);

                                        // Path-Specific Stats
                                        String path = stack.getOrDefault(
                                                        net.kankrittapon.rpgem.init.ModDataComponents.ARMOR_PATH.get(),
                                                        "none");

                                        if (path.equals("reduction")) {
                                                // DR Path: High DR, High Reflect Resist, High Seal Resist
                                                event.addModifier(
                                                                net.kankrittapon.rpgem.init.ModAttributes.DAMAGE_REDUCTION,
                                                                new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                                net.minecraft.resources.ResourceLocation
                                                                                                .fromNamespaceAndPath(
                                                                                                                RPGEasyMode.MODID,
                                                                                                                "upgrade_dr"),
                                                                                level * 0.005, // 0.5% per level -> 15%
                                                                                               // at Lv30
                                                                                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                                net.minecraft.world.entity.EquipmentSlotGroup.ARMOR);

                                                event.addModifier(
                                                                net.kankrittapon.rpgem.init.ModAttributes.REFLECT_RESIST,
                                                                new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                                net.minecraft.resources.ResourceLocation
                                                                                                .fromNamespaceAndPath(
                                                                                                                RPGEasyMode.MODID,
                                                                                                                "upgrade_reflect_resist"),
                                                                                level * 0.02, // 2% per level
                                                                                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                                net.minecraft.world.entity.EquipmentSlotGroup.ARMOR);

                                                event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.SEAL_RESIST,
                                                                new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                                net.minecraft.resources.ResourceLocation
                                                                                                .fromNamespaceAndPath(
                                                                                                                RPGEasyMode.MODID,
                                                                                                                "upgrade_seal_resist"),
                                                                                level * 0.02, // 2% per level
                                                                                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                                net.minecraft.world.entity.EquipmentSlotGroup.ARMOR);

                                        } else if (path.equals("evasion")) {
                                                // Evasion Path: High Evasion, Mod Seal/Reflect
                                                event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.EVASION,
                                                                new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                                net.minecraft.resources.ResourceLocation
                                                                                                .fromNamespaceAndPath(
                                                                                                                RPGEasyMode.MODID,
                                                                                                                "upgrade_evasion"),
                                                                                level * 0.01, // 1% per level -> 30% at
                                                                                              // Lv30 (can stack to cap)
                                                                                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                                net.minecraft.world.entity.EquipmentSlotGroup.ARMOR);

                                                event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.SEAL_RESIST,
                                                                new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                                net.minecraft.resources.ResourceLocation
                                                                                                .fromNamespaceAndPath(
                                                                                                                RPGEasyMode.MODID,
                                                                                                                "upgrade_seal_resist"),
                                                                                level * 0.01, // 1% per level
                                                                                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                                net.minecraft.world.entity.EquipmentSlotGroup.ARMOR);
                                        }
                                }
                        }
                }
        }

        @SubscribeEvent
        public static void onLivingDrops(net.neoforged.neoforge.event.entity.living.LivingDropsEvent event) {
                if (event.getEntity() instanceof net.kankrittapon.rpgem.entity.custom.ZombieKing) {
                        // Tier 1 Stone (Guaranteed)
                        event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(event.getEntity().level(),
                                        event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                                        new net.minecraft.world.item.ItemStack(
                                                        net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_1
                                                                        .get())));

                        // Tier 2 Stone (50% Chance + Looting)
                        int looting = 0;
                        if (event.getSource().getEntity() instanceof net.minecraft.world.entity.LivingEntity attacker) {
                                // looting =
                                // net.minecraft.world.item.enchantment.EnchantmentHelper.getMobLooting(attacker);
                                // MVP: Hardcode looting effect or ignore for now to avoid complexity with 1.21
                                // changes
                                looting = 0;
                        }

                        if (event.getEntity().getRandom().nextFloat() < 0.5f + (looting * 0.1f)) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_2
                                                                                .get())));
                        }
                } else if (event.getEntity() instanceof net.kankrittapon.rpgem.entity.custom.SkeletonLord) {
                        // Tier 2 Stone (Guaranteed)
                        event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(event.getEntity().level(),
                                        event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                                        new net.minecraft.world.item.ItemStack(
                                                        net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_2
                                                                        .get())));

                        // Tier 3 Stone (30% Chance + Looting)
                        int looting = 0;
                        if (event.getSource().getEntity() instanceof net.minecraft.world.entity.LivingEntity attacker) {
                                // looting =
                                // net.minecraft.world.item.enchantment.EnchantmentHelper.getMobLooting(attacker);
                                looting = 0;
                        }

                        if (event.getEntity().getRandom().nextFloat() < 0.3f + (looting * 0.1f)) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_3
                                                                                .get())));
                        }
                }
        }
}
