/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 */
package com.perigrine3.createcybernetics.common.surgery;

import com.perigrine3.createcybernetics.ConfigValues;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.block.entity.RobosurgeonBlockEntity;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.damage.ModDamageSources;
import com.perigrine3.createcybernetics.common.surgery.RobosurgeonSlotMap;
import com.perigrine3.createcybernetics.event.custom.CyberwareSurgeryEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

public final class SurgeryController {
    private SurgeryController() {
    }

    public static void performSurgery(Player player, RobosurgeonBlockEntity surgeon) {
        SurgeryController.performSurgery(player, surgeon, surgeon.staged, surgeon.markedForRemoval);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void performSurgery(Player player, RobosurgeonBlockEntity surgeon, boolean[] staged, boolean[] markedForRemoval) {
        if (player.level().isClientSide) {
            return;
        }
        surgeon.beginSurgery();
        try {
            int forced;
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            boolean didWork = false;
            int installs = 0;
            int removals = 0;
            ArrayList<CyberwareSurgeryEvent.Change> installedChanges = new ArrayList<CyberwareSurgeryEvent.Change>();
            ArrayList<CyberwareSurgeryEvent.Change> removedChanges = new ArrayList<CyberwareSurgeryEvent.Change>();
            boolean[] wasInstalledBefore = new boolean[surgeon.inventory.getSlots()];
            for (CyberwareSlot slot : CyberwareSlot.values()) {
                for (int i = 0; i < slot.size; ++i) {
                    int invIndex = RobosurgeonSlotMap.toInventoryIndex(slot, i);
                    if (invIndex < 0 || invIndex >= surgeon.inventory.getSlots()) continue;
                    InstalledCyberware inst = data.get(slot, i);
                    wasInstalledBefore[invIndex] = inst != null && inst.getItem() != null && !inst.getItem().isEmpty();
                }
            }
            HashSet<Item> removedItemsThisSurgery = new HashSet<Item>();
            for (CyberwareSlot slot : CyberwareSlot.values()) {
                for (int i = 0; i < slot.size; ++i) {
                    Item giveBack;
                    ICyberwareItem cwReq;
                    Object required;
                    Item cap2;
                    Item giveBack2;
                    boolean willInstall;
                    boolean isMarked;
                    int invIndex = RobosurgeonSlotMap.toInventoryIndex(slot, i);
                    if (invIndex < 0 || invIndex >= surgeon.inventory.getSlots()) continue;
                    ItemStack stackInGui = surgeon.inventory.getStackInSlot(invIndex);
                    boolean isStaged = staged != null && invIndex < staged.length && staged[invIndex];
                    boolean willRemove = isMarked = markedForRemoval != null && invIndex < markedForRemoval.length && markedForRemoval[invIndex];
                    boolean bl = willInstall = isStaged && !stackInGui.isEmpty();
                    if (willRemove && willInstall) {
                        ItemStack installedStack;
                        InstalledCyberware current = data.get(slot, i);
                        ItemStack itemStack = installedStack = current != null && current.getItem() != null ? current.getItem() : ItemStack.EMPTY;
                        if (!installedStack.isEmpty() && ItemStack.isSameItemSameComponents((ItemStack)stackInGui, (ItemStack)installedStack)) {
                            willInstall = false;
                            surgeon.inventory.setStackInSlot(invIndex, ItemStack.EMPTY);
                            surgeon.installed[invIndex] = false;
                            surgeon.staged[invIndex] = false;
                            stackInGui = ItemStack.EMPTY;
                        }
                    }
                    if (willRemove) {
                        InstalledCyberware removed = data.remove(slot, i);
                        if (removed != null && removed.getItem() != null && !removed.getItem().isEmpty()) {
                            didWork = true;
                            ++removals;
                            removedChanges.add(new CyberwareSurgeryEvent.Change(slot, i, removed.getItem().copy()));
                            removedItemsThisSurgery.add(removed.getItem().getItem());
                            Item item = removed.getItem().getItem();
                            if (item instanceof ICyberwareItem) {
                                ICyberwareItem cw = (ICyberwareItem)item;
                                cw.onRemoved(player);
                            }
                            giveBack2 = removed.getItem().copy();
                            if (!player.getInventory().add((ItemStack)giveBack2)) {
                                player.drop((ItemStack)giveBack2, false);
                            }
                        }
                        if (!willInstall) {
                            surgeon.inventory.setStackInSlot(invIndex, ItemStack.EMPTY);
                            surgeon.installed[invIndex] = false;
                            surgeon.staged[invIndex] = false;
                            surgeon.markedForRemoval[invIndex] = false;
                            continue;
                        }
                    }
                    if (!willInstall) {
                        if (!isStaged || !stackInGui.isEmpty()) continue;
                        surgeon.staged[invIndex] = false;
                        continue;
                    }
                    giveBack2 = stackInGui.getItem();
                    if (giveBack2 instanceof ICyberwareItem) {
                        int plannedRemovedSame;
                        ICyberwareItem cw = (ICyberwareItem)giveBack2;
                        int cap2 = Math.max(1, cw.maxStacksPerSlotType(stackInGui, slot));
                        int currentlyInstalledSame = SurgeryController.countInstalledSameInSlotType(data, slot, stackInGui);
                        int effectiveSameAfterAllRemovals = currentlyInstalledSame - (plannedRemovedSame = SurgeryController.countPlannedRemovalsSameInSlotType(data, slot, surgeon, markedForRemoval, stackInGui));
                        if (effectiveSameAfterAllRemovals >= cap2) {
                            ItemStack giveBack3 = stackInGui.copy();
                            if (!player.getInventory().add(giveBack3)) {
                                player.drop(giveBack3, false);
                            }
                            surgeon.inventory.setStackInSlot(invIndex, ItemStack.EMPTY);
                            surgeon.installed[invIndex] = false;
                            surgeon.staged[invIndex] = false;
                            surgeon.markedForRemoval[invIndex] = false;
                            continue;
                        }
                    }
                    if ((cap2 = stackInGui.getItem()) instanceof ICyberwareItem && (required = (cwReq = (ICyberwareItem)cap2).requiresCyberware(stackInGui, slot)) != null && !required.isEmpty() && !SurgeryController.hasAnyRequiredCyberware(data, surgeon, staged, (Set<Item>)required, slot)) {
                        giveBack = stackInGui.copy();
                        if (!player.getInventory().add((ItemStack)giveBack)) {
                            player.drop((ItemStack)giveBack, false);
                        }
                        surgeon.inventory.setStackInSlot(invIndex, ItemStack.EMPTY);
                        surgeon.installed[invIndex] = false;
                        surgeon.staged[invIndex] = false;
                        surgeon.markedForRemoval[invIndex] = false;
                        continue;
                    }
                    required = stackInGui.getItem();
                    if (required instanceof ICyberwareItem) {
                        ICyberwareItem cwInc = (ICyberwareItem)required;
                        if (SurgeryController.hasAnyIncompatibleCyberware(data, surgeon, staged, markedForRemoval, stackInGui, slot, i)) {
                            ItemStack giveBack4 = stackInGui.copy();
                            if (!player.getInventory().add(giveBack4)) {
                                player.drop(giveBack4, false);
                            }
                            surgeon.inventory.setStackInSlot(invIndex, ItemStack.EMPTY);
                            surgeon.installed[invIndex] = false;
                            surgeon.staged[invIndex] = false;
                            surgeon.markedForRemoval[invIndex] = false;
                            continue;
                        }
                    }
                    int humanityCost = 0;
                    giveBack = stackInGui.getItem();
                    if (giveBack instanceof ICyberwareItem) {
                        ICyberwareItem cyberwareItem = (ICyberwareItem)giveBack;
                        humanityCost = cyberwareItem.getHumanityCost();
                    }
                    InstalledCyberware installed = new InstalledCyberware(stackInGui.copy(), slot, i, humanityCost);
                    installed.setPowered(true);
                    data.set(slot, i, installed);
                    didWork = true;
                    ++installs;
                    installedChanges.add(new CyberwareSurgeryEvent.Change(slot, i, installed.getItem().copy()));
                    Item item = stackInGui.getItem();
                    if (item instanceof ICyberwareItem) {
                        ICyberwareItem cw = (ICyberwareItem)item;
                        cw.onInstalled(player, installed.getItem());
                        surgeon.inventory.setStackInSlot(invIndex, installed.getItem().copy());
                    }
                    surgeon.installed[invIndex] = true;
                    surgeon.staged[invIndex] = false;
                    surgeon.markedForRemoval[invIndex] = false;
                }
            }
            if (!removedItemsThisSurgery.isEmpty() && (forced = SurgeryController.forceRemoveDependents(player, surgeon, data, wasInstalledBefore, removedItemsThisSurgery, removedChanges)) > 0) {
                didWork = true;
                removals += forced;
            }
            data.recomputeHumanityBaseFromInstalled();
            if (didWork) {
                float damage;
                if (player instanceof ServerPlayer) {
                    ServerPlayer sp = (ServerPlayer)player;
                    NeoForge.EVENT_BUS.post((Event)new CyberwareSurgeryEvent(sp, installs, removals, installedChanges, removedChanges));
                }
                if ((damage = ConfigValues.SURGERY_DAMAGE_SCALING ? (float)installs * 4.0f + (float)removals * 6.0f : 10.0f) > 0.0f) {
                    player.hurt(ModDamageSources.cyberwareSurgery(player.level(), (Entity)player, null), damage);
                }
            }
            data.setDirty();
            if (player instanceof ServerPlayer) {
                ServerPlayer sp = (ServerPlayer)player;
                ModAttachments.syncCyberware(sp);
            }
            surgeon.clearSlotStates();
            surgeon.setChanged();
            player.level().sendBlockUpdated(surgeon.getBlockPos(), surgeon.getBlockState(), surgeon.getBlockState(), 3);
        }
        finally {
            surgeon.endSurgery();
        }
    }

    /*
     * Could not resolve type clashes
     */
    private static int forceRemoveDependents(Player player, RobosurgeonBlockEntity surgeon, PlayerCyberwareData data, boolean[] wasInstalledBefore, Set<Item> removedItemsThisSurgery, List<CyberwareSurgeryEvent.Change> removedChanges) {
        boolean changed;
        int forcedRemovals = 0;
        do {
            changed = false;
            for (CyberwareSlot slot : CyberwareSlot.values()) {
                for (int i = 0; i < slot.size; ++i) {
                    boolean tagsSatisfied;
                    boolean hasReqTags;
                    ItemStack installedStack;
                    Item item;
                    InstalledCyberware inst;
                    int invIndex = RobosurgeonSlotMap.toInventoryIndex(slot, i);
                    if (invIndex < 0 || invIndex >= surgeon.inventory.getSlots() || invIndex >= wasInstalledBefore.length || !wasInstalledBefore[invIndex] || (inst = data.get(slot, i)) == null || inst.getItem() == null || inst.getItem().isEmpty() || !((item = (installedStack = inst.getItem()).getItem()) instanceof ICyberwareItem)) continue;
                    ICyberwareItem cw = (ICyberwareItem)item;
                    Set<Item> requiredItems = cw.requiresCyberware(installedStack, slot);
                    Set<TagKey<Item>> requiredTags = cw.requiresCyberwareTags(installedStack, slot);
                    boolean hasReqItems = requiredItems != null && !requiredItems.isEmpty();
                    boolean bl = hasReqTags = requiredTags != null && !requiredTags.isEmpty();
                    if (!hasReqItems && !hasReqTags) continue;
                    boolean intersectsRemoved = false;
                    if (hasReqItems) {
                        for (Item req : requiredItems) {
                            if (req == null || !removedItemsThisSurgery.contains(req)) continue;
                            intersectsRemoved = true;
                            break;
                        }
                    }
                    if (!intersectsRemoved && hasReqTags) {
                        for (TagKey tag : requiredTags) {
                            if (tag == null || !SurgeryController.anyRemovedItemMatchesTag(removedItemsThisSurgery, (TagKey<Item>)tag)) continue;
                            intersectsRemoved = true;
                            break;
                        }
                    }
                    if (!intersectsRemoved) continue;
                    boolean itemsSatisfied = hasReqItems && SurgeryController.hasAnyInstalledItem(data, requiredItems);
                    boolean bl2 = tagsSatisfied = hasReqTags && SurgeryController.hasAnyInstalledItemWithAnyTag(data, requiredTags);
                    if (itemsSatisfied || tagsSatisfied) continue;
                    InstalledCyberware removed = data.remove(slot, i);
                    if (removed == null || removed.getItem() == null || removed.getItem().isEmpty()) {
                        surgeon.inventory.setStackInSlot(invIndex, ItemStack.EMPTY);
                        surgeon.installed[invIndex] = false;
                        continue;
                    }
                    ++forcedRemovals;
                    changed = true;
                    Item removedItem = removed.getItem().getItem();
                    if (removedItem != null) {
                        removedItemsThisSurgery.add(removedItem);
                    }
                    removedChanges.add(new CyberwareSurgeryEvent.Change(slot, i, removed.getItem().copy()));
                    if (removedItem instanceof ICyberwareItem) {
                        ICyberwareItem cwRemoved = (ICyberwareItem)removedItem;
                        cwRemoved.onRemoved(player);
                    }
                    ItemStack giveBack = removed.getItem().copy();
                    if (!player.getInventory().add(giveBack)) {
                        player.drop(giveBack, false);
                    }
                    surgeon.inventory.setStackInSlot(invIndex, ItemStack.EMPTY);
                    surgeon.installed[invIndex] = false;
                    surgeon.staged[invIndex] = false;
                    surgeon.markedForRemoval[invIndex] = false;
                }
            }
        } while (changed);
        return forcedRemovals;
    }

    private static boolean hasAnyInstalledItem(PlayerCyberwareData data, Set<Item> items) {
        if (items == null || items.isEmpty()) {
            return true;
        }
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (InstalledCyberware inst : arr) {
                ItemStack st;
                if (inst == null || inst.getItem() == null || (st = inst.getItem()).isEmpty() || !items.contains(st.getItem())) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean hasAnyInstalledItemWithAnyTag(PlayerCyberwareData data, Set<TagKey<Item>> tags) {
        if (tags == null || tags.isEmpty()) {
            return true;
        }
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (InstalledCyberware inst : arr) {
                ItemStack st;
                if (inst == null || inst.getItem() == null || (st = inst.getItem()).isEmpty()) continue;
                for (TagKey<Item> tag : tags) {
                    if (tag == null || !st.is(tag)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean anyRemovedItemMatchesTag(Set<Item> removedItems, TagKey<Item> tag) {
        if (removedItems == null || removedItems.isEmpty()) {
            return false;
        }
        if (tag == null) {
            return false;
        }
        for (Item item : removedItems) {
            if (item == null || !new ItemStack((ItemLike)item).is(tag)) continue;
            return true;
        }
        return false;
    }

    private static boolean hasAnyRequiredCyberware(PlayerCyberwareData data, RobosurgeonBlockEntity surgeon, boolean[] staged, Set<Item> required, CyberwareSlot installSlotType) {
        int i;
        int mappedSize;
        if (required == null || required.isEmpty()) {
            return true;
        }
        if (staged != null) {
            mappedSize = RobosurgeonSlotMap.mappedSize(installSlotType);
            for (i = 0; i < mappedSize; ++i) {
                ItemStack st;
                int invIndex = RobosurgeonSlotMap.toInventoryIndex(installSlotType, i);
                if (invIndex < 0 || invIndex >= surgeon.inventory.getSlots() || invIndex >= staged.length || !staged[invIndex] || (st = surgeon.inventory.getStackInSlot(invIndex)).isEmpty() || !required.contains(st.getItem())) continue;
                return true;
            }
        }
        mappedSize = RobosurgeonSlotMap.mappedSize(installSlotType);
        for (i = 0; i < mappedSize; ++i) {
            InstalledCyberware inst = data.get(installSlotType, i);
            if (inst == null || inst.getItem() == null || inst.getItem().isEmpty() || !required.contains(inst.getItem().getItem())) continue;
            return true;
        }
        return false;
    }

    private static boolean hasAnyIncompatibleCyberware(PlayerCyberwareData data, RobosurgeonBlockEntity surgeon, boolean[] staged, boolean[] markedForRemoval, ItemStack installingStack, CyberwareSlot installingSlotType, int installingIndex) {
        Item item = installingStack.getItem();
        if (!(item instanceof ICyberwareItem)) {
            return false;
        }
        ICyberwareItem installingItem = (ICyberwareItem)item;
        Set<Object> badItems = installingItem.incompatibleCyberware(installingStack, installingSlotType);
        Set<Object> badTags = installingItem.incompatibleCyberwareTags(installingStack, installingSlotType);
        if ((badItems == null || badItems.isEmpty()) && (badTags == null || badTags.isEmpty())) {
            badItems = Set.of();
            badTags = Set.of();
        }
        int currentInvIndex = RobosurgeonSlotMap.toInventoryIndex(installingSlotType, installingIndex);
        for (CyberwareSlot otherSlot : CyberwareSlot.values()) {
            for (int otherIndex = 0; otherIndex < otherSlot.size; ++otherIndex) {
                ItemStack otherStack;
                boolean otherIsStaged;
                int otherInvIndex = RobosurgeonSlotMap.toInventoryIndex(otherSlot, otherIndex);
                if (otherInvIndex < 0 || otherInvIndex >= surgeon.inventory.getSlots() || otherInvIndex == currentInvIndex || markedForRemoval != null && otherInvIndex < markedForRemoval.length && markedForRemoval[otherInvIndex]) continue;
                boolean bl = otherIsStaged = staged != null && otherInvIndex < staged.length && staged[otherInvIndex];
                if (otherIsStaged) {
                    otherStack = surgeon.inventory.getStackInSlot(otherInvIndex);
                } else {
                    InstalledCyberware inst = data.get(otherSlot, otherIndex);
                    ItemStack itemStack = otherStack = inst != null && inst.getItem() != null ? inst.getItem() : ItemStack.EMPTY;
                }
                if (otherStack.isEmpty()) continue;
                if (badItems.contains(otherStack.getItem())) {
                    return true;
                }
                for (TagKey tagKey : badTags) {
                    if (tagKey == null || !otherStack.is(tagKey)) continue;
                    return true;
                }
                Item item2 = otherStack.getItem();
                if (!(item2 instanceof ICyberwareItem)) continue;
                ICyberwareItem otherCyberware = (ICyberwareItem)item2;
                Set<Item> set = otherCyberware.incompatibleCyberware(otherStack, otherSlot);
                if (set != null && set.contains(installingStack.getItem())) {
                    return true;
                }
                Set<TagKey<Item>> otherBadTags = otherCyberware.incompatibleCyberwareTags(otherStack, otherSlot);
                if (otherBadTags == null) continue;
                for (TagKey<Item> tag3 : otherBadTags) {
                    if (tag3 == null || !installingStack.is(tag3)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static int countInstalledSameInSlotType(PlayerCyberwareData data, CyberwareSlot slotType, ItemStack needle) {
        int count = 0;
        for (int i = 0; i < slotType.size; ++i) {
            InstalledCyberware inst = data.get(slotType, i);
            if (inst == null || inst.getItem() == null || inst.getItem().isEmpty() || !ItemStack.isSameItemSameComponents((ItemStack)inst.getItem(), (ItemStack)needle)) continue;
            ++count;
        }
        return count;
    }

    private static int countPlannedRemovalsSameInSlotType(PlayerCyberwareData data, CyberwareSlot slotType, RobosurgeonBlockEntity surgeon, boolean[] markedForRemoval, ItemStack needle) {
        if (markedForRemoval == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < slotType.size; ++i) {
            InstalledCyberware inst;
            int invIndex = RobosurgeonSlotMap.toInventoryIndex(slotType, i);
            if (invIndex < 0 || invIndex >= surgeon.inventory.getSlots() || invIndex >= markedForRemoval.length || !markedForRemoval[invIndex] || (inst = data.get(slotType, i)) == null || inst.getItem() == null || inst.getItem().isEmpty() || !ItemStack.isSameItemSameComponents((ItemStack)inst.getItem(), (ItemStack)needle)) continue;
            ++count;
        }
        return count;
    }
}

