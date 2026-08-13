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
import io.redspace.ironsspellbooks.particle.TraceParticleOptions;
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

public class TraceParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final Vec3 destination;
    private final Vec3 forward;
    private final double speed;

    TraceParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double xd, double yd, double zd, TraceParticleOptions options) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        this.lifetime = 4 + this.level.random.nextInt(5);
        this.gravity = 0.0f;
        this.sprites = spriteSet;
        this.quadSize = 0.75f + this.level.random.nextFloat() * 0.25f;
        this.destination = new Vec3((double)options.destination.x, (double)options.destination.y, (double)options.destination.z);
        this.forward = this.destination.subtract(new Vec3(pX, pY, pZ)).normalize();
        this.speed = new Vec3(xd, yd, zd).length();
        this.rCol = options.color.x;
        this.gCol = options.color.y;
        this.bCol = options.color.z;
        this.friction = 1.0f;
    }

    private Vec3 vec3Copy(Vector3f vector3f) {
        return new Vec3((double)vector3f.x, (double)vector3f.y, (double)vector3f.z);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.move(this.forward.x * this.speed, this.forward.y * this.speed, this.forward.z * this.speed);
        if (this.age++ > this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    public float getQuadSize(float scaleFactor) {
        float f = ((float)this.age + scaleFactor) / (float)this.lifetime;
        f *= f;
        return Mth.lerp((float)f, (float)this.quadSize, (float)(this.quadSize * 0.5f));
    }

    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp((double)partialTick, (double)this.xo, (double)this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)partialTick, (double)this.yo, (double)this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)partialTick, (double)this.zo, (double)this.z) - vec3.z());
        Vec3 ray = this.getPos().subtract(vec3).normalize();
        Vec3 forward = this.forward;
        Vec3 up = forward.cross(ray);
        Vector3f[] vertices = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
        for (int i = 0; i < 4; ++i) {
            float x = (float)(forward.x * (double)vertices[i].x + up.x * (double)vertices[i].y);
            float y = (float)(forward.y * (double)vertices[i].x + up.y * (double)vertices[i].y);
            float z = (float)(forward.z * (double)vertices[i].x + up.z * (double)vertices[i].y);
            vertices[i] = new Vector3f(x, y, z);
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
    implements ParticleProvider<TraceParticleOptions> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull TraceParticleOptions options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            TraceParticle shriekparticle = new TraceParticle(pLevel, pX, pY, pZ, this.sprite, pXSpeed, pYSpeed, pZSpeed, options);
            shriekparticle.setSpriteFromAge(this.sprite);
            shriekparticle.setAlpha(1.0f);
            return shriekparticle;
        }
    }
}

