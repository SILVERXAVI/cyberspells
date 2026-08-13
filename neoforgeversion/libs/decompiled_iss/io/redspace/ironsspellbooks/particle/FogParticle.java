/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.Util
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
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.particle.FogParticleOptions;
import java.util.function.Consumer;
import net.minecraft.Util;
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
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public class FogParticle
extends TextureSheetParticle {
    private static final Vector3f ROTATION_VECTOR = (Vector3f)Util.make((Object)new Vector3f(0.5f, 0.5f, 0.5f), Vector3f::normalize);
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0f, -1.0f, 0.0f);
    private static final float DEGREES_90 = 1.5707964f;

    FogParticle(ClientLevel pLevel, double pX, double pY, double pZ, double xd, double yd, double zd, FogParticleOptions options) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        float mag = 0.3f;
        this.xd = xd + (Math.random() * 2.0 - 1.0) * (double)mag;
        this.yd = yd + (Math.random() * 2.0 - 1.0) * (double)mag;
        this.zd = zd + (Math.random() * 2.0 - 1.0) * (double)mag;
        double d0 = (Math.random() + Math.random() + 1.0) * (double)mag * (double)0.3f;
        double d1 = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
        this.xd = this.xd / d1 * d0 * (double)mag;
        this.yd = this.yd / d1 * d0 * (double)mag + (double)(mag * 0.25f);
        this.zd = this.zd / d1 * d0 * (double)mag;
        this.quadSize = 1.5f * options.getScale();
        this.lifetime = Utils.random.nextIntBetweenInclusive(60, 120);
        this.gravity = 0.1f;
        float f = this.random.nextFloat() * 0.14f + 0.85f;
        this.rCol = options.getColor().x() * f;
        this.gCol = options.getColor().y() * f;
        this.bCol = options.getColor().z() * f;
        this.friction = 1.0f;
    }

    public float getQuadSize(float pScaleFactor) {
        return this.quadSize * (1.0f + Mth.clamp((float)(((float)this.age + pScaleFactor) / (float)this.lifetime * 0.75f), (float)0.0f, (float)1.0f)) * Mth.clamp((float)((float)this.age / 5.0f), (float)0.0f, (float)1.0f);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= 0.04 * (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.yd *= (double)0.85f;
            this.xd *= (double)0.94f;
            this.zd *= (double)0.94f;
        }
    }

    private float noise(float offset) {
        float f = 10.0f * Mth.sin((float)(offset * 0.01f));
        return f;
    }

    public void render(VertexConsumer buffer, Camera camera, float partialticks) {
        this.alpha = 1.0f - Mth.clamp((float)(((float)this.age + partialticks - 20.0f) / (float)this.lifetime), (float)0.2f, (float)0.7f);
        this.renderRotatedParticle(buffer, camera, partialticks, p_234005_ -> {
            p_234005_.mul((Quaternionfc)Axis.YP.rotation(0.0f));
            p_234005_.mul((Quaternionfc)Axis.XP.rotation(-1.5707964f));
        });
        this.renderRotatedParticle(buffer, camera, partialticks, p_234000_ -> {
            p_234000_.mul((Quaternionfc)Axis.YP.rotation((float)(-Math.PI)));
            p_234000_.mul((Quaternionfc)Axis.XP.rotation(1.5707964f));
        });
    }

    private void renderRotatedParticle(VertexConsumer pConsumer, Camera camera, float partialTick, Consumer<Quaternionf> pQuaternion) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp((double)partialTick, (double)this.xo, (double)this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)partialTick, (double)this.yo, (double)this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)partialTick, (double)this.zo, (double)this.z) - vec3.z());
        Quaternionf quaternion = new Quaternionf().setAngleAxis(0.0f, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        pQuaternion.accept(quaternion);
        quaternion.transform(TRANSFORM_VECTOR);
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
        Vec3 wiggle = new Vec3((double)this.noise((float)((double)this.age + this.x)), (double)this.noise((float)((double)this.age - this.x)), (double)this.noise((float)((double)this.age + this.z))).scale((double)0.02f);
        pConsumer.addVertex(pVec3f.x() + (float)wiggle.x, pVec3f.y() + 0.08f + this.alpha * 0.125f, pVec3f.z() + (float)wiggle.z).setUv(p_233996_, p_233997_).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(p_233998_);
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<FogParticleOptions> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull FogParticleOptions options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            FogParticle shriekparticle = new FogParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, options);
            shriekparticle.pickSprite(this.sprite);
            shriekparticle.setAlpha(1.0f);
            return shriekparticle;
        }
    }
}

