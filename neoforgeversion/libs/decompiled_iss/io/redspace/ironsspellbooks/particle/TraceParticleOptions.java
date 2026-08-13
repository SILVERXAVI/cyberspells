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
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.particle;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector3f;

public class TraceParticleOptions
implements ParticleOptions {
    public final Vector3f color;
    public final Vector3f destination;
    public static StreamCodec<? super ByteBuf, TraceParticleOptions> STREAM_CODEC = StreamCodec.of((buf, option) -> {
        buf.writeFloat(option.destination.x);
        buf.writeFloat(option.destination.y);
        buf.writeFloat(option.destination.z);
        buf.writeFloat(option.color.x);
        buf.writeFloat(option.color.y);
        buf.writeFloat(option.color.z);
    }, buf -> new TraceParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()));
    public static MapCodec<TraceParticleOptions> MAP_CODEC = RecordCodecBuilder.mapCodec(object -> object.group((App)Codec.FLOAT.fieldOf("x").forGetter(p -> Float.valueOf(p.destination.x)), (App)Codec.FLOAT.fieldOf("y").forGetter(p -> Float.valueOf(p.destination.y)), (App)Codec.FLOAT.fieldOf("z").forGetter(p -> Float.valueOf(p.destination.z)), (App)Codec.FLOAT.fieldOf("r").forGetter(p -> Float.valueOf(p.color.x)), (App)Codec.FLOAT.fieldOf("g").forGetter(p -> Float.valueOf(p.color.y)), (App)Codec.FLOAT.fieldOf("b").forGetter(p -> Float.valueOf(p.color.z))).apply((Applicative)object, TraceParticleOptions::new));

    public TraceParticleOptions(Vector3f destination, Vector3f color) {
        this.color = color;
        this.destination = destination;
    }

    public TraceParticleOptions(float x, float y, float z, float r, float g, float b) {
        this(new Vector3f(x, y, z), new Vector3f(r, g, b));
    }

    public ParticleType<?> getType() {
        return ParticleRegistry.TRACE_PARTICLE.get();
    }
}

