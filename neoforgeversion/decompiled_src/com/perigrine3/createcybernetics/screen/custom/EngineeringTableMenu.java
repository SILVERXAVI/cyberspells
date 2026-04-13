/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.NonNullList
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.ResultContainer
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.block.entity.EngineeringTableBlockEntity;
import com.perigrine3.createcybernetics.recipe.EngineeringTableRecipe;
import com.perigrine3.createcybernetics.recipe.EngineeringTableRecipeInput;
import com.perigrine3.createcybernetics.recipe.ModRecipes;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class EngineeringTableMenu
extends AbstractContainerMenu {
    private static final int CRAFT_START_X = 66;
    private static final int CRAFT_START_Y = 65;
    private static final int SLOT_SPACING = 18;
    private static final int RESULT_X = 175;
    private static final int RESULT_Y = 101;
    private static final int DECON_INPUT_X = 28;
    private static final int DECON_INPUT_Y = 62;
    private static final int DECON_OUT_START_X = 19;
    private static final int DECON_OUT_START_Y = 97;
    private static final int DECON_OUT_COLS = 2;
    private static final int DECON_OUT_ROWS = 3;
    private static final int INV_START_X = 48;
    private static final int INV_START_Y = 162;
    private static final int HOTBAR_Y = 220;
    private static final int GRID_W = 5;
    private static final int GRID_H = 5;
    private static final int GRID_SLOTS = 25;
    private final ContainerLevelAccess access;
    private final Level level;
    private final Player player;
    private final BlockPos pos;
    private final ItemStackHandler grid;
    private final ResultContainer resultSlots = new ResultContainer();
    private final ItemStackHandler deconstructInput;
    private final ItemStackHandler deconstructOutputs;
    private RecipeHolder<EngineeringTableRecipe> lastRecipe = null;

    public EngineeringTableMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos());
    }

    public EngineeringTableMenu(int id, Inventory inv, BlockPos pos) {
        super((MenuType)ModMenuTypes.ENGINEERING_TABLE_MENU.get(), id);
        int col;
        int row;
        int col2;
        this.player = inv.player;
        this.level = inv.player.level();
        this.pos = pos;
        this.access = ContainerLevelAccess.create((Level)this.level, (BlockPos)pos);
        ItemStackHandler gridHandler = new ItemStackHandler(25);
        ItemStackHandler deconInHandler = new ItemStackHandler(1);
        ItemStackHandler deconOutHandler = new ItemStackHandler(6);
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (blockEntity instanceof EngineeringTableBlockEntity) {
            EngineeringTableBlockEntity be = (EngineeringTableBlockEntity)blockEntity;
            gridHandler = be.getCrafting();
            deconInHandler = be.getDeconstructInput();
            deconOutHandler = be.getDeconstructOutputs();
        }
        this.grid = gridHandler;
        this.deconstructInput = deconInHandler;
        this.deconstructOutputs = deconOutHandler;
        this.addSlot(new EngineeringResultSlot(this.resultSlots, 0, 175, 101));
        int idx = 0;
        for (int row2 = 0; row2 < 5; ++row2) {
            for (col2 = 0; col2 < 5; ++col2) {
                this.addSlot((Slot)new GridSlot(this.grid, idx++, 66 + col2 * 18, 65 + row2 * 18));
            }
        }
        this.addSlot((Slot)new DeconstructInputSlot(this.deconstructInput, 0, 28, 62));
        int outIndex = 0;
        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 2; ++col) {
                this.addSlot((Slot)new DeconstructOutputSlot(this, this.deconstructOutputs, outIndex++, 19 + col * 18, 97 + row * 18));
            }
        }
        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot((Container)inv, 9 + row * 9 + col, 48 + col * 18, 162 + row * 18));
            }
        }
        for (col2 = 0; col2 < 9; ++col2) {
            this.addSlot(new Slot((Container)inv, col2, 48 + col2 * 18, 220));
        }
        if (!this.level.isClientSide) {
            this.updateResult();
        }
    }

    public boolean stillValid(Player player) {
        return EngineeringTableMenu.stillValid((ContainerLevelAccess)this.access, (Player)player, (Block)((Block)ModBlocks.ENGINEERING_TABLE.get()));
    }

    public void removed(Player player) {
        super.removed(player);
    }

    private EngineeringTableRecipeInput buildEngineeringInput() {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>(25);
        for (int i = 0; i < 25; ++i) {
            items.add(this.grid.getStackInSlot(i).copy());
        }
        return new EngineeringTableRecipeInput(items);
    }

    private void updateResult() {
        if (this.level.isClientSide) {
            return;
        }
        EngineeringTableRecipeInput input = this.buildEngineeringInput();
        Optional opt = this.level.getRecipeManager().getRecipeFor((RecipeType)ModRecipes.ENGINEERING_TABLE_TYPE.get(), (RecipeInput)input, this.level);
        this.lastRecipe = opt.orElse(null);
        ItemStack out = ItemStack.EMPTY;
        if (this.lastRecipe != null) {
            out = ((EngineeringTableRecipe)this.lastRecipe.value()).assemble(input, (HolderLookup.Provider)this.level.registryAccess());
        }
        this.resultSlots.setItem(0, out);
        this.broadcastChanges();
    }

    private boolean deconstructOutputsEmpty() {
        for (int i = 0; i < 6; ++i) {
            if (this.deconstructOutputs.getStackInSlot(i).isEmpty()) continue;
            return false;
        }
        return true;
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return empty;
        }
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        boolean CRAFT_RESULT = false;
        boolean CRAFT_GRID_START = true;
        int CRAFT_GRID_END_EXCL = 26;
        int DECON_INPUT = 26;
        int DECON_OUT_START = 27;
        int DECON_OUT_END_EXCL = 33;
        int PLAYER_INV_START = 33;
        int PLAYER_INV_END_EXCL = 69;
        if (index == 0) {
            if (!this.moveItemStackTo(stack, 33, 69, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, copy);
        } else if (index >= 27 && index < 33 ? !this.moveItemStackTo(stack, 33, 69, true) : (index >= 33 && index < 69 ? !this.moveItemStackTo(stack, 1, 26, false) : (index >= 1 && index < 26 ? !this.moveItemStackTo(stack, 33, 69, false) : (index == 26 ? !this.moveItemStackTo(stack, 33, 69, false) : !this.moveItemStackTo(stack, 33, 69, false))))) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == copy.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        if (!this.level.isClientSide) {
            this.updateResult();
        }
        return copy;
    }

    private Placement findPlacement(EngineeringTableRecipe recipe, EngineeringTableRecipeInput input) {
        int w = recipe.width();
        int h = recipe.height();
        if (w <= 0 || h <= 0 || w > 5 || h > 5) {
            return null;
        }
        int maxX = 5 - w;
        int maxY = 5 - h;
        for (int offY = 0; offY <= maxY; ++offY) {
            for (int offX = 0; offX <= maxX; ++offX) {
                if (this.matchesAt(recipe, input, offX, offY, false)) {
                    return new Placement(offX, offY, false);
                }
                if (!recipe.accept_mirrored() || !this.matchesAt(recipe, input, offX, offY, true)) continue;
                return new Placement(offX, offY, true);
            }
        }
        return null;
    }

    private boolean matchesAt(EngineeringTableRecipe recipe, EngineeringTableRecipeInput input, int offX, int offY, boolean mirror) {
        int w = recipe.width();
        int h = recipe.height();
        NonNullList<Ingredient> ings = recipe.ingredients();
        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 5; ++x) {
                int relY;
                int gridIndex = x + y * 5;
                boolean inside = x >= offX && x < offX + w && y >= offY && y < offY + h;
                ItemStack stack = input.getItem(gridIndex);
                if (!inside) {
                    if (stack.isEmpty()) continue;
                    return false;
                }
                int relX = x - offX;
                int patX = mirror ? w - 1 - relX : relX;
                int patIndex = patX + (relY = y - offY) * w;
                Ingredient ing = (Ingredient)ings.get(patIndex);
                if (ing.test(stack)) continue;
                return false;
            }
        }
        return true;
    }

    private final class EngineeringResultSlot
    extends Slot {
        private EngineeringResultSlot(ResultContainer container, int slot, int x, int y) {
            super((Container)container, slot, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public void onTake(Player player, ItemStack crafted) {
            EngineeringTableRecipeInput input;
            super.onTake(player, crafted);
            if (EngineeringTableMenu.this.level.isClientSide) {
                return;
            }
            if (EngineeringTableMenu.this.lastRecipe == null) {
                EngineeringTableMenu.this.updateResult();
                return;
            }
            EngineeringTableRecipe recipe = (EngineeringTableRecipe)EngineeringTableMenu.this.lastRecipe.value();
            Placement placement = EngineeringTableMenu.this.findPlacement(recipe, input = EngineeringTableMenu.this.buildEngineeringInput());
            if (placement == null) {
                EngineeringTableMenu.this.updateResult();
                return;
            }
            int w = recipe.width();
            int h = recipe.height();
            NonNullList<Ingredient> ings = recipe.ingredients();
            for (int relY = 0; relY < h; ++relY) {
                for (int relX = 0; relX < w; ++relX) {
                    int gridY;
                    int gridX;
                    int gridIndex;
                    ItemStack in;
                    int patX = placement.mirror ? w - 1 - relX : relX;
                    int patIndex = patX + relY * w;
                    Ingredient ing = (Ingredient)ings.get(patIndex);
                    if (ing.isEmpty() || (in = EngineeringTableMenu.this.grid.getStackInSlot(gridIndex = (gridX = placement.offX + relX) + (gridY = placement.offY + relY) * 5)).isEmpty()) continue;
                    in.shrink(1);
                    EngineeringTableMenu.this.grid.setStackInSlot(gridIndex, in);
                }
            }
            EngineeringTableMenu.this.updateResult();
        }
    }

    private final class GridSlot
    extends SlotItemHandler {
        private GridSlot(ItemStackHandler handler, int index, int x, int y) {
            super((IItemHandler)handler, index, x, y);
        }

        public void setChanged() {
            super.setChanged();
            if (!EngineeringTableMenu.this.level.isClientSide) {
                EngineeringTableMenu.this.updateResult();
            }
        }

        public void set(ItemStack stack) {
            super.set(stack);
            if (!EngineeringTableMenu.this.level.isClientSide) {
                EngineeringTableMenu.this.updateResult();
            }
        }

        public ItemStack remove(int amount) {
            ItemStack out = super.remove(amount);
            if (!EngineeringTableMenu.this.level.isClientSide) {
                EngineeringTableMenu.this.updateResult();
            }
            return out;
        }
    }

    private final class DeconstructInputSlot
    extends SlotItemHandler {
        private DeconstructInputSlot(ItemStackHandler handler, int index, int x, int y) {
            super((IItemHandler)handler, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            if (!EngineeringTableBlockEntity.isDeconstructable(stack)) {
                return false;
            }
            return EngineeringTableMenu.this.deconstructOutputsEmpty();
        }

        public int getMaxStackSize() {
            return 1;
        }

        public void set(ItemStack stack) {
            super.set(stack);
        }
    }

    private final class DeconstructOutputSlot
    extends SlotItemHandler {
        private DeconstructOutputSlot(EngineeringTableMenu engineeringTableMenu, ItemStackHandler handler, int index, int x, int y) {
            super((IItemHandler)handler, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    private record Placement(int offX, int offY, boolean mirror) {
    }
}

