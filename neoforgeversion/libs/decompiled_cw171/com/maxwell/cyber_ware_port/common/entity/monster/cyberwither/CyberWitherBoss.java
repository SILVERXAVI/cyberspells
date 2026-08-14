/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ColorParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerBossEvent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.world.BossEvent$BossBarColor
 *  net.minecraft.world.BossEvent$BossBarOverlay
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.PowerableMob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.FlyingMoveControl
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.RangedAttackGoal
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.monster.RangedAttackMob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.WitherSkull
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.entity.monster.cyberwither;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.entity.ICyberwareMob;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwitherskeleton.CyberWitherSkeletonEntity;
import com.maxwell.cyber_ware_port.init.ModEntities;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.Nullable;

public class CyberWitherBoss
extends Monster
implements PowerableMob,
RangedAttackMob,
ICyberwareMob {
    private static final EntityDataAccessor<Integer> DATA_TARGET_A = SynchedEntityData.defineId(CyberWitherBoss.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_B = SynchedEntityData.defineId(CyberWitherBoss.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_C = SynchedEntityData.defineId(CyberWitherBoss.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final List<EntityDataAccessor<Integer>> DATA_TARGETS = List.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);
    private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(CyberWitherBoss.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_MINION_1 = SynchedEntityData.defineId(CyberWitherBoss.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_MINION_2 = SynchedEntityData.defineId(CyberWitherBoss.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_MINION_3 = SynchedEntityData.defineId(CyberWitherBoss.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = entity -> entity.getType() != EntityType.WITHER_SKELETON && entity.attackable();
    private final float[] xRotHeads = new float[2];
    private final float[] yRotHeads = new float[2];
    private final float[] xRotOHeads = new float[2];
    private final float[] yRotOHeads = new float[2];
    private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
    private boolean hasSummonedMinions = false;
    private int shieldTimer = 0;
    private int empCooldown = 200;
    private boolean hasExplodedAtHalfHealth = false;

    public CyberWitherBoss(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl((Mob)this, 10, false);
        this.setHealth(this.getMaxHealth());
        this.xpReward = 100;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 800.0).add(Attributes.MOVEMENT_SPEED, 0.6).add(Attributes.FLYING_SPEED, 0.6).add(Attributes.FOLLOW_RANGE, 64.0).add(Attributes.ARMOR, 12.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public List<Item> getForbiddenDrops() {
        return Arrays.asList((Item)ModItems.CYBER_ARM_LEFT.get(), (Item)ModItems.CYBER_ARM_RIGHT.get(), (Item)ModItems.CYBER_LEG_LEFT.get(), (Item)ModItems.CYBER_LEG_RIGHT.get(), (Item)ModItems.RETRACTABLE_CLAWS.get(), (Item)ModItems.REINFORCED_FIST.get(), (Item)ModItems.FINE_MANIPULATORS.get(), (Item)ModItems.RAPID_FIRE_FLYWHEEL.get(), (Item)ModItems.LINEAR_ACTUATORS.get(), (Item)ModItems.FALL_BRACERS.get(), (Item)ModItems.AQUATIC_PROPULSION.get(), (Item)ModItems.DEPLOYABLE_WHEELS.get(), (Item)ModItems.IMPLANTED_SPURS.get());
    }

    @Override
    public boolean isHighTierMob() {
        return true;
    }

    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation nav = new FlyingPathNavigation((Mob)this, pLevel);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        nav.setCanPassDoors(true);
        return nav;
    }

    public boolean isInvertedHealAndHarm() {
        return true;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, (Goal)new CyberWitherDoNothingGoal());
        this.goalSelector.addGoal(2, (Goal)new RangedAttackGoal((RangedAttackMob)this, 1.0, 40, 20.0f));
        this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomFlyingGoal((PathfinderMob)this, 1.0));
        this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_TARGET_A, (Object)0);
        pBuilder.define(DATA_TARGET_B, (Object)0);
        pBuilder.define(DATA_TARGET_C, (Object)0);
        pBuilder.define(DATA_ID_INV, (Object)0);
        pBuilder.define(DATA_MINION_1, (Object)-1);
        pBuilder.define(DATA_MINION_2, (Object)-1);
        pBuilder.define(DATA_MINION_3, (Object)-1);
    }

    public void aiStep() {
        Entity entity;
        Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.6, 1.0);
        if (!this.level().isClientSide && this.getAlternativeTarget(0) > 0 && (entity = this.level().getEntity(this.getAlternativeTarget(0))) != null) {
            double d0 = vec3.y;
            if (this.getY() < entity.getY() || !this.isPowered() && this.getY() < entity.getY() + 2.0) {
                d0 = Math.max(0.0, d0);
                d0 += 0.3 - d0 * (double)0.6f;
            }
            vec3 = new Vec3(vec3.x, d0, vec3.z);
            Vec3 vec31 = new Vec3(entity.getX() - this.getX(), 0.0, entity.getZ() - this.getZ());
            if (vec31.horizontalDistanceSqr() > 9.0) {
                Vec3 vec32 = vec31.normalize();
                vec3 = vec3.add(vec32.x * 0.3 - vec3.x * 0.6, 0.0, vec32.z * 0.3 - vec3.z * 0.6);
            }
        }
        this.setDeltaMovement(vec3);
        if (vec3.horizontalDistanceSqr() > 0.05) {
            this.setYRot((float)Mth.atan2((double)vec3.z, (double)vec3.x) * 57.295776f - 90.0f);
        }
        super.aiStep();
        for (int i = 0; i < 2; ++i) {
            this.yRotOHeads[i] = this.yRotHeads[i];
            this.xRotOHeads[i] = this.xRotHeads[i];
        }
        for (int j = 0; j < 2; ++j) {
            int k = this.getAlternativeTarget(j + 1);
            Entity entity1 = null;
            if (k > 0) {
                entity1 = this.level().getEntity(k);
            }
            if (entity1 != null) {
                double d9 = this.getHeadX(j + 1);
                double d1 = this.getHeadY(j + 1);
                double d3 = this.getHeadZ(j + 1);
                double d4 = entity1.getX() - d9;
                double d5 = entity1.getEyeY() - d1;
                double d6 = entity1.getZ() - d3;
                double d7 = Math.sqrt(d4 * d4 + d6 * d6);
                float f = (float)(Mth.atan2((double)d6, (double)d4) * 57.2957763671875) - 90.0f;
                float f1 = (float)(-(Mth.atan2((double)d5, (double)d7) * 57.2957763671875));
                this.xRotHeads[j] = this.rotlerp(this.xRotHeads[j], f1, 40.0f);
                this.yRotHeads[j] = this.rotlerp(this.yRotHeads[j], f, 10.0f);
                continue;
            }
            this.yRotHeads[j] = this.rotlerp(this.yRotHeads[j], this.yBodyRot, 10.0f);
        }
        boolean flag = this.isPowered();
        for (int l = 0; l < 3; ++l) {
            double d8 = this.getHeadX(l);
            double d10 = this.getHeadY(l);
            double d2 = this.getHeadZ(l);
            this.level().addParticle((ParticleOptions)ParticleTypes.SMOKE, d8 + this.random.nextGaussian() * 0.3, d10 + this.random.nextGaussian() * 0.3, d2 + this.random.nextGaussian() * 0.3, 0.0, 0.0, 0.0);
            if (!flag || this.level().random.nextInt(4) != 0) continue;
            this.level().addParticle((ParticleOptions)ColorParticleOption.create((ParticleType)ParticleTypes.ENTITY_EFFECT, (float)0.7f, (float)0.7f, (float)0.5f), d8 + this.random.nextGaussian() * 0.3, d10 + this.random.nextGaussian() * 0.3, d2 + this.random.nextGaussian() * 0.3, 0.0, 0.0, 0.0);
        }
        if (this.getInvulnerableTicks() > 0) {
            for (int i1 = 0; i1 < 3; ++i1) {
                this.level().addParticle((ParticleOptions)ColorParticleOption.create((ParticleType)ParticleTypes.ENTITY_EFFECT, (float)0.7f, (float)0.7f, (float)0.9f), this.getX() + this.random.nextGaussian(), this.getY() + (double)(this.random.nextFloat() * 3.3f), this.getZ() + this.random.nextGaussian(), 0.0, 0.0, 0.0);
            }
        }
        if (!this.level().isClientSide) {
            this.customServerAiStep();
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getInvulnerableTicks() > 0) {
            int k1 = this.getInvulnerableTicks() - 1;
            this.bossEvent.setProgress(1.0f - (float)k1 / 220.0f);
            if (k1 <= 0) {
                this.level().explode((Entity)this, null, null, this.getX(), this.getEyeY(), this.getZ(), 7.0f, false, Level.ExplosionInteraction.MOB);
                if (!this.isSilent()) {
                    this.level().globalLevelEvent(1023, this.blockPosition(), 0);
                }
            }
            this.setInvulnerableTicks(k1);
            if (this.tickCount % 10 == 0 && !this.hasExplodedAtHalfHealth) {
                this.heal(10.0f);
            }
        } else {
            float maxHealth = this.getMaxHealth();
            float currentHealth = this.getHealth();
            float halfHealth = maxHealth / 2.0f;
            if (!this.hasExplodedAtHalfHealth) {
                float phase1Progress = (currentHealth - halfHealth) / halfHealth;
                this.bossEvent.setProgress(Mth.clamp((float)phase1Progress, (float)0.0f, (float)1.0f));
            } else {
                float phase2Progress = currentHealth / halfHealth;
                this.bossEvent.setProgress(Mth.clamp((float)phase2Progress, (float)0.0f, (float)1.0f));
            }
            if (this.getTarget() != null) {
                this.setAlternativeTarget(0, this.getTarget().getId());
            } else {
                this.setAlternativeTarget(0, 0);
            }
            if (!this.hasExplodedAtHalfHealth && currentHealth <= halfHealth) {
                this.hasExplodedAtHalfHealth = true;
                this.setInvulnerableTicks(220);
                return;
            }
            if (this.empCooldown > 0) {
                --this.empCooldown;
            } else {
                this.performEmpBlast();
                this.empCooldown = 300;
            }
            if (this.getHealth() <= 150.0f && !this.hasSummonedMinions) {
                this.startPhaseTwo();
            }
            if (this.shieldTimer > 0) {
                --this.shieldTimer;
                if (this.shieldTimer < 1180) {
                    this.checkMinionStatus(DATA_MINION_1);
                    this.checkMinionStatus(DATA_MINION_2);
                    this.checkMinionStatus(DATA_MINION_3);
                    if (this.getMinionId(1) == -1 && this.getMinionId(2) == -1 && this.getMinionId(3) == -1) {
                        this.shieldTimer = 0;
                    }
                }
                if (this.shieldTimer == 0) {
                    this.endPhaseTwo();
                } else {
                    this.setDeltaMovement(0.0, 0.0, 0.0);
                }
            }
        }
    }

    private void startPhaseTwo() {
        this.hasSummonedMinions = true;
        this.shieldTimer = 1200;
        this.spawnMinion(DATA_MINION_1, 3.0, 0.0);
        this.spawnMinion(DATA_MINION_2, -1.5, 2.6);
        this.spawnMinion(DATA_MINION_3, -1.5, -2.6);
        this.level().globalLevelEvent(1023, this.blockPosition(), 0);
    }

    private void spawnMinion(EntityDataAccessor<Integer> dataAccessor, double offsetX, double offsetZ) {
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            CyberWitherSkeletonEntity minion = (CyberWitherSkeletonEntity)((EntityType)ModEntities.CYBER_WITHER_SKELETON.get()).create((Level)serverLevel);
            if (minion != null) {
                minion.moveTo(this.getX() + offsetX, this.getY(), this.getZ() + offsetZ, this.getYRot(), 0.0f);
                minion.finalizeSpawn((ServerLevelAccessor)serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
                if (this.getTarget() != null) {
                    minion.setTarget(this.getTarget());
                }
                serverLevel.addFreshEntity((Entity)minion);
                this.entityData.set(dataAccessor, (Object)minion.getId());
            }
        }
    }

    private void checkMinionStatus(EntityDataAccessor<Integer> accessor) {
        Entity entity;
        int id = (Integer)this.entityData.get(accessor);
        if (!(id == -1 || (entity = this.level().getEntity(id)) != null && entity.isAlive())) {
            this.entityData.set(accessor, (Object)-1);
        }
    }

    private void endPhaseTwo() {
        this.entityData.set(DATA_MINION_1, (Object)-1);
        this.entityData.set(DATA_MINION_2, (Object)-1);
        this.entityData.set(DATA_MINION_3, (Object)-1);
    }

    private void performEmpBlast() {
        if (!this.isAlive()) {
            return;
        }
        List nearbyPlayers = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20.0));
        boolean hitAny = false;
        for (Player player : nearbyPlayers) {
            if (!(player instanceof ServerPlayer)) continue;
            ServerPlayer serverPlayer = (ServerPlayer)player;
            CyberwareUserData data = (CyberwareUserData)serverPlayer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            if (data.getEnergyStored() > 0) {
                data.extractEnergy(5000, false);
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                serverPlayer.sendSystemMessage((Component)Component.literal((String)"\u00a7cWARNING: EMP SURGE DETECTED - SYSTEMS OFFLINE"));
            }
            hitAny = true;
        }
        if (hitAny) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)SoundEvents.GENERIC_EXPLODE.value(), this.getSoundSource(), 2.0f, 0.5f);
            this.level().addParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 1.0, 0.0, 0.0);
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(pSource, pAmount);
        }
        if (this.getInvulnerableTicks() > 0 || this.shieldTimer > 0) {
            return false;
        }
        if (pSource.getEntity() instanceof CyberWitherBoss) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    private double getHeadX(int pHead) {
        if (pHead <= 0) {
            return this.getX();
        }
        float f = (this.yBodyRot + (float)(180 * (pHead - 1))) * ((float)Math.PI / 180);
        return this.getX() + (double)Mth.cos((float)f) * 1.3;
    }

    private double getHeadY(int pHead) {
        return pHead <= 0 ? this.getY() + 3.0 : this.getY() + 2.2;
    }

    private double getHeadZ(int pHead) {
        if (pHead <= 0) {
            return this.getZ();
        }
        float f = (this.yBodyRot + (float)(180 * (pHead - 1))) * ((float)Math.PI / 180);
        return this.getZ() + (double)Mth.sin((float)f) * 1.3;
    }

    private float rotlerp(float pAngle, float pTarget, float pMax) {
        float f = Mth.wrapDegrees((float)(pTarget - pAngle));
        if (f > pMax) {
            f = pMax;
        }
        if (f < -pMax) {
            f = -pMax;
        }
        return pAngle + f;
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Invul", this.getInvulnerableTicks());
        pCompound.putBoolean("PhaseTwoUsed", this.hasSummonedMinions);
        pCompound.putInt("ShieldTimer", this.shieldTimer);
        pCompound.putBoolean("HalfHealthExploded", this.hasExplodedAtHalfHealth);
    }

    public boolean isNoGravity() {
        return true;
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setInvulnerableTicks(pCompound.getInt("Invul"));
        this.hasSummonedMinions = pCompound.getBoolean("PhaseTwoUsed");
        this.shieldTimer = pCompound.getInt("ShieldTimer");
        this.hasExplodedAtHalfHealth = pCompound.getBoolean("HalfHealthExploded");
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public int getMinionId(int index) {
        return switch (index) {
            case 1 -> (Integer)this.entityData.get(DATA_MINION_1);
            case 2 -> (Integer)this.entityData.get(DATA_MINION_2);
            case 3 -> (Integer)this.entityData.get(DATA_MINION_3);
            default -> -1;
        };
    }

    public void setCustomName(@Nullable Component pName) {
        super.setCustomName(pName);
        this.bossEvent.setName(this.getDisplayName());
    }

    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);
        this.bossEvent.addPlayer(pPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        this.bossEvent.removePlayer(pPlayer);
    }

    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        this.performRangedAttack(0, pTarget);
    }

    private void performRangedAttack(int pHead, LivingEntity pTarget) {
        this.performRangedAttack(pHead, pTarget.getX(), pTarget.getY() + (double)pTarget.getEyeHeight() * 0.5, pTarget.getZ(), pHead == 0 && this.random.nextFloat() < 0.001f);
    }

    private void performRangedAttack(int pHead, double pX, double pY, double pZ, boolean pIsDangerous) {
        if (!this.isSilent()) {
            this.level().levelEvent(null, 1024, this.blockPosition(), 0);
        }
        double headX = this.getHeadX(pHead);
        double headY = this.getHeadY(pHead);
        double headZ = this.getHeadZ(pHead);
        double vecX = pX - headX;
        double vecY = pY - headY;
        double vecZ = pZ - headZ;
        WitherSkull witherskull = new WitherSkull(this.level(), (LivingEntity)this, new Vec3(vecX, vecY, vecZ));
        witherskull.setOwner((Entity)this);
        if (pIsDangerous) {
            witherskull.setDangerous(true);
        }
        witherskull.setPosRaw(headX, headY, headZ);
        this.level().addFreshEntity((Entity)witherskull);
    }

    public boolean isPowered() {
        return this.getHealth() <= this.getMaxHealth() / 2.0f;
    }

    public int getInvulnerableTicks() {
        return (Integer)this.entityData.get(DATA_ID_INV);
    }

    public void setInvulnerableTicks(int pInvulnerableTicks) {
        this.entityData.set(DATA_ID_INV, (Object)pInvulnerableTicks);
    }

    public int getAlternativeTarget(int pHead) {
        return (Integer)this.entityData.get(DATA_TARGETS.get(pHead));
    }

    public void setAlternativeTarget(int pTargetOffset, int pNewId) {
        this.entityData.set(DATA_TARGETS.get(pTargetOffset), (Object)pNewId);
    }

    public float getHeadYRot(int pHead) {
        return pHead <= 0 ? this.getYRot() : this.yRotHeads[pHead - 1];
    }

    public float getHeadXRot(int pHead) {
        return pHead <= 0 ? this.getXRot() : this.xRotHeads[pHead - 1];
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WITHER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    public boolean canChangeDimensions(Level pOldLevel, Level pNewLevel) {
        return false;
    }

    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    class CyberWitherDoNothingGoal
    extends Goal {
        public CyberWitherDoNothingGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return CyberWitherBoss.this.getInvulnerableTicks() > 0;
        }
    }
}

