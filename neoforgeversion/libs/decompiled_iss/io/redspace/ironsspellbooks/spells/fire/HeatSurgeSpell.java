/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.spells.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.network.particles.ShockwaveParticlesPacket;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class HeatSurgeSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"heat_surge");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.FIRE_RESOURCE).setMaxLevel(6).setCooldownSeconds(45.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.rend", (Object[])new Object[]{Utils.stringTruncation((this.getRendAmplifier(spellLevel, caster) + 1) * 5, 1)}), Component.translatable((String)"ui.irons_spellbooks.effect_length", (Object[])new Object[]{Utils.timeFromTicks(this.getDuration(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.radius", (Object[])new Object[]{Utils.stringTruncation(this.getRadius(spellLevel, caster), 2)}));
    }

    public HeatSurgeSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 2;
        this.castTime = 20;
        this.baseManaCost = 50;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of((SoundEvent)SoundRegistry.HEAT_SURGE_PREPARE.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = this.getRadius(spellLevel, entity);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(SchoolRegistry.FIRE.get().getTargetingColor(), radius), entity.getX(), entity.getY() + (double)0.165f, entity.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
        PacketDistributor.sendToPlayersTrackingEntityAndSelf((Entity)entity, (CustomPacketPayload)new ShockwaveParticlesPacket(new Vec3(entity.getX(), entity.getY() + (double)0.165f, entity.getZ()), radius, (ParticleType)ParticleRegistry.FIRE_PARTICLE.get()), (CustomPacketPayload[])new CustomPacketPayload[0]);
        level.getEntities((Entity)entity, entity.getBoundingBox().inflate((double)radius, 4.0, (double)radius), target -> !DamageSources.isFriendlyFireBetween(target, (Entity)entity) && Utils.hasLineOfSight(level, (Entity)entity, target, true)).forEach(target -> {
            LivingEntity livingEntity;
            if (target instanceof LivingEntity && (livingEntity = (LivingEntity)target).distanceToSqr((Entity)entity) < (double)(radius * radius)) {
                int i = this.getDuration(spellLevel, entity);
                livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.REND, i, this.getRendAmplifier(spellLevel, entity)));
                livingEntity.setRemainingFireTicks(Math.min(i / 2, 160));
                MagicManager.spawnParticles(level, ParticleHelper.EMBERS, livingEntity.getX(), livingEntity.getY() + (double)(livingEntity.getBbHeight() * 0.5f), livingEntity.getZ(), 50, livingEntity.getBbWidth() * 0.5f, livingEntity.getBbHeight() * 0.5f, livingEntity.getBbWidth() * 0.5f, 0.03, false);
            }
        });
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return 6.0f + (float)spellLevel * 0.5f;
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return (int)(this.getSpellPower(spellLevel, (Entity)caster) * 20.0f);
    }

    public int getRendAmplifier(int spellLevel, LivingEntity caster) {
        return 1 + spellLevel;
    }

    @Override
    public void onServerCastComplete(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
        super.onServerCastComplete(level, spellLevel, entity, playerMagicData, cancelled);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.TOUCH_GROUND_ANIMATION;
    }

    @Override
    public boolean stopSoundOnCancel() {
        return true;
    }
}

