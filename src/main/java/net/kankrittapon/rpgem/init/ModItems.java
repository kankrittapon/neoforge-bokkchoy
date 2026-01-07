package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RPGEasyMode.MODID);

    public static final DeferredItem<Item> BONE_OF_MAZE = ITEMS.registerSimpleItem("bone_of_maze",
            new Item.Properties());
    public static final DeferredItem<Item> COSMIC_EMERALD = ITEMS.registerSimpleItem("cosmic_emerald",
            new Item.Properties());
    public static final DeferredItem<Item> ETHERNAL_BOTTLE = ITEMS.registerSimpleItem("ethernal_bottle",
            new Item.Properties());
    public static final DeferredItem<Item> ZOMBIE_HEART = ITEMS.registerSimpleItem("zombie_heart",
            new Item.Properties());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
