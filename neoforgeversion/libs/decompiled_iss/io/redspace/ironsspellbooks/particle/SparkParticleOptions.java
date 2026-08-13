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

public class SparkParticleOptions
implements ParticleOptions {
    public final Vector3f color;
    public static StreamCodec<? super ByteBuf, SparkParticleOptions> STREAM_CODEC = StreamCodec.of((buf, option) -> {
        buf.writeFloat(option.color.x);
        buf.writeFloat(option.color.y);
        buf.writeFloat(option.color.z);
    }, buf -> new SparkParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat()));
    public static MapCodec<SparkParticleOptions> MAP_CODEC = RecordCodecBuilder.mapCodec(object -> object.group((App)Codec.FLOAT.fieldOf("r").forGetter(p -> Float.valueOf(p.color.x)), (App)Codec.FLOAT.fieldOf("g").forGetter(p -> Float.valueOf(p.color.y)), (App)Codec.FLOAT.fieldOf("b").forGetter(p -> Float.valueOf(p.color.z))).apply((Applicative)object, SparkParticleOptions::new));

    public SparkParticleOptions(Vector3f color) {
        this.color = color;
    }

    public SparkParticleOptions(float r, float g, float b) {
        this(new Vector3f(r, g, b));
    }

    public ParticleType<?> getType() {
        return ParticleRegistry.SPARK_PARTICLE.get();
    }
}

