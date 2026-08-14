/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.SimpleContainerData
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.neoforged.neoforge.capabilities.Capabilities$ItemHandler
 *  net.neoforged.neoforge.items.IItemHandler
 *  net.neoforged.neoforge.items.SlotItemHandler
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.common.container;

import com.maxwell.cyber_ware_port.common.block.blueprintchest.BlueprintChestBlockEntity;
import com.maxwell.cyber_ware_port.common.block.component_box.ComponentBoxBlockEntity;
import com.maxwell.cyber_ware_port.common.block.cwb.CyberwareWorkbenchBlockEntity;
import com.maxwell.cyber_ware_port.common.item.BlueprintItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import com.maxwell.cyber_ware_port.init.ModMenuTypes;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CyberwareWorkbenchMenu
extends AbstractContainerMenu {
    private static final int WORKBENCH_SLOTS = 10;
    private static final int PANEL_X = -61;
    private static final int PANEL_Y = 12;
    private static final Field slotX;
    private static final Field slotY;
    public final CyberwareWorkbenchBlockEntity blockEntity;
    private final Level level;
    private final List<List<Slot>> pageSlots = new ArrayList<List<Slot>>();
    private final List<List<Slot>> blueprintPageSlots = new ArrayList<List<Slot>>();
    public boolean hasExtendedInventory = false;
    public boolean hasBlueprintLibrary = false;
    public boolean isExtendedOpen = true;
    private int currentPage = 0;
    private int maxPages = 0;
    private int blueprintCurrentPage = 0;
    private int blueprintMaxPages = 0;
    private final ContainerData pageData = new SimpleContainerData(6){

        public int get(int index) {
            return switch (index) {
                case 0 -> CyberwareWorkbenchMenu.this.currentPage;
                case 1 -> CyberwareWorkbenchMenu.this.maxPages;
                case 2 -> {
                    if (CyberwareWorkbenchMenu.this.isExtendedOpen) {
                        yield 1;
                    }
                    yield 0;
                }
                case 3 -> {
                    if (CyberwareWorkbenchMenu.this.hasBlueprintLibrary) {
                        yield 1;
                    }
                    yield 0;
                }
                case 4 -> CyberwareWorkbenchMenu.this.blueprintCurrentPage;
                case 5 -> CyberwareWorkbenchMenu.this.blueprintMaxPages;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0: {
                    CyberwareWorkbenchMenu.this.currentPage = value;
                    CyberwareWorkbenchMenu.this.updateSlotPositions();
                    break;
                }
                case 1: {
                    CyberwareWorkbenchMenu.this.maxPages = value;
                    break;
                }
                case 2: {
                    CyberwareWorkbenchMenu.this.isExtendedOpen = value == 1;
                    CyberwareWorkbenchMenu.this.updateSlotPositions();
                    break;
                }
                case 3: {
                    CyberwareWorkbenchMenu.this.hasBlueprintLibrary = value == 1;
                    break;
                }
                case 4: {
                    CyberwareWorkbenchMenu.this.blueprintCurrentPage = value;
                    CyberwareWorkbenchMenu.this.updateSlotPositions();
                    break;
                }
                case 5: {
                    CyberwareWorkbenchMenu.this.blueprintMaxPages = value;
                }
            }
        }
    };

    public CyberwareWorkbenchMenu(int pContainerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CyberwareWorkbenchMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super((MenuType)ModMenuTypes.CYBERWARE_WORKBENCH_MENU.get(), pContainerId);
        this.blockEntity = (CyberwareWorkbenchBlockEntity)entity;
        this.level = inv.player.level();
        IItemHandler handler = this.blockEntity.getItemHandler(null);
        this.addSlot((Slot)new SlotItemHandler(handler, 0, 15, 20));
        this.addSlot((Slot)new SlotItemHandler(handler, 1, 15, 53));
        this.addSlot((Slot)new SlotItemHandler(handler, 2, 115, 53));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlot((Slot)new SlotItemHandler(handler, 3 + (i * 2 + j), 71 + j * 18, 17 + i * 18));
            }
        }
        this.addSlot((Slot)new SlotItemHandler(handler, 9, 141, 21));
        this.findAndAddExternalInventory();
        this.findAndAddBlueprintLibrary();
        this.addDataSlots(this.pageData);
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        this.updateSlotPositions();
    }

    private void findAndAddExternalInventory() {
        BlockPos center = this.blockEntity.getBlockPos();
        for (int x = -3; x <= 3; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -3; z <= 3; ++z) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    BlockPos pos = center.offset(x, y, z);
                    IItemHandler handler = (IItemHandler)this.level.getCapability(Capabilities.ItemHandler.BLOCK, pos, (Object)Direction.UP);
                    if (!(this.level.getBlockEntity(pos) instanceof ComponentBoxBlockEntity) || handler == null) continue;
                    this.hasExtendedInventory = true;
                    ArrayList<SlotItemHandler> currentBoxSlots = new ArrayList<SlotItemHandler>();
                    for (int i = 0; i < 18; ++i) {
                        SlotItemHandler slot = new SlotItemHandler(handler, i, -10000, -10000);
                        this.addSlot((Slot)slot);
                        currentBoxSlots.add(slot);
                    }
                    this.pageSlots.add(currentBoxSlots);
                }
            }
        }
        this.maxPages = this.pageSlots.size();
    }

    private void findAndAddBlueprintLibrary() {
        BlockPos center = this.blockEntity.getBlockPos();
        for (int x = -3; x <= 3; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -3; z <= 3; ++z) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    BlockPos pos = center.offset(x, y, z);
                    IItemHandler handler = (IItemHandler)this.level.getCapability(Capabilities.ItemHandler.BLOCK, pos, (Object)Direction.UP);
                    if (!(this.level.getBlockEntity(pos) instanceof BlueprintChestBlockEntity) || handler == null) continue;
                    this.hasBlueprintLibrary = true;
                    ArrayList<2> currentChestSlots = new ArrayList<2>();
                    for (int i = 0; i < 18; ++i) {
                        SlotItemHandler slot = new SlotItemHandler(this, handler, i, -10000, -10000){

                            public boolean mayPlace(@NotNull ItemStack s) {
                                return s.getItem() instanceof BlueprintItem;
                            }
                        };
                        this.addSlot((Slot)slot);
                        currentChestSlots.add(slot);
                    }
                    this.blueprintPageSlots.add(currentChestSlots);
                }
            }
        }
        this.blueprintMaxPages = this.blueprintPageSlots.size();
    }

    public void updateSlotPositions() {
        int leftX = -51;
        int topY = 18;
        for (int i = 0; i < this.pageSlots.size(); ++i) {
            this.layoutSlots(this.pageSlots.get(i), i == this.currentPage && this.isExtendedOpen, leftX, topY);
        }
        int rightX = 181;
        for (int i = 0; i < this.blueprintPageSlots.size(); ++i) {
            this.layoutSlots(this.blueprintPageSlots.get(i), i == this.blueprintCurrentPage && this.isExtendedOpen && this.hasBlueprintLibrary, rightX, 18);
        }
    }

    private void setSlotPos(Slot slot, int x, int y) {
        try {
            slotX.set(slot, x);
            slotY.set(slot, y);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void layoutSlots(List<Slot> slots, boolean visible, int startX, int startY) {
        for (int i = 0; i < slots.size(); ++i) {
            Slot slot = slots.get(i);
            if (visible) {
                this.setSlotPos(slot, startX + i % 3 * 18, startY + i / 3 * 18);
                continue;
            }
            this.setSlotPos(slot, -10000, -10000);
        }
    }

    public int getCurrentPage() {
        return this.pageData.get(0);
    }

    public int getMaxPages() {
        return this.pageData.get(1);
    }

    public int getBlueprintCurrentPage() {
        return this.pageData.get(4);
    }

    public int getBlueprintMaxPages() {
        return this.pageData.get(5);
    }

    public void setExtendedOpen(boolean open) {
        this.isExtendedOpen = open;
        this.pageData.set(2, open ? 1 : 0);
    }

    @NotNull
    public ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        Slot sourceSlot = (Slot)this.slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        int WB_END = 10;
        int EXT_END = WB_END + this.pageSlots.stream().mapToInt(List::size).sum();
        int LIB_END = EXT_END + this.blueprintPageSlots.stream().mapToInt(List::size).sum();
        if (pIndex < LIB_END ? !this.moveItemStackTo(sourceStack, LIB_END, this.slots.size(), true) : (sourceStack.getItem() instanceof BlueprintItem ? !this.moveItemStackTo(sourceStack, 2, 3, false) && !this.moveItemStackTo(sourceStack, EXT_END, LIB_END, false) : (sourceStack.getItem() instanceof ICyberware ? !this.moveItemStackTo(sourceStack, 0, 1, false) && !this.moveItemStackTo(sourceStack, WB_END, EXT_END, false) : (sourceStack.is(Items.PAPER) ? !this.moveItemStackTo(sourceStack, 1, 2, false) : !this.moveItemStackTo(sourceStack, WB_END, EXT_END, false))))) {
            return ItemStack.EMPTY;
        }
        if (sourceStack.isEmpty()) {
            sourceSlot.setByPlayer(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    public boolean stillValid(@NotNull Player pPlayer) {
        return CyberwareWorkbenchMenu.stillValid((ContainerLevelAccess)ContainerLevelAccess.create((Level)this.level, (BlockPos)this.blockEntity.getBlockPos()), (Player)pPlayer, (Block)((Block)ModBlocks.CYBERWARE_WORKBENCH.get()));
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

    public void changePage(int direction) {
        this.currentPage = Math.clamp((long)(this.currentPage + direction), (int)0, (int)Math.max(0, this.maxPages - 1));
        this.updateSlotPositions();
    }

    public void changeBlueprintPage(int direction) {
        this.blueprintCurrentPage = Math.clamp((long)(this.blueprintCurrentPage + direction), (int)0, (int)Math.max(0, this.blueprintMaxPages - 1));
        this.updateSlotPositions();
    }

    static {
        try {
            slotX = Slot.class.getDeclaredField("x");
            slotX.setAccessible(true);
            slotY = Slot.class.getDeclaredField("y");
            slotY.setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

