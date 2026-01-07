package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.screen.AlchemyTableMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU,
            RPGEasyMode.MODID);

    public static final Supplier<MenuType<AlchemyTableMenu>> ALCHEMY_TABLE_MENU = MENUS.register("alchemy_table_menu",
            () -> IMenuTypeExtension.create(AlchemyTableMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
