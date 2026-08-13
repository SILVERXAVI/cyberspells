/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.particle.ShockwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LightningStrike
extends AoeEntity {
    static final int chargeTime = 20;
    static final int vfxHeight = 15;

    public LightningStrike(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setRadius(3.0f);
        this.setCircular();
    }

    public LightningStrike(Level level) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.LIGHTNING_STRIKE.get()), level);
    }

    @Override
    public void tick() {
        if (this.level.isClientSide) {
            return;
        }
        if (this.tickCount == 1) {
            int total = 5;
            int light = Utils.random.nextInt(total);
            Vec3 location = this.position().add(0.0, 15.0, 0.0);
            MagicManager.spawnParticles(this.level, ParticleHelper.FOG_THUNDER_LIGHT, location.x, location.y, location.z, light, 1.0, 1.0, 1.0, 1.0, true);
            MagicManager.spawnParticles(this.level, ParticleHelper.FOG_THUNDER_DARK, location.x, location.y, location.z, total - light, 1.0, 1.0, 1.0, 1.0, true);
            MagicManager.spawnParticles(this.level, new ShockwaveParticleOptions(SchoolRegistry.LIGHTNING.get().getTargetingColor(), -1.5f, true), this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
        }
        if (this.tickCount == 20) {
            this.checkHits();
            MagicManager.spawnParticles(this.level, ParticleHelper.ELECTRIC_SPARKS, this.getX(), this.getY(), this.getZ(), 25, 0.2f, 0.2f, 0.2f, 0.25, true);
            MagicManager.spawnParticles(this.level, ParticleHelper.FIERY_SPARKS, this.getX(), this.getY(), this.getZ(), 5, 0.2f, 0.2f, 0.2f, 0.125, true);
            Vec3 bottom = this.position();
            Vec3 top = bottom.add(0.0, 15.0, 0.0);
            Vec3 middle = bottom.add(Utils.getRandomScaled(2.0), (double)Utils.random.nextIntBetweenInclusive(3, 12), Utils.getRandomScaled(2.0));
            MagicManager.spawnParticles(this.level, new ZapParticleOption(top), middle.x, middle.y, middle.z, 1, 0.0, 0.0, 0.0, 0.0, true);
            MagicManager.spawnParticles(this.level, new ZapParticleOption(middle), this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
            if (Utils.random.nextFloat() < 0.3f) {
                Vec3 split = middle.add(Utils.getRandomScaled(2.0), -Math.abs(Utils.getRandomScaled(2.0)), Utils.getRandomScaled(2.0));
                MagicManager.spawnParticles(this.level, new ZapParticleOption(middle), split.x, split.y, split.z, 1, 0.0, 0.0, 0.0, 0.0, true);
            }
            this.playSound((SoundEvent)SoundRegistry.SMALL_LIGHTNING_STRIKE.get(), 2.0f, 0.8f + this.random.nextFloat() * 0.5f);
        }
        if (this.tickCount > 20) {
            this.discard();
        }
    }

    @Override
    public void applyEffect(LivingEntity target) {
        DamageSources.applyDamage((Entity)target, this.getDamage(), SpellRegistry.THUNDERSTORM_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
    }

    @Override
    public float getParticleCount() {
        return 0.0f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }
}

