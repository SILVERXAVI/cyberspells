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

public class FlameStrikeParticleOptions
implements ParticleOptions {
    public final float scale;
    public final float xf;
    public final float yf;
    public final float zf;
    public final boolean mirror;
    public final boolean vertical;
    public static StreamCodec<? super ByteBuf, FlameStrikeParticleOptions> STREAM_CODEC = StreamCodec.of((buf, option) -> {
        buf.writeFloat(option.xf);
        buf.writeFloat(option.yf);
        buf.writeFloat(option.zf);
        buf.writeBoolean(option.mirror);
        buf.writeBoolean(option.vertical);
        buf.writeFloat(option.scale);
    }, buf -> new FlameStrikeParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readBoolean(), buf.readFloat()));
    public static MapCodec<FlameStrikeParticleOptions> MAP_CODEC = RecordCodecBuilder.mapCodec(object -> object.group((App)Codec.FLOAT.fieldOf("xf").forGetter(p -> Float.valueOf(p.xf)), (App)Codec.FLOAT.fieldOf("yf").forGetter(p -> Float.valueOf(p.yf)), (App)Codec.FLOAT.fieldOf("zf").forGetter(p -> Float.valueOf(p.zf)), (App)Codec.BOOL.fieldOf("mirror").forGetter(p -> p.mirror), (App)Codec.BOOL.fieldOf("vertical").forGetter(p -> p.vertical), (App)Codec.FLOAT.fieldOf("scale").forGetter(p -> Float.valueOf(p.scale))).apply((Applicative)object, FlameStrikeParticleOptions::new));

    public FlameStrikeParticleOptions(float xf, float yf, float zf, boolean mirror, boolean vertical, float scale) {
        this.scale = scale;
        this.xf = xf;
        this.yf = yf;
        this.zf = zf;
        this.mirror = mirror;
        this.vertical = vertical;
    }

    @NotNull
    public ParticleType<FlameStrikeParticleOptions> getType() {
        return ParticleRegistry.FLAME_STRIKE_PARTICLE.get();
    }
}

