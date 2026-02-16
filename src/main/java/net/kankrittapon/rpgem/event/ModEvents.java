package net.kankrittapon.rpgem.event;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.init.ModBlocks;
import net.kankrittapon.rpgem.init.ModMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.kankrittapon.rpgem.Config;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.kankrittapon.rpgem.init.ModVillagers;
import net.kankrittapon.rpgem.init.ModAttachments;
import net.kankrittapon.rpgem.init.ModItems;

import java.util.List;
import java.util.ArrayList;

@EventBusSubscriber(modid = RPGEasyMode.MODID)
public class ModEvents {

        @SubscribeEvent
        public static void onLivingIncomingDamage(
                        net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent event) {
                if (event.getEntity() instanceof Player player) {
                        // === EVASION (Stack: rpgem:evasion + apothic_attributes:dodge_chance) ===
                        double evasionChance = player
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.EVASION);

                        // Accuracy Counter: Subtract attacker's accuracy from victim's evasion
                        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
                                double accuracy = attacker
                                                .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.ACCURACY);
                                evasionChance = Math.max(0, evasionChance - accuracy);
                        }

                        // Stack: Apothic Attributes dodge_chance (optional — works without Apothic
                        // installed)
                        if (net.kankrittapon.rpgem.Config.ENABLE_APOTHEOSIS_INTEGRATION.get()) {
                                try {
                                        net.minecraft.resources.ResourceLocation apothicDodgeRL = net.minecraft.resources.ResourceLocation
                                                        .fromNamespaceAndPath("apothic_attributes", "dodge_chance");
                                        net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> apothicDodge = net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE
                                                        .getHolder(apothicDodgeRL).orElse(null);
                                        if (apothicDodge != null) {
                                                double apothicDodgeValue = player.getAttributeValue(apothicDodge);
                                                evasionChance = Math.min(1.0, evasionChance + apothicDodgeValue);
                                        }
                                } catch (Exception ignored) {
                                        // Apothic Attributes not installed — use rpgem:evasion only
                                }
                        }

                        // Potion fallback: If Evasion potion effect but attribute = 0
                        if (player.hasEffect(ModMobEffects.EVASION)) {
                                evasionChance = Math.max(evasionChance,
                                                net.kankrittapon.rpgem.Config.DODGE_CHANCE.get());
                        }

                        // GOD MODE: Minimum 80% Evasion
                        if (player.hasEffect(ModMobEffects.BOUNDLESS_GRACE)) {
                                evasionChance = Math.max(evasionChance, 0.8);
                        }

                        if (evasionChance > 0 && player.getRandom().nextFloat() < evasionChance) {
                                event.setCanceled(true); // Negate all damage
                                player.level().playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_FLAP,
                                                SoundSource.PLAYERS, 1.0f, 2.0f);
                                player.displayClientMessage(net.minecraft.network.chat.Component
                                                .literal("§b<< Dodged! (" + (int) (evasionChance * 100) + "%) >>"),
                                                true);
                                return;
                        }

                        // === SEAL RESIST (Counter: Ragnarok Trait) ===
                        double sealResist = player
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.SEAL_RESIST);
                        if (player.hasEffect(ModMobEffects.BOUNDLESS_GRACE))
                                sealResist = 1.0; // Potion T3 = 100% Seal Resist

                        if (sealResist > 0) {
                                // Check if player is about to be "Sealed" by Ragnarok trait (hypothetical
                                // event/effect)
                                // Since we don't have L2H API, we can hook into common sealing triggers
                        }
                }
        }

        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent.Pre event) {
                // GLOBAL DEBUG to check if event fires
                if (event.getEntity() != null
                                && event.getSource().getEntity() instanceof net.minecraft.world.entity.player.Player) {
                        System.out.println("DEBUG: ModEvents.onLivingDamage fired! Target: "
                                        + event.getEntity().getName().getString());
                }

                net.minecraft.world.damagesource.DamageSource source = event.getSource();
                Entity attacker = source.getEntity();

                double criticalChance = 0.0;
                double armorPen = 0.0; // % Max Health Damage Cap (config)

                if (attacker instanceof LivingEntity livingAttacker) {
                        if (event.getEntity() instanceof LivingEntity victim) {
                                // 1. Armor Penetration
                                armorPen = livingAttacker
                                                .getAttributeValue(
                                                                net.kankrittapon.rpgem.init.ModAttributes.ARMOR_PENETRATION);
                                if (armorPen > 0) {
                                        float currentDamage = event.getNewDamage();
                                        float victimArmor = victim.getArmorValue();
                                        float bonusDamage = victimArmor * (float) armorPen;

                                        // Apply Damage Cap
                                        double cap = Config.ARMOR_PENETRATION_CAP.get();
                                        if (bonusDamage > cap) {
                                                bonusDamage = (float) cap;
                                        }

                                        float newDamage = currentDamage + bonusDamage;
                                        event.setNewDamage(newDamage);

                                        if (livingAttacker.level() instanceof ServerLevel sl) {
                                                sl.sendParticles(ParticleTypes.CRIT, victim.getX(), victim.getY() + 1.0,
                                                                victim.getZ(), 5, 0.3, 0.3, 0.3, 0.05);
                                        }
                                }

                                // --- FATE SEAL (Undying Counter) ---
                                double antiHeal = livingAttacker
                                                .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.ANTI_HEAL);
                                boolean isGodMode = livingAttacker.hasEffect(ModMobEffects.BOUNDLESS_GRACE);
                                boolean canExecute = isGodMode || antiHeal >= 0.5;

                                // DEBUG: Check values (Unconditional for Players)
                                if (livingAttacker instanceof net.minecraft.world.entity.player.Player) {
                                        System.out.println("DEBUG: Fate Seal Check");
                                        System.out.println(" - Attacker: " + livingAttacker.getName().getString());
                                        System.out.println(" - Victim HP: " + victim.getHealth() + "/"
                                                        + victim.getMaxHealth() + " ("
                                                        + (victim.getHealth() / victim.getMaxHealth()) + ")");
                                        System.out.println(" - isGodMode: " + isGodMode);
                                        System.out.println(" - antiHeal: " + antiHeal);
                                        System.out.println(" - canExecute: " + canExecute);
                                        System.out.println(" - Threshold From Config: "
                                                        + net.kankrittapon.rpgem.Config.FATE_SEAL_THRESHOLD.get());
                                        System.out.println(" - Chance From Config: "
                                                        + net.kankrittapon.rpgem.Config.FATE_SEAL_CHANCE.get());
                                }

                                if (canExecute && (victim.getHealth() / victim
                                                .getMaxHealth() < net.kankrittapon.rpgem.Config.FATE_SEAL_THRESHOLD
                                                                .get())) {

                                        if (!event.getSource().is(
                                                        net.minecraft.world.damagesource.DamageTypes.FELL_OUT_OF_WORLD)) {

                                                // Check Chance
                                                if (livingAttacker.getRandom()
                                                                .nextDouble() < net.kankrittapon.rpgem.Config.FATE_SEAL_CHANCE
                                                                                .get()) {
                                                        System.out.println("DEBUG: Fate Seal TRIGGERED (Success)!");

                                                        if (!victim.level().isClientSide()) {
                                                                // STRATEGY 0: Nuke Undying Trait from NBT
                                                                net.minecraft.nbt.CompoundTag nbt = new net.minecraft.nbt.CompoundTag();
                                                                victim.saveWithoutId(nbt);
                                                                if (nbt.contains("neoforge:attachments")) {
                                                                        net.minecraft.nbt.CompoundTag attachments = nbt
                                                                                        .getCompound("neoforge:attachments");
                                                                        if (attachments.contains("l2hostility:mob")) {
                                                                                net.minecraft.nbt.CompoundTag hostility = attachments
                                                                                                .getCompound("l2hostility:mob");
                                                                                if (hostility.contains("traits")) {
                                                                                        net.minecraft.nbt.CompoundTag traits = hostility
                                                                                                        .getCompound("traits");
                                                                                        if (traits.contains(
                                                                                                        "l2hostility:undying")) {
                                                                                                traits.remove("l2hostility:undying");
                                                                                                victim.load(nbt);
                                                                                                System.out.println(
                                                                                                                "DEBUG: Removed Undying Trait via NBT!");
                                                                                        }
                                                                                }
                                                                        }
                                                                }
                                                        }

                                                        // STRATEGY 1: Strip All Effects (Remove Buffs)
                                                        victim.removeAllEffects();

                                                        // STRATEGY 2: Remove Held Items (Totem of Undying)
                                                        victim.setItemInHand(
                                                                        net.minecraft.world.InteractionHand.MAIN_HAND,
                                                                        net.minecraft.world.item.ItemStack.EMPTY);
                                                        victim.setItemInHand(
                                                                        net.minecraft.world.InteractionHand.OFF_HAND,
                                                                        net.minecraft.world.item.ItemStack.EMPTY);

                                                        // STRATEGY 3: Force Death
                                                        victim.setHealth(0f);
                                                        event.setNewDamage(0f); // Cancel physical damage

                                                        // STRATEGY 4: Void Damage (Execution)
                                                        victim.hurt(livingAttacker.damageSources().fellOutOfWorld(),
                                                                        Float.MAX_VALUE); // Apply Void Death

                                                        if (victim.level() instanceof ServerLevel serverLevel) {
                                                                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM,
                                                                                victim.getX(), victim.getY() + 1.0,
                                                                                victim.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                                                                livingAttacker.level().playSound(null,
                                                                                victim.blockPosition(),
                                                                                SoundEvents.WARDEN_SONIC_BOOM,
                                                                                SoundSource.PLAYERS, 1.0f, 1.0f);
                                                        }
                                                        if (livingAttacker instanceof Player player) {
                                                                player.displayClientMessage(
                                                                                net.minecraft.network.chat.Component
                                                                                                .literal("§5<< FATE SEAL >>"),
                                                                                true);
                                                        }
                                                        return;
                                                } else {
                                                        System.out.println("DEBUG: Fate Seal MISSED (RNG)!");
                                                }
                                        }
                                }
                        }

                        // 3. Critical Chance
                        double critChance = livingAttacker
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.CRIT_CHANCE);
                        if (critChance > 0 && livingAttacker.getRandom().nextFloat() < critChance) {
                                event.setNewDamage(event.getOriginalDamage() * 2.0f);
                                livingAttacker.level().playSound(null, livingAttacker.blockPosition(),
                                                SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0f, 1.0f);
                                if (livingAttacker instanceof Player player) {
                                        player.displayClientMessage(net.minecraft.network.chat.Component
                                                        .literal("§c<< CRITICAL HIT! >>"), true);
                                }
                        }

                        // 4. Element Damage / God of Element
                        double elementDmg = livingAttacker
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.ELEMENT_DAMAGE);
                        boolean isGodMode = livingAttacker.hasEffect(ModMobEffects.BOUNDLESS_GRACE);

                        if (isGodMode || elementDmg > 0) {
                                if (event.getEntity() instanceof LivingEntity victim) {
                                        float currentDamage = event.getNewDamage();
                                        if (elementDmg > 0) {
                                                event.setNewDamage(currentDamage * (1.0f + (float) elementDmg));
                                        }

                                        if (isGodMode) {
                                                // ... God Mode Effects ...
                                                victim.setRemainingFireTicks(100);
                                                victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60,
                                                                2));
                                                victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1));
                                                victim.knockback(1.5f, attacker.getX() - victim.getX(),
                                                                attacker.getZ() - victim.getZ());
                                                event.setNewDamage(event.getNewDamage() + 10.0f);

                                                if (attacker.level() instanceof ServerLevel sl) {
                                                        sl.sendParticles(ParticleTypes.ENCHANTED_HIT, victim.getX(),
                                                                        victim.getY() + 1.0, victim.getZ(), 10, 0.5,
                                                                        0.5, 0.5, 0.1);
                                                        sl.sendParticles(ParticleTypes.FLAME, victim.getX(),
                                                                        victim.getY() + 1.0, victim.getZ(), 5, 0.5, 0.5,
                                                                        0.5, 0.1);
                                                }
                                        } else {
                                                // ... Normal Element Cycle ...
                                                int activeElement = livingAttacker.getPersistentData()
                                                                .getInt("rpgem:active_element");
                                                switch (activeElement) {
                                                        case 0 -> {
                                                                victim.setRemainingFireTicks(40);
                                                        }
                                                        case 1 -> {
                                                                victim.addEffect(new MobEffectInstance(
                                                                                MobEffects.MOVEMENT_SPEED, 40, 1));
                                                        } // Intentionally keeping Speed/Slowness logic from before
                                                        case 2 -> {
                                                                victim.addEffect(new MobEffectInstance(
                                                                                MobEffects.WEAKNESS, 40, 0));
                                                        }
                                                        case 3 -> {
                                                                victim.knockback(0.5f, attacker.getX() - victim.getX(),
                                                                                attacker.getZ() - victim.getZ());
                                                        }
                                                        case 4 -> {
                                                                event.setNewDamage(event.getNewDamage() + 2.0f);
                                                        }
                                                }
                                        }
                                }
                        }
                }

                // === PLAYER DEFENDING (Damage Reduction, Reflect Resist) ===
                if (event.getEntity() instanceof

                Player victim) {
                        double dr = victim
                                        .getAttributeValue(net.kankrittapon.rpgem.init.ModAttributes.DAMAGE_REDUCTION);
                        if (dr > 0) {
                                float damage = event.getNewDamage();
                                dr = Math.min(dr, 0.8);
                                event.setNewDamage(damage * (1.0f - (float) dr));
                        }
                        if (event.getSource().is(net.minecraft.world.damagesource.DamageTypes.THORNS)) {
                                double reflectResist = victim.getAttributeValue(
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

                        // REFLECTION LOGIC (Savior V2: 80% Reflect Shield)
                        if (attacker instanceof LivingEntity livingAttacker) {
                                boolean hasThorns = player.hasEffect(ModMobEffects.IRON_THORNS);
                                boolean hasSavior = player.hasEffect(ModMobEffects.BOUNDLESS_GRACE);

                                double reflectChance = player
                                                .getAttributeValue(
                                                                net.kankrittapon.rpgem.init.ModAttributes.REFLECT_CHANCE);

                                if (hasThorns)
                                        reflectChance = Math.max(reflectChance,
                                                        net.kankrittapon.rpgem.Config.THORNS_CHANCE.get());
                                if (hasSavior)
                                        reflectChance = Math.max(reflectChance, 0.8); // Savior V2: 80% Reflection

                                if (reflectChance > 0 && player.getRandom().nextFloat() < reflectChance) {
                                        float reflectDamage = amount
                                                        * net.kankrittapon.rpgem.Config.REFLECTION_MULTIPLIER.get()
                                                                        .floatValue();
                                        livingAttacker.hurt(player.damageSources().thorns(player), reflectDamage);

                                        player.level().playSound(null, player.blockPosition(),
                                                        (net.minecraft.sounds.SoundEvent) SoundEvents.ANVIL_LAND,
                                                        SoundSource.PLAYERS,
                                                        0.5f, 0.5f);
                                        if (hasSavior) {
                                                player.displayClientMessage(
                                                                net.minecraft.network.chat.Component.literal(
                                                                                "§6<< DIVINE REFLECTION! (80%) >>"),
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
        public static void onPlayerTick(PlayerTickEvent.Post event) {
                Player player = event.getEntity();
                if (player.level().isClientSide())
                        return;

                // === SAVIOR AURA V2 ===
                if (player.hasEffect(ModMobEffects.BOUNDLESS_GRACE)) {
                        // Apply Evasion Buff (50%) directly via effect or check in damage event
                        // We also apply Aura to nearby enemies
                        if (player.level().getGameTime() % 20 == 0) { // Every second
                                List<Entity> nearby = player.level().getEntities(player,
                                                player.getBoundingBox().inflate(6.0));
                                for (Entity e : nearby) {
                                        if (e instanceof LivingEntity living && !(e instanceof Player)) {
                                                // 1. Slowness Aura
                                                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40,
                                                                -5)); // Slowness V

                                                // 2. Soul Purge (Anti-Heal Counter: Undying Trait)
                                                // Reduces mob regeneration drastically
                                                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 2));
                                                if (net.kankrittapon.rpgem.Config.ENABLE_L2_HOSTILITY_INTEGRATION.get()
                                                                && living.getPersistentData()
                                                                                .contains("L2Hostility_Traits")) {
                                                        // Reduce L2H regeneration if possible via NBT or status
                                                        living.getPersistentData()
                                                                        .putFloat("hostility:health_regen_mult", 0.0f);
                                                }

                                                // Spawn Particles
                                                ((ServerLevel) player.level()).sendParticles(ParticleTypes.SOUL,
                                                                living.getX(), living.getY() + 1.5, living.getZ(), 3,
                                                                0.2, 0.2, 0.2, 0.1);
                                        }
                                }
                        }

                        // === GOD OF ELEMENT (Counter: Adaptive Trait & High HP Mobs) ===
                        // Instead of cycling, we now trigger ALL elements at once when attacking
                        // See onLivingDamage for implementation.

                        // Passive Particles (God Mode Aura)
                        if (player.level() instanceof ServerLevel serverLevel
                                        && player.level().getGameTime() % 10 == 0) {
                                serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                                                player.getX(), player.getY() + 1.2, player.getZ(),
                                                3, 0.3, 0.5, 0.3, 0.05);
                                serverLevel.sendParticles(ParticleTypes.FLAME,
                                                player.getX(), player.getY() + 0.5, player.getZ(),
                                                1, 0.3, 0.1, 0.3, 0.02);
                        }
                }

                // === INVENTORY WEIGHT SYSTEM & FAIRY SKILL 4.3 ===
                if (player.level().getGameTime() % 20 == 0) { // Check every second
                        net.kankrittapon.rpgem.util.WeightHandler.tick(player);
                        int penalty = net.kankrittapon.rpgem.util.WeightHandler.getPenaltyLevel(player);
                        if (penalty > 0) {
                                if (penalty >= 3) {
                                        // Immobile
                                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 10,
                                                        false, false, true));
                                        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 40, 128, false, false,
                                                        true)); // Prevent Jump
                                        player.displayClientMessage(net.minecraft.network.chat.Component
                                                        .translatable("message.rpgem.weight_overburdened")
                                                        .withStyle(net.minecraft.ChatFormatting.RED), true);
                                } else if (penalty >= 2) {
                                        // Heavy Load (No Jump, Slow)
                                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 4,
                                                        false, false, true)); // Slowness V
                                        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 40, 128, false, false,
                                                        true)); // Prevent Jump
                                        player.displayClientMessage(
                                                        net.minecraft.network.chat.Component
                                                                        .translatable("message.rpgem.weight_heavy")
                                                                        .withStyle(net.minecraft.ChatFormatting.GOLD),
                                                        true);
                                } else {
                                        // Overweight (Slow)
                                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1,
                                                        false, false, true)); // Slowness II
                                        player.displayClientMessage(
                                                        net.minecraft.network.chat.Component
                                                                        .translatable("message.rpgem.weight_encumbered")
                                                                        .withStyle(net.minecraft.ChatFormatting.YELLOW),
                                                        true);
                                }
                        }
                }
        }

        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
                if (event.getEntity().level().isClientSide) {
                        return;
                }

                if (event.getEntity() instanceof Player player) {
                        // 1. FAIRY RESURRECTION (Skill 4.4)
                        if (!event.isCanceled()) {
                                java.util.List<net.kankrittapon.rpgem.entity.custom.FairyEntity> fairies = player
                                                .level().getEntitiesOfClass(
                                                                net.kankrittapon.rpgem.entity.custom.FairyEntity.class,
                                                                player.getBoundingBox().inflate(10.0),
                                                                f -> f.getOwnerUUID().isPresent() && f.getOwnerUUID()
                                                                                .get().equals(player.getUUID()));

                                if (!fairies.isEmpty()) {
                                        if (fairies.get(0).tryUseFairyTear(player)) {
                                                event.setCanceled(true);
                                                return; // Successfully Resurrected by Fairy
                                        }
                                }
                        }

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
                net.neoforged.neoforge.registries.DeferredHolder<net.minecraft.core.component.DataComponentType<?>, net.minecraft.core.component.DataComponentType<Integer>> upgradeLevelHolder = net.kankrittapon.rpgem.init.ModDataComponents.UPGRADE_LEVEL;
                if (upgradeLevelHolder.isBound() && stack.has(upgradeLevelHolder.get())) {
                        int level = stack.get(upgradeLevelHolder.get());
                        if (level > 0) {
                                boolean isWeapon = stack.getItem() instanceof SwordItem
                                                || stack.getItem() instanceof AxeItem;
                                boolean isArmor = stack.getItem() instanceof ArmorItem;

                                // === WEAPON STATS (Restructuring for Path) ===
                                if (isWeapon) {
                                        // 1. Attack Damage (All Levels)
                                        double damageBonus = 0;
                                        if (level <= 15)
                                                damageBonus = level * 0.25;
                                        else if (level <= 25)
                                                damageBonus = 3.75 + ((level - 15) * 0.75); // Moderate scaling
                                        else
                                                damageBonus = 3.75 + 7.5 + ((level - 25) * 2.0); // Late-game scaling

                                        event.addModifier(
                                                        net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE,
                                                        new AttributeModifier(
                                                                        ResourceLocation
                                                                                        .fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "upgrade_damage"),
                                                                        damageBonus,
                                                                        AttributeModifier.Operation.ADD_VALUE),
                                                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);

                                        // 2. Life Steal (Lv 1+)
                                        event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.LIFE_STEAL,
                                                        new AttributeModifier(
                                                                        ResourceLocation
                                                                                        .fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "upgrade_life_steal"),
                                                                        level * 0.005,
                                                                        AttributeModifier.Operation.ADD_VALUE),
                                                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);

                                        // 3. Crit Chance (Lv 16+)
                                        if (level >= 16) {
                                                event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.CRIT_CHANCE,
                                                                new AttributeModifier(
                                                                                ResourceLocation
                                                                                                .fromNamespaceAndPath(
                                                                                                                RPGEasyMode.MODID,
                                                                                                                "upgrade_crit_chance"),
                                                                                (level - 15) * 0.02, // 2% per level
                                                                                                     // after 15
                                                                                AttributeModifier.Operation.ADD_VALUE),
                                                                net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);
                                        }

                                        // 4. Element Damage (Lv 26+)
                                        if (level >= 26) {
                                                event.addModifier(
                                                                net.kankrittapon.rpgem.init.ModAttributes.ELEMENT_DAMAGE,
                                                                new AttributeModifier(
                                                                                ResourceLocation
                                                                                                .fromNamespaceAndPath(
                                                                                                                RPGEasyMode.MODID,
                                                                                                                "upgrade_element_damage"),
                                                                                (level - 25) * 0.05, // 5% per level
                                                                                                     // after 25
                                                                                AttributeModifier.Operation.ADD_VALUE),
                                                                net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);
                                        }

                                        // === Specialized Stats (Path) ===
                                        if (level >= 10) {
                                                // Accuracy (Lv.10+)
                                                double accuracy = (level - 9) * 0.02;
                                                event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.ACCURACY,
                                                                new AttributeModifier(
                                                                                ResourceLocation.fromNamespaceAndPath(
                                                                                                RPGEasyMode.MODID,
                                                                                                "forge_accuracy"),
                                                                                accuracy,
                                                                                AttributeModifier.Operation.ADD_VALUE),
                                                                net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);

                                                // Armor Penetration (% HP) (Lv.20+)
                                                if (level >= 20) {
                                                        double armorPen = (level - 19) * 0.005; // Max 0.05 (5%) at
                                                                                                // Lv.30
                                                        event.addModifier(
                                                                        net.kankrittapon.rpgem.init.ModAttributes.ARMOR_PENETRATION,
                                                                        new AttributeModifier(
                                                                                        ResourceLocation.fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "forge_armor_pen"),
                                                                                        armorPen,
                                                                                        AttributeModifier.Operation.ADD_VALUE),
                                                                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);

                                                        // Seal Resist (Lv.20+) - Moved from Armor to Weapon per User
                                                        // Feedback
                                                        double sealResist = (level - 19) * 0.05; // 5% per level -> Max
                                                                                                 // 55% at Lv.30
                                                        event.addModifier(
                                                                        net.kankrittapon.rpgem.init.ModAttributes.SEAL_RESIST,
                                                                        new AttributeModifier(
                                                                                        ResourceLocation.fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "forge_seal_resist"),
                                                                                        sealResist,
                                                                                        AttributeModifier.Operation.ADD_VALUE),
                                                                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);
                                                }

                                                // Anti-Heal (Lv.25+)
                                                if (level >= 25) {
                                                        double antiHeal = (level - 24) * 0.1; // Max 0.6 (60%) at Lv.30
                                                        event.addModifier(
                                                                        net.kankrittapon.rpgem.init.ModAttributes.ANTI_HEAL,
                                                                        new AttributeModifier(
                                                                                        ResourceLocation.fromNamespaceAndPath(
                                                                                                        RPGEasyMode.MODID,
                                                                                                        "forge_anti_heal"),
                                                                                        antiHeal,
                                                                                        AttributeModifier.Operation.ADD_VALUE),
                                                                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);
                                                }
                                        }
                                }

                                // === ARMOR STATS ===
                                if (isArmor && net.kankrittapon.rpgem.Config.ENABLE_L2_COMPLEMENTS_BALANCING.get()) {
                                        // 1. Defense (All Levels)
                                        double armorBonus = level * 0.25; // Balanced for L2 Complements (Total +30 for
                                                                          // full set at Lv30)
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
                                                event.addModifier(
                                                                net.kankrittapon.rpgem.init.ModAttributes.DAMAGE_REDUCTION,
                                                                new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                                net.minecraft.resources.ResourceLocation
                                                                                                .fromNamespaceAndPath(
                                                                                                                RPGEasyMode.MODID,
                                                                                                                "upgrade_dr"),
                                                                                level * 0.015, // 1.5% per level -> 45%
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
                                                                                level * 0.03, // 3% per level
                                                                                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                                                                net.minecraft.world.entity.EquipmentSlotGroup.ARMOR);
                                        } else if (path.equals("evasion")) {
                                                event.addModifier(net.kankrittapon.rpgem.init.ModAttributes.EVASION,
                                                                new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                                                                net.minecraft.resources.ResourceLocation
                                                                                                .fromNamespaceAndPath(
                                                                                                                RPGEasyMode.MODID,
                                                                                                                "upgrade_evasion"),
                                                                                level * 0.015, // 1.5% per level -> 45%
                                                                                               // at Lv30
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
                        // 1. Fragment (Common - 50%)
                        if (event.getEntity().getRandom().nextFloat() < 0.5f) {
                                int count = 1 + event.getEntity().getRandom().nextInt(3); // 1-3
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.PIECE_OF_HEART
                                                                                .get(),
                                                                count)));
                        }

                        // 2. Big Item (Rare - Config)
                        if (event.getEntity().getRandom()
                                        .nextDouble() < net.kankrittapon.rpgem.Config.ZOMBIE_KING_HEART_CHANCE.get()) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.ZOMBIE_HEART
                                                                                .get())));
                        }

                        // Tier 1 Stone (Guaranteed)
                        event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(event.getEntity().level(),
                                        event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                                        new net.minecraft.world.item.ItemStack(
                                                        net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_1
                                                                        .get())));

                        // Tier 2 Stone (50% Chance)
                        if (event.getEntity().getRandom().nextFloat() < 0.5f) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_2
                                                                                .get())));
                        }
                } else if (event.getEntity() instanceof net.kankrittapon.rpgem.entity.custom.SkeletonLord) {
                        // 1. Fragment (Common - 50%)
                        if (event.getEntity().getRandom().nextFloat() < 0.5f) {
                                int count = 1 + event.getEntity().getRandom().nextInt(3); // 1-3
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.PIECE_OF_BONE
                                                                                .get(),
                                                                count)));
                        }

                        // 2. Big Item (Rare - Config)
                        if (event.getEntity().getRandom()
                                        .nextDouble() < net.kankrittapon.rpgem.Config.SKELETON_LORD_BONE_CHANCE.get()) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.BONE_OF_MAZE
                                                                                .get())));
                        }

                        // Tier 2 Stone (Guaranteed)
                        event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(event.getEntity().level(),
                                        event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                                        new net.minecraft.world.item.ItemStack(
                                                        net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_2
                                                                        .get())));

                        // Tier 3 Stone (30% Chance)
                        if (event.getEntity().getRandom().nextFloat() < 0.3f) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_3
                                                                                .get())));
                        }
                } else if (event.getEntity() instanceof net.minecraft.world.entity.monster.warden.Warden) {
                        // 1. Fragment (Common - 50%)
                        if (event.getEntity().getRandom().nextFloat() < 0.5f) {
                                int count = 1 + event.getEntity().getRandom().nextInt(3); // 1-3
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.PIECE_OF_COSMIC_EMERALD
                                                                                .get(),
                                                                count)));
                        }

                        // 2. Big Item (Rare - Config)
                        if (event.getEntity().getRandom()
                                        .nextDouble() < net.kankrittapon.rpgem.Config.WARDEN_COSMIC_CHANCE.get()) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.COSMIC_EMERALD
                                                                                .get())));
                        }
                } else if (event.getEntity() instanceof net.minecraft.world.entity.monster.Zombie) {
                        // Zombie (Normal)
                        // 1. Fragment (Common - 25%)
                        if (event.getEntity().getRandom().nextFloat() < 0.25f) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.PIECE_OF_HEART
                                                                                .get())));
                        }
                        // 2. Big Item (Rare - Config)
                        if (event.getEntity().getRandom()
                                        .nextDouble() < net.kankrittapon.rpgem.Config.ZOMBIE_HEART_CHANCE
                                                        .get()) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.ZOMBIE_HEART
                                                                                .get())));
                        }
                } else if (event.getEntity() instanceof net.minecraft.world.entity.monster.AbstractSkeleton) {
                        // Skeleton (Normal)
                        // 1. Fragment (Common - 25%)
                        if (event.getEntity().getRandom().nextFloat() < 0.25f) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.PIECE_OF_BONE
                                                                                .get())));
                        }
                        // 2. Big Item (Rare - Config)
                        if (event.getEntity().getRandom()
                                        .nextDouble() < net.kankrittapon.rpgem.Config.SKELETON_BONE_CHANCE
                                                        .get()) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.BONE_OF_MAZE
                                                                                .get())));
                        }
                } else if (event.getEntity() instanceof net.minecraft.world.entity.monster.EnderMan
                                || event.getEntity() instanceof net.minecraft.world.entity.monster.Witch) {
                        // Enderman / Witch (Normal)
                        // 1. Fragment (Common - 25%)
                        if (event.getEntity().getRandom().nextFloat() < 0.25f) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.PIECE_OF_COSMIC_EMERALD
                                                                                .get())));
                        }
                        // 2. Big Item (Rare - Config)
                        if (event.getEntity().getRandom().nextDouble() < net.kankrittapon.rpgem.Config.MOB_COSMIC_CHANCE
                                        .get()) {
                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                event.getEntity().level(),
                                                event.getEntity().getX(), event.getEntity().getY(),
                                                event.getEntity().getZ(),
                                                new net.minecraft.world.item.ItemStack(
                                                                net.kankrittapon.rpgem.init.ModItems.COSMIC_EMERALD
                                                                                .get())));
                        }
                } else {
                        // === APOTHEOSIS BOSS DROPS ===
                        if (net.kankrittapon.rpgem.Config.ENABLE_APOTHEOSIS_INTEGRATION.get()) {
                                LivingEntity entity = event.getEntity();
                                if (entity.getPersistentData().contains("apotheosis:boss") ||
                                                entity.getPersistentData().contains("apotheosis:is_boss")) {

                                        // Tier 2/3 Stone for Apotheosis Bosses
                                        if (entity.getRandom().nextFloat() < 0.2f) { // 20% Chance for Apotheosis Bosses
                                                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                                                                entity.level(), entity.getX(), entity.getY(),
                                                                entity.getZ(),
                                                                new net.minecraft.world.item.ItemStack(
                                                                                net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_2
                                                                                                .get())));
                                        }
                                }
                        }
                }
        }

        @SubscribeEvent
        public static void onLivingDeathApocalypse(LivingDeathEvent event) {
                if (event.getEntity().level().isClientSide) {
                        return;
                }

                if (event.getEntity() instanceof net.minecraft.world.entity.monster.Zombie zombie
                                && !(zombie instanceof net.minecraft.world.entity.monster.ZombifiedPiglin)) {
                        if (event.getEntity().level().random.nextFloat() < 0.05f) { // 5% Chance
                                ServerLevel level = (ServerLevel) event.getEntity().level();
                                // Spawn Horde
                                for (int i = 0; i < 10; i++) {
                                        net.minecraft.world.entity.monster.Zombie reinforcement = net.minecraft.world.entity.EntityType.ZOMBIE
                                                        .create(level);
                                        if (reinforcement != null) {
                                                reinforcement.setPos(
                                                                zombie.getX() + (level.random.nextDouble() - 0.5) * 5,
                                                                zombie.getY(),
                                                                zombie.getZ() + (level.random.nextDouble() - 0.5) * 5);
                                                level.addFreshEntity(reinforcement);
                                        }
                                }
                                // Notify players nearby? (Optional)
                        }
                } else if (event.getEntity() instanceof net.minecraft.world.entity.monster.Skeleton skeleton) {
                        if (event.getEntity().level().random.nextFloat() < 0.05f) { // 5% Chance
                                ServerLevel level = (ServerLevel) event.getEntity().level();
                                // Spawn Skeleton Horseman
                                net.minecraft.world.entity.animal.horse.SkeletonHorse horse = net.minecraft.world.entity.EntityType.SKELETON_HORSE
                                                .create(level);
                                net.minecraft.world.entity.monster.Skeleton rider = net.minecraft.world.entity.EntityType.SKELETON
                                                .create(level);
                                if (horse != null && rider != null) {
                                        horse.setPos(skeleton.getX(), skeleton.getY(), skeleton.getZ());
                                        horse.setTamed(true);

                                        rider.setPos(skeleton.getX(), skeleton.getY(), skeleton.getZ());
                                        // Gear
                                        rider.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                                                        new ItemStack(net.minecraft.world.item.Items.BOW));
                                        rider.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD,
                                                        new ItemStack(net.minecraft.world.item.Items.DIAMOND_HELMET));
                                        rider.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, new ItemStack(
                                                        net.minecraft.world.item.Items.DIAMOND_CHESTPLATE));

                                        rider.startRiding(horse);
                                        level.addFreshEntity(horse);
                                        level.addFreshEntity(rider);
                                }
                        }
                } else if (event.getEntity() instanceof net.minecraft.world.entity.monster.EnderMan enderman) {
                        if (event.getEntity().level().random.nextFloat() < 0.05f) { // 5% Chance
                                ServerLevel level = (ServerLevel) event.getEntity().level();
                                // Spawn Swarm
                                for (int i = 0; i < 5; i++) {
                                        net.minecraft.world.entity.monster.EnderMan reinforcement = net.minecraft.world.entity.EntityType.ENDERMAN
                                                        .create(level);
                                        if (reinforcement != null) {
                                                reinforcement.setPos(
                                                                enderman.getX() + (level.random.nextDouble() - 0.5) * 8,
                                                                enderman.getY(),
                                                                enderman.getZ() + (level.random.nextDouble() - 0.5)
                                                                                * 8);
                                                level.addFreshEntity(reinforcement);
                                        }
                                }
                        }
                }
        }

        @SubscribeEvent
        public static void onVillagerTrades(net.neoforged.neoforge.event.village.VillagerTradesEvent event) {
                if (event.getType() == net.minecraft.world.entity.npc.VillagerProfession.CLERIC) {
                        // Level 5 (Master)
                        var trades = event.getTrades().get(5);

                }
        }

        @SubscribeEvent
        public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
                if (event.getLevel().isClientSide)
                        return;

                if (event.getTarget() instanceof net.minecraft.world.entity.npc.Villager villager) {
                        if (villager.getVillagerData().getProfession() == ModVillagers.ALCHEMIST.get()) {
                                Player player = event.getEntity();
                                ItemStack heldItem = player.getMainHandItem();

                                // === 1. FRAGMENT EXCHANGE (100 -> 1 Big Item) ===
                                if (heldItem.is(ModItems.PIECE_OF_HEART.get())) {
                                        if (getInventoryItemCount(player, ModItems.PIECE_OF_HEART.get()) >= 100) {
                                                consumeItemFromInventory(player, ModItems.PIECE_OF_HEART.get(), 100);
                                                player.addItem(new ItemStack(ModItems.ZOMBIE_HEART.get()));
                                                playExchangeSound(player);
                                                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                                                                "§6[Alchemist]§e A Zombie Heart! Pure vitality condensed."));
                                                event.setCanceled(true);
                                                event.setCancellationResult(InteractionResult.SUCCESS);
                                                return;
                                        }
                                } else if (heldItem.is(ModItems.PIECE_OF_BONE.get())) {
                                        if (getInventoryItemCount(player, ModItems.PIECE_OF_BONE.get()) >= 100) {
                                                consumeItemFromInventory(player, ModItems.PIECE_OF_BONE.get(), 100);
                                                player.addItem(new ItemStack(ModItems.BONE_OF_MAZE.get()));
                                                playExchangeSound(player);
                                                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                                                                "§6[Alchemist]§e The Bone of Maze... structure and resilience."));
                                                event.setCanceled(true);
                                                event.setCancellationResult(InteractionResult.SUCCESS);
                                                return;
                                        }
                                } else if (heldItem.is(ModItems.PIECE_OF_COSMIC_EMERALD.get())) {
                                        if (getInventoryItemCount(player,
                                                        ModItems.PIECE_OF_COSMIC_EMERALD.get()) >= 100) {
                                                consumeItemFromInventory(player, ModItems.PIECE_OF_COSMIC_EMERALD.get(),
                                                                100);
                                                player.addItem(new ItemStack(ModItems.COSMIC_EMERALD.get()));
                                                playExchangeSound(player);
                                                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                                                                "§6[Alchemist]§e A Cosmic Emerald! The energy of the universe."));
                                                event.setCanceled(true);
                                                event.setCancellationResult(InteractionResult.SUCCESS);
                                                return;
                                        }
                                }

                                // === 2. POTION INFUSION (Dynamic Upgrade) ===
                                if (heldItem.is(ModItems.ETHERNAL_BOTTLE.get())
                                                || heldItem.getItem() instanceof net.kankrittapon.rpgem.item.SequentialInfinitePotion) {

                                        List<String> history = getIngredientHistory(heldItem);
                                        ItemStack nextPotion = ItemStack.EMPTY;

                                        // Determine Next Tier
                                        if (heldItem.is(ModItems.ETHERNAL_BOTTLE.get())) {
                                                nextPotion = new ItemStack(ModItems.INFINITE_POTION_TIER_1.get());
                                        } else if (heldItem.is(ModItems.INFINITE_POTION_TIER_1.get())) {
                                                nextPotion = new ItemStack(ModItems.INFINITE_POTION_TIER_2.get());
                                        } else if (heldItem.is(ModItems.INFINITE_POTION_TIER_2.get())) {
                                                nextPotion = new ItemStack(ModItems.INFINITE_POTION_TIER_3.get());
                                        } else {
                                                // Already Max Tier (Tier 3) -> Cannot upgrade further via this method
                                                return;
                                        }

                                        // Scan Inventory for Compatible Big Items
                                        // Priority: Heart (H) -> Bone (B) -> Cosmic (C) (Just for checking order)
                                        String ingredientCode = null;
                                        net.minecraft.world.item.Item ingredientItem = null;

                                        if (!history.contains("H")
                                                        && hasItemInInventory(player, ModItems.ZOMBIE_HEART.get())) {
                                                ingredientCode = "H";
                                                ingredientItem = ModItems.ZOMBIE_HEART.get();
                                        } else if (!history.contains("B")
                                                        && hasItemInInventory(player, ModItems.BONE_OF_MAZE.get())) {
                                                ingredientCode = "B";
                                                ingredientItem = ModItems.BONE_OF_MAZE.get();
                                        } else if (!history.contains("C")
                                                        && hasItemInInventory(player, ModItems.COSMIC_EMERALD.get())) {
                                                ingredientCode = "C";
                                                ingredientItem = ModItems.COSMIC_EMERALD.get();
                                        }

                                        // Execute Infusion
                                        if (ingredientCode != null && ingredientItem != null) {
                                                // 1. Consume Big Item
                                                consumeItemFromInventory(player, ingredientItem, 1);

                                                // 2. Update NBT History
                                                history.add(ingredientCode);
                                                applyIngredientHistory(nextPotion, history);

                                                // 3. Replace Item
                                                heldItem.shrink(1);
                                                player.addItem(nextPotion);

                                                // 4. Feedback
                                                player.level().playSound(null, player.blockPosition(),
                                                                SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS,
                                                                1.0f, 1.0f);
                                                player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                                                                "§6[Alchemist]§a Infusion Successful! The potion evolves..."));

                                                event.setCanceled(true);
                                                event.setCancellationResult(InteractionResult.SUCCESS);
                                        } else {
                                                if (heldItem.is(ModItems.ETHERNAL_BOTTLE.get())) {
                                                        player.sendSystemMessage(net.minecraft.network.chat.Component
                                                                        .literal("§6[Alchemist]§c I need a powerful catalyst (Heart, Bone, or Cosmic Emerald) to activate this bottle."));
                                                } else {
                                                        player.sendSystemMessage(net.minecraft.network.chat.Component
                                                                        .literal("§6[Alchemist]§c You need a new catalyst (Heart, Bone, or Cosmic Emerald) that hasn't been used yet!"));
                                                }
                                                // Don't cancel event to allow normal specific interactions if needed,
                                                // allows trading GUI to open if no valid infusion
                                        }
                                }
                        }
                }
        }

        private static void playExchangeSound(Player player) {
                player.level().playSound(null, player.blockPosition(), SoundEvents.VILLAGER_WORK_CLERIC,
                                SoundSource.PLAYERS, 1.0f, 1.0f);
        }

        // === NBT HELPERS ===
        private static List<String> getIngredientHistory(ItemStack stack) {
                List<String> history = new ArrayList<>();
                if (stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)) {
                        net.minecraft.world.item.component.CustomData customData = stack
                                        .get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
                        net.minecraft.nbt.CompoundTag tag = customData.copyTag();
                        if (tag.contains("IngredientHistory")) {
                                net.minecraft.nbt.ListTag list = tag.getList("IngredientHistory", 8); // 8 = String
                                for (int i = 0; i < list.size(); i++) {
                                        history.add(list.getString(i));
                                }
                        }
                }
                return history;
        }

        private static void applyIngredientHistory(ItemStack stack, List<String> history) {
                net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
                net.minecraft.nbt.ListTag listTag = new net.minecraft.nbt.ListTag();
                for (String s : history) {
                        listTag.add(net.minecraft.nbt.StringTag.valueOf(s));
                }
                tag.put("IngredientHistory", listTag);
                stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                                net.minecraft.world.item.component.CustomData.of(tag));
        }

        // === INVENTORY HELPERS ===
        private static int getInventoryItemCount(Player player, net.minecraft.world.item.Item item) {
                int count = 0;
                for (ItemStack stack : player.getInventory().items) {
                        if (stack.is(item)) {
                                count += stack.getCount();
                        }
                }
                return count;
        }

        private static boolean hasItemInInventory(Player player, net.minecraft.world.item.Item item) {
                for (ItemStack stack : player.getInventory().items) {
                        if (stack.is(item))
                                return true;
                }
                return false;
        }

        private static void consumeItemFromInventory(Player player, net.minecraft.world.item.Item item, int count) {
                int remaining = count;
                for (ItemStack stack : player.getInventory().items) {
                        if (stack.is(item)) {
                                int take = Math.min(stack.getCount(), remaining);
                                stack.shrink(take);
                                remaining -= take;
                                if (remaining <= 0)
                                        break;
                        }
                }
        }

        @SubscribeEvent
        public static void onItemTooltip(net.neoforged.neoforge.event.entity.player.ItemTooltipEvent event) {
                net.minecraft.world.item.ItemStack stack = event.getItemStack();
                if (stack.isDamageableItem()) {
                        int max = stack.getMaxDamage();
                        int current = max - stack.getDamageValue();
                        // Color based on percentage
                        net.minecraft.ChatFormatting color = net.minecraft.ChatFormatting.GREEN;
                        float percent = (float) current / max;
                        if (percent < 0.5f)
                                color = net.minecraft.ChatFormatting.YELLOW;
                        if (percent < 0.2f)
                                color = net.minecraft.ChatFormatting.RED;

                        event.getToolTip().add(net.minecraft.network.chat.Component
                                        .literal("Durability: " + current + " / " + max).withStyle(color));
                }
        }
}
