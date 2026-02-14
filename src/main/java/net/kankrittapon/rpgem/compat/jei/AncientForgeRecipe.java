package net.kankrittapon.rpgem.compat.jei;

import net.kankrittapon.rpgem.init.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.ArrayList;
import java.util.List;

public record AncientForgeRecipe(ItemStack equipment, ItemStack stone, String description) {

    public static List<AncientForgeRecipe> getRecipes() {
        List<AncientForgeRecipe> recipes = new ArrayList<>();

        // Tier 1 Upgrades
        recipes.add(new AncientForgeRecipe(new ItemStack(Items.DIAMOND_SWORD),
                new ItemStack(ModItems.UPGRADE_STONE_TIER_1.get()), "Upgrade Level: +1 to +15 (70% Success)"));

        // Tier 2 Upgrades
        recipes.add(new AncientForgeRecipe(new ItemStack(Items.DIAMOND_SWORD),
                new ItemStack(ModItems.UPGRADE_STONE_TIER_2.get()), "Upgrade Level: I to X (40% Success)"));

        // Tier 3 Upgrades
        recipes.add(new AncientForgeRecipe(new ItemStack(Items.DIAMOND_SWORD),
                new ItemStack(ModItems.UPGRADE_STONE_TIER_3.get()), "Upgrade Level: Final Tier (10% Success)"));

        // Path Application (Armor)
        recipes.add(new AncientForgeRecipe(new ItemStack(Items.DIAMOND_CHESTPLATE),
                new ItemStack(ModItems.FORGED_STONE_FORTITUDE.get()), "Path: REDUCTION (+DR, +Reflect Resist)"));
        recipes.add(new AncientForgeRecipe(new ItemStack(Items.DIAMOND_CHESTPLATE),
                new ItemStack(ModItems.FORGED_STONE_AGILITY.get()), "Path: EVASION (+Evasion)"));

        return recipes;
    }
}
