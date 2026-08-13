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
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.particle;

import io.redspace.ironsspellbooks.particle.SparkParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class SparkParticle
extends TextureSheetParticle {
    float targetR;
    float targetG;
    float targetB;
    int colorTime;
    boolean touchedGround;
    float bounciness;

    public SparkParticle(SparkParticleOptions options, ClientLevel level, double xCoord, double yCoord, double zCoord, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.scale(this.random.nextFloat() * 0.65f + 0.4f);
        this.lifetime = 20 + (int)(Math.random() * 45.0);
        this.gravity = 1.3f;
        this.friction = 0.985f;
        this.quadSize = 0.0625f;
        this.targetR = options.color.x() * (0.9f + this.random.nextFloat() * 0.1f);
        this.targetG = options.color.y() * (0.9f + this.random.nextFloat() * 0.1f);
        this.targetB = options.color.z() * (0.9f + this.random.nextFloat() * 0.1f);
        this.bounciness = 0.6f + this.random.nextFloat() * 0.2f;
        this.colorTime = 5 + (int)(Math.random() * 20.0);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
    }

    public void tick() {
        if (!this.touchedGround && this.lifetime < 30) {
            ++this.lifetime;
        }
        float f = Mth.clamp((float)((float)this.age / (float)this.colorTime), (float)0.0f, (float)1.0f);
        this.rCol = Mth.lerp((float)f, (float)1.0f, (float)this.targetR);
        this.gCol = Mth.lerp((float)f, (float)1.0f, (float)this.targetG);
        this.bCol = Mth.lerp((float)f, (float)1.0f, (float)this.targetB);
        if (this.onGround) {
            this.touchedGround = true;
            this.yd *= (double)(-this.bounciness);
            this.bounciness *= 0.8f;
            this.quadSize *= 0.9f;
        }
        super.tick();
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
    implements ParticleProvider<SparkParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(@NotNull SparkParticleOptions options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            SparkParticle particle = new SparkParticle(options, level, x, y, z, dx, dy, dz);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}

