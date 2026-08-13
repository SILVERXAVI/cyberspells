/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.spells.ice;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.network.particles.FrostStepParticlesPacket;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class FrostStepSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"frost_step");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.ICE_RESOURCE).setMaxLevel(8).setCooldownSeconds(12.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{Utils.stringTruncation(this.getDistance(spellLevel, caster), 1)}), Component.translatable((String)"ui.irons_spellbooks.shatter_damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 1)}));
    }

    public FrostStepSpell() {
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 15;
        this.manaCostPerLevel = 5;
        this.castTime = 0;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
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
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.FROST_STEP.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TeleportSpell.TeleportData teleportData = (TeleportSpell.TeleportData)playerMagicData.getAdditionalCastData();
        FrozenHumanoid shadow = new FrozenHumanoid(level, entity);
        shadow.setShatterDamage(this.getDamage(spellLevel, entity));
        shadow.setDeathTimer(100);
        level.addFreshEntity((Entity)shadow);
        LivingEntity tauntTarget = entity.getLastHurtByMob();
        Predicate<Entity> predicate = tauntTarget == null ? mob -> entity instanceof Enemy ^ mob instanceof Enemy : mob -> mob.getClass().isAssignableFrom(tauntTarget.getClass()) || mob.isAlliedTo((Entity)tauntTarget) || entity instanceof Enemy ^ mob instanceof Enemy;
        Utils.performTaunt(shadow, 10.0f, predicate);
        Vec3 dest = null;
        if (teleportData != null) {
            Vec3 potentialTarget;
            dest = potentialTarget = teleportData.getTeleportTargetPosition();
        }
        if (dest == null) {
            dest = this.findTeleportLocation(spellLevel, level, entity);
        }
        PacketDistributor.sendToPlayersTrackingEntityAndSelf((Entity)entity, (CustomPacketPayload)new FrostStepParticlesPacket(entity.position(), dest), (CustomPacketPayload[])new CustomPacketPayload[0]);
        if (entity.isPassenger()) {
            entity.stopRiding();
        }
        Utils.handleSpellTeleport(this, (Entity)entity, dest);
        entity.resetFallDistance();
        level.playSound(null, dest.x, dest.y, dest.z, this.getCastFinishSound().get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
        playerMagicData.resetAdditionalCastData();
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private Vec3 findTeleportLocation(int spellLevel, Level level, LivingEntity entity) {
        return TeleportSpell.findTeleportLocation(level, entity, this.getDistance(spellLevel, entity));
    }

    public static void particleCloud(Level level, Vec3 pos) {
        if (level.isClientSide) {
            double width = 0.5;
            float height = 1.0f;
            for (int i = 0; i < 25; ++i) {
                double x = pos.x + Utils.random.nextDouble() * width * 2.0 - width;
                double y = pos.y + (double)height + Utils.random.nextDouble() * (double)height * 1.2 * 2.0 - (double)height * 1.2;
                double z = pos.z + Utils.random.nextDouble() * width * 2.0 - width;
                double dx = Utils.random.nextDouble() * 0.1 * (double)(Utils.random.nextBoolean() ? 1 : -1);
                double dy = Utils.random.nextDouble() * 0.1 * (double)(Utils.random.nextBoolean() ? 1 : -1);
                double dz = Utils.random.nextDouble() * 0.1 * (double)(Utils.random.nextBoolean() ? 1 : -1);
                level.addParticle(ParticleHelper.SNOWFLAKE, true, x, y, z, dx, dy, dz);
                level.addParticle((ParticleOptions)ParticleTypes.SNOWFLAKE, true, x, y, z, -dx, -dy, -dz);
            }
        }
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return 9.0f + (float)(Utils.softCapFormula(this.getEntityPowerMultiplier(sourceEntity)) * (double)spellLevel * 1.5);
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return AnimationHolder.none();
    }
}

