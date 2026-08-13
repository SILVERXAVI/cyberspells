/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  net.neoforged.neoforge.entity.PartEntity
 */
package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.entity.PartEntity;

public abstract class AbstractShieldEntity
extends Entity
implements AntiMagicSusceptible {
    private static final EntityDataAccessor<Float> DATA_HEALTH_ID = SynchedEntityData.defineId(AbstractShieldEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    public boolean hurtThisTick;

    public AbstractShieldEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AbstractShieldEntity(Level level, float health) {
        this((EntityType)EntityRegistry.SHIELD_ENTITY.get(), level);
        this.setHealth(health);
    }

    protected abstract void createShield();

    public abstract void takeDamage(DamageSource var1, float var2, @Nullable Vec3 var3);

    public void tick() {
        this.hurtThisTick = false;
        for (PartEntity<?> subEntity : this.getParts()) {
            Vec3 pos = subEntity.position();
            subEntity.setPos(pos);
            subEntity.xo = pos.x;
            subEntity.yo = pos.y;
            subEntity.zo = pos.z;
            subEntity.xOld = pos.x;
            subEntity.yOld = pos.y;
            subEntity.zOld = pos.z;
        }
    }

    protected void destroy() {
        this.kill();
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public abstract PartEntity<?>[] getParts();

    public void setId(int id) {
        super.setId(id);
        PartEntity<?>[] subEntities = this.getParts();
        for (int i = 0; i < subEntities.length; ++i) {
            subEntities[i].setId(id + i + 1);
        }
    }

    public float getHealth() {
        return ((Float)this.entityData.get(DATA_HEALTH_ID)).floatValue();
    }

    public void setHealth(float pHealth) {
        this.entityData.set(DATA_HEALTH_ID, (Object)Float.valueOf(pHealth));
    }

    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_HEALTH_ID, (Object)Float.valueOf(1.0f));
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Health", 99)) {
            this.setHealth(pCompound.getFloat("Health"));
        }
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("Health", this.getHealth());
    }

    public List<VoxelShape> getVoxels() {
        ArrayList<VoxelShape> voxels = new ArrayList<VoxelShape>();
        for (PartEntity<?> shieldPart : this.getParts()) {
            voxels.add(Shapes.create((AABB)shieldPart.getBoundingBox()));
        }
        return voxels;
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.discard();
    }
}

