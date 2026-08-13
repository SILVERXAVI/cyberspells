/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.shapes.CollisionContext
 */
package io.redspace.ironsspellbooks.entity.spells.ice_spike;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;

public class IceSpikeEntity
extends AoeEntity {
    private static final EntityDataAccessor<Float> DATA_SIZE = SynchedEntityData.defineId(IceSpikeEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_WAIT_TIME = SynchedEntityData.defineId(IceSpikeEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    public static final int RISE_TIME = 6;
    public static final int REST_TIME = 40;
    public static final int LOWER_TIME = 30;
    private final List<Entity> victims = new ArrayList<Entity>();

    public IceSpikeEntity(EntityType<? extends AoeEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public IceSpikeEntity(Level level, LivingEntity owner) {
        this((EntityType<? extends AoeEntity>)((EntityType)EntityRegistry.ICE_SPIKE.get()), level);
        this.setOwner((Entity)owner);
    }

    @Override
    public void applyEffect(LivingEntity target) {
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_SIZE, (Object)Float.valueOf(1.0f));
        pBuilder.define(DATA_WAIT_TIME, (Object)10);
    }

    public float getSpikeSize() {
        return ((Float)this.entityData.get(DATA_SIZE)).floatValue();
    }

    public void setSpikeSize(float f) {
        this.entityData.set(DATA_SIZE, (Object)Float.valueOf(f));
        this.refreshDimensions();
    }

    public int getWaitTime() {
        return (Integer)this.entityData.get(DATA_WAIT_TIME);
    }

    public void setWaitTime(int i) {
        this.entityData.set(DATA_WAIT_TIME, (Object)i);
    }

    public float getPositionOffset(float partialTick) {
        float f = (float)this.tickCount + partialTick;
        int waitTime = this.getWaitTime();
        if (f < (float)waitTime) {
            return -1.0f;
        }
        if (f < (float)(waitTime + 6)) {
            f = (f - (float)waitTime) / 6.0f;
            return Mth.sin((float)(f * (float)Math.PI)) / (float)Math.PI + f - 1.0f;
        }
        if (f < (float)(waitTime + 6 + 40)) {
            return 0.0f;
        }
        f = Mth.clamp((float)((f - (float)(waitTime + 6 + 40)) / 30.0f), (float)0.0f, (float)1.0f) + 1.0f;
        return -(Mth.sin((float)(f * (float)Math.PI)) / (float)Math.PI + f - 1.0f);
    }

    @Override
    public void tick() {
        this.refreshDimensions();
        int waitTime = this.getWaitTime();
        if (this.tickCount == waitTime) {
            if (!this.level.isClientSide) {
                float f = this.getSpikeSize();
                if (!this.isSilent()) {
                    this.level.playSound(null, this.blockPosition(), (SoundEvent)SoundRegistry.ICE_SPIKE_EMERGE.get(), SoundSource.NEUTRAL, 1.25f * this.getSpikeSize(), (float)Mth.randomBetweenInclusive((RandomSource)Utils.random, (int)6, (int)12) * 0.1f);
                }
                MagicManager.spawnParticles(this.level, ParticleHelper.SNOWFLAKE, this.getX(), this.level.clip(new ClipContext(this.position().add(0.0, 2.0, 0.0), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getLocation().y() + 0.1, this.getZ(), (int)(10.0f * f * f), 0.1 * (double)f, 0.1 * (double)f, 0.1f * f, 0.12 * (double)f, false);
                MagicManager.spawnParticles(this.level, ParticleHelper.SNOW_DUST, this.getX(), this.level.clip(new ClipContext(this.position().add(0.0, 2.0, 0.0), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getLocation().y() + 0.1, this.getZ(), (int)(15.0f * f * f), 0.1 * (double)f, 0.1 * (double)f, 0.1f * f, 0.08 * (double)f, false);
            }
        } else if (this.tickCount > waitTime && this.tickCount < waitTime + 6) {
            AABB damager = this.getBoundingBox();
            damager.setMaxY(this.getY() + damager.getYsize() * (double)(this.getPositionOffset(0.0f) + 1.0f));
            for (Entity entity : this.level.getEntities((Entity)this, damager).stream().filter(target -> this.canHitEntity((Entity)target) && !this.victims.contains(target)).collect(Collectors.toSet())) {
                if (DamageSources.applyDamage(entity, this.damage, SpellRegistry.ICE_SPIKES_SPELL.get().getDamageSource((Entity)this, this.getOwner()))) {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, (double)this.getSpikeSize() * 0.3, 0.0));
                    entity.hurtMarked = true;
                    entity.setTicksFrozen(entity.getTicksFrozen() + (int)(40.0f * this.getSpikeSize()));
                }
                this.victims.add(entity);
                if (!(entity instanceof ShieldPart) && !(entity instanceof AbstractShieldEntity)) continue;
                this.discard();
                return;
            }
        } else if (this.tickCount > waitTime + 6 + 40 + 30) {
            this.discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("waitTime", this.getWaitTime());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setWaitTime(pCompound.getInt("waitTime"));
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable((float)(this.getSpikeSize() * 0.4f), (float)(this.getSpikeSize() * 1.25f * (this.getPositionOffset(1.0f) + 1.0f)));
    }

    public boolean canBeCollidedWith() {
        return true;
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

