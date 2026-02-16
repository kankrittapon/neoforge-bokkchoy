package net.kankrittapon.rpgem.init;

import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.item.custom.FairyWingItem;
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
        public static final DeferredItem<FairyWingItem> FAIRY_WING = ITEMS.register("fairy_wing",
                        () -> new FairyWingItem(new Item.Properties().stacksTo(1)));

        // Repair Items
        public static final DeferredItem<Item> MEMORY_FRAGMENT = ITEMS.registerSimpleItem("memory_fragment",
                        new Item.Properties());
        public static final DeferredItem<Item> PROTECTION_STONE = ITEMS.registerSimpleItem("protection_stone",
                        new Item.Properties());
        public static final DeferredItem<Item> ARTISANS_MEMORY = ITEMS.registerSimpleItem("artisans_memory",
                        new Item.Properties());

        // Quest Items (Fragments)
        public static final DeferredItem<Item> PIECE_OF_HEART = ITEMS.registerSimpleItem("piece_of_heart",
                        new Item.Properties());
        public static final DeferredItem<Item> PIECE_OF_BONE = ITEMS.registerSimpleItem("piece_of_bone",
                        new Item.Properties());
        public static final DeferredItem<Item> PIECE_OF_COSMIC_EMERALD = ITEMS.registerSimpleItem(
                        "piece_of_cosmic_emerald",
                        new Item.Properties());

        // Upgrade Stones
        public static final DeferredItem<Item> UPGRADE_STONE_TIER_1 = ITEMS.registerSimpleItem("upgrade_stone_tier_1",
                        new Item.Properties());
        public static final DeferredItem<Item> UPGRADE_STONE_TIER_2 = ITEMS.registerSimpleItem("upgrade_stone_tier_2",
                        new Item.Properties());
        public static final DeferredItem<Item> UPGRADE_STONE_TIER_3 = ITEMS.registerSimpleItem("upgrade_stone_tier_3",
                        new Item.Properties());

        // Forged Stones — Tier 2 (I ~ X)
        public static final DeferredItem<Item> FORGED_STONE_FORTITUDE = ITEMS.registerSimpleItem(
                        "forged_stone_fortitude", new Item.Properties());
        public static final DeferredItem<Item> FORGED_STONE_AGILITY = ITEMS.registerSimpleItem(
                        "forged_stone_agility", new Item.Properties());
        public static final DeferredItem<Item> FORGED_STONE_DESTRUCTION = ITEMS.registerSimpleItem(
                        "forged_stone_destruction", new Item.Properties());

        // Forged Stones — Ultimate
        public static final DeferredItem<Item> FORGED_STONE_ULTIMATE_FORTITUDE = ITEMS.registerSimpleItem(
                        "forged_stone_ultimate_fortitude", new Item.Properties());
        public static final DeferredItem<Item> FORGED_STONE_ULTIMATE_AGILITY = ITEMS.registerSimpleItem(
                        "forged_stone_ultimate_agility", new Item.Properties());
        public static final DeferredItem<Item> FORGED_STONE_ULTIMATE_DESTRUCTION = ITEMS.registerSimpleItem(
                        "forged_stone_ultimate_destruction", new Item.Properties());

        public static final DeferredItem<Item> INFINITE_POTION_TIER_1 = ITEMS.register("infinite_potion_tier_1",
                        () -> new net.kankrittapon.rpgem.item.SequentialInfinitePotion(new Item.Properties()));
        public static final DeferredItem<Item> INFINITE_POTION_TIER_2 = ITEMS.register("infinite_potion_tier_2",
                        () -> new net.kankrittapon.rpgem.item.SequentialInfinitePotion(new Item.Properties()));
        public static final DeferredItem<Item> INFINITE_POTION_TIER_3 = ITEMS.register("infinite_potion_tier_3",
                        () -> new net.kankrittapon.rpgem.item.SequentialInfinitePotion(new Item.Properties()));

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }
}
