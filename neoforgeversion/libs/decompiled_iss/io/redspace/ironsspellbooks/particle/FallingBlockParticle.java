/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.RenderBuffers
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  net.neoforged.neoforge.client.model.data.ModelData
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.particle.FallingBlockParticleOption;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(value={Dist.CLIENT})
public class FallingBlockParticle
extends TextureSheetParticle {
    private final BlockState blockState;
    private final boolean particlesOnImpact;
    private final BlockPos originalPos;
    private static final List<Renderable> toRender = new ArrayList<Renderable>();

    @SubscribeEvent
    public static void globalrender(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            toRender.clear();
            return;
        }
        RenderBuffers bufs = Minecraft.getInstance().renderBuffers();
        MultiBufferSource.BufferSource buf = bufs.bufferSource();
        for (Renderable particle : toRender) {
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            poseStack.translate((float)particle.relativePos.x, (float)particle.relativePos.y, (float)particle.relativePos.z);
            BlockPos blockpos = particle.worldPos.above();
            poseStack.translate(-0.5, 0.0, -0.5);
            BakedModel model = dispatcher.getBlockModel(particle.state);
            for (RenderType renderType : model.getRenderTypes(particle.state, RandomSource.create((long)0L), ModelData.EMPTY)) {
                dispatcher.getModelRenderer().tesselateBlock((BlockAndTintGetter)level, model, particle.state, blockpos, poseStack, buf.getBuffer(renderType), false, RandomSource.create(), particle.state.getSeed(particle.originalPos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
            }
            poseStack.popPose();
        }
        toRender.clear();
    }

    FallingBlockParticle(ClientLevel pLevel, double pX, double pY, double pZ, double xd, double yd, double zd, FallingBlockParticleOption options) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        this.xd = options.getMotion().x;
        this.yd = options.getMotion().y;
        this.zd = options.getMotion().z;
        this.lifetime = 200;
        this.quadSize = 1.0f;
        this.blockState = options.getState();
        this.gravity = 0.08f;
        this.originalPos = BlockPos.containing((double)this.x, (double)this.y, (double)this.z);
        this.particlesOnImpact = false;
    }

    public void tick() {
        boolean onGround = this.onGround;
        ++this.age;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.move(this.xd, this.yd, this.zd);
        this.yd -= (double)this.gravity;
        if (this.blockState.isAir() || onGround || this.age > this.lifetime) {
            if (onGround && this.particlesOnImpact) {
                double speed = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
                for (int i = 0; i < 25; ++i) {
                    Vec3 random = Utils.getRandomVec3(1.0).multiply(1.0, 0.25, 1.0).normalize().scale(speed * 10.0 + 0.1);
                    this.level.addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, this.blockState), this.x, this.y, this.z, random.x, random.y, random.z);
                }
            }
            this.remove();
        }
    }

    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        if (this.blockState.getRenderShape() == RenderShape.MODEL) {
            Vec3 vec3 = camera.getPosition();
            float f = (float)(Mth.lerp((double)partialTick, (double)this.xo, (double)this.x) - vec3.x());
            float f1 = (float)(Mth.lerp((double)partialTick, (double)this.yo, (double)this.y) - vec3.y());
            float f2 = (float)(Mth.lerp((double)partialTick, (double)this.zo, (double)this.z) - vec3.z());
            toRender.add(new Renderable(BlockPos.containing((double)this.x, (double)this.y, (double)this.z), this.originalPos, new Vec3((double)f, (double)f1, (double)f2), this.blockState));
        }
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    protected int getLightColor(float pPartialTick) {
        return 0xF000F0;
    }

    record Renderable(BlockPos worldPos, BlockPos originalPos, Vec3 relativePos, BlockState state) {
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<FallingBlockParticleOption> {
        public Particle createParticle(@NotNull FallingBlockParticleOption options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new FallingBlockParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, options);
        }
    }
}

