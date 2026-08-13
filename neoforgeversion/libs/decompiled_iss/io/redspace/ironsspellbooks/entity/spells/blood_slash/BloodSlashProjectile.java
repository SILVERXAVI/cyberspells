/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.blood_slash;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BloodSlashProjectile
extends Projectile
implements AntiMagicSusceptible {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(BloodSlashProjectile.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final double SPEED = 1.0;
    private static final int EXPIRE_TIME = 80;
    public final int animationSeed = Utils.random.nextInt(9999);
    private final float maxRadius;
    public AABB oldBB;
    private int age;
    private float damage;
    public int animationTime;
    private List<Entity> victims;

    public BloodSlashProjectile(EntityType<? extends BloodSlashProjectile> entityType, Level level) {
        super(entityType, level);
        this.setRadius(0.6f);
        this.maxRadius = 3.0f;
        this.oldBB = this.getBoundingBox();
        this.victims = new ArrayList<Entity>();
        this.setNoGravity(true);
    }

    public BloodSlashProjectile(EntityType<? extends BloodSlashProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.setOwner((Entity)shooter);
        this.setYRot(shooter.getYRot());
        this.setXRot(shooter.getXRot());
    }

    public BloodSlashProjectile(Level levelIn, LivingEntity shooter) {
        this((EntityType<? extends BloodSlashProjectile>)((EntityType)EntityRegistry.BLOOD_SLASH_PROJECTILE.get()), levelIn, shooter);
    }

    public void shoot(Vec3 rotation) {
        this.setDeltaMovement(rotation.scale(1.0));
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_RADIUS, (Object)Float.valueOf(0.5f));
    }

    public void setRadius(float newRadius) {
        if (newRadius <= this.maxRadius && !this.level.isClientSide) {
            this.getEntityData().set(DATA_RADIUS, (Object)Float.valueOf(Mth.clamp((float)newRadius, (float)0.0f, (float)this.maxRadius)));
        }
    }

    public float getRadius() {
        return ((Float)this.getEntityData().get(DATA_RADIUS)).floatValue();
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public void tick() {
        super.tick();
        if (++this.age > 80) {
            this.discard();
            return;
        }
        this.oldBB = this.getBoundingBox();
        this.setRadius(this.getRadius() + 0.12f);
        if (!this.level.isClientSide) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector((Entity)this, this::canHitEntity);
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                this.onHitBlock((BlockHitResult)hitresult);
            }
            for (Entity entity : this.level.getEntities((Entity)this, this.getBoundingBox()).stream().filter(target -> this.canHitEntity((Entity)target) && !this.victims.contains(target)).collect(Collectors.toSet())) {
                this.damageEntity(entity);
                MagicManager.spawnParticles(this.level, ParticleHelper.BLOOD, entity.getX(), entity.getY(), entity.getZ(), 50, 0.0, 0.0, 0.0, 0.5, true);
                if (!(entity instanceof ShieldPart) && !(entity instanceof AbstractShieldEntity)) continue;
                this.discard();
                return;
            }
        }
        this.setPos(this.position().add(this.getDeltaMovement()));
        this.spawnParticles();
    }

    public EntityDimensions getDimensions(Pose p_19721_) {
        this.getBoundingBox();
        return EntityDimensions.scalable((float)(this.getRadius() * 2.0f), (float)0.5f);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_19729_) {
        if (DATA_RADIUS.equals(p_19729_)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(p_19729_);
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.discard();
    }

    private void damageEntity(Entity entity) {
        if (!this.victims.contains(entity)) {
            DamageSources.applyDamage(entity, this.damage, SpellRegistry.BLOOD_SLASH_SPELL.get().getDamageSource((Entity)this, this.getOwner()));
            this.victims.add(entity);
        }
    }

    public void spawnParticles() {
        if (this.level.isClientSide) {
            float width = (float)this.getBoundingBox().getXsize();
            float step = 0.25f;
            float radians = (float)Math.PI / 180 * this.getYRot();
            float speed = 0.1f;
            int i = 0;
            while ((float)i < width / step) {
                double x = this.getX();
                double y = this.getY();
                double z = this.getZ();
                double offset = step * ((float)i - width / step / 2.0f);
                double rotX = offset * Math.cos(radians);
                double rotZ = -offset * Math.sin(radians);
                double dx = Math.random() * (double)speed * 2.0 - (double)speed;
                double dy = Math.random() * (double)speed * 2.0 - (double)speed;
                double dz = Math.random() * (double)speed * 2.0 - (double)speed;
                this.level.addParticle(ParticleHelper.BLOOD, false, x + rotX + dx, y + dy, z + rotZ + dz, dx, dy, dz);
                ++i;
            }
        }
    }

    protected boolean canHitEntity(Entity entity) {
        return entity != this.getOwner() && super.canHitEntity(entity);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.discard();
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("Damage", this.damage);
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.damage = pCompound.getFloat("Damage");
    }
}

