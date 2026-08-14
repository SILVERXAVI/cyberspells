/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.narration.NarrationElementOutput
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.SingleRecipeInput
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.maxwell.cyber_ware_port.client.screen;

import com.maxwell.cyber_ware_port.common.block.cwb.recipe.AssemblyRecipe;
import com.maxwell.cyber_ware_port.common.block.cwb.recipe.EngineeringRecipe;
import com.maxwell.cyber_ware_port.common.container.CyberwareWorkbenchMenu;
import com.maxwell.cyber_ware_port.common.item.BlueprintItem;
import com.maxwell.cyber_ware_port.common.network.ComponentChangePagePacket;
import com.maxwell.cyber_ware_port.common.network.ComponentToggleExtendTabPacket;
import com.maxwell.cyber_ware_port.common.network.StartWorkbenchCraftingPacket;
import com.maxwell.cyber_ware_port.init.ModRecipes;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class CyberwareWorkbenchScreen
extends AbstractContainerScreen<CyberwareWorkbenchMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/engineering.png");
    private static final ResourceLocation COMPONENT_BOX_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/component_box.png");
    private static final ResourceLocation BLUEPRINT_PANEL_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/blueprint_chest.png");
    private ItemStack cachedBlueprint = ItemStack.EMPTY;
    private List<AssemblyRecipe.SizedIngredient> cachedIngredients = null;
    private float slideProgress = 1.0f;
    private Button toggleButton;
    private Button prevButton;
    private Button nextButton;
    private Button prevBlueprintBtn;
    private Button nextBlueprintBtn;

    public CyberwareWorkbenchScreen(CyberwareWorkbenchMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super((AbstractContainerMenu)pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelY = 6;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    protected void init() {
        super.init();
        this.prevButton = Button.builder((Component)Component.literal((String)"<"), btn -> PacketDistributor.sendToServer((CustomPacketPayload)new ComponentChangePagePacket(-1, 0), (CustomPacketPayload[])new CustomPacketPayload[0])).bounds(0, 0, 15, 20).build();
        this.nextButton = Button.builder((Component)Component.literal((String)">"), btn -> PacketDistributor.sendToServer((CustomPacketPayload)new ComponentChangePagePacket(1, 0), (CustomPacketPayload[])new CustomPacketPayload[0])).bounds(0, 0, 15, 20).build();
        this.addRenderableWidget((GuiEventListener)this.prevButton);
        this.addRenderableWidget((GuiEventListener)this.nextButton);
        this.prevBlueprintBtn = Button.builder((Component)Component.literal((String)"<"), btn -> PacketDistributor.sendToServer((CustomPacketPayload)new ComponentChangePagePacket(-1, 1), (CustomPacketPayload[])new CustomPacketPayload[0])).bounds(0, 0, 15, 20).build();
        this.nextBlueprintBtn = Button.builder((Component)Component.literal((String)">"), btn -> PacketDistributor.sendToServer((CustomPacketPayload)new ComponentChangePagePacket(1, 1), (CustomPacketPayload[])new CustomPacketPayload[0])).bounds(0, 0, 15, 20).build();
        this.addRenderableWidget((GuiEventListener)this.prevBlueprintBtn);
        this.addRenderableWidget((GuiEventListener)this.nextBlueprintBtn);
        this.toggleButton = Button.builder((Component)Component.literal((String)"\u2261"), btn -> {
            boolean newState = !((CyberwareWorkbenchMenu)this.menu).isExtendedOpen;
            PacketDistributor.sendToServer((CustomPacketPayload)new ComponentToggleExtendTabPacket(newState), (CustomPacketPayload[])new CustomPacketPayload[0]);
            ((CyberwareWorkbenchMenu)this.menu).isExtendedOpen = newState;
        }).bounds(this.leftPos + 5, this.topPos - 10, 12, 12).build();
        if (!((CyberwareWorkbenchMenu)this.menu).hasExtendedInventory && !((CyberwareWorkbenchMenu)this.menu).hasBlueprintLibrary) {
            this.toggleButton.visible = false;
        }
        this.addRenderableWidget((GuiEventListener)this.toggleButton);
        this.addRenderableWidget((GuiEventListener)new AbstractWidget(this.leftPos + 40, this.topPos + 35, 18, 18, (Component)Component.empty()){

            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                ItemStack inputStack = ((CyberwareWorkbenchMenu)CyberwareWorkbenchScreen.this.menu).getSlot(0).getItem();
                ItemStack blueprintStack = ((CyberwareWorkbenchMenu)CyberwareWorkbenchScreen.this.menu).getSlot(2).getItem();
                ItemStack paperStack = ((CyberwareWorkbenchMenu)CyberwareWorkbenchScreen.this.menu).getSlot(1).getItem();
                boolean hasPaper = paperStack.is(Items.PAPER);
                if (this.isHovered() && !inputStack.isEmpty()) {
                    guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x50FFFFFF);
                }
                if (this.isHovered()) {
                    ArrayList<MutableComponent> tooltip = new ArrayList<MutableComponent>();
                    if (!blueprintStack.isEmpty()) {
                        tooltip.add(Component.translatable((String)"gui.cyber_ware_port.assemble").withStyle(new ChatFormatting[]{ChatFormatting.BOLD, ChatFormatting.GREEN}));
                    } else if (!inputStack.isEmpty()) {
                        Optional recipeOpt;
                        tooltip.add(Component.translatable((String)"gui.cyber_ware_port.deconstruct").withStyle(new ChatFormatting[]{ChatFormatting.BOLD, ChatFormatting.RED}));
                        if (CyberwareWorkbenchScreen.this.minecraft != null && ((CyberwareWorkbenchScreen)CyberwareWorkbenchScreen.this).minecraft.level != null && (recipeOpt = ((CyberwareWorkbenchScreen)CyberwareWorkbenchScreen.this).minecraft.level.getRecipeManager().getRecipeFor((RecipeType)ModRecipes.ENGINEERING_TYPE.get(), (RecipeInput)new SingleRecipeInput(inputStack), (Level)((CyberwareWorkbenchScreen)CyberwareWorkbenchScreen.this).minecraft.level)).isPresent()) {
                            float chance = hasPaper ? ((EngineeringRecipe)((RecipeHolder)recipeOpt.get()).value()).getBlueprintChance() : 0.0f;
                            tooltip.add(Component.translatable((String)"gui.cyber_ware_port.blueprint_chance", (Object[])new Object[]{String.format("%.0f", Float.valueOf(chance * 100.0f))}).withStyle(ChatFormatting.GRAY));
                        }
                    }
                    if (!tooltip.isEmpty()) {
                        guiGraphics.renderTooltip(CyberwareWorkbenchScreen.this.font, tooltip, Optional.empty(), mouseX, mouseY);
                    }
                }
            }

            public void onClick(double pMouseX, double pMouseY) {
                PacketDistributor.sendToServer((CustomPacketPayload)new StartWorkbenchCraftingPacket(), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }

            protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
                this.defaultButtonNarrationText(pNarrationElementOutput);
            }
        });
        this.updateButtons();
    }

    protected void containerTick() {
        float target;
        super.containerTick();
        float f = target = ((CyberwareWorkbenchMenu)this.menu).isExtendedOpen ? 1.0f : 0.0f;
        this.slideProgress = Math.abs(this.slideProgress - target) > 0.01f ? (this.slideProgress += (target - this.slideProgress) * 0.2f) : target;
        this.updateButtons();
    }

    private void updateButtons() {
        boolean showRightButtons;
        boolean showLeftButtons;
        boolean isPanelVisible = ((CyberwareWorkbenchMenu)this.menu).isExtendedOpen;
        this.prevButton.visible = showLeftButtons = isPanelVisible && ((CyberwareWorkbenchMenu)this.menu).hasExtendedInventory && ((CyberwareWorkbenchMenu)this.menu).getMaxPages() > 1;
        this.nextButton.visible = showLeftButtons;
        if (showLeftButtons) {
            this.prevButton.active = ((CyberwareWorkbenchMenu)this.menu).getCurrentPage() > 0;
            this.nextButton.active = ((CyberwareWorkbenchMenu)this.menu).getCurrentPage() < ((CyberwareWorkbenchMenu)this.menu).getMaxPages() - 1;
            int panelOriginX = (int)(-61.0f * this.slideProgress);
            this.prevButton.setX(this.leftPos + panelOriginX + 5);
            this.prevButton.setY(this.topPos + 137);
            this.nextButton.setX(this.leftPos + panelOriginX + 47);
            this.nextButton.setY(this.topPos + 137);
        }
        this.prevBlueprintBtn.visible = showRightButtons = isPanelVisible && ((CyberwareWorkbenchMenu)this.menu).hasBlueprintLibrary && ((CyberwareWorkbenchMenu)this.menu).getBlueprintMaxPages() > 1;
        this.nextBlueprintBtn.visible = showRightButtons;
        if (showRightButtons) {
            this.prevBlueprintBtn.active = ((CyberwareWorkbenchMenu)this.menu).getBlueprintCurrentPage() > 0;
            this.nextBlueprintBtn.active = ((CyberwareWorkbenchMenu)this.menu).getBlueprintCurrentPage() < ((CyberwareWorkbenchMenu)this.menu).getBlueprintMaxPages() - 1;
            int panelWidth = 61;
            int slideOffset = (int)((float)panelWidth * this.slideProgress);
            int panelDrawX = this.leftPos + 176 - panelWidth + slideOffset;
            this.prevBlueprintBtn.setX(panelDrawX + 5);
            this.prevBlueprintBtn.setY(this.topPos + 137);
            this.nextBlueprintBtn.setX(panelDrawX + 47);
            this.nextBlueprintBtn.setY(this.topPos + 137);
        }
    }

    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int drawX;
        int x = this.leftPos;
        int y = this.topPos;
        if (((CyberwareWorkbenchMenu)this.menu).hasExtendedInventory && this.slideProgress > 0.01f) {
            drawX = x - (int)(61.0f * this.slideProgress) + 2;
            guiGraphics.blit(COMPONENT_BOX_TEXTURE, drawX, y, 0.0f, 0.0f, 61, 141, 256, 256);
            if (((CyberwareWorkbenchMenu)this.menu).getMaxPages() > 1 && this.slideProgress > 0.8f) {
                guiGraphics.drawCenteredString(this.font, ((CyberwareWorkbenchMenu)this.menu).getCurrentPage() + 1 + "/" + ((CyberwareWorkbenchMenu)this.menu).getMaxPages(), drawX + 32, y + 129, 0xFFFFFF);
            }
        }
        if (((CyberwareWorkbenchMenu)this.menu).hasBlueprintLibrary && this.slideProgress > 0.01f) {
            drawX = x + 176 - 61 + (int)(61.0f * this.slideProgress);
            guiGraphics.blit(BLUEPRINT_PANEL_TEXTURE, drawX, y, 0.0f, 0.0f, 61, 141, 256, 256);
            if (((CyberwareWorkbenchMenu)this.menu).getBlueprintMaxPages() > 1 && this.slideProgress > 0.8f) {
                guiGraphics.drawCenteredString(this.font, ((CyberwareWorkbenchMenu)this.menu).getBlueprintCurrentPage() + 1 + "/" + ((CyberwareWorkbenchMenu)this.menu).getBlueprintMaxPages(), drawX + 32, y + 129, 0xFFFFFF);
            }
        }
        guiGraphics.blit(TEXTURE, x, y, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
    }

    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(guiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderBlueprintGhosts(guiGraphics);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);
    }

    private void renderBlueprintGhosts(GuiGraphics guiGraphics) {
        ItemStack currentBlueprint = ((CyberwareWorkbenchMenu)this.menu).getSlot(2).getItem();
        if (currentBlueprint.isEmpty() || !(currentBlueprint.getItem() instanceof BlueprintItem)) {
            this.cachedBlueprint = ItemStack.EMPTY;
            this.cachedIngredients = null;
            return;
        }
        if (!ItemStack.isSameItemSameComponents((ItemStack)this.cachedBlueprint, (ItemStack)currentBlueprint) || this.cachedIngredients == null) {
            this.cachedBlueprint = currentBlueprint.copy();
            Item targetItem = BlueprintItem.getTargetItem(currentBlueprint);
            if (targetItem != null && this.minecraft != null && this.minecraft.level != null) {
                for (RecipeHolder holder : this.minecraft.level.getRecipeManager().getAllRecipesFor((RecipeType)ModRecipes.ASSEMBLY_TYPE.get())) {
                    if (!((AssemblyRecipe)holder.value()).getResultItem((HolderLookup.Provider)this.minecraft.level.registryAccess()).is(targetItem)) continue;
                    this.cachedIngredients = ((AssemblyRecipe)holder.value()).getInputs();
                    break;
                }
            }
        }
        if (this.cachedIngredients != null) {
            for (int i = 0; i < Math.min(this.cachedIngredients.size(), 6); ++i) {
                AssemblyRecipe.SizedIngredient req = this.cachedIngredients.get(i);
                ItemStack[] items = req.ingredient().getItems();
                if (items.length == 0) continue;
                Slot targetSlot = ((CyberwareWorkbenchMenu)this.menu).getSlot(3 + i);
                int x = this.leftPos + targetSlot.x;
                int y = this.topPos + targetSlot.y;
                ItemStack stackInSlot = targetSlot.getItem();
                if (!stackInSlot.isEmpty() && req.ingredient().test(stackInSlot) && stackInSlot.getCount() >= req.count()) continue;
                guiGraphics.renderItem(items[0], x, y);
                RenderSystem.enableBlend();
                guiGraphics.fill(x, y, x + 16, y + 16, Integer.MIN_VALUE);
                RenderSystem.disableBlend();
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0.0f, 0.0f, 200.0f);
                guiGraphics.drawString(this.font, String.valueOf(req.count()), x + 10, y + 10, 0xFF5555, true);
                guiGraphics.pose().popPose();
            }
        }
    }
}

