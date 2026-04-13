/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
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
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.damage.ModDamageSources;
import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.sound.ModSounds;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class SandevistanItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private final SoundEvent activateSound;
    private static final int ACTIVE_TICKS_TOTAL = 30;
    private static final int COOLDOWN_TICKS_TOTAL = 3000;
    private static final int FORCED_COOLDOWN_TICKS_TOTAL = 60;
    private static final String TAG_ACTIVE_TICKS = "cc_sandevistan_active";
    private static final String TAG_COOLDOWN_TICKS = "cc_sandevistan_cd";
    private static final String TAG_FORCED_TICKS = "cc_sandevistan_forced_cd";
    private static final float OVERCLOCK_MIN_CHANCE = 0.05f;
    private static final float OVERCLOCK_MAX_CHANCE = 0.6f;
    private static final float OVERCLOCK_DAMAGE = 7.0f;

    public SandevistanItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
        this.activateSound = ModSounds.SANDY_STARTUP.get();
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

    @Override
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
        this.forceStopAndStartCooldown(player);
        this.removeAll(player);
    }

    @Override
    public void onTick(Player player) {
        if (player.level().isClientSide()) {
            return;
        }
        CompoundTag pd = player.getPersistentData();
        int activeTicks = SandevistanItem.getInt(pd, TAG_ACTIVE_TICKS);
        int cooldownTicks = SandevistanItem.getInt(pd, TAG_COOLDOWN_TICKS);
        int forcedTicks = SandevistanItem.getInt(pd, TAG_FORCED_TICKS);
        if (cooldownTicks > 0) {
            SandevistanItem.setInt(pd, TAG_COOLDOWN_TICKS, --cooldownTicks);
        }
        if (forcedTicks > 0) {
            SandevistanItem.setInt(pd, TAG_FORCED_TICKS, --forcedTicks);
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            if (activeTicks > 0) {
                this.forceStopAndStartCooldown(player);
            }
            this.removeAll(player);
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            if (activeTicks > 0) {
                this.forceStopAndStartCooldown(player);
            }
            this.removeAll(player);
            return;
        }
        InstalledRef ref = this.findEnabledRefForThisItem(data);
        if (ref == null) {
            if (activeTicks > 0) {
                this.forceStopAndStartCooldown(player);
            }
            this.removeAll(player);
            return;
        }
        if (activeTicks > 0) {
            this.applyAll(player, activeTicks);
            SandevistanItem.setInt(pd, TAG_ACTIVE_TICKS, --activeTicks);
            if (activeTicks <= 0) {
                SandevistanItem.setInt(pd, TAG_ACTIVE_TICKS, 0);
                this.removeAll(player);
                SandevistanItem.setInt(pd, TAG_FORCED_TICKS, 60);
                SandevistanItem.setInt(pd, TAG_COOLDOWN_TICKS, 3000);
            }
            return;
        }
        this.removeAll(player);
        if (!player.isSprinting()) {
            return;
        }
        if (forcedTicks > 0) {
            return;
        }
        if (cooldownTicks > 0) {
            this.tryOverclockBacklash(player, cooldownTicks);
            SandevistanItem.setInt(pd, TAG_COOLDOWN_TICKS, 3000);
        }
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), this.activateSound, SoundSource.PLAYERS, 5.0f, 1.0f);
        SandevistanItem.setInt(pd, TAG_ACTIVE_TICKS, 30);
        this.applyAll(player, 30);
    }

    private void tryOverclockBacklash(Player player, int cooldownTicksRemaining) {
        float progress = Mth.clamp((float)((float)cooldownTicksRemaining / 3000.0f), (float)0.0f, (float)1.0f);
        progress *= progress;
        float chance = 0.05f + 0.55f * progress;
        if (player.getRandom().nextFloat() < chance) {
            player.hurt(ModDamageSources.davidsDemise(player.level(), (Entity)player, null), 7.0f);
        }
    }

    private void applyAll(Player player, int remainingTicks) {
        player.addEffect(new MobEffectInstance(ModEffects.SANDEVISTAN_EFFECT, Math.max(1, remainingTicks), 0, false, false, false));
    }

    private void removeAll(Player player) {
        player.removeEffect(ModEffects.SANDEVISTAN_EFFECT);
    }

    private void forceStopAndStartCooldown(Player player) {
        if (player.level().isClientSide()) {
            return;
        }
        CompoundTag pd = player.getPersistentData();
        int activeTicks = SandevistanItem.getInt(pd, TAG_ACTIVE_TICKS);
        if (activeTicks > 0) {
            SandevistanItem.setInt(pd, TAG_ACTIVE_TICKS, 0);
            this.removeAll(player);
            SandevistanItem.setInt(pd, TAG_FORCED_TICKS, 60);
            int curCd = SandevistanItem.getInt(pd, TAG_COOLDOWN_TICKS);
            SandevistanItem.setInt(pd, TAG_COOLDOWN_TICKS, Math.max(curCd, 3000));
        }
    }

    private static int getInt(CompoundTag tag, String key) {
        return tag.contains(key, 3) ? tag.getInt(key) : 0;
    }

    private static void setInt(CompoundTag tag, String key, int value) {
        tag.putInt(key, Math.max(0, value));
    }

    private InstalledRef findEnabledRefForThisItem(PlayerCyberwareData data) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware inst = arr[i];
                if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || st.getItem() != this || !data.isEnabled(slot, i)) continue;
                return new InstalledRef(slot, i);
            }
        }
        return null;
    }

    private record InstalledRef(CyberwareSlot slot, int index) {
    }
}

