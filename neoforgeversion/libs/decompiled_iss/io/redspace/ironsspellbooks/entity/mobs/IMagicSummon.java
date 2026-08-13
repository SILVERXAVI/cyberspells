/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.OwnableEntity
 *  net.minecraft.world.level.GameRules
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.registries.DeferredHolder
 */
package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface IMagicSummon
extends AntiMagicSusceptible {
    default public Entity getSummoner() {
        return SummonManager.getOwner((Entity)this);
    }

    public void onUnSummon();

    @Override
    default public void onAntiMagic(MagicData playerMagicData) {
        this.onUnSummon();
    }

    default public boolean shouldIgnoreDamage(DamageSource damageSource) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !((Boolean)ServerConfigs.CAN_ATTACK_OWN_SUMMONS.get()).booleanValue() && damageSource.getEntity() != null) {
            return DamageSources.isFriendlyFireBetween(damageSource.getEntity(), (Entity)this);
        }
        return false;
    }

    default public boolean isAlliedHelper(Entity entity) {
        Entity owner = this.getSummoner();
        if (owner == null) {
            return false;
        }
        if (entity instanceof IMagicSummon) {
            IMagicSummon magicSummon = (IMagicSummon)entity;
            Entity otherOwner = magicSummon.getSummoner();
            return otherOwner != null && (owner == otherOwner || otherOwner.isAlliedTo(otherOwner));
        }
        if (entity instanceof OwnableEntity) {
            OwnableEntity tamableAnimal = (OwnableEntity)entity;
            LivingEntity otherOwner = tamableAnimal.getOwner();
            return otherOwner != null && (owner == otherOwner || otherOwner.isAlliedTo((Entity)otherOwner));
        }
        return false;
    }

    default public void onDeathHelper() {
        IMagicSummon iMagicSummon = this;
        if (iMagicSummon instanceof LivingEntity) {
            Entity entity;
            LivingEntity entity2 = (LivingEntity)iMagicSummon;
            Level level = entity2.level;
            Component deathMessage = entity2.getCombatTracker().getDeathMessage();
            if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && (entity = this.getSummoner()) instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)entity;
                player.sendSystemMessage(deathMessage);
            }
        }
    }

    default public void onRemovedHelper(Entity entity) {
        Entity entity2;
        if (entity.level.isClientSide) {
            return;
        }
        Entity.RemovalReason reason = entity.getRemovalReason();
        if (reason == null || reason == Entity.RemovalReason.UNLOADED_TO_CHUNK) {
            // empty if block
        }
        if (reason == Entity.RemovalReason.DISCARDED && (entity2 = this.getSummoner()) instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer)entity2;
            player.sendSystemMessage((Component)Component.translatable((String)"ui.irons_spellbooks.summon_despawn_message", (Object[])new Object[]{((Entity)this).getDisplayName()}));
        }
        if (reason != null && reason.shouldDestroy()) {
            SummonManager.removeSummon(entity);
            SummonManager.stopTrackingExpiration(entity);
        }
    }

    @Deprecated(forRemoval=true)
    default public void onRemovedHelper(Entity entity, DeferredHolder<MobEffect, SummonTimer> holder) {
        Entity entity2;
        Entity.RemovalReason reason = entity.getRemovalReason();
        if (reason != null && (entity2 = this.getSummoner()) instanceof ServerPlayer) {
            MobEffectInstance effect;
            ServerPlayer player = (ServerPlayer)entity2;
            if (reason.shouldDestroy() && (effect = player.getEffect(holder)) != null) {
                MobEffectInstance decrement = new MobEffectInstance(holder, effect.getDuration(), effect.getAmplifier() - 1, false, false, true);
                if (decrement.getAmplifier() >= 0) {
                    player.getActiveEffectsMap().put(holder, decrement);
                    player.connection.send((Packet)new ClientboundUpdateMobEffectPacket(player.getId(), decrement, false));
                } else {
                    player.removeEffect(holder);
                }
            }
        }
        this.onRemovedHelper(entity);
    }
}

