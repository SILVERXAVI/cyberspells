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
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.small_magic_arrow;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class SmallMagicArrow
extends AbstractMagicProjectile {
    private static final EntityDataAccessor<Boolean> IN_GROUND = SynchedEntityData.defineId(SmallMagicArrow.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    public int shakeTime;
    protected boolean inGround;

    public SmallMagicArrow(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SmallMagicArrow(Level levelIn, Entity shooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.SMALL_MAGIC_ARROW.get()), levelIn);
        this.setOwner(shooter);
    }

    @Override
    public void shoot(Vec3 rotation) {
        this.setDeltaMovement(rotation);
    }

    @Override
    public void tick() {
        if (this.shakeTime > 0) {
            --this.shakeTime;
        }
        if (!this.inGround) {
            super.tick();
        } else {
            this.deltaMovementOld = this.getDeltaMovement();
            if (this.tickCount > 300) {
                this.discard();
                return;
            }
            if (this.shouldFall()) {
                this.inGround = false;
                this.setDeltaMovement(this.getDeltaMovement().normalize().scale((double)0.05f));
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(IN_GROUND, (Object)false);
    }

    private boolean shouldFall() {
        return this.inGround && this.level.noCollision(new AABB(this.position(), this.position()).inflate(0.06));
    }

    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        Vec3 vec3 = pResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale((double)0.05f);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.playSound(SoundEvents.ARROW_HIT, 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
        this.inGround = true;
        this.shakeTime = 7;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        boolean ignore;
        if (this.level.isClientSide) {
            return;
        }
        Entity entity = entityHitResult.getEntity();
        boolean hit = DamageSources.applyDamage(entity, this.getDamage(), SpellRegistry.ARROW_VOLLEY_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
        boolean bl = ignore = entity.getType() == EntityType.ENDERMAN;
        if (hit) {
            this.pierceOrDiscard();
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1));
            this.setYRot(this.getYRot() + 180.0f);
            this.yRotO += 180.0f;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("inGround", this.inGround);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inGround = tag.getBoolean("inGround");
    }

    @Override
    public void trailParticles() {
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return 2.0f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }
}

