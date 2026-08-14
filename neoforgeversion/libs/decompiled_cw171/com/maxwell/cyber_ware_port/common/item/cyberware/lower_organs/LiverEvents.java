/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Applicable
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Applicable$Result
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.lower_organs;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.init.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid="cyber_ware_port")
public class LiverEvents {
    @SubscribeEvent
    public static void onPotionAdded(MobEffectEvent.Applicable event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player) {
            CyberwareUserData data;
            Player player = (Player)livingEntity;
            if (((MobEffect)event.getEffectInstance().getEffect().value()).getCategory() == MobEffectCategory.HARMFUL && (data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).isCyberwareInstalled((Item)ModItems.LIVER_FILTER.get())) {
                int cost = 50;
                if (data.getEnergyStored() >= cost) {
                    data.extractEnergy(cost, false);
                    event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                }
            }
        }
    }
}

