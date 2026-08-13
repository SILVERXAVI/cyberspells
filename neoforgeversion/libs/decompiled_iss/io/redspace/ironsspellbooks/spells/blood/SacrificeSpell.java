/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.network.casting.SyncTargetingDataPacket;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class SacrificeSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sacrifice");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.BLOOD_RESOURCE).setMaxLevel(5).setCooldownSeconds(1.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.base_damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.radius", (Object[])new Object[]{3}));
    }

    public SacrificeSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 25;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        EntityHitResult entityHit;
        Entity entity2;
        Vec3 end;
        float aimAssist = 0.25f;
        float range = 25.0f;
        Vec3 start = entity.getEyePosition();
        HitResult target = Utils.raycastForEntity(entity.level, (Entity)entity, start, end = entity.getLookAngle().normalize().scale((double)range).add(start), true, aimAssist, e -> {
            IMagicSummon summon;
            return e instanceof IMagicSummon && (summon = (IMagicSummon)e).getSummoner() == entity;
        });
        if (target instanceof EntityHitResult && (entity2 = (entityHit = (EntityHitResult)target).getEntity()) instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity)entity2;
            playerMagicData.setAdditionalCastData(new TargetEntityCastData(livingTarget));
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncTargetingDataPacket(livingTarget, this), (CustomPacketPayload[])new CustomPacketPayload[0]);
                serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.spell_target_success", (Object[])new Object[]{livingTarget.getDisplayName().getString(), this.getDisplayName((Player)serverPlayer)}).withStyle(ChatFormatting.GREEN)));
            }
            return true;
        }
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.sacrifice_target_failure").withStyle(ChatFormatting.RED)));
        }
        return false;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        IMagicSummon summon;
        TargetEntityCastData targetData;
        LivingEntity targetEntity;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetEntityCastData && (targetEntity = (targetData = (TargetEntityCastData)iCastData).getTarget((ServerLevel)level)) instanceof IMagicSummon && (summon = (IMagicSummon)targetEntity).getSummoner().getUUID().equals(entity.getUUID())) {
            float damage = this.getDamage(spellLevel, entity) + targetEntity.getHealth() * 0.5f;
            float explosionRadius = 3.0f * (1.0f + 0.5f * targetEntity.getHealth() / targetEntity.getMaxHealth());
            MagicManager.spawnParticles(level, ParticleHelper.BLOOD, targetEntity.getX(), targetEntity.getY() + 0.25, targetEntity.getZ(), 100, 0.03, 0.4, 0.03, 0.4, true);
            MagicManager.spawnParticles(level, ParticleHelper.BLOOD, targetEntity.getX(), targetEntity.getY() + 0.25, targetEntity.getZ(), 100, 0.03, 0.4, 0.03, 0.4, false);
            MagicManager.spawnParticles(level, new BlastwaveParticleOptions(SchoolRegistry.BLOOD.get().getTargetingColor(), explosionRadius), targetEntity.getX(), targetEntity.getBoundingBox().getCenter().y, targetEntity.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
            List entities = level.getEntities((Entity)targetEntity, targetEntity.getBoundingBox().inflate((double)explosionRadius));
            for (Entity victim : entities) {
                double distanceSqr = victim.distanceToSqr(targetEntity.position());
                if (!victim.canBeHitByProjectile() || !(distanceSqr < (double)(explosionRadius * explosionRadius)) || !Utils.hasLineOfSight(level, targetEntity.getBoundingBox().getCenter(), victim.getBoundingBox().getCenter(), true)) continue;
                float p = (float)(distanceSqr / (double)(explosionRadius * explosionRadius));
                p = 1.0f - p * p * p;
                DamageSources.applyDamage(victim, damage * p, this.getDamageSource((Entity)targetEntity, (Entity)entity));
            }
            CameraShakeManager.addCameraShake(new CameraShakeData(level, 10, targetEntity.position(), 20.0f));
            targetEntity.remove(Entity.RemovalReason.KILLED);
            level.playSound(null, targetEntity.blockPosition(), (SoundEvent)SoundRegistry.BLOOD_EXPLOSION.get(), SoundSource.PLAYERS, 3.0f, (float)Utils.random.nextIntBetweenInclusive(8, 12) * 0.1f);
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, @Nullable LivingEntity caster) {
        return (10.0f + this.getSpellPower(spellLevel, (Entity)caster)) * (caster == null ? 1.0f : (float)caster.getAttributeValue(AttributeRegistry.SUMMON_DAMAGE));
    }
}

