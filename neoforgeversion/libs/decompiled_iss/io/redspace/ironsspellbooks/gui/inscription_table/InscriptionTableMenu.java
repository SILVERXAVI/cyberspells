/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.ResultContainer
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 */
package io.redspace.ironsspellbooks.gui.inscription_table;

import io.redspace.ironsspellbooks.api.events.InscribeSpellEvent;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainerMutable;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

public class InscriptionTableMenu
extends AbstractContainerMenu {
    private final Player player;
    private final Level level;
    private final Slot spellBookSlot;
    private final Slot scrollSlot;
    private final Slot resultSlot;
    private int selectedSpellIndex = -1;
    private boolean fromCurioSlot = false;
    protected final ResultContainer resultContainer = new ResultContainer();
    protected final Container scrollContainer = new SimpleContainer(1){

        public void setChanged() {
            super.setChanged();
            InscriptionTableMenu.this.slotsChanged((Container)this);
        }
    };
    protected final Container spellbookContainer = new SimpleContainer(1){

        public void setChanged() {
            super.setChanged();
            InscriptionTableMenu.this.slotsChanged((Container)this);
        }

        public boolean canPlaceItem(int pSlot, ItemStack pStack) {
            return super.canPlaceItem(pSlot, pStack);
        }
    };
    protected final ContainerLevelAccess access;
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
    private static final int VANILLA_SLOT_COUNT = 36;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = 36;
    private static final int TE_INVENTORY_SLOT_COUNT = 3;

    public InscriptionTableMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, ContainerLevelAccess.NULL);
    }

    public InscriptionTableMenu(int containerId, Inventory inv, ContainerLevelAccess access) {
        super(MenuRegistry.INSCRIPTION_TABLE_MENU.get(), containerId);
        this.access = access;
        InscriptionTableMenu.checkContainerSize((Container)inv, (int)3);
        this.level = inv.player.level();
        this.player = inv.player;
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        this.spellBookSlot = new Slot(this.spellbookContainer, 0, 17, 21){

            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof SpellBook;
            }

            public void set(ItemStack pStack) {
                super.set(pStack);
                if (InscriptionTableMenu.this.fromCurioSlot && InscriptionTableMenu.this.player != null) {
                    Utils.setPlayerSpellbookStack(InscriptionTableMenu.this.player, pStack);
                }
            }

            public void onTake(Player pPlayer, ItemStack pStack) {
                InscriptionTableMenu.this.setSelectedSpell(-1);
                super.onTake(pPlayer, pStack);
            }
        };
        this.scrollSlot = new Slot(this, this.scrollContainer, 0, 17, 53){

            public boolean mayPlace(ItemStack stack) {
                return stack.is((Item)ItemRegistry.SCROLL.get());
            }
        };
        this.resultSlot = new Slot((Container)this.resultContainer, 2, 208, 136){

            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public void onTake(Player player, ItemStack stack) {
                ItemStack spellBookStack = InscriptionTableMenu.this.spellBookSlot.getItem();
                ISpellContainerMutable spellList = ISpellContainer.get(spellBookStack).mutableCopy();
                spellList.removeSpellAtIndex(InscriptionTableMenu.this.selectedSpellIndex);
                ISpellContainer.set(spellBookStack, spellList.toImmutable());
                super.onTake(player, spellBookStack);
            }
        };
        this.addSlot(this.spellBookSlot);
        this.addSlot(this.scrollSlot);
        this.addSlot(this.resultSlot);
        ItemStack spellbookStack = Utils.getPlayerSpellbookStack(inv.player);
        if (spellbookStack != null) {
            this.fromCurioSlot = true;
            this.spellBookSlot.set(spellbookStack);
        }
    }

    public Slot getSpellBookSlot() {
        return this.spellBookSlot;
    }

    public Slot getScrollSlot() {
        return this.scrollSlot;
    }

    public Slot getResultSlot() {
        return this.resultSlot;
    }

    public void slotsChanged(Container pContainer) {
        super.slotsChanged(pContainer);
        this.setupResultSlot();
    }

    public void setSelectedSpell(int index) {
        this.selectedSpellIndex = index;
        this.setupResultSlot();
    }

    public void doInscription(int selectedIndex) {
        ItemStack spellBookItemStack = this.getSpellBookSlot().getItem();
        ItemStack scrollItemStack = this.getScrollSlot().getItem();
        if (spellBookItemStack.getItem() instanceof SpellBook && scrollItemStack.getItem() instanceof Scroll) {
            ISpellContainer bookContainer = ISpellContainer.get(spellBookItemStack);
            ISpellContainer scrollContainer = ISpellContainer.get(scrollItemStack);
            SpellData scrollSlot = scrollContainer.getSpellAtIndex(0);
            ISpellContainerMutable mutableBookContainer = bookContainer.mutableCopy();
            if (mutableBookContainer.addSpellAtIndex(scrollSlot.getSpell(), scrollSlot.getLevel(), selectedIndex, false)) {
                this.getScrollSlot().remove(1);
                ISpellContainer.set(spellBookItemStack, mutableBookContainer.toImmutable());
            }
        }
    }

    public boolean clickMenuButton(Player pPlayer, int pId) {
        if (pId < 0) {
            Item item;
            ItemStack scrollStack = this.getScrollSlot().getItem();
            if (this.selectedSpellIndex >= 0 && (item = scrollStack.getItem()) instanceof Scroll) {
                Scroll scroll = (Scroll)item;
                SpellData spellData = ISpellContainer.get(scrollStack).getSpellAtIndex(0);
                if (((InscribeSpellEvent)NeoForge.EVENT_BUS.post((Event)new InscribeSpellEvent(pPlayer, spellData))).isCanceled()) {
                    return false;
                }
                this.doInscription(this.selectedSpellIndex);
            }
        } else {
            this.setSelectedSpell(pId);
        }
        return true;
    }

    private void setupResultSlot() {
        ItemStack resultStack = ItemStack.EMPTY;
        ItemStack spellBookStack = this.spellBookSlot.getItem();
        if (spellBookStack.getItem() instanceof SpellBook) {
            SpellData spellData;
            ISpellContainer spellList = ISpellContainer.get(spellBookStack);
            if (this.selectedSpellIndex >= 0 && (spellData = spellList.getSpellAtIndex(this.selectedSpellIndex)) != SpellData.EMPTY && spellData.canRemove()) {
                resultStack = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
                resultStack.setCount(1);
                ISpellContainer.createScrollContainer(spellData.getSpell(), spellData.getLevel(), resultStack);
            }
        }
        if (!ItemStack.matches((ItemStack)resultStack, (ItemStack)this.resultSlot.getItem())) {
            this.resultSlot.set(resultStack);
        }
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = (Slot)this.slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (index < 36) {
            if (!this.moveItemStackTo(sourceStack, 36, 39, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < 39) {
            if (!this.moveItemStackTo(sourceStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    public boolean stillValid(Player pPlayer) {
        return (Boolean)this.access.evaluate((level, blockPos) -> !level.getBlockState(blockPos).is((Block)BlockRegistry.INSCRIPTION_TABLE_BLOCK.get()) ? false : pPlayer.distanceToSqr((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5) <= 64.0, (Object)true);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot((Container)playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot((Container)playerInventory, i, 8 + i * 18, 142));
        }
    }

    public void removed(Player pPlayer) {
        if (pPlayer instanceof ServerPlayer) {
            super.removed(pPlayer);
            this.access.execute((p_39796_, p_39797_) -> {
                this.clearContainer(pPlayer, this.scrollContainer);
                if (this.fromCurioSlot) {
                    Utils.setPlayerSpellbookStack(pPlayer, this.spellBookSlot.remove(1));
                } else {
                    this.clearContainer(pPlayer, this.spellbookContainer);
                }
            });
        }
    }
}

