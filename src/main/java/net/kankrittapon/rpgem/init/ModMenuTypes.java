package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU,
            RPGEasyMode.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<net.kankrittapon.rpgem.menu.AlchemyTableMenu>> ALCHEMY_TABLE_MENU = MENUS
            .register("alchemy_table_menu",
                    () -> IMenuTypeExtension.create(
                            net.kankrittapon.rpgem.menu.AlchemyTableMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<net.kankrittapon.rpgem.menu.AncientForgeMenu>> ANCIENT_FORGE_MENU = MENUS
            .register("ancient_forge_menu",
                    () -> IMenuTypeExtension.create(net.kankrittapon.rpgem.menu.AncientForgeMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<net.kankrittapon.rpgem.menu.FairyMenu>> FAIRY_MENU = MENUS
            .register("fairy_menu",
                    () -> IMenuTypeExtension.create(net.kankrittapon.rpgem.menu.FairyMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
