/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$DestFactor
 *  com.mojang.blaze3d.platform.GlStateManager$SourceFactor
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.Util
 *  net.minecraft.client.Camera
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.texture.TextureAtlas
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package io.redspace.ironsspellbooks.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ZapParticle
extends TextureSheetParticle {
    private static final Vector3f ROTATION_VECTOR = (Vector3f)Util.make((Object)new Vector3f(0.5f, 0.5f, 0.5f), Vector3f::normalize);
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0f, -1.0f, 0.0f);
    private static final float DEGREES_90 = 1.5707964f;
    Vec3 destination;
    public static ParticleRenderType PARTICLE_EMISSIVE = new ParticleRenderType(){

        public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
            RenderSystem.depthMask((boolean)true);
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        public boolean isTranslucent() {
            return true;
        }

        public String toString() {
            return "irons_spellbooks:particle_emissive";
        }
    };

    ZapParticle(ClientLevel pLevel, double pX, double pY, double pZ, double xd, double yd, double zd, ZapParticleOption options) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        this.setSize(1.0f, 1.0f);
        this.quadSize = 1.0f;
        this.destination = options.getDestination();
        this.lifetime = Utils.random.nextIntBetweenInclusive(3, 8);
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
    }

    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    public Vector3f randomVector3f(RandomSource random, float scale) {
        return new Vector3f((2.0f * random.nextFloat() - 1.0f) * scale, (2.0f * random.nextFloat() - 1.0f) * scale, (2.0f * random.nextFloat() - 1.0f) * scale);
    }

    private void setRGBA(float r, float g, float b, float a) {
        this.rCol = r * a;
        this.gCol = g * a;
        this.bCol = b * a;
        this.alpha = 1.0f;
    }

    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp((double)partialTick, (double)this.xo, (double)this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)partialTick, (double)this.yo, (double)this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)partialTick, (double)this.zo, (double)this.z) - vec3.z());
        Vector3f start = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f end = new Vector3f((float)(this.destination.x - this.x), (float)(this.destination.y - this.y), (float)(this.destination.z - this.z));
        RandomSource randomSource = RandomSource.create((long)((long)(this.age + this.lifetime) * 3456798L));
        int segments = randomSource.nextIntBetweenInclusive(1, 3);
        end.mul(1.0f / (float)segments);
        for (int i = 0; i < segments; ++i) {
            Vector3f wiggle = this.randomVector3f(randomSource, 0.2f);
            end.add((Vector3fc)wiggle);
            this.drawLightningBeam(consumer, partialTick, f, f1, f2, start, end, 0.6f, randomSource);
            start = new Vector3f(end.x, end.y, end.z);
            end.sub((Vector3fc)wiggle);
            end.add((Vector3fc)end);
        }
    }

    private void drawLightningBeam(VertexConsumer consumer, float partialTick, float f, float f1, float f2, Vector3f start, Vector3f end, float chanceToBranch, RandomSource randomSource) {
        Vector3f d = new Vector3f(end.x() - start.x(), end.y() - start.y(), end.z() - start.z());
        d.normalize();
        Vec2 heading = new Vec2((float)Math.asin(-d.y()), (float)(-Mth.atan2((double)d.x(), (double)d.z())));
        this.setRGBA(1.0f, 1.0f, 1.0f, 1.0f);
        this.tube(consumer, partialTick, f, f1, f2, heading, start, end, 0.06f);
        this.setRGBA(0.25f, 0.7f, 1.0f, 0.3f);
        this.tube(consumer, partialTick, f, f1, f2, heading, start, end, 0.11f);
        this.setRGBA(0.25f, 0.7f, 1.0f, 0.15f);
        this.tube(consumer, partialTick, f, f1, f2, heading, start, end, 0.25f);
        if (randomSource.nextFloat() < chanceToBranch) {
            Vector3f branch = this.randomVector3f(randomSource, 0.5f);
            branch.add((Vector3fc)end);
            this.drawLightningBeam(consumer, partialTick, f, f1, f2, start, branch, chanceToBranch * 0.5f, randomSource);
        }
    }

    private void tube(VertexConsumer consumer, float partialTick, float f, float f1, float f2, Vec2 heading, Vector3f start, Vector3f end, float width) {
        float h = width * 0.5f;
        Vector3f[] left = new Vector3f[]{new Vector3f(-h * Mth.cos((float)heading.y) + start.x(), -h + start.y(), start.z() - h * Mth.sin((float)heading.y)), new Vector3f(-h * Mth.cos((float)heading.y) + start.x(), h + start.y(), start.z() - h * Mth.sin((float)heading.y)), new Vector3f(-h * Mth.cos((float)heading.y) + end.x(), h + end.y(), end.z() - h * Mth.sin((float)heading.y)), new Vector3f(-h * Mth.cos((float)heading.y) + end.x(), -h + end.y(), end.z() - h * Mth.sin((float)heading.y))};
        Vector3f[] right = new Vector3f[]{new Vector3f(h * Mth.cos((float)heading.y) + end.x(), -h + end.y(), end.z() + h * Mth.sin((float)heading.y)), new Vector3f(h * Mth.cos((float)heading.y) + end.x(), h + end.y(), end.z() + h * Mth.sin((float)heading.y)), new Vector3f(h * Mth.cos((float)heading.y) + start.x(), h + start.y(), start.z() + h * Mth.sin((float)heading.y)), new Vector3f(h * Mth.cos((float)heading.y) + start.x(), -h + start.y(), start.z() + h * Mth.sin((float)heading.y))};
        Vector3f[] top = new Vector3f[]{new Vector3f(h * Mth.cos((float)heading.y) + start.x(), -h + start.y(), start.z() + h * Mth.sin((float)heading.y)), new Vector3f(-h * Mth.cos((float)heading.y) + start.x(), -h + start.y(), start.z() - h * Mth.sin((float)heading.y)), new Vector3f(-h * Mth.cos((float)heading.y) + end.x(), -h + end.y(), end.z() - h * Mth.sin((float)heading.y)), new Vector3f(h * Mth.cos((float)heading.y) + end.x(), -h + end.y(), end.z() + h * Mth.sin((float)heading.y))};
        Vector3f[] bottom = new Vector3f[]{new Vector3f(h * Mth.cos((float)heading.y) + end.x(), h + end.y(), end.z() + h * Mth.sin((float)heading.y)), new Vector3f(-h * Mth.cos((float)heading.y) + end.x(), h + end.y(), end.z() - h * Mth.sin((float)heading.y)), new Vector3f(-h * Mth.cos((float)heading.y) + start.x(), h + start.y(), start.z() - h * Mth.sin((float)heading.y)), new Vector3f(h * Mth.cos((float)heading.y) + start.x(), h + start.y(), start.z() + h * Mth.sin((float)heading.y))};
        this.quad(consumer, partialTick, f, f1, f2, left);
        this.quad(consumer, partialTick, f, f1, f2, right);
        this.quad(consumer, partialTick, f, f1, f2, top);
        this.quad(consumer, partialTick, f, f1, f2, bottom);
    }

    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVec3f, float p_233996_, float p_233997_, int p_233998_) {
        pConsumer.addVertex(pVec3f.x(), pVec3f.y(), pVec3f.z()).setUv(p_233996_, p_233997_).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(p_233998_);
    }

    private void quad(VertexConsumer pConsumer, float partialTick, float f, float f1, float f2, Vector3f[] avector3f) {
        float f3 = this.getQuadSize(partialTick);
        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }
        int j = this.getLightColor(partialTick);
        this.makeCornerVertex(pConsumer, avector3f[0], this.getU1(), this.getV1(), j);
        this.makeCornerVertex(pConsumer, avector3f[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(pConsumer, avector3f[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(pConsumer, avector3f[3], this.getU0(), this.getV1(), j);
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return PARTICLE_EMISSIVE;
    }

    public AABB getRenderBoundingBox(float partialTicks) {
        return AABB.INFINITE;
    }

    protected int getLightColor(float pPartialTick) {
        return 0xF000F0;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<ZapParticleOption> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull ZapParticleOption options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            ZapParticle particle = new ZapParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, options);
            particle.pickSprite(this.sprite);
            particle.setAlpha(1.0f);
            return particle;
        }
    }
}

