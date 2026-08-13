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
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 *  org.joml.Vector4f
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
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BlastwaveParticleOptions
implements ParticleOptions {
    private final float scale;
    private final Vector3f color;
    public static StreamCodec<? super ByteBuf, BlastwaveParticleOptions> STREAM_CODEC = StreamCodec.of((buf, option) -> {
        buf.writeFloat(option.color.x);
        buf.writeFloat(option.color.y);
        buf.writeFloat(option.color.z);
        buf.writeFloat(option.scale);
    }, buf -> new BlastwaveParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()));
    public static MapCodec<BlastwaveParticleOptions> MAP_CODEC = RecordCodecBuilder.mapCodec(object -> object.group((App)Codec.FLOAT.fieldOf("r").forGetter(p -> Float.valueOf(p.color.x)), (App)Codec.FLOAT.fieldOf("g").forGetter(p -> Float.valueOf(p.color.y)), (App)Codec.FLOAT.fieldOf("b").forGetter(p -> Float.valueOf(p.color.z)), (App)Codec.FLOAT.fieldOf("scale").forGetter(p -> Float.valueOf(p.scale))).apply((Applicative)object, BlastwaveParticleOptions::new));

    public BlastwaveParticleOptions(Vector3f color, float scale) {
        this.scale = scale;
        this.color = color;
    }

    public float getScale() {
        return this.scale;
    }

    public Vector3f getColor() {
        return this.color;
    }

    public Vector3f color() {
        return this.color;
    }

    public BlastwaveParticleOptions(float r, float g, float b, float scale) {
        this(new Vector3f(r, g, b), scale);
    }

    private BlastwaveParticleOptions(Vector4f vector4f) {
        this(vector4f.x, vector4f.y, vector4f.z, vector4f.w);
    }

    @NotNull
    public ParticleType<BlastwaveParticleOptions> getType() {
        return ParticleRegistry.BLASTWAVE_PARTICLE.get();
    }
}

