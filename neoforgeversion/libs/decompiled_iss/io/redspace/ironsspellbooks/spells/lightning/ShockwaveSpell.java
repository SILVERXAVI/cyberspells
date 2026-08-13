/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LightningBolt
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.spells.lightning;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ShockwaveSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"shockwave");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(8).setCooldownSeconds(30.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.radius", (Object[])new Object[]{Utils.stringTruncation(this.getRadius(spellLevel, caster), 2)}));
    }

    public ShockwaveSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 1;
        this.castTime = 16;
        this.baseManaCost = 70;
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
        return Optional.of((SoundEvent)SoundRegistry.SHOCKWAVE_PREPARE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.SHOCKWAVE_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = this.getRadius(spellLevel, entity);
        Vector3f edge = new Vector3f(0.7f, 1.0f, 1.0f);
        Vector3f center = new Vector3f(1.0f, 1.0f, 1.0f);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(edge, radius * 1.02f), entity.getX(), entity.getY() + (double)0.15f, entity.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(edge, radius * 0.98f), entity.getX(), entity.getY() + (double)0.15f, entity.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(center, radius), entity.getX(), entity.getY() + (double)0.165f, entity.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(center, radius), entity.getX(), entity.getY() + (double)0.135f, entity.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
        MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, entity.getX(), entity.getY() + 1.0, entity.getZ(), 80, 0.25, 0.25, 0.25, 0.7f + radius * 0.1f, false);
        CameraShakeManager.addCameraShake(new CameraShakeData(level, 30, entity.position(), radius * 2.0f));
        Vec3 start = entity.getBoundingBox().getCenter();
        float damage = this.getDamage(spellLevel, entity);
        LightningBolt dummyLightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        dummyLightningBolt.setDamage(0.0f);
        dummyLightningBolt.setVisualOnly(true);
        level.getEntities((Entity)entity, entity.getBoundingBox().inflate((double)radius, (double)radius, (double)radius), target -> !DamageSources.isFriendlyFireBetween(target, (Entity)entity) && Utils.hasLineOfSight(level, (Entity)entity, target, true)).forEach(target -> {
            if (target instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)target;
                if (this.canHit((Entity)entity, (Entity)target) && livingEntity.distanceToSqr((Entity)entity) < (double)(radius * radius)) {
                    Vec3 dest = livingEntity.getBoundingBox().getCenter();
                    ((ServerLevel)level).sendParticles((ParticleOptions)new ZapParticleOption(dest), start.x, start.y, start.z, 1, 0.0, 0.0, 0.0, 0.0);
                    MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, livingEntity.getX(), livingEntity.getY() + (double)(livingEntity.getBbHeight() / 2.0f), livingEntity.getZ(), 10, livingEntity.getBbWidth() / 3.0f, livingEntity.getBbHeight() / 3.0f, livingEntity.getBbWidth() / 3.0f, 0.1, false);
                    DamageSources.applyDamage(target, damage, this.getDamageSource((Entity)entity));
                    if (target instanceof Creeper) {
                        Creeper creeper = (Creeper)target;
                        creeper.thunderHit((ServerLevel)level, dummyLightningBolt);
                    }
                }
            }
        });
        for (int i = 0; i < 7; ++i) {
            Vec3 dest = start.add(Utils.getRandomVec3(1.0).multiply(4.0, 2.5, 4.0).add(0.0, 4.0, 0.0));
            ((ServerLevel)level).sendParticles((ParticleOptions)new ZapParticleOption(dest), start.x, start.y, start.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private boolean canHit(Entity owner, Entity target) {
        return target != owner && target.isAlive() && target.isPickable() && !target.isSpectator();
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return 8 + spellLevel;
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return 4.0f + this.getSpellPower(spellLevel, (Entity)caster) * 0.75f;
    }

    @Override
    public void playSound(Optional<SoundEvent> sound, Entity entity) {
        sound.ifPresent(soundEvent -> entity.playSound(soundEvent, 3.0f, 0.9f + Utils.random.nextFloat() * 0.2f));
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.PREPARE_CROSS_ARMS;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.CAST_T_POSE;
    }
}

