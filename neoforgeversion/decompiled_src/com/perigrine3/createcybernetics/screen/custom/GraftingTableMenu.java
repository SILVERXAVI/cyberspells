/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.NonNullList
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.Level
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.recipe.GraftingTableRecipe;
import com.perigrine3.createcybernetics.recipe.GraftingTableRecipeInput;
import com.perigrine3.createcybernetics.recipe.ModRecipes;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public final class GraftingTableMenu
extends AbstractContainerMenu {
    private static final int SLOT_IN_COUNT = 7;
    private static final int SLOT_OUT = 7;
    private static final int SLOT_PLAYER_START = 8;
    private final SimpleContainer inputs = new SimpleContainer(7);
    private final SimpleContainer result = new SimpleContainer(1);
    private final ContainerLevelAccess access;
    private ItemStack computedResult = ItemStack.EMPTY;

    public GraftingTableMenu(int containerId, Inventory playerInv, ContainerLevelAccess access) {
        super((MenuType)ModMenuTypes.GRAFTING_TABLE_MENU.get(), containerId);
        this.access = access;
        this.inputs.addListener(this::slotsChanged);
        this.addSlot(new Slot((Container)this.inputs, 0, 26, 26));
        this.addSlot(new Slot((Container)this.inputs, 1, 44, 26));
        this.addSlot(new Slot((Container)this.inputs, 2, 26, 44));
        this.addSlot(new Slot((Container)this.inputs, 3, 44, 44));
        this.addSlot(new OnlyItemSlot((Container)this.inputs, 4, 71, 26, (Item)ModItems.COMPONENT_MESH.get()));
        this.addSlot(new OnlyItemSlot((Container)this.inputs, 5, 71, 44, Items.STRING));
        this.addSlot(new OnlyItemSlot((Container)this.inputs, 6, 89, 35, Items.GHAST_TEAR));
        this.addSlot(new ResultSlot((Container)this.result, 0, 131, 35));
        this.addPlayerInventory(playerInv, 8, 84);
        this.addHotbar(playerInv, 8, 142);
    }

    public GraftingTableMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
        this(containerId, playerInv, ContainerLevelAccess.NULL);
    }

    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (this.slots.isEmpty()) {
            return;
        }
        Player p = this.getPlayer();
        if (p == null || p.level().isClientSide) {
            return;
        }
        Level level = p.level();
        GraftingTableRecipeInput input = this.buildRecipeInput();
        this.computedResult = level.getRecipeManager().getRecipeFor((RecipeType)ModRecipes.GRAFTING_TABLE_TYPE.get(), (RecipeInput)input, level).map(holder -> ((GraftingTableRecipe)holder.value()).assemble(input, (HolderLookup.Provider)level.registryAccess())).orElse(ItemStack.EMPTY);
        this.result.setItem(0, this.computedResult);
        this.broadcastChanges();
    }

    private Player getPlayer() {
        for (Slot s : this.slots) {
            Container container = s.container;
            if (!(container instanceof Inventory)) continue;
            Inventory inv = (Inventory)container;
            return inv.player;
        }
        return null;
    }

    private GraftingTableRecipeInput buildRecipeInput() {
        return new GraftingTableRecipeInput(List.of(this.inputs.getItem(0), this.inputs.getItem(1), this.inputs.getItem(2), this.inputs.getItem(3), this.inputs.getItem(4), this.inputs.getItem(5), this.inputs.getItem(6)));
    }

    public boolean stillValid(Player player) {
        return true;
    }

    public void removed(Player player) {
        ItemStack stack;
        int i;
        super.removed(player);
        if (player.level().isClientSide) {
            return;
        }
        for (i = 0; i < this.inputs.getContainerSize(); ++i) {
            stack = this.inputs.removeItemNoUpdate(i);
            if (stack.isEmpty()) continue;
            player.getInventory().placeItemBackInInventory(stack);
        }
        for (i = 0; i < this.result.getContainerSize(); ++i) {
            stack = this.result.removeItemNoUpdate(i);
            if (stack.isEmpty()) continue;
            player.getInventory().placeItemBackInInventory(stack);
        }
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return empty;
        }
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        int playerEnd = this.slots.size();
        if (index == 7) {
            if (!this.moveItemStackTo(stack, 8, playerEnd, true)) {
                return empty;
            }
            slot.onQuickCraft(stack, copy);
        } else if (index >= 8 ? (stack.is((Item)ModItems.COMPONENT_MESH.get()) ? !this.moveItemStackTo(stack, 4, 5, false) : (stack.is(Items.STRING) ? !this.moveItemStackTo(stack, 5, 6, false) : (stack.is(Items.GHAST_TEAR) ? !this.moveItemStackTo(stack, 6, 7, false) : !this.moveItemStackTo(stack, 0, 4, false)))) : !this.moveItemStackTo(stack, 8, playerEnd, true)) {
            return empty;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }

    private void addPlayerInventory(Inventory inv, int left, int top) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot((Container)inv, col + row * 9 + 9, left + col * 18, top + row * 18));
            }
        }
    }

    private void addHotbar(Inventory inv, int left, int top) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot((Container)inv, col, left + col * 18, top));
        }
    }

    public static final class Layout {
        public static final int GUI_W = 176;
        public static final int GUI_H = 166;
        public static final int IN00_X = 26;
        public static final int IN00_Y = 26;
        public static final int IN01_X = 44;
        public static final int IN01_Y = 26;
        public static final int IN10_X = 26;
        public static final int IN10_Y = 44;
        public static final int IN11_X = 44;
        public static final int IN11_Y = 44;
        public static final int MESH_X = 71;
        public static final int MESH_Y = 26;
        public static final int STRING_X = 71;
        public static final int STRING_Y = 44;
        public static final int TEAR_X = 89;
        public static final int TEAR_Y = 35;
        public static final int OUT_X = 131;
        public static final int OUT_Y = 35;
        public static final int INV_X = 8;
        public static final int INV_Y = 84;
        public static final int HOT_X = 8;
        public static final int HOT_Y = 142;

        private Layout() {
        }
    }

    private static final class OnlyItemSlot
    extends Slot {
        private final Item allowed;

        private OnlyItemSlot(Container container, int index, int x, int y, Item allowed) {
            super(container, index, x, y);
            this.allowed = allowed;
        }

        public boolean mayPlace(ItemStack stack) {
            return !stack.isEmpty() && stack.is(this.allowed);
        }
    }

    private final class ResultSlot
    extends Slot {
        private ResultSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public void onTake(Player player, ItemStack taken) {
            super.onTake(player, taken);
            GraftingTableMenu.this.access.execute((level, pos) -> {
                int i;
                GraftingTableRecipeInput input = GraftingTableMenu.this.buildRecipeInput();
                Optional opt = level.getRecipeManager().getRecipeFor((RecipeType)ModRecipes.GRAFTING_TABLE_TYPE.get(), (RecipeInput)input, level);
                if (opt.isEmpty()) {
                    return;
                }
                GraftingTableRecipe recipe = (GraftingTableRecipe)((RecipeHolder)opt.get()).value();
                NonNullList<ItemStack> remains = recipe.getRemainingItems(input);
                for (i = 0; i < 7; ++i) {
                    ItemStack in = GraftingTableMenu.this.inputs.getItem(i);
                    if (in.isEmpty()) continue;
                    in.shrink(1);
                    GraftingTableMenu.this.inputs.setItem(i, in);
                }
                for (i = 0; i < 7; ++i) {
                    ItemStack rem = (ItemStack)remains.get(i);
                    if (rem.isEmpty()) continue;
                    ItemStack cur = GraftingTableMenu.this.inputs.getItem(i);
                    if (cur.isEmpty()) {
                        GraftingTableMenu.this.inputs.setItem(i, rem);
                        continue;
                    }
                    if (player.getInventory().add(rem)) continue;
                    player.drop(rem, false);
                }
                GraftingTableMenu.this.slotsChanged((Container)GraftingTableMenu.this.inputs);
            });
        }
    }
}

