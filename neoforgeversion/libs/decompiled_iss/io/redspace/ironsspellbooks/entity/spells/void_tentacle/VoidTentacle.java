/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
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
package io.redspace.ironsspellbooks.entity.spells.void_tentacle;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class VoidTentacle
extends LivingEntity
implements GeoEntity,
AntiMagicSusceptible {
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private float damage;
    private int age;
    private final RawAnimation ANIMATION_RISE = RawAnimation.begin().thenPlay("rise");
    private final RawAnimation ANIMATION_RETREAT = RawAnimation.begin().thenPlay("retreat");
    private final RawAnimation ANIMATION_FLAIL = RawAnimation.begin().thenPlay("flail");
    private final RawAnimation ANIMATION_FLAIL2 = RawAnimation.begin().thenPlay("flail2");
    private final RawAnimation ANIMATION_FLAIL3 = RawAnimation.begin().thenPlay("flail3");
    private final RawAnimation ANIMATION_IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimationController controller = new AnimationController((GeoAnimatable)this, "void_tentacle_controller", 20, this::animationPredicate);
    private final AnimationController riseController = new AnimationController((GeoAnimatable)this, "void_tentacle_rise_controller", 0, this::risePredicate);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);

    public VoidTentacle(EntityType<? extends VoidTentacle> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public VoidTentacle(Level level, LivingEntity owner, float damage) {
        this((EntityType<? extends VoidTentacle>)((EntityType)EntityRegistry.SCULK_TENTACLE.get()), level);
        this.setOwner(owner);
        this.setDamage(damage);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public boolean isPushedByFluid() {
        return false;
    }

    public boolean isOnFire() {
        return false;
    }

    public void tick() {
        if (!this.level.isClientSide) {
            if (this.age > 300) {
                this.discard();
            } else if (this.age < 280 && this.age % 20 == 0) {
                this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5)).forEach(this::dealDamage);
                if (Utils.random.nextFloat() < 0.15f) {
                    this.playSound((SoundEvent)SoundRegistry.VOID_TENTACLES_AMBIENT.get(), 1.5f, 0.5f + Utils.random.nextFloat() * 0.65f);
                }
            }
            if (this.age == 260 && Utils.random.nextFloat() < 0.3f) {
                this.playSound((SoundEvent)SoundRegistry.VOID_TENTACLES_LEAVE.get(), 2.0f, 1.0f);
            }
        } else if (this.age < 280 && Utils.random.nextFloat() < 0.15f) {
            this.level.addParticle(ParticleHelper.VOID_TENTACLE_FOG, this.getX() + Utils.getRandomScaled(0.5), this.getY() + Utils.getRandomScaled(0.5) + (double)0.2f, this.getZ() + Utils.getRandomScaled(0.5), Utils.getRandomScaled(2.0), (double)(-this.random.nextFloat() * 0.5f), Utils.getRandomScaled(2.0));
        }
        ++this.age;
    }

    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public boolean dealDamage(LivingEntity target) {
        if (target != this.getOwner() && DamageSources.applyDamage((Entity)target, this.damage, SpellRegistry.SCULK_TENTACLES_SPELL.get().getDamageSource((Entity)this, (Entity)this.getOwner()))) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
            return true;
        }
        return false;
    }

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }

    public boolean isPushable() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Nullable
    public LivingEntity getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel && (entity = ((ServerLevel)this.level).getEntity(this.ownerUUID)) instanceof LivingEntity) {
            this.owner = (LivingEntity)entity;
        }
        return this.owner;
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.age = pCompound.getInt("Age");
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }

    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Age", this.age);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.SMOKE, this.getX(), this.getY() + 1.0, this.getZ(), 50, 0.2, 1.25, 0.2, 0.08, false);
        this.discard();
    }

    private PlayState animationPredicate(AnimationState event) {
        AnimationController controller = event.getController();
        if (this.age > 220 && Utils.random.nextFloat() < 0.04f) {
            controller.setAnimation(this.ANIMATION_RETREAT);
        } else if (controller.getAnimationState() == AnimationController.State.STOPPED) {
            controller.setAnimation(this.ANIMATION_IDLE);
        }
        return PlayState.CONTINUE;
    }

    private PlayState risePredicate(AnimationState event) {
        AnimationController controller = event.getController();
        if (this.age < 10) {
            controller.setAnimation(this.ANIMATION_RISE);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.riseController);
        controllerRegistrar.add(this.controller);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

