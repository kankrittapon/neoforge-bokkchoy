package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
            .createDataComponents(RPGEasyMode.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> UPGRADE_LEVEL = register(
            "upgrade_level",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
            UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENTS.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}
