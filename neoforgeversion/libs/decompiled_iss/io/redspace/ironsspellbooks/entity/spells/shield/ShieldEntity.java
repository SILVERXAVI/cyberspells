/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.PartEntity
 */
package io.redspace.ironsspellbooks.entity.spells.shield;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;

public class ShieldEntity
extends AbstractShieldEntity {
    protected ShieldPart[] subEntities = new ShieldPart[this.width * this.height];
    protected final Vec3[] subPositions = new Vec3[this.width * this.height];
    protected final int LIFETIME;
    protected int width = 5;
    protected int height = 5;
    protected int age;

    public ShieldEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setHealth(10.0f);
        this.LIFETIME = 400;
        this.createShield();
    }

    public ShieldEntity(Level level, float health) {
        this((EntityType)EntityRegistry.SHIELD_ENTITY.get(), level);
        this.setHealth(health);
    }

    @Override
    protected void createShield() {
        for (int x = 0; x < this.width; ++x) {
            for (int y = 0; y < this.height; ++y) {
                int i = x * this.height + y;
                this.subEntities[i] = new ShieldPart(this, "part" + (i + 1), 0.5f, 0.5f, true);
                this.subPositions[i] = new Vec3((double)(((float)x - (float)this.width / 2.0f) * 0.5f + 0.25f), (double)(((float)y - (float)this.height / 2.0f) * 0.5f), 0.0);
            }
        }
    }

    public void setRotation(float x, float y) {
        this.setXRot(x);
        this.xRotO = x;
        this.setYRot(y);
        this.yRotO = y;
    }

    @Override
    public void takeDamage(DamageSource source, float amount, @Nullable Vec3 location) {
        if (!this.isInvulnerableTo(source)) {
            this.setHealth(this.getHealth() - amount);
            if (!this.level().isClientSide && location != null) {
                MagicManager.spawnParticles(this.level(), (ParticleOptions)ParticleTypes.ELECTRIC_SPARK, location.x, location.y, location.z, 30, 0.1, 0.1, 0.1, 0.5, false);
                this.level().playSound(null, location.x, location.y, location.z, (SoundEvent)SoundRegistry.FORCE_IMPACT.get(), SoundSource.NEUTRAL, 0.8f, 1.0f);
            }
        }
    }

    @Override
    public void tick() {
        this.hurtThisTick = false;
        if (this.getHealth() <= 0.0f) {
            this.destroy();
        } else if (++this.age > this.LIFETIME) {
            if (!this.level().isClientSide) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.NEUTRAL, 1.0f, 1.4f);
            }
            this.discard();
        } else {
            for (int i = 0; i < this.subEntities.length; ++i) {
                ShieldPart subEntity = this.subEntities[i];
                Vec3 pos = this.subPositions[i].xRot((float)Math.PI / 180 * -this.getXRot()).yRot((float)Math.PI / 180 * -this.getYRot()).add(this.position());
                subEntity.setPos(pos);
                subEntity.xo = pos.x;
                subEntity.yo = pos.y;
                subEntity.zo = pos.z;
                subEntity.xOld = pos.x;
                subEntity.yOld = pos.y;
                subEntity.zOld = pos.z;
            }
        }
    }

    @Override
    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    @Override
    protected void destroy() {
        if (!this.level().isClientSide) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 2.0f, 0.65f);
        }
        super.destroy();
    }
}

