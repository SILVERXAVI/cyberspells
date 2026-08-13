/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.FlyingMoveControl
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoEntity
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package io.redspace.ironsspellbooks.entity.spells.wisp;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.goals.WispAttackGoal;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.holy.WispSpell;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Collections;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WispEntity
extends PathfinderMob
implements GeoEntity {
    @javax.annotation.Nullable
    private UUID ownerUUID;
    @javax.annotation.Nullable
    private Entity cachedOwner;
    private final RawAnimation animation = RawAnimation.begin().thenPlay("animation.wisp.flying");
    private Vec3 targetSearchStart;
    private Vec3 lastTickPos;
    private float damageAmount;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);

    public WispEntity(EntityType<? extends WispEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public WispEntity(Level levelIn, LivingEntity owner, float damageAmount) {
        this((EntityType<? extends WispEntity>)((EntityType)EntityRegistry.WISP.get()), levelIn);
        this.moveControl = new FlyingMoveControl((Mob)this, 20, true);
        this.damageAmount = damageAmount;
        this.setOwner((Entity)owner);
        float xRot = owner.getXRot();
        float yRot = owner.getYRot();
        float yHeadRot = owner.getYHeadRot();
        this.setYRot(yRot);
        this.setXRot(xRot);
        this.setYBodyRot(yRot);
        this.setYHeadRot(yHeadRot);
        this.lastTickPos = this.position();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, (Goal)new WispAttackGoal(this, 1.0));
    }

    public static boolean isValidTarget(@javax.annotation.Nullable Entity entity) {
        LivingEntity livingEntity;
        return entity instanceof LivingEntity && (livingEntity = (LivingEntity)entity).isAlive() && livingEntity instanceof Enemy;
    }

    @Nullable
    public LivingEntity getTarget() {
        return super.getTarget();
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            this.spawnParticles();
        } else {
            LivingEntity target = this.getTarget();
            if (target == null || target.isRemoved()) {
                if (this.tickCount > 10) {
                    this.popAndDie();
                }
            } else if (this.getBoundingBox().intersects(target.getBoundingBox())) {
                DamageSources.applyDamage((Entity)target, this.damageAmount, SpellRegistry.WISP_SPELL.get().getDamageSource((Entity)this, this.cachedOwner));
                this.playSound(WispSpell.getImpactSound(), 1.0f, 1.0f);
                Vec3 p = target.getEyePosition();
                MagicManager.spawnParticles(this.level, ParticleHelper.WISP, p.x, p.y, p.z, 25, 0.0, 0.0, 0.0, 0.18, true);
                this.discard();
            }
        }
        this.lastTickPos = this.position();
    }

    public void setOwner(@javax.annotation.Nullable Entity pOwner) {
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
            this.cachedOwner = pOwner;
        }
    }

    @NotNull
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, (Mob)this, pLevel){

            public boolean isStableDestination(BlockPos blockPos) {
                return !this.level.getBlockState(blockPos.below()).isAir();
            }

            public void tick() {
                super.tick();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02f, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)0.8f));
            } else if (this.isInLava()) {
                this.moveRelative(0.02f, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            } else {
                this.moveRelative(this.getSpeed(), pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)0.91f));
            }
        }
        this.calculateEntityAnimation(false);
    }

    public boolean isNoGravity() {
        return true;
    }

    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
    }

    protected void customServerAiStep() {
        if (this.cachedOwner == null || !this.cachedOwner.isAlive()) {
            this.discard();
        }
    }

    private PlayState predicate(AnimationState event) {
        event.getController().setAnimation(this.animation);
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController((GeoAnimatable)this, "controller", 0, this::predicate));
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.FLYING_SPEED, 0.2).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.level.isClientSide) {
            this.popAndDie();
        }
        return true;
    }

    private void popAndDie() {
        this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0f, 1.0f);
        ((ServerLevel)this.level).sendParticles((ParticleOptions)ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2, 0.2, 0.2, 0.0);
        this.discard();
    }

    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    public void spawnParticles() {
    }
}

