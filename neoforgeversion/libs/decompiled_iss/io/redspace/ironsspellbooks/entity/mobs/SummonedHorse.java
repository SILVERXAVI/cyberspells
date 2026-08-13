/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.PanicGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.entity.animal.horse.AbstractHorse
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericFollowOwnerGoal;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonedHorse
extends AbstractHorse
implements IMagicSummon {
    public SummonedHorse(EntityType<? extends AbstractHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SummonedHorse(Level pLevel) {
        this((EntityType<? extends AbstractHorse>)((EntityType)EntityRegistry.SPECTRAL_STEED.get()), pLevel);
    }

    @Deprecated(forRemoval=true)
    public SummonedHorse(Level pLevel, LivingEntity owner) {
        this(pLevel);
        this.setOwnerUUID(owner.getUUID());
        this.setSummoner(owner);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(2, (Goal)new GenericFollowOwnerGoal((PathfinderMob)this, this::getSummoner, 0.8f, 12.0f, 4.0f, false, 32.0f));
        this.goalSelector.addGoal(3, (Goal)new PanicGoal((PathfinderMob)this, (double)0.9f));
        this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.6));
        this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
    }

    public void openCustomInventoryScreen(Player pPlayer) {
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.JUMP_STRENGTH, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    public void tick() {
        this.spawnParticles();
        super.tick();
    }

    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.HORSE_DEATH;
    }

    @Override
    public void onUnSummon() {
        if (!this.level.isClientSide) {
            MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.setRemoved(Entity.RemovalReason.DISCARDED);
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.shouldIgnoreDamage(pSource)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Nullable
    protected SoundEvent getEatingSound() {
        return SoundEvents.HORSE_EAT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.HORSE_HURT;
    }

    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.HORSE_ANGRY;
    }

    public void spawnParticles() {
        if (this.level.isClientSide && Utils.random.nextFloat() < 0.25f) {
            float radius = 0.75f;
            Vec3 vec = new Vec3((double)(this.random.nextFloat() * 2.0f * radius - radius), (double)(this.random.nextFloat() * 2.0f * radius - radius), (double)(this.random.nextFloat() * 2.0f * radius - radius));
            this.level.addParticle((ParticleOptions)ParticleTypes.ENCHANT, this.getX() + vec.x, this.getY() + vec.y + 1.0, this.getZ() + vec.z, vec.x * (double)0.01f, 0.08 + vec.y * (double)0.01f, vec.z * (double)0.01f);
        }
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (this.isVehicle()) {
            return super.mobInteract(pPlayer, pHand);
        }
        if (pPlayer == this.getSummoner()) {
            this.doPlayerRide(pPlayer);
        } else {
            this.makeMad();
        }
        return InteractionResult.sidedSuccess((boolean)this.level.isClientSide);
    }

    @Deprecated(forRemoval=true)
    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner == null) {
            return;
        }
        SummonManager.setOwner((Entity)this, (Entity)owner);
    }

    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    public void onRemovedFromLevel() {
        this.onRemovedHelper((Entity)this);
        super.onRemovedFromLevel();
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setOwnerUUID(OwnerHelper.deserializeOwner(compoundTag));
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, this.getOwnerUUID());
    }

    public boolean canBeLeashed() {
        return false;
    }

    protected boolean canParent() {
        return false;
    }

    public boolean isSaddled() {
        return true;
    }

    public boolean isTamed() {
        return true;
    }
}

