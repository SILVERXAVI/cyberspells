/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.common.Tags$Items
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class SpinalInjectorItem
extends Item
implements ICyberwareItem {
    public static final int SLOT_COUNT = 4;
    public static final int STACK_FACTOR = 4;
    private static final String ENTRY_ITEM = "item";
    private static final String ENTRY_COUNT = "count";
    private static final String STACK_ROOT = "cc_spinal_injector";
    private static final String STACK_INV = "inv";
    private static final int ONE_MINECRAFT_DAY_TICKS = 24000;
    private final int humanityCost;
    private static final String INJECT_COOLDOWN_TAG = "cc_spinal_injector_cd";
    private static final int INJECT_COOLDOWN_TICKS = 20;

    public SpinalInjectorItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.BONE);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    public static boolean isInjectable(ItemStack stack) {
        return !stack.isEmpty() && stack.is(Tags.Items.POTIONS);
    }

    public static int maxStackFor(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        int base = Math.max(1, stack.getMaxStackSize());
        return Math.min(64, base * 4);
    }

    private static CompoundTag getOrCreateRoot(ItemStack injectorStack) {
        CustomData cd = (CustomData)injectorStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY);
        CompoundTag all = cd.copyTag();
        if (!all.contains(STACK_ROOT, 10)) {
            all.put(STACK_ROOT, (Tag)new CompoundTag());
        }
        return all;
    }

    private static CompoundTag getRootView(ItemStack injectorStack) {
        CustomData cd = (CustomData)injectorStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY);
        CompoundTag all = cd.copyTag();
        return all.contains(STACK_ROOT, 10) ? all.getCompound(STACK_ROOT) : new CompoundTag();
    }

    private static void writeBack(ItemStack injectorStack, CompoundTag all) {
        injectorStack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)all));
    }

    public static void loadFromInstalledStack(ItemStack injectorStack, HolderLookup.Provider provider, Container intoInv, int[] counts) {
        for (int i = 0; i < 4; ++i) {
            intoInv.setItem(i, ItemStack.EMPTY);
            if (counts == null || i >= counts.length) continue;
            counts[i] = 0;
        }
        if (injectorStack == null || injectorStack.isEmpty()) {
            return;
        }
        CompoundTag root = SpinalInjectorItem.getRootView(injectorStack);
        if (!root.contains(STACK_INV, 9)) {
            return;
        }
        ListTag list = root.getList(STACK_INV, 10);
        for (int i = 0; i < 4 && i < list.size(); ++i) {
            int c;
            int cap;
            CompoundTag entry = list.getCompound(i);
            if (entry.contains(ENTRY_ITEM, 10)) {
                ItemStack base = ItemStack.parseOptional((HolderLookup.Provider)provider, (CompoundTag)entry.getCompound(ENTRY_ITEM));
                if (base.isEmpty() || !SpinalInjectorItem.isInjectable(base)) continue;
                cap = SpinalInjectorItem.maxStackFor(base);
                c = entry.contains(ENTRY_COUNT, 3) ? entry.getInt(ENTRY_COUNT) : 0;
                c = Math.max(0, Math.min(cap, c));
                base.setCount(1);
                intoInv.setItem(i, base);
                if (counts == null || i >= counts.length) continue;
                counts[i] = c;
                continue;
            }
            ItemStack legacy = ItemStack.parseOptional((HolderLookup.Provider)provider, (CompoundTag)entry);
            if (legacy.isEmpty() || !SpinalInjectorItem.isInjectable(legacy)) continue;
            cap = SpinalInjectorItem.maxStackFor(legacy);
            c = legacy.getCount();
            c = Math.max(0, Math.min(cap, c));
            legacy.setCount(1);
            intoInv.setItem(i, legacy);
            if (counts == null || i >= counts.length) continue;
            counts[i] = c;
        }
    }

    public static void saveIntoInstalledStack(ItemStack injectorStack, HolderLookup.Provider provider, Container fromInv, int[] counts) {
        if (injectorStack == null || injectorStack.isEmpty()) {
            return;
        }
        ListTag list = new ListTag();
        for (int i = 0; i < 4; ++i) {
            int c;
            ItemStack base = fromInv.getItem(i);
            int n = c = counts != null && i < counts.length ? counts[i] : 0;
            if (!base.isEmpty() && SpinalInjectorItem.isInjectable(base) && c > 0) {
                int cap = SpinalInjectorItem.maxStackFor(base);
                c = Math.min(cap, c);
                ItemStack rep = base.copy();
                rep.setCount(1);
                CompoundTag entry = new CompoundTag();
                entry.put(ENTRY_ITEM, rep.save(provider));
                entry.putInt(ENTRY_COUNT, c);
                list.add((Object)entry);
                continue;
            }
            list.add((Object)new CompoundTag());
        }
        CompoundTag all = SpinalInjectorItem.getOrCreateRoot(injectorStack);
        CompoundTag root = all.getCompound(STACK_ROOT);
        root.put(STACK_INV, (Tag)list);
        all.put(STACK_ROOT, (Tag)root);
        SpinalInjectorItem.writeBack(injectorStack, all);
    }

    public static void dropAndClearInstalledStack(ServerPlayer sp, HolderLookup.Provider provider, ItemStack injectorStack) {
        if (injectorStack == null || injectorStack.isEmpty()) {
            return;
        }
        SimpleContainer tmp = new SimpleContainer(4);
        int[] counts = new int[4];
        SpinalInjectorItem.loadFromInstalledStack(injectorStack, provider, (Container)tmp, counts);
        for (int i = 0; i < 4; ++i) {
            ItemStack base = tmp.getItem(i);
            int c = counts[i];
            if (base.isEmpty() || c <= 0) continue;
            ItemStack one = base.copy();
            one.setCount(1);
            for (int n = 0; n < c; ++n) {
                sp.drop(one.copy(), false);
            }
        }
        CustomData cd = (CustomData)injectorStack.getOrDefault(DataComponents.CUSTOM_DATA, (Object)CustomData.EMPTY);
        CompoundTag all = cd.copyTag();
        all.remove(STACK_ROOT);
        injectorStack.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)all));
    }

    @Override
    public void onRemoved(Player player) {
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
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware inst = arr[i];
                if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || st.getItem() != this) continue;
                SpinalInjectorItem.dropAndClearInstalledStack(sp, (HolderLookup.Provider)sp.level().registryAccess(), st);
            }
        }
        data.setDirty();
        sp.syncData(ModAttachments.CYBERWARE);
    }

    private static int getCooldown(ServerPlayer sp) {
        CompoundTag pd = sp.getPersistentData();
        return pd.contains(INJECT_COOLDOWN_TAG, 3) ? pd.getInt(INJECT_COOLDOWN_TAG) : 0;
    }

    private static void setCooldown(ServerPlayer sp, int ticks) {
        sp.getPersistentData().putInt(INJECT_COOLDOWN_TAG, Math.max(0, ticks));
    }

    private static void tickDownCooldown(ServerPlayer sp) {
        int cd = SpinalInjectorItem.getCooldown(sp);
        if (cd > 0) {
            SpinalInjectorItem.setCooldown(sp, cd - 1);
        }
    }

    private static void applyNeuropozyneDay(ServerPlayer sp) {
        Holder<MobEffect> neuropozyne = ModEffects.NEUROPOZYNE;
        sp.addEffect(new MobEffectInstance(neuropozyne, 24000, 0, false, true, true));
    }

    private static void applyNeuropozyneBoostDay(ServerPlayer sp) {
        Holder<MobEffect> neuropozyne = ModEffects.NEUROPOZYNE;
        MobEffectInstance cur = sp.getEffect(neuropozyne);
        int amp = cur == null ? 0 : Math.min(255, cur.getAmplifier() + 1);
        sp.addEffect(new MobEffectInstance(neuropozyne, 24000, amp, false, true, true));
    }

    private static void applyPotionContentsTo(ServerPlayer sp, PotionContents pc, float durationFactor) {
        if (pc == null || pc == PotionContents.EMPTY) {
            return;
        }
        pc.forEachEffect(inst -> {
            if (inst == null) {
                return;
            }
            Holder effectHolder = inst.getEffect();
            MobEffect effect = (MobEffect)effectHolder.value();
            if (effect.isInstantenous()) {
                effect.applyInstantenousEffect((Entity)sp, (Entity)sp, (LivingEntity)sp, inst.getAmplifier(), 1.0);
                return;
            }
            int dur = Mth.floor((float)((float)inst.getDuration() * durationFactor));
            if (dur <= 0) {
                return;
            }
            sp.addEffect(new MobEffectInstance(effectHolder, dur, inst.getAmplifier(), inst.isAmbient(), inst.isVisible(), inst.showIcon()));
        });
    }

    private static boolean tryInjectFromStack(ServerPlayer sp, PlayerCyberwareData data, ItemStack injectorStack) {
        if (injectorStack == null || injectorStack.isEmpty()) {
            return false;
        }
        if (!(injectorStack.getItem() instanceof SpinalInjectorItem)) {
            return false;
        }
        RegistryAccess provider = sp.level().registryAccess();
        SimpleContainer tmp = new SimpleContainer(4);
        int[] counts = new int[4];
        SpinalInjectorItem.loadFromInstalledStack(injectorStack, (HolderLookup.Provider)provider, (Container)tmp, counts);
        for (int i = 0; i < 4; ++i) {
            ItemStack base = tmp.getItem(i);
            int c = counts[i];
            if (base.isEmpty() || c <= 0 || !SpinalInjectorItem.isInjectable(base)) continue;
            if (base.is((Item)ModItems.NEUROPOZYNE_AUTOINJECTOR.get())) {
                Holder<MobEffect> cyberwareRejection = ModEffects.CYBERWARE_REJECTION;
                if (!sp.hasEffect(cyberwareRejection)) continue;
                if (sp.hasEffect(ModEffects.NEUROPOZYNE)) {
                    SpinalInjectorItem.applyNeuropozyneBoostDay(sp);
                } else {
                    SpinalInjectorItem.applyNeuropozyneDay(sp);
                }
                counts[i] = Math.max(0, counts[i] - 1);
                if (counts[i] <= 0) {
                    tmp.setItem(i, ItemStack.EMPTY);
                }
                SpinalInjectorItem.saveIntoInstalledStack(injectorStack, (HolderLookup.Provider)provider, (Container)tmp, counts);
                data.setDirty();
                sp.syncData(ModAttachments.CYBERWARE);
                SpinalInjectorItem.dropEmptyAutoInjectorBehind(sp);
                return true;
            }
            PotionContents pc = (PotionContents)base.getOrDefault(DataComponents.POTION_CONTENTS, (Object)PotionContents.EMPTY);
            if (pc == null || !pc.hasEffects()) continue;
            boolean missingNonInstant = false;
            for (MobEffectInstance inst : pc.getAllEffects()) {
                MobEffect effValue;
                Holder eff;
                if (inst == null || (eff = inst.getEffect()) == null || (effValue = (MobEffect)eff.value()) != null && effValue.isInstantenous() || sp.hasEffect(eff)) continue;
                missingNonInstant = true;
                break;
            }
            if (!missingNonInstant) continue;
            SpinalInjectorItem.applyPotionContentsTo(sp, pc, 2.0f);
            counts[i] = Math.max(0, counts[i] - 1);
            if (counts[i] <= 0) {
                tmp.setItem(i, ItemStack.EMPTY);
            }
            SpinalInjectorItem.saveIntoInstalledStack(injectorStack, (HolderLookup.Provider)provider, (Container)tmp, counts);
            data.setDirty();
            sp.syncData(ModAttachments.CYBERWARE);
            SpinalInjectorItem.dropEmptyBottleBehind(sp);
            return true;
        }
        return false;
    }

    private static void dropItemBehind(ServerPlayer sp, ItemStack drop) {
        if (drop == null || drop.isEmpty()) {
            return;
        }
        Vec3 look = sp.getLookAngle();
        Vec3 back = new Vec3(-look.x, 0.0, -look.z);
        back = back.lengthSqr() < 1.0E-6 ? new Vec3(0.0, 0.0, 1.0) : back.normalize();
        Vec3 offset = back.scale(0.35);
        double x = sp.getX() + offset.x;
        double y = sp.getY() + (double)sp.getBbHeight() * 0.65;
        double z = sp.getZ() + offset.z;
        ItemEntity ent = new ItemEntity(sp.level(), x, y, z, drop.copy());
        ent.setDefaultPickUpDelay();
        ent.setDeltaMovement(offset.x * 0.25, 0.1, offset.z * 0.25);
        sp.level().addFreshEntity((Entity)ent);
    }

    private static void dropEmptyBottleBehind(ServerPlayer sp) {
        SpinalInjectorItem.dropItemBehind(sp, new ItemStack((ItemLike)Items.GLASS_BOTTLE));
    }

    private static void dropEmptyAutoInjectorBehind(ServerPlayer sp) {
        SpinalInjectorItem.dropItemBehind(sp, new ItemStack((ItemLike)ModItems.EMPTY_AUTOINJECTOR.get()));
    }

    private static boolean tryInjectFromAnyInstalled(ServerPlayer sp, PlayerCyberwareData data) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware inst = arr[i];
                if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || !data.isEnabled(slot, i) || !(st.getItem() instanceof SpinalInjectorItem) || !SpinalInjectorItem.tryInjectFromStack(sp, data, st)) continue;
                return true;
            }
        }
        return false;
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class AutoInjectHandler {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();
            if (!(player instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer sp = (ServerPlayer)player;
            if (sp.level().isClientSide()) {
                return;
            }
            if (!sp.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)sp.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            int cd = SpinalInjectorItem.getCooldown(sp);
            if (cd > 0) {
                SpinalInjectorItem.tickDownCooldown(sp);
                return;
            }
            boolean injected = SpinalInjectorItem.tryInjectFromAnyInstalled(sp, data);
            if (injected) {
                SpinalInjectorItem.setCooldown(sp, 20);
            }
        }
    }
}

