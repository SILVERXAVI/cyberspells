/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Camera
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
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.particle.EnderSlashParticleOptions;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.Camera;
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
import org.joml.Vector3f;

public class EnderSlashParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final Vec3 forward;
    private final Vec3 up;
    private final Vector3f[] localVertices;

    EnderSlashParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double xd, double yd, double zd, EnderSlashParticleOptions options) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        this.lifetime = 5;
        this.gravity = 0.0f;
        this.sprites = spriteSet;
        this.quadSize = options.scale * 3.25f;
        this.forward = new Vec3((double)options.xf, (double)options.yf, (double)options.zf).normalize();
        this.up = new Vec3((double)options.xu, (double)options.yu, (double)options.zu).normalize();
        this.localVertices = this.calculateVertices();
        Vec3 vec3 = new Vec3(xd, yd, zd);
        if (vec3.lengthSqr() > 0.0) {
            this.xd = xd;
            this.yd = yd;
            this.zd = zd;
        } else {
            this.xd = this.forward.x * 0.1;
            this.yd = this.forward.y * 0.1;
            this.zd = this.forward.z * 0.1;
        }
        this.friction = 1.0f;
    }

    private Vec3 vec3Copy(Vector3f vector3f) {
        return new Vec3((double)vector3f.x, (double)vector3f.y, (double)vector3f.z);
    }

    public void tick() {
        if (this.age == 0) {
            this.createEmberTrail();
        }
        this.move(this.xd, this.yd, this.zd);
        if (this.age++ > this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    private void createEmberTrail() {
        int particleCount = (int)(15.0f * this.quadSize);
        for (int i = 1; i < particleCount - 1; ++i) {
            float t = (float)i / (float)particleCount;
            float u = 1.0f - t;
            Vec3 localPos = this.vec3Copy(this.localVertices[1]).scale(0.4).scale((double)(u * u * u)).add(this.vec3Copy(this.localVertices[2]).scale((double)(3.0f * u * u * t)).add(this.vec3Copy(this.localVertices[3]).scale((double)(3.0f * u * t * t)).add(this.vec3Copy(this.localVertices[0]).scale(0.85).scale((double)(t * t * t))))).scale((double)this.quadSize * 0.85);
            Vec3 pos = localPos.add(Utils.getRandomVec3(0.2 + (double)((float)i * 0.01f)));
            Vec3 motion = new Vec3(this.xd, this.yd, this.zd).scale(this.random.nextDouble() * 6.0);
            if (!(this.random.nextFloat() < 0.5f)) continue;
            this.level.addParticle(ParticleHelper.UNSTABLE_ENDER, this.x + pos.x, this.y + pos.y, this.z + pos.z, motion.x * 1.5, motion.y * 1.5, motion.z * 1.5);
        }
    }

    private Vector3f[] calculateVertices() {
        Vec3 forward = this.forward;
        Vec3 up = this.up;
        Vec3 right = forward.cross(up);
        Vector3f[] vertices = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
        for (int i = 0; i < 4; ++i) {
            float x = (float)(forward.x * (double)vertices[i].x + right.x * (double)vertices[i].y);
            float y = (float)(forward.y * (double)vertices[i].x + right.y * (double)vertices[i].y);
            float z = (float)(forward.z * (double)vertices[i].x + right.z * (double)vertices[i].y);
            vertices[i] = new Vector3f(x, y, z);
        }
        return vertices;
    }

    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp((double)partialTick, (double)this.xo, (double)this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)partialTick, (double)this.yo, (double)this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)partialTick, (double)this.zo, (double)this.z) - vec3.z());
        Vector3f[] vertices = new Vector3f[4];
        for (int i = 0; i < 4; ++i) {
            Vector3f localVertex = this.localVertices[i];
            vertices[i] = new Vector3f(localVertex.x, localVertex.y, localVertex.z);
            vertices[i].mul(this.getQuadSize(partialTick));
            vertices[i].add(f, f1, f2);
        }
        int j = this.getLightColor(partialTick);
        this.makeCornerVertex(buffer, vertices[0], this.getU1(), this.getV1(), j);
        this.makeCornerVertex(buffer, vertices[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[3], this.getU0(), this.getV1(), j);
        this.makeCornerVertex(buffer, vertices[3], this.getU0(), this.getV1(), j);
        this.makeCornerVertex(buffer, vertices[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(buffer, vertices[0], this.getU1(), this.getV1(), j);
    }

    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVec3f, float p_233996_, float p_233997_, int p_233998_) {
        pConsumer.addVertex(pVec3f.x(), pVec3f.y(), pVec3f.z()).setUv(p_233996_, p_233997_).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(p_233998_);
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    protected int getLightColor(float pPartialTick) {
        return 0xF000F0;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<EnderSlashParticleOptions> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull EnderSlashParticleOptions options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            EnderSlashParticle shriekparticle = new EnderSlashParticle(pLevel, pX, pY, pZ, this.sprite, pXSpeed, pYSpeed, pZSpeed, options);
            shriekparticle.setSpriteFromAge(this.sprite);
            shriekparticle.setAlpha(1.0f);
            return shriekparticle;
        }
    }
}

