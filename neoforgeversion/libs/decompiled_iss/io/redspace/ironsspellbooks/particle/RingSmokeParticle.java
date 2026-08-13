/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Camera
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.util.Mth
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public class RingSmokeParticle
extends TextureSheetParticle {
    private static final Vector3f ROTATION_VECTOR = new Vector3f(0.0f, 0.0f, 0.0f);
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0f, -1.0f, 0.0f);
    private final float targetSize;
    private final boolean isFullbright;
    private float rx;
    private float ry;

    RingSmokeParticle(ClientLevel pLevel, double pX, double pY, double pZ, double xd, double yd, double zd) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        this.xd = xd * (double)0.7f;
        this.yd = yd * (double)0.7f;
        this.zd = zd * (double)0.7f;
        Vec3 deltaMovement = new Vec3(xd, yd, zd);
        this.rx = (float)(-Math.asin(yd / deltaMovement.length()));
        this.ry = 1.5707964f - (float)Mth.atan2((double)zd, (double)xd);
        this.targetSize = 2.5f;
        this.quadSize = 0.5f;
        this.lifetime = (int)(20.0 + Mth.absMax((double)0.0, (double)((double)this.targetSize - deltaMovement.length() * 5.0)) * 20.0);
        this.gravity = 0.0f;
        float f = this.random.nextFloat() * 0.14f + 0.85f;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.friction = 1.0f;
        this.isFullbright = false;
    }

    public float getQuadSize(float partialTick) {
        float f = (partialTick + (float)this.age) / (float)this.lifetime;
        return Mth.lerp((float)f, (float)0.15f, (float)this.targetSize);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.yd *= (double)0.99f;
            this.xd *= (double)0.99f;
            this.zd *= (double)0.99f;
        }
    }

    public void render(VertexConsumer buffer, Camera camera, float partialticks) {
        this.alpha = 1.0f - Mth.clamp((float)(((float)this.age + partialticks) / (float)this.lifetime), (float)0.0f, (float)1.0f);
        this.renderRotatedParticle(buffer, camera, partialticks, quaternion -> {
            quaternion.mul((Quaternionfc)Axis.YP.rotation(this.ry));
            quaternion.mul((Quaternionfc)Axis.XP.rotation(this.rx));
        });
        this.renderRotatedParticle(buffer, camera, partialticks, quaternion -> {
            quaternion.mul((Quaternionfc)Axis.YP.rotation(this.ry));
            quaternion.mul((Quaternionfc)Axis.XP.rotation((float)Math.PI));
            quaternion.mul((Quaternionfc)Axis.XP.rotation(this.rx));
        });
    }

    private void renderRotatedParticle(VertexConsumer pConsumer, Camera camera, float partialTick, Consumer<Quaternionf> pQuaternion) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp((double)partialTick, (double)this.xo, (double)this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)partialTick, (double)this.yo, (double)this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)partialTick, (double)this.zo, (double)this.z) - vec3.z());
        Quaternionf quaternion = new Quaternionf().setAngleAxis(0.0f, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        pQuaternion.accept(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
        float f3 = this.getQuadSize(partialTick);
        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate((Quaternionfc)quaternion);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }
        int j = this.getLightColor(partialTick);
        this.makeCornerVertex(pConsumer, avector3f[0], this.getU1(), this.getV1(), j);
        this.makeCornerVertex(pConsumer, avector3f[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(pConsumer, avector3f[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(pConsumer, avector3f[3], this.getU0(), this.getV1(), j);
    }

    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVec3f, float p_233996_, float p_233997_, int p_233998_) {
        pConsumer.addVertex(pVec3f.x(), pVec3f.y(), pVec3f.z()).setUv(p_233996_, p_233997_).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(p_233998_);
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    protected int getLightColor(float pPartialTick) {
        if (this.isFullbright) {
            return 0xF000F0;
        }
        BlockPos blockpos = BlockPos.containing((double)this.x, (double)this.y, (double)this.z).above();
        return this.level.hasChunkAt(blockpos) ? LevelRenderer.getLightColor((BlockAndTintGetter)this.level, (BlockPos)blockpos) : 0;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull SimpleParticleType options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            RingSmokeParticle shriekparticle = new RingSmokeParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            shriekparticle.pickSprite(this.sprite);
            shriekparticle.setAlpha(1.0f);
            return shriekparticle;
        }
    }
}

