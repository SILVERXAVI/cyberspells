/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 */
package io.redspace.ironsspellbooks.entity.spells.blood_needle;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BloodNeedle
extends AbstractMagicProjectile {
    private static final EntityDataAccessor<Float> DATA_Z_ROT = SynchedEntityData.defineId(BloodNeedle.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(BloodNeedle.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static int soundTimestamp;

    public BloodNeedle(EntityType<? extends BloodNeedle> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public BloodNeedle(Level levelIn, LivingEntity shooter) {
        super((EntityType<? extends Projectile>)((EntityType)EntityRegistry.BLOOD_NEEDLE.get()), levelIn);
        this.setOwner((Entity)shooter);
    }

    public void setZRot(float zRot) {
        if (!this.level.isClientSide) {
            this.entityData.set(DATA_Z_ROT, (Object)Float.valueOf(zRot));
        }
    }

    public void setScale(float scale) {
        if (!this.level.isClientSide) {
            this.entityData.set(DATA_SCALE, (Object)Float.valueOf(scale));
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_Z_ROT, (Object)Float.valueOf(0.0f));
        pBuilder.define(DATA_SCALE, (Object)Float.valueOf(1.0f));
        super.defineSynchedData(pBuilder);
    }

    public float getZRot() {
        return ((Float)this.entityData.get(DATA_Z_ROT)).floatValue();
    }

    public float getScale() {
        return ((Float)this.entityData.get(DATA_SCALE)).floatValue();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("zRot", this.getZRot());
        if (this.getScale() != 1.0f) {
            tag.putFloat("Scale", this.getScale());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setZRot(tag.getFloat("zRot"));
        if (tag.contains("Scale")) {
            this.setScale(tag.getFloat("Scale"));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        DamageSources.applyDamage(entityHitResult.getEntity(), this.getDamage(), SpellRegistry.BLOOD_NEEDLES_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        this.discardHelper(hitresult);
    }

    @Override
    protected void doImpactSound(Holder<SoundEvent> sound) {
        if (soundTimestamp != this.tickCount) {
            super.doImpactSound(sound);
            soundTimestamp = this.tickCount;
        }
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 2; ++i) {
            double speed = 0.05;
            double dx = Utils.random.nextDouble() * 2.0 * speed - speed;
            double dy = Utils.random.nextDouble() * 2.0 * speed - speed;
            double dz = Utils.random.nextDouble() * 2.0 * speed - speed;
            this.level.addParticle(ParticleHelper.BLOOD, this.getX() + dx, this.getY() + dy, this.getZ() + dz, dx, dy, dz);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.BLOOD, x, y, z, 15, 0.1, 0.1, 0.1, 0.18, true);
    }

    @Override
    public float getSpeed() {
        return 2.5f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundRegistry.BLOOD_NEEDLE_IMPACT);
    }
}

