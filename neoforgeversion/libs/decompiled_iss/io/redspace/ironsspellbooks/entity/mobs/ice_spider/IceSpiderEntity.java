/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$MoveFunction
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.BodyRotationControl
 *  net.minecraft.world.entity.ai.control.LookControl
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.animal.IronGolem
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.npc.AbstractVillager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.raid.Raider
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.PartEntity
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Quaternionf
 *  org.joml.Vector3f
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 */
package io.redspace.ironsspellbooks.entity.mobs.ice_spider;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.MomentHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderNavigation;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderPartEntity;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.LeapBackGoal;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.PounceGrappleGoal;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombEntity;
import io.redspace.ironsspellbooks.entity.spells.root.PreventDismount;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class IceSpiderEntity
extends AbstractSpellCastingMob
implements Enemy,
IAnimatedAttacker,
PreventDismount {
    private static final EntityDataAccessor<Boolean> DATA_IS_CLIMBING = SynchedEntityData.defineId(IceSpiderEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_CROUCHING = SynchedEntityData.defineId(IceSpiderEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_GRAPPLE_UUID = SynchedEntityData.defineId(IceSpiderEntity.class, (EntityDataSerializer)EntityDataSerializers.OPTIONAL_UUID);
    private static final AttributeModifier CROUCH_SPEED_MODIFIER = new AttributeModifier(IronsSpellbooks.id("crouching"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final Vec3 TORSO_OFFSET = new Vec3(0.0, 18.0, 0.0);
    private static final int EMERGE_TIME = 45;
    public final Vec3[] cornerPins = new Vec3[]{Vec3.ZERO, Vec3.ZERO, Vec3.ZERO, Vec3.ZERO};
    public Vec3 normal = Vec3.ZERO;
    public Vec3 lastNormal = Vec3.ZERO;
    private int emergeTick;
    int crouchTick;
    public boolean wantsToLeapBack;
    public boolean wantsToCastSpells;
    IceSpiderPartEntity[] subEntities;
    IceSpiderAttackGoal attackGoal;
    @javax.annotation.Nullable
    int grappleTime;
    @javax.annotation.Nullable
    Entity cachedGrappleTarget = null;
    RawAnimation animationToPlay = null;
    private final AnimationController<IceSpiderEntity> meleeController = new AnimationController((GeoAnimatable)this, "melee_animations", 0, this::predicate);

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        float y;
        super.recreateFromPacket(packet);
        this.yRotO = y = this.getYRot();
        this.yBodyRot = y;
        this.yBodyRotO = y;
        this.yHeadRot = y;
        this.yHeadRotO = y;
    }

    public IceSpiderEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
        this.subEntities = new IceSpiderPartEntity[]{new IceSpiderPartEntity(this, TORSO_OFFSET.add(0.0, 0.0, 16.0), 1.2f, 0.8f), new IceSpiderPartEntity(this, TORSO_OFFSET, 0.75f, 0.75f), new IceSpiderPartEntity(this, TORSO_OFFSET.add(0.0, 0.0, -20.0), 1.75f, 1.5f)};
        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntities.length + 1) + 1);
        this.moveControl = this.createMoveControl();
    }

    public IceSpiderEntity(Level level) {
        this((EntityType<? extends PathfinderMob>)((EntityType)EntityRegistry.ICE_SPIDER.get()), level);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.MAX_HEALTH, 50.0).add(Attributes.ARMOR, 20.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.6).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ENTITY_INTERACTION_RANGE, 4.0).add(Attributes.STEP_HEIGHT, 1.5).add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    public void tick() {
        super.tick();
        float scalar = this.getScale() * 4.0f;
        Vec3 worldpos = this.position();
        for (int x = 0; x < 2; ++x) {
            for (int y = 0; y < 2; ++y) {
                Vec3 vec = this.rotateWithBody(new Vec3(((double)x - 0.5) * (double)scalar, 0.0, ((double)y - 0.5) * (double)scalar));
                int maxStep = 2;
                int climbOffset = this.isClimbing() ? 4 * Mth.sign((double)((double)y - 0.5)) : 0;
                this.cornerPins[x * 2 + y] = Utils.moveToRelativeGroundLevel(this.level, worldpos.add(vec), maxStep + climbOffset, maxStep - climbOffset).subtract(worldpos);
            }
        }
        Vec3[] vx = this.cornerPins;
        Vec3 n0 = vx[1].subtract(vx[0]).cross(vx[2].subtract(vx[0]));
        Vec3 n1 = vx[3].subtract(vx[1]).cross(vx[0].subtract(vx[1]));
        Vec3 n2 = vx[0].subtract(vx[2]).cross(vx[3].subtract(vx[2]));
        Vec3 n3 = vx[2].subtract(vx[3]).cross(vx[1].subtract(vx[3]));
        Vec3 targetNormal = n0.add(n1).add(n2).add(n3).normalize();
        this.lastNormal = this.normal;
        this.normal = Utils.lerp(0.2f, this.normal, targetNormal);
        Quaternionf quat = Utils.rotationBetweenVectors(new Vector3f(0.0f, 1.0f, 0.0f), Utils.v3f(this.normal));
        for (IceSpiderPartEntity part : this.subEntities) {
            part.positionSelf(quat);
        }
        if (this.emergeTick > 0) {
            --this.emergeTick;
            if (!this.level.isClientSide) {
                if (this.emergeTick == 0) {
                    this.setPose(Pose.STANDING);
                }
            } else {
                this.updateWalkAnimation((float)this.emergeTick / 45.0f);
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.tickGrapple();
        this.handleCrouchStatus();
        this.handleClimbingStatus();
    }

    private void handleCrouchStatus() {
        AABB projection;
        if (this.level.isClientSide) {
            return;
        }
        if (this.isCrouching()) {
            AABB projection2 = this.getDefaultDimensions(Pose.STANDING).makeBoundingBox(this.position());
            if (this.level.noCollision((Entity)this, projection2.deflate(1.0E-7))) {
                this.stopCrouching();
            }
        } else if (this.horizontalCollision && this.level.noCollision((Entity)this, (projection = this.getDefaultDimensions(Pose.CROUCHING).makeBoundingBox(this.position().add(this.getForward().scale(0.15)))).deflate(1.0E-7))) {
            this.startCrouching();
        }
    }

    private void handleClimbingStatus() {
        if (this.level.isClientSide || this.isCrouching()) {
            return;
        }
        if (this.verticalCollision && !this.verticalCollisionBelow) {
            AABB leftprojection = this.getBoundingBox().deflate(0.2).move(this.getForward().scale(0.5).yRot(-1.5707964f));
            boolean strafeLeft = this.level.noCollision((Entity)this, leftprojection);
            this.getMoveControl().strafe(0.0f, strafeLeft ? 1.0f : -1.0f);
            return;
        }
        if (this.isClimbing()) {
            if (!this.horizontalCollision) {
                this.setIsClimbing(false);
            }
        } else if (this.horizontalCollision) {
            float deflate = 0.75f;
            AABB projection = this.getBoundingBox().deflate((double)deflate).move(this.getForward().scale(0.25 + (double)(deflate / 2.0f)));
            if (!this.level.noCollision((Entity)this, projection)) {
                this.setIsClimbing(true);
            }
        }
    }

    public void setEmergeFromGround() {
        if (!this.level.isClientSide) {
            this.setPose(Pose.EMERGING);
            this.emergeTick = 45;
        }
    }

    public void setIsClimbing(boolean climbing) {
        this.entityData.set(DATA_IS_CLIMBING, (Object)climbing);
    }

    public boolean isClimbing() {
        return (Boolean)this.entityData.get(DATA_IS_CLIMBING);
    }

    public void setIsCrouching(boolean climbing) {
        this.entityData.set(DATA_IS_CROUCHING, (Object)climbing);
    }

    public boolean isCrouching() {
        return (Boolean)this.entityData.get(DATA_IS_CROUCHING);
    }

    public Vec3 getDeltaMovement() {
        return this.isClimbing() ? super.getDeltaMovement().multiply(1.0, 0.0, 1.0).add(0.0, (double)0.275f, 0.0) : super.getDeltaMovement();
    }

    public float getCrouchHeightMultiplier(float partialTick) {
        return Mth.lerp((float)this.crouchTweenPercent(partialTick), (float)0.5f, (float)1.0f);
    }

    public void startCrouching() {
        this.setPose(Pose.CROUCHING);
        this.getAttribute(Attributes.MOVEMENT_SPEED).addOrUpdateTransientModifier(CROUCH_SPEED_MODIFIER);
        this.setIsCrouching(true);
    }

    public void stopCrouching() {
        this.setPose(Pose.STANDING);
        this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(CROUCH_SPEED_MODIFIER);
        this.setIsCrouching(false);
    }

    public float getCrouchHeightMultiplier() {
        return this.isCrouching() ? 0.5f : 1.0f;
    }

    @Override
    public void castComplete() {
        super.castComplete();
        this.wantsToCastSpells = false;
    }

    @Override
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        if (!this.wantsToCastSpells) {
            return;
        }
        if (spell.getCastType() == CastType.INSTANT) {
            this.serverTriggerAnimation("attack_fang_basic");
        } else {
            this.serverTriggerAnimation("long_cast");
        }
        super.initiateCastSpell(spell, spellLevel);
    }

    public float maxUpStep() {
        return Math.max(1.0f, super.maxUpStep() * this.getScale());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_CLIMBING, (Object)false);
        pBuilder.define(DATA_IS_CROUCHING, (Object)false);
        pBuilder.define(DATA_GRAPPLE_UUID, Optional.empty());
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

    protected PathNavigation createNavigation(Level level) {
        return new IceSpiderNavigation((Mob)this, level);
    }

    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, (Goal)new LeapBackGoal(this));
        this.goalSelector.addGoal(1, (Goal)new PounceGrappleGoal(this));
        this.attackGoal = (IceSpiderAttackGoal)((WarlockAttackGoal)new IceSpiderAttackGoal(this, 1.1, 0, 40).setMoveset(List.of(new AttackAnimationData.Builder("attack_bite").length(22).attacks(new AttackKeyframe(14, new Vec3(0.0, 0.0, 1.0))).build(), new AttackAnimationData.Builder("attack_fang_basic").length(20).attacks(new AttackKeyframe(12, new Vec3(0.0, 0.0, 1.0))).build(), new AttackAnimationData.Builder("attack_right_swipe").length(14).attacks(new AttackKeyframe(10, new Vec3(0.0, 0.1, -1.0), new Vec3(0.0, 0.0, 1.0))).build())).setMeleeBias(1.0f, 1.0f).setSpells((List)List.of(SpellRegistry.SNOWBALL_SPELL.get(), SpellRegistry.ICE_SPIKES_SPELL.get()), List.of(), List.of(), List.of())).setSpellQuality(0.75f, 0.75f);
        this.goalSelector.addGoal(2, (Goal)this.attackGoal);
        this.goalSelector.addGoal(3, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 32.0f, 0.08f));
        this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.7));
        this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
        this.targetSelector.addGoal(1, (Goal)new MomentHurtByTargetGoal(this, IceSpiderEntity.class));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, LivingEntity.class, true, livingEntity -> livingEntity instanceof Player || livingEntity instanceof IronGolem));
        this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, LivingEntity.class, true, livingEntity -> livingEntity instanceof Animal || livingEntity instanceof AbstractVillager || livingEntity instanceof Raider));
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return (SoundEvent)SoundRegistry.ICE_SPIDER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return (SoundEvent)SoundRegistry.ICE_SPIDER_DEATH.get();
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return (SoundEvent)SoundRegistry.ICE_SPIDER_AMBIENT.get();
    }

    @Override
    protected LookControl createLookControl() {
        return super.createLookControl();
    }

    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl((Mob)this){

            public void rotateHeadTowardsFront() {
                float rot = this.mob.yBodyRot;
                super.rotateHeadTowardsFront();
                if (rot != this.mob.yBodyRot) {
                    IceSpiderEntity.this.updateWalkAnimation(1.0f);
                }
            }
        };
    }

    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }
    }

    public float crouchTweenPercent(float partialTick) {
        float tick = (float)this.tickCount + partialTick - (float)this.crouchTick;
        float tweenTime = 10.0f;
        float f = tick > tweenTime ? 1.0f : tick / tweenTime;
        if (this.isCrouching()) {
            f = 1.0f - f;
        }
        return f;
    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.getPose().equals((Object)Pose.EMERGING);
    }

    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15f, 1.0f);
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || this.getPose().equals((Object)Pose.EMERGING);
    }

    public boolean hurt(IceSpiderPartEntity bodypart, DamageSource source, float amount) {
        return this.hurt(source, amount);
    }

    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (damageSource.getEntity() != null && damageSource.getEntity().getUUID().equals(this.getGrappleTargetUUID())) {
            damageAmount *= 0.2f;
        }
        if (damageSource.getEntity() instanceof IronGolem) {
            damageAmount *= 0.5f;
        }
        if (this.isAggressive() && !this.isCrouching() && !this.isGrappling() && !this.wantsToLeapBack && damageSource.isDirect()) {
            float f = Mth.lerp((float)Math.clamp((float)(damageAmount / 12.0f), (float)0.0f, (float)1.0f), (float)0.02f, (float)0.7f);
            if (this.random.nextFloat() < f) {
                this.wantsToCastSpells = true;
                this.wantsToLeapBack = true;
            }
        }
        super.actuallyHurt(damageSource, damageAmount);
    }

    public Vec3 rotateWithBody(Vec3 vec3) {
        float y = -this.yBodyRot + 1.5707964f;
        return vec3.yRot(y * ((float)Math.PI / 180));
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.subEntities.length; ++i) {
            this.subEntities[i].setId(id + i + 1);
        }
    }

    @javax.annotation.Nullable
    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    public boolean isPickable() {
        return false;
    }

    public void refreshDimensions() {
        super.refreshDimensions();
        for (IceSpiderPartEntity part : this.subEntities) {
            part.refreshDimensions();
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (pKey == DATA_IS_CROUCHING) {
            this.refreshDimensions();
            this.crouchTick = this.tickCount;
        } else if (pKey == Entity.DATA_POSE && this.getPose() == Pose.EMERGING) {
            this.playAnimation("emerge_from_ground");
            this.emergeTick = 45;
        }
    }

    protected EntityDimensions getDefaultDimensions(Pose pose) {
        EntityDimensions dimensions = super.getDefaultDimensions(pose);
        if (pose == Pose.CROUCHING) {
            dimensions = dimensions.scale(1.0f, 0.5f);
        }
        return dimensions;
    }

    @javax.annotation.Nullable
    public UUID getGrappleTargetUUID() {
        return ((Optional)this.entityData.get(DATA_GRAPPLE_UUID)).orElse(null);
    }

    public boolean isGrappling() {
        return this.getGrappleTargetUUID() != null;
    }

    public void setGrappleTargetUUID(@javax.annotation.Nullable UUID uuid) {
        this.entityData.set(DATA_GRAPPLE_UUID, Optional.ofNullable(uuid));
        if (uuid == null) {
            this.cachedGrappleTarget = null;
        }
    }

    protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
        if (passenger.getUUID().equals(this.getGrappleTargetUUID())) {
            Vec3 vec = this.position().add(this.rotateWithBody(new Vec3(0.0, 0.0, (double)(this.getScale() * 2.0f))));
            callback.accept(passenger, vec.x, vec.y, vec.z);
        } else {
            super.positionRider(passenger, callback);
        }
    }

    public boolean shouldRiderFaceForward(Player player) {
        return false;
    }

    public boolean canFreeze() {
        return false;
    }

    public void startGrapple(Entity entity) {
        if (this.getGrappleTargetUUID() == null && !entity.isPassenger() && entity.startRiding((Entity)this)) {
            this.grappleTime = 0;
            this.setGrappleTargetUUID(entity.getUUID());
        }
        this.wantsToCastSpells = false;
        this.wantsToLeapBack = false;
    }

    public void tickGrapple() {
        Entity entity;
        UUID uuid = this.getGrappleTargetUUID();
        if (uuid == null) {
            return;
        }
        Level level = this.level;
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        if (this.cachedGrappleTarget == null) {
            entity = serverLevel.getEntity(uuid);
            if (entity == null) {
                this.setGrappleTargetUUID(null);
                return;
            }
            this.cachedGrappleTarget = entity;
        }
        if (this.cachedGrappleTarget.isRemoved()) {
            this.stopGrappling();
            return;
        }
        if (this.grappleTime % 20 == 0) {
            this.heal(this.getMaxHealth() * 0.08f);
        }
        if (this.grappleTime++ > 40) {
            entity = this.cachedGrappleTarget;
            this.stopGrappling();
            this.entomb(entity);
        } else {
            this.cachedGrappleTarget.setTicksFrozen(Math.min(this.cachedGrappleTarget.getTicksRequiredToFreeze() * 3, this.cachedGrappleTarget.getTicksFrozen() + 10));
            this.yHeadRot = this.yBodyRot;
        }
    }

    public void stopGrappling() {
        if (this.cachedGrappleTarget != null && this.isPassengerOfSameVehicle(this.cachedGrappleTarget)) {
            this.cachedGrappleTarget.stopRiding();
        }
        this.cachedGrappleTarget = null;
        this.setGrappleTargetUUID(null);
    }

    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (passenger.getUUID().equals(this.getGrappleTargetUUID())) {
            this.stopGrappling();
        }
    }

    @Override
    public boolean canEntityDismount(Entity entity) {
        return !entity.getUUID().equals(this.getGrappleTargetUUID());
    }

    public IceTombEntity entomb(Entity entity) {
        IceTombEntity iceTombEntity = new IceTombEntity(this.level, (Entity)this);
        iceTombEntity.moveTo(entity.position());
        iceTombEntity.setDeltaMovement(entity.getDeltaMovement().add(this.getForward().add(0.0, 1.0, 0.0).scale(0.5)));
        iceTombEntity.setEvil();
        iceTombEntity.setLifetime(100);
        this.level.addFreshEntity((Entity)iceTombEntity);
        entity.startRiding((Entity)iceTombEntity, true);
        this.playSound((SoundEvent)SoundRegistry.ICE_SPIDER_GRAPPLE_SPIT.get());
        return iceTombEntity;
    }

    @javax.annotation.Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity != null && entity.getUUID().equals(this.getGrappleTargetUUID())) {
            return null;
        }
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

    public boolean hasIndirectPassenger(Entity pEntity) {
        return pEntity.getUUID().equals(this.getGrappleTargetUUID());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.getGrappleTargetUUID() != null) {
            pCompound.putInt("grappleTime", this.grappleTime);
            pCompound.putUUID("grappleTarget", this.getGrappleTargetUUID());
        }
        pCompound.putBoolean("crouching", this.isCrouching());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.hasUUID("grappleTarget")) {
            this.setGrappleTargetUUID(pCompound.getUUID("grappleTarget"));
            this.grappleTime = pCompound.getInt("grappleTime");
        }
        if (pCompound.getBoolean("crouching")) {
            this.startCrouching();
        }
    }

    @Override
    public void playAnimation(String animationId) {
        this.animationToPlay = RawAnimation.begin().thenPlay(animationId);
    }

    private PlayState predicate(AnimationState<IceSpiderEntity> animationEvent) {
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
    }

    @Override
    public boolean isAnimating() {
        return this.meleeController.getAnimationState() == AnimationController.State.RUNNING;
    }
}

