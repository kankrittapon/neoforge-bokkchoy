package net.kankrittapon.rpgem.compat.jei;

import net.kankrittapon.rpgem.init.ModItems;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public record AlchemyTableRecipe(ItemStack input, ItemStack ingredient, ItemStack output) {

    public static List<AlchemyTableRecipe> getRecipes() {
        List<AlchemyTableRecipe> recipes = new ArrayList<>();

        // Potion Path
        recipes.add(new AlchemyTableRecipe(new ItemStack(ModItems.ETHERNAL_BOTTLE.get()),
                new ItemStack(ModItems.ZOMBIE_HEART.get()), new ItemStack(ModItems.INFINITE_POTION_TIER_1.get())));
        recipes.add(new AlchemyTableRecipe(new ItemStack(ModItems.INFINITE_POTION_TIER_1.get()),
                new ItemStack(ModItems.BONE_OF_MAZE.get()), new ItemStack(ModItems.INFINITE_POTION_TIER_2.get())));
        recipes.add(new AlchemyTableRecipe(new ItemStack(ModItems.INFINITE_POTION_TIER_2.get()),
                new ItemStack(ModItems.COSMIC_EMERALD.get()), new ItemStack(ModItems.INFINITE_POTION_TIER_3.get())));

        // Forged Stone Path
        recipes.add(new AlchemyTableRecipe(new ItemStack(ModItems.UPGRADE_STONE_TIER_2.get()),
                new ItemStack(ModItems.ZOMBIE_HEART.get()), new ItemStack(ModItems.FORGED_STONE_FORTITUDE.get())));
        recipes.add(new AlchemyTableRecipe(new ItemStack(ModItems.UPGRADE_STONE_TIER_2.get()),
                new ItemStack(ModItems.BONE_OF_MAZE.get()), new ItemStack(ModItems.FORGED_STONE_AGILITY.get())));
        recipes.add(new AlchemyTableRecipe(new ItemStack(ModItems.UPGRADE_STONE_TIER_2.get()),
                new ItemStack(ModItems.COSMIC_EMERALD.get()), new ItemStack(ModItems.FORGED_STONE_DESTRUCTION.get())));

        // Ultimate Forged Stone Path
        recipes.add(new AlchemyTableRecipe(new ItemStack(ModItems.FORGED_STONE_FORTITUDE.get()),
                new ItemStack(ModItems.UPGRADE_STONE_TIER_3.get()),
                new ItemStack(ModItems.FORGED_STONE_ULTIMATE_FORTITUDE.get())));
        recipes.add(new AlchemyTableRecipe(new ItemStack(ModItems.FORGED_STONE_AGILITY.get()),
                new ItemStack(ModItems.UPGRADE_STONE_TIER_3.get()),
                new ItemStack(ModItems.FORGED_STONE_ULTIMATE_AGILITY.get())));
        recipes.add(new AlchemyTableRecipe(new ItemStack(ModItems.FORGED_STONE_DESTRUCTION.get()),
                new ItemStack(ModItems.UPGRADE_STONE_TIER_3.get()),
                new ItemStack(ModItems.FORGED_STONE_ULTIMATE_DESTRUCTION.get())));

        return recipes;
    }
}
