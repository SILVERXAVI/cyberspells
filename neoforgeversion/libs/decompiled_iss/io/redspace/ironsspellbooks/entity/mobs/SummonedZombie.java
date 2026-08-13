/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.MeleeAttackGoal
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.entity.monster.Zombie
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.state.BlockState
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 *  software.bernie.geckolib.util.GeckoLibUtil
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
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SummonedZombie
extends Zombie
implements IMagicSummon,
GeoAnimatable {
    private static final EntityDataAccessor<Boolean> DATA_IS_ANIMATING_RISE = SynchedEntityData.defineId(SummonedZombie.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private int riseAnimTime = 80;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);

    public SummonedZombie(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 0;
    }

    @Deprecated(forRemoval=true)
    public SummonedZombie(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this((EntityType<? extends Zombie>)((EntityType)EntityRegistry.SUMMONED_ZOMBIE.get()), level);
        this.setSummoner(owner);
        if (playRiseAnimation) {
            this.triggerRiseAnimation();
        }
    }

    public void registerGoals() {
        this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(1, (Goal)new MeleeAttackGoal((PathfinderMob)this, (double)1.2f, true));
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

    public boolean isPreventingPlayerRest(Player pPlayer) {
        return !this.isAlliedTo((Entity)pPlayer);
    }

    protected boolean isSunSensitive() {
        return false;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_ANIMATING_RISE, (Object)false);
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        if (randomsource.nextDouble() < 0.25) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SWORD));
        }
        return pSpawnData;
    }

    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
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

    public void remove(Entity.RemovalReason pReason) {
        super.remove(pReason);
    }

    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack((Mob)this, pEntity, SpellRegistry.RAISE_DEAD_SPELL.get().getDamageSource((Entity)this, this.getSummoner()));
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && (this.isAnimatingRise() || this.shouldIgnoreDamage(pSource))) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public void tick() {
        if (this.isAnimatingRise()) {
            if (this.level().isClientSide) {
                this.clientDiggingParticles((LivingEntity)this);
            }
            if (--this.riseAnimTime < 0) {
                this.entityData.set(DATA_IS_ANIMATING_RISE, (Object)false);
                this.setXRot(0.0f);
                this.setOldPosAndRot();
            }
        } else {
            super.tick();
        }
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void onUnSummon() {
        if (!this.level().isClientSide) {
            MagicManager.spawnParticles(this.level(), (ParticleOptions)ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 25, 0.4, 0.8, 0.4, 0.03, false);
            this.setRemoved(Entity.RemovalReason.DISCARDED);
        }
    }

    protected void clientDiggingParticles(LivingEntity livingEntity) {
        RandomSource randomsource = livingEntity.getRandom();
        BlockState blockstate = livingEntity.getBlockStateOn();
        if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 15; ++i) {
                double d0 = livingEntity.getX() + (double)Mth.randomBetween((RandomSource)randomsource, (float)-0.5f, (float)0.5f);
                double d1 = livingEntity.getY();
                double d2 = livingEntity.getZ() + (double)Mth.randomBetween((RandomSource)randomsource, (float)-0.5f, (float)0.5f);
                livingEntity.level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0, 0.0, 0.0);
            }
        }
    }

    public boolean isAnimatingRise() {
        return (Boolean)this.entityData.get(DATA_IS_ANIMATING_RISE);
    }

    public void triggerRiseAnimation() {
        this.entityData.set(DATA_IS_ANIMATING_RISE, (Object)true);
    }

    public boolean isPushable() {
        return super.isPushable() && !this.isAnimatingRise();
    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.isAnimatingRise();
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController((GeoAnimatable)this, "rise", 0, this::risePredicate));
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public double getTick(Object o) {
        return this.tickCount;
    }

    private PlayState risePredicate(AnimationState event) {
        if (!this.isAnimatingRise()) {
            return PlayState.STOP;
        }
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            String animation = (new String[]{"rise_from_ground_01", "rise_from_ground_02", "rise_from_ground_03", "rise_from_ground_04"})[this.random.nextIntBetweenInclusive(0, 3)];
            event.getController().setAnimation(RawAnimation.begin().thenPlay(animation));
        }
        return PlayState.CONTINUE;
    }
}

