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
 *  org.joml.Vector3f
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
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

public class FireflyParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private boolean lit;
    private float litTween;
    private int litTimer;
    private float wander;
    private final float flickerIntensity;
    private static final Vector3f litColor = new Vector3f(1.0f, 1.0f, 1.0f);
    private static final Vector3f unlitColor = new Vector3f(0.08627451f, 0.078431375f, 0.07058824f);

    public FireflyParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.scale(2.5f);
        this.lifetime = 20 + (int)(Math.random() * 90.0);
        this.sprites = spriteSet;
        this.gravity = 0.0f;
        this.lit = Utils.random.nextBoolean();
        this.litTween = this.lit ? 1.0f : 0.0f;
        this.wander = Utils.random.nextFloat() * 2.5f;
        this.wander *= this.wander * this.wander * this.wander;
        this.setSprite(this.sprites.get(0, 1));
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.flickerIntensity = (float)Utils.random.nextIntBetweenInclusive(18, 45) * 0.01f;
    }

    public void tick() {
        float xj = this.random.nextFloat() * 0.001f * this.wander * (float)(this.random.nextBoolean() ? 1 : -1);
        float yj = this.random.nextFloat() * 0.001f * this.wander * (float)(this.random.nextBoolean() ? 1 : -1) + 2.5E-4f;
        float zj = this.random.nextFloat() * 0.001f * this.wander * (float)(this.random.nextBoolean() ? 1 : -1);
        this.wander *= 0.98f;
        if (this.onGround) {
            this.yd = Math.abs(this.yd);
        }
        this.xd += (double)xj;
        this.yd += (double)yj;
        this.zd += (double)zj;
        if (--this.litTimer <= 0) {
            this.lit = !this.lit;
            this.litTimer = this.random.nextIntBetweenInclusive(5, 20);
        }
        this.litTween = this.lit ? Mth.lerp((float)this.flickerIntensity, (float)this.litTween, (float)1.0f) : Mth.lerp((float)this.flickerIntensity, (float)this.litTween, (float)0.0f);
        this.rCol = Mth.lerp((float)this.litTween, (float)unlitColor.x(), (float)litColor.x());
        this.gCol = Mth.lerp((float)this.litTween, (float)unlitColor.y(), (float)litColor.y());
        this.bCol = Mth.lerp((float)this.litTween, (float)unlitColor.z(), (float)litColor.z());
        if (this.age >= this.lifetime - 1 && this.litTween > 0.1f) {
            ++this.lifetime;
        }
        super.tick();
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    protected int getLightColor(float pPartialTick) {
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
            return new FireflyParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}

