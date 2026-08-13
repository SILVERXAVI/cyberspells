/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.FlyingMoveControl
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal
 *  net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.fluids.FluidType
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package io.redspace.ironsspellbooks.entity.spells.summoned_weapons;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericCopyOwnerTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericFollowOwnerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericOwnerHurtTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericProtectOwnerTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class SummonedWeaponEntity
extends AbstractSpellCastingMob
implements IMagicSummon,
IAnimatedAttacker {
    GenericAnimatedWarlockAttackGoal<? extends SummonedWeaponEntity> attackGoal;
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
    private final AnimationController<SummonedWeaponEntity> meleeController = new AnimationController((GeoAnimatable)this, "animations", 0, this::predicate);
    RawAnimation animationToPlay = null;

    @Override
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
    }

    public abstract GenericAnimatedWarlockAttackGoal<? extends SummonedWeaponEntity> makeAttackGoal();

    public SummonedWeaponEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl((Mob)this, 20, true);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        this.setNoGravity(true);
        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }

    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    public boolean fireImmune() {
        return true;
    }

    protected void registerGoals() {
        this.attackGoal = this.makeAttackGoal();
        this.goalSelector.addGoal(1, (Goal)this.attackGoal.setMeleeBias(1.0f, 1.0f));
        this.goalSelector.addGoal(3, (Goal)new GenericFollowOwnerGoal(this, this::getSummoner, 1.0, 9.0f, 4.0f, true, 20.0f));
        this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomFlyingGoal((PathfinderMob)this, 0.75));
        this.targetSelector.addGoal(1, (Goal)new GenericOwnerHurtByTargetGoal((Mob)this, this::getSummoner));
        this.targetSelector.addGoal(2, (Goal)new GenericOwnerHurtTargetGoal((Mob)this, this::getSummoner));
        this.targetSelector.addGoal(3, (Goal)new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (Goal)new GenericHurtByTargetGoal(this, entity -> entity == this.getSummoner()).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(5, (Goal)new GenericProtectOwnerTargetGoal((Mob)this, this::getSummoner));
    }

    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation((Mob)this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.tickCount % 8 == 0) {
            Entity owner = this.getSummoner();
            LivingEntity target = this.getTarget();
            Entity trackEntity = target == null ? owner : target;
            double targetY = trackEntity == null ? Utils.moveToRelativeGroundLevel((Level)this.level, (Vec3)this.position(), (int)3).y + 1.0 : trackEntity.getY() + 1.0;
            double f = targetY - this.getY();
            double force = Math.clamp((double)(f * 0.05), (double)-0.15, (double)0.15);
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, force, 0.0));
        }
        if (this.tickCount % 80 == 0) {
            this.heal(1.0f);
        }
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack((Mob)this, pEntity, SpellRegistry.SUMMON_SWORDS.get().getDamageSource((Entity)this, this.getSummoner()));
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Deprecated(forRemoval=true)
    public void setSummoner(@javax.annotation.Nullable LivingEntity owner) {
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

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && this.shouldIgnoreDamage(pSource)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void onUnSummon() {
        if (!this.level.isClientSide) {
            MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.setRemoved(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public double getBoneResetTime() {
        return 3.0;
    }

    @Override
    public void playAnimation(String animationId) {
        this.animationToPlay = RawAnimation.begin().thenPlay(animationId);
    }

    private PlayState predicate(AnimationState<SummonedWeaponEntity> animationEvent) {
        AnimationController controller = animationEvent.getController();
        if (this.animationToPlay != null) {
            controller.forceAnimationReset();
            controller.setAnimation(this.animationToPlay);
            this.animationToPlay = null;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.summonerUUID = OwnerHelper.deserializeOwner(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, this.summonerUUID);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.FLYING_SPEED, 1.0).add(Attributes.ENTITY_INTERACTION_RANGE, 4.0).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(this.meleeController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

