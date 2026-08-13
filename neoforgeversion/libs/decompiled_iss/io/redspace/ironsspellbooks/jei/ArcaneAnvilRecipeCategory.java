/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.gui.builder.IIngredientAcceptor
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
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.jei.ArcaneAnvilJeiRecipe;
import io.redspace.ironsspellbooks.jei.JeiPlugin;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class ArcaneAnvilRecipeCategory
implements IRecipeCategory<ArcaneAnvilJeiRecipe> {
    public static final RecipeType<ArcaneAnvilJeiRecipe> ARCANE_ANVIL_RECIPE_RECIPE_TYPE = RecipeType.create((String)"irons_spellbooks", (String)"arcane_anvil", ArcaneAnvilJeiRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final String leftSlotName = "leftSlot";
    private final String rightSlotName = "rightSlot";
    private final String outputSlotName = "outputSlot";
    private final int paddingBottom = 15;

    public ArcaneAnvilRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(JeiPlugin.RECIPE_GUI_VANILLA, 0, 168, 125, 18).addPadding(0, 15, 0, 0).build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack((ItemLike)BlockRegistry.ARCANE_ANVIL_BLOCK.get()));
    }

    public RecipeType<ArcaneAnvilJeiRecipe> getRecipeType() {
        return ARCANE_ANVIL_RECIPE_RECIPE_TYPE;
    }

    public Component getTitle() {
        return ((Block)BlockRegistry.ARCANE_ANVIL_BLOCK.get()).getName();
    }

    public IDrawable getBackground() {
        return this.background;
    }

    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ArcaneAnvilJeiRecipe recipe, IFocusGroup focuses) {
        ArcaneAnvilJeiRecipe.Tuple<List<ItemStack>, List<ItemStack>, List<ItemStack>> recipeitems = recipe.getRecipeItems();
        List<ItemStack> leftInputs = recipeitems.a();
        List<ItemStack> rightInputs = recipeitems.b();
        List<ItemStack> outputs = recipeitems.c();
        IRecipeSlotBuilder leftInputSlot = ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(leftInputs)).setSlotName("leftSlot");
        IRecipeSlotBuilder rightInputSlot = ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addItemStacks(rightInputs)).setSlotName("rightSlot");
        IRecipeSlotBuilder outputSlot = ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStacks(outputs)).setSlotName("outputSlot");
        if (leftInputs.size() == rightInputs.size()) {
            if (leftInputs.size() == outputs.size()) {
                builder.createFocusLink(new IIngredientAcceptor[]{leftInputSlot, rightInputSlot, outputSlot});
            }
        } else if (leftInputs.size() == outputs.size() && rightInputs.size() == 1) {
            builder.createFocusLink(new IIngredientAcceptor[]{leftInputSlot, outputSlot});
        } else if (rightInputs.size() == outputs.size() && leftInputs.size() == 1) {
            builder.createFocusLink(new IIngredientAcceptor[]{rightInputSlot, outputSlot});
        }
    }

    public void draw(ArcaneAnvilJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Optional leftStack = recipeSlotsView.findSlotByName("leftSlot").flatMap(IRecipeSlotView::getDisplayedItemStack);
        Optional rightStack = recipeSlotsView.findSlotByName("rightSlot").flatMap(IRecipeSlotView::getDisplayedItemStack);
        Optional outputStack = recipeSlotsView.findSlotByName("outputSlot").flatMap(IRecipeSlotView::getDisplayedItemStack);
        if (leftStack.isEmpty() || rightStack.isEmpty() || outputStack.isEmpty()) {
            return;
        }
        Item item = ((ItemStack)leftStack.get()).getItem();
        if (item instanceof Scroll) {
            Scroll leftScroll = (Scroll)item;
            item = ((ItemStack)rightStack.get()).getItem();
            if (item instanceof Scroll) {
                Scroll rightScroll = (Scroll)item;
                item = ((ItemStack)outputStack.get()).getItem();
                if (item instanceof Scroll) {
                    Scroll outputScroll = (Scroll)item;
                    Minecraft minecraft = Minecraft.getInstance();
                    this.drawScrollInfo(minecraft, guiGraphics, ISpellContainer.get((ItemStack)leftStack.get()), ISpellContainer.get((ItemStack)outputStack.get()));
                }
            }
        }
    }

    private void drawScrollInfo(Minecraft minecraft, GuiGraphics guiGraphics, ISpellContainer leftScroll, ISpellContainer outputScroll) {
        SpellData inputSpellData = leftScroll.getSpellAtIndex(0);
        String inputText = String.format("L%d", inputSpellData.getLevel());
        int inputColor = inputSpellData.getSpell().getRarity(inputSpellData.getLevel()).getChatFormatting().getColor();
        SpellData outputSpellData = outputScroll.getSpellAtIndex(0);
        String outputText = String.format("L%d", outputSpellData.getLevel());
        int outputColor = outputSpellData.getSpell().getRarity(outputSpellData.getLevel()).getChatFormatting().getColor();
        int n = this.getHeight() / 2 + 7;
        Objects.requireNonNull(minecraft.font);
        int y = n + 9 / 2 - 4;
        int x = 3;
        guiGraphics.drawString(minecraft.font, inputText, x, y, inputColor);
        guiGraphics.drawString(minecraft.font, inputText, x += 50, y, inputColor);
        int outputWidth = minecraft.font.width(outputText);
        guiGraphics.drawString(minecraft.font, outputText, this.getWidth() - (outputWidth + 3), y, outputColor);
    }
}

