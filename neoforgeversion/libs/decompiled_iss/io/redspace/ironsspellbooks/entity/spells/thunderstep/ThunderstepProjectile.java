/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.thunderstep;

import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ThunderstepProjectile
extends AbstractMagicProjectile {
    protected boolean inGround;

    public ThunderstepProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThunderstepProjectile(Level levelIn, Entity shooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.THUNDERSTEP_PROJECTILE.get()), levelIn);
        this.setOwner(shooter);
    }

    @Override
    public void tick() {
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

    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        Vec3 vec3 = pResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale((double)0.05f);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.inGround = true;
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return false;
    }

    private boolean shouldFall() {
        return this.inGround && this.level.noCollision(new AABB(this.position(), this.position()).inflate(0.06));
    }

    @Override
    public void trailParticles() {
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return 1.0f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}

