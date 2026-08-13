/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package io.redspace.ironsspellbooks.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EmberousAshParticle
extends TextureSheetParticle {
    final float seed;
    final float speed;

    public EmberousAshParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.scale(this.random.nextFloat() * 0.65f + 0.4f);
        this.lifetime = 40 + (int)(Math.random() * 45.0);
        this.gravity = 0.0f;
        this.friction = 1.0f;
        this.quadSize = 0.0625f;
        this.rCol = 1.0f * (0.9f + this.random.nextFloat() * 0.1f);
        this.gCol = 0.6f * (0.9f + this.random.nextFloat() * 0.1f);
        this.bCol = 0.3f * (0.9f + this.random.nextFloat() * 0.1f);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.seed = (this.random.nextFloat() - 0.5f) * 2.0f * 5.0f;
        this.speed = (float)new Vec3(xd, yd, zd).length();
        if (this.speed > 4.0f) {
            this.xd = 0.15;
            this.yd = 0.0;
            this.zd = 0.0;
        }
    }

    private float f(float x) {
        return Mth.sin((float)(this.seed * Mth.sin((float)x) + x));
    }

    private float function(float x) {
        return 0.05f * (this.f(2.0f * x) + 1.0f * this.f(0.25f * x) + 2.0f * this.f(0.125f * x));
    }

    public float getQuadSize(float pScaleFactor) {
        return Mth.lerp((float)(((float)this.age + pScaleFactor) / (float)this.lifetime), (float)super.getQuadSize(pScaleFactor), (float)0.0f) * Mth.clamp((float)(((float)this.age + pScaleFactor) / 5.0f), (float)0.0f, (float)1.0f);
    }

    public void tick() {
        super.tick();
        float f = (double)Math.abs(this.seed) < 0.2 ? 1.0f : this.seed;
        this.xd = 0.3 * (double)this.seed * (double)(0.05f * Mth.sin((float)(((float)this.age + 700.0f * this.seed) * 0.2f / f)) + this.function((float)this.age * 0.2f + 700.0f * this.seed));
        this.yd = 0.3 * (double)this.seed * (double)(0.05f * Mth.sin((float)(((float)this.age + 500.0f * this.seed) * 0.2f / f)) + this.function((float)this.age * 0.2f + 500.0f * this.seed));
        this.zd = 0.3 * (double)this.seed * (double)(0.05f * Mth.cos((float)(((float)this.age + 100.0f * this.seed) * 0.2f / f)) + this.function((float)this.age * 0.2f + 100.0f * this.seed));
        if (this.speed > 4.0f) {
            this.xd = Math.abs(this.xd) * 3.0;
        }
        if ((double)this.random.nextFloat() < 0.5) {
            this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        }
        Vec3 vec3 = new Vec3(this.x - this.xo, this.y - this.yo, this.z - this.zo);
        if (vec3.lengthSqr() < 0.001) {
            this.remove();
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public int getLightColor(float p_107564_) {
        return 0xF000F0;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            EmberousAshParticle p = new EmberousAshParticle(level, x, y, z, dx, dy, dz);
            p.pickSprite(this.sprites);
            return p;
        }
    }
}

