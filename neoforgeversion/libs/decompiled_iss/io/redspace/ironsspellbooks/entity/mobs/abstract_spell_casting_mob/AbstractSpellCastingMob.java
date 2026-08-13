/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  javax.annotation.Nullable
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.LookControl
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoEntity
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import com.google.common.collect.Maps;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import java.util.HashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractSpellCastingMob
extends PathfinderMob
implements GeoEntity,
IMagicEntity {
    public static final ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"geo/abstract_casting_mob.geo.json");
    public static final ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/abstract_casting_mob/abstract_casting_mob.png");
    public static final ResourceLocation animationInstantCast = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animations/casting_animations.json");
    private static final EntityDataAccessor<Boolean> DATA_CANCEL_CAST = SynchedEntityData.defineId(AbstractSpellCastingMob.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_DRINKING_POTION = SynchedEntityData.defineId(AbstractSpellCastingMob.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private final MagicData playerMagicData = new MagicData(true);
    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(IronsSpellbooks.id("potion_slowdown"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    @javax.annotation.Nullable
    private SpellData castingSpell;
    private final HashMap<String, AbstractSpell> spells = Maps.newHashMap();
    private int drinkTime;
    public boolean hasUsedSingleAttack;
    private boolean recreateSpell;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
    private AbstractSpell lastCastSpellType = SpellRegistry.none();
    private AbstractSpell instantCastSpellType = SpellRegistry.none();
    private boolean cancelCastAnimation = false;
    private boolean animatingLegs = false;
    private final AnimationController animationControllerOtherCast = new AnimationController((GeoAnimatable)this, "other_casting", 0, this::otherCastingPredicate);
    private final AnimationController animationControllerInstantCast = new AnimationController((GeoAnimatable)this, "instant_casting", 0, this::instantCastingPredicate);
    private final AnimationController animationControllerLongCast = new AnimationController((GeoAnimatable)this, "long_casting", 0, this::longCastingPredicate);

    protected AbstractSpellCastingMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.playerMagicData.setSyncedData(new SyncedSpellData((LivingEntity)this));
        this.noCulling = true;
        this.lookControl = this.createLookControl();
    }

    @Override
    public boolean getHasUsedSingleAttack() {
        return this.hasUsedSingleAttack;
    }

    @Override
    public void setHasUsedSingleAttack(boolean hasUsedSingleAttack) {
        this.hasUsedSingleAttack = hasUsedSingleAttack;
    }

    public void rideTick() {
        super.rideTick();
        Entity entity = this.getVehicle();
        if (entity instanceof PathfinderMob) {
            PathfinderMob pathfindermob = (PathfinderMob)entity;
            pathfindermob.yBodyRot = this.yBodyRot;
        }
    }

    protected LookControl createLookControl() {
        return new LookControl((Mob)this){

            protected boolean resetXRotOnTick() {
                return AbstractSpellCastingMob.this.getTarget() == null;
            }
        };
    }

    @Override
    public MagicData getMagicData() {
        return this.playerMagicData;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_CANCEL_CAST, (Object)false);
        pBuilder.define(DATA_DRINKING_POTION, (Object)false);
    }

    @Override
    public boolean isDrinkingPotion() {
        return (Boolean)this.entityData.get(DATA_DRINKING_POTION);
    }

    protected void setDrinkingPotion(boolean drinkingPotion) {
        this.entityData.set(DATA_DRINKING_POTION, (Object)drinkingPotion);
    }

    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public void startDrinkingPotion() {
        if (!this.level.isClientSide) {
            this.setDrinkingPotion(true);
            this.drinkTime = 35;
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_DRINKING);
            attributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
        }
    }

    private void finishDrinkingPotion() {
        this.setDrinkingPotion(false);
        this.heal(Math.min(Math.max(10.0f, this.getMaxHealth() / 10.0f), this.getMaxHealth() / 4.0f));
        this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
        if (!this.isSilent()) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0f, 0.8f + this.random.nextFloat() * 0.4f);
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (!this.level.isClientSide) {
            return;
        }
        if (pKey.id() == DATA_CANCEL_CAST.id()) {
            this.cancelCast();
        }
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.playerMagicData.getSyncedData().saveNBTData(pCompound, (HolderLookup.Provider)this.level.registryAccess());
        pCompound.putBoolean("usedSpecial", this.hasUsedSingleAttack);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        SyncedSpellData syncedSpellData = new SyncedSpellData((LivingEntity)this);
        syncedSpellData.loadNBTData(pCompound, (HolderLookup.Provider)this.level.registryAccess());
        if (syncedSpellData.isCasting()) {
            this.recreateSpell = true;
        }
        this.playerMagicData.setSyncedData(syncedSpellData);
        this.hasUsedSingleAttack = pCompound.getBoolean("usedSpecial");
    }

    @Override
    public void cancelCast() {
        if (this.isCasting()) {
            if (this.level.isClientSide) {
                this.cancelCastAnimation = true;
            } else {
                this.entityData.set(DATA_CANCEL_CAST, (Object)((Boolean)this.entityData.get(DATA_CANCEL_CAST) == false ? 1 : 0));
            }
            this.castComplete();
        }
    }

    @Override
    public void castComplete() {
        if (!this.level.isClientSide) {
            if (this.castingSpell != null) {
                this.castingSpell.getSpell().onServerCastComplete(this.level, this.castingSpell.getLevel(), (LivingEntity)this, this.playerMagicData, false);
            }
        } else {
            this.playerMagicData.resetCastingState();
        }
        this.castingSpell = null;
    }

    @Override
    public void setSyncedSpellData(SyncedSpellData syncedSpellData) {
        if (!this.level.isClientSide) {
            return;
        }
        boolean isCasting = this.playerMagicData.isCasting();
        this.playerMagicData.setSyncedData(syncedSpellData);
        this.castingSpell = this.playerMagicData.getCastingSpell();
        if (this.castingSpell == null) {
            return;
        }
        if (!this.playerMagicData.isCasting() && isCasting) {
            this.castComplete();
        } else if (this.playerMagicData.isCasting() && !isCasting) {
            AbstractSpell spell = this.playerMagicData.getCastingSpell().getSpell();
            this.initiateCastSpell(spell, this.playerMagicData.getCastingSpellLevel());
            if (this.castingSpell.getSpell().getCastType() == CastType.INSTANT) {
                this.instantCastSpellType = this.castingSpell.getSpell();
                this.castingSpell.getSpell().onClientPreCast(this.level, this.castingSpell.getLevel(), (LivingEntity)this, InteractionHand.MAIN_HAND, this.playerMagicData);
                this.castComplete();
            }
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.recreateSpell) {
            this.recreateSpell = false;
            SyncedSpellData syncedSpellData = this.playerMagicData.getSyncedData();
            AbstractSpell spell = SpellRegistry.getSpell(syncedSpellData.getCastingSpellId());
            this.initiateCastSpell(spell, syncedSpellData.getCastingSpellLevel());
        }
        if (this.isDrinkingPotion()) {
            if (this.drinkTime-- <= 0) {
                this.finishDrinkingPotion();
            } else if (this.drinkTime % 4 == 0 && !this.isSilent()) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_DRINK, this.getSoundSource(), 1.0f, Utils.random.nextFloat() * 0.1f + 0.9f);
            }
        }
        if (this.castingSpell == null) {
            return;
        }
        this.playerMagicData.handleCastDuration();
        if (this.playerMagicData.isCasting()) {
            this.castingSpell.getSpell().onServerCastTick(this.level, this.castingSpell.getLevel(), (LivingEntity)this, this.playerMagicData);
        }
        this.forceLookAtTarget(this.getTarget());
        if (this.playerMagicData.getCastDurationRemaining() <= 0) {
            if (this.castingSpell.getSpell().getCastType() == CastType.LONG || this.castingSpell.getSpell().getCastType() == CastType.INSTANT) {
                this.castingSpell.getSpell().onCast(this.level, this.castingSpell.getLevel(), (LivingEntity)this, CastSource.MOB, this.playerMagicData);
            }
            this.castComplete();
        } else if (this.castingSpell.getSpell().getCastType() == CastType.CONTINUOUS && (this.playerMagicData.getCastDurationRemaining() + 1) % 10 == 0) {
            this.castingSpell.getSpell().onCast(this.level, this.castingSpell.getLevel(), (LivingEntity)this, CastSource.MOB, this.playerMagicData);
        }
    }

    @Override
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        if (spell == SpellRegistry.none()) {
            this.castingSpell = null;
            return;
        }
        if (this.level.isClientSide) {
            this.cancelCastAnimation = false;
        }
        this.castingSpell = new SpellData(spell, spellLevel);
        if (this.getTarget() != null) {
            this.forceLookAtTarget(this.getTarget());
        }
        if (!this.level.isClientSide && !this.castingSpell.getSpell().checkPreCastConditions(this.level, spellLevel, (LivingEntity)this, this.playerMagicData)) {
            this.castingSpell = null;
            return;
        }
        if (spell == SpellRegistry.TELEPORT_SPELL.get() || spell == SpellRegistry.FROST_STEP_SPELL.get()) {
            this.setTeleportLocationBehindTarget(10);
        } else if (spell == SpellRegistry.BLOOD_STEP_SPELL.get()) {
            this.setTeleportLocationBehindTarget(3);
        } else if (spell == SpellRegistry.BURNING_DASH_SPELL.get()) {
            this.setBurningDashDirectionData();
        }
        this.playerMagicData.initiateCast(this.castingSpell.getSpell(), this.castingSpell.getLevel(), this.castingSpell.getSpell().getEffectiveCastTime(this.castingSpell.getLevel(), (LivingEntity)this), CastSource.MOB, SpellSelectionManager.MAINHAND);
        if (!this.level.isClientSide) {
            this.castingSpell.getSpell().onServerPreCast(this.level, this.castingSpell.getLevel(), (LivingEntity)this, this.playerMagicData);
        }
    }

    @Override
    public void notifyDangerousProjectile(Projectile projectile) {
    }

    @Override
    public boolean isCasting() {
        return this.playerMagicData.isCasting();
    }

    @Override
    public boolean setTeleportLocationBehindTarget(int distance) {
        LivingEntity target = this.getTarget();
        boolean valid = false;
        if (target != null) {
            Vec3 rotation = target.getLookAngle().normalize().scale((double)(-distance));
            Vec3 pos = target.position();
            Vec3 teleportPos = rotation.add(pos);
            for (int i = 0; i < 24; ++i) {
                Vec3 randomness = Utils.getRandomVec3(0.15f * (float)i).multiply(1.0, 0.0, 1.0);
                teleportPos = Utils.moveToRelativeGroundLevel(this.level, target.position().subtract(new Vec3(0.0, 0.0, (double)((float)distance / (float)(i / 7 + 1))).yRot(-(target.getYRot() + (float)(i * 45)) * ((float)Math.PI / 180))).add(randomness), 5);
                teleportPos = new Vec3(teleportPos.x, teleportPos.y + (double)0.1f, teleportPos.z);
                AABB reposBB = this.getBoundingBox().move(teleportPos.subtract(this.position()));
                if (this.level.collidesWithSuffocatingBlock((Entity)this, reposBB.inflate((double)-0.05f))) continue;
                valid = true;
                break;
            }
            if (valid) {
                this.playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(teleportPos));
            } else {
                this.playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(this.position()));
            }
        } else {
            this.playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(this.position()));
        }
        return valid;
    }

    @Override
    public void setBurningDashDirectionData() {
        this.playerMagicData.setAdditionalCastData(new BurningDashSpell.BurningDashDirectionOverrideCastData());
    }

    private void forceLookAtTarget(LivingEntity target) {
        if (target != null) {
            double d0 = target.getX() - this.getX();
            double d2 = target.getZ() - this.getZ();
            double d1 = target.getEyeY() - this.getEyeY();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float f = (float)(Mth.atan2((double)d2, (double)d0) * 57.2957763671875) - 90.0f;
            float f1 = (float)(-(Mth.atan2((double)d1, (double)d3) * 57.2957763671875));
            this.setXRot(f1 % 360.0f);
            this.setYRot(f % 360.0f);
        }
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void triggerAnim(@Nullable String controllerName, String animName) {
        super.triggerAnim(controllerName, animName);
    }

    public double getBoneResetTime() {
        return 5.0;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.animationControllerOtherCast);
        controllerRegistrar.add(this.animationControllerInstantCast);
        controllerRegistrar.add(this.animationControllerLongCast);
    }

    private PlayState instantCastingPredicate(AnimationState event) {
        if (this.cancelCastAnimation) {
            return PlayState.STOP;
        }
        AnimationController controller = event.getController();
        if (this.instantCastSpellType != SpellRegistry.none() && controller.getAnimationState() == AnimationController.State.STOPPED) {
            this.setStartAnimationFromSpell(controller, this.instantCastSpellType);
            this.instantCastSpellType = SpellRegistry.none();
        }
        return PlayState.CONTINUE;
    }

    private PlayState longCastingPredicate(AnimationState event) {
        AnimationController controller = event.getController();
        if (this.cancelCastAnimation || controller.getAnimationState() == AnimationController.State.STOPPED && (!this.isCasting() || this.castingSpell == null || this.castingSpell.getSpell().getCastType() != CastType.LONG)) {
            return PlayState.STOP;
        }
        if (this.isCasting()) {
            if (controller.getAnimationState() == AnimationController.State.STOPPED) {
                this.setStartAnimationFromSpell(controller, this.castingSpell.getSpell());
            }
        } else if (this.lastCastSpellType.getCastType() == CastType.LONG) {
            this.setFinishAnimationFromSpell(controller, this.lastCastSpellType);
        }
        return PlayState.CONTINUE;
    }

    private PlayState otherCastingPredicate(AnimationState event) {
        if (this.cancelCastAnimation) {
            return PlayState.STOP;
        }
        AnimationController controller = event.getController();
        if (this.isCasting() && this.castingSpell != null && controller.getAnimationState() == AnimationController.State.STOPPED) {
            if (this.castingSpell.getSpell().getCastType() == CastType.CONTINUOUS) {
                this.setStartAnimationFromSpell(controller, this.castingSpell.getSpell());
            }
            return PlayState.CONTINUE;
        }
        if (this.isCasting()) {
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private void setStartAnimationFromSpell(AnimationController controller, AbstractSpell spell) {
        spell.getCastStartAnimation().getForMob().ifPresentOrElse(animationBuilder -> {
            controller.forceAnimationReset();
            controller.setAnimation(animationBuilder);
            this.lastCastSpellType = spell;
            this.cancelCastAnimation = false;
            this.animatingLegs = spell.getCastStartAnimation().animatesLegs;
        }, () -> {
            this.cancelCastAnimation = true;
        });
    }

    private void setFinishAnimationFromSpell(AnimationController controller, AbstractSpell spell) {
        if (spell.getCastFinishAnimation().isPass) {
            this.cancelCastAnimation = false;
            return;
        }
        spell.getCastFinishAnimation().getForMob().ifPresentOrElse(animationBuilder -> {
            controller.forceAnimationReset();
            controller.setAnimation(animationBuilder);
            this.lastCastSpellType = SpellRegistry.none();
            this.cancelCastAnimation = false;
        }, () -> {
            this.cancelCastAnimation = true;
        });
    }

    public boolean isAnimating() {
        return this.isCasting() || this.animationControllerLongCast.getAnimationState() == AnimationController.State.RUNNING || this.animationControllerOtherCast.getAnimationState() == AnimationController.State.RUNNING || this.animationControllerInstantCast.getAnimationState() == AnimationController.State.RUNNING;
    }

    public boolean shouldBeExtraAnimated() {
        return true;
    }

    public boolean shouldAlwaysAnimateHead() {
        return true;
    }

    public boolean shouldAlwaysAnimateLegs() {
        return !this.animatingLegs;
    }

    public boolean shouldPointArmsWhileCasting() {
        return true;
    }

    public boolean bobBodyWhileWalking() {
        return true;
    }

    public boolean shouldSheathSword() {
        return false;
    }
}

