/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
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
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.entity.animal.PolarBear
 *  net.minecraft.world.entity.animal.PolarBear$PolarBearMeleeAttackGoal
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericCopyOwnerTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericFollowOwnerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericProtectOwnerTargetGoal;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonedPolarBear
extends PolarBear
implements IMagicSummon {
    public SummonedPolarBear(EntityType<? extends PolarBear> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 0;
    }

    @Deprecated(forRemoval=true)
    public SummonedPolarBear(Level pLevel, LivingEntity owner) {
        this((EntityType<? extends PolarBear>)((EntityType)EntityRegistry.SUMMONED_POLAR_BEAR.get()), pLevel);
        this.setSummoner(owner);
    }

    public float maxUpStep() {
        return 1.0f;
    }

    public void registerGoals() {
        this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(1, (Goal)new PolarBear.PolarBearMeleeAttackGoal((PolarBear)this));
        this.goalSelector.addGoal(7, (Goal)new GenericFollowOwnerGoal((PathfinderMob)this, this::getSummoner, 0.9f, 15.0f, 5.0f, false, 25.0f));
        this.goalSelector.addGoal(8, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.8));
        this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0f));
        this.targetSelector.addGoal(1, (Goal)new GenericOwnerHurtByTargetGoal((Mob)this, this::getSummoner));
        this.targetSelector.addGoal(2, (Goal)new GenericOwnerHurtTargetGoal((Mob)this, this::getSummoner));
        this.targetSelector.addGoal(3, (Goal)new GenericCopyOwnerTargetGoal((PathfinderMob)this, this::getSummoner));
        this.targetSelector.addGoal(4, (Goal)new GenericHurtByTargetGoal((PathfinderMob)this, entity -> entity == this.getSummoner()).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, (Goal)new GenericProtectOwnerTargetGoal((Mob)this, this::getSummoner));
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (this.isVehicle()) {
            return super.mobInteract(pPlayer, pHand);
        }
        if (pPlayer == this.getSummoner()) {
            this.doPlayerRide(pPlayer);
        }
        return InteractionResult.sidedSuccess((boolean)this.level.isClientSide);
    }

    protected void doPlayerRide(Player pPlayer) {
        this.setStanding(false);
        if (!this.level.isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding((Entity)this);
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.tickCount % 80 == 0) {
            this.heal(1.0f);
        }
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
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
    }

    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack((Mob)this, pEntity, SpellRegistry.SUMMON_POLAR_BEAR_SPELL.get().getDamageSource((Entity)this, this.getSummoner()));
    }

    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.STEP_HEIGHT, 1.0).add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Mob) {
            return (Mob)entity;
        }
        entity = this.getFirstPassenger();
        if (entity instanceof Player) {
            return (Player)entity;
        }
        return null;
    }

    protected void tickRidden(Player player, Vec3 p_275242_) {
        super.tickRidden(player, p_275242_);
        this.yRotO = this.getYRot();
        this.setYRot(player.getYRot());
        this.setXRot(player.getXRot());
        this.setRot(this.getYRot(), this.getXRot());
        this.yBodyRot = this.yRotO;
        this.yHeadRot = this.getYRot();
    }

    protected Vec3 getRiddenInput(Player player, Vec3 p_275300_) {
        float f = player.xxa * 0.5f;
        float f1 = player.zza;
        if (f1 <= 0.0f) {
            f1 *= 0.25f;
        }
        if (this.isInWater()) {
            f *= 0.3f;
            f1 *= 0.3f;
        }
        return new Vec3((double)f, 0.0, (double)f1);
    }

    protected float getRiddenSpeed(Player p_278336_) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.8f;
    }
}

