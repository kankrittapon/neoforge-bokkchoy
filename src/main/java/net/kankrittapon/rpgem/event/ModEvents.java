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
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.List;

@EventBusSubscriber(modid = RPGEasyMode.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

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
}
