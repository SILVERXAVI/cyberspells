/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.devour_jaw;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DevourJaw
extends AoeEntity {
    LivingEntity target;
    public int vigorLevel;
    public final int waitTime = 5;
    public final int warmupTime = 13;
    public final int deathTime = 21;

    public DevourJaw(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public DevourJaw(Level level, LivingEntity owner, LivingEntity target) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.DEVOUR_JAW.get()), level);
        this.setOwner((Entity)owner);
        this.target = target;
    }

    @Override
    public void applyEffect(LivingEntity target) {
        Entity entity;
        if (target == this.target && DamageSources.applyDamage((Entity)target, this.getDamage(), SpellRegistry.DEVOUR_SPELL.get().getDamageSource((Entity)this, this.getOwner())) && (entity = this.getOwner()) instanceof LivingEntity) {
            LivingEntity livingOwner = (LivingEntity)entity;
            target.setDeltaMovement(target.getDeltaMovement().add(0.0, 0.5, 0.0));
            target.hurtMarked = true;
            if (target.isDeadOrDying()) {
                MobEffectInstance oldVigor = livingOwner.getEffect(MobEffectRegistry.VIGOR);
                int addition = 0;
                if (oldVigor != null) {
                    addition = oldVigor.getAmplifier() + 1;
                }
                livingOwner.addEffect(new MobEffectInstance(MobEffectRegistry.VIGOR, 1200, Math.min(this.vigorLevel + addition, 9), false, false, true));
                livingOwner.heal((float)((this.vigorLevel + 1) * 2));
            }
        }
    }

    @Override
    public void tick() {
        if (this.tickCount < 5) {
            if (this.target != null) {
                this.setPos(this.target.position());
            }
        } else if (this.tickCount == 5) {
            this.playSound((SoundEvent)SoundRegistry.DEVOUR_BITE.get(), 2.0f, 1.0f);
        } else if (this.tickCount == 13) {
            if (this.level.isClientSide) {
                float y = this.getYRot();
                int countPerSide = 25;
                for (int i = -countPerSide; i < countPerSide; ++i) {
                    Vec3 motion = new Vec3(0.0, (double)(Math.abs(countPerSide) - i), (double)((float)countPerSide * 0.5f)).yRot(y).normalize().multiply((double)0.4f, (double)0.8f, (double)0.4f);
                    this.level.addParticle(ParticleHelper.BLOOD, this.getX(), this.getY() + 0.5, this.getZ(), motion.x, motion.y, motion.z);
                }
            } else {
                this.checkHits();
            }
        } else if (this.tickCount > 21) {
            this.discard();
        }
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(2.0, 2.0, 2.0);
    }

    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    public void ambientParticles() {
    }

    @Override
    public float getParticleCount() {
        return 0.0f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }
}

