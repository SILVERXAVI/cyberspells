/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.EvokerFangs
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;

public class ExtendedEvokerFang
extends EvokerFangs
implements AntiMagicSusceptible {
    private final float damage;
    private boolean sentSpikeEvent;
    private int warmupDelayTicks;
    private boolean attackStarted;

    public ExtendedEvokerFang(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, LivingEntity pOwner, float damage) {
        super(pLevel, pX, pY, pZ, pYRot, pWarmupDelay, pOwner);
        this.warmupDelayTicks = pWarmupDelay;
        if (this.warmupDelayTicks < 0) {
            this.warmupDelayTicks = 0;
        }
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void tick() {
        this.baseTick();
        if (this.warmupDelayTicks == 0) {
            this.attackStarted = true;
            this.level().broadcastEntityEvent((Entity)this, (byte)4);
        }
        if (this.attackStarted && this.warmupDelayTicks == -8) {
            if (this.level().isClientSide) {
                for (int i = 0; i < 12; ++i) {
                    double d0 = this.getX() + (this.random.nextDouble() * 2.0 - 1.0) * (double)this.getBbWidth() * 0.5;
                    double d1 = this.getY() + 0.05 + this.random.nextDouble();
                    double d2 = this.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * (double)this.getBbWidth() * 0.5;
                    double d3 = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                    double d4 = 0.3 + this.random.nextDouble() * 0.3;
                    double d5 = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                    this.level().addParticle((ParticleOptions)ParticleTypes.CRIT, d0, d1 + 1.0, d2, d3, d4, d5);
                }
            } else {
                for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.4, 0.0, 0.4))) {
                    this.dealDamageTo(livingentity);
                }
            }
        }
        if (--this.warmupDelayTicks < -22) {
            this.discard();
        }
    }

    private void dealDamageTo(LivingEntity pTarget) {
        LivingEntity livingentity = this.getOwner();
        if (pTarget.isAlive() && !pTarget.isInvulnerable() && pTarget != livingentity) {
            AbstractSpell spell = SpellRegistry.FANG_STRIKE_SPELL.get();
            DamageSources.applyDamage((Entity)pTarget, this.damage, spell.getDamageSource((Entity)this, (Entity)this.getOwner()));
        }
    }

    public float getAnimationProgress(float pPartialTicks) {
        if (!this.attackStarted) {
            return 0.0f;
        }
        int lifeTicks = this.warmupDelayTicks + 22;
        int i = lifeTicks - 2;
        return i <= 0 ? 1.0f : 1.0f - ((float)i - pPartialTicks) / 20.0f;
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.discard();
    }
}

