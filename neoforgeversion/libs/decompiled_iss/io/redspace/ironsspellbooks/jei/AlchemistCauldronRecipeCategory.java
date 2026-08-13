/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.gui.builder.IRecipeLayoutBuilder
 *  mezz.jei.api.gui.builder.IRecipeSlotBuilder
 *  mezz.jei.api.gui.drawable.IDrawable
 *  mezz.jei.api.gui.ingredient.IRecipeSlotView
 *  mezz.jei.api.gui.ingredient.IRecipeSlotsView
 *  mezz.jei.api.helpers.IGuiHelper
 *  mezz.jei.api.recipe.IFocusGroup
 *  mezz.jei.api.recipe.RecipeIngredientRole
 *  mezz.jei.api.recipe.RecipeType
 *  mezz.jei.api.recipe.category.IRecipeCategory
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.neoforged.neoforge.fluids.FluidStack
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.jei.AlchemistCauldronJeiRecipe;
import io.redspace.ironsspellbooks.jei.JeiPlugin;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class AlchemistCauldronRecipeCategory
implements IRecipeCategory<AlchemistCauldronJeiRecipe> {
    public static final RecipeType<AlchemistCauldronJeiRecipe> ALCHEMIST_CAULDRON_RECIPE_TYPE = RecipeType.create((String)"irons_spellbooks", (String)"alchemist_cauldron", AlchemistCauldronJeiRecipe.class);
    private final IDrawable background;
    private final IDrawable cauldron_block_icon;
    private final String inputSlotName = "itemIn";
    private final String fluidInputSlotName = "fluidIn";
    private final String outputSlotNameBase = "outputSlot";
    private final String byproductSlotName = "byproductSlot";
    private final int paddingBottom = 20;

    public AlchemistCauldronRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(JeiPlugin.ALCHEMIST_CAULDRON_GUI, 0, 0, 125, 19).addPadding(0, 20, 0, 0).build();
        this.cauldron_block_icon = guiHelper.createDrawableItemStack(new ItemStack((ItemLike)BlockRegistry.ALCHEMIST_CAULDRON.get()));
    }

    public RecipeType<AlchemistCauldronJeiRecipe> getRecipeType() {
        return ALCHEMIST_CAULDRON_RECIPE_TYPE;
    }

    public Component getTitle() {
        return ((Block)BlockRegistry.ALCHEMIST_CAULDRON.get()).getName();
    }

    public IDrawable getBackground() {
        return this.background;
    }

    public IDrawable getIcon() {
        return this.cauldron_block_icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, AlchemistCauldronJeiRecipe recipe, IFocusGroup focuses) {
        int fluidRenderHeight = 16;
        IRecipeSlotBuilder itemInput = ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(Arrays.stream(recipe.itemIn().getItems()).toList())).setSlotName("itemIn");
        IRecipeSlotBuilder fluidInput = builder.addSlot(RecipeIngredientRole.INPUT, 54, 17 - fluidRenderHeight).addFluidStack(recipe.fluidIn().getFluid(), (long)recipe.fluidIn().getAmount(), recipe.fluidIn().getComponentsPatch()).setFluidRenderer((long)recipe.fluidIn().getAmount(), false, 16, fluidRenderHeight).setSlotName("fluidIn");
        if (!recipe.results().isEmpty()) {
            int width = 16 / recipe.results().size();
            int diff = 16 - width * recipe.results().size();
            int xpos = 108;
            int maxCap = recipe.results().stream().mapToInt(FluidStack::getAmount).max().getAsInt();
            for (int i = 0; i < recipe.results().size(); ++i) {
                int w = width + (i == 0 ? diff : 0);
                FluidStack stack = recipe.results().get(i);
                IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, xpos, 17 - fluidRenderHeight).addFluidStack(stack.getFluid(), (long)stack.getAmount(), stack.getComponentsPatch()).setFluidRenderer((long)maxCap, false, w, fluidRenderHeight).setSlotName("outputSlot" + i);
                xpos += w;
            }
        }
        if (!recipe.resultByproduct().isEmpty()) {
            int ypos = recipe.results().isEmpty() ? 1 : 17;
            IRecipeSlotBuilder iRecipeSlotBuilder = ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, 108, ypos).addItemStacks(List.of(recipe.resultByproduct()))).setSlotName("byproductSlot");
        }
    }

    public void draw(@NotNull AlchemistCauldronJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiHelper, double mouseX, double mouseY) {
        Optional leftStack = recipeSlotsView.findSlotByName("itemIn").flatMap(IRecipeSlotView::getDisplayedItemStack);
        guiHelper.pose().pushPose();
        guiHelper.pose().translate((float)(this.getWidth() / 2) - 11.2f, (float)(this.getHeight() / 2 - 2), 0.0f);
        guiHelper.pose().scale(1.4f, 1.4f, 1.4f);
        this.cauldron_block_icon.draw(guiHelper);
        guiHelper.pose().popPose();
        if (leftStack.isPresent() && ((ItemStack)leftStack.get()).is((Item)ItemRegistry.SCROLL.get())) {
            String inputText = String.format("%s%%", (int)((Double)ServerConfigs.SCROLL_RECYCLE_CHANCE.get() * 100.0));
            Font font = Minecraft.getInstance().font;
            int y = this.getHeight() / 2;
            int x = (this.getWidth() - font.width(inputText)) * 3 / 4;
            guiHelper.drawString(font, inputText, x, y, (Math.min((Double)ServerConfigs.SCROLL_RECYCLE_CHANCE.get(), 1.0) == 1.0 ? ChatFormatting.GREEN.getColor() : ChatFormatting.RED.getColor()).intValue());
        }
    }
}

