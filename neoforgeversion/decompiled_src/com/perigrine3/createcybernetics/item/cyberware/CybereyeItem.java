/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class CybereyeItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;

    public CybereyeItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.basecyberware_cybereye.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public boolean isDyeable(ItemStack stack, CyberwareSlot slot) {
        return slot == CyberwareSlot.EYES;
    }

    @Override
    public boolean isDyeable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnergyUsedPerTick(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return 5;
    }

    @Override
    public boolean requiresEnergyToFunction(Player player, ItemStack installedStack, CyberwareSlot slot) {
        return true;
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.EYES);
    }

    @Override
    public boolean replacesOrgan() {
        return true;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of(CyberwareSlot.EYES);
    }

    @Override
    public int maxStacksPerSlotType(ItemStack stack, CyberwareSlot slotType) {
        return 3;
    }

    @Override
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
        player.removeEffect(MobEffects.BLINDNESS);
        player.removeEffect(MobEffects.DARKNESS);
    }

    @Override
    public void onTick(Player player) {
        ICyberwareItem.super.onTick(player);
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class PowerFailHooks {
        private static final int DURATION = 30;
        private static final int BLINDNESS_AMPLIFIER = 1;
        private static final int DARKNESS_AMPLIFIER = 0;

        private PowerFailHooks() {
        }

        @SubscribeEvent(priority=EventPriority.LOWEST)
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();
            if (player.level().isClientSide) {
                return;
            }
            if (!player.hasData(ModAttachments.CYBERWARE)) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            EyesStatus status = PowerFailHooks.getEyesStatus(player, data);
            if (status.hasCybereyes && status.unpowered) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 1, false, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 30, 0, false, false, false));
            }
        }

        private static EyesStatus getEyesStatus(Player player, PlayerCyberwareData data) {
            InstalledCyberware[] arr = data.getAll().get((Object)CyberwareSlot.EYES);
            if (arr == null) {
                return new EyesStatus(false, false);
            }
            boolean hasEyes = false;
            for (int idx = 0; idx < arr.length; ++idx) {
                ItemStack st;
                InstalledCyberware installed = arr[idx];
                if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !(st.getItem() instanceof CybereyeItem) || !data.isEnabled(CyberwareSlot.EYES, idx)) continue;
                hasEyes = true;
                if (installed.isPowered()) continue;
                return new EyesStatus(true, true);
            }
            return new EyesStatus(hasEyes, false);
        }

        private record EyesStatus(boolean hasCybereyes, boolean unpowered) {
        }
    }
}

