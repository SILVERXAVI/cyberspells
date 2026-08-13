/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.thunderstep;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.thunderstep.ThunderstepProjectile;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ThunderstepProjectileRenderer
extends EntityRenderer<ThunderstepProjectile> {
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ball_lightning_model"), "main");
    private static final ResourceLocation[] SWIRL_TEXTURES = new ResourceLocation[]{IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_0.png"), IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_1.png"), IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_2.png"), IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_3.png"), IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_4.png")};
    private final ModelPart orb;

    public ThunderstepProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
        this.orb = modelpart.getChild("orb");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("orb", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f), PartPose.ZERO);
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)8, (int)8);
    }

    public void render(ThunderstepProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0, entity.getBoundingBox().getYsize() * 0.5, 0.0);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getTextureLocation(entity)));
        poseStack.scale(0.2f, 0.2f, 0.2f);
        for (int i = 0; i < 3; ++i) {
            poseStack.pushPose();
            float r = 0.25f;
            float g = 0.8f;
            float b = 1.0f;
            r = Mth.clamp((float)(r + r * (float)i), (float)0.0f, (float)1.0f);
            g = Mth.clamp((float)(g + g * (float)i), (float)0.0f, (float)1.0f);
            b = Mth.clamp((float)(b + b * (float)i), (float)0.0f, (float)1.0f);
            float f = (float)entity.tickCount + partialTicks + (float)(i * 777);
            float swirlX = Mth.cos((float)(0.065f * f)) * 180.0f;
            float swirlY = Mth.sin((float)(0.065f * f)) * 180.0f;
            float swirlZ = Mth.cos((float)(0.065f * f + 5464.0f)) * 180.0f;
            float scalePerLayer = 0.2f;
            poseStack.mulPose(Axis.XP.rotationDegrees(swirlX * (float)((int)Math.pow(-1.0, i))));
            poseStack.mulPose(Axis.YP.rotationDegrees(swirlY * (float)((int)Math.pow(-1.0, i))));
            poseStack.mulPose(Axis.ZP.rotationDegrees(swirlZ * (float)((int)Math.pow(-1.0, i))));
            consumer = bufferSource.getBuffer(RenderHelper.CustomerRenderType.magic(this.getSwirlTextureLocation(entity, i * i)));
            float scale = 2.0f - (float)i * scalePerLayer;
            poseStack.scale(scale, scale, scale);
            this.orb.render(poseStack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY, RenderHelper.colorf(r, g, b));
            poseStack.popPose();
        }
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(ThunderstepProjectile entity) {
        return SWIRL_TEXTURES[0];
    }

    private ResourceLocation getSwirlTextureLocation(ThunderstepProjectile entity, int offset) {
        int frame = (entity.tickCount + offset) % SWIRL_TEXTURES.length;
        return SWIRL_TEXTURES[frame];
    }
}

