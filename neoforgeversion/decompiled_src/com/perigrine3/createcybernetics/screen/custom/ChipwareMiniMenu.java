/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import com.perigrine3.createcybernetics.screen.slot.DataShardSlot;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.function.BooleanSupplier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ChipwareMiniMenu
extends AbstractContainerMenu {
    public static final int CHIP_X = 146;
    public static final int CHIP_Y0 = 81;
    public static final int CHIP_SPACING = 18;
    public static final int INV_X0 = 48;
    public static final int INV_Y0 = 142;
    public static final int HOTBAR_Y = 200;
    public static final int SLOT_SPACING = 18;
    private final Container chipInv;

    public ChipwareMiniMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInv, (Container)new SimpleContainer(2));
    }

    public ChipwareMiniMenu(int containerId, Inventory playerInv, Container chipInv) {
        super((MenuType)ModMenuTypes.CHIPWARE_MINI_MENU.get(), containerId);
        this.chipInv = chipInv;
        ChipwareMiniMenu.checkContainerSize((Container)chipInv, (int)2);
        chipInv.startOpen(playerInv.player);
        BooleanSupplier active = () -> true;
        this.addSlot(new DataShardSlot(chipInv, 0, 146, 81, active));
        this.addSlot(new DataShardSlot(chipInv, 1, 146, 99, active));
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int idx = 9 + col + row * 9;
                int x = 48 + col * 18;
                int y = 142 + row * 18;
                this.addSlot(new Slot((Container)playerInv, idx, x, y));
            }
        }
        for (int col = 0; col < 9; ++col) {
            int x = 48 + col * 18;
            this.addSlot(new Slot((Container)playerInv, col, x, 200));
        }
    }

    public boolean stillValid(Player player) {
        return true;
    }

    public void removed(Player player) {
        super.removed(player);
        this.chipInv.stopOpen(player);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        boolean moved;
        Slot slot = this.getSlot(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        boolean CHIP_START = false;
        int CHIP_END = 2;
        int PLAYER_START = 2;
        int PLAYER_END = 38;
        if (index >= 0 && index < 2) {
            moved = this.moveItemStackTo(stack, 2, 38, false);
        } else if (stack.is(ModTags.Items.DATA_SHARDS)) {
            moved = this.moveItemStackTo(stack, 0, 2, false);
        } else {
            int INV_START = 2;
            int INV_END = 29;
            int HOTBAR_START = 29;
            int HOTBAR_END = 38;
            moved = index >= 2 && index < 29 ? this.moveItemStackTo(stack, 29, 38, false) : (index >= 29 && index < 38 ? this.moveItemStackTo(stack, 2, 29, false) : false);
        }
        if (!moved) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        slot.onTake(player, stack);
        return copy;
    }

    public static boolean canOpen(Player player) {
        if (player == null) {
            return false;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        return data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_CHIPWARESLOTS.get(), CyberwareSlot.BRAIN);
    }
}

