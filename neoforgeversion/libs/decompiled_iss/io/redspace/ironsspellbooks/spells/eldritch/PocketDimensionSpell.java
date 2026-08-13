/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.portal.DimensionTransition
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlockEntity;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.PocketDimensionManager;
import io.redspace.ironsspellbooks.capabilities.magic.PortalManager;
import io.redspace.ironsspellbooks.capabilities.magic.SerializedTargetData;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalData;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalPos;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PocketDimensionSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"pocket_dimension");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE).setMaxLevel(1).setCooldownSeconds(60.0).build();

    public PocketDimensionSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 40;
        this.baseManaCost = 300;
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
        if (level.dimension().equals(PocketDimensionManager.POCKET_DIMENSION)) {
            serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_dimension").withStyle(ChatFormatting.RED)));
            return false;
        }
        if (entity.getCombatTracker().inCombat) {
            serverPlayer.displayClientMessage((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_combat").withStyle(ChatFormatting.RED), true);
            return false;
        }
        return true;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.POCKET_DIMENSION_TRAVEL.get());
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of((SoundEvent)SoundRegistry.ELDRITCH_PREPARE.get());
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
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
    public ICastDataSerializable getEmptyCastData() {
        return new SerializedTargetData();
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            PortalData portalData = new PortalData();
            portalData.setPortalDuration(1200);
            portalData.firstPortal(serverPlayer.getUUID(), PortalPos.of((ResourceKey<Level>)serverPlayer.level.dimension(), serverPlayer.position(), serverPlayer.getYRot()));
            PocketDimensionManager.INSTANCE.maybeGeneratePocketRoom(serverPlayer);
            BlockPos portalPos = PocketDimensionManager.INSTANCE.findPortalForStructure(serverPlayer.serverLevel(), PocketDimensionManager.INSTANCE.structurePosForPlayer((Player)serverPlayer));
            ServerLevel pocketLevel = serverPlayer.getServer().getLevel(PocketDimensionManager.POCKET_DIMENSION);
            BlockEntity portal = pocketLevel.getBlockEntity(portalPos);
            if (portal instanceof PortalFrameBlockEntity) {
                PortalFrameBlockEntity portalFrameBlockEntity = (PortalFrameBlockEntity)portal;
                Vec3 particlePos = serverPlayer.getBoundingBox().getCenter();
                MagicManager.spawnParticles(level, (ParticleOptions)ParticleTypes.SMOKE, particlePos.x, particlePos.y, particlePos.z, 100, 0.1, 0.2, 0.1, 0.1, false);
                UUID uuid = portalFrameBlockEntity.getUUID();
                portalData.secondPortal(uuid, PortalPos.of(PocketDimensionManager.POCKET_DIMENSION, portalPos.getBottomCenter(), 180.0f));
                PortalManager.INSTANCE.addPortalData(uuid, portalData);
                portalFrameBlockEntity.setChanged();
                PortalManager.INSTANCE.addDirectPortalCooldown((Entity)serverPlayer, uuid);
                Scroll.attemptRemoveScrollAfterCast(serverPlayer);
                serverPlayer.stopRiding();
                serverPlayer.changeDimension(new DimensionTransition(pocketLevel, portalData.globalPos2.pos(), Vec3.ZERO, portalData.globalPos2.rotation(), serverPlayer.getXRot(), DimensionTransition.DO_NOTHING));
            }
        }
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }
}

