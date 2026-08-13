/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.entity.PartEntity
 *  org.joml.Quaternionf
 */
package io.redspace.ironsspellbooks.entity.mobs.ice_spider;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.ICritablePartEntity;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.joml.Quaternionf;

public class IceSpiderPartEntity
extends PartEntity<IceSpiderEntity>
implements ICritablePartEntity {
    public final IceSpiderEntity parentMob;
    private final EntityDimensions size;
    private final Vec3 baseOffset;
    private final boolean collision;

    public IceSpiderPartEntity(IceSpiderEntity pParentMob, Vec3 offset16, float pWidth, float pHeight, boolean collision) {
        super((Entity)pParentMob);
        float inflate = 0.1f;
        this.size = EntityDimensions.scalable((float)(pWidth + inflate * 2.0f), (float)(pHeight + inflate * 2.0f));
        this.parentMob = pParentMob;
        this.refreshDimensions();
        this.baseOffset = offset16.scale(0.0625).subtract(0.0, (double)inflate, 0.0);
        this.collision = collision;
    }

    public IceSpiderPartEntity(IceSpiderEntity pParentMob, Vec3 offset16, float pWidth, float pHeight) {
        this(pParentMob, offset16, pWidth, pHeight, false);
    }

    public void positionSelf(Quaternionf normal) {
        Vec3 parentPos = this.parentMob.position();
        Vec3 offset = this.baseOffset.multiply(1.0, (double)this.parentMob.getCrouchHeightMultiplier(), 1.0);
        Vec3 localVec = Utils.v3d(normal.transform(Utils.v3f(this.parentMob.rotateWithBody(offset))));
        this.hardSetPos(parentPos.add(localVec.scale((double)this.parentMob.getScale())));
    }

    public boolean canBeCollidedWith() {
        return this.collision;
    }

    private void hardSetPos(Vec3 newVector) {
        this.setPos(newVector);
        this.setDeltaMovement(newVector);
        Vec3 vec3 = this.position();
        this.xo = vec3.x;
        this.yo = vec3.y;
        this.zo = vec3.z;
        this.xOld = vec3.x;
        this.yOld = vec3.y;
        this.zOld = vec3.z;
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return this.parentMob.hurt(this, pSource, pAmount);
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    public boolean isPickable() {
        return true;
    }

    public ItemStack getPickResult() {
        return this.parentMob.getPickResult();
    }

    public boolean is(Entity pEntity) {
        return this == pEntity || this.parentMob == pEntity;
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return this.size.scale(this.parentMob.getScale());
    }

    public boolean shouldBeSaved() {
        return false;
    }
}

