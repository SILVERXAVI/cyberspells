/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.BossEvent$BossBarColor
 *  net.minecraft.world.BossEvent$BossBarOverlay
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.FlyingMoveControl
 *  net.minecraft.world.entity.ai.control.LookControl
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.WrappedGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.animal.IronGolem
 *  net.minecraft.world.entity.monster.AbstractIllager
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.npc.Villager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.PacketDistributor
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.BossbarManager;
import io.redspace.ironsspellbooks.api.util.MusicManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingMusicHandler;
import io.redspace.ironsspellbooks.entity.mobs.goals.MomentHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.ExtendedServerBossEvent;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.NotIdioticNavigation;
import io.redspace.ironsspellbooks.network.EntityEventPacket;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class DeadKingBoss
extends AbstractSpellCastingMob
implements Enemy,
IAnimatedAttacker,
IClientEventEntity {
    public static final byte CLIENT_STOP_TRACKING = 0;
    public static final byte CLIENT_START_TRACKING = 1;
    private static final BossbarManager.BossbarSprite BOSSBAR_SPRITE = new BossbarManager.BossbarSprite(IronsSpellbooks.id("boss_bars/dead_king_bossbar"), 192, 21, 3, -2);
    private static final AttributeModifier MANA_MODIFIER = new AttributeModifier(IronsSpellbooks.id("mana"), 2000.0, AttributeModifier.Operation.ADD_VALUE);
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(DeadKingBoss.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private int transitionAnimationTime = 139;
    private boolean isCloseToGround;
    public boolean isMeleeing;
    private int destroyBlockDelay;
    private ExtendedServerBossEvent bossEvent;
    private final RawAnimation phase_transition_animation = RawAnimation.begin().thenPlay("dead_king_die");
    private final RawAnimation melee = RawAnimation.begin().thenPlay("dead_king_melee");
    private final RawAnimation slam = RawAnimation.begin().thenPlay("dead_king_slam");
    private final AnimationController<DeadKingBoss> transitionController = new AnimationController((GeoAnimatable)this, "dead_king_transition", 0, this::transitionPredicate);
    private final AnimationController<DeadKingBoss> meleeController = new AnimationController((GeoAnimatable)this, "dead_king_animations", 0, this::meleePredicate);
    RawAnimation animationToPlay = null;

    @Override
    public void handleClientEvent(byte eventId) {
        switch (eventId) {
            case 0: {
                MusicManager.stopEvent(this.getUUID());
                BossbarManager.stopTracking(this.uuid);
                break;
            }
            case 1: {
                BossbarManager.startTracking(this.uuid, BOSSBAR_SPRITE);
                MusicManager.createEvent((Entity)this, new DeadKingMusicHandler(this));
            }
        }
    }

    public DeadKingBoss(Level pLevel) {
        this((EntityType<? extends AbstractSpellCastingMob>)((EntityType)EntityRegistry.DEAD_KING.get()), pLevel);
        this.setPersistenceRequired();
    }

    public DeadKingBoss(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPersistenceRequired();
        this.xpReward = 60;
        this.lookControl = this.createLookControl();
        this.moveControl = this.createMoveControl();
        this.createBossEvent();
    }

    private DeadKingAnimatedWarlockAttackGoal getCombatGoal() {
        return (DeadKingAnimatedWarlockAttackGoal)((WarlockAttackGoal)new DeadKingAnimatedWarlockAttackGoal(this, 1.0, 55, 85).setMeleeAttackInverval(0, 20).setSpellQuality(0.3f, 0.5f).setSpells((List)List.of(SpellRegistry.RAY_OF_SIPHONING_SPELL.get(), SpellRegistry.BLOOD_SLASH_SPELL.get(), SpellRegistry.BLOOD_SLASH_SPELL.get(), SpellRegistry.WITHER_SKULL_SPELL.get(), SpellRegistry.WITHER_SKULL_SPELL.get(), SpellRegistry.WITHER_SKULL_SPELL.get(), SpellRegistry.FANG_STRIKE_SPELL.get(), SpellRegistry.FANG_STRIKE_SPELL.get(), SpellRegistry.POISON_ARROW_SPELL.get(), SpellRegistry.POISON_ARROW_SPELL.get(), SpellRegistry.BLIGHT_SPELL.get(), SpellRegistry.ACID_ORB_SPELL.get()), (List)List.of(SpellRegistry.FANG_WARD_SPELL.get(), SpellRegistry.BLOOD_STEP_SPELL.get()), List.of(), List.of())).setMeleeBias(0.8f, 0.8f).setAllowFleeing(false);
    }

    protected void registerGoals() {
        this.setFirstPhaseGoals();
        this.targetSelector.addGoal(1, (Goal)new MomentHurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
        this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
        this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, Villager.class, true));
        this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractIllager.class, true));
    }

    protected void setFirstPhaseGoals() {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals(x -> true);
        this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(1, (Goal)new DeadKingBarrageGoal(this, SpellRegistry.WITHER_SKULL_SPELL.get(), 3, 4, 70, 140, 3));
        this.goalSelector.addGoal(2, (Goal)new DeadKingBarrageGoal(this, SpellRegistry.RAISE_DEAD_SPELL.get(), 4, 4, 400, 600, 1));
        this.goalSelector.addGoal(3, (Goal)new DeadKingBarrageGoal(this, SpellRegistry.BLOOD_STEP_SPELL.get(), 1, 1, 100, 180, 1));
        this.goalSelector.addGoal(4, (Goal)this.getCombatGoal().setSingleUseSpell(SpellRegistry.RAISE_DEAD_SPELL.get(), 20, 20, 8, 8));
        this.goalSelector.addGoal(5, (Goal)new PatrolNearLocationGoal(this, 32.0f, 0.9f));
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
    }

    protected void setFinalPhaseGoals() {
        this.goalSelector.getAvailableGoals().forEach(WrappedGoal::stop);
        this.goalSelector.removeAllGoals(x -> true);
        this.goalSelector.addGoal(1, (Goal)new DeadKingBarrageGoal(this, SpellRegistry.WITHER_SKULL_SPELL.get(), 5, 5, 60, 140, 4));
        this.goalSelector.addGoal(2, (Goal)new DeadKingBarrageGoal(this, SpellRegistry.SUMMON_VEX_SPELL.get(), 2, 4, 200, 400, 1));
        this.goalSelector.addGoal(3, (Goal)new DeadKingBarrageGoal(this, SpellRegistry.BLOOD_STEP_SPELL.get(), 1, 1, 100, 180, 1));
        this.goalSelector.addGoal(4, (Goal)this.getCombatGoal().setIsFlying().setSingleUseSpell(SpellRegistry.BLAZE_STORM_SPELL.get(), 10, 30, 10, 10));
        this.goalSelector.addGoal(5, (Goal)new PatrolNearLocationGoal(this, 32.0f, 0.9f));
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.hasUsedSingleAttack = false;
        this.moveControl = new FlyingMoveControl((Mob)this, 30, true);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return (SoundEvent)SoundRegistry.DEAD_KING_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return (SoundEvent)SoundRegistry.DEAD_KING_DEATH.get();
    }

    public void handleEntityEvent(byte pId) {
        if (pId != 3) {
            super.handleEntityEvent(pId);
        }
    }

    public float getVoicePitch() {
        return 1.0f;
    }

    public boolean isPushable() {
        return !this.isPhaseTransitioning();
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        this.getAttribute((Holder)AttributeRegistry.MAX_MANA).addOrReplacePermanentModifier(MANA_MODIFIER);
        return pSpawnData;
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack((ItemLike)ItemRegistry.BLOOD_STAFF.get()));
        this.setDropChance(EquipmentSlot.OFFHAND, 0.0f);
    }

    public boolean isAlliedTo(Entity pEntity) {
        IMagicSummon summon;
        return super.isAlliedTo(pEntity) || pEntity instanceof IMagicSummon && (summon = (IMagicSummon)pEntity).getSummoner() == this;
    }

    public boolean isInvertedHealAndHarm() {
        return true;
    }

    public void tick() {
        if (this.isPhase(Phases.FinalPhase)) {
            this.setNoGravity(true);
            if (this.tickCount % 10 == 0) {
                this.isCloseToGround = Utils.raycastForBlock(this.level, this.position(), this.position().subtract(0.0, 2.5, 0.0), ClipContext.Fluid.ANY).getType() == HitResult.Type.BLOCK;
            }
            Vec3 woosh = new Vec3((double)Mth.sin((float)((float)(this.tickCount * 5) * ((float)Math.PI / 180))), ((double)Mth.cos((float)((float)(this.tickCount * 3 + 986741) * ((float)Math.PI / 180))) + (this.isCloseToGround ? 0.05 : -0.185)) * 0.5, (double)Mth.sin((float)((float)(this.tickCount * 1 + 465) * ((float)Math.PI / 180))));
            if (this.getTarget() == null) {
                woosh = woosh.scale(0.25);
            }
            this.setDeltaMovement(this.getDeltaMovement().add(woosh.scale((double)0.0085f)));
            if (this.isAggressive() && this.getTarget() != null && this.distanceToSqr((Entity)this.getTarget()) > 16.0) {
                this.setDeltaMovement(this.getDeltaMovement().add(this.getForward().scale(0.02)));
            }
        }
        super.tick();
        if (this.level.isClientSide) {
            if (this.isPhase(Phases.FinalPhase) && !this.isInvisible()) {
                float radius = 0.35f;
                for (int i = 0; i < 5; ++i) {
                    Vec3 random = this.position().add(new Vec3((double)((this.random.nextFloat() * 2.0f - 1.0f) * radius), (double)(1.0f + (this.random.nextFloat() * 2.0f - 1.0f) * radius), (double)((this.random.nextFloat() * 2.0f - 1.0f) * radius)));
                    this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, random.x, random.y, random.z, 0.0, -0.1, 0.0);
                }
            }
        } else {
            float halfHealth = this.getMaxHealth() / 2.0f;
            if (this.isPhase(Phases.FirstPhase)) {
                this.bossEvent.setProgress((this.getHealth() - halfHealth) / (this.getMaxHealth() - halfHealth));
                if (this.getHealth() <= halfHealth) {
                    this.setPhase(Phases.Transitioning);
                    if (!this.isDeadOrDying()) {
                        this.setHealth(halfHealth);
                    }
                    this.playSound((SoundEvent)SoundRegistry.DEAD_KING_FAKE_DEATH.get());
                    this.setInvulnerable(true);
                    this.getCombatGoal().stop();
                    this.cancelCast();
                }
            } else if (this.isPhase(Phases.Transitioning)) {
                if (--this.transitionAnimationTime <= 0) {
                    this.setPhase(Phases.FinalPhase);
                    MagicManager.spawnParticles(this.level, ParticleHelper.FIRE, this.position().x, this.position().y + 2.5, this.position().z, 80, 0.2, 0.2, 0.2, 0.25, true);
                    this.setFinalPhaseGoals();
                    this.setNoGravity(true);
                    this.playSound((SoundEvent)SoundRegistry.DEAD_KING_EXPLODE.get());
                    this.level.getEntities((Entity)this, this.getBoundingBox().inflate(5.0), entity -> entity instanceof LivingEntity && entity.isPickable() && entity.distanceToSqr(this.position()) < 25.0).forEach(x$0 -> super.doHurtTarget(x$0));
                    this.setInvulnerable(false);
                }
            } else if (this.isPhase(Phases.FinalPhase)) {
                this.bossEvent.setProgress(this.getHealth() / (this.getMaxHealth() - halfHealth));
            }
        }
        if (this.destroyBlockDelay > 0) {
            --this.destroyBlockDelay;
        }
    }

    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    public boolean isPhase(Phases phase) {
        return phase.value == this.getPhase();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity entity;
        if (pSource == this.level.damageSources().lava()) {
            return false;
        }
        if (pSource.is(DamageTypes.IN_WALL) && this.destroyBlockDelay <= 0) {
            Utils.doMobBreakSuffocatingBlocks((LivingEntity)this);
            this.destroyBlockDelay = 40;
        }
        if ((entity = pSource.getEntity()) != null) {
            float distance = entity.distanceTo((Entity)this);
            float damageReduction = Mth.clampedLerp((float)1.0f, (float)0.5f, (float)((distance - 8.0f) / 16.0f));
            pAmount *= damageReduction;
        }
        return super.hurt(pSource, pAmount);
    }

    protected boolean isImmobile() {
        return this.isPhase(Phases.Transitioning) || super.isImmobile();
    }

    public boolean isPhaseTransitioning() {
        return this.isPhase(Phases.Transitioning);
    }

    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);
        this.bossEvent.addPlayer(pPlayer);
        PacketDistributor.sendToPlayer((ServerPlayer)pPlayer, new EntityEventPacket((Entity)this, 1), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        this.bossEvent.removePlayer(pPlayer);
        PacketDistributor.sendToPlayer((ServerPlayer)pPlayer, new EntityEventPacket((Entity)this, 0), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 10.0).add(AttributeRegistry.SPELL_POWER, 1.15).add(Attributes.ARMOR, 15.0).add(AttributeRegistry.SPELL_RESIST, 1.0).add(Attributes.MAX_HEALTH, 500.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.8).add(Attributes.ATTACK_KNOCKBACK, 0.6).add(Attributes.ENTITY_INTERACTION_RANGE, 4.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.FLYING_SPEED, 0.155).add(Attributes.MOVEMENT_SPEED, 0.155);
    }

    public void setCustomName(@Nullable Component pName) {
        super.setCustomName(pName);
        this.bossEvent.setName(this.getDisplayName());
    }

    private void setPhase(int phase) {
        this.entityData.set(PHASE, (Object)phase);
    }

    private void setPhase(Phases phase) {
        this.setPhase(phase.value);
    }

    public int getPhase() {
        return (Integer)this.entityData.get(PHASE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("phase", this.getPhase());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
        this.setPhase(pCompound.getInt("phase"));
        if (this.isPhase(Phases.FinalPhase)) {
            this.setFinalPhaseGoals();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(PHASE, (Object)0);
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

    private PlayState meleePredicate(AnimationState<DeadKingBoss> animationEvent) {
        AnimationController controller = animationEvent.getController();
        if (this.animationToPlay != null) {
            controller.forceAnimationReset();
            controller.setAnimation(this.animationToPlay);
            this.animationToPlay = null;
        }
        return this.transitionController.getAnimationState() == AnimationController.State.STOPPED ? PlayState.CONTINUE : PlayState.STOP;
    }

    private PlayState transitionPredicate(AnimationState animationEvent) {
        AnimationController controller = animationEvent.getController();
        if (this.isPhaseTransitioning()) {
            controller.setAnimation(this.phase_transition_animation);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.transitionController);
        controllerRegistrar.add(this.meleeController);
        super.registerControllers(controllerRegistrar);
    }

    @Override
    public boolean shouldAlwaysAnimateHead() {
        return !this.isPhaseTransitioning();
    }

    @Override
    public boolean bobBodyWhileWalking() {
        return this.isPhase(Phases.FirstPhase);
    }

    @Override
    public boolean isAnimating() {
        return this.transitionController.getAnimationState() != AnimationController.State.STOPPED || this.meleeController.getAnimationState() != AnimationController.State.STOPPED || super.isAnimating();
    }

    public boolean doHurtTarget(Entity pEntity) {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)SoundRegistry.DEAD_KING_HIT.get(), SoundSource.HOSTILE, 1.0f, 1.0f);
        return super.doHurtTarget(pEntity);
    }

    @Override
    public boolean shouldAlwaysAnimateLegs() {
        return this.isPhase(Phases.FirstPhase);
    }

    @Override
    protected LookControl createLookControl() {
        return new LookControl((Mob)this){

            protected float rotateTowards(float pFrom, float pTo, float pMaxDelta) {
                return super.rotateTowards(pFrom, pTo, pMaxDelta * 2.5f);
            }

            protected boolean resetXRotOnTick() {
                return !DeadKingBoss.this.isCasting();
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

    public void load(CompoundTag compound) {
        super.load(compound);
        if (!this.level.isClientSide) {
            this.createBossEvent();
        }
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new NotIdioticNavigation((Mob)this, pLevel);
    }

    protected void createBossEvent() {
        this.bossEvent = (ExtendedServerBossEvent)new ExtendedServerBossEvent(this.getUUID(), this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true).setCreateWorldFog(true);
    }

    private class DeadKingBarrageGoal
    extends SpellBarrageGoal {
        public DeadKingBarrageGoal(IMagicEntity abstractSpellCastingMob, AbstractSpell spell, int minLevel, int maxLevel, int pAttackIntervalMin, int pAttackIntervalMax, int projectileCount) {
            super(abstractSpellCastingMob, spell, minLevel, maxLevel, pAttackIntervalMin, pAttackIntervalMax, projectileCount);
        }

        @Override
        public boolean canUse() {
            return !DeadKingBoss.this.isMeleeing && super.canUse();
        }
    }

    public static enum Phases {
        FirstPhase(0),
        Transitioning(1),
        FinalPhase(2);

        final int value;

        private Phases(int value) {
            this.value = value;
        }
    }

    public static enum AttackType {
        DOUBLE_SWING(51, "dead_king_double_swing", 16, 36),
        SLAM(48, "dead_king_slam", 30);

        public final AttackAnimationData data;

        private AttackType(int lengthInTicks, String animationId, int ... attackTimestamps) {
            this.data = new AttackAnimationData(lengthInTicks, animationId, attackTimestamps);
        }
    }
}

