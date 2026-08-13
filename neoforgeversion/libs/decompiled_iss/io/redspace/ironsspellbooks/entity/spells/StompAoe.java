/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Position
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StompAoe
extends AbstractMagicProjectile {
    int step;
    int maxSteps;

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

    public StompAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.setNoGravity(true);
        this.maxSteps = 5;
    }

    public StompAoe(Level level, int steps, float yRot) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.STOMP_AOE.get()), level);
        this.maxSteps = steps;
        this.setYRot(yRot);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            if (this.tickCount % 1 == 0) {
                this.checkHits();
            }
            if (this.step > this.maxSteps) {
                this.discard();
            }
        }
    }

    protected void checkHits() {
        if (!this.level.isClientSide) {
            ++this.step;
            int width = Math.max(this.step / 2, 2);
            float angle = this.getYRot() * ((float)Math.PI / 180);
            Vec3 forward = new Vec3((double)Mth.sin((float)(-angle)), 0.0, (double)Mth.cos((float)(-angle)));
            Vec3 orth = new Vec3(-forward.z, 0.0, forward.x);
            Vec3 center = this.position().add(forward.scale((double)this.step));
            Vec3 leftBound = Utils.moveToRelativeGroundLevel(this.level, center.subtract(orth.scale((double)width)), 2).add(0.0, 0.75, 0.0);
            Vec3 rightBound = Utils.moveToRelativeGroundLevel(this.level, center.add(orth.scale((double)width)), 2).add(0.0, 0.75, 0.0);
            this.level.getEntities((Entity)this, new AABB(leftBound.add(0.0, -1.0, 0.0), rightBound.add(0.0, 1.0, 0.0))).forEach(entity -> {
                if (this.canHitEntity((Entity)entity) && Utils.checkEntityIntersecting(entity, leftBound, rightBound, 1.0f).getType() != HitResult.Type.MISS && DamageSources.applyDamage(entity, this.getDamage(), SpellRegistry.STOMP_SPELL.get().getDamageSource((Entity)this, this.getOwner())) && entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity)entity;
                    livingEntity.knockback((double)(this.explosionRadius * -0.35f), forward.x, forward.z);
                }
            });
            for (int i = 0; i < this.step; ++i) {
                Vec3 pos = leftBound.add(rightBound.subtract(leftBound).scale(((double)i + 0.5) / (double)this.step));
                BlockPos blockPos = BlockPos.containing((Position)Utils.moveToRelativeGroundLevel(this.level, pos, 2)).below();
                float impulseStrength = Utils.random.nextFloat() * 0.15f + 0.3f;
                Utils.createTremorBlock(this.level, blockPos, impulseStrength);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("stompStep", this.step);
        pCompound.putInt("maxSteps", this.maxSteps);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.step = pCompound.getInt("stompStep");
        this.maxSteps = pCompound.getInt("maxSteps");
    }
}

