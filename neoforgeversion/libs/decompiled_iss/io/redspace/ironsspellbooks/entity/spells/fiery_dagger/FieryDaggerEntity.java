/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.IEntityWithComplexSpawn
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Vector3f
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package io.redspace.ironsspellbooks.entity.spells.fiery_dagger;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireField;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.NBT;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FieryDaggerEntity
extends AbstractMagicProjectile
implements IEntityWithComplexSpawn,
GeoAnimatable {
    public int delay;
    @Nullable
    public Vec3 ownerTrack = null;
    @Nullable
    private UUID targetEntity = null;
    @Nullable
    private Entity cachedTarget = null;
    int age;
    boolean isGrounded;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);

    public FieryDaggerEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public FieryDaggerEntity(Level level) {
        this((EntityType<? extends Projectile>)((EntityType)EntityRegistry.FIERY_DAGGER_PROJECTILE.get()), level);
    }

    public void setTarget(Entity target) {
        this.cachedTarget = target;
        this.targetEntity = target.getUUID();
    }

    public boolean isTrackingOwner() {
        return this.ownerTrack != null;
    }

    public boolean hasTarget() {
        return this.targetEntity != null;
    }

    public boolean isSpawnDagger() {
        return this.explosionRadius > 0.0f;
    }

    private void createFireField() {
        FireField fireField = new FireField(this.level);
        fireField.setOwner((Entity)this.level.getNearestEntity(FireBossEntity.class, TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(), null, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(32.0)));
        fireField.setPos(Utils.moveToRelativeGroundLevel(this.level, this.position(), 3));
        fireField.setRadius(this.explosionRadius + 1.0f);
        fireField.setCircular();
        fireField.setDamage(this.getDamage() * 0.5f);
        fireField.setDuration(300);
        fireField.setDelay(this.delay + 25);
        fireField.setRadiusPerTick(-fireField.getRadius() / (float)fireField.getDuration());
        this.level.addFreshEntity((Entity)fireField);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        entityHitResult.getEntity().hurt(new DamageSource((Holder)this.level.damageSources().damageTypes.getHolderOrThrow(ISSDamageTypes.FIRE_MAGIC), (Entity)this, this.getOwner()), this.getDamage());
        entityHitResult.getEntity().invulnerableTime = 0;
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        if (this.isSpawnDagger() && this.level instanceof ServerLevel) {
            this.createDaggerZone(Utils.moveToRelativeGroundLevel(this.level, hitresult.getLocation(), 3));
        }
        this.discardHelper(hitresult);
    }

    public void createDaggerZone(Vec3 center) {
        MagicManager.spawnParticles(this.level, new BlastwaveParticleOptions(new Vector3f(1.0f, 0.6f, 0.3f), this.explosionRadius + 1.0f), center.x, center.y + 0.15, center.z, 1, 0.0, 0.0, 0.0, 0.0, false);
        this.playSound((SoundEvent)SoundRegistry.FIRE_CAST.get(), 2.0f, (float)Utils.random.nextIntBetweenInclusive(80, 110) * 0.01f);
        float spawnRadius = this.explosionRadius;
        float density = 1.0f;
        int rings = (int)(spawnRadius * density);
        float ringSpacing = 1.0f / density;
        for (int i = 1; i < rings; ++i) {
            float ringRadius = ringSpacing * (float)i;
            int daggerCount = (int)(ringRadius * ((float)Math.PI * 2));
            float angle = 360.0f / (float)daggerCount * ((float)Math.PI / 180);
            for (int j = 0; j < daggerCount; ++j) {
                Vec3 jitter = Utils.getRandomVec3(ringSpacing * 0.4f);
                Vec3 pos = Utils.moveToRelativeGroundLevel(this.level, center.add((double)(ringRadius * Mth.sin((float)(angle * (float)j))), 0.0, (double)(ringRadius * Mth.cos((float)(angle * (float)j)))).add(jitter), 8);
                FieryDaggerEntity dagger = new FieryDaggerEntity(this.level);
                dagger.setOwner(this.getOwner());
                dagger.setDamage(this.getDamage());
                dagger.delay = this.delay + Utils.random.nextInt(20);
                dagger.setDeltaMovement(0.0, this.getSpeed(), 0.0);
                dagger.deltaMovementOld = dagger.getDeltaMovement();
                dagger.moveTo(pos);
                dagger.isGrounded = true;
                this.level.addFreshEntity((Entity)dagger);
            }
        }
        this.createFireField();
    }

    @Override
    public void tick() {
        if (!this.isSpawnDagger() && this.age++ < this.delay) {
            Entity target;
            Entity owner = this.getOwner();
            float strength = 0.5f;
            if (owner != null && this.isTrackingOwner()) {
                Vec3 ownerMotion = owner.position().subtract(owner.xOld, owner.yOld, owner.zOld);
                this.setPos(this.position().add(ownerMotion));
            }
            if ((target = this.getTargetEntity()) != null) {
                Vec3 currentMotion;
                Vec3 targetPos = target.getBoundingBox().getCenter();
                Vec3 targetMotion = targetPos.subtract(this.position()).normalize().scale((double)this.getSpeed());
                this.deltaMovementOld = currentMotion = this.getDeltaMovement();
                this.setDeltaMovement(currentMotion.add(targetMotion.subtract(currentMotion).scale((double)strength)));
                if (this.tickCount == 1) {
                    this.deltaMovementOld = this.getDeltaMovement();
                }
            }
            if (this.age == this.delay) {
                List hits;
                EntityHitResult hitResult;
                if (this.isGrounded) {
                    if (Utils.random.nextFloat() < 0.25f) {
                        this.playSound((SoundEvent)SoundRegistry.FIERY_DAGGER_THROW.get(), 0.75f, (float)Utils.random.nextIntBetweenInclusive(90, 110) * 0.01f);
                    }
                } else {
                    this.playSound((SoundEvent)SoundRegistry.FIERY_DAGGER_THROW.get(), 2.0f, (float)Utils.random.nextIntBetweenInclusive(90, 110) * 0.01f);
                }
                EntityHitResult entityHitResult = hitResult = (hits = this.level.getEntities((Entity)this, this.getBoundingBox().inflate((double)0.4f), this::canHitEntity)).isEmpty() ? null : new EntityHitResult((Entity)hits.getFirst());
                if (hitResult != null) {
                    this.onHit((HitResult)hitResult);
                }
            }
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleHelper.EMBERS, this.getX(), this.getY() + (double)(this.getBbHeight() * 0.5f), this.getZ(), 0.0, 0.0, 0.0);
            }
        } else {
            super.tick();
        }
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return !this.isSpawnDagger() && super.canHitEntity(pTarget);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level, (ParticleOptions)ParticleTypes.LAVA, x, y, z, 5, 0.1, 0.1, 0.1, 0.25, true);
    }

    @Override
    public void trailParticles() {
        float yHeading = -((float)(Mth.atan2((double)this.getDeltaMovement().z, (double)this.getDeltaMovement().x) * 57.2957763671875) + 90.0f);
        float radius = 0.25f;
        int steps = 2;
        Vec3 vec = this.getDeltaMovement();
        double x2 = this.getX();
        double x1 = x2 - vec.x;
        double y2 = this.getY();
        double y1 = y2 - vec.y;
        double z2 = this.getZ();
        double z1 = z2 - vec.z;
        for (int j = 0; j < steps; ++j) {
            float offset = 1.0f / (float)steps * (float)j;
            double radians = ((float)this.tickCount + offset) / 7.5f * 360.0f * ((float)Math.PI / 180);
            Vec3 swirl = new Vec3(Math.cos(radians) * (double)radius, Math.sin(radians) * (double)radius, 0.0).yRot(yHeading * ((float)Math.PI / 180));
            double x = Mth.lerp((double)offset, (double)x1, (double)x2) + swirl.x;
            double y = Mth.lerp((double)offset, (double)y1, (double)y2) + swirl.y + (double)(this.getBbHeight() / 2.0f);
            double z = Mth.lerp((double)offset, (double)z1, (double)z2) + swirl.z;
            Vec3 jitter = Vec3.ZERO;
            this.level.addParticle(ParticleHelper.EMBERS, x, y, z, jitter.x, jitter.y, jitter.z);
        }
    }

    @Override
    public float getSpeed() {
        return 1.25f;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return this.isGrounded ? Optional.empty() : Optional.of(SoundRegistry.FIRE_IMPACT);
    }

    public Entity getTargetEntity() {
        Level level;
        if (this.cachedTarget != null && this.cachedTarget.isAlive()) {
            return this.cachedTarget;
        }
        if (this.targetEntity != null && (level = this.level) instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.cachedTarget = serverLevel.getEntity(this.targetEntity);
            if (this.cachedTarget == null) {
                this.targetEntity = null;
            }
            return this.cachedTarget;
        }
        return null;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("delay", this.delay);
        if (this.ownerTrack != null) {
            tag.put("ownerTrack", (Tag)NBT.writeVec3Pos(this.ownerTrack));
        }
        if (this.targetEntity != null) {
            tag.putUUID("target", this.targetEntity);
        }
        tag.putInt("Age", this.age);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.delay = tag.getInt("delay");
        if (tag.hasUUID("ownerTrack")) {
            this.ownerTrack = NBT.readVec3(tag.getCompound("ownerTrack"));
        }
        if (tag.hasUUID("target")) {
            this.targetEntity = tag.getUUID("target");
        }
        this.age = tag.getInt("Age");
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.delay);
        buffer.writeFloat(this.explosionRadius);
        buffer.writeBoolean(this.isGrounded);
        boolean tracking = this.ownerTrack != null;
        buffer.writeBoolean(tracking);
        if (tracking) {
            buffer.writeDouble(this.ownerTrack.x);
            buffer.writeDouble(this.ownerTrack.y);
            buffer.writeDouble(this.ownerTrack.z);
        }
        boolean target = this.cachedTarget != null;
        buffer.writeBoolean(target);
        if (target) {
            buffer.writeInt(this.cachedTarget.getId());
        }
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
        this.delay = buffer.readInt();
        this.explosionRadius = buffer.readFloat();
        this.isGrounded = buffer.readBoolean();
        if (buffer.readBoolean()) {
            this.ownerTrack = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }
        if (buffer.readBoolean()) {
            this.cachedTarget = this.level.getEntity(buffer.readInt());
            if (this.cachedTarget != null) {
                this.targetEntity = this.cachedTarget.getUUID();
            }
        }
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public double getTick(Object object) {
        return this.tickCount;
    }
}

