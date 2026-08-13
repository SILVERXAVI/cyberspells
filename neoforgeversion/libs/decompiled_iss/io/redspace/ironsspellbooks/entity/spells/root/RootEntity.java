/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$MoveFunction
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.fluids.FluidType
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoEntity
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package io.redspace.ironsspellbooks.entity.spells.root;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.root.PreventDismount;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RootEntity
extends LivingEntity
implements GeoEntity,
PreventDismount,
AntiMagicSusceptible {
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private int duration;
    private boolean playSound = true;
    private LivingEntity target;
    private boolean played = false;
    private final RawAnimation ANIMATION = RawAnimation.begin().thenPlay("emerge");
    private final AnimationController controller = new AnimationController((GeoAnimatable)this, "root_controller", 0, this::animationPredicate);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);

    public float getScale() {
        return this.target == null ? 1.0f : this.target.getScale();
    }

    public RootEntity(EntityType<? extends RootEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RootEntity(Level level, LivingEntity owner) {
        this((EntityType<? extends RootEntity>)((EntityType)EntityRegistry.ROOT.get()), level);
        this.setOwner(owner);
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public boolean canCollideWith(@NotNull Entity pEntity) {
        return false;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    protected void doPush(@NotNull Entity pEntity) {
    }

    public void push(@NotNull Entity pEntity) {
    }

    protected void pushEntities() {
    }

    public boolean dismountsUnderwater() {
        return false;
    }

    public boolean shouldRiderSit() {
        return false;
    }

    public Vec3 getPassengerRidingPosition(Entity pEntity) {
        return this.position();
    }

    public boolean shouldRiderFaceForward(@NotNull Player player) {
        return false;
    }

    protected EntityDimensions getDefaultDimensions(Pose pPose) {
        Entity rooted = this.getFirstPassenger();
        if (rooted != null) {
            return EntityDimensions.fixed((float)(rooted.getBbWidth() * 1.25f), (float)0.75f);
        }
        return super.getDefaultDimensions(pPose);
    }

    public void tick() {
        super.tick();
        if (this.playSound) {
            this.refreshDimensions();
            this.playSound((SoundEvent)SoundRegistry.ROOT_EMERGE.get(), 2.0f, 1.0f);
            this.playSound = false;
        }
        if (!this.level().isClientSide) {
            if (this.tickCount > this.duration || this.target != null && this.target.isDeadOrDying() || !this.isVehicle()) {
                this.removeRoot();
            }
        } else if (this.tickCount < 20) {
            this.clientDiggingParticles(this);
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

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Nullable
    public LivingEntity getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel && (entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID)) instanceof LivingEntity) {
            this.owner = (LivingEntity)entity;
        }
        return this.owner;
    }

    public void removeRoot() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 5; ++i) {
                this.level().addParticle(ParticleHelper.ROOT_FOG, this.getX() + Utils.getRandomScaled(0.1f), this.getY() + Utils.getRandomScaled(0.1f), this.getZ() + Utils.getRandomScaled(0.1f), Utils.getRandomScaled(2.0), (double)(-this.random.nextFloat() * 0.5f), Utils.getRandomScaled(2.0));
            }
        }
        this.ejectPassengers();
        this.discard();
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Age", this.tickCount);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
        pCompound.putInt("Duration", this.duration);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.tickCount = pCompound.getInt("Age");
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
        this.duration = pCompound.getInt("Duration");
    }

    public boolean hasIndirectPassenger(Entity pEntity) {
        return true;
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.removeRoot();
    }

    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean isDamageSourceBlocked(DamageSource pDamageSource) {
        return true;
    }

    public boolean showVehicleHealth() {
        return false;
    }

    public void knockback(double pStrength, double pX, double pZ) {
    }

    public void positionRider(Entity passenger, Entity.MoveFunction p_19958_) {
        int x = (int)(this.getX() - passenger.getX());
        int y = (int)(this.getY() - passenger.getY());
        int z = (int)(this.getZ() - passenger.getZ());
        if ((x *= x) + (y *= y) + (z *= z) > 25) {
            this.removeRoot();
        } else {
            passenger.setPos(this.getX(), this.getY(), this.getZ());
        }
    }

    protected boolean isImmobile() {
        return true;
    }

    public boolean isAffectedByPotions() {
        return false;
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.removeRoot();
            return true;
        }
        return false;
    }

    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    private PlayState animationPredicate(AnimationState event) {
        AnimationController controller = event.getController();
        if (!this.played && controller.getAnimationState() == AnimationController.State.STOPPED) {
            controller.forceAnimationReset();
            controller.setAnimation(this.ANIMATION);
            this.played = true;
        }
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.controller);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

