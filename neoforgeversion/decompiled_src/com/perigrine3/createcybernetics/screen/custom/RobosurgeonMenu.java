/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ClickType
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.block.entity.RobosurgeonBlockEntity;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.surgery.DefaultOrgans;
import com.perigrine3.createcybernetics.common.surgery.RobosurgeonSlotMap;
import com.perigrine3.createcybernetics.screen.ModMenuTypes;
import com.perigrine3.createcybernetics.screen.custom.RobosurgeonSlotItemHandler;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RobosurgeonMenu
extends AbstractContainerMenu {
    public final RobosurgeonBlockEntity blockEntity;
    private final Level level;
    private final List<RobosurgeonSlotItemHandler> robosurgeonSlots = new ArrayList<RobosurgeonSlotItemHandler>();
    private static final int INVENTORY_Y_OFFSET = 56;
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
    private static final int VANILLA_SLOT_COUNT = 36;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = 36;
    private static final int TE_INVENTORY_SLOT_COUNT = 65;

    public int getTeInventoryFirstSlotIndex() {
        return 36;
    }

    public RobosurgeonMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        super((MenuType)ModMenuTypes.ROBOSURGEON_MENU.get(), containerId);
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = inv.player.level().getBlockEntity(pos);
        if (!(be instanceof RobosurgeonBlockEntity)) {
            throw new IllegalStateException("Invalid block entity");
        }
        RobosurgeonBlockEntity rs = (RobosurgeonBlockEntity)be;
        this.blockEntity = rs;
        this.level = inv.player.level();
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        this.addRobosurgeonSlots();
        this.populateFromPlayer(inv.player);
    }

    public RobosurgeonMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super((MenuType)ModMenuTypes.ROBOSURGEON_MENU.get(), containerId);
        this.blockEntity = (RobosurgeonBlockEntity)blockEntity;
        this.level = inv.player.level();
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        this.addRobosurgeonSlots();
        this.populateFromPlayer(inv.player);
    }

    public boolean isInstalled(int index) {
        return this.blockEntity.isInstalled(index);
    }

    public boolean isStaged(int index) {
        return this.blockEntity.isStaged(index);
    }

    public boolean isMarkedForRemoval(int index) {
        return this.blockEntity.isMarkedForRemoval(index);
    }

    public void toggleMarkedForRemoval(int index) {
        this.blockEntity.toggleMarkedForRemoval(index);
    }

    public void setStaged(int index, boolean value) {
        this.blockEntity.setStaged(index, value);
    }

    public void setInstalled(int index, boolean value) {
        this.blockEntity.setInstalled(index, value);
    }

    private void setMarkedForRemoval(int invIndex, boolean value) {
        boolean current = this.isMarkedForRemoval(invIndex);
        if (current != value) {
            this.toggleMarkedForRemoval(invIndex);
        }
    }

    private void addSlotColumn(int startIndex, int count, int x, int startY, CyberwareSlot type) {
        for (int i = 0; i < count; ++i) {
            RobosurgeonSlotItemHandler slot = new RobosurgeonSlotItemHandler(this.blockEntity.inventory, startIndex + i, x + 1, startY - i * 18 + 1, type);
            this.robosurgeonSlots.add(slot);
            this.addSlot((Slot)slot);
        }
    }

    private void addRobosurgeonSlots() {
        this.addSlotColumn(0, 5, 151, 110, CyberwareSlot.BRAIN);
        this.addSlotColumn(5, 5, 151, 110, CyberwareSlot.EYES);
        this.addSlotColumn(10, 6, 151, 110, CyberwareSlot.HEART);
        this.addSlotColumn(16, 6, 151, 110, CyberwareSlot.LUNGS);
        this.addSlotColumn(22, 6, 151, 110, CyberwareSlot.ORGANS);
        this.addSlotColumn(28, 6, 43, 110, CyberwareSlot.RARM);
        this.addSlotColumn(34, 6, 115, 110, CyberwareSlot.LARM);
        this.addSlotColumn(40, 5, 43, 110, CyberwareSlot.RLEG);
        this.addSlotColumn(45, 5, 115, 110, CyberwareSlot.LLEG);
        this.addSlotColumn(50, 5, 79, 110, CyberwareSlot.MUSCLE);
        this.addSlotColumn(55, 5, 106, 110, CyberwareSlot.BONE);
        this.addSlotColumn(60, 5, 52, 110, CyberwareSlot.SKIN);
    }

    private void populateFromPlayer(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            InstalledCyberware[] installedData = data.getAll().get((Object)slot);
            int mappedSize = RobosurgeonSlotMap.mappedSize(slot);
            for (int i = 0; i < mappedSize; ++i) {
                ItemStack inst;
                int invIndex = RobosurgeonSlotMap.toInventoryIndex(slot, i);
                if (invIndex < 0 || invIndex >= this.blockEntity.inventory.getSlots() || this.isStaged(invIndex) || this.isMarkedForRemoval(invIndex)) continue;
                ItemStack stack = ItemStack.EMPTY;
                if (installedData != null && i < installedData.length && installedData[i] != null && (inst = installedData[i].getItem()) != null && !inst.isEmpty()) {
                    stack = inst.copy();
                }
                this.blockEntity.inventory.setStackInSlot(invIndex, stack);
                this.setInstalled(invIndex, !stack.isEmpty());
                this.setStaged(invIndex, false);
                this.setMarkedForRemoval(invIndex, false);
            }
        }
    }

    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId < 0 || slotId >= this.slots.size()) {
            super.clicked(slotId, button, clickType, player);
            return;
        }
        Slot slot = (Slot)this.slots.get(slotId);
        if (slot instanceof RobosurgeonSlotItemHandler) {
            RobosurgeonSlotItemHandler rsSlot = (RobosurgeonSlotItemHandler)slot;
            int handlerIndex = rsSlot.getSlotIndex();
            ItemStack carried = this.getCarried();
            if (clickType == ClickType.QUICK_MOVE) {
                return;
            }
            if (clickType == ClickType.PICKUP && button == 1) {
                if (carried.isEmpty() && rsSlot.hasItem() && this.isStaged(handlerIndex)) {
                    ItemStack stagedStack = rsSlot.getItem().copy();
                    rsSlot.set(ItemStack.EMPTY);
                    this.setStaged(handlerIndex, false);
                    if (this.isMarkedForRemoval(handlerIndex)) {
                        this.setMarkedForRemoval(handlerIndex, false);
                    }
                    if (this.isInstalled(handlerIndex)) {
                        ItemStack restore = this.getInstalledOrDefault(player, handlerIndex);
                        rsSlot.set(restore);
                    }
                    if (!player.getInventory().add(stagedStack)) {
                        player.drop(stagedStack, false);
                    }
                    return;
                }
                return;
            }
            if (clickType == ClickType.PICKUP && button == 0) {
                if (carried.isEmpty() && rsSlot.hasItem() && this.isInstalled(handlerIndex) && !this.isStaged(handlerIndex)) {
                    this.toggleMarkedForRemoval(handlerIndex);
                    return;
                }
                if (!carried.isEmpty() && rsSlot.hasItem() && this.isInstalled(handlerIndex) && !this.isStaged(handlerIndex) && rsSlot.mayPlace(carried)) {
                    ItemStack stagedOne = carried.split(1);
                    rsSlot.set(stagedOne);
                    this.setStaged(handlerIndex, true);
                    this.setMarkedForRemoval(handlerIndex, true);
                    this.setInstalled(handlerIndex, true);
                    return;
                }
                if (!carried.isEmpty() && !rsSlot.hasItem() && !this.isInstalled(handlerIndex) && rsSlot.mayPlace(carried)) {
                    super.clicked(slotId, button, clickType, player);
                    if (rsSlot.hasItem()) {
                        this.setStaged(handlerIndex, true);
                    }
                    return;
                }
                return;
            }
        }
        super.clicked(slotId, button, clickType, player);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= this.slots.size()) {
            return ItemStack.EMPTY;
        }
        Slot sourceSlot = (Slot)this.slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copy = sourceStack.copy();
        boolean replacesOrgan = RobosurgeonMenu.stackReplacesOrgan(sourceStack);
        if (index < 36) {
            CyberwareSlot forcedSide = null;
            if (sourceStack.is(ModTags.Items.LEFTLEG_ITEMS)) {
                forcedSide = CyberwareSlot.LLEG;
            } else if (sourceStack.is(ModTags.Items.RIGHTLEG_ITEMS)) {
                forcedSide = CyberwareSlot.RLEG;
            } else if (sourceStack.is(ModTags.Items.LEFTARM_ITEMS)) {
                forcedSide = CyberwareSlot.LARM;
            } else if (sourceStack.is(ModTags.Items.RIGHTARM_ITEMS)) {
                forcedSide = CyberwareSlot.RARM;
            }
            for (int pass = 0; pass < 2; ++pass) {
                for (RobosurgeonSlotItemHandler targetSlot : this.robosurgeonSlots) {
                    boolean hasInType;
                    int handlerIndex = targetSlot.getSlotIndex();
                    if (!targetSlot.isActive()) continue;
                    CyberwareSlot targetType = targetSlot.getSlotType();
                    if (forcedSide != null && targetType != forcedSide || !this.sideMatches(sourceStack, targetType) || !targetSlot.mayPlace(sourceStack)) continue;
                    int maxStacks = RobosurgeonMenu.getMaxStacksPerSlotType(sourceStack, targetType);
                    int alreadyInType = this.countItemInSlotType(targetType, sourceStack);
                    boolean bl = hasInType = alreadyInType > 0;
                    if (pass == 0 && hasInType || pass == 1 && hasInType && alreadyInType >= maxStacks) continue;
                    boolean installed = this.isInstalled(handlerIndex);
                    boolean staged = this.isStaged(handlerIndex);
                    if (!installed) {
                        if (targetSlot.hasItem()) continue;
                        ItemStack moved = sourceStack.split(1);
                        targetSlot.set(moved);
                        this.setStaged(handlerIndex, true);
                        if (sourceStack.isEmpty()) {
                            sourceSlot.set(ItemStack.EMPTY);
                        } else {
                            sourceSlot.setChanged();
                        }
                        return copy;
                    }
                    if (!installed || !targetSlot.hasItem() || staged || !replacesOrgan || !RobosurgeonMenu.canReplaceExisting(sourceStack, targetSlot.getItem(), targetType)) continue;
                    ItemStack moved = sourceStack.split(1);
                    targetSlot.set(moved);
                    this.setStaged(handlerIndex, true);
                    this.setMarkedForRemoval(handlerIndex, true);
                    if (sourceStack.isEmpty()) {
                        sourceSlot.set(ItemStack.EMPTY);
                    } else {
                        sourceSlot.setChanged();
                    }
                    return copy;
                }
            }
            return ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    private static TagKey<Item> getReplacementTag(ItemStack incoming, CyberwareSlot targetType) {
        Item item = incoming.getItem();
        if (!(item instanceof ICyberwareItem)) {
            return null;
        }
        ICyberwareItem cw = (ICyberwareItem)item;
        return cw.getReplacedOrganItemTag(incoming, targetType);
    }

    private static boolean canReplaceExisting(ItemStack incoming, ItemStack existing, CyberwareSlot targetType) {
        if (existing.isEmpty()) {
            return false;
        }
        TagKey<Item> tag = RobosurgeonMenu.getReplacementTag(incoming, targetType);
        if (tag == null) {
            return true;
        }
        return existing.is(tag);
    }

    public boolean stillValid(Player player) {
        return RobosurgeonMenu.stillValid((ContainerLevelAccess)ContainerLevelAccess.create((Level)this.level, (BlockPos)this.blockEntity.getBlockPos()), (Player)player, (Block)((Block)ModBlocks.ROBOSURGEON.get()));
    }

    private boolean sideMatches(ItemStack stack, CyberwareSlot targetType) {
        if (stack.is(ModTags.Items.LEFTARM_ITEMS)) {
            return targetType == CyberwareSlot.LARM;
        }
        if (stack.is(ModTags.Items.RIGHTARM_ITEMS)) {
            return targetType == CyberwareSlot.RARM;
        }
        if (stack.is(ModTags.Items.LEFTLEG_ITEMS)) {
            return targetType == CyberwareSlot.LLEG;
        }
        if (stack.is(ModTags.Items.RIGHTLEG_ITEMS)) {
            return targetType == CyberwareSlot.RLEG;
        }
        return true;
    }

    private static boolean isDefaultOrganStack(ItemStack stack, CyberwareSlot slot, int idx) {
        if (stack.isEmpty()) {
            return false;
        }
        ItemStack def = DefaultOrgans.get(slot, idx);
        if (def == null || def.isEmpty()) {
            return false;
        }
        return ItemStack.isSameItemSameComponents((ItemStack)stack, (ItemStack)def);
    }

    private static boolean stackReplacesOrgan(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ICyberwareItem)) {
            return false;
        }
        ICyberwareItem cw = (ICyberwareItem)item;
        return cw.replacesOrgan();
    }

    private static int getMaxStacksPerSlotType(ItemStack stack, CyberwareSlot slotType) {
        Item item = stack.getItem();
        if (item instanceof ICyberwareItem) {
            ICyberwareItem cw = (ICyberwareItem)item;
            return Math.max(1, cw.maxStacksPerSlotType(stack, slotType));
        }
        return 1;
    }

    private int countItemInSlotType(CyberwareSlot slotType, ItemStack stack) {
        int count = 0;
        for (int i = 0; i < RobosurgeonSlotMap.mappedSize(slotType); ++i) {
            ItemStack inTe;
            int invIndex = RobosurgeonSlotMap.toInventoryIndex(slotType, i);
            if (invIndex < 0 || (inTe = this.blockEntity.inventory.getStackInSlot(invIndex)).isEmpty() || !ItemStack.isSameItemSameComponents((ItemStack)inTe, (ItemStack)stack)) continue;
            ++count;
        }
        return count;
    }

    private boolean slotTypeHasItem(CyberwareSlot slotType, ItemStack stack) {
        return this.countItemInSlotType(slotType, stack) > 0;
    }

    private SlotRef resolveSlotRef(int invIndex) {
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            int mapped = RobosurgeonSlotMap.mappedSize(slot);
            for (int i = 0; i < mapped; ++i) {
                int check = RobosurgeonSlotMap.toInventoryIndex(slot, i);
                if (check != invIndex) continue;
                return new SlotRef(slot, i);
            }
        }
        return null;
    }

    private ItemStack getInstalledOrDefault(Player player, int invIndex) {
        InstalledCyberware installed;
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return ItemStack.EMPTY;
        }
        SlotRef ref = this.resolveSlotRef(invIndex);
        if (ref == null) {
            return ItemStack.EMPTY;
        }
        InstalledCyberware[] arr = data.getAll().get((Object)ref.slot());
        if (arr != null && ref.idx() >= 0 && ref.idx() < arr.length && (installed = arr[ref.idx()]) != null && installed.getItem() != null && !installed.getItem().isEmpty()) {
            return installed.getItem().copy();
        }
        ItemStack def = DefaultOrgans.get(ref.slot(), ref.idx());
        return def == null ? ItemStack.EMPTY : def.copy();
    }

    private boolean keepInventoryActive() {
        return this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
    }

    private void clearRobosurgeonInventoryAndFlags() {
        for (int i = 0; i < this.blockEntity.inventory.getSlots(); ++i) {
            this.blockEntity.inventory.setStackInSlot(i, ItemStack.EMPTY);
            this.setInstalled(i, false);
            this.setStaged(i, false);
            this.setMarkedForRemoval(i, false);
        }
    }

    private boolean playerHasOnlyDefaultOrgans(PlayerCyberwareData data) {
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            InstalledCyberware[] installed = data.getAll().get((Object)slot);
            int mappedSize = RobosurgeonSlotMap.mappedSize(slot);
            for (int i = 0; i < mappedSize; ++i) {
                ItemStack def = DefaultOrgans.get(slot, i);
                if (def == null) {
                    def = ItemStack.EMPTY;
                }
                ItemStack actual = ItemStack.EMPTY;
                if (installed != null && i < installed.length && installed[i] != null && (actual = installed[i].getItem()) == null) {
                    actual = ItemStack.EMPTY;
                }
                if (actual.isEmpty()) continue;
                if (def.isEmpty()) {
                    return false;
                }
                if (ItemStack.isSameItemSameComponents((ItemStack)actual, (ItemStack)def)) continue;
                return false;
            }
        }
        return true;
    }

    private boolean robosurgeonHasAnyNonDefaultStacks() {
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            int mappedSize = RobosurgeonSlotMap.mappedSize(slot);
            for (int i = 0; i < mappedSize; ++i) {
                ItemStack inTe;
                int invIndex = RobosurgeonSlotMap.toInventoryIndex(slot, i);
                if (invIndex < 0 || (inTe = this.blockEntity.inventory.getStackInSlot(invIndex)).isEmpty()) continue;
                ItemStack def = DefaultOrgans.get(slot, i);
                if (def == null) {
                    def = ItemStack.EMPTY;
                }
                if (def.isEmpty()) {
                    return true;
                }
                if (ItemStack.isSameItemSameComponents((ItemStack)inTe, (ItemStack)def)) continue;
                return true;
            }
        }
        return false;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot((Container)playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18 + 56));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot((Container)playerInventory, i, 8 + i * 18, 198));
        }
    }

    private record SlotRef(CyberwareSlot slot, int idx) {
    }
}

