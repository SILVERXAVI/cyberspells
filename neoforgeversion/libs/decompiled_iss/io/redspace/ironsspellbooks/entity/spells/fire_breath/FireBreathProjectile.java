/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BaseFireBlock
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.fire_breath;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireBreathProjectile
extends AbstractConeProjectile {
    public FireBreathProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public FireBreathProjectile(Level level, LivingEntity entity) {
        super((EntityType<? extends AbstractConeProjectile>)((EntityType)EntityRegistry.FIRE_BREATH_PROJECTILE.get()), level, entity);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide && this.dealDamageActive && ((Boolean)ServerConfigs.SPELL_GREIFING.get()).booleanValue()) {
            float range = 0.2617994f;
            for (int i = 0; i < 3; ++i) {
                HitResult shieldResult;
                Vec3 cast = this.getOwner().getLookAngle().normalize().xRot(Utils.random.nextFloat() * range * 2.0f - range).yRot(Utils.random.nextFloat() * range * 2.0f - range);
                BlockHitResult hitResult = this.level.clip(new ClipContext(this.getOwner().getEyePosition(), this.getOwner().getEyePosition().add(cast.scale(10.0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this));
                if (hitResult.getType() != HitResult.Type.BLOCK || (shieldResult = Utils.raycastForEntityOfClass(this.level, (Entity)this, this.getOwner().getEyePosition(), hitResult.getLocation(), false, AbstractShieldEntity.class)).getType() != HitResult.Type.MISS) continue;
                Vec3 pos = hitResult.getLocation().subtract(cast.scale(0.5));
                BlockPos blockPos = BlockPos.containing((double)pos.x, (double)pos.y, (double)pos.z);
                if (!this.level.getBlockState(blockPos).isAir()) continue;
                this.level.setBlockAndUpdate(blockPos, BaseFireBlock.getState((BlockGetter)this.level, (BlockPos)blockPos));
            }
        }
        super.tick();
    }

    @Override
    public void spawnParticles() {
        Entity owner = this.getOwner();
        if (!this.level.isClientSide || owner == null) {
            return;
        }
        Vec3 rotation = owner.getLookAngle().normalize();
        Vec3 pos = owner.position().add(rotation.scale(1.6));
        double x = pos.x;
        double y = pos.y + (double)(owner.getEyeHeight() * 0.9f);
        double z = pos.z;
        double speed = this.random.nextDouble() * 0.35 + 0.35;
        for (int i = 0; i < 10; ++i) {
            double offset = 0.15;
            double ox = Math.random() * 2.0 * offset - offset;
            double oy = Math.random() * 2.0 * offset - offset;
            double oz = Math.random() * 2.0 * offset - offset;
            double angularness = 0.5;
            Vec3 randomVec = new Vec3(Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness, Math.random() * 2.0 * angularness - angularness).normalize();
            Vec3 result = rotation.scale(3.0).add(randomVec).normalize().scale(speed);
            this.level.addParticle(ParticleHelper.FIRE_EMITTER, x + ox, y + oy, z + oz, result.x, result.y, result.z);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        DamageSources.applyDamage(entity, this.damage, SpellRegistry.FIRE_BREATH_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
    }
}

