/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.armortrim.ArmorTrim
 *  net.minecraft.world.item.armortrim.TrimMaterial
 *  net.minecraft.world.item.crafting.RecipeHolder
 *  net.minecraft.world.item.crafting.RecipeInput
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.SingleRecipeInput
 *  net.minecraft.world.item.crafting.SmeltingRecipe
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
 */
package com.perigrine3.createcybernetics.common.capabilities;

import com.perigrine3.createcybernetics.ConfigValues;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareData;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.client.TrimColorPresets;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.surgery.DefaultOrgans;
import com.perigrine3.createcybernetics.item.cyberware.ArmCannonItem;
import com.perigrine3.createcybernetics.item.cyberware.SpinalInjectorItem;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class PlayerCyberwareData
implements ICyberwareData {
    public static final String HOLO_SNAPSHOT_FLAG = "cc_holo_snapshot";
    public static final String HOLO_SNAPSHOT_CYBERWARE = "cc_holo_snapshot_cyberware";
    private static final String NBT_CYBERWARE = "Cyberware";
    private static final String NBT_HUMANITY = "Humanity";
    private static final String NBT_ENERGY = "Energy";
    private boolean forcedChamberCrouch = false;
    private static final String NBT_NEUROPOZYNE_APPLY_COUNT = "NeuropozyneApplyCount";
    private int neuropozyneApplyCount = 0;
    private static final String NBT_SPINAL_INJECTOR_INV = "SpinalInjectorInv";
    private final ItemStack[] spinalInjectorInv = new ItemStack[4];
    private static final String NBT_ARM_CANNON_INV = "ArmCannonInv";
    private final ItemStack[] armCannonInv = new ItemStack[4];
    private static final String NBT_ARM_CANNON_SELECTED = "ArmCannonSelected";
    private int armCannonSelected = 0;
    private static final String NBT_COPERNICUS_OXYGEN = "CopernicusOxygen";
    private int copernicusOxygen = 0;
    private boolean copernicusOxygenatedEnvironment = false;
    private int copernicusOxygenSecondTicker = 0;
    public static final int CHIPWARE_SLOT_COUNT = 2;
    private static final String NBT_CHIPWARE_INV = "ChipwareInv";
    private final ItemStack[] chipwareInv = new ItemStack[2];
    public static final int HEAT_ENGINE_SLOT_COUNT = 3;
    public static final int HEAT_ENGINE_FUEL = 0;
    public static final int HEAT_ENGINE_INPUT = 1;
    public static final int HEAT_ENGINE_OUTPUT = 2;
    private static final String NBT_HEAT_ENGINE_INV = "HeatEngineInv";
    private static final String NBT_HEAT_ENGINE_BURN = "HeatEngineBurn";
    private static final String NBT_HEAT_ENGINE_BURN_TOTAL = "HeatEngineBurnTotal";
    private static final String NBT_HEAT_ENGINE_COOK = "HeatEngineCook";
    private static final String NBT_HEAT_ENGINE_COOK_TOTAL = "HeatEngineCookTotal";
    private final ItemStack[] heatEngineInv = new ItemStack[3];
    private int heatEngineBurnTime = 0;
    private int heatEngineBurnTimeTotal = 0;
    private int heatEngineCookTime = 0;
    private int heatEngineCookTimeTotal = 200;
    private final EnumMap<CyberwareSlot, InstalledCyberware[]> slots = new EnumMap(CyberwareSlot.class);
    private final EnumMap<CyberwareSlot, boolean[]> enabled = new EnumMap(CyberwareSlot.class);
    private boolean dirty = false;
    private int humanity = ConfigValues.BASE_HUMANITY;
    private int humanityBonus = 0;
    private int energyStored = 0;

    public PlayerCyberwareData() {
        int i;
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            this.slots.put(slot, new InstalledCyberware[slot.size]);
            boolean[] en = new boolean[slot.size];
            for (int i2 = 0; i2 < en.length; ++i2) {
                en[i2] = true;
            }
            this.enabled.put(slot, en);
        }
        for (i = 0; i < this.spinalInjectorInv.length; ++i) {
            this.spinalInjectorInv[i] = ItemStack.EMPTY;
        }
        for (i = 0; i < this.armCannonInv.length; ++i) {
            this.armCannonInv[i] = ItemStack.EMPTY;
        }
        for (i = 0; i < this.chipwareInv.length; ++i) {
            this.chipwareInv[i] = ItemStack.EMPTY;
        }
        for (i = 0; i < this.heatEngineInv.length; ++i) {
            this.heatEngineInv[i] = ItemStack.EMPTY;
        }
        this.armCannonSelected = 0;
    }

    @Override
    public InstalledCyberware get(CyberwareSlot slot, int index) {
        return this.slots.get((Object)slot)[index];
    }

    @Override
    public void set(CyberwareSlot slot, int index, InstalledCyberware cyberware) {
        this.slots.get((Object)((Object)slot))[index] = cyberware;
        boolean[] en = this.enabled.get((Object)slot);
        if (en != null && index >= 0 && index < en.length) {
            ICyberwareItem cwItem;
            Item item;
            ItemStack st;
            boolean enabledByDefault = true;
            if (cyberware != null && (st = cyberware.getItem()) != null && !st.isEmpty() && (item = st.getItem()) instanceof ICyberwareItem && (cwItem = (ICyberwareItem)item).isToggleableByWheel(st, slot)) {
                enabledByDefault = false;
            }
            en[index] = enabledByDefault;
        }
        this.dirty = true;
    }

    @Override
    public InstalledCyberware remove(CyberwareSlot slot, int index) {
        InstalledCyberware old = this.slots.get((Object)slot)[index];
        this.slots.get((Object)((Object)slot))[index] = null;
        boolean[] en = this.enabled.get((Object)slot);
        if (en != null && index >= 0 && index < en.length) {
            en[index] = true;
        }
        this.dirty = true;
        return old;
    }

    @Override
    public Map<CyberwareSlot, InstalledCyberware[]> getAll() {
        return this.slots;
    }

    @Override
    public int getHumanity() {
        return this.humanity + this.humanityBonus;
    }

    @Override
    public void setHumanity(int value) {
        this.humanity = value;
        this.dirty = true;
    }

    public int getHumanityBase() {
        return this.humanity;
    }

    public int getHumanityBonus() {
        return this.humanityBonus;
    }

    public void setHumanityBonus(int bonus) {
        int clamped = Mth.clamp((int)bonus, (int)0, (int)1000);
        if (clamped != this.humanityBonus) {
            this.humanityBonus = clamped;
            this.dirty = true;
        }
    }

    public void clearHumanityBonus() {
        this.setHumanityBonus(0);
    }

    public void recomputeHumanityBaseFromInstalled() {
        int base = ConfigValues.BASE_HUMANITY;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : this.slots.entrySet()) {
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (InstalledCyberware cw : arr) {
                Item item;
                ItemStack stack;
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !((item = stack.getItem()) instanceof ICyberwareItem)) continue;
                ICyberwareItem item2 = (ICyberwareItem)item;
                base -= item2.getHumanityCost();
            }
        }
        this.humanity = base;
        this.dirty = true;
    }

    public int getArmCannonSelected() {
        return Mth.clamp((int)this.armCannonSelected, (int)0, (int)3);
    }

    public void setArmCannonSelected(int idx) {
        int clamped = Mth.clamp((int)idx, (int)0, (int)3);
        if (clamped != this.armCannonSelected) {
            this.armCannonSelected = clamped;
            this.dirty = true;
        }
    }

    public ItemStack getChipwareStack(int slot) {
        if (slot < 0 || slot >= this.chipwareInv.length) {
            return ItemStack.EMPTY;
        }
        ItemStack st = this.chipwareInv[slot];
        return st == null ? ItemStack.EMPTY : st;
    }

    public void setChipwareStack(int slot, ItemStack stack) {
        if (slot < 0 || slot >= this.chipwareInv.length) {
            return;
        }
        if (stack == null || stack.isEmpty()) {
            this.chipwareInv[slot] = ItemStack.EMPTY;
            this.dirty = true;
            return;
        }
        if (!stack.is(ModTags.Items.DATA_SHARDS)) {
            this.chipwareInv[slot] = ItemStack.EMPTY;
            this.dirty = true;
            return;
        }
        ItemStack copy = stack.copy();
        copy.setCount(1);
        this.chipwareInv[slot] = copy;
        this.dirty = true;
    }

    public void clearChipwareInventory() {
        for (int i = 0; i < this.chipwareInv.length; ++i) {
            this.chipwareInv[i] = ItemStack.EMPTY;
        }
        this.dirty = true;
    }

    public boolean hasChipwareShard(TagKey<Item> tag) {
        for (int i = 0; i < 2; ++i) {
            ItemStack st = this.getChipwareStack(i);
            if (st.isEmpty() || !st.is(tag)) continue;
            return true;
        }
        return false;
    }

    public boolean hasChipwareShardExact(Item shardItem) {
        if (shardItem == null) {
            return false;
        }
        for (int i = 0; i < 2; ++i) {
            ItemStack st = this.getChipwareStack(i);
            if (st.isEmpty() || !st.is(shardItem)) continue;
            return true;
        }
        return false;
    }

    public boolean isEnabled(CyberwareSlot slot, int index) {
        boolean[] arr = this.enabled.get((Object)slot);
        if (arr == null) {
            return true;
        }
        if (index < 0 || index >= arr.length) {
            return true;
        }
        return arr[index];
    }

    public void setEnabled(CyberwareSlot slot, int index, boolean value) {
        boolean[] arr = this.enabled.get((Object)slot);
        if (arr == null) {
            return;
        }
        if (index < 0 || index >= arr.length) {
            return;
        }
        if (arr[index] != value) {
            arr[index] = value;
            this.dirty = true;
        }
    }

    public boolean toggleEnabled(CyberwareSlot slot, int index) {
        boolean next = !this.isEnabled(slot, index);
        this.setEnabled(slot, index, next);
        return next;
    }

    public boolean hasOrgan(CyberwareSlot slot) {
        InstalledCyberware[] arr;
        for (InstalledCyberware cw : arr = this.slots.get((Object)slot)) {
            if (cw == null || cw.getItem() == null || cw.getItem().isEmpty()) continue;
            return true;
        }
        return false;
    }

    public boolean isOrganReplaced(CyberwareSlot slot) {
        InstalledCyberware[] arr;
        for (InstalledCyberware cw : arr = this.slots.get((Object)slot)) {
            ICyberwareItem item;
            Item item2;
            if (cw == null || cw.getItem().isEmpty() || !((item2 = cw.getItem().getItem()) instanceof ICyberwareItem) || !(item = (ICyberwareItem)item2).replacesOrgan() || !item.getReplacedOrgans().contains((Object)slot)) continue;
            return true;
        }
        return false;
    }

    public boolean hasAnyInSlots(CyberwareSlot slot) {
        InstalledCyberware[] arr = this.slots.get((Object)slot);
        if (arr == null) {
            return false;
        }
        for (InstalledCyberware cw : arr) {
            if (cw == null || cw.getItem().isEmpty()) continue;
            return true;
        }
        return false;
    }

    public boolean hasAnyTagged(TagKey<Item> tag, CyberwareSlot ... slotsToCheck) {
        for (CyberwareSlot slot : slotsToCheck) {
            InstalledCyberware[] arr = this.slots.get((Object)slot);
            if (arr == null) continue;
            for (InstalledCyberware cw : arr) {
                ItemStack stack;
                if (cw == null || (stack = cw.getItem()).isEmpty() || !stack.is(tag)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean hasSpecificItem(Item item, CyberwareSlot ... slotsToCheck) {
        for (CyberwareSlot slot : slotsToCheck) {
            InstalledCyberware[] arr = this.slots.get((Object)slot);
            if (arr == null) continue;
            for (InstalledCyberware cw : arr) {
                ItemStack stack;
                if (cw == null || (stack = cw.getItem()).isEmpty() || !stack.is(item)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean hasMultipleSpecificItem(Item item, CyberwareSlot slotToCheck, int requiredCount) {
        if (item == null) {
            return false;
        }
        if (requiredCount <= 0) {
            return true;
        }
        InstalledCyberware[] arr = this.slots.get((Object)slotToCheck);
        if (arr == null) {
            return false;
        }
        int found = 0;
        for (InstalledCyberware cw : arr) {
            ItemStack stack;
            if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !stack.is(item) || ++found < requiredCount) continue;
            return true;
        }
        return false;
    }

    public boolean hasMultipleSpecificItem(Item item, int requiredCount, CyberwareSlot ... slotsToCheck) {
        if (item == null) {
            return false;
        }
        if (requiredCount <= 0) {
            return true;
        }
        if (slotsToCheck == null || slotsToCheck.length == 0) {
            return false;
        }
        int found = 0;
        for (CyberwareSlot slot : slotsToCheck) {
            InstalledCyberware[] arr = this.slots.get((Object)slot);
            if (arr == null) continue;
            for (InstalledCyberware cw : arr) {
                ItemStack stack;
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !stack.is(item) || ++found < requiredCount) continue;
                return true;
            }
        }
        return false;
    }

    public boolean isInstalled(Item item, CyberwareSlot slot, int index) {
        InstalledCyberware[] arr = this.slots.get((Object)slot);
        if (arr == null) {
            return false;
        }
        if (index < 0 || index >= arr.length) {
            return false;
        }
        InstalledCyberware cw = arr[index];
        if (cw == null) {
            return false;
        }
        ItemStack stack = cw.getItem();
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return stack.is(item);
    }

    public boolean isInstalled(Item item, CyberwareSlot slot) {
        return this.hasSpecificItem(item, slot);
    }

    public boolean isDyed(CyberwareSlot slot, int index) {
        InstalledCyberware installed = this.get(slot, index);
        if (installed == null) {
            return false;
        }
        ItemStack st = installed.getItem();
        if (st == null || st.isEmpty()) {
            return false;
        }
        Item item = st.getItem();
        if (!(item instanceof ICyberwareItem)) {
            return false;
        }
        ICyberwareItem item2 = (ICyberwareItem)item;
        return item2.isDyed(st, slot);
    }

    public int dyeColor(CyberwareSlot slot, int index) {
        InstalledCyberware installed = this.get(slot, index);
        if (installed == null) {
            return -1;
        }
        ItemStack st = installed.getItem();
        if (st == null || st.isEmpty()) {
            return -1;
        }
        Item item = st.getItem();
        if (!(item instanceof ICyberwareItem)) {
            return -1;
        }
        ICyberwareItem item2 = (ICyberwareItem)item;
        return item2.dyeColor(st, slot);
    }

    public boolean isDyed(Item item, CyberwareSlot slotToCheck) {
        InstalledCyberware[] arr = this.slots.get((Object)slotToCheck);
        if (arr == null) {
            return false;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware installed = arr[i];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !st.is(item)) continue;
            Item item2 = st.getItem();
            if (item2 instanceof ICyberwareItem) {
                ICyberwareItem cw = (ICyberwareItem)item2;
                return cw.isDyed(st, slotToCheck);
            }
            return false;
        }
        return false;
    }

    public int dyeColor(Item item, CyberwareSlot slotToCheck) {
        InstalledCyberware[] arr = this.slots.get((Object)slotToCheck);
        if (arr == null) {
            return -1;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware installed = arr[i];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !st.is(item)) continue;
            Item item2 = st.getItem();
            if (item2 instanceof ICyberwareItem) {
                ICyberwareItem cw = (ICyberwareItem)item2;
                return cw.dyeColor(st, slotToCheck);
            }
            return -1;
        }
        return -1;
    }

    public boolean isTrimmed(CyberwareSlot slot, int index) {
        InstalledCyberware installed = this.get(slot, index);
        if (installed == null) {
            return false;
        }
        ItemStack st = installed.getItem();
        if (st == null || st.isEmpty()) {
            return false;
        }
        return st.get(DataComponents.TRIM) != null;
    }

    public boolean isTrimmed(Item item, CyberwareSlot slotToCheck) {
        if (item == null) {
            return false;
        }
        InstalledCyberware[] arr = this.slots.get((Object)slotToCheck);
        if (arr == null) {
            return false;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware installed = arr[i];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !st.is(item)) continue;
            return st.get(DataComponents.TRIM) != null;
        }
        return false;
    }

    public ResourceLocation trimMaterialId(Item item, CyberwareSlot slotToCheck) {
        if (item == null) {
            return null;
        }
        InstalledCyberware[] arr = this.slots.get((Object)slotToCheck);
        if (arr == null) {
            return null;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware installed = arr[i];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !st.is(item)) continue;
            ArmorTrim trim = (ArmorTrim)st.get(DataComponents.TRIM);
            if (trim == null) {
                return null;
            }
            return trim.material().unwrapKey().map(k -> k.location()).orElse(null);
        }
        return null;
    }

    public ResourceLocation trimPatternId(Item item, CyberwareSlot slotToCheck) {
        if (item == null) {
            return null;
        }
        InstalledCyberware[] arr = this.slots.get((Object)slotToCheck);
        if (arr == null) {
            return null;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware installed = arr[i];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !st.is(item)) continue;
            ArmorTrim trim = (ArmorTrim)st.get(DataComponents.TRIM);
            if (trim == null) {
                return null;
            }
            return trim.pattern().unwrapKey().map(k -> k.location()).orElse(null);
        }
        return null;
    }

    public int trimColor(Item item, CyberwareSlot slotToCheck) {
        if (item == null) {
            return -1;
        }
        InstalledCyberware[] arr = this.slots.get((Object)slotToCheck);
        if (arr == null) {
            return -1;
        }
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware installed = arr[i];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !st.is(item)) continue;
            ArmorTrim trim = (ArmorTrim)st.get(DataComponents.TRIM);
            if (trim == null) {
                return -1;
            }
            return TrimColorPresets.colorFor((Holder<TrimMaterial>)trim.material());
        }
        return -1;
    }

    public int trimColor(CyberwareSlot slot, int index) {
        InstalledCyberware installed = this.get(slot, index);
        if (installed == null) {
            return -1;
        }
        ItemStack st = installed.getItem();
        if (st == null || st.isEmpty()) {
            return -1;
        }
        ArmorTrim trim = (ArmorTrim)st.get(DataComponents.TRIM);
        if (trim == null) {
            return -1;
        }
        return TrimColorPresets.colorFor((Holder<TrimMaterial>)trim.material());
    }

    public ItemStack getSpinalInjectorStack(int slot) {
        if (slot < 0 || slot >= this.spinalInjectorInv.length) {
            return ItemStack.EMPTY;
        }
        ItemStack st = this.spinalInjectorInv[slot];
        return st == null ? ItemStack.EMPTY : st;
    }

    public void setSpinalInjectorStack(int slot, ItemStack stack) {
        if (slot < 0 || slot >= this.spinalInjectorInv.length) {
            return;
        }
        if (stack == null || stack.isEmpty() || !SpinalInjectorItem.isInjectable(stack)) {
            this.spinalInjectorInv[slot] = ItemStack.EMPTY;
            this.dirty = true;
            return;
        }
        ItemStack copy = stack.copy();
        int cap = SpinalInjectorItem.maxStackFor(copy);
        if (copy.getCount() > cap) {
            copy.setCount(cap);
        }
        this.spinalInjectorInv[slot] = copy;
        this.dirty = true;
    }

    public void clearSpinalInjectorInventory() {
        for (int i = 0; i < this.spinalInjectorInv.length; ++i) {
            this.spinalInjectorInv[i] = ItemStack.EMPTY;
        }
        this.dirty = true;
    }

    public ItemStack getArmCannonStack(int slot) {
        if (slot < 0 || slot >= this.armCannonInv.length) {
            return ItemStack.EMPTY;
        }
        ItemStack st = this.armCannonInv[slot];
        return st == null ? ItemStack.EMPTY : st;
    }

    public void setArmCannonStack(int slot, ItemStack stack) {
        if (slot < 0 || slot >= this.armCannonInv.length) {
            return;
        }
        if (stack == null || stack.isEmpty() || !ArmCannonItem.isValidStoredItem(stack)) {
            this.armCannonInv[slot] = ItemStack.EMPTY;
            this.dirty = true;
            return;
        }
        ItemStack copy = stack.copy();
        int cap = Math.max(1, copy.getMaxStackSize());
        if (copy.getCount() > cap) {
            copy.setCount(cap);
        }
        this.armCannonInv[slot] = copy;
        this.dirty = true;
    }

    public void clearArmCannonInventory() {
        for (int i = 0; i < this.armCannonInv.length; ++i) {
            this.armCannonInv[i] = ItemStack.EMPTY;
        }
        this.dirty = true;
    }

    @Override
    public void clear() {
        int i;
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            this.slots.put(slot, new InstalledCyberware[slot.size]);
            boolean[] en = this.enabled.get((Object)slot);
            if (en == null || en.length != slot.size) {
                en = new boolean[slot.size];
                this.enabled.put(slot, en);
            }
            for (int i2 = 0; i2 < en.length; ++i2) {
                en[i2] = true;
            }
        }
        for (i = 0; i < this.spinalInjectorInv.length; ++i) {
            this.spinalInjectorInv[i] = ItemStack.EMPTY;
        }
        for (i = 0; i < this.armCannonInv.length; ++i) {
            this.armCannonInv[i] = ItemStack.EMPTY;
        }
        for (i = 0; i < this.chipwareInv.length; ++i) {
            this.chipwareInv[i] = ItemStack.EMPTY;
        }
        for (i = 0; i < this.heatEngineInv.length; ++i) {
            this.heatEngineInv[i] = ItemStack.EMPTY;
        }
        this.heatEngineBurnTime = 0;
        this.heatEngineBurnTimeTotal = 0;
        this.heatEngineCookTime = 0;
        this.heatEngineCookTimeTotal = 200;
        this.armCannonSelected = 0;
        this.copernicusOxygen = 0;
        this.copernicusOxygenSecondTicker = 0;
        this.dirty = true;
    }

    public int getCopernicusOxygen() {
        return Math.max(0, this.copernicusOxygen);
    }

    public void setCopernicusOxygen(int value, int maxDisplayUnits) {
        int max = Math.max(0, maxDisplayUnits);
        int clamped = Mth.clamp((int)value, (int)0, (int)max);
        if (clamped != this.copernicusOxygen) {
            this.copernicusOxygen = clamped;
            this.dirty = true;
        }
    }

    public boolean getCopernicusOxygenatedEnvironment() {
        return this.copernicusOxygenatedEnvironment;
    }

    public void setCopernicusOxygenatedEnvironment(boolean oxygenated) {
        this.copernicusOxygenatedEnvironment = oxygenated;
    }

    public void tickCopernicusOxygen(boolean oxygenatedEnvironment, int depletionPerSecond, int rechargePerSecond, int maxDisplayUnits) {
        ++this.copernicusOxygenSecondTicker;
        if (this.copernicusOxygenSecondTicker < 20) {
            return;
        }
        this.copernicusOxygenSecondTicker = 0;
        int oxy = this.getCopernicusOxygen();
        oxy = oxygenatedEnvironment ? Math.min(maxDisplayUnits, oxy + Math.max(0, rechargePerSecond)) : Math.max(0, oxy - Math.max(0, depletionPerSecond));
        this.setCopernicusOxygen(oxy, maxDisplayUnits);
    }

    public boolean isHeatEngineActive() {
        return this.heatEngineBurnTime > 0;
    }

    public int getHeatEngineBurnTime() {
        return Math.max(0, this.heatEngineBurnTime);
    }

    public int getHeatEngineBurnTimeTotal() {
        return Math.max(0, this.heatEngineBurnTimeTotal);
    }

    public int getHeatEngineCookTime() {
        return Math.max(0, this.heatEngineCookTime);
    }

    public int getHeatEngineCookTimeTotal() {
        return Math.max(1, this.heatEngineCookTimeTotal);
    }

    public ItemStack getHeatEngineStack(int slot) {
        if (slot < 0 || slot >= this.heatEngineInv.length) {
            return ItemStack.EMPTY;
        }
        ItemStack st = this.heatEngineInv[slot];
        return st == null ? ItemStack.EMPTY : st;
    }

    public void setHeatEngineStack(int slot, ItemStack stack) {
        if (slot < 0 || slot >= this.heatEngineInv.length) {
            return;
        }
        if (stack == null || stack.isEmpty()) {
            this.heatEngineInv[slot] = ItemStack.EMPTY;
            this.dirty = true;
            return;
        }
        ItemStack copy = stack.copy();
        int cap = Math.max(1, copy.getMaxStackSize());
        if (copy.getCount() > cap) {
            copy.setCount(cap);
        }
        this.heatEngineInv[slot] = copy;
        this.dirty = true;
    }

    public ItemStack removeHeatEngineStack(int slot, int amount) {
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack cur = this.getHeatEngineStack(slot);
        if (cur.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int taken = Math.min(amount, cur.getCount());
        ItemStack out = cur.copy();
        out.setCount(taken);
        cur.shrink(taken);
        if (cur.isEmpty()) {
            this.heatEngineInv[slot] = ItemStack.EMPTY;
        }
        this.dirty = true;
        return out;
    }

    public void tickHeatEngine(ServerPlayer player) {
        int burn;
        ItemStack fuel;
        if (player == null) {
            return;
        }
        Level level = player.level();
        if (level.isClientSide) {
            return;
        }
        if (this.heatEngineBurnTime <= 0 && !(fuel = this.getHeatEngineStack(0)).isEmpty() && AbstractFurnaceBlockEntity.isFuel((ItemStack)fuel) && (burn = fuel.getBurnTime(RecipeType.SMELTING)) > 0) {
            ItemStack remainder = ItemStack.EMPTY;
            Item remainderItem = fuel.getItem().getCraftingRemainingItem();
            if (remainderItem != null) {
                remainder = new ItemStack((ItemLike)remainderItem);
            }
            this.removeHeatEngineStack(0, 1);
            if (!remainder.isEmpty() && !player.getInventory().add(remainder)) {
                player.drop(remainder, false);
            }
            this.heatEngineBurnTime = burn;
            this.heatEngineBurnTimeTotal = burn;
            this.dirty = true;
        }
        if (this.heatEngineBurnTime > 0) {
            --this.heatEngineBurnTime;
            this.receiveEnergy((Player)player, 50);
            this.dirty = true;
            this.tickHeatEngineSmelt(level);
        } else if (this.heatEngineCookTime != 0) {
            this.heatEngineCookTime = 0;
            this.dirty = true;
        }
    }

    private void tickHeatEngineSmelt(Level level) {
        ItemStack in = this.getHeatEngineStack(1);
        if (in.isEmpty()) {
            if (this.heatEngineCookTime != 0) {
                this.heatEngineCookTime = 0;
                this.dirty = true;
            }
            return;
        }
        SingleRecipeInput input = new SingleRecipeInput(in);
        Optional opt = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, (RecipeInput)input, level);
        if (opt.isEmpty()) {
            if (this.heatEngineCookTime != 0) {
                this.heatEngineCookTime = 0;
                this.dirty = true;
            }
            return;
        }
        RecipeHolder holder = (RecipeHolder)opt.get();
        ItemStack result = ((SmeltingRecipe)holder.value()).assemble(input, (HolderLookup.Provider)level.registryAccess());
        if (result.isEmpty() || !this.canAcceptSmeltResult(result)) {
            if (this.heatEngineCookTime != 0) {
                this.heatEngineCookTime = 0;
                this.dirty = true;
            }
            return;
        }
        this.heatEngineCookTimeTotal = Math.max(1, ((SmeltingRecipe)holder.value()).getCookingTime());
        ++this.heatEngineCookTime;
        this.dirty = true;
        if (this.heatEngineCookTime >= this.heatEngineCookTimeTotal) {
            this.removeHeatEngineStack(1, 1);
            ItemStack out = this.getHeatEngineStack(2);
            if (out.isEmpty()) {
                this.setHeatEngineStack(2, result);
            } else {
                out.grow(result.getCount());
                this.setHeatEngineStack(2, out);
            }
            this.heatEngineCookTime = 0;
            this.dirty = true;
        }
    }

    private boolean canAcceptSmeltResult(ItemStack result) {
        ItemStack out = this.getHeatEngineStack(2);
        if (out.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents((ItemStack)out, (ItemStack)result)) {
            return false;
        }
        int max = out.getMaxStackSize();
        return out.getCount() + result.getCount() <= max;
    }

    public void setHeatEngineBurnTime(int v) {
        this.heatEngineBurnTime = Math.max(0, v);
        this.dirty = true;
    }

    public void setHeatEngineBurnTimeTotal(int v) {
        this.heatEngineBurnTimeTotal = Math.max(0, v);
        this.dirty = true;
    }

    public void setHeatEngineCookTime(int v) {
        this.heatEngineCookTime = Math.max(0, v);
        this.dirty = true;
    }

    public void setHeatEngineCookTimeTotal(int v) {
        this.heatEngineCookTimeTotal = Math.max(1, v);
        this.dirty = true;
    }

    @Override
    public void setDirty() {
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void clean() {
        this.dirty = false;
    }

    public void resetToDefaultOrgans() {
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            InstalledCyberware[] arr = this.getAll().get((Object)slot);
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack def = DefaultOrgans.get(slot, i);
                if (def == null || def.isEmpty()) {
                    arr[i] = null;
                    this.setEnabled(slot, i, true);
                    continue;
                }
                int humanityCost = 0;
                Item item = def.getItem();
                if (item instanceof ICyberwareItem) {
                    ICyberwareItem cyberwareItem = (ICyberwareItem)item;
                    humanityCost = cyberwareItem.getHumanityCost();
                }
                arr[i] = new InstalledCyberware(def, slot, i, humanityCost);
                arr[i].setPowered(true);
                this.setEnabled(slot, i, true);
            }
        }
        this.dirty = true;
    }

    public int getEnergyStored() {
        return this.energyStored;
    }

    public void setEnergyStored(Player player, int value) {
        int cap = this.getTotalEnergyCapacity(player);
        int clamped = Mth.clamp((int)value, (int)0, (int)cap);
        if (clamped != this.energyStored) {
            this.energyStored = clamped;
            this.dirty = true;
        }
    }

    public int getTotalEnergyCapacity(Player player) {
        int total = 0;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : this.slots.entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ICyberwareItem item;
                int cap;
                Item item2;
                ItemStack stack;
                InstalledCyberware cw = arr[i];
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !((item2 = stack.getItem()) instanceof ICyberwareItem) || (cap = (item = (ICyberwareItem)item2).getEnergyCapacity(player, stack, slot)) <= 0) continue;
                total += cap;
            }
        }
        return Math.max(0, total);
    }

    public int receiveEnergy(Player player, int amount) {
        if (amount <= 0) {
            return 0;
        }
        int cap = this.getTotalEnergyCapacity(player);
        if (cap <= 0) {
            return 0;
        }
        int before = this.energyStored;
        int after = Mth.clamp((int)(before + amount), (int)0, (int)cap);
        if (after != before) {
            this.energyStored = after;
            this.dirty = true;
        }
        return after - before;
    }

    public int extractEnergy(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int before = this.energyStored;
        int after = Math.max(0, before - amount);
        if (after != before) {
            this.energyStored = after;
            this.dirty = true;
        }
        return before - after;
    }

    public boolean tryConsumeEnergy(int amount) {
        if (amount <= 0) {
            return true;
        }
        if (this.energyStored < amount) {
            return false;
        }
        this.energyStored -= amount;
        this.dirty = true;
        return true;
    }

    public void clampEnergyToCapacity(Player player) {
        int cap = this.getTotalEnergyCapacity(player);
        int clamped = Mth.clamp((int)this.energyStored, (int)0, (int)cap);
        if (clamped != this.energyStored) {
            this.energyStored = clamped;
            this.dirty = true;
        }
    }

    public int getNeuropozyneApplyCount() {
        return Math.max(0, this.neuropozyneApplyCount);
    }

    public int incrementNeuropozyneApplyCount() {
        this.neuropozyneApplyCount = Math.max(0, this.neuropozyneApplyCount) + 1;
        this.dirty = true;
        return this.neuropozyneApplyCount;
    }

    public void resetNeuropozyneApplyCount() {
        this.neuropozyneApplyCount = 0;
        this.dirty = true;
    }

    private static CompoundTag cc$saveStackToCompound(HolderLookup.Provider provider, ItemStack stack) {
        CompoundTag ct;
        Tag t = stack.save(provider);
        return t instanceof CompoundTag ? (ct = (CompoundTag)t) : new CompoundTag();
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : this.slots.entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack stack;
                InstalledCyberware installed = arr[i];
                if (installed == null || (stack = installed.getItem()) == null || stack.isEmpty()) continue;
                CompoundTag c = installed.save(provider);
                c.putString("SlotGroup", slot.name());
                c.putInt("Index", i);
                c.putBoolean("Enabled", this.isEnabled(slot, i));
                list.add((Object)c);
            }
        }
        tag.put(NBT_CYBERWARE, (Tag)list);
        tag.putInt(NBT_HUMANITY, this.humanity);
        tag.putInt(NBT_ENERGY, this.energyStored);
        tag.putInt(NBT_ARM_CANNON_SELECTED, this.getArmCannonSelected());
        ListTag inj = new ListTag();
        for (int i = 0; i < this.spinalInjectorInv.length; ++i) {
            CompoundTag c = new CompoundTag();
            ItemStack st = this.spinalInjectorInv[i];
            if (st != null && !st.isEmpty() && SpinalInjectorItem.isInjectable(st)) {
                ItemStack copy = st.copy();
                int cap = SpinalInjectorItem.maxStackFor(copy);
                if (copy.getCount() > cap) {
                    copy.setCount(cap);
                }
                copy.save(provider, (Tag)c);
            }
            inj.add((Object)c);
        }
        tag.put(NBT_SPINAL_INJECTOR_INV, (Tag)inj);
        ListTag arm = new ListTag();
        for (int i = 0; i < this.armCannonInv.length; ++i) {
            CompoundTag c = new CompoundTag();
            ItemStack st = this.armCannonInv[i];
            if (st != null && !st.isEmpty()) {
                ItemStack copy = st.copy();
                int cap = Math.max(1, copy.getMaxStackSize());
                if (copy.getCount() > cap) {
                    copy.setCount(cap);
                }
                copy.save(provider, (Tag)c);
            }
            arm.add((Object)c);
        }
        tag.put(NBT_ARM_CANNON_INV, (Tag)arm);
        ListTag chip = new ListTag();
        for (int i = 0; i < this.chipwareInv.length; ++i) {
            ItemStack st = this.chipwareInv[i];
            if (st != null && !st.isEmpty() && st.is(ModTags.Items.DATA_SHARDS)) {
                ItemStack copy = st.copy();
                copy.setCount(1);
                chip.add((Object)PlayerCyberwareData.cc$saveStackToCompound(provider, copy));
                continue;
            }
            chip.add((Object)new CompoundTag());
        }
        ListTag heat = new ListTag();
        for (int i = 0; i < this.heatEngineInv.length; ++i) {
            ItemStack st = this.heatEngineInv[i];
            heat.add((Object)(st != null && !st.isEmpty() ? PlayerCyberwareData.cc$saveStackToCompound(provider, st) : new CompoundTag()));
        }
        tag.put(NBT_HEAT_ENGINE_INV, (Tag)heat);
        tag.putInt(NBT_HEAT_ENGINE_BURN, this.heatEngineBurnTime);
        tag.putInt(NBT_HEAT_ENGINE_BURN_TOTAL, this.heatEngineBurnTimeTotal);
        tag.putInt(NBT_HEAT_ENGINE_COOK, this.heatEngineCookTime);
        tag.putInt(NBT_HEAT_ENGINE_COOK_TOTAL, this.heatEngineCookTimeTotal);
        tag.put(NBT_CHIPWARE_INV, (Tag)chip);
        tag.putInt(NBT_NEUROPOZYNE_APPLY_COUNT, this.neuropozyneApplyCount);
        tag.putInt(NBT_COPERNICUS_OXYGEN, this.copernicusOxygen);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag, HolderLookup.Provider provider) {
        CompoundTag c;
        int i;
        this.clear();
        ListTag list = tag.getList(NBT_CYBERWARE, 10);
        for (i = 0; i < list.size(); ++i) {
            CompoundTag c2 = list.getCompound(i);
            if (!c2.contains("SlotGroup", 8)) continue;
            CyberwareSlot slot = CyberwareSlot.valueOf(c2.getString("SlotGroup"));
            int index = c2.getInt("Index");
            InstalledCyberware loaded = InstalledCyberware.load(c2, provider);
            InstalledCyberware[] arr = this.slots.get((Object)slot);
            if (arr == null || index < 0 || index >= arr.length) continue;
            arr[index] = loaded;
            boolean en = c2.contains("Enabled", 1) ? c2.getBoolean("Enabled") : true;
            this.setEnabled(slot, index, en);
        }
        if (tag.contains(NBT_HUMANITY, 3)) {
            this.humanity = tag.getInt(NBT_HUMANITY);
        } else {
            this.humanity = ConfigValues.BASE_HUMANITY;
            this.recomputeHumanityBaseFromInstalled();
        }
        this.humanityBonus = 0;
        this.energyStored = tag.contains(NBT_ENERGY, 3) ? tag.getInt(NBT_ENERGY) : 0;
        this.armCannonSelected = tag.contains(NBT_ARM_CANNON_SELECTED, 3) ? Mth.clamp((int)tag.getInt(NBT_ARM_CANNON_SELECTED), (int)0, (int)3) : 0;
        for (i = 0; i < this.spinalInjectorInv.length; ++i) {
            this.spinalInjectorInv[i] = ItemStack.EMPTY;
        }
        if (tag.contains(NBT_SPINAL_INJECTOR_INV, 9)) {
            ListTag inj = tag.getList(NBT_SPINAL_INJECTOR_INV, 10);
            for (int i2 = 0; i2 < this.spinalInjectorInv.length && i2 < inj.size(); ++i2) {
                c = inj.getCompound(i2);
                ItemStack st = ItemStack.parseOptional((HolderLookup.Provider)provider, (CompoundTag)c);
                if (st.isEmpty() || !SpinalInjectorItem.isInjectable(st)) {
                    this.spinalInjectorInv[i2] = ItemStack.EMPTY;
                    continue;
                }
                int cap = SpinalInjectorItem.maxStackFor(st);
                if (st.getCount() > cap) {
                    st.setCount(cap);
                }
                this.spinalInjectorInv[i2] = st;
            }
        }
        if (tag.contains(NBT_ARM_CANNON_INV, 9)) {
            ListTag arm = tag.getList(NBT_ARM_CANNON_INV, 10);
            for (int i3 = 0; i3 < this.armCannonInv.length && i3 < arm.size(); ++i3) {
                c = arm.getCompound(i3);
                ItemStack st = ItemStack.parseOptional((HolderLookup.Provider)provider, (CompoundTag)c);
                if (st.isEmpty()) {
                    this.armCannonInv[i3] = ItemStack.EMPTY;
                    continue;
                }
                int cap = Math.max(1, st.getMaxStackSize());
                if (st.getCount() > cap) {
                    st.setCount(cap);
                }
                this.armCannonInv[i3] = st;
            }
        }
        for (int i4 = 0; i4 < this.chipwareInv.length; ++i4) {
            this.chipwareInv[i4] = ItemStack.EMPTY;
        }
        if (tag.contains(NBT_CHIPWARE_INV, 9)) {
            ListTag chip = tag.getList(NBT_CHIPWARE_INV, 10);
            for (int i5 = 0; i5 < this.chipwareInv.length && i5 < chip.size(); ++i5) {
                c = chip.getCompound(i5);
                ItemStack st = ItemStack.parseOptional((HolderLookup.Provider)provider, (CompoundTag)c);
                if (st.isEmpty() || !st.is(ModTags.Items.DATA_SHARDS)) {
                    this.chipwareInv[i5] = ItemStack.EMPTY;
                    continue;
                }
                st.setCount(1);
                this.chipwareInv[i5] = st;
            }
        }
        for (int i6 = 0; i6 < this.heatEngineInv.length; ++i6) {
            this.heatEngineInv[i6] = ItemStack.EMPTY;
        }
        if (tag.contains(NBT_HEAT_ENGINE_INV, 9)) {
            ListTag heat = tag.getList(NBT_HEAT_ENGINE_INV, 10);
            for (int i7 = 0; i7 < this.heatEngineInv.length && i7 < heat.size(); ++i7) {
                this.heatEngineInv[i7] = ItemStack.parseOptional((HolderLookup.Provider)provider, (CompoundTag)heat.getCompound(i7));
            }
        }
        this.heatEngineBurnTime = tag.contains(NBT_HEAT_ENGINE_BURN, 3) ? Math.max(0, tag.getInt(NBT_HEAT_ENGINE_BURN)) : 0;
        this.heatEngineBurnTimeTotal = tag.contains(NBT_HEAT_ENGINE_BURN_TOTAL, 3) ? Math.max(0, tag.getInt(NBT_HEAT_ENGINE_BURN_TOTAL)) : 0;
        this.heatEngineCookTime = tag.contains(NBT_HEAT_ENGINE_COOK, 3) ? Math.max(0, tag.getInt(NBT_HEAT_ENGINE_COOK)) : 0;
        this.heatEngineCookTimeTotal = tag.contains(NBT_HEAT_ENGINE_COOK_TOTAL, 3) ? Math.max(1, tag.getInt(NBT_HEAT_ENGINE_COOK_TOTAL)) : 200;
        this.neuropozyneApplyCount = tag.contains(NBT_NEUROPOZYNE_APPLY_COUNT, 3) ? Math.max(0, tag.getInt(NBT_NEUROPOZYNE_APPLY_COUNT)) : 0;
        this.copernicusOxygen = tag.contains(NBT_COPERNICUS_OXYGEN, 3) ? Math.max(0, tag.getInt(NBT_COPERNICUS_OXYGEN)) : 0;
        this.dirty = false;
    }

    public boolean commandInstall(Player player, ItemStack stack) {
        if (player == null) {
            return false;
        }
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        if (!(item instanceof ICyberwareItem)) {
            return false;
        }
        ICyberwareItem cw = (ICyberwareItem)item;
        ItemStack installStack = stack.copy();
        installStack.setCount(1);
        InstallTarget target = this.findInstallTargetForCommand(installStack, cw);
        if (target == null) {
            return false;
        }
        if (cw.replacesOrgan()) {
            for (CyberwareSlot replaced : cw.getReplacedOrgans()) {
                InstalledCyberware installedCyberware = this.removeFirstNonEmptyInSlotGroup(player, replaced);
            }
        }
        int humanityCost = cw.getHumanityCost();
        InstalledCyberware installed = new InstalledCyberware(installStack, target.slot, target.index, humanityCost);
        installed.setPowered(true);
        this.set(target.slot, target.index, installed);
        cw.onInstalled(player);
        this.recomputeHumanityBaseFromInstalled();
        this.clampEnergyToCapacity(player);
        return true;
    }

    public boolean commandRemove(Player player, Item item) {
        if (player == null) {
            return false;
        }
        if (item == null) {
            return false;
        }
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            InstalledCyberware[] arr = this.slots.get((Object)slot);
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware installed = arr[i];
                if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !st.is(item)) continue;
                this.remove(slot, i);
                if (item instanceof ICyberwareItem) {
                    ICyberwareItem cw = (ICyberwareItem)item;
                    cw.onRemoved(player);
                }
                this.recomputeHumanityBaseFromInstalled();
                this.clampEnergyToCapacity(player);
                return true;
            }
        }
        return false;
    }

    public Component commandListComponent() {
        MutableComponent root = Component.literal((String)"Installed Implants:");
        boolean any = false;
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            InstalledCyberware[] arr = this.slots.get((Object)slot);
            if (arr == null) continue;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arr.length; ++i) {
                ItemStack def;
                ItemStack st;
                InstalledCyberware installed = arr[i];
                if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || (def = DefaultOrgans.get(slot, i)) != null && !def.isEmpty() && ItemStack.isSameItemSameComponents((ItemStack)st, (ItemStack)def)) continue;
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(st.getHoverName().getString());
            }
            if (sb.isEmpty()) continue;
            any = true;
            root.append((Component)Component.literal((String)("\n" + slot.name() + ": " + String.valueOf(sb))));
        }
        if (!any) {
            root.append((Component)Component.literal((String)"\n(X)"));
        }
        return root;
    }

    private InstallTarget findInstallTargetForCommand(ItemStack stack, ICyberwareItem cw) {
        if (cw.replacesOrgan()) {
            for (CyberwareSlot replaced : cw.getReplacedOrgans()) {
                InstallTarget t = this.findFirstValidSpaceInSlotGroup(stack, cw, replaced);
                if (t == null) continue;
                return t;
            }
        }
        for (CyberwareSlot slot : CyberwareSlot.values()) {
            InstallTarget t;
            if (!cw.supportsSlot(slot) || (t = this.findFirstValidSpaceInSlotGroup(stack, cw, slot)) == null) continue;
            return t;
        }
        return null;
    }

    private InstallTarget findFirstValidSpaceInSlotGroup(ItemStack incoming, ICyberwareItem cw, CyberwareSlot slot) {
        InstalledCyberware[] arr = this.slots.get((Object)slot);
        if (arr == null) {
            return null;
        }
        int max = Math.max(1, cw.maxStacksPerSlotType(incoming, slot));
        int already = 0;
        for (InstalledCyberware installed : arr) {
            ItemStack st;
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !ItemStack.isSameItemSameComponents((ItemStack)st, (ItemStack)incoming)) continue;
            ++already;
        }
        if (already >= max) {
            return null;
        }
        for (int i = 0; i < arr.length; ++i) {
            InstalledCyberware installed = arr[i];
            if (installed != null && installed.getItem() != null && !installed.getItem().isEmpty()) continue;
            return new InstallTarget(slot, i);
        }
        return null;
    }

    private InstalledCyberware removeFirstNonEmptyInSlotGroup(Player player, CyberwareSlot slot) {
        InstalledCyberware[] arr = this.slots.get((Object)slot);
        if (arr == null) {
            return null;
        }
        for (int i = 0; i < arr.length; ++i) {
            Item removedItem;
            ItemStack st;
            InstalledCyberware installed = arr[i];
            if (installed == null || (st = installed.getItem()) == null || st.isEmpty()) continue;
            InstalledCyberware removed = this.remove(slot, i);
            Item item = removedItem = st != null ? st.getItem() : null;
            if (removedItem instanceof ICyberwareItem) {
                ICyberwareItem cw = (ICyberwareItem)removedItem;
                cw.onRemoved(player);
            }
            return removed;
        }
        return null;
    }

    public static CompoundTag createSnapshotTagFor(Player player, HolderLookup.Provider provider) {
        PlayerCyberwareData data = PlayerCyberwareData.getForVisual(player, (HolderLookup.Provider)player.registryAccess());
        if (data == null) {
            return new CompoundTag();
        }
        return data.serializeNBT(provider);
    }

    public static PlayerCyberwareData fromSnapshotTag(CompoundTag tag, HolderLookup.Provider provider) {
        PlayerCyberwareData d = new PlayerCyberwareData();
        if (tag != null && !tag.isEmpty()) {
            d.deserializeNBT(tag, provider);
        }
        return d;
    }

    public static PlayerCyberwareData getForVisual(Player player, HolderLookup.Provider provider) {
        if (player == null) {
            return null;
        }
        CompoundTag pd = player.getPersistentData();
        if (pd.getBoolean(HOLO_SNAPSHOT_FLAG) && pd.contains(HOLO_SNAPSHOT_CYBERWARE, 10)) {
            CompoundTag snap = pd.getCompound(HOLO_SNAPSHOT_CYBERWARE);
            return PlayerCyberwareData.fromSnapshotTag(snap, provider);
        }
        return (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
    }

    private record InstallTarget(CyberwareSlot slot, int index) {
    }
}

