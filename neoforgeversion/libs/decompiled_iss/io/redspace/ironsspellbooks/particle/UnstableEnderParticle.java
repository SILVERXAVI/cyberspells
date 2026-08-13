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
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package io.redspace.ironsspellbooks.particle;

import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class UnstableEnderParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;

    public UnstableEnderParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.friction = 0.77f;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize = 0.14f * (this.random.nextFloat() * 0.15f + 0.3f);
        this.scale(2.25f);
        this.lifetime = 7 + (int)(Math.random() * 10.0 + Math.min(new Vec3(xd, yd, zd).length() * 100.0, 20.0));
        this.sprites = spriteSet;
        this.gravity = 0.0f;
        this.randomlyAnimate();
        float f = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = f * 0.9f;
        this.gCol = f * 0.3f;
        this.bCol = f;
    }

    public void tick() {
        super.tick();
        float xj = this.random.nextFloat() / 50.0f * (float)(this.random.nextBoolean() ? 1 : -1);
        float yj = this.random.nextFloat() / 50.0f * (float)(this.random.nextBoolean() ? 1 : -1);
        float zj = this.random.nextFloat() / 50.0f * (float)(this.random.nextBoolean() ? 1 : -1);
        this.setPos(this.x + (double)xj, this.y + (double)yj, this.z + (double)zj);
    }

    private void randomlyAnimate() {
        this.setSprite(this.sprites.get(Utils.random));
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public int getLightColor(float p_107564_) {
        return 0xF000F0;
    }

    public float getQuadSize(float p_107567_) {
        float f = ((float)this.age + p_107567_) / (float)this.lifetime;
        f = 1.0f - f;
        f *= f;
        f = 1.0f - f;
        return this.quadSize * f;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new UnstableEnderParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}

