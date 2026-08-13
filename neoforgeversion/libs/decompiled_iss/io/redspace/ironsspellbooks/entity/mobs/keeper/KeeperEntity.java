/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.BodyRotationControl
 *  net.minecraft.world.entity.ai.control.LookControl
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.IEntityWithComplexSpawn
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 */
package io.redspace.ironsspellbooks.entity.mobs.keeper;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.NotIdioticNavigation;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class KeeperEntity
extends AbstractSpellCastingMob
implements Enemy,
IAnimatedAttacker,
IEntityWithComplexSpawn {
    private static final EntityDataAccessor<Boolean> DATA_IS_SUMMONED = SynchedEntityData.defineId(KeeperEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_RESTORED = SynchedEntityData.defineId(KeeperEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    public static final int RISE_ANIM_TIME = 25;
    public int riseAnimTick;
    public int destroyBlockDelay;
    private final AnimationController<KeeperEntity> meleeController = new AnimationController((GeoAnimatable)this, "keeper_animations", 0, this::predicate);
    RawAnimation animationToPlay = null;

    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.riseAnimTick);
    }

    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        float y;
        this.riseAnimTick = additionalData.readInt();
        if (this.riseAnimTick > 0) {
            this.animationToPlay = RawAnimation.begin().thenPlay("keeper_kneeling_rise");
        }
        this.yBodyRot = y = this.getYRot();
        this.yBodyRotO = y;
        this.yHeadRot = y;
        this.yHeadRotO = y;
        this.yRotO = y;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_SUMMONED, (Object)false);
        pBuilder.define(DATA_IS_RESTORED, (Object)false);
    }

    public void triggerRise() {
        this.riseAnimTick = 25;
    }

    public KeeperEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 25;
        this.lookControl = this.createLookControl();
        this.moveControl = this.createMoveControl();
    }

    public boolean isSummoned() {
        return (Boolean)this.entityData.get(DATA_IS_SUMMONED);
    }

    public void setIsSummoned() {
        this.entityData.set(DATA_IS_SUMMONED, (Object)true);
    }

    public boolean isRestored() {
        return (Boolean)this.entityData.get(DATA_IS_RESTORED);
    }

    public void setIsRestored() {
        this.entityData.set(DATA_IS_RESTORED, (Object)true);
    }

    protected boolean shouldDropLoot() {
        return super.shouldDropLoot() && !this.isSummoned();
    }

    public boolean shouldDropExperience() {
        return super.shouldDropExperience() && !this.isSummoned();
    }

    public void tick() {
        super.tick();
        if (this.riseAnimTick > 0) {
            --this.riseAnimTick;
            if (!this.level.isClientSide) {
                Vec3 vec3 = this.getBoundingBox().getCenter();
                MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(), vec3.x, vec3.y, vec3.z, 5, 0.2, 0.2, 0.2, 0.05, false);
            }
        }
    }

    public boolean isRising() {
        return this.riseAnimTick > 0;
    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.isRising();
    }

    public KeeperEntity(Level pLevel) {
        this((EntityType<? extends AbstractSpellCastingMob>)((EntityType)EntityRegistry.KEEPER.get()), pLevel);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(4, (Goal)new KeeperAnimatedWarlockAttackGoal(this, 1.0, 10, 30));
        this.goalSelector.addGoal(4, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0));
        this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
        this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Mob.class, true, entity -> !entity.getType().is(ModTags.INFERNAL_ALLIES)));
    }

    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl((Mob)this);
    }

    @Override
    protected LookControl createLookControl() {
        return new LookControl(this, (Mob)this){

            protected float rotateTowards(float pFrom, float pTo, float pMaxDelta) {
                return super.rotateTowards(pFrom, pTo, pMaxDelta * 2.5f);
            }
        };
    }

    protected MoveControl createMoveControl() {
        return new MoveControl(this, (Mob)this){

            protected float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) {
                double d1;
                double d0 = this.wantedX - this.mob.getX();
                if (d0 * d0 + (d1 = this.wantedZ - this.mob.getZ()) * d1 < 0.5) {
                    return pSourceAngle;
                }
                return super.rotlerp(pSourceAngle, pTargetAngle, pMaximumChange * 0.25f);
            }
        };
    }

    protected SoundEvent getAmbientSound() {
        return (SoundEvent)SoundRegistry.KEEPER_IDLE.get();
    }

    public void playAmbientSound() {
        this.playSound(this.getAmbientSound(), 1.0f, (float)Mth.randomBetweenInclusive((RandomSource)this.getRandom(), (int)5, (int)10) * 0.1f);
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return (SoundEvent)SoundRegistry.KEEPER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return (SoundEvent)SoundRegistry.KEEPER_DEATH.get();
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return pSpawnData;
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(this.isRestored() ? ItemRegistry.LEGIONNAIRE_FLAMBERGE : ItemRegistry.KEEPER_FLAMBERGE));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 25.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.8).add(Attributes.ATTACK_KNOCKBACK, 2.0).add(Attributes.STEP_HEIGHT, 1.0).add(Attributes.ENTITY_INTERACTION_RANGE, 3.5).add(Attributes.MOVEMENT_SPEED, 0.19);
    }

    public boolean fireImmune() {
        return true;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity entity = pSource.getDirectEntity();
        if (entity instanceof Projectile) {
            Projectile projectile = (Projectile)entity;
            pAmount *= 0.75f;
        }
        if (this.tickCount < 10 && pSource.is(DamageTypes.IN_WALL)) {
            Utils.doMobBreakSuffocatingBlocks((LivingEntity)this);
        }
        return super.hurt(pSource, pAmount);
    }

    protected void playStepSound(BlockPos pPos, BlockState pState) {
        this.playSound((SoundEvent)SoundRegistry.KEEPER_STEP.get(), 0.25f, 1.0f);
    }

    protected float nextStep() {
        return this.moveDist + 0.8f;
    }

    public boolean isInvulnerableTo(DamageSource pSource) {
        return super.isInvulnerableTo(pSource) || pSource.is(DamageTypeTags.IS_FALL);
    }

    @Override
    public void playAnimation(String animationId) {
        try {
            AttackType attackType = AttackType.valueOf(animationId);
            this.animationToPlay = RawAnimation.begin().thenPlay(attackType.data.animationId);
        }
        catch (Exception ignored) {
            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", (Object)this, (Object)animationId);
        }
    }

    private PlayState predicate(AnimationState<KeeperEntity> animationEvent) {
        AnimationController controller = animationEvent.getController();
        if (this.animationToPlay != null) {
            controller.forceAnimationReset();
            controller.setAnimation(this.animationToPlay);
            this.animationToPlay = null;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.meleeController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public boolean isAnimating() {
        return this.meleeController.getAnimationState() != AnimationController.State.STOPPED || super.isAnimating();
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new NotIdioticNavigation((Mob)this, pLevel);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.isSummoned()) {
            pCompound.putBoolean("summoned", true);
        }
        if (this.isRestored()) {
            pCompound.putBoolean("restored", true);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.getBoolean("summoned")) {
            this.setIsSummoned();
        }
        if (pCompound.getBoolean("restored")) {
            this.setIsRestored();
        }
    }

    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || pEntity.getType().is(ModTags.INFERNAL_ALLIES);
    }

    public static enum AttackType {
        Double_Slash(43, "sword_double_slash", 13, 29),
        Single_Upward(26, "sword_single_upward", 13),
        Single_Horizontal(28, "sword_single_horizontal", 12),
        Single_Horizontal_Fast(24, "sword_single_horizontal_fast", 12),
        Single_Stab(21, "sword_stab", 11),
        Lunge(76, "sword_lunge", 56, 57, 58, 59, 60, 61, 62, 63, 64);

        public final AttackAnimationData data;

        private AttackType(int lengthInTicks, String animationId, int ... attackTimestamps) {
            this.data = new AttackAnimationData(lengthInTicks, animationId, attackTimestamps);
        }
    }
}

