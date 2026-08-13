/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BaseFireBlock
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.fireball;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SmallMagicFireball
extends AbstractMagicProjectile {
    public SmallMagicFireball(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public SmallMagicFireball(Level pLevel, LivingEntity pShooter) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.SMALL_FIREBALL_PROJECTILE.get()), pLevel);
        this.setOwner((Entity)pShooter);
    }

    public void shoot(Vec3 rotation, float inaccuracy) {
        double speed = rotation.length();
        Vec3 offset = Utils.getRandomVec3(1.0).normalize().scale((double)inaccuracy);
        Vec3 motion = rotation.normalize().add(offset).normalize().scale(speed);
        super.shoot(motion);
    }

    @Override
    public void trailParticles() {
        if (this.tickCount <= 3) {
            return;
        }
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() - vec3.x;
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - vec3.z;
        int count = Mth.clamp((int)((int)(vec3.lengthSqr() * 4.0)), (int)1, (int)5);
        for (int i = 0; i < count; ++i) {
            Vec3 random = Utils.getRandomVec3(0.1);
            float f = (float)i / (float)count;
            double x = Mth.lerp((double)f, (double)d0, (double)this.getX());
            double y = Mth.lerp((double)f, (double)d1, (double)this.getY());
            double z = Mth.lerp((double)f, (double)d2, (double)this.getZ());
            this.level.addParticle(ParticleHelper.EMBERS, x - random.x, y + 0.5 - random.y, z - random.z, random.x * 0.5, random.y * 0.5, random.z * 0.5);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, ParticleHelper.FIERY_SPARKS, x, y, z, 5, 0.0, 0.0, 0.0, 0.25, true);
        MagicManager.spawnParticles(this.level, ParticleHelper.FIERY_SPARKS, x, y, z, 5, 0.0, 0.0, 0.0, 0.25, false);
    }

    @Override
    public float getSpeed() {
        return 1.85f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundRegistry.FIRE_IMPACT);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!this.level.isClientSide) {
            Entity target = pResult.getEntity();
            Entity owner = this.getOwner();
            DamageSources.applyDamage(target, this.damage, SpellRegistry.BLAZE_STORM_SPELL.get().getDamageSource((Entity)this, owner));
        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
        BlockPos blockpos;
        super.onHitBlock(pResult);
        if (!this.level.isClientSide && ((Boolean)ServerConfigs.SPELL_GREIFING.get()).booleanValue() && this.level.isEmptyBlock(blockpos = pResult.getBlockPos().relative(pResult.getDirection()))) {
            this.level.setBlockAndUpdate(blockpos, BaseFireBlock.getState((BlockGetter)this.level, (BlockPos)blockpos));
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        this.discardHelper(pResult);
    }
}

