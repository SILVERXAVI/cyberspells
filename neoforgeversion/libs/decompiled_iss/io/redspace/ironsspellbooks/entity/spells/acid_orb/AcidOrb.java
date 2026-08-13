/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.acid_orb;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class AcidOrb
extends AbstractMagicProjectile {
    int rendLevel;
    int rendDuration;

    public AcidOrb(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AcidOrb(Level level, LivingEntity shooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.ACID_ORB.get()), level);
        this.setOwner((Entity)shooter);
    }

    public int getRendLevel() {
        return this.rendLevel;
    }

    public void setRendLevel(int rendLevel) {
        this.rendLevel = rendLevel;
    }

    public int getRendDuration() {
        return this.rendDuration;
    }

    public void setRendDuration(int rendDuration) {
        this.rendDuration = rendDuration;
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.position().subtract(this.getDeltaMovement().scale(2.0));
        this.level.addParticle(ParticleHelper.ACID, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.ACID, x, y, z, 55, 0.08, 0.08, 0.08, 0.3, true);
        MagicManager.spawnParticles(this.level, ParticleHelper.ACID_BUBBLE, x, y, z, 25, 0.08, 0.08, 0.08, 0.3, false);
    }

    @Override
    public float getSpeed() {
        return 1.0f;
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        if (!this.level.isClientSide) {
            float explosionRadius = 3.5f;
            List entities = this.level.getEntities((Entity)this, this.getBoundingBox().inflate((double)explosionRadius));
            for (Entity entity : entities) {
                LivingEntity livingEntity;
                double distance = entity.position().distanceTo(hitresult.getLocation());
                if (!(distance < (double)explosionRadius) || !Utils.hasLineOfSight(this.level, hitresult.getLocation(), entity.getEyePosition(), true) || !(entity instanceof LivingEntity) || (livingEntity = (LivingEntity)entity) == this.getOwner()) continue;
                livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.REND, this.getRendDuration(), this.getRendLevel()));
            }
            this.discardHelper(hitresult);
        }
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundRegistry.ACID_ORB_IMPACT);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("RendLevel", this.rendLevel);
        tag.putInt("RendDuration", this.rendDuration);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.rendLevel = tag.getInt("RendLevel");
        this.rendDuration = tag.getInt("RendDuration");
    }
}

