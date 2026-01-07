package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT,
            RPGEasyMode.MODID);

    public static final Holder<MobEffect> BOUNDLESS_GRACE = MOB_EFFECTS.register("boundless_grace",
            () -> new BoundlessGraceEffect(MobEffectCategory.BENEFICIAL, 0xFFD700)); // Gold Color

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

    // Simple Effect Class
    public static class BoundlessGraceEffect extends MobEffect {
        protected BoundlessGraceEffect(MobEffectCategory category, int color) {
            super(category, color);
        }
    }
}
