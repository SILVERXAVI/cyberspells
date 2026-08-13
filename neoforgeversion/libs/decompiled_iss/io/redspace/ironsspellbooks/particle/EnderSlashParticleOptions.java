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

public class EnderSlashParticleOptions
implements ParticleOptions {
    public final float scale;
    public final float xf;
    public final float yf;
    public final float zf;
    public final float xu;
    public final float yu;
    public final float zu;
    public static StreamCodec<? super ByteBuf, EnderSlashParticleOptions> STREAM_CODEC = StreamCodec.of((buf, option) -> {
        buf.writeFloat(option.xf);
        buf.writeFloat(option.yf);
        buf.writeFloat(option.zf);
        buf.writeFloat(option.xu);
        buf.writeFloat(option.yu);
        buf.writeFloat(option.zu);
        buf.writeFloat(option.scale);
    }, buf -> new EnderSlashParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()));
    public static MapCodec<EnderSlashParticleOptions> MAP_CODEC = RecordCodecBuilder.mapCodec(object -> object.group((App)Codec.FLOAT.fieldOf("xf").forGetter(p -> Float.valueOf(p.xf)), (App)Codec.FLOAT.fieldOf("yf").forGetter(p -> Float.valueOf(p.yf)), (App)Codec.FLOAT.fieldOf("zf").forGetter(p -> Float.valueOf(p.zf)), (App)Codec.FLOAT.fieldOf("xu").forGetter(p -> Float.valueOf(p.xu)), (App)Codec.FLOAT.fieldOf("yu").forGetter(p -> Float.valueOf(p.yu)), (App)Codec.FLOAT.fieldOf("zu").forGetter(p -> Float.valueOf(p.zu)), (App)Codec.FLOAT.fieldOf("scale").forGetter(p -> Float.valueOf(p.scale))).apply((Applicative)object, EnderSlashParticleOptions::new));

    public EnderSlashParticleOptions(float xf, float yf, float zf, float xu, float yu, float zu, float scale) {
        this.scale = scale;
        this.xf = xf;
        this.yf = yf;
        this.zf = zf;
        this.xu = xu;
        this.yu = yu;
        this.zu = zu;
    }

    @NotNull
    public ParticleType<EnderSlashParticleOptions> getType() {
        return ParticleRegistry.ENDER_SLASH_PARTICLE.get();
    }
}

