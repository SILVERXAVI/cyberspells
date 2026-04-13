/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.entity.custom.CyberskeletonEntity;
import com.perigrine3.createcybernetics.entity.custom.CyberzombieEntity;
import com.perigrine3.createcybernetics.entity.custom.SmasherEntity;
import com.perigrine3.createcybernetics.event.custom.FullBorgHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class CyborgStruckByLightningHandler {
    private static final ResourceLocation IRONS_LIGHTNING_MAGIC_ID = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"lightning_magic");
    private static final ResourceKey<DamageType> IRONS_LIGHTNING_MAGIC_KEY = ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)IRONS_LIGHTNING_MAGIC_ID);

    private CyborgStruckByLightningHandler() {
    }

    private static boolean isIronsLightningMagic(DamageSource src) {
        if (src == null) {
            return false;
        }
        Holder holder = src.typeHolder();
        return holder.unwrapKey().filter(IRONS_LIGHTNING_MAGIC_KEY::equals).isPresent();
    }

    private static boolean isAnyLightning(DamageSource src) {
        if (src == null) {
            return false;
        }
        if (src.is(DamageTypes.LIGHTNING_BOLT)) {
            return true;
        }
        return CyborgStruckByLightningHandler.isIronsLightningMagic(src);
    }

    @SubscribeEvent
    public static void onLightningStrike(EntityStruckByLightningEvent event) {
        ServerPlayer player;
        if (event.getEntity().level().isClientSide) {
            return;
        }
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity)entity;
        if (living instanceof CyberskeletonEntity || living instanceof CyberzombieEntity || living instanceof SmasherEntity) {
            CyborgStruckByLightningHandler.applyEmp(living);
            return;
        }
        if (living instanceof ServerPlayer && FullBorgHandler.hasAnyImplantsAtAll(player = (ServerPlayer)living)) {
            CyborgStruckByLightningHandler.applyEmp((LivingEntity)player);
            if (!FullBorgHandler.isFullBorg(player)) {
                player.hurt(player.damageSources().lightningBolt(), 7.0f);
            }
        }
    }

    @SubscribeEvent
    public static void onLightningSpell(LivingIncomingDamageEvent event) {
        ServerPlayer player;
        LivingEntity living = event.getEntity();
        if (living.level().isClientSide) {
            return;
        }
        if (!CyborgStruckByLightningHandler.isAnyLightning(event.getSource())) {
            return;
        }
        if (living instanceof CyberskeletonEntity || living instanceof CyberzombieEntity || living instanceof SmasherEntity) {
            CyborgStruckByLightningHandler.applyEmp(living);
            return;
        }
        if (living instanceof ServerPlayer && FullBorgHandler.isFullBorg(player = (ServerPlayer)living)) {
            CyborgStruckByLightningHandler.applyEmp((LivingEntity)player);
        }
    }

    private static void applyEmp(LivingEntity living) {
        living.addEffect(new MobEffectInstance(ModEffects.EMP, 200, 0, true, true, true));
    }
}

