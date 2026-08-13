/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.particle;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import java.util.stream.IntStream;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public class ZapParticleOption
implements ParticleOptions {
    public static StreamCodec<? super ByteBuf, ZapParticleOption> STREAM_CODEC = StreamCodec.of((buf, option) -> {
        buf.writeInt((int)option.destination.x * 10);
        buf.writeInt((int)option.destination.y * 10);
        buf.writeInt((int)option.destination.z * 10);
    }, buf -> new ZapParticleOption((float)buf.readInt() / 10.0f, (float)buf.readInt() / 10.0f, (float)buf.readInt() / 10.0f));
    public static MapCodec<ZapParticleOption> MAP_CODEC = RecordCodecBuilder.mapCodec(object -> object.group((App)Codec.INT_STREAM.fieldOf("destination").forGetter(option -> IntStream.of((int)option.destination.x * 10, (int)option.destination.y * 10, (int)option.destination.z * 10))).apply((Applicative)object, stream -> {
        int[] array = stream.toArray();
        return new ZapParticleOption(new Vec3((double)((float)array[0] / 10.0f), (double)((float)array[1] / 10.0f), (double)((float)array[2] / 10.0f)));
    }));
    private final Vec3 destination;

    public ZapParticleOption(Vec3 destination) {
        this.destination = destination;
    }

    public ZapParticleOption(float x, float y, float z) {
        this(new Vec3((double)x, (double)y, (double)z));
    }

    public ParticleType<ZapParticleOption> getType() {
        return ParticleRegistry.ZAP_PARTICLE.get();
    }

    public Vec3 getDestination() {
        return this.destination;
    }
}

