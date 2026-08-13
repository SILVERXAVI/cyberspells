/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.food.FoodProperties
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Finish
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class GluttonyEffect
extends MagicMobEffect {
    public GluttonyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void finishEating(LivingEntityUseItemEvent.Finish event) {
        MobEffectInstance gluttony;
        FoodProperties food;
        LivingEntity entity = event.getEntity();
        if (!entity.level.isClientSide && (food = event.getItem().getFoodProperties(entity)) != null && (gluttony = entity.getEffect(MobEffectRegistry.GLUTTONY)) != null) {
            MagicData pmg = MagicData.getPlayerMagicData(entity);
            pmg.addMana((float)food.nutrition() * GluttonyEffect.ratioForAmplifier(gluttony.getAmplifier()));
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncManaPacket(pmg), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
        }
    }

    public static float ratioForAmplifier(int amplifier) {
        return (float)(4 + amplifier) * 0.5f;
    }
}

