package net.kankrittapon.rpgem.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.init.ModBlocks;
import net.kankrittapon.rpgem.init.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@JeiPlugin
public class JEIRPGEasyModePlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID,
            "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AlchemyTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AncientForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(AlchemyTableRecipeCategory.TYPE, AlchemyTableRecipe.getRecipes());
        registration.addRecipes(AncientForgeRecipeCategory.TYPE, AncientForgeRecipe.getRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ALCHEMY_TABLE.get()), AlchemyTableRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ANCIENT_FORGE_TABLE.get()),
                AncientForgeRecipeCategory.TYPE);
    }
}
