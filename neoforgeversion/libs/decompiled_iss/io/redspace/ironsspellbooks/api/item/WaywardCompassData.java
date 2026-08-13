/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 */
package io.redspace.ironsspellbooks.api.item;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record WaywardCompassData(BlockPos blockPos) {
    public static final Codec<WaywardCompassData> CODEC = RecordCodecBuilder.create(builder -> builder.group((App)BlockPos.CODEC.fieldOf("catacombs_pos").forGetter(WaywardCompassData::blockPos)).apply((Applicative)builder, WaywardCompassData::new));
    public static final StreamCodec<FriendlyByteBuf, WaywardCompassData> STREAM_CODEC = StreamCodec.of((buf, data) -> buf.writeBlockPos(data.blockPos), buf -> new WaywardCompassData(buf.readBlockPos()));

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof WaywardCompassData)) return false;
        WaywardCompassData waywardCompassData = (WaywardCompassData)obj;
        if (!waywardCompassData.blockPos.equals((Object)this.blockPos)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.blockPos.hashCode();
    }
}

