/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.summoned_weapons;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.wizards.GenericAnimatedWarlockAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.AnimatedActionGoal;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedWeaponEntity;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonedClaymoreEntity
extends SummonedWeaponEntity {
    private static final EntityDataAccessor<Boolean> DATA_IS_TAUNTING = SynchedEntityData.defineId(SummonedClaymoreEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.MAX_HEALTH, 40.0).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.FLYING_SPEED, 1.0).add(Attributes.ENTITY_INTERACTION_RANGE, 4.0).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_TAUNTING, (Object)false);
    }

    public boolean isTaunting() {
        return (Boolean)this.entityData.get(DATA_IS_TAUNTING);
    }

    public void setTaunting(boolean taunting) {
        this.entityData.set(DATA_IS_TAUNTING, (Object)taunting);
    }

    public SummonedClaymoreEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SummonedClaymoreEntity(Level level, LivingEntity owner) {
        this((EntityType<? extends PathfinderMob>)((EntityType)EntityRegistry.SUMMONED_CLAYMORE.get()), level);
        this.setSummoner(owner);
    }

    @Override
    public GenericAnimatedWarlockAttackGoal<? extends SummonedWeaponEntity> makeAttackGoal() {
        return new GenericAnimatedWarlockAttackGoal<SummonedClaymoreEntity>(this, 1.5, 20, 40).setMoveset(List.of(AttackAnimationData.builder("summoned_sword_pommel_strike").length(24).attacks(new AttackKeyframe(12, new Vec3(0.0, 0.0, (double)0.45f), new Vec3(0.0, 0.0, 1.0))).build(), AttackAnimationData.builder("summoned_sword_basic_downswing").length(45).attacks(new AttackKeyframe(25, new Vec3(0.0, -0.2, (double)0.15f), new Vec3(0.0, 0.0, 1.0))).area(0.7f).build()));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isTaunting()) {
            this.zza = 0.0f;
            this.xxa = 0.0f;
            MagicManager.spawnParticles(this.level, ParticleHelper.UNSTABLE_ENDER, this.getX(), this.getY(), this.getZ(), 3, 0.1, 0.1, 0.1, 0.2, false);
        }
    }

    public boolean isPushable() {
        return super.isPushable() && !this.isTaunting();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, (Goal)new ClaymoreTauntGoal(this));
        super.registerGoals();
    }

    public void move(MoverType pType, Vec3 pPos) {
        if (this.isTaunting()) {
            return;
        }
        super.move(pType, pPos);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isTaunting()) {
            pAmount *= 0.2f;
        }
        return super.hurt(pSource, pAmount);
    }

    public static class ClaymoreTauntGoal
    extends AnimatedActionGoal<SummonedClaymoreEntity> {
        List<Entity> targets = null;

        public ClaymoreTauntGoal(SummonedClaymoreEntity mob) {
            super(mob);
        }

        @Override
        protected boolean canStartAction() {
            LivingEntity target = ((SummonedClaymoreEntity)this.mob).getTarget();
            if (target == null) {
                return false;
            }
            List entities = ((SummonedClaymoreEntity)this.mob).level.getEntities((Entity)this.mob, ((SummonedClaymoreEntity)this.mob).getBoundingBox().inflate(12.0, 6.0, 12.0), entity -> entity.getClass().isAssignableFrom(target.getClass()) || entity.isAlliedTo((Entity)target));
            if (entities.size() > 2) {
                this.targets = entities;
                return true;
            }
            return false;
        }

        @Override
        protected int getActionTimestamp() {
            return 20;
        }

        @Override
        protected int getActionDuration() {
            return 120;
        }

        @Override
        protected int getCooldown() {
            return 100;
        }

        @Override
        protected String getAnimationId() {
            return "claymore_taunt";
        }

        @Override
        public void tick() {
            super.tick();
            ((SummonedClaymoreEntity)this.mob).setDeltaMovement(((SummonedClaymoreEntity)this.mob).getDeltaMovement().multiply(0.8, 0.0, 0.8).add(0.0, -1.0, 0.0));
        }

        @Override
        protected void doAction() {
            ((SummonedClaymoreEntity)this.mob).setTaunting(true);
            ((SummonedClaymoreEntity)this.mob).playSound((SoundEvent)SoundRegistry.ECHOING_STRIKE.get(), 2.0f, 1.0f);
            MagicManager.spawnParticles(((SummonedClaymoreEntity)this.mob).level, new BlastwaveParticleOptions(SpellRegistry.ECHOING_STRIKES_SPELL.get().getSchoolType().getTargetingColor(), 3.0f), ((SummonedClaymoreEntity)this.mob).getX(), ((SummonedClaymoreEntity)this.mob).getY(), ((SummonedClaymoreEntity)this.mob).getZ(), 1, 0.0, 0.0, 0.0, 0.0, true);
            if (this.targets != null) {
                this.targets.forEach(entity -> {
                    if (entity instanceof Mob) {
                        Mob tauntmob = (Mob)entity;
                        MagicManager.spawnParticles(((SummonedClaymoreEntity)this.mob).level, (ParticleOptions)ParticleTypes.ANGRY_VILLAGER, tauntmob.getX(), tauntmob.getEyeY() + (tauntmob.getBoundingBox().maxY - tauntmob.getEyeY()) * 2.0, tauntmob.getZ(), 5, 0.3, 0.3, 0.3, 0.0, false);
                        tauntmob.setTarget((LivingEntity)this.mob);
                    }
                });
            }
        }

        @Override
        public void stop() {
            super.stop();
            ((SummonedClaymoreEntity)this.mob).setTaunting(false);
            this.targets = null;
        }
    }
}

