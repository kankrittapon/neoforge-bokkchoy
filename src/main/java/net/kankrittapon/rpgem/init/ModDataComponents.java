package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.core.component.DataComponentType;
import com.mojang.serialization.Codec;
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
                        builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT)
                                        .networkSynchronized(ByteBufCodecs.VAR_INT));

        /**
         * Armor upgrade path: "none", "reduction", or "evasion". Set at +6 with first
         * Forged Stone use.
         */
        public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ARMOR_PATH = register(
                        "armor_path",
                        builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

        public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ORIGINAL_NAME = register(
                        "original_name",
                        builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

        private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                        UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
                return DATA_COMPONENTS.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
        }

        public static void register(IEventBus eventBus) {
                DATA_COMPONENTS.register(eventBus);
        }
}
