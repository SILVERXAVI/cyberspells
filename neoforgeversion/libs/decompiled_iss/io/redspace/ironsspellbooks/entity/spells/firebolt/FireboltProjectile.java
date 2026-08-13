/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.firebolt;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FireboltProjectile
extends AbstractMagicProjectile {
    public FireboltProjectile(EntityType<? extends FireboltProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public FireboltProjectile(Level levelIn, LivingEntity shooter) {
        this((EntityType<? extends FireboltProjectile>)((EntityType)EntityRegistry.FIREBOLT_PROJECTILE.get()), levelIn);
        this.setOwner((Entity)shooter);
    }

    @Override
    public float getSpeed() {
        return 1.75f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)SoundEvents.FIREWORK_ROCKET_BLAST));
    }

    @Override
    protected void doImpactSound(Holder<SoundEvent> sound) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.NEUTRAL, 2.0f, 1.2f + Utils.random.nextFloat() * 0.2f);
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity target = entityHitResult.getEntity();
        DamageSources.applyDamage(target, this.getDamage(), SpellRegistry.FIREBOLT_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
        this.pierceOrDiscard();
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.LAVA, x, y, z, 5, 0.1, 0.1, 0.1, 0.25, true);
    }

    @Override
    public void trailParticles() {
        float yHeading = -((float)(Mth.atan2((double)this.getDeltaMovement().z, (double)this.getDeltaMovement().x) * 57.2957763671875) + 90.0f);
        float radius = 0.25f;
        int steps = 2;
        Vec3 vec = this.getDeltaMovement();
        double x2 = this.getX();
        double x1 = x2 - vec.x;
        double y2 = this.getY();
        double y1 = y2 - vec.y;
        double z2 = this.getZ();
        double z1 = z2 - vec.z;
        for (int j = 0; j < steps; ++j) {
            float offset = 1.0f / (float)steps * (float)j;
            double radians = ((float)this.tickCount + offset) / 7.5f * 360.0f * ((float)Math.PI / 180);
            Vec3 swirl = new Vec3(Math.cos(radians) * (double)radius, Math.sin(radians) * (double)radius, 0.0).yRot(yHeading * ((float)Math.PI / 180));
            double x = Mth.lerp((double)offset, (double)x1, (double)x2) + swirl.x;
            double y = Mth.lerp((double)offset, (double)y1, (double)y2) + swirl.y + (double)(this.getBbHeight() / 2.0f);
            double z = Mth.lerp((double)offset, (double)z1, (double)z2) + swirl.z;
            Vec3 jitter = Vec3.ZERO;
            this.level.addParticle(ParticleHelper.EMBERS, x, y, z, jitter.x, jitter.y, jitter.z);
        }
    }
}

