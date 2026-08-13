/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.small_magic_arrow.SmallMagicArrow;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ArrowVolleyEntity
extends AbstractMagicProjectile {
    int rows;
    int arrowsPerRow;
    int delay = 5;

    public ArrowVolleyEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            if (this.tickCount % this.delay == 0) {
                int arrows = this.arrowsPerRow;
                float speed = 0.85f;
                Vec3 motion = Vec3.directionFromRotation((float)(this.getXRot() - (float)this.tickCount / 5.0f * 7.0f), (float)this.getYRot()).normalize().scale((double)speed);
                Vec3 orth = new Vec3((double)(-Mth.cos((float)(-this.getYRot() * ((float)Math.PI / 180) - (float)Math.PI))), 0.0, (double)Mth.sin((float)(-this.getYRot() * ((float)Math.PI / 180) - (float)Math.PI)));
                for (int i = 0; i < arrows; ++i) {
                    float distance = ((float)i - (float)arrows * 0.5f) * 0.7f;
                    SmallMagicArrow arrow = new SmallMagicArrow(this.level, this.getOwner());
                    arrow.setDamage(this.getDamage());
                    Vec3 spawn = this.position().add(orth.scale((double)distance));
                    arrow.setPos(spawn);
                    arrow.shoot(motion.add(Utils.getRandomVec3(0.04f)));
                    arrow.setOwner(this.getOwner());
                    this.level.addFreshEntity((Entity)arrow);
                    MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.FIREWORK, spawn.x, spawn.y, spawn.z, 2, 0.1, 0.1, 0.1, 0.05, false);
                }
                this.level.playSound(null, this.position().x, this.position().y, this.position().z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL, 3.0f, 1.1f + Utils.random.nextFloat() * 0.3f);
                this.level.playSound(null, this.position().x, this.position().y, this.position().z, (SoundEvent)SoundRegistry.BOW_SHOOT.get(), SoundSource.NEUTRAL, 2.0f, (float)Utils.random.nextIntBetweenInclusive(16, 20) * 0.1f);
            } else if (this.tickCount > this.rows * this.delay) {
                this.discard();
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("rows", this.rows);
        tag.putInt("arrowsPerRow", this.arrowsPerRow);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.rows = tag.getInt("rows");
        this.arrowsPerRow = tag.getInt("arrowsPerRow");
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setArrowsPerRow(int arrowsPerRow) {
        this.arrowsPerRow = arrowsPerRow;
    }

    @Override
    public void trailParticles() {
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return 0.0f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }
}

