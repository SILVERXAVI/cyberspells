/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.PartEntity
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.entity.NoKnockbackProjectile;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.ConePart;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;

public abstract class AbstractConeProjectile
extends Projectile
implements NoKnockbackProjectile {
    protected static final int FAILSAFE_EXPIRE_TIME = 400;
    protected int age;
    protected float damage;
    protected boolean dealDamageActive = true;
    protected final ConePart[] subEntities;

    public AbstractConeProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level, LivingEntity entity) {
        this(entityType, level);
        this.setOwner((Entity)entity);
    }

    public AbstractConeProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.blocksBuilding = false;
        this.subEntities = new ConePart[]{new ConePart(this, "part1", 1.0f, 1.0f), new ConePart(this, "part2", 2.5f, 1.5f), new ConePart(this, "part3", 3.5f, 2.0f), new ConePart(this, "part4", 4.5f, 3.0f)};
    }

    public boolean isOnFire() {
        return false;
    }

    public abstract void spawnParticles();

    public boolean shouldBeSaved() {
        return false;
    }

    protected abstract void onHitEntity(EntityHitResult var1);

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.subEntities.length; ++i) {
            this.subEntities[i].setId(id + i + 1);
        }
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    protected static Vec3 rayTrace(Entity owner) {
        float f = owner.getXRot();
        float f1 = owner.getYRot();
        float f2 = Mth.cos((float)(-f1 * ((float)Math.PI / 180) - (float)Math.PI));
        float f3 = Mth.sin((float)(-f1 * ((float)Math.PI / 180) - (float)Math.PI));
        float f4 = -Mth.cos((float)(-f * ((float)Math.PI / 180)));
        float f5 = Mth.sin((float)(-f * ((float)Math.PI / 180)));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        return new Vec3((double)f6, (double)f5, (double)f7);
    }

    public void tick() {
        Entity owner;
        super.tick();
        if (++this.age > 400) {
            this.discard();
        }
        if ((owner = this.getOwner()) != null) {
            Vec3 rayTraceVector = AbstractConeProjectile.rayTrace(owner);
            Vec3 ownerEyePos = owner.getEyePosition(1.0f).subtract(0.0, 0.8, 0.0);
            this.setPos(ownerEyePos);
            this.setXRot(owner.getXRot());
            this.setYRot(owner.getYRot());
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
            double scale = 1.0;
            for (int i = 0; i < this.subEntities.length; ++i) {
                ConePart subEntity = this.subEntities[i];
                double distance = 1.0 + (double)i * scale * (double)subEntity.getDimensions(null).width() / 2.0;
                Vec3 newVector = ownerEyePos.add(rayTraceVector.multiply(distance, distance, distance));
                subEntity.setPos(newVector);
                subEntity.setDeltaMovement(newVector);
                Vec3 vec3 = new Vec3(subEntity.getX(), subEntity.getY(), subEntity.getZ());
                subEntity.xo = vec3.x;
                subEntity.yo = vec3.y;
                subEntity.zo = vec3.z;
                subEntity.xOld = vec3.x;
                subEntity.yOld = vec3.y;
                subEntity.zOld = vec3.z;
            }
        }
        if (!this.level.isClientSide) {
            if (this.dealDamageActive) {
                for (Entity entity : this.getSubEntityCollisions()) {
                    this.onHitEntity(new EntityHitResult(entity));
                }
                this.dealDamageActive = false;
            }
        } else {
            this.spawnParticles();
        }
    }

    public void setDealDamageActive() {
        this.dealDamageActive = true;
    }

    protected Set<Entity> getSubEntityCollisions() {
        ArrayList collisions = new ArrayList();
        for (ConePart conepart : this.subEntities) {
            collisions.addAll(this.level().getEntities((Entity)conepart, conepart.getBoundingBox()));
        }
        return collisions.stream().filter(target -> target != this.getOwner() && target instanceof LivingEntity && Utils.hasLineOfSight(this.level, (Entity)this, target, true)).collect(Collectors.toSet());
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("Damage", this.damage);
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.damage = pCompound.getFloat("Damage");
    }
}

