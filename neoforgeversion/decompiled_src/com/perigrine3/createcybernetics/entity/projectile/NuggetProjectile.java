/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.ThrowableItemProjectile
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package com.perigrine3.createcybernetics.entity.projectile;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NuggetProjectile
extends ThrowableItemProjectile {
    private static final float DAMAGE = 6.0f;

    public NuggetProjectile(EntityType<? extends NuggetProjectile> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    public NuggetProjectile(EntityType<? extends NuggetProjectile> type, Level level, LivingEntity owner, ItemStack ammo) {
        super(type, owner, level);
        this.setAmmo(ammo);
        this.setNoGravity(true);
    }

    public void setAmmo(ItemStack ammo) {
        if (ammo == null || ammo.isEmpty()) {
            this.setItem(ItemStack.EMPTY);
            return;
        }
        ItemStack one = ammo.copy();
        one.setCount(1);
        this.setItem(one);
    }

    public ItemStack getAmmo() {
        ItemStack st = this.getItem();
        return st == null ? ItemStack.EMPTY : st;
    }

    protected Item getDefaultItem() {
        return Items.IRON_NUGGET;
    }

    public void tick() {
        this.setNoGravity(true);
        super.tick();
        Vec3 v = this.getDeltaMovement();
        double dx = v.x;
        double dy = v.y;
        double dz = v.z;
        double h = Math.sqrt(dx * dx + dz * dz);
        if (h > 1.0E-6) {
            float yaw = (float)(Mth.atan2((double)dx, (double)dz) * 57.29577951308232);
            float pitch = (float)(Mth.atan2((double)dy, (double)h) * 57.29577951308232);
            this.setYRot(yaw);
            this.setXRot(pitch);
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level().isClientSide) {
            return;
        }
        Entity target = result.getEntity();
        Entity owner = this.getOwner();
        target.hurt(this.damageSources().thrown((Entity)this, owner), 6.0f);
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            Level level = this.level();
            if (level instanceof ServerLevel) {
                ServerLevel sl = (ServerLevel)level;
                sl.sendParticles((ParticleOptions)ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 4, 0.05, 0.05, 0.05, 0.02);
            }
            this.discard();
        }
    }
}

