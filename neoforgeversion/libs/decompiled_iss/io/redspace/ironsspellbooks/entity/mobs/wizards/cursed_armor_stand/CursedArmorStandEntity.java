/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.RandomSource
 *  net.minecraft.util.TimeUtil
 *  net.minecraft.util.valueproviders.UniformInt
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.NeutralMob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.LookControl
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.ArmorStandAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.ArmorStandReturnToHomeGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.NotIdioticNavigation;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.NBT;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class CursedArmorStandEntity
extends AbstractSpellCastingMob
implements IAnimatedAttacker,
NeutralMob {
    public static final int JIGGLE_TIME = 15;
    private static final EntityDataAccessor<Boolean> DATA_FROZEN = SynchedEntityData.defineId(CursedArmorStandEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_POSE = SynchedEntityData.defineId(CursedArmorStandEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    @javax.annotation.Nullable
    Vec3 spawn = null;
    float originalYRot = 0.0f;
    int bootJiggle;
    int legJiggle;
    int chestJiggle;
    int helmetJiggle;
    int interactionAnger;
    RawAnimation animationToPlay = null;
    private final AnimationController<CursedArmorStandEntity> meleeController = new AnimationController((GeoAnimatable)this, "keeper_animations", 0, this::predicate);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds((int)20, (int)39);
    private int remainingPersistentAngerTime;
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_FROZEN, (Object)true);
        pBuilder.define(DATA_POSE, (Object)"DEFAULT");
    }

    public boolean isArmorStandFrozen() {
        return (Boolean)this.entityData.get(DATA_FROZEN);
    }

    public Pose getArmorstandPose() {
        return Pose.valueOf((String)this.entityData.get(DATA_POSE));
    }

    public void setArmorstandPose(Pose pose) {
        this.entityData.set(DATA_POSE, (Object)pose.name());
    }

    public void setArmorStandFrozen(boolean frozen) {
        boolean wasFrozen = this.isArmorStandFrozen();
        this.entityData.set(DATA_FROZEN, (Object)frozen);
        if (frozen) {
            this.setYHeadRot(this.originalYRot);
            this.setYBodyRot(this.originalYRot);
            this.setYRot(this.originalYRot);
        } else if (wasFrozen && !this.level.isClientSide) {
            MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + 1.25, this.getZ(), 15, 0.3, 0.2, 0.3, 0.0, false);
        }
    }

    public InteractionResult interactAt(Player pPlayer, Vec3 pVector, InteractionHand pHand) {
        if (this.isArmorStandFrozen()) {
            if (pPlayer.level.isClientSide) {
                this.handleInteraction(pVector, slot -> {
                    switch (slot) {
                        case HEAD: {
                            this.helmetJiggle = 15;
                            break;
                        }
                        case CHEST: {
                            this.chestJiggle = 15;
                            break;
                        }
                        case LEGS: {
                            this.legJiggle = 15;
                            break;
                        }
                        case FEET: {
                            this.bootJiggle = 15;
                        }
                    }
                });
            } else {
                AtomicReference<SoundEvent> sound = new AtomicReference<SoundEvent>(SoundEvents.ARMOR_STAND_HIT);
                this.handleInteraction(pVector, slot -> {
                    Item patt0$temp;
                    if (this.hasItemInSlot((EquipmentSlot)slot) && (patt0$temp = this.getItemBySlot((EquipmentSlot)slot).getItem()) instanceof ArmorItem) {
                        ArmorItem armorItem = (ArmorItem)patt0$temp;
                        sound.set((SoundEvent)((ArmorMaterial)armorItem.getMaterial().value()).equipSound().value());
                    }
                    if (pPlayer.isCreative() && pPlayer.isCrouching()) {
                        ArmorItem armorItem;
                        Item patt1$temp;
                        ItemStack equipped = this.getItemBySlot((EquipmentSlot)slot);
                        ItemStack playerHeld = pPlayer.getItemInHand(pHand);
                        if ((equipped.isEmpty() || equipped.getItem() instanceof ArmorItem) && (playerHeld.isEmpty() || (patt1$temp = playerHeld.getItem()) instanceof ArmorItem && (armorItem = (ArmorItem)patt1$temp).getEquipmentSlot().equals(slot))) {
                            pPlayer.setItemInHand(pHand, equipped);
                            this.setItemSlot((EquipmentSlot)slot, playerHeld);
                        }
                    }
                });
                this.playSound(sound.get());
                if (this.canAttack((LivingEntity)pPlayer) && this.interactionAnger++ >= 2) {
                    this.setTarget((LivingEntity)pPlayer);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactAt(pPlayer, pVector, pHand);
    }

    private void handleInteraction(Vec3 interactionVector, Consumer<EquipmentSlot> onInteract) {
        double d0 = interactionVector.y / (double)(this.getScale() * this.getAgeScale());
        if (d0 >= 0.1 && d0 < 0.55) {
            onInteract.accept(EquipmentSlot.FEET);
        } else if (d0 >= 0.9 && d0 < 1.6) {
            onInteract.accept(EquipmentSlot.CHEST);
        } else if (d0 >= 0.4 && d0 < 1.2000000000000002) {
            onInteract.accept(EquipmentSlot.LEGS);
        } else if (d0 >= 1.6) {
            onInteract.accept(EquipmentSlot.HEAD);
        }
    }

    @Override
    public boolean shouldBeExtraAnimated() {
        return !this.isArmorStandFrozen();
    }

    public CursedArmorStandEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 0;
        this.lookControl = this.createLookControl();
        this.moveControl = this.createMoveControl();
    }

    @Override
    protected LookControl createLookControl() {
        return new LookControl((Mob)this){

            protected float rotateTowards(float pFrom, float pTo, float pMaxDelta) {
                return super.rotateTowards(pFrom, pTo, pMaxDelta * 2.5f);
            }

            protected boolean resetXRotOnTick() {
                return CursedArmorStandEntity.this.getTarget() == null;
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

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.helmetJiggle > 0) {
                --this.helmetJiggle;
            }
            if (this.chestJiggle > 0) {
                --this.chestJiggle;
            }
            if (this.legJiggle > 0) {
                --this.legJiggle;
            }
            if (this.bootJiggle > 0) {
                --this.bootJiggle;
            }
        }
    }

    protected void playHurtSound(DamageSource pSource) {
        Item item;
        ItemStack chestplate = this.getItemBySlot(EquipmentSlot.CHEST);
        if (!chestplate.isEmpty() && (item = chestplate.getItem()) instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem)item;
            this.playSound((SoundEvent)((ArmorMaterial)armorItem.getMaterial().value()).equipSound().value(), this.getSoundVolume(), this.getVoicePitch());
        }
        super.playHurtSound(pSource);
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ARMOR_STAND_HIT;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("originalYRot", this.originalYRot);
        pCompound.putBoolean("armorStandFrozen", this.isArmorStandFrozen());
        if (this.spawn != null) {
            pCompound.put("spawnPos", (Tag)NBT.writeVec3Pos(this.spawn));
        }
        pCompound.putString("armorStandPose", this.getArmorstandPose().name());
        this.addPersistentAngerSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.originalYRot = pCompound.getFloat("originalYRot");
        if (pCompound.contains("spawnPos", 10)) {
            this.spawn = NBT.readVec3(pCompound.getCompound("spawnPos"));
        }
        this.setArmorStandFrozen(pCompound.getBoolean("armorStandFrozen"));
        String pose = pCompound.getString("armorStandPose");
        try {
            this.setArmorstandPose(Pose.valueOf(pose));
        }
        catch (Exception ignored) {
            IronsSpellbooks.LOGGER.warn("Entity {} attempting to load invalid pose: {}", (Object)this, (Object)pose);
            this.setArmorstandPose(Pose.DEFAULT);
        }
        this.readPersistentAngerSaveData(this.level, pCompound);
    }

    @Override
    protected void customServerAiStep() {
        Level level;
        super.customServerAiStep();
        if (this.spawn == null) {
            this.spawn = this.position();
        }
        if ((level = this.level) instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.updatePersistentAnger(serverLevel, true);
        }
        if (this.interactionAnger > 0 && this.tickCount % 30 == 0) {
            --this.interactionAnger;
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(3, (Goal)new ArmorStandAttackGoal(this, 1.0, 50, 75).setMoveset(List.of(new AttackAnimationData(10, "simple_sword_horizontal_cross_swipe", 8), new AttackAnimationData(20, "simple_sword_downstrike", 16))).setComboChance(0.2f).setMeleeAttackInverval(10, 30).setMeleeMovespeedModifier(1.5f).setMeleeBias(1.0f, 1.0f).setSpells(List.of(), List.of(), List.of(), List.of()));
        this.goalSelector.addGoal(5, (Goal)new ArmorStandReturnToHomeGoal(this, 1.0));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, arg_0 -> ((CursedArmorStandEntity)this).isAngryAt(arg_0)));
        this.targetSelector.addGoal(5, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
    }

    public boolean isPushable() {
        return false;
    }

    protected void pushEntities() {
    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.isArmorStandFrozen();
    }

    public void setTarget(@Nullable LivingEntity pTarget) {
        if (pTarget != null) {
            this.setArmorStandFrozen(false);
        }
        super.setTarget(pTarget);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        this.setArmorStandFrozen(false);
        return super.hurt(pSource, pAmount);
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData) {
        if (pReason.equals((Object)MobSpawnType.STRUCTURE)) {
            this.originalYRot = this.getYRot();
            this.spawn = null;
            this.setPersistenceRequired();
        }
        super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
        this.setLeftHanded(false);
        return pSpawnData;
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)ItemRegistry.CULTIST_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)ItemRegistry.CULTIST_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)ItemRegistry.MISERY.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0f);
        this.setDropChance(EquipmentSlot.CHEST, 0.0f);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 24.0).add(Attributes.ENTITY_INTERACTION_RANGE, 3.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public void playAnimation(String animationId) {
        try {
            this.animationToPlay = RawAnimation.begin().thenPlay(animationId);
        }
        catch (Exception ignored) {
            IronsSpellbooks.LOGGER.error("Entity {} Failed to play animation: {}", (Object)this, (Object)animationId);
        }
    }

    private PlayState predicate(AnimationState<CursedArmorStandEntity> animationEvent) {
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

    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = pRemainingPersistentAngerTime;
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {
        this.persistentAngerTarget = pPersistentAngerTarget;
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public static enum Pose {
        DEFAULT,
        KNEELING,
        HEROIC,
        STOIC;

    }
}

