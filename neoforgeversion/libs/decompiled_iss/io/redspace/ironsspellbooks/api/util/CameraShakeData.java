/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.api.util;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CameraShakeData {
    final int duration;
    final float radius;
    int tickCount = 0;
    final int id;
    final Vec3 origin;
    final ResourceKey<Level> dimension;

    @Deprecated(forRemoval=true)
    public CameraShakeData(int duration, Vec3 origin, float radius) {
        this(null, duration, origin, radius);
        IronsSpellbooks.LOGGER.warn("Addon creating camera shake without specifying dimension! Adding to overworld.");
    }

    public CameraShakeData(@NotNull Level level, int duration, Vec3 origin, float radius) {
        this((ResourceKey<Level>)(level == null ? ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)ResourceLocation.withDefaultNamespace((String)"overworld")) : level.dimension()), CameraShakeManager.getNextId(), duration, origin, radius);
    }

    private CameraShakeData(ResourceKey<Level> level, int id, int duration, Vec3 origin, float radius) {
        this.dimension = level;
        this.id = id;
        this.duration = duration;
        this.origin = origin;
        this.radius = radius;
    }

    public void serializeToBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeInt(this.duration);
        buf.writeInt(this.tickCount);
        buf.writeInt((int)(this.origin.x * 10.0));
        buf.writeInt((int)(this.origin.y * 10.0));
        buf.writeInt((int)(this.origin.z * 10.0));
        buf.writeInt((int)(this.radius * 10.0f));
        buf.writeResourceKey(this.dimension);
    }

    public static CameraShakeData deserializeFromBuffer(FriendlyByteBuf buf) {
        int id = buf.readInt();
        int duration = buf.readInt();
        int tickCount = buf.readInt();
        Vec3 origin = new Vec3((double)((float)buf.readInt() / 10.0f), (double)((float)buf.readInt() / 10.0f), (double)((float)buf.readInt() / 10.0f));
        float radius = (float)buf.readInt() / 10.0f;
        ResourceKey dimension = buf.readResourceKey(Registries.DIMENSION);
        CameraShakeData data = new CameraShakeData((ResourceKey<Level>)dimension, id, duration, origin, radius);
        data.tickCount = tickCount;
        return data;
    }
}

