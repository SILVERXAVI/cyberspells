/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.ResourceKey
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import com.mojang.serialization.MapCodec;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.EnderSlashParticleOptions;
import io.redspace.ironsspellbooks.particle.FallingBlockParticleOption;
import io.redspace.ironsspellbooks.particle.FlameStrikeParticleOptions;
import io.redspace.ironsspellbooks.particle.FogParticleOptions;
import io.redspace.ironsspellbooks.particle.ShockwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.SparkParticleOptions;
import io.redspace.ironsspellbooks.particle.TraceParticleOptions;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create((ResourceKey)Registries.PARTICLE_TYPE, (String)"irons_spellbooks");
    public static final Supplier<SimpleParticleType> BLOOD_PARTICLE = PARTICLE_TYPES.register("blood", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> WISP_PARTICLE = PARTICLE_TYPES.register("wisp", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> BLOOD_GROUND_PARTICLE = PARTICLE_TYPES.register("blood_ground", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SNOWFLAKE_PARTICLE = PARTICLE_TYPES.register("snowflake", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> ELECTRICITY_PARTICLE = PARTICLE_TYPES.register("electricity", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> UNSTABLE_ENDER_PARTICLE = PARTICLE_TYPES.register("unstable_ender", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> DRAGON_FIRE_PARTICLE = PARTICLE_TYPES.register("dragon_fire", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> FIRE_PARTICLE = PARTICLE_TYPES.register("fire", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> EMBER_PARTICLE = PARTICLE_TYPES.register("embers", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SIPHON_PARTICLE = PARTICLE_TYPES.register("spell", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> ACID_PARTICLE = PARTICLE_TYPES.register("acid", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> ACID_BUBBLE_PARTICLE = PARTICLE_TYPES.register("acid_bubble", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SNOW_DUST = PARTICLE_TYPES.register("snow_dust", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> RING_SMOKE_PARTICLE = PARTICLE_TYPES.register("ring_smoke", () -> new SimpleParticleType(false));
    public static final Supplier<ParticleType<FogParticleOptions>> FOG_PARTICLE = PARTICLE_TYPES.register("fog", () -> new ParticleType<FogParticleOptions>(true){

        public MapCodec<FogParticleOptions> codec() {
            return FogParticleOptions.MAP_CODEC;
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, FogParticleOptions> streamCodec() {
            return FogParticleOptions.STREAM_CODEC;
        }
    });
    public static final Supplier<ParticleType<ShockwaveParticleOptions>> SHOCKWAVE_PARTICLE = PARTICLE_TYPES.register("shockwave", () -> new ParticleType<ShockwaveParticleOptions>(false){

        public MapCodec<ShockwaveParticleOptions> codec() {
            return ShockwaveParticleOptions.CODEC;
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, ShockwaveParticleOptions> streamCodec() {
            return ShockwaveParticleOptions.STREAM_CODEC;
        }
    });
    public static final Supplier<ParticleType<ZapParticleOption>> ZAP_PARTICLE = PARTICLE_TYPES.register("zap", () -> new ParticleType<ZapParticleOption>(false){

        public MapCodec<ZapParticleOption> codec() {
            return ZapParticleOption.MAP_CODEC;
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, ZapParticleOption> streamCodec() {
            return ZapParticleOption.STREAM_CODEC;
        }
    });
    public static final Supplier<SimpleParticleType> FIREFLY_PARTICLE = PARTICLE_TYPES.register("firefly", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> PORTAL_FRAME_PARTICLE = PARTICLE_TYPES.register("portal_frame", () -> new SimpleParticleType(false));
    public static final Supplier<ParticleType<BlastwaveParticleOptions>> BLASTWAVE_PARTICLE = PARTICLE_TYPES.register("blastwave", () -> new ParticleType<BlastwaveParticleOptions>(true){

        public MapCodec<BlastwaveParticleOptions> codec() {
            return BlastwaveParticleOptions.MAP_CODEC;
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, BlastwaveParticleOptions> streamCodec() {
            return BlastwaveParticleOptions.STREAM_CODEC;
        }
    });
    public static final Supplier<ParticleType<SparkParticleOptions>> SPARK_PARTICLE = PARTICLE_TYPES.register("spark", () -> new ParticleType<SparkParticleOptions>(true){

        public MapCodec<SparkParticleOptions> codec() {
            return SparkParticleOptions.MAP_CODEC;
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, SparkParticleOptions> streamCodec() {
            return SparkParticleOptions.STREAM_CODEC;
        }
    });
    public static final Supplier<SimpleParticleType> CLEANSE_PARTICLE = PARTICLE_TYPES.register("cleanse", () -> new SimpleParticleType(false));
    public static final Supplier<ParticleType<FlameStrikeParticleOptions>> FLAME_STRIKE_PARTICLE = PARTICLE_TYPES.register("flame_strike", () -> new ParticleType<FlameStrikeParticleOptions>(true){

        public MapCodec<FlameStrikeParticleOptions> codec() {
            return FlameStrikeParticleOptions.MAP_CODEC;
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, FlameStrikeParticleOptions> streamCodec() {
            return FlameStrikeParticleOptions.STREAM_CODEC;
        }
    });
    public static final Supplier<SimpleParticleType> EMBEROUS_ASH_PARTICLE = PARTICLE_TYPES.register("emberous_ash", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> FIERY_SMOKE_PARTICLE = PARTICLE_TYPES.register("fiery_smoke", () -> new SimpleParticleType(true));
    public static final Supplier<ParticleType<EnderSlashParticleOptions>> ENDER_SLASH_PARTICLE = PARTICLE_TYPES.register("ender_slash", () -> new ParticleType<EnderSlashParticleOptions>(true){

        public MapCodec<EnderSlashParticleOptions> codec() {
            return EnderSlashParticleOptions.MAP_CODEC;
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, EnderSlashParticleOptions> streamCodec() {
            return EnderSlashParticleOptions.STREAM_CODEC;
        }
    });
    public static final Supplier<ParticleType<TraceParticleOptions>> TRACE_PARTICLE = PARTICLE_TYPES.register("trace", () -> new ParticleType<TraceParticleOptions>(true){

        public MapCodec<TraceParticleOptions> codec() {
            return TraceParticleOptions.MAP_CODEC;
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, TraceParticleOptions> streamCodec() {
            return TraceParticleOptions.STREAM_CODEC;
        }
    });
    public static final Supplier<ParticleType<FallingBlockParticleOption>> FALLING_BLOCK_PARTICLE = PARTICLE_TYPES.register("falling_block", () -> new ParticleType<FallingBlockParticleOption>(true){

        public MapCodec<FallingBlockParticleOption> codec() {
            return FallingBlockParticleOption.codec(this);
        }

        public StreamCodec<? super RegistryFriendlyByteBuf, FallingBlockParticleOption> streamCodec() {
            return FallingBlockParticleOption.streamCodec(this);
        }
    });

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}

