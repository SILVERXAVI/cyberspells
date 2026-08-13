/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
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
package io.redspace.ironsspellbooks.entity.spells.eldritch_blast;

import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class EldritchBlastVisualEntity
extends Entity
implements IEntityWithComplexSpawn {
    public static final int lifetime = 8;
    public float distance;

    public EldritchBlastVisualEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    public EldritchBlastVisualEntity(Level level, Vec3 start, Vec3 end, LivingEntity owner) {
        super((EntityType)EntityRegistry.ELDRITCH_BLAST_VISUAL_ENTITY.get(), level);
        this.setPos(start);
        this.distance = (float)start.distanceTo(end);
        this.setRot(owner.getYRot(), owner.getXRot());
    }

    public void tick() {
        if (this.tickCount == 1) {
            if (this.level.isClientSide) {
                Vec3 forward = this.getForward();
                for (float i = 1.0f; i < this.distance; i += 0.5f) {
                    Vec3 pos = this.position().add(forward.scale((double)i));
                    this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, false, pos.x, pos.y + 0.5, pos.z, 0.0, 0.0, 0.0);
                }
            }
        } else if (this.tickCount > 8) {
            this.discard();
        }
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    public boolean shouldBeSaved() {
        return false;
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

