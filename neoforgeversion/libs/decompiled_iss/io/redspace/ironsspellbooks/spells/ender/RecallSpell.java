/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.portal.DimensionTransition
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.event.entity.player.PlayerRespawnPositionEvent
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.entity.mobs.goals.HomeOwner;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerRespawnPositionEvent;
import org.jetbrains.annotations.Nullable;

public class RecallSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"recall");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(1).setCooldownSeconds(300.0).build();

    public RecallSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 80;
        this.baseManaCost = 100;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        return this.castTime;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (!(entity instanceof ServerPlayer)) {
            return false;
        }
        ServerPlayer serverPlayer = (ServerPlayer)entity;
        if (entity.getCombatTracker().inCombat) {
            serverPlayer.displayClientMessage((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_combat").withStyle(ChatFormatting.RED), true);
            return false;
        }
        return true;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of((SoundEvent)SoundRegistry.RECALL_PREPARE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.ENDERMAN_TELEPORT);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        HomeOwner homeOwner;
        this.playSound(this.getCastFinishSound(), (Entity)entity);
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            DimensionTransition destination = ((PlayerRespawnPositionEvent)NeoForge.EVENT_BUS.post((Event)new PlayerRespawnPositionEvent(serverPlayer, serverPlayer.findRespawnPositionAndUseSpawnBlock(true, DimensionTransition.DO_NOTHING), false))).getDimensionTransition();
            serverPlayer.changeDimension(destination);
        } else if (entity instanceof HomeOwner && (homeOwner = (HomeOwner)entity).getHome() != null) {
            BlockPos pos = homeOwner.getHome();
            entity.teleportTo((double)pos.getX(), (double)pos.getY() + 0.15, (double)pos.getZ());
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public static void ambientParticles(LivingEntity entity, SyncedSpellData spellData) {
        float f = (float)entity.tickCount * 0.125f;
        Vec3 trail1 = new Vec3((double)Mth.cos((float)f), (double)Mth.sin((float)(f * 2.0f)), (double)Mth.sin((float)f)).normalize();
        Vec3 trail2 = new Vec3((double)Mth.sin((float)f), (double)Mth.cos((float)(f * 2.0f)), (double)Mth.cos((float)f)).normalize();
        Vec3 trail3 = trail1.multiply(trail2).normalize().scale((double)(1.0f + (Mth.sin((float)f) + Mth.cos((float)f)) * 0.5f));
        Vec3 pos = entity.getBoundingBox().getCenter();
        entity.level.addParticle(ParticleHelper.UNSTABLE_ENDER, pos.x + trail1.x, pos.y + trail1.y, pos.z + trail1.z, 0.0, 0.0, 0.0);
        entity.level.addParticle(ParticleHelper.UNSTABLE_ENDER, pos.x + trail2.x, pos.y + trail2.y, pos.z + trail2.z, 0.0, 0.0, 0.0);
        entity.level.addParticle(ParticleHelper.UNSTABLE_ENDER, pos.x + trail3.x, pos.y + trail3.y, pos.z + trail3.z, 0.0, 0.0, 0.0);
    }

    @Override
    public void playSound(Optional<SoundEvent> sound, Entity entity) {
        sound.ifPresent(soundEvent -> entity.playSound(soundEvent, 2.0f, 1.0f));
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.none();
    }

    @Override
    public boolean stopSoundOnCancel() {
        return true;
    }
}

