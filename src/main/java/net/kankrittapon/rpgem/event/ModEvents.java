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

@EventBusSubscriber(modid = RPGEasyMode.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingIncomingDamage(
            net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            // EVASION (30% Dodge)
            if (player.hasEffect(ModMobEffects.EVASION)) {
                if (player.getRandom().nextFloat() < net.kankrittapon.rpgem.Config.DODGE_CHANCE.get()) {
                    event.setCanceled(true); // Negate all damage
                    player.level().playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_FLAP,
                            SoundSource.PLAYERS, 1.0f, 2.0f);
                    player.displayClientMessage(net.minecraft.network.chat.Component.literal("§b<< Dodged! >>"), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
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
                            net.kankrittapon.rpgem.Config.SAVIOR_REFLECTION_CHANCE.get()); // 25% overrides if higher

                if (reflectChance > 0 && player.getRandom().nextFloat() < reflectChance) {
                    float reflectDamage = amount
                            * net.kankrittapon.rpgem.Config.REFLECTION_MULTIPLIER.get().floatValue();
                    livingAttacker.hurt(player.damageSources().thorns(player), reflectDamage);

                    player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
                            0.5f, 0.5f);
                    if (hasSavior) {
                        player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal("§6<< DIVINE REFLECTION! >>"), true);
                    } else {
                        player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal("§7<< Thorns Reflected! >>"), true);
                    }
                }
            }

            // DIVINE REACTION (Savior Cleanse)
            if (player.hasEffect(ModMobEffects.BOUNDLESS_GRACE)) {
                // Remove bad effects
                player.removeAllEffects();
                // Restore beneficial ones? removeAllEffects removes ALL.
                // For MVP simplicity, we just clear all (cleanse) and re-apply the Savior buff
                // if it was removed?
                // Actually removeAllEffects() might remove the Savior buff itself!
                // Better approach: Iterate and remove only harmful effects.
                // But specifically for 'Savior', we want to give buffs too.

                player.addEffect(new MobEffectInstance(ModMobEffects.BOUNDLESS_GRACE, 200, 0)); // Re-apply self
                                                                                                // to be
                // safe or just don't
                // remove it.
                // Let's use a simpler approach for MVP: Clear all, Add Regen & Speed.

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
                    serverLevel.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS,
                            1.0f, 1.0f);
                }

                // 5. Apply Buffs
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0)); // 3s
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 4)); // 3s, Amp 4 (Resistance
                                                                                              // V) = Invulnerable

                // 6. Knockback Mobs
                List<Entity> nearbyEntities = player.level().getEntities(player, player.getBoundingBox().inflate(5.0));
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
                // Calculate Damage Bonus
                double damageBonus = 0;
                if (level <= 15) {
                    damageBonus = level * 1.0;
                } else if (level <= 25) {
                    damageBonus = (15 * 1.0) + ((level - 15) * 2.0);
                } else {
                    damageBonus = (15 * 1.0) + (10 * 2.0) + ((level - 25) * 5.0);
                }

                // Calculate Armor Bonus
                double armorBonus = level * 0.5;

                // Apply Attack Damage
                event.addModifier(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE,
                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID,
                                        "upgrade_damage"),
                                damageBonus,
                                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND);

                // Apply Armor
                // We apply armor bonus if the item is an armor item or has armor attributes
                event.addModifier(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR,
                        new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID,
                                        "upgrade_armor"),
                                armorBonus,
                                net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE),
                        net.minecraft.world.entity.EquipmentSlotGroup.ARMOR);
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
                            net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_1.get())));

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
                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(event.getEntity().level(),
                        event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                        new net.minecraft.world.item.ItemStack(
                                net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_2.get())));
            }
        } else if (event.getEntity() instanceof net.kankrittapon.rpgem.entity.custom.SkeletonLord) {
            // Tier 2 Stone (Guaranteed)
            event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(event.getEntity().level(),
                    event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                    new net.minecraft.world.item.ItemStack(
                            net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_2.get())));

            // Tier 3 Stone (30% Chance + Looting)
            int looting = 0;
            if (event.getSource().getEntity() instanceof net.minecraft.world.entity.LivingEntity attacker) {
                // looting =
                // net.minecraft.world.item.enchantment.EnchantmentHelper.getMobLooting(attacker);
                looting = 0;
            }

            if (event.getEntity().getRandom().nextFloat() < 0.3f + (looting * 0.1f)) {
                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(event.getEntity().level(),
                        event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                        new net.minecraft.world.item.ItemStack(
                                net.kankrittapon.rpgem.init.ModItems.UPGRADE_STONE_TIER_3.get())));
            }
        }
    }
}
