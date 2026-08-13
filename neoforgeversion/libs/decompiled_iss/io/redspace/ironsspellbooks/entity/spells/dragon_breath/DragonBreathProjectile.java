/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.dragon_breath;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.entity.spells.dragon_breath.DragonBreathPool;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class DragonBreathProjectile
extends AbstractConeProjectile {
    public DragonBreathProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public DragonBreathProjectile(Level level, LivingEntity entity) {
        super((EntityType<? extends AbstractConeProjectile>)((EntityType)EntityRegistry.DRAGON_BREATH_PROJECTILE.get()), level, entity);
    }

    @Override
    public void spawnParticles() {
        Entity owner = this.getOwner();
        if (!this.level().isClientSide || owner == null) {
            return;
        }
        Vec3 rotation = owner.getLookAngle().normalize();
        Vec3 pos = owner.position().add(rotation.scale(1.6));
        double x = pos.x;
        double y = pos.y + (double)(owner.getEyeHeight() * 0.9f);
        double z = pos.z;
        double speed = this.random.nextDouble() * 0.35 + 0.25;
        for (int i = 0; i < 12; ++i) {
            double offset = 0.15;
            double ox = Math.random() * 2.0 * offset - offset;
            double oy = Math.random() * 2.0 * offset - offset;
            double oz = Math.random() * 2.0 * offset - offset;
            double angularness = 0.3;
            Vec3 randomVec = new Vec3(Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness).normalize();
            Vec3 result = rotation.scale(3.0).add(randomVec).normalize().scale(speed);
            this.level().addParticle((ParticleOptions)ParticleTypes.DRAGON_BREATH, x + ox, y + oy, z + oz, result.x, result.y, result.z);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (DamageSources.applyDamage(entity, this.damage, SpellRegistry.DRAGON_BREATH_SPELL.get().getDamageSource((Entity)this, this.getOwner())) && this.random.nextFloat() < 0.3f) {
            this.createDragonBreathPuddle(entity.position());
        }
    }

    private void createDragonBreathPuddle(Vec3 pos) {
        DragonBreathPool pool = new DragonBreathPool(this.level());
        pool.setOwner(this.getOwner());
        pool.setDamage(this.damage);
        pool.moveTo(pos);
        this.level().addFreshEntity((Entity)pool);
    }
}

