/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.comet.Comet;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class StarfallSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"starfall");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(10).setCooldownSeconds(16.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.radius", (Object[])new Object[]{Utils.stringTruncation(this.getRadius(caster), 1)}));
    }

    public StarfallSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 1;
        this.castTime = 160;
        this.baseManaCost = 5;
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
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
        return Optional.of((SoundEvent)SoundRegistry.ENDER_CAST.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!(playerMagicData.getAdditionalCastData() instanceof StarfallCastData)) {
            Vec3 targetArea = Utils.moveToRelativeGroundLevel(world, Utils.raycastForEntity(world, (Entity)entity, 40.0f, true).getLocation(), 12);
            playerMagicData.setAdditionalCastData(new StarfallCastData(targetArea));
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public static void particleTrail(Level level, Vec3 a, Vec3 b, ParticleOptions particleType) {
        double d = a.distanceTo(b) * 4.0;
        int i = 0;
        while ((double)i < d) {
            double p = (double)i / d;
            Vec3 vec = a.add(b.subtract(a).scale(p));
            MagicManager.spawnParticles(level, particleType, vec.x, vec.y, vec.z, 1, 0.0, 0.0, 0.0, 0.0, true);
            ++i;
        }
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        ICastData iCastData;
        if (playerMagicData == null || !((iCastData = playerMagicData.getAdditionalCastData()) instanceof StarfallCastData)) {
            return;
        }
        StarfallCastData castData = (StarfallCastData)iCastData;
        float radius = this.getRadius(entity);
        int tick = playerMagicData.getCastDurationRemaining() - 1;
        if (tick % 20 == 0) {
            castData.updateTrackedEntities(level.getEntities((Entity)entity, AABB.ofSize((Vec3)castData.center, (double)(radius * 3.0f), (double)radius, (double)(radius * 3.0f)), e -> e instanceof LivingEntity && !DamageSources.isFriendlyFireBetween((Entity)entity, e)));
        }
        if (tick % 4 == 0) {
            for (int i = 0; i < 2; ++i) {
                Vec3 center = castData.center;
                Vec3 weightedArea = Vec3.ZERO;
                for (Entity target : castData.trackedEntities) {
                    weightedArea = weightedArea.add(target.position().subtract(center).scale((double)(1.0f / (float)castData.trackedEntities.size())));
                }
                double spawnRadius = Mth.clampedLerp((double)radius, (double)((double)radius * 0.5), (double)(weightedArea.length() / (double)radius));
                Vec3 spawnTarget = Utils.moveToRelativeGroundLevel(level, center.add(weightedArea).add(new Vec3(0.0, 0.0, (double)entity.getRandom().nextFloat() * spawnRadius).yRot((float)entity.getRandom().nextInt(360) * ((float)Math.PI / 180))), 3).add(0.0, 0.5, 0.0);
                Vec3 trajectory = new Vec3((double)0.15f, (double)-0.85f, 0.0).normalize();
                Vec3 spawn = Utils.raycastForBlock(level, spawnTarget, spawnTarget.add(trajectory.scale(-12.0)), ClipContext.Fluid.NONE).getLocation().add(trajectory);
                this.shootComet(level, spellLevel, entity, spawn, trajectory);
                MagicManager.spawnParticles(level, ParticleHelper.COMET_FOG, spawn.x, spawn.y, spawn.z, 1, 1.0, 1.0, 1.0, 1.0, false);
                MagicManager.spawnParticles(level, ParticleHelper.COMET_FOG, spawn.x, spawn.y, spawn.z, 1, 1.0, 1.0, 1.0, 1.0, true);
            }
        }
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster) * 0.5f;
    }

    private float getRadius(LivingEntity caster) {
        return 6.0f;
    }

    public void shootComet(Level world, int spellLevel, LivingEntity entity, Vec3 spawn, Vec3 trajectory) {
        Comet fireball = new Comet(world, entity);
        fireball.setPos(spawn.add(-1.0, 0.0, 0.0));
        fireball.shoot(trajectory, 0.075f);
        fireball.setDamage(this.getDamage(spellLevel, entity));
        fireball.setExplosionRadius(2.0f);
        world.addFreshEntity((Entity)fireball);
        world.playSound(null, spawn.x, spawn.y, spawn.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0f, 0.7f + Utils.random.nextFloat() * 0.3f);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_CONTINUOUS_OVERHEAD;
    }

    public static class StarfallCastData
    implements ICastData {
        Vec3 center;
        final List<Entity> trackedEntities = new ArrayList<Entity>();

        public StarfallCastData(Vec3 center) {
            this.center = center;
        }

        @Override
        public void reset() {
            this.trackedEntities.clear();
        }

        public void updateTrackedEntities(List<Entity> entities) {
            this.trackedEntities.clear();
            this.trackedEntities.addAll(entities);
        }
    }
}

