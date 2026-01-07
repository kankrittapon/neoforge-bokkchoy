package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, RPGEasyMode.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RPGEM_TAB = CREATIVE_MODE_TABS
            .register("rpgem_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.rpgem"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.COSMIC_EMERALD.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Items
                        output.accept(ModItems.BONE_OF_MAZE.get());
                        output.accept(ModItems.COSMIC_EMERALD.get());
                        output.accept(ModItems.ETHERNAL_BOTTLE.get());
                        output.accept(ModItems.ZOMBIE_HEART.get());

                        // Potions
                        output.accept(ModItems.INFINITE_POTION_TIER_1.get());
                        output.accept(ModItems.INFINITE_POTION_TIER_2.get());
                        output.accept(ModItems.INFINITE_POTION_TIER_3.get());

                        // Blocks
                        output.accept(ModBlocks.ALCHEMY_TABLE.get());
                        output.accept(ModBlocks.ANCIENT_FORGE_TABLE.get());
                        output.accept(ModBlocks.TOME_OF_FORGOTTEN_TABLE.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
