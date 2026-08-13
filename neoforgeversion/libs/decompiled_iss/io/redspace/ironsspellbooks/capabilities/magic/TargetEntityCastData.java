/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.spells.ICastData;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class TargetEntityCastData
implements ICastData {
    private final UUID targetUUID;

    public TargetEntityCastData(LivingEntity target) {
        this.targetUUID = target.getUUID();
    }

    @Override
    public void reset() {
    }

    @Nullable
    public LivingEntity getTarget(ServerLevel level) {
        return (LivingEntity)level.getEntity(this.targetUUID);
    }

    public UUID getTargetUUID() {
        return this.targetUUID;
    }

    @Nullable
    public Vec3 getTargetPosition(ServerLevel level) {
        LivingEntity target = this.getTarget(level);
        return target == null ? null : target.position();
    }
}

