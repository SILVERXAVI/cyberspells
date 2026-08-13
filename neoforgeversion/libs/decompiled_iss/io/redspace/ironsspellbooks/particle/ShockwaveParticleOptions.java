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
 *  net.minecraft.util.ExtraCodecs
 *  org.jetbrains.annotations.NotNull
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
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class ShockwaveParticleOptions
implements ParticleOptions {
    protected final boolean fullbright;
    private final float scale;
    private final Vector3f color;
    public static final MapCodec<ShockwaveParticleOptions> CODEC = RecordCodecBuilder.mapCodec(p_175793_ -> p_175793_.group((App)ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(option -> option.color), (App)Codec.FLOAT.fieldOf("scale").forGetter(option -> Float.valueOf(option.scale)), (App)Codec.BOOL.fieldOf("fullbright").forGetter(option -> option.fullbright)).apply((Applicative)p_175793_, ShockwaveParticleOptions::new));
    public static StreamCodec<? super ByteBuf, ShockwaveParticleOptions> STREAM_CODEC = StreamCodec.of((buf, option) -> {
        buf.writeFloat(option.color.x);
        buf.writeFloat(option.color.y);
        buf.writeFloat(option.color.z);
        buf.writeFloat(option.scale);
        buf.writeBoolean(option.fullbright);
    }, buf -> new ShockwaveParticleOptions(new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat()), buf.readFloat(), buf.readBoolean()));

    public ShockwaveParticleOptions(Vector3f color, float scale, boolean glowing) {
        this.scale = scale;
        this.color = color;
        this.fullbright = glowing;
    }

    public float getScale() {
        return this.scale;
    }

    public boolean isFullbright() {
        return this.fullbright;
    }

    public Vector3f color() {
        return this.color;
    }

    @NotNull
    public ParticleType<ShockwaveParticleOptions> getType() {
        return ParticleRegistry.SHOCKWAVE_PARTICLE.get();
    }
}

