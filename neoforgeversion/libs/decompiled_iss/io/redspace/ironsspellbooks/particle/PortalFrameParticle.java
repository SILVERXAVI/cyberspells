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
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PortalFrameParticle
extends TextureSheetParticle {
    float pathWidth;
    float pathHeight;
    float yRad;
    float rotSpeed;
    float rot;
    final Vec3 origin;

    public PortalFrameParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, double pathWidth, double pathHeight, double yDegrees) {
        super(level, xCoord, yCoord, zCoord, pathWidth, pathHeight, yDegrees);
        this.origin = new Vec3(xCoord, yCoord, zCoord);
        this.pathWidth = (float)pathWidth * 0.5f;
        this.pathHeight = (float)pathHeight * 0.5f;
        this.yRad = (float)(yDegrees * 0.01745329238474369);
        this.scale(this.random.nextFloat() * 1.75f + 1.0f);
        this.lifetime = 40 + (int)(Math.random() * 45.0);
        this.gravity = 0.0f;
        this.rotSpeed = (float)Utils.random.nextIntBetweenInclusive(5, 10) * 0.04f;
        this.rotSpeed *= this.rotSpeed * (float)(Utils.random.nextBoolean() ? -1 : 1);
        this.rot = Utils.random.nextFloat() * ((float)Math.PI * 2);
        this.quadSize = 0.0625f;
        float f = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = f * 0.9f;
        this.gCol = f * 0.3f;
        this.bCol = f;
        this.alpha = 0.5f;
        this.rCol = Math.clamp((float)(this.rCol * 2.0f), (float)0.0f, (float)1.0f);
        this.gCol = Math.clamp((float)(this.gCol * 2.0f), (float)0.0f, (float)1.0f);
        this.bCol = Math.clamp((float)(this.bCol * 2.0f), (float)0.0f, (float)1.0f);
        this.updatePos();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.updatePos();
            this.pathWidth *= (float)(1.0 + (double)(Utils.random.nextFloat() * 0.075f * this.rotSpeed));
            this.pathHeight *= (float)(1.0 + (double)(Utils.random.nextFloat() * 0.075f * this.rotSpeed));
            this.quadSize -= this.rotSpeed * this.rotSpeed * 0.02f;
        }
    }

    private void updatePos() {
        this.x = this.origin.x + (double)(Mth.cos((float)this.rot) * this.pathWidth * Mth.cos((float)this.yRad));
        this.y = this.origin.y + (double)(Mth.sin((float)this.rot) * this.pathHeight);
        this.z = this.origin.z + (double)(Mth.cos((float)this.rot) * this.pathWidth * Mth.sin((float)this.yRad));
        this.rot = (this.rotSpeed + this.rot) % ((float)Math.PI * 2);
        Vec3 speed = new Vec3(this.x - this.xo, this.y - this.yo, this.z - this.zo);
        this.setBoundingBox(this.getBoundingBox().move(speed.x, speed.y, speed.z));
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
            PortalFrameParticle particle = new PortalFrameParticle(level, x, y, z, dx, dy, dz);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}

