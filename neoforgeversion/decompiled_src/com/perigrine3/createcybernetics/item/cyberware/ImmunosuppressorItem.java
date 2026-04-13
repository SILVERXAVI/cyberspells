/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.Holder
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ImmunosuppressorItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final String NBT_WEAK_LAST_AMP = "cc_immuno_weak_lastAmp";
    private static final String NBT_WEAK_LAST_DUR = "cc_immuno_weak_lastDur";
    private static final String NBT_POISON_LAST_AMP = "cc_immuno_poison_lastAmp";
    private static final String NBT_POISON_LAST_DUR = "cc_immuno_poison_lastDur";

    public ImmunosuppressorItem(Item.Properties props, int humanityCost) {
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
        return Set.of(CyberwareSlot.SKIN);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    @Override
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        CompoundTag tag = player.getPersistentData();
        tag.remove(NBT_WEAK_LAST_AMP);
        tag.remove(NBT_WEAK_LAST_DUR);
        tag.remove(NBT_POISON_LAST_AMP);
        tag.remove(NBT_POISON_LAST_DUR);
    }

    @Override
    public void onTick(Player player) {
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (player.level().isClientSide) {
            return;
        }
        if (!data.hasSpecificItem((Item)ModItems.SKINUPGRADES_IMMUNO.get(), CyberwareSlot.SKIN)) {
            ImmunosuppressorItem.amplifyOncePerApplication(player, (Holder<MobEffect>)MobEffects.WEAKNESS, NBT_WEAK_LAST_AMP, NBT_WEAK_LAST_DUR);
            ImmunosuppressorItem.amplifyOncePerApplication(player, (Holder<MobEffect>)MobEffects.POISON, NBT_POISON_LAST_AMP, NBT_POISON_LAST_DUR);
        }
    }

    private static void amplifyOncePerApplication(Player player, Holder<MobEffect> effect, String nbtLastAmpKey, String nbtLastDurKey) {
        boolean refreshedOrNew;
        MobEffectInstance inst = player.getEffect(effect);
        CompoundTag tag = player.getPersistentData();
        if (inst == null) {
            tag.putInt(nbtLastAmpKey, Integer.MIN_VALUE);
            tag.putInt(nbtLastDurKey, Integer.MIN_VALUE);
            return;
        }
        int lastAmp = tag.getInt(nbtLastAmpKey);
        int lastDur = tag.getInt(nbtLastDurKey);
        int curAmp = inst.getAmplifier();
        int curDur = inst.getDuration();
        boolean bl = refreshedOrNew = curDur > lastDur || curAmp != lastAmp;
        if (!refreshedOrNew) {
            return;
        }
        int newAmp = curAmp + 1;
        MobEffectInstance boosted = new MobEffectInstance(effect, curDur, newAmp, inst.isAmbient(), inst.isVisible(), inst.showIcon());
        player.addEffect(boosted);
        tag.putInt(nbtLastAmpKey, newAmp);
        tag.putInt(nbtLastDurKey, curDur);
    }
}

