/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.IEntityWithComplexSpawn
 */
package io.redspace.ironsspellbooks.entity.spells.ray_of_frost;

import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class RayOfFrostVisualEntity
extends Entity
implements IEntityWithComplexSpawn {
    public static final int lifetime = 15;
    public float distance;

    public RayOfFrostVisualEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RayOfFrostVisualEntity(Level level, Vec3 start, Vec3 end, LivingEntity owner) {
        super((EntityType)EntityRegistry.RAY_OF_FROST_VISUAL_ENTITY.get(), level);
        this.setPos(start.subtract(0.0, 0.75, 0.0));
        this.distance = (float)start.distanceTo(end);
        this.setRot(owner.getYRot(), owner.getXRot());
    }

    public void tick() {
        if (++this.tickCount > 15) {
            this.discard();
        }
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    public boolean shouldBeSaved() {
        return false;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt((int)(this.distance * 10.0f));
    }

    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.distance = (float)additionalData.readInt() / 10.0f;
    }
}

