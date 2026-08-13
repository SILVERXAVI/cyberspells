/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.spells.sunbeam;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SunbeamEntity
extends AoeEntity
implements AntiMagicSusceptible {
    @Nullable
    LivingEntity target;
    public static final int WARMUP_TIME = 15;

    public SunbeamEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setRadius((float)(this.getBoundingBox().getXsize() * 0.5));
        this.setNoGravity(true);
    }

    public SunbeamEntity(Level level) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.SUNBEAM.get()), level);
    }

    @Override
    public void tick() {
        this.setOldPosAndRot();
        if (this.tickCount == 15 && !this.level.isClientSide) {
            this.checkHits();
            MagicManager.spawnParticles(this.level, ParticleHelper.EMBERS, this.getX(), this.getY() + 0.06, this.getZ(), 50, this.getRadius() * 0.7f, 0.2f, this.getRadius() * 0.7f, 0.6f, true);
            MagicManager.spawnParticles(this.level, new BlastwaveParticleOptions(1.0f, 0.85f, 0.4f, 7.0f), this.getX(), this.getY() + 0.06, this.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
            this.level.playSound(null, this.blockPosition(), (SoundEvent)SoundRegistry.SUNBEAM_IMPACT.get(), SoundSource.NEUTRAL, 4.5f, (float)Utils.random.nextIntBetweenInclusive(9, 11) * 0.1f);
        }
        if (this.tickCount > 15) {
            this.discard();
        }
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return true;
    }

    @Override
    public void applyEffect(LivingEntity target) {
        DamageSources.applyDamage((Entity)target, this.getDamage(), SpellRegistry.SUNBEAM_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(2.0, 2.0, 2.0);
    }

    @Override
    public float getParticleCount() {
        return 0.0f;
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    public void ambientParticles() {
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        this.discard();
    }
}

