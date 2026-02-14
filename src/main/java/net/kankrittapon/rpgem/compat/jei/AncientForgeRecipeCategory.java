package net.kankrittapon.rpgem.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.kankrittapon.rpgem.RPGEasyMode;
import net.kankrittapon.rpgem.init.ModBlocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AncientForgeRecipeCategory implements IRecipeCategory<AncientForgeRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(RPGEasyMode.MODID,
            "ancient_forge");
    public static final RecipeType<AncientForgeRecipe> TYPE = new RecipeType<>(UID, AncientForgeRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AncientForgeRecipeCategory(IGuiHelper helper) {
        // Using Anvil GUI texture as base for forge
        this.background = helper.createDrawable(
                ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/anvil.png"), 20, 40, 140,
                50);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.ANCIENT_FORGE_TABLE.get()));
    }

    @Override
    public RecipeType<AncientForgeRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.rpgem.ancient_forge");
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
    public void setRecipe(IRecipeLayoutBuilder builder, AncientForgeRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 7).addItemStack(recipe.equipment());
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 7).addItemStack(recipe.stone());
    }

    @Override
    public void draw(AncientForgeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
            double mouseX, double mouseY) {
        guiGraphics.drawString(net.minecraft.client.Minecraft.getInstance().font, recipe.description(), 5, 30,
                0xFF404040, false);
    }
}
