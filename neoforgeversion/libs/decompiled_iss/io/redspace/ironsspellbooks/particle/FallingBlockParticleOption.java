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
 *  net.minecraft.core.IdMap
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
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
import java.util.Optional;
import net.minecraft.core.IdMap;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FallingBlockParticleOption
implements ParticleOptions {
    private static final Codec<BlockState> BLOCK_STATE_CODEC = Codec.withAlternative((Codec)BlockState.CODEC, (Codec)BuiltInRegistries.BLOCK.byNameCodec(), Block::defaultBlockState);
    private static final StreamCodec<ByteBuf, BlockState> BLOCK_STATE_STREAM_CODEC = ByteBufCodecs.idMapper((IdMap)Block.BLOCK_STATE_REGISTRY);
    private static final StreamCodec<RegistryFriendlyByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.of(FriendlyByteBuf::writeVec3, FriendlyByteBuf::readVec3);
    private final ParticleType<FallingBlockParticleOption> type;
    private final BlockState state;
    private final Vec3 motion;

    public Vec3 getMotion() {
        return this.motion;
    }

    public static MapCodec<FallingBlockParticleOption> codec(ParticleType<FallingBlockParticleOption> particleType) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group((App)BLOCK_STATE_CODEC.fieldOf("block_state").forGetter(FallingBlockParticleOption::getState), (App)Vec3.CODEC.optionalFieldOf("motion", (Object)Vec3.ZERO).forGetter(FallingBlockParticleOption::getMotion)).apply((Applicative)builder, (state, motion) -> new FallingBlockParticleOption(particleType, (BlockState)state, (Vec3)motion)));
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, FallingBlockParticleOption> streamCodec(ParticleType<FallingBlockParticleOption> particleType) {
        return StreamCodec.composite(BLOCK_STATE_STREAM_CODEC, FallingBlockParticleOption::getState, (StreamCodec)ByteBufCodecs.optional(VEC3_STREAM_CODEC).map(opt -> opt.orElse(Vec3.ZERO), vec3 -> vec3 == Vec3.ZERO ? Optional.empty() : Optional.of(vec3)), FallingBlockParticleOption::getMotion, (state, motion) -> new FallingBlockParticleOption(particleType, (BlockState)state, (Vec3)motion));
    }

    public FallingBlockParticleOption(ParticleType<FallingBlockParticleOption> type, BlockState state, Vec3 motion) {
        this.type = type;
        this.state = state;
        this.motion = motion;
    }

    public FallingBlockParticleOption(ParticleType<FallingBlockParticleOption> type, BlockState state) {
        this(type, state, Vec3.ZERO);
    }

    public FallingBlockParticleOption(BlockState state, Vec3 motion) {
        this(ParticleRegistry.FALLING_BLOCK_PARTICLE.get(), state, motion);
    }

    public FallingBlockParticleOption(BlockState state) {
        this(state, Vec3.ZERO);
    }

    public ParticleType<FallingBlockParticleOption> getType() {
        return this.type;
    }

    public BlockState getState() {
        return this.state;
    }
}

