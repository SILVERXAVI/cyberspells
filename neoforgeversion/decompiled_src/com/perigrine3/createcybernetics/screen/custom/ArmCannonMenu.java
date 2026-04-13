/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.item.cyberware.ArmCannonItem;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ArmCannonMenu
extends AbstractContainerMenu {
    private static final int DATA_X0 = 192;
    private static final int DATA_Y0 = 86;
    private static final int SLOT_SPACING = 18;
    private static final int INV_X0 = 48;
    private static final int INV_Y0 = 142;
    private static final int HOTBAR_Y = 200;
    private final Player owner;
    private final Container armInv;
    private final HolderLookup.Provider provider;
    private final CyberwareSlot installedSlot;
    private final int installedIndex;

    public ArmCannonMenu(int id, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        this(id, playerInv, CyberwareSlot.valueOf(buf.readUtf()), buf.readVarInt());
    }

    public ArmCannonMenu(int id, Inventory playerInv, CyberwareSlot installedSlot, int installedIndex) {
        super((MenuType)ModMenuTypes.ARM_CANNON_MENU.get(), id);
        int y;
        int x;
        this.owner = playerInv.player;
        this.installedSlot = installedSlot;
        this.installedIndex = installedIndex;
        this.provider = this.owner.level().registryAccess();
        this.armInv = new SimpleContainer(this, 4){

            public boolean stillValid(Player p) {
                return true;
            }
        };
        Player player = this.owner;
        if (player instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)player;
            ItemStack real = this.getRealInstalledArmCannonStack(sp);
            ArmCannonItem.loadFromInstalledStack(real, this.provider, this.armInv);
        }
        int i = 0;
        while (i < 4) {
            int idx = i++;
            x = 192 + idx % 2 * 18;
            y = 86 + idx / 2 * 18;
            this.addSlot(new Slot(this, this.armInv, idx, x, y){

                public boolean mayPlace(ItemStack stack) {
                    return ArmCannonItem.isValidStoredItem(stack);
                }

                public int getMaxStackSize(ItemStack stack) {
                    return Math.max(1, stack.getMaxStackSize());
                }
            });
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                x = 48 + col * 18;
                y = 142 + row * 18;
                this.addSlot(new Slot((Container)playerInv, 9 + row * 9 + col, x, y));
            }
        }
        for (int col = 0; col < 9; ++col) {
            int x2 = 48 + col * 18;
            this.addSlot(new Slot((Container)playerInv, col, x2, 200));
        }
    }

    private ItemStack getRealInstalledArmCannonStack(ServerPlayer sp) {
        if (!sp.hasData(ModAttachments.CYBERWARE)) {
            return ItemStack.EMPTY;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return ItemStack.EMPTY;
        }
        InstalledCyberware inst = data.get(this.installedSlot, this.installedIndex);
        if (inst == null) {
            return ItemStack.EMPTY;
        }
        ItemStack real = inst.getItem();
        return real == null ? ItemStack.EMPTY : real;
    }

    public ItemStack quickMoveStack(Player player, int index) {
        int armEnd;
        if (player.level().isClientSide) {
            return ItemStack.EMPTY;
        }
        Slot slot = (Slot)this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stackInSlot = slot.getItem();
        ItemStack copy = stackInSlot.copy();
        int playerStart = armEnd = 4;
        int playerEnd = this.slots.size();
        if (index < armEnd) {
            if (!this.moveItemStackTo(stackInSlot, playerStart, playerEnd, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!ArmCannonItem.isValidStoredItem(stackInSlot)) {
                return ItemStack.EMPTY;
            }
            if (!this.moveItemStackTo(stackInSlot, 0, armEnd, false)) {
                return ItemStack.EMPTY;
            }
        }
        if (stackInSlot.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }

    public void removed(Player player) {
        boolean stillInstalled;
        super.removed(player);
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        if (!sp.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        ItemStack real = this.getRealInstalledArmCannonStack(sp);
        boolean bl = stillInstalled = !real.isEmpty() && real.getItem() == ModItems.ARMUPGRADES_ARMCANNON.get();
        if (stillInstalled) {
            ArmCannonItem.saveIntoInstalledStack(real, this.provider, this.armInv);
            data.setDirty();
            sp.syncData(ModAttachments.CYBERWARE);
            return;
        }
        if (!real.isEmpty()) {
            ArmCannonItem.saveIntoInstalledStack(real, this.provider, this.armInv);
            ArmCannonItem.dropAndClearInstalledStack(sp, this.provider, real);
        } else {
            for (int i = 0; i < 4; ++i) {
                ItemStack st = this.armInv.getItem(i);
                if (st.isEmpty()) continue;
                sp.drop(st, false);
            }
        }
        data.setDirty();
        sp.syncData(ModAttachments.CYBERWARE);
    }

    public boolean stillValid(Player player) {
        return true;
    }
}

