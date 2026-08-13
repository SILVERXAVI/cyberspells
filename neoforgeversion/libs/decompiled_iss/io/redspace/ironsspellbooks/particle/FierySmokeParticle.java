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
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.util.Mth
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
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FierySmokeParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final boolean mirrored;

    public FierySmokeParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.scale(this.random.nextFloat() * 2.5f + 1.5f);
        this.lifetime = 10 + (int)(Math.random() * 30.0);
        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
        this.gravity = -0.0025f;
        this.mirrored = this.random.nextBoolean();
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.alpha = Mth.clampedLerp((float)1.0f, (float)0.0f, (float)((float)(this.age - this.lifetime + 8) / 8.0f));
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.xd += (double)(this.random.nextFloat() / 500.0f * (float)(this.random.nextBoolean() ? 1 : -1));
            this.yd += (double)(this.random.nextFloat() / 100.0f - this.gravity);
            this.zd += (double)(this.random.nextFloat() / 500.0f * (float)(this.random.nextBoolean() ? 1 : -1));
            this.setSpriteFromAge(this.sprites);
            this.scale(1.023f);
        }
    }

    protected float getU0() {
        return this.mirrored ? super.getU1() : super.getU0();
    }

    protected float getU1() {
        return this.mirrored ? super.getU0() : super.getU1();
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            return new FierySmokeParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}

