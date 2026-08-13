/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.item.FallingBlockEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.common.Tags$EntityTypes
 */
package io.redspace.ironsspellbooks.entity.spells.black_hole;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

public class BlackHole
extends Projectile
implements AntiMagicSusceptible {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(BlackHole.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    List<Entity> trackingEntities = new ArrayList<Entity>();
    private float damage;
    private int duration = 600;
    private static final int loopSoundDurationInTicks = 40;

    public BlackHole(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BlackHole(Level pLevel, LivingEntity owner) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.BLACK_HOLE.get()), pLevel);
        this.setOwner((Entity)owner);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable((float)(this.getRadius() * 2.0f), (float)(this.getRadius() * 2.0f));
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_RADIUS, (Object)Float.valueOf(5.0f));
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (this.getRadius() < 0.1f) {
                this.discard();
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void setRadius(float pRadius) {
        if (!this.level().isClientSide) {
            this.getEntityData().set(DATA_RADIUS, (Object)Float.valueOf(Math.min(pRadius, 48.0f)));
        }
    }

    public float getRadius() {
        return ((Float)this.getEntityData().get(DATA_RADIUS)).floatValue();
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putInt("Age", this.tickCount);
        pCompound.putFloat("Damage", this.getDamage());
        pCompound.putInt("Duration", this.duration);
        super.addAdditionalSaveData(pCompound);
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("Age");
        this.damage = pCompound.getFloat("Damage");
        this.duration = pCompound.getInt("Duration");
        if (this.damage == 0.0f) {
            this.damage = 1.0f;
        }
        if (pCompound.getInt("Radius") > 0) {
            this.setRadius(pCompound.getFloat("Radius"));
        }
        super.readAdditionalSaveData(pCompound);
    }

    public void tick() {
        super.tick();
        int update = Math.max((int)(this.getRadius() / 2.0f), 2);
        if (this.tickCount % update == 0) {
            this.updateTrackingEntities();
        }
        AABB bb = this.getBoundingBox();
        float radius = (float)bb.getXsize();
        boolean hitTick = this.tickCount % 10 == 0;
        Vec3 center = bb.getCenter();
        for (Entity entity : this.trackingEntities) {
            float f;
            float distance;
            if (entity == this.getOwner() || DamageSources.isFriendlyFireBetween(this.getOwner(), entity) || entity.isSpectator() || (distance = (float)center.distanceTo(entity.position())) > radius) continue;
            float f2 = 1.0f - distance / radius;
            float scale = f2 * f2 * f2 * f2 * 0.25f;
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                f = Mth.clamp((float)(1.0f - (float)livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)), (float)0.3f, (float)1.0f);
            } else {
                f = 1.0f;
            }
            float resistance = f;
            float bossResistance = entity.getType().is(Tags.EntityTypes.BOSSES) ? 0.5f : 1.0f;
            Vec3 diff = center.subtract(entity.position()).scale((double)(scale * resistance * bossResistance));
            entity.push(diff.x, diff.y, diff.z);
            double dmgRadius = Math.min(2.0, (double)radius / 5.0);
            if (hitTick && (double)distance < dmgRadius * dmgRadius && this.canHitEntity(entity)) {
                DamageSources.applyDamage(entity, this.damage, SpellRegistry.BLACK_HOLE_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
            }
            entity.fallDistance = 0.0f;
        }
        if (!this.level.isClientSide && ((Boolean)ServerConfigs.SPELL_GREIFING.get()).booleanValue()) {
            BlockHitResult blockHit;
            int tries = 0;
            do {
                BlockPos blockpos;
                Vec3 dir;
                Vec3 pick;
                if ((blockHit = Utils.raycastForBlock(this.level, center, center.add(pick = (dir = Utils.getRandomVec3(1.0).normalize()).scale((double)radius * 1.25)), ClipContext.Fluid.NONE)).getType() == HitResult.Type.MISS || this.level.getBlockEntity(blockpos = blockHit.getBlockPos()) != null) continue;
                BlockState state = this.level.getBlockState(blockpos);
                this.level.setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
                Vec3 spawn = blockpos.getCenter().subtract(dir.scale(1.5));
                FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(this.level, spawn.x, spawn.y, spawn.z, state);
                fallingBlockEntity.setDeltaMovement(dir.scale(-0.1));
                this.level.addFreshEntity((Entity)fallingBlockEntity);
            } while (blockHit.getType() == HitResult.Type.MISS && tries++ < 3);
        }
        if (!this.level().isClientSide) {
            if (this.tickCount > this.duration) {
                this.discard();
                this.playSound((SoundEvent)SoundRegistry.BLACK_HOLE_CAST.get(), this.getRadius() / 2.0f, 1.0f);
                MagicManager.spawnParticles(this.level(), ParticleHelper.UNSTABLE_ENDER, this.getX(), this.getY() + (double)this.getRadius(), this.getZ(), 200, 1.0, 1.0, 1.0, 1.0, true);
                for (Entity entity : this.trackingEntities) {
                    if (!(entity.distanceToSqr(center) < 9.0)) continue;
                    entity.setDeltaMovement(entity.getDeltaMovement().add(entity.position().subtract(center).normalize().scale(0.5)));
                    entity.hurtMarked = true;
                }
            } else if ((this.tickCount - 1) % 40 == 0 && (this.duration < 40 || this.tickCount + 40 < this.duration)) {
                this.playSound((SoundEvent)SoundRegistry.BLACK_HOLE_LOOP.get(), this.getRadius() / 3.0f, 0.9f + Utils.random.nextFloat() * 0.2f);
            }
        }
    }

    private void updateTrackingEntities() {
        this.trackingEntities = this.level().getEntities((Entity)this, this.getBoundingBox().inflate(1.0));
    }

    public boolean displayFireAnimation() {
        return false;
    }
}

