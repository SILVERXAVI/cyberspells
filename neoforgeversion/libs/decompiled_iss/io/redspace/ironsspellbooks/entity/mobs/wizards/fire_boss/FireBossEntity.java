/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.BossEvent$BossBarColor
 *  net.minecraft.world.BossEvent$BossBarOverlay
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.control.LookControl
 *  net.minecraft.world.entity.ai.control.MoveControl
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.animal.Pig
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.storage.loot.LootParams
 *  net.minecraft.world.level.storage.loot.LootParams$Builder
 *  net.minecraft.world.level.storage.loot.LootTable
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParams
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.IEntityWithComplexSpawn
 *  net.neoforged.neoforge.network.PacketDistributor
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.BossbarManager;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.FogManager;
import io.redspace.ironsspellbooks.api.util.MusicManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.goals.MomentHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.ExtendedServerBossEvent;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FieryDaggerSwarmAbilityGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FieryDaggerZoneAbilityGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossAttackKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossMoveControl;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossMusicHandler;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.InvokeDaggerKeyframe;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.NotIdioticNavigation;
import io.redspace.ironsspellbooks.entity.spells.FireEruptionAoe;
import io.redspace.ironsspellbooks.entity.spells.fireball.MagicFireball;
import io.redspace.ironsspellbooks.network.EntityEventPacket;
import io.redspace.ironsspellbooks.network.particles.FieryExplosionParticlesPacket;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class FireBossEntity
extends AbstractSpellCastingMob
implements Enemy,
IAnimatedAttacker,
IEntityWithComplexSpawn,
IClientEventEntity {
    public static final byte CLIENT_STOP_TRACKING = 0;
    public static final byte CLIENT_START_TRACKING = 1;
    public static final byte PROC_HALF_HEALTH_TIMER = 2;
    public static final byte STOP_HALF_HEALTH_TIMER = 3;
    public static final byte START_MUSIC = 4;
    public static final byte STOP_MUSIC = 5;
    public static final byte PROC_SPECTRAL_DAGGER = 6;
    public static final int PROC_DESPAWN_SECONDS = 60;
    public static final int UNLOADED_DESPAWN_LIMIT_SECONDS = 300;
    private static final BossbarManager.BossbarSprite BOSSBAR_SPRITE = new BossbarManager.BossbarSprite(IronsSpellbooks.id("boss_bars/tyros_bossbar"), 192, 18, 3, -1);
    private static final EntityDataAccessor<Boolean> DATA_SOUL_MODE = SynchedEntityData.defineId(FireBossEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_DESPAWNING = SynchedEntityData.defineId(FireBossEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final AttributeModifier SOUL_SPEED_MODIFIER = new AttributeModifier(IronsSpellbooks.id("soul_mode"), 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier SOUL_SCALE_MODIFIER = new AttributeModifier(IronsSpellbooks.id("soul_mode"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier MANA_MODIFIER = new AttributeModifier(IronsSpellbooks.id("mana"), 10000.0, AttributeModifier.Operation.ADD_VALUE);
    private int despawnAggroDelay;
    private int destroyBlockDelay;
    private int stuckDetectorDelay;
    private int stuckDetector;
    private Vec3 lastStuckPos = Vec3.ZERO;
    private int playerScale;
    private boolean canAnimateOver;
    private boolean stopHeadAnimation;
    public float isAnimatingDampener;
    private ExtendedServerBossEvent bossEvent;
    FireBossAttackGoal attackGoal;
    int stanceBreakCounter;
    int stanceBreakTimer;
    static final int STANCE_BREAK_ANIM_TIME = 180;
    static final int STANCE_BREAK_BEGIN_SLAMS_TIMESTAMP = 130;
    static final int STANCE_BREAK_COUNT = 2;
    int spawnTimer;
    private static final int SPAWN_ANIM_TIME = 175;
    private static final int SPAWN_DELAY = 40;
    boolean hasPerformedHalfHealthAttack;
    protected int halfHealthTimer;
    protected float halfHealthDamageAccumulated;
    protected static final int HALF_HEALTH_ANIM_DURATION = 235;
    protected static final int HALF_HEALTH_JUMP_TIMESTAMP = 11;
    protected static final int HALF_HEALTH_CAST_TIMESTAMP = 230;
    int daggerTime;
    int parryCooldown;
    boolean clientDaggerParticles;
    SimpleContainer deathLoot = null;
    RawAnimation animationToPlay = null;
    private final AnimationController<FireBossEntity> meleeController = new AnimationController((GeoAnimatable)this, "melee_animations", 0, this::predicate);

    @Override
    public void handleClientEvent(byte eventId) {
        switch (eventId) {
            case 0: {
                FogManager.stopEvent(this.uuid);
                MusicManager.stopEvent(this.uuid);
                BossbarManager.stopTracking(this.uuid);
                break;
            }
            case 1: {
                FogManager.createEvent((Entity)this, new FogManager.FogEvent(Optional.empty(), true));
                if (!this.isSpawning()) {
                    MusicManager.createEvent((Entity)this, new FireBossMusicHandler(true));
                }
                BossbarManager.startTracking(this.uuid, BOSSBAR_SPRITE);
                break;
            }
            case 2: {
                this.halfHealthTimer = 235;
                break;
            }
            case 3: {
                this.halfHealthTimer = 0;
                this.playAnimation("idle");
                break;
            }
            case 4: {
                MusicManager.createEvent((Entity)this, new FireBossMusicHandler(true));
                break;
            }
            case 5: {
                MusicManager.stopEvent(this.uuid);
                break;
            }
            case 6: {
                this.procSpectralDagger();
            }
        }
    }

    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.spawnTimer);
    }

    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        float y;
        this.spawnTimer = additionalData.readInt();
        this.yBodyRot = y = this.getYRot();
        this.yBodyRotO = y;
        this.yHeadRot = y;
        this.yHeadRotO = y;
        this.yRotO = y;
    }

    public FireBossEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 25;
        this.lookControl = this.createLookControl();
        this.moveControl = this.createMoveControl();
        this.createBossEvent();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_SOUL_MODE, (Object)false);
        pBuilder.define(DATA_IS_DESPAWNING, (Object)false);
    }

    @Override
    protected LookControl createLookControl() {
        return new LookControl((Mob)this){

            protected float rotateTowards(float pFrom, float pTo, float pMaxDelta) {
                return super.rotateTowards(pFrom, pTo, pMaxDelta * 2.5f);
            }

            protected boolean resetXRotOnTick() {
                return FireBossEntity.this.getTarget() == null;
            }
        };
    }

    protected MoveControl createMoveControl() {
        return new FireBossMoveControl((Mob)this);
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

    public FireBossMoveControl getMoveControl() {
        return (FireBossMoveControl)super.getMoveControl();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
        this.attackGoal = (FireBossAttackGoal)new FireBossAttackGoal(this, 1.5, 50, 75).setMoveset(List.of(AttackAnimationData.builder("scythe_dagger_double_horizontal").length(60).attacks(new FireBossAttackKeyframe(15, new Vec3(0.0, 0.0, 0.25), new FireBossAttackKeyframe.SwingData(false, true)), new InvokeDaggerKeyframe(35), new FireBossAttackKeyframe(36, new Vec3(0.0, 0.0, 0.75), new FireBossAttackKeyframe.SwingData(false, false)), new AttackKeyframe(42, new Vec3(0.0, 0.0, 0.0))).build(), AttackAnimationData.builder("scythe_backpedal").length(40).rangeMultiplier(2.0f).attacks(new FireBossAttackKeyframe(20, new Vec3(0.0, 0.3, -2.0), new FireBossAttackKeyframe.SwingData(false, true))).build(), AttackAnimationData.builder("scythe_sideslash_downslash_sideslash").length(62).rangeMultiplier(2.0f).attacks(new FireBossAttackKeyframe(18, new Vec3(0.0, 0.0, 0.45), new FireBossAttackKeyframe.SwingData(false, true)), new FireBossAttackKeyframe(30, new Vec3(0.0, 0.0, 0.45), new FireBossAttackKeyframe.SwingData(false, false)), new FireBossAttackKeyframe(50, new Vec3(0.0, 0.1, 1.25), new Vec3(0.0, 0.3, 0.8), new FireBossAttackKeyframe.SwingData(false, false))).build(), AttackAnimationData.builder("scythe_jump_combo").length(45).cancellable().rangeMultiplier(3.0f).attacks(new FireBossAttackKeyframe(20, new Vec3(0.0, 1.0, 0.0), new Vec3(0.0, 1.15, 0.1), new FireBossAttackKeyframe.SwingData(true, false)), new FireBossAttackKeyframe(35, new Vec3(0.0, 0.0, -0.2), new Vec3(0.0, 0.0, 0.5), new FireBossAttackKeyframe.SwingData(false, false))).build(), AttackAnimationData.builder("scythe_downslash_sideslash").length(60).attacks(new FireBossAttackKeyframe(22, new Vec3(0.0, 0.0, 0.5), new Vec3(0.0, -0.2, 0.0), new FireBossAttackKeyframe.SwingData(true, true)), new FireBossAttackKeyframe(40, new Vec3(0.0, 0.1, 0.8), new FireBossAttackKeyframe.SwingData(false, false))).build(), AttackAnimationData.builder("scythe_horizontal_slash_spin").length(45).area(0.25f).rangeMultiplier(3.0f).attacks(new FireBossAttackKeyframe(14, new Vec3(0.0, 0.1, 1.25), new Vec3(0.0, 0.1, 0.8), new FireBossAttackKeyframe.SwingData(false, true)), new FireBossAttackKeyframe(30, new Vec3(0.0, 0.1, 1.85), new Vec3(0.0, 0.3, 0.8), new FireBossAttackKeyframe.SwingData(false, false))).build())).setComboChance(1.0f).setMeleeAttackInverval(10, 30).setMeleeBias(1.0f, 1.0f).setSpells((List)List.of(SpellRegistry.FIRE_ARROW_SPELL.get(), SpellRegistry.FIRE_ARROW_SPELL.get(), SpellRegistry.SCORCH_SPELL.get()), List.of(), List.of(), List.of());
        this.goalSelector.addGoal(2, (Goal)new FieryDaggerSwarmAbilityGoal(this));
        this.goalSelector.addGoal(2, (Goal)new FieryDaggerZoneAbilityGoal(this));
        this.goalSelector.addGoal(2, (Goal)new SpellBarrageGoal(this, SpellRegistry.RAISE_HELL_SPELL.get(), 5, 5, 80, 240, 1));
        this.goalSelector.addGoal(3, (Goal)this.attackGoal);
        this.goalSelector.addGoal(4, (Goal)new PatrolNearLocationGoal(this, 30.0f, 0.75));
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.targetSelector.addGoal(1, (Goal)new MomentHurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Pig.class, true));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, DeadKingBoss.class, true));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
    }

    public void triggerHalfHealthAttack() {
        this.hasPerformedHalfHealthAttack = true;
        this.halfHealthTimer = 235;
        this.castComplete();
        this.attackGoal.stopMeleeAction();
        this.attackGoal.fireballcooldown = 200;
        this.serverTriggerEvent((byte)2);
        this.serverTriggerAnimation("fire_boss_half_health_attack");
        this.playSound((SoundEvent)SoundRegistry.BOSS_STANCE_BREAK.get(), 5.0f, 2.0f);
    }

    public void stopHalfHealthAttack() {
        this.halfHealthTimer = 0;
        this.setNoGravity(false);
        this.serverTriggerEvent((byte)3);
    }

    public boolean isHalfHealthAttacking() {
        return this.halfHealthTimer > 0;
    }

    public void triggerSpawnAnim() {
        this.spawnTimer = 215;
    }

    public void triggerStanceBreak() {
        ++this.stanceBreakCounter;
        this.stanceBreakTimer = 180;
        this.castComplete();
        this.attackGoal.stopMeleeAction();
        this.stopHalfHealthAttack();
        this.serverTriggerAnimation("fire_boss_break_stance");
        this.playSound((SoundEvent)SoundRegistry.BOSS_STANCE_BREAK.get(), 3.0f, 1.0f);
        Vec3 vec3 = this.getBoundingBox().getCenter();
        MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(), vec3.x, vec3.y, vec3.z, 25, 0.2, 0.2, 0.2, 0.12, false);
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return (SoundEvent)SoundRegistry.FIRE_BOSS_HURT.get();
    }

    public boolean isStanceBroken() {
        return this.stanceBreakTimer > 0;
    }

    public boolean isSpawning() {
        return this.spawnTimer > 0;
    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.isStanceBroken() || this.isSpawning() || this.isHalfHealthAttacking();
    }

    public boolean isInvulnerableTo(DamageSource pSource) {
        return this.isSpawning() || this.isDespawning() || super.isInvulnerableTo(pSource);
    }

    public boolean requiresCustomPersistence() {
        return true;
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        this.setLeftHanded(false);
        this.getAttribute((Holder)AttributeRegistry.MAX_MANA).addOrReplacePermanentModifier(MANA_MODIFIER);
        this.playerScale = pLevel.players().stream().filter(player -> this.distanceToSqr((Entity)player) < 3600.0 && !player.isSpectator() && !player.isCreative()).toList().size();
        int extraPlayers = Math.max(0, this.playerScale - 1);
        double extraHealthPercent = (double)extraPlayers * 0.4 + (double)(extraPlayers * extraPlayers) * 0.1;
        double extraHealth = (Double)ServerConfigs.TYROS_ADDITIONAL_HEALTH.get();
        double extraDamage = (Double)ServerConfigs.TYROS_ADDITIONAL_ATTACK_DAMAGE.get();
        double extraPower = (Double)ServerConfigs.TYROS_ADDITIONAL_SPELL_POWER.get();
        if (extraHealth != 0.0) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(IronsSpellbooks.id("config"), extraHealth, AttributeModifier.Operation.ADD_VALUE));
        }
        if (extraHealthPercent != 0.0) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(IronsSpellbooks.id("player_scale"), extraHealthPercent, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        if (extraDamage != 0.0) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(IronsSpellbooks.id("config"), extraDamage, AttributeModifier.Operation.ADD_VALUE));
        }
        if (extraPower != 0.0) {
            this.getAttribute((Holder)AttributeRegistry.SPELL_POWER).addPermanentModifier(new AttributeModifier(IronsSpellbooks.id("config"), extraPower, AttributeModifier.Operation.ADD_VALUE));
        }
        this.setHealth(this.getMaxHealth());
        return pSpawnData;
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(this.isSoulMode() ? ItemRegistry.HELLRAZOR : ItemRegistry.DECREPIT_SCYTHE));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }

    public void procSpectralDagger() {
        if (!this.level.isClientSide) {
            this.serverTriggerEvent((byte)6);
        } else {
            this.clientDaggerParticles = true;
        }
        this.daggerTime = 15;
    }

    public boolean spectralDaggerActive() {
        return this.daggerTime > 0;
    }

    public void tick() {
        super.tick();
        float maxHealth = this.getMaxHealth();
        float currentHealth = this.getHealth();
        this.bossEvent.setProgress(currentHealth / maxHealth);
        if (this.daggerTime > 0) {
            --this.daggerTime;
        }
        if (this.parryCooldown > 0) {
            --this.parryCooldown;
        }
        if (this.isSpawning()) {
            --this.spawnTimer;
            this.handleSpawnSequence();
            if (this.spawnTimer == 0 && !this.level.isClientSide) {
                this.spawnKnight(true);
                this.spawnKnight(false);
            }
        } else if (this.isDespawning()) {
            ++this.deathTime;
            if (!this.level.isClientSide) {
                this.deathParticles();
                if (this.getTarget() != null) {
                    this.setDespawning(false);
                }
                if (this.deathTime > 160) {
                    this.doForcedDespawned();
                }
            }
        } else if (this.deathTime > 0 && !this.isDeadOrDying()) {
            this.deathTime = Math.max(0, this.deathTime - 3);
        } else if (this.isHalfHealthAttacking()) {
            --this.halfHealthTimer;
            if (!this.level.isClientSide) {
                this.handleHalfHealthSequence();
            }
        }
        if (!this.level.isClientSide) {
            if (this.isStanceBroken()) {
                --this.stanceBreakTimer;
                this.handleStanceBreakSequence();
            }
            if (this.isSoulMode() && !this.dead) {
                this.soulParticles();
            }
        }
        if (this.destroyBlockDelay > 0) {
            --this.destroyBlockDelay;
        }
        if (this.stuckDetectorDelay > 0) {
            --this.stuckDetectorDelay;
        }
    }

    @Override
    protected void customServerAiStep() {
        int knightCount;
        super.customServerAiStep();
        float maxHealth = this.getMaxHealth();
        float currentHealth = this.getHealth();
        if (this.stanceBreakCounter == 0) {
            if (currentHealth < maxHealth * 0.75f) {
                this.triggerStanceBreak();
                return;
            }
        } else if (this.stanceBreakCounter == 1 && currentHealth < maxHealth * 0.333f) {
            this.triggerStanceBreak();
            return;
        }
        if (!this.hasPerformedHalfHealthAttack && currentHealth < maxHealth * 0.5f) {
            this.triggerHalfHealthAttack();
        }
        if (this.tickCount > 400 && !this.isDespawning() && this.getTarget() == null && this.tickCount - this.getLastHurtByMobTimestamp() > 200) {
            if (this.tickCount % 20 == 0) {
                this.heal(5.0f);
            }
            if (this.despawnAggroDelay++ > 1200) {
                this.setDespawning(true);
                this.level.playSound(null, this.blockPosition(), (SoundEvent)SoundRegistry.FIRE_BOSS_ACCENT.get(), SoundSource.HOSTILE, 4.0f, 0.75f);
            }
        }
        if (this.isAggressive() && this.tickCount % 240 == 0 && (knightCount = this.level.getEntitiesOfClass(KeeperEntity.class, this.getBoundingBox().inflate(50.0, 20.0, 50.0)).size()) < 2 + Math.max(this.playerScale - 1, 0) / 2) {
            this.spawnKnight(this.random.nextBoolean());
        }
    }

    public boolean doHurtTarget(Entity entity) {
        if (super.doHurtTarget(entity)) {
            entity.igniteForSeconds(2.0f);
            return true;
        }
        return false;
    }

    private void handleHalfHealthSequence() {
        if (this.level.isClientSide) {
            return;
        }
        this.targetSelector.tick();
        if (this.getTarget() != null) {
            this.lookControl.setLookAt((Entity)this.getTarget());
        }
        this.lookControl.tick();
        if (this.halfHealthDamageAccumulated > this.getMaxHealth() * 0.1f) {
            PacketDistributor.sendToPlayersTrackingEntity((Entity)this, (CustomPacketPayload)new FieryExplosionParticlesPacket(this.getBoundingBox().getCenter(), 10.0f), (CustomPacketPayload[])new CustomPacketPayload[0]);
            this.setHealth(Math.max(10.0f, Math.min(this.getHealth(), this.getMaxHealth() * 0.33f - 1.0f)));
            this.stopHalfHealthAttack();
            return;
        }
        int tick = 235 - this.halfHealthTimer;
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.1, 1.0, 0.1));
        if (tick == 11) {
            this.setDeltaMovement(0.0, 0.75, 0.0);
        } else if (tick > 11 && tick < 230) {
            if (tick == 31) {
                this.setNoGravity(true);
            }
            if (tick % 5 == 0) {
                int targetHeight = 8;
                double groundY = Utils.raycastForBlock((Level)this.level, (Vec3)this.position(), (Vec3)this.position().subtract((double)0.0, (double)((double)(targetHeight + 1)), (double)0.0), (ClipContext.Fluid)ClipContext.Fluid.NONE).getLocation().y;
                this.push(0.0, this.getY() - groundY > (double)targetHeight ? -0.02 : 0.02, 0.0);
            }
            Vec3 vec3 = this.position().add(0.0, this.getBoundingBox().getYsize() * 1.25, 0.0);
            MagicManager.spawnParticles(this.level, ParticleHelper.FIRE_EMITTER, vec3.x, vec3.y, vec3.z, 1, 0.1, 0.1, 0.1, 0.03, true);
            if (tick % 10 == 0) {
                float pitch = Mth.lerp((float)((float)tick / 235.0f), (float)0.5f, (float)1.8f);
                this.playSound((SoundEvent)SoundRegistry.SCORCH_PREPARE.get(), 2.0f + pitch, pitch);
            }
        } else if (tick == 230) {
            this.setNoGravity(false);
            MagicFireball fireball = new MagicFireball(this.level, (LivingEntity)this);
            fireball.setDamage((float)(this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 12.0));
            fireball.setExplosionRadius(30.0f);
            Vec3 origin = this.position().subtract(0.0, (double)(fireball.getBbHeight() / 2.0f), 0.0).add(0.0, this.getBoundingBox().getYsize() * 1.25, 0.0);
            Vec3 trajectory = this.getTarget() == null ? this.getForward() : this.getTarget().position().subtract(origin).normalize();
            fireball.setPos(origin);
            fireball.shoot(trajectory);
            this.level.addFreshEntity((Entity)fireball);
            this.playSound((SoundEvent)SoundRegistry.FIRE_BOSS_FIREBALL.get(), 4.0f, 1.0f);
        }
    }

    private void handleStanceBreakSequence() {
        int tick = 180 - this.stanceBreakTimer;
        if (this.stanceBreakCounter == 2) {
            if (tick == 80) {
                this.setSoulMode(true);
                Vec3 vec3 = this.getBoundingBox().getCenter();
                MagicManager.spawnParticles(this.level, ParticleHelper.FIRE, vec3.x, vec3.y, vec3.z, 120, 0.3, 0.3, 0.3, 0.3, true);
                AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
                speed.removeModifier(SOUL_SPEED_MODIFIER);
                speed.addPermanentModifier(SOUL_SPEED_MODIFIER);
                AttributeInstance scale = this.getAttribute(Attributes.SCALE);
                scale.removeModifier(SOUL_SCALE_MODIFIER);
                scale.addPermanentModifier(SOUL_SCALE_MODIFIER);
                this.playSound((SoundEvent)SoundRegistry.FIRE_BOSS_TRANSITION_SOUL.get(), 3.0f, 1.0f);
                if (this.getItemBySlot(EquipmentSlot.MAINHAND).is(ItemRegistry.DECREPIT_SCYTHE)) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.HELLRAZOR, 1, this.getItemBySlot(EquipmentSlot.MAINHAND).getComponentsPatch()));
                }
            } else if (tick < 80) {
                double f = Mth.lerp((double)((float)tick / 80.0f), (double)0.2, (double)0.4);
                Vec3 vec3 = this.getBoundingBox().getCenter();
                MagicManager.spawnParticles(this.level, ParticleHelper.FIRE, vec3.x, vec3.y, vec3.z, 12 + (int)(f * 10.0), f, f, f, 0.02, true);
            }
        }
        if (tick >= 130) {
            if (tick == 130) {
                this.createEruptionEntity(8.0f, (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                this.playSound((SoundEvent)SoundRegistry.FIRE_ERUPTION_SLAM.get(), 2.0f, 1.2f);
            } else if (tick == 155) {
                this.createEruptionEntity(11.0f, (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.0f);
                this.playSound((SoundEvent)SoundRegistry.FIRE_ERUPTION_SLAM.get(), 3.0f, 1.0f);
            } else if (tick == 180) {
                this.createEruptionEntity(15.0f, (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 3.0f);
                this.playSound((SoundEvent)SoundRegistry.FIRE_ERUPTION_SLAM.get(), 4.0f, 0.9f);
            }
        }
    }

    private void handleSpawnSequence() {
        int animProgress = 215 - this.spawnTimer;
        float walkProgress = this.getSpawnWalkPercent(0.0f);
        float worldZOffset = Mth.lerp((float)walkProgress, (float)(-3.75f * this.getScale()), (float)0.0f);
        Vec3 position = this.position().add(new Vec3(0.0, 0.0, (double)worldZOffset).yRot(-this.getYRot() * ((float)Math.PI / 180)));
        if (!this.level.isClientSide && animProgress == 65) {
            this.serverTriggerEvent((byte)4);
        }
        if (animProgress == 40) {
            if (!this.level.isClientSide) {
                MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, position.x, position.y + 1.2, position.z, (int)(165.0f * this.getScale()), 0.4 * (double)this.getScale(), 1.0 * (double)this.getScale(), 0.4 * (double)this.getScale(), 0.01, true);
                MagicManager.spawnParticles(this.level, ParticleHelper.FOG_CAMPFIRE_SMOKE, position.x, position.y + 0.1, position.z, 6, 0.6, 0.1, 0.6, 0.05, true);
                MagicManager.spawnParticles(this.level, new BlastwaveParticleOptions(1.0f, 0.6f, 0.3f, 8.0f), position.x, position.y, position.z, 0, 0.0, 0.0, 0.0, 0.0, true);
                this.serverTriggerAnimation("fire_boss_spawn");
            }
            this.level.playSound(null, position.x, position.y, position.z, SoundRegistry.SOULCALLER_TOLL_SUCCESS, SoundSource.PLAYERS, 5.0f, 0.75f);
        }
        if (animProgress == 60 || animProgress == 80 || animProgress == 100 || animProgress == 120 || animProgress == 140 || animProgress == 154 || animProgress == 168) {
            this.level.playSound(null, position.x, position.y, position.z, SoundRegistry.KEEPER_STEP, this.getSoundSource(), 0.4f, 1.0f);
        }
        if (animProgress == 155) {
            this.level.playSound(null, position.x, position.y, position.z, SoundRegistry.FIRE_BOSS_SUMMON_SCYTHE, this.getSoundSource(), 3.0f, 1.0f);
        }
    }

    protected float getSpawnWalkPercent(float partialTick) {
        return Math.clamp((float)(((float)(175 - this.spawnTimer) + partialTick) / 175.0f), (float)0.0f, (float)1.0f);
    }

    private void doForcedDespawned() {
        this.playSound((SoundEvent)SoundRegistry.FIRE_BOSS_ACCENT.get(), 5.0f, 1.0f);
        Vec3 vec3 = this.getBoundingBox().getCenter();
        MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(), vec3.x, vec3.y, vec3.z, 25, 0.2, 0.2, 0.2, 0.12, false);
        this.killNearbySummonedKnights();
        this.remove(Entity.RemovalReason.DISCARDED);
        IronsSpellbooks.LOGGER.info("{} despawned due to inactivity", (Object)this);
    }

    public void spawnKnight(boolean left) {
        Level level = this.level;
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            KeeperEntity knight = new KeeperEntity(this.level);
            float angle = (float)(left ? -90 : 90) * ((float)Math.PI / 180);
            Vec3 offset = this.getForward().multiply(3.0, 0.0, 3.0).scale((double)this.getScale()).yRot(angle);
            Vec3 spawn = Utils.moveToRelativeGroundLevel(this.level, Utils.raycastForBlock(this.level, this.getEyePosition(), this.position().add(offset), ClipContext.Fluid.NONE).getLocation(), 4);
            knight.moveTo(spawn.add(0.0, 0.1, 0.0));
            knight.triggerRise();
            knight.setYRot(this.getYRot());
            knight.setIsSummoned();
            if (this.isSoulMode()) {
                knight.setIsRestored();
            }
            knight.finalizeSpawn((ServerLevelAccessor)serverLevel, this.level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
            this.level.addFreshEntity((Entity)knight);
            this.level.playSound(null, spawn.x, spawn.y, spawn.z, (SoundEvent)SoundRegistry.FIRE_BOSS_ACCENT.get(), this.getSoundSource(), 2.0f, 0.9f);
        }
    }

    public void soulParticles() {
        Vec3 vec3 = this.getBoundingBox().getCenter();
        MagicManager.spawnParticles(this.level, ParticleHelper.FIRE, vec3.x, vec3.y, vec3.z, 2, 0.2, 0.6, 0.2, 0.01, true);
    }

    private void createEruptionEntity(float radius, float damage) {
        Vec3 forward = this.getForward().multiply(1.0, 0.0, 1.0).normalize().scale(3.0);
        Vec3 pos = Utils.moveToRelativeGroundLevel(this.level, this.position().add(forward).add(0.0, 1.0, 0.0), 4);
        FireEruptionAoe aoe = new FireEruptionAoe(this.level, radius);
        aoe.setOwner((Entity)this);
        aoe.setDamage(damage);
        aoe.moveTo(pos);
        this.level.addFreshEntity((Entity)aoe);
        CameraShakeManager.addCameraShake(new CameraShakeData(this.level, 20 + (int)radius, pos, radius * 2.0f + 5.0f));
    }

    public void kill() {
        if (this.isDeadOrDying() || this.isSpawning()) {
            this.discard();
        } else {
            super.kill();
        }
    }

    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
        if (this.isDeadOrDying() && !this.level.isClientSide) {
            this.stanceBreakTimer = 0;
            this.castComplete();
            this.attackGoal.stop();
            this.serverTriggerAnimation("fire_boss_death");
            this.serverTriggerEvent((byte)5);
            this.playSound((SoundEvent)SoundRegistry.FIRE_BOSS_DEATH.get(), 5.0f, 1.0f);
            Vec3 vec3 = this.getBoundingBox().getCenter();
            MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(), vec3.x, vec3.y, vec3.z, 25, 0.2, 0.2, 0.2, 0.12, false);
            this.killNearbySummonedKnights();
        }
    }

    private void killNearbySummonedKnights() {
        this.level.getEntitiesOfClass(KeeperEntity.class, this.getBoundingBox().inflate(50.0, 20.0, 50.0)).stream().filter(KeeperEntity::isSummoned).forEach(LivingEntity::kill);
    }

    protected void dropAllDeathLoot(ServerLevel pLevel, DamageSource pDamageSource) {
        this.dropEquipment();
        this.dropExperience(pDamageSource.getEntity());
        boolean playerDeath = this.lastHurtByPlayerTime > 0;
        this.dropCustomDeathLoot(pLevel, pDamageSource, playerDeath);
        ResourceKey resourcekey = this.getLootTable();
        LootTable mainLoot = this.level.getServer().reloadableRegistries().getLootTable(resourcekey);
        LootTable lootPerPlayer = this.level.getServer().reloadableRegistries().getLootTable(ResourceKey.create((ResourceKey)resourcekey.registryKey(), (ResourceLocation)resourcekey.location().withSuffix("_per_player")));
        LootParams.Builder lootparams$builder = new LootParams.Builder(pLevel).withParameter(LootContextParams.THIS_ENTITY, (Object)this).withParameter(LootContextParams.ORIGIN, (Object)this.position()).withParameter(LootContextParams.DAMAGE_SOURCE, (Object)pDamageSource).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, (Object)pDamageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, (Object)pDamageSource.getDirectEntity());
        if (playerDeath && this.lastHurtByPlayer != null) {
            lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, (Object)this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }
        LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
        ObjectArrayList objectarraylist = new ObjectArrayList();
        mainLoot.getRandomItems(lootparams, this.getLootTableSeed(), arg_0 -> ((ObjectArrayList)objectarraylist).add(arg_0));
        for (int i = 0; i < this.playerScale; ++i) {
            lootPerPlayer.getRandomItems(lootparams, this.getLootTableSeed(), arg_0 -> ((ObjectArrayList)objectarraylist).add(arg_0));
        }
        this.deathLoot = new SimpleContainer(objectarraylist.size());
        objectarraylist.forEach(arg_0 -> ((SimpleContainer)this.deathLoot).addItem(arg_0));
    }

    protected void tickDeath() {
        ++this.deathTime;
        if (!this.level.isClientSide) {
            float scale = this.getScale();
            Vec3 vec3 = this.position();
            this.deathParticles();
            if (this.deathTime >= 160 && !this.level().isClientSide() && !this.isRemoved()) {
                if (this.deathLoot != null) {
                    this.deathLoot.getItems().forEach(arg_0 -> ((FireBossEntity)this).spawnAtLocation(arg_0));
                }
                this.remove(Entity.RemovalReason.KILLED);
                MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(), vec3.x, vec3.y + 1.0, vec3.z, 50, 0.3, 0.3, 0.3, 0.2 * (double)scale, true);
                this.playSound((SoundEvent)SoundRegistry.FIRE_BOSS_ACCENT.get(), 4.0f, 0.9f);
            }
        }
    }

    private void deathParticles() {
        float scale = this.getScale();
        Vec3 vec3 = this.position();
        int particles = (int)Mth.lerp((float)Math.clamp((float)((float)(this.deathTime - 20) / 60.0f), (float)0.0f, (float)1.0f), (float)0.0f, (float)(5.0f * scale));
        float range = Mth.lerp((float)Math.clamp((float)((float)(this.deathTime - 20) / 80.0f), (float)0.0f, (float)1.0f), (float)0.0f, (float)(0.4f * scale));
        if (particles > 0) {
            MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleRegistry.EMBEROUS_ASH_PARTICLE.get(), vec3.x, vec3.y + 1.0, vec3.z, particles, range, range, range, 100.0, false);
        }
    }

    public void calculateEntityAnimation(boolean pIncludeHeight) {
        super.calculateEntityAnimation(false);
    }

    protected void updateWalkAnimation(float f) {
        super.updateWalkAnimation(f * (!this.onGround() ? 0.5f : (this.isSoulMode() ? 0.7f : 0.9f)));
    }

    @Override
    public boolean bobBodyWhileWalking() {
        return !this.isAnimating();
    }

    protected void playStepSound(BlockPos pPos, BlockState pState) {
        this.playSound((SoundEvent)SoundRegistry.KEEPER_STEP.get(), 0.25f, 0.9f);
    }

    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 10.0).add(AttributeRegistry.SPELL_POWER, 1.25).add(Attributes.ARMOR, 15.0).add(AttributeRegistry.SPELL_RESIST, 1.25).add(AttributeRegistry.FIRE_MAGIC_RESIST, 1.5).add(Attributes.MAX_HEALTH, 1000.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.8).add(Attributes.ATTACK_KNOCKBACK, 0.6).add(Attributes.FOLLOW_RANGE, 48.0).add(Attributes.SCALE, 1.75).add(Attributes.GRAVITY, 0.03).add(Attributes.ENTITY_INTERACTION_RANGE, 3.0).add(Attributes.STEP_HEIGHT, 1.0).add(Attributes.MOVEMENT_SPEED, 0.21);
    }

    public void push(Entity pEntity) {
        if (!this.isSpawning()) {
            super.push(pEntity);
        }
    }

    public void knockback(double pStrength, double pX, double pZ) {
        if (this.isStanceBroken()) {
            return;
        }
        super.knockback(pStrength, pX, pZ);
    }

    public boolean isPushable() {
        return super.isPushable() && !this.isImmobile();
    }

    @Override
    public void playAnimation(String animationId) {
        this.animationToPlay = RawAnimation.begin().thenPlay(animationId);
        this.canAnimateOver = animationId.equals("fire_boss_spawn") || animationId.equals("summon_fiery_daggers");
        this.stopHeadAnimation = animationId.equals("fire_boss_break_stance") || animationId.equals("fire_boss_death");
    }

    @Override
    public boolean shouldAlwaysAnimateHead() {
        return !this.stopHeadAnimation;
    }

    private PlayState predicate(AnimationState<FireBossEntity> animationEvent) {
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
        return this.meleeController.getAnimationState() == AnimationController.State.RUNNING && !this.canAnimateOver || super.isAnimating();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        float limit;
        boolean canParry;
        if (this.level.isClientSide) {
            return false;
        }
        boolean bl = canParry = this.isAggressive() && this.parryCooldown <= 0 && !this.isImmobile() && !this.attackGoal.isActing() && pSource.getEntity() != null && pSource.getSourcePosition() != null && pSource.getSourcePosition().subtract(this.position()).normalize().dot(this.getForward()) >= 0.35 && !pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        if (canParry && (double)this.random.nextFloat() < 0.5) {
            this.serverTriggerAnimation("offhand_parry");
            this.procSpectralDagger();
            this.parryCooldown = 100;
            this.playSound((SoundEvent)SoundRegistry.FIRE_DAGGER_PARRY.get());
            return false;
        }
        if (this.isStanceBroken()) {
            pAmount *= 0.6f;
        }
        if (this.isSoulMode()) {
            pAmount *= 0.5f;
        }
        if (this.isHalfHealthAttacking()) {
            pAmount *= 0.8f;
        }
        if (pAmount > (limit = this.getMaxHealth() * 0.025f)) {
            pAmount = limit + (pAmount - limit) * 0.3f;
        }
        if (pSource.is(DamageTypes.IN_WALL) && this.destroyBlockDelay <= 0) {
            Utils.doMobBreakSuffocatingBlocks((LivingEntity)this);
            this.destroyBlockDelay = 40;
        }
        return super.hurt(pSource, pAmount);
    }

    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        super.actuallyHurt(damageSource, damageAmount);
        if (this.isHalfHealthAttacking()) {
            this.halfHealthDamageAccumulated += damageAmount;
        }
        Vec3 oldStuckPos = this.lastStuckPos;
        this.lastStuckPos = this.position();
        if (this.stuckDetectorDelay <= 0) {
            if (oldStuckPos.distanceToSqr(this.lastStuckPos) < 9.0 && !this.isImmobile()) {
                this.stuckDetectorDelay = 20;
                if (this.horizontalCollision) {
                    ++this.stuckDetector;
                }
            } else {
                this.stuckDetector = 0;
            }
        }
        if (this.stuckDetector >= 3 && this.destroyBlockDelay <= 0) {
            Utils.doMobBreakSuffocatingBlocks((LivingEntity)this, this.getForward().scale(1.5));
            this.stuckDetector = 0;
            this.destroyBlockDelay = 40;
        }
    }

    public boolean isSoulMode() {
        return (Boolean)this.entityData.get(DATA_SOUL_MODE);
    }

    public void setSoulMode(boolean soulMode) {
        this.entityData.set(DATA_SOUL_MODE, (Object)soulMode);
    }

    public boolean isDespawning() {
        return (Boolean)this.entityData.get(DATA_IS_DESPAWNING);
    }

    public void setDespawning(boolean despawning) {
        this.entityData.set(DATA_IS_DESPAWNING, (Object)despawning);
        if (!despawning) {
            this.despawnAggroDelay = 0;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("stanceBreakCount", this.stanceBreakCounter);
        pCompound.putInt("playerScale", this.playerScale);
        if (this.stanceBreakTimer > 0) {
            pCompound.putInt("stanceBreakTime", this.stanceBreakTimer);
        }
        pCompound.putBoolean("soulMode", this.isSoulMode());
        if (this.deathLoot != null) {
            pCompound.put("deathLootItems", (Tag)this.deathLoot.createTag((HolderLookup.Provider)this.registryAccess()));
        }
        pCompound.putLong("unloadedGametime", this.level.getGameTime());
        pCompound.putInt("halfHealthTimer", this.halfHealthTimer);
        pCompound.putFloat("halfHealthDamage", this.halfHealthDamageAccumulated);
        pCompound.putBoolean("halfHealthAttack", this.hasPerformedHalfHealthAttack);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        int stanceTime;
        super.readAdditionalSaveData(pCompound);
        this.stanceBreakCounter = pCompound.getInt("stanceBreakCount");
        this.playerScale = pCompound.getInt("playerScale");
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
        if ((stanceTime = pCompound.getInt("stanceBreakTime")) > 0) {
            this.stanceBreakTimer = stanceTime;
            if (this.level.isClientSide) {
                this.animationToPlay = RawAnimation.begin().thenPlay("fire_boss_break_stance");
            }
        }
        this.setSoulMode(pCompound.getBoolean("soulMode"));
        if (pCompound.contains("deathLootItems", 9)) {
            ListTag tag = pCompound.getList("deathLootItems", 10);
            this.deathLoot = new SimpleContainer(tag.size());
            this.deathLoot.fromTag(tag, (HolderLookup.Provider)this.registryAccess());
        }
        this.halfHealthTimer = pCompound.getInt("halfHealthTimer");
        this.halfHealthDamageAccumulated = pCompound.getFloat("halfHealthDamage");
        this.hasPerformedHalfHealthAttack = pCompound.getBoolean("halfHealthAttack");
    }

    public void load(CompoundTag pCompound) {
        if (pCompound.contains("unloadedGametime", 99)) {
            long unloadTimestamp = pCompound.getLong("unloadedGametime");
            long delta = this.level.getGameTime() - unloadTimestamp;
            if (delta > 6000L) {
                this.setRemoved(Entity.RemovalReason.DISCARDED);
                IronsSpellbooks.LOGGER.info("Refusing to load {}, elapsed time {} greater than limit {}", new Object[]{this, delta, 6000});
                return;
            }
        }
        super.load(pCompound);
        if (!this.level.isClientSide) {
            this.createBossEvent();
        }
    }

    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || pEntity.getType().is(ModTags.INFERNAL_ALLIES);
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new NotIdioticNavigation((Mob)this, pLevel);
    }

    protected void createBossEvent() {
        this.bossEvent = (ExtendedServerBossEvent)new ExtendedServerBossEvent(this.getUUID(), (Component)this.getDisplayName().copy().withStyle(ChatFormatting.RED), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setCreateWorldFog(true);
    }
}

