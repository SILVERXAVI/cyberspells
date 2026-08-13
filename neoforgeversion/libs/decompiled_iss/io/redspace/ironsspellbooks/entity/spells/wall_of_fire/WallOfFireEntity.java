/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.IEntityWithComplexSpawn
 *  net.neoforged.neoforge.entity.PartEntity
 */
package io.redspace.ironsspellbooks.entity.spells.wall_of_fire;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.entity.PartEntity;

public class WallOfFireEntity
extends AbstractShieldEntity
implements IEntityWithComplexSpawn {
    protected ShieldPart[] subEntities;
    protected List<Vec3> partPositions = new ArrayList<Vec3>();
    protected List<Vec3> anchorPoints = new ArrayList<Vec3>();
    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    protected float damage;
    protected int lifetime = 240;

    public WallOfFireEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.subEntities = new ShieldPart[0];
    }

    @Override
    public void takeDamage(DamageSource source, float amount, @Nullable Vec3 location) {
    }

    public WallOfFireEntity(Level level, Entity owner, List<Vec3> anchors, float damage) {
        this((EntityType)EntityRegistry.WALL_OF_FIRE_ENTITY.get(), level);
        this.anchorPoints = anchors;
        this.createShield();
        this.damage = damage;
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        if (this.anchorPoints.size() <= 1 || this.subEntities.length <= 1) {
            this.discard();
            return;
        }
        int subEntitiesLength = this.subEntities.length;
        for (int i = 0; i < subEntitiesLength; ++i) {
            ShieldPart subEntity = this.subEntities[i];
            Vec3 pos = this.partPositions.get(i);
            subEntity.setPos(pos);
            subEntity.xo = pos.x;
            subEntity.yo = pos.y;
            subEntity.zo = pos.z;
            subEntity.xOld = pos.x;
            subEntity.yOld = pos.y;
            subEntity.zOld = pos.z;
            if (this.level.isClientSide && i < subEntitiesLength - 1) {
                int count = this.random.nextIntBetweenInclusive(1, 2);
                for (int j = 0; j < count; ++j) {
                    Vec3 offset = this.partPositions.get(i + 1).subtract(pos).scale((double)Utils.random.nextFloat()).add(Utils.getRandomVec3(0.1));
                    this.level.addParticle(ParticleHelper.FIRE, pos.x + offset.x, pos.y + (double)Utils.random.nextFloat() * 0.25 + 0.1, pos.z + offset.z, 0.0, Math.random() * 0.25 + 0.05, 0.0);
                }
                continue;
            }
            for (LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, subEntity.getBoundingBox().inflate(0.2, 0.0, 0.2))) {
                if (livingentity == this.getOwner()) continue;
                DamageSources.applyDamage((Entity)livingentity, this.damage, SpellRegistry.WALL_OF_FIRE_SPELL.get().getDamageSource(this, this.getOwner()));
            }
        }
        if (!this.level.isClientSide && --this.lifetime < 0) {
            this.discard();
        }
    }

    @Override
    public void createShield() {
        float height = 3.0f;
        float step = 0.8f;
        ArrayList<ShieldPart> entitiesList = new ArrayList<ShieldPart>();
        for (int i = 0; i < this.anchorPoints.size() - 1; ++i) {
            Vec3 start = this.anchorPoints.get(i);
            Vec3 end = this.anchorPoints.get(i + 1);
            Vec3 dirVec = end.subtract(start).normalize().scale((double)step);
            int steps = (int)((start.distanceTo(end) + 0.5) / (double)step);
            for (int currentStep = 0; currentStep < steps; ++currentStep) {
                ShieldPart part = new ShieldPart(this, "part" + i * steps + currentStep, 0.55f, height, false);
                double x = start.x + dirVec.x * (double)currentStep;
                double y = start.y + dirVec.y * (double)currentStep;
                double z = start.z + dirVec.z * (double)currentStep;
                double groundY = Utils.moveToRelativeGroundLevel((Level)this.level, (Vec3)new Vec3((double)x, (double)y, (double)z), (int)4, (int)4).y;
                Vec3 pos = new Vec3(x, groundY, z);
                this.partPositions.add(pos);
                entitiesList.add(part);
            }
        }
        this.subEntities = entitiesList.toArray(this.subEntities);
    }

    public void setOwner(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
            this.cachedOwner = pOwner;
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        }
        if (this.ownerUUID != null && this.level instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel)this.level).getEntity(this.ownerUUID);
            return this.cachedOwner;
        }
        return null;
    }

    @Override
    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }
        compoundTag.putInt("lifetime", this.lifetime);
        ListTag anchors = new ListTag();
        for (Vec3 vec : this.anchorPoints) {
            CompoundTag anchor = new CompoundTag();
            anchor.putFloat("x", (float)vec.x);
            anchor.putFloat("y", (float)vec.y);
            anchor.putFloat("z", (float)vec.z);
            anchors.add((Object)anchor);
        }
        compoundTag.put("Anchors", (Tag)anchors);
        super.addAdditionalSaveData(compoundTag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.hasUUID("Owner")) {
            this.ownerUUID = compoundTag.getUUID("Owner");
        }
        if (compoundTag.contains("lifetime")) {
            this.lifetime = compoundTag.getInt("lifetime");
        }
        this.anchorPoints = new ArrayList<Vec3>();
        if (compoundTag.contains("Anchors", 9)) {
            ListTag anchors = (ListTag)compoundTag.get("Anchors");
            for (Tag tag : anchors) {
                if (!(tag instanceof CompoundTag)) continue;
                CompoundTag anchor = (CompoundTag)tag;
                this.anchorPoints.add(new Vec3(anchor.getDouble("x"), anchor.getDouble("y"), anchor.getDouble("z")));
            }
        }
        super.readAdditionalSaveData(compoundTag);
    }

    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.anchorPoints.size());
        for (Vec3 vec : this.anchorPoints) {
            buffer.writeFloat((float)vec.x);
            buffer.writeFloat((float)vec.y);
            buffer.writeFloat((float)vec.z);
        }
    }

    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.anchorPoints = new ArrayList<Vec3>();
        int length = additionalData.readInt();
        for (int i = 0; i < length; ++i) {
            this.anchorPoints.add(new Vec3((double)additionalData.readFloat(), (double)additionalData.readFloat(), (double)additionalData.readFloat()));
        }
        this.createShield();
    }
}

