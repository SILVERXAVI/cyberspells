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
 *  net.minecraft.world.inventory.ClickType
 *  net.minecraft.world.inventory.DataSlot
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
import com.perigrine3.createcybernetics.item.cyberware.SpinalInjectorItem;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SpinalInjectorMenu
extends AbstractContainerMenu {
    private static final int INJECTOR_MAX = 16;
    private static final int[] INJECTOR_X = new int[]{49, 121, 49, 121};
    private static final int[] INJECTOR_Y = new int[]{29, 29, 101, 101};
    private final Player owner;
    private final Container injectorInv;
    private final HolderLookup.Provider provider;
    private final CyberwareSlot installedSlot;
    private final int installedIndex;
    private ItemStack serverFallbackSnapshot = ItemStack.EMPTY;
    private final int[] injectorCounts = new int[4];

    public int getInjectorDisplayCount(int slot) {
        if (slot < 0 || slot >= this.injectorCounts.length) {
            return 0;
        }
        return this.injectorCounts[slot];
    }

    public int getInjectorSlotX(int i) {
        return INJECTOR_X[i];
    }

    public int getInjectorSlotY(int i) {
        return INJECTOR_Y[i];
    }

    public SpinalInjectorMenu(int id, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        this(id, playerInv, CyberwareSlot.valueOf(buf.readUtf()), buf.readVarInt());
    }

    public SpinalInjectorMenu(int id, Inventory playerInv, CyberwareSlot installedSlot, int installedIndex) {
        super((MenuType)ModMenuTypes.SPINAL_INJECTOR_MENU.get(), id);
        int col;
        this.owner = playerInv.player;
        this.installedSlot = installedSlot;
        this.installedIndex = installedIndex;
        this.provider = playerInv.player.level().registryAccess();
        this.injectorInv = new SimpleContainer(this, 4){

            public boolean stillValid(Player p) {
                return true;
            }
        };
        int i = 0;
        while (i < 4) {
            final int idx = i++;
            this.addDataSlot(new DataSlot(){

                public int get() {
                    return SpinalInjectorMenu.this.injectorCounts[idx];
                }

                public void set(int value) {
                    SpinalInjectorMenu.this.injectorCounts[idx] = value;
                }
            });
        }
        Player idx = this.owner;
        if (idx instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)idx;
            ItemStack real = this.getRealInstalledInjectorStack(sp);
            this.serverFallbackSnapshot = real.isEmpty() ? ItemStack.EMPTY : real.copy();
            SpinalInjectorItem.loadFromInstalledStack(real, this.provider, this.injectorInv, this.injectorCounts);
            this.sanitizeInjectorState();
        }
        for (i = 0; i < 4; ++i) {
            this.addSlot(new Slot(this, this.injectorInv, i, INJECTOR_X[i], INJECTOR_Y[i]){

                public boolean mayPlace(ItemStack stack) {
                    return SpinalInjectorItem.isInjectable(stack);
                }

                public int getMaxStackSize(ItemStack stack) {
                    return Math.min(16, SpinalInjectorItem.maxStackFor(stack));
                }

                public int getMaxStackSize() {
                    return 16;
                }
            });
        }
        int invY = 152;
        int invX = 13;
        for (int row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot((Container)playerInv, col + row * 9 + 9, invX + col * 18, invY + row * 18));
            }
        }
        int hotbarY = invY + 58;
        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot((Container)playerInv, col, invX + col * 18, hotbarY));
        }
    }

    private ItemStack getRealInstalledInjectorStack(ServerPlayer sp) {
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

    private void sanitizeInjectorState() {
        for (int i = 0; i < 4; ++i) {
            ItemStack st = this.injectorInv.getItem(i);
            if (st.isEmpty()) {
                this.injectorCounts[i] = 0;
                continue;
            }
            if (!SpinalInjectorItem.isInjectable(st)) {
                this.injectorInv.setItem(i, ItemStack.EMPTY);
                this.injectorCounts[i] = 0;
                continue;
            }
            if (st.getCount() != 1) {
                st.setCount(1);
            }
            int cap = Math.min(16, SpinalInjectorItem.maxStackFor(st));
            int c = this.injectorCounts[i];
            if (c <= 0) {
                c = 1;
            }
            this.injectorCounts[i] = Math.min(cap, c);
        }
    }

    public void broadcastChanges() {
        if (this.owner instanceof ServerPlayer) {
            this.sanitizeInjectorState();
        }
        super.broadcastChanges();
    }

    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (container == this.injectorInv && this.owner instanceof ServerPlayer) {
            this.broadcastChanges();
        }
    }

    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (player.level().isClientSide) {
            super.clicked(slotId, button, clickType, player);
            return;
        }
        if (clickType == ClickType.PICKUP && slotId >= 0 && slotId < 4) {
            Slot target = (Slot)this.slots.get(slotId);
            ItemStack carried = this.getCarried();
            ItemStack inSlot = target.getItem();
            if (carried.isEmpty()) {
                if (inSlot.isEmpty()) {
                    return;
                }
                int cur = this.injectorCounts[slotId];
                if (cur <= 0) {
                    cur = 1;
                }
                ItemStack one = inSlot.copy();
                one.setCount(1);
                this.setCarried(one);
                this.injectorCounts[slotId] = --cur;
                if (cur <= 0) {
                    target.set(ItemStack.EMPTY);
                    this.injectorCounts[slotId] = 0;
                }
                target.setChanged();
                this.broadcastChanges();
                return;
            }
            if (!carried.isEmpty() && SpinalInjectorItem.isInjectable(carried)) {
                int want = button == 1 ? 1 : carried.getCount();
                int cap = Math.min(16, SpinalInjectorItem.maxStackFor(carried));
                if (inSlot.isEmpty()) {
                    int move = Math.min(want, cap);
                    if (move <= 0) {
                        return;
                    }
                    ItemStack rep = carried.copy();
                    rep.setCount(1);
                    target.set(rep);
                    this.injectorCounts[slotId] = move;
                    carried.shrink(move);
                    this.setCarried(carried);
                    target.setChanged();
                    this.broadcastChanges();
                    return;
                }
                if (ItemStack.isSameItemSameComponents((ItemStack)inSlot, (ItemStack)carried)) {
                    int space;
                    int move;
                    int cur = this.injectorCounts[slotId];
                    if (cur <= 0) {
                        cur = 1;
                    }
                    if ((move = Math.min(space = cap - cur, want)) <= 0) {
                        return;
                    }
                    this.injectorCounts[slotId] = cur + move;
                    carried.shrink(move);
                    this.setCarried(carried);
                    target.setChanged();
                    this.broadcastChanges();
                    return;
                }
            }
        }
        super.clicked(slotId, button, clickType, player);
    }

    private boolean movePotionIntoInjector(ItemStack stack) {
        Slot s;
        int i;
        if (stack.isEmpty() || !SpinalInjectorItem.isInjectable(stack)) {
            return false;
        }
        boolean movedAny = false;
        for (i = 0; i < 4 && !stack.isEmpty(); ++i) {
            int move;
            int space;
            s = (Slot)this.slots.get(i);
            ItemStack curStack = s.getItem();
            if (curStack.isEmpty() || !ItemStack.isSameItemSameComponents((ItemStack)curStack, (ItemStack)stack)) continue;
            int cap = Math.min(16, SpinalInjectorItem.maxStackFor(curStack));
            int cur = this.injectorCounts[i];
            if (cur <= 0) {
                cur = 1;
            }
            if ((space = cap - cur) <= 0 || (move = Math.min(space, stack.getCount())) <= 0) continue;
            this.injectorCounts[i] = cur + move;
            stack.shrink(move);
            s.setChanged();
            movedAny = true;
        }
        for (i = 0; i < 4 && !stack.isEmpty(); ++i) {
            int cap;
            int move;
            s = (Slot)this.slots.get(i);
            if (!s.getItem().isEmpty() || (move = Math.min(cap = Math.min(16, SpinalInjectorItem.maxStackFor(stack)), stack.getCount())) <= 0) continue;
            ItemStack rep = stack.copy();
            rep.setCount(1);
            s.set(rep);
            this.injectorCounts[i] = move;
            stack.shrink(move);
            s.setChanged();
            movedAny = true;
        }
        if (movedAny) {
            this.broadcastChanges();
        }
        return movedAny;
    }

    public ItemStack quickMoveStack(Player player, int index) {
        int injectorEnd;
        if (player.level().isClientSide) {
            return ItemStack.EMPTY;
        }
        Slot slot = (Slot)this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        int playerStart = injectorEnd = 4;
        int playerEnd = this.slots.size();
        if (index < injectorEnd) {
            ItemStack base = slot.getItem();
            int cur = this.injectorCounts[index];
            if (cur <= 0) {
                cur = 1;
            }
            int moved = 0;
            for (int n = 0; n < cur; ++n) {
                ItemStack one = base.copy();
                one.setCount(1);
                if (!this.moveItemStackTo(one, playerStart, playerEnd, true)) break;
                ++moved;
            }
            if (moved == 0) {
                return ItemStack.EMPTY;
            }
            this.injectorCounts[index] = cur - moved;
            if (this.injectorCounts[index] <= 0) {
                this.injectorCounts[index] = 0;
                slot.set(ItemStack.EMPTY);
            }
            slot.setChanged();
            this.broadcastChanges();
            return base.copy();
        }
        ItemStack in = slot.getItem();
        if (!SpinalInjectorItem.isInjectable(in)) {
            return ItemStack.EMPTY;
        }
        ItemStack original = in.copy();
        if (!this.movePotionIntoInjector(in)) {
            return ItemStack.EMPTY;
        }
        if (in.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        this.broadcastChanges();
        return original;
    }

    public void removed(Player player) {
        ItemStack toDrop;
        boolean stillInstalled;
        super.removed(player);
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer sp = (ServerPlayer)player;
        PlayerCyberwareData data = sp.hasData(ModAttachments.CYBERWARE) ? (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE) : null;
        ItemStack real = this.getRealInstalledInjectorStack(sp);
        boolean bl = stillInstalled = !real.isEmpty() && real.getItem() == ModItems.BONEUPGRADES_SPINALINJECTOR.get();
        if (stillInstalled && data != null) {
            SpinalInjectorItem.saveIntoInstalledStack(real, this.provider, this.injectorInv, this.injectorCounts);
            data.setDirty();
            sp.syncData(ModAttachments.CYBERWARE);
            return;
        }
        ItemStack itemStack = toDrop = real.isEmpty() ? this.serverFallbackSnapshot : real;
        if (!toDrop.isEmpty()) {
            SpinalInjectorItem.saveIntoInstalledStack(toDrop, this.provider, this.injectorInv, this.injectorCounts);
            SpinalInjectorItem.dropAndClearInstalledStack(sp, this.provider, toDrop);
        }
        if (data != null) {
            data.setDirty();
            sp.syncData(ModAttachments.CYBERWARE);
        }
    }

    public boolean stillValid(Player player) {
        return true;
    }
}

