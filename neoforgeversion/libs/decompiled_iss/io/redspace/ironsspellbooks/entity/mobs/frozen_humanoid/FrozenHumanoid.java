/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.WalkAnimationState
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.monster.Zombie
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.IEntityWithComplexSpawn
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.Nullable;

public class FrozenHumanoid
extends LivingEntity
implements IEntityWithComplexSpawn {
    protected static final EntityDataAccessor<Float> DATA_ATTACK_TIME = SynchedEntityData.defineId(FrozenHumanoid.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> DATA_IS_BABY = SynchedEntityData.defineId(FrozenHumanoid.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    float walkAnimSpeed;
    float walkAnimPos;
    private float shatterProjectileDamage;
    private int deathTimer = -1;
    private UUID summonerUUID;
    private LivingEntity cachedSummoner;
    @Nullable
    EntityType<?> entityToCopy;
    private HumanoidArm mainArm = HumanoidArm.RIGHT;

    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        LivingEntity owner = this.getSummoner();
        buffer.writeInt(owner == null ? -1 : owner.getId());
        if (this.entityToCopy == null) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            buffer.writeResourceLocation(BuiltInRegistries.ENTITY_TYPE.getKey(this.entityToCopy));
        }
    }

    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        Entity owner = this.level.getEntity(additionalData.readInt());
        if (owner instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)owner;
            this.setSummoner(livingEntity);
        }
        if (additionalData.readBoolean()) {
            this.setEntityTypeToCopy((EntityType)BuiltInRegistries.ENTITY_TYPE.get(additionalData.readResourceLocation()));
        }
    }

    public FrozenHumanoid(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.walkAnimation = new FakeWalkAnimationState();
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_ATTACK_TIME, (Object)Float.valueOf(0.0f));
        pBuilder.define(DATA_IS_BABY, (Object)false);
    }

    protected static void copyEntityVisualProperties(LivingEntity baseEntity, LivingEntity entityToCopy) {
        baseEntity.moveTo(entityToCopy.getX(), entityToCopy.getY(), entityToCopy.getZ(), entityToCopy.getYRot(), entityToCopy.getXRot());
        baseEntity.setYBodyRot(entityToCopy.yBodyRot);
        baseEntity.yBodyRotO = baseEntity.yBodyRot;
        baseEntity.setYHeadRot(entityToCopy.getYHeadRot());
        baseEntity.yHeadRotO = baseEntity.yHeadRot;
        baseEntity.setPose(entityToCopy.getPose());
        if (baseEntity instanceof FrozenHumanoid) {
            FrozenHumanoid frozenHumanoid = (FrozenHumanoid)baseEntity;
            frozenHumanoid.mainArm = entityToCopy.getMainArm();
            frozenHumanoid.getEntityData().set(DATA_ATTACK_TIME, (Object)Float.valueOf(entityToCopy.attackAnim));
            if (entityToCopy.isBaby()) {
                frozenHumanoid.getEntityData().set(DATA_IS_BABY, (Object)true);
            }
        } else if (baseEntity.level.isClientSide) {
            baseEntity.walkAnimation = entityToCopy.walkAnimation;
            baseEntity.attackAnim = entityToCopy.attackAnim;
            baseEntity.oAttackAnim = entityToCopy.attackAnim;
            if (entityToCopy.isBaby()) {
                if (baseEntity instanceof AgeableMob) {
                    AgeableMob ageableMob = (AgeableMob)baseEntity;
                    ageableMob.setAge(-10);
                } else if (baseEntity instanceof Zombie) {
                    Zombie zombie = (Zombie)baseEntity;
                    zombie.setBaby(true);
                }
            }
        }
        if (baseEntity.getAttributes().hasAttribute(Attributes.SCALE) && entityToCopy.getAttributes().hasAttribute(Attributes.SCALE)) {
            baseEntity.getAttributes().getInstance(Attributes.SCALE).setBaseValue(entityToCopy.getAttributeValue(Attributes.SCALE));
        }
        if (entityToCopy instanceof Player) {
            Player player = (Player)entityToCopy;
            baseEntity.setCustomName(player.getDisplayName());
            baseEntity.setCustomNameVisible(true);
        }
    }

    public boolean isBaby() {
        return (Boolean)this.entityData.get(DATA_IS_BABY);
    }

    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return this.entityToCopy == null ? super.getDefaultDimensions(pose) : this.entityToCopy.getDimensions();
    }

    public void setEntityTypeToCopy(@Nullable EntityType<?> entityToCopy) {
        this.entityToCopy = entityToCopy;
        this.refreshDimensions();
    }

    public FrozenHumanoid(Level level, LivingEntity entityToCopy) {
        this((EntityType<? extends LivingEntity>)((EntityType)EntityRegistry.FROZEN_HUMANOID.get()), level);
        FrozenHumanoid.copyEntityVisualProperties(this, entityToCopy);
        if (!(entityToCopy instanceof Player)) {
            this.setEntityTypeToCopy(entityToCopy.getType());
        }
        this.invulnerableTime = 1;
        this.setSummoner(entityToCopy);
    }

    public boolean canFreeze() {
        return false;
    }

    public void setTicksFrozen(int ticksFrozen) {
    }

    public void setSummoner(@javax.annotation.Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    public LivingEntity getSummoner() {
        if (this.cachedSummoner != null && this.cachedSummoner.isAlive()) {
            return this.cachedSummoner;
        }
        if (this.summonerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.summonerUUID);
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity;
                this.cachedSummoner = livingEntity = (LivingEntity)entity;
            }
            return this.cachedSummoner;
        }
        return null;
    }

    public float getWalkAnimSpeed() {
        return this.walkAnimSpeed;
    }

    public float getWalkAnimPos() {
        return this.walkAnimPos;
    }

    public void tick() {
        if (this.firstTick && this.level.isClientSide && this.cachedSummoner != null) {
            this.walkAnimSpeed = this.cachedSummoner.walkAnimation.speed();
            this.walkAnimPos = this.cachedSummoner.walkAnimation.position();
        }
        super.tick();
        if (this.deathTimer > 0) {
            --this.deathTimer;
        }
        if (this.deathTimer == 0) {
            this.hurt(this.level().damageSources().generic(), 100.0f);
        }
    }

    public void setDeathTimer(int timeInTicks) {
        this.deathTimer = timeInTicks;
    }

    public float getAttacktime() {
        return ((Float)this.entityData.get(DATA_ATTACK_TIME)).floatValue();
    }

    public boolean isPushable() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isPickable() {
        return true;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.GLASS_BREAK;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.GLASS_BREAK;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.level().isClientSide || this.isInvulnerableTo(pSource) || this.invulnerableTime > 0) {
            return false;
        }
        this.invulnerableTime = 10;
        this.doPuffDamage();
        this.spawnIcicleShards(this.getEyePosition(), this.shatterProjectileDamage);
        this.playHurtSound(pSource);
        this.discard();
        return true;
    }

    private void doPuffDamage() {
        float damage = this.shatterProjectileDamage * 0.5f;
        AABB collider = this.getBoundingBox().inflate(2.0);
        double radius = collider.getXsize();
        Vec3 center = collider.getCenter();
        List entities = this.level.getEntities((Entity)this, collider);
        for (Entity entity : entities) {
            double distanceSqr = entity.distanceToSqr(center);
            if (!(distanceSqr < radius * radius) || !entity.canBeHitByProjectile() || DamageSources.isFriendlyFireBetween(entity, (Entity)this.getSummoner()) || !Utils.hasLineOfSight(this.level, center, entity.getBoundingBox().getCenter(), true)) continue;
            DamageSources.applyDamage(entity, damage, SpellRegistry.ICICLE_SPELL.get().getDamageSource((Entity)this, (Entity)this.getSummoner()));
        }
        MagicManager.spawnParticles(this.level, ParticleHelper.SNOW_DUST, this.getX(), this.getY() + 1.0, this.getZ(), 50, 0.2, 0.2, 0.2, 0.2, false);
        MagicManager.spawnParticles(this.level, ParticleHelper.SNOWFLAKE, this.getX(), this.getY() + 1.0, this.getZ(), 50, 0.2, 0.2, 0.2, 0.2, false);
    }

    private void spawnIcicleShards(Vec3 origin, float damage) {
        int count = 8;
        int offset = 360 / count;
        for (int i = 0; i < count; ++i) {
            Vec3 motion = new Vec3(0.0, 0.0, 1.0);
            motion = motion.xRot(0.20943952f);
            motion = motion.yRot((float)(offset * i) * ((float)Math.PI / 180));
            IcicleProjectile shard = new IcicleProjectile(this.level(), this.getSummoner());
            shard.setDamage(damage);
            shard.setDeltaMovement(motion);
            shard.setNoGravity(false);
            Vec3 spawn = origin.add(motion.multiply(1.0, 0.0, 1.0).normalize().scale(0.5));
            Vec2 angle = Utils.rotationFromDirection(motion);
            shard.moveTo(spawn.x, spawn.y - shard.getBoundingBox().getYsize() / 2.0, spawn.z, angle.y, angle.x);
            this.level().addFreshEntity((Entity)shard);
        }
    }

    public void setShatterDamage(float damage) {
        this.shatterProjectileDamage = damage;
    }

    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.hasUUID("Summoner")) {
            this.summonerUUID = compoundTag.getUUID("Summoner");
        }
        if (compoundTag.contains("entityToCopy")) {
            try {
                this.setEntityTypeToCopy((EntityType)BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse((String)compoundTag.getString("entityToCopy"))));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        this.deathTimer = compoundTag.getInt("deathTimer");
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.summonerUUID != null) {
            compoundTag.putUUID("Summoner", this.summonerUUID);
        }
        if (this.entityToCopy != null) {
            compoundTag.putString("entityToCopy", BuiltInRegistries.ENTITY_TYPE.getKey(this.entityToCopy).toString());
        }
        compoundTag.putInt("deathTimer", this.deathTimer);
    }

    public HumanoidArm getMainArm() {
        return this.mainArm;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 0.0).add(Attributes.MAX_HEALTH, 1.0).add(Attributes.FOLLOW_RANGE, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 100.0).add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    private class FakeWalkAnimationState
    extends WalkAnimationState {
        private FakeWalkAnimationState() {
        }

        public float position() {
            return FrozenHumanoid.this.getWalkAnimPos();
        }

        public float position(float partialTick) {
            return FrozenHumanoid.this.getWalkAnimPos();
        }

        public float speed() {
            return FrozenHumanoid.this.getWalkAnimSpeed();
        }

        public float speed(float partialTick) {
            return FrozenHumanoid.this.getWalkAnimSpeed();
        }
    }
}

