package net.kankrittapon.rpgem.loot;

import com.mojang.serialization.MapCodec;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModLootModifiers {
    // Register the Serializer, not the Modifier instance itself
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister
            .create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RPGEasyMode.MODID);

    public static final Supplier<MapCodec<AddItemModifier>> ADD_ITEM = LOOT_MODIFIER_SERIALIZERS.register("add_item",
            () -> AddItemModifier.CODEC);

    public static final Supplier<MapCodec<ScaledItemModifier>> SCALED_ITEM = LOOT_MODIFIER_SERIALIZERS.register(
            "scaled_item",
            () -> ScaledItemModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
