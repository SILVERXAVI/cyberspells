/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 */
package io.redspace.ironsspellbooks.gui.scroll_forge;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeTile;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ScrollForgeMenu
extends AbstractContainerMenu {
    public final ScrollForgeTile blockEntity;
    private final Level level;
    private final Slot inkSlot;
    private final Slot blankScrollSlot;
    private final Slot focusSlot;
    private final Slot resultSlot;
    private AbstractSpell spellRecipeSelection = SpellRegistry.none();
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
    private static final int VANILLA_SLOT_COUNT = 36;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = 36;
    private static final int TE_INVENTORY_SLOT_COUNT = 4;

    public ScrollForgeMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public ScrollForgeMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(MenuRegistry.SCROLL_FORGE_MENU.get(), containerId);
        ScrollForgeMenu.checkContainerSize((Container)inv, (int)4);
        this.blockEntity = (ScrollForgeTile)entity;
        this.level = inv.player.level();
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        ItemStackHandler itemHandler = this.blockEntity.getItemHandler();
        this.inkSlot = new SlotItemHandler(this, (IItemHandler)itemHandler, 0, 12, 17){

            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof InkItem;
            }
        };
        this.blankScrollSlot = new SlotItemHandler(this, (IItemHandler)itemHandler, 1, 35, 17){

            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.PAPER);
            }
        };
        this.focusSlot = new SlotItemHandler(this, (IItemHandler)itemHandler, 2, 58, 17){

            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModTags.SCHOOL_FOCUS);
            }
        };
        this.resultSlot = new SlotItemHandler((IItemHandler)itemHandler, 3, 35, 47){

            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public void onTake(Player player, ItemStack stack) {
                ScrollForgeMenu.this.inkSlot.remove(1);
                ScrollForgeMenu.this.blankScrollSlot.remove(1);
                ScrollForgeMenu.this.focusSlot.remove(1);
                ScrollForgeMenu.this.level.playSound(null, ScrollForgeMenu.this.blockEntity.getBlockPos(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 0.8f, 1.1f);
                super.onTake(player, stack);
            }
        };
        this.addSlot(this.inkSlot);
        this.addSlot(this.blankScrollSlot);
        this.addSlot(this.focusSlot);
        this.addSlot(this.resultSlot);
    }

    public void onSlotsChanged(int slot) {
        if (slot != 3) {
            this.setupResultSlot(this.spellRecipeSelection);
        }
    }

    private void setupResultSlot(AbstractSpell spell) {
        Item item;
        ItemStack scrollStack = this.blankScrollSlot.getItem();
        ItemStack inkStack = this.inkSlot.getItem();
        ItemStack focusStack = this.focusSlot.getItem();
        ItemStack resultStack = ItemStack.EMPTY;
        if (!scrollStack.isEmpty() && !inkStack.isEmpty() && !focusStack.isEmpty() && !spell.equals(SpellRegistry.none()) && spell.allowCrafting() && SchoolRegistry.getSchoolsFromFocus(focusStack).contains(spell.getSchoolType()) && scrollStack.getItem().equals(Items.PAPER) && (item = inkStack.getItem()) instanceof InkItem) {
            InkItem inkItem = (InkItem)item;
            resultStack = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
            resultStack.setCount(1);
            ISpellContainer.createScrollContainer(spell, spell.getMinLevelForRarity(inkItem.getRarity()), resultStack);
        }
        if (!ItemStack.matches((ItemStack)resultStack, (ItemStack)this.resultSlot.getItem())) {
            if (resultStack.isEmpty()) {
                this.spellRecipeSelection = SpellRegistry.none();
            }
            this.resultSlot.set(resultStack);
        }
    }

    public void setRecipeSpell(AbstractSpell typeFromValue) {
        this.spellRecipeSelection = typeFromValue;
        this.setupResultSlot(typeFromValue);
    }

    public Slot getInkSlot() {
        return this.inkSlot;
    }

    public Slot getBlankScrollSlot() {
        return this.blankScrollSlot;
    }

    public Slot getFocusSlot() {
        return this.focusSlot;
    }

    public Slot getResultSlot() {
        return this.resultSlot;
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = (Slot)this.slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (index < 36) {
            if (!this.moveItemStackTo(sourceStack, 36, 40, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < 40) {
            if (!this.moveItemStackTo(sourceStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
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

    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultSlot.container && super.canTakeItemForPickAll(pStack, pSlot);
    }

    public boolean stillValid(Player pPlayer) {
        return ScrollForgeMenu.stillValid((ContainerLevelAccess)ContainerLevelAccess.create((Level)this.level, (BlockPos)this.blockEntity.getBlockPos()), (Player)pPlayer, (Block)((Block)BlockRegistry.SCROLL_FORGE_BLOCK.get()));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot((Container)playerInventory, l + i * 9 + 9, 8 + l * 18 + 21, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot((Container)playerInventory, i, 8 + i * 18 + 21, 142));
        }
    }
}

