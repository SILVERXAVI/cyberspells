/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.util;

import io.redspace.ironsspellbooks.particle.FogParticleOptions;
import io.redspace.ironsspellbooks.particle.SparkParticleOptions;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import org.joml.Vector3f;

public class ParticleHelper {
    public static final ParticleOptions FIRE = (ParticleOptions)ParticleRegistry.DRAGON_FIRE_PARTICLE.get();
    public static final ParticleOptions FIRE_EMITTER = (ParticleOptions)ParticleRegistry.FIRE_PARTICLE.get();
    public static final ParticleOptions BLOOD = (ParticleOptions)ParticleRegistry.BLOOD_PARTICLE.get();
    public static final ParticleOptions WISP = (ParticleOptions)ParticleRegistry.WISP_PARTICLE.get();
    public static final ParticleOptions BLOOD_GROUND = (ParticleOptions)ParticleRegistry.BLOOD_GROUND_PARTICLE.get();
    public static final ParticleOptions SNOWFLAKE = (ParticleOptions)ParticleRegistry.SNOWFLAKE_PARTICLE.get();
    public static final ParticleOptions ELECTRICITY = (ParticleOptions)ParticleRegistry.ELECTRICITY_PARTICLE.get();
    public static final ParticleOptions UNSTABLE_ENDER = (ParticleOptions)ParticleRegistry.UNSTABLE_ENDER_PARTICLE.get();
    public static final ParticleOptions EMBERS = (ParticleOptions)ParticleRegistry.EMBER_PARTICLE.get();
    public static final ParticleOptions SIPHON = (ParticleOptions)ParticleRegistry.SIPHON_PARTICLE.get();
    public static final ParticleOptions ACID = (ParticleOptions)ParticleRegistry.ACID_PARTICLE.get();
    public static final ParticleOptions ACID_BUBBLE = (ParticleOptions)ParticleRegistry.ACID_BUBBLE_PARTICLE.get();
    public static final ParticleOptions FOG = new FogParticleOptions(new Vector3f(1.0f, 1.0f, 1.0f), 1.0f);
    public static final ParticleOptions VOID_TENTACLE_FOG = new FogParticleOptions(new Vector3f(0.0f, 0.075f, 0.13f), 2.0f);
    public static final ParticleOptions ROOT_FOG = new FogParticleOptions(new Vector3f(0.23921569f, 0.15686275f, 0.07058824f), 0.4f);
    public static final ParticleOptions COMET_FOG = new FogParticleOptions(new Vector3f(0.75f, 0.55f, 1.0f), 1.5f);
    public static final ParticleOptions FOG_THUNDER_LIGHT = new FogParticleOptions(new Vector3f(0.5f, 0.5f, 0.5f), 1.5f);
    public static final ParticleOptions FOG_THUNDER_DARK = new FogParticleOptions(new Vector3f(0.4f, 0.4f, 0.4f), 1.5f);
    public static final ParticleOptions POISON_CLOUD = new FogParticleOptions(new Vector3f(0.08f, 0.64f, 0.16f), 1.0f);
    public static final ParticleOptions FOG_CAMPFIRE_SMOKE = new FogParticleOptions(new Vector3f(0.5411765f, 0.50980395f, 0.49019608f), 1.0f);
    public static final ParticleOptions ICY_FOG = new FogParticleOptions(new Vector3f(0.8156863f, 0.9764706f, 1.0f), 0.6f);
    public static final ParticleOptions SUNBEAM = new FogParticleOptions(new Vector3f(0.95f, 0.97f, 0.36f), 1.0f);
    public static final ParticleOptions FIREFLY = (ParticleOptions)ParticleRegistry.FIREFLY_PARTICLE.get();
    public static final ParticleOptions PORTAL_FRAME = (ParticleOptions)ParticleRegistry.PORTAL_FRAME_PARTICLE.get();
    public static final ParticleOptions FIERY_SPARKS = new SparkParticleOptions(new Vector3f(1.0f, 0.6f, 0.3f));
    public static final ParticleOptions ELECTRIC_SPARKS = new SparkParticleOptions(new Vector3f(0.333f, 1.0f, 1.0f));
    public static final ParticleOptions ENDER_SPARKS = new SparkParticleOptions(new Vector3f(1.0f, 0.333f, 1.0f));
    public static final ParticleOptions SNOW_DUST = (ParticleOptions)ParticleRegistry.SNOW_DUST.get();
    public static final ParticleOptions CLEANSE_PARTICLE = (ParticleOptions)ParticleRegistry.CLEANSE_PARTICLE.get();
    public static final ParticleOptions FIERY_SMOKE = (ParticleOptions)ParticleRegistry.FIERY_SMOKE_PARTICLE.get();
}

