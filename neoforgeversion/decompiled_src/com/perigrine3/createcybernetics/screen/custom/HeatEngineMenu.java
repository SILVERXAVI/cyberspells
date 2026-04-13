/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.SingleRecipeInput
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public final class HeatEngineMenu
extends AbstractContainerMenu {
    public static final int INPUT_X = 72;
    public static final int INPUT_Y = 19;
    public static final int OUTPUT_X = 96;
    public static final int OUTPUT_Y = 19;
    public static final int FUEL_X = 84;
    public static final int FUEL_Y = 39;
    public static final int SLOT_SIZE = 18;
    private static final int IDX_BURN = 0;
    private static final int IDX_BURN_TOTAL = 1;
    private static final int IDX_COOK = 2;
    private static final int IDX_COOK_TOTAL = 3;
    private final Player player;
    private final PlayerCyberwareData data;
    private final Container heatContainer;
    private int cBurn;
    private int cBurnTotal;
    private int cCook;
    private int cCookTotal;
    private final ContainerData syncedData = new ContainerData(){

        public int get(int index) {
            if (!HeatEngineMenu.this.player.level().isClientSide && HeatEngineMenu.this.data != null) {
                return switch (index) {
                    case 0 -> HeatEngineMenu.this.data.getHeatEngineBurnTime();
                    case 1 -> HeatEngineMenu.this.data.getHeatEngineBurnTimeTotal();
                    case 2 -> HeatEngineMenu.this.data.getHeatEngineCookTime();
                    case 3 -> HeatEngineMenu.this.data.getHeatEngineCookTimeTotal();
                    default -> 0;
                };
            }
            return switch (index) {
                case 0 -> HeatEngineMenu.this.cBurn;
                case 1 -> HeatEngineMenu.this.cBurnTotal;
                case 2 -> HeatEngineMenu.this.cCook;
                case 3 -> HeatEngineMenu.this.cCookTotal;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0: {
                    HeatEngineMenu.this.cBurn = value;
                    break;
                }
                case 1: {
                    HeatEngineMenu.this.cBurnTotal = value;
                    break;
                }
                case 2: {
                    HeatEngineMenu.this.cCook = value;
                    break;
                }
                case 3: {
                    HeatEngineMenu.this.cCookTotal = value;
                }
            }
        }

        public int getCount() {
            return 4;
        }
    };

    public HeatEngineMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        this(containerId, inv);
    }

    public HeatEngineMenu(int containerId, Inventory inv) {
        super((MenuType)ModMenuTypes.HEAT_ENGINE_MENU.get(), containerId);
        int col;
        this.player = inv.player;
        PlayerCyberwareData maybe = null;
        if (this.player.hasData(ModAttachments.CYBERWARE)) {
            maybe = (PlayerCyberwareData)this.player.getData(ModAttachments.CYBERWARE);
        }
        this.data = maybe;
        this.heatContainer = new HeatBackedContainer(this.data);
        this.addSlot(new InputSlot(this.player.level(), this.heatContainer, 1, 72, 19));
        this.addSlot(new OutputSlot(this.heatContainer, 2, 96, 19));
        this.addSlot(new FuelSlot(this.heatContainer, 0, 84, 39));
        int invX = 12;
        int invY = 81;
        for (int row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot((Container)inv, 9 + row * 9 + col, invX + col * 18, invY + row * 18));
            }
        }
        int hotbarY = 139;
        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot((Container)inv, col, invX + col * 18, hotbarY));
        }
        this.addDataSlots(this.syncedData);
    }

    public boolean isActiveClient() {
        return this.syncedData.get(0) > 0;
    }

    public int getBurnTimeClient() {
        return Math.max(0, this.syncedData.get(0));
    }

    public int getBurnTimeTotalClient() {
        return Math.max(1, this.syncedData.get(1));
    }

    public int getCookTimeClient() {
        return Math.max(0, this.syncedData.get(2));
    }

    public int getCookTimeTotalClient() {
        return Math.max(1, this.syncedData.get(3));
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return empty;
        }
        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        int HEAT_SLOT_COUNT = 3;
        int PLAYER_INV_START = 3;
        int PLAYER_INV_END = this.slots.size();
        if (index < 3) {
            if (!this.moveItemStackTo(source, 3, PLAYER_INV_END, true)) {
                return empty;
            }
        } else if (AbstractFurnaceBlockEntity.isFuel((ItemStack)source)) {
            if (!this.moveItemStackTo(source, 2, 3, false)) {
                return empty;
            }
        } else if (HeatEngineMenu.isSmeltable(player.level(), source)) {
            if (!this.moveItemStackTo(source, 0, 1, false)) {
                return empty;
            }
        } else {
            return empty;
        }
        if (source.isEmpty()) {
            slot.set(empty);
        } else {
            slot.setChanged();
        }
        slot.onTake(player, source);
        return copy;
    }

    private static boolean isSmeltable(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, (RecipeInput)new SingleRecipeInput(stack), level).isPresent();
    }

    public boolean stillValid(Player player) {
        return true;
    }

    private static final class HeatBackedContainer
    extends SimpleContainer {
        private final PlayerCyberwareData data;

        private HeatBackedContainer(PlayerCyberwareData data) {
            super(3);
            this.data = data;
        }

        public ItemStack getItem(int slot) {
            if (this.data == null) {
                return ItemStack.EMPTY;
            }
            return this.data.getHeatEngineStack(slot);
        }

        public void setItem(int slot, ItemStack stack) {
            if (this.data == null) {
                return;
            }
            this.data.setHeatEngineStack(slot, stack);
        }

        public ItemStack removeItem(int slot, int amount) {
            if (this.data == null) {
                return ItemStack.EMPTY;
            }
            return this.data.removeHeatEngineStack(slot, amount);
        }

        public boolean canPlaceItem(int slot, ItemStack stack) {
            return slot != 2;
        }

        public void setChanged() {
            if (this.data != null) {
                this.data.setDirty();
            }
            super.setChanged();
        }
    }

    private static final class InputSlot
    extends Slot {
        private final Level level;

        private InputSlot(Level level, Container container, int index, int x, int y) {
            super(container, index, x, y);
            this.level = level;
        }

        public boolean mayPlace(ItemStack stack) {
            if (stack.isEmpty()) {
                return false;
            }
            return this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, (RecipeInput)new SingleRecipeInput(stack), this.level).isPresent();
        }
    }

    private static final class OutputSlot
    extends Slot {
        private OutputSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    private static final class FuelSlot
    extends Slot {
        private FuelSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return AbstractFurnaceBlockEntity.isFuel((ItemStack)stack);
        }
    }
}

