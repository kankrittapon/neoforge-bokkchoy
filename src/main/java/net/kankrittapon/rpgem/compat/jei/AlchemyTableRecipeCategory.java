package net.kankrittapon.rpgem.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.init.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AlchemyTableRecipeCategory implements IRecipeCategory<AlchemyTableRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID,
            "alchemy_table");
    public static final RecipeType<AlchemyTableRecipe> TYPE = new RecipeType<>(UID, AlchemyTableRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AlchemyTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(
                ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/dispenser.png"), 7, 10, 162,
                70);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.ALCHEMY_TABLE.get()));
    }

    @Override
    public RecipeType<AlchemyTableRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.rpgem.alchemy_table");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlchemyTableRecipe recipe, IFocusGroup focuses) {
        // Layout simplified: Input Top, Ingredient Middle, Output Bottom
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 1).addItemStack(recipe.input());
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 49).addItemStack(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 69).addItemStack(recipe.output());
    }
}
