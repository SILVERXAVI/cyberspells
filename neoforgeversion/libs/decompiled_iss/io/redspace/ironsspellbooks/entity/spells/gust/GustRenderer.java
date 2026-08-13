/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
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
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 */
package io.redspace.ironsspellbooks.entity.spells.gust;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.gust.GustCollider;
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
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class GustRenderer
extends EntityRenderer<GustCollider> {
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"gust_model"), "main");
    private static ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/trident_riptide.png");
    private final ModelPart body;

    public GustRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -16.0f, -8.0f, 16.0f, 32.0f, 16.0f), PartPose.ZERO);
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)64);
    }

    public void render(GustCollider entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0, entity.getBoundingBox().getYsize() * 0.5, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot() - 180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.getXRot() - 90.0f));
        poseStack.scale(0.25f, 0.25f, 0.25f);
        float f = (float)entity.tickCount + partialTicks;
        float scale = Mth.lerp((float)Mth.clamp((float)(f / 6.0f), (float)0.0f, (float)1.0f), (float)1.0f, (float)2.3f);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent((ResourceLocation)this.getTextureLocation(entity)));
        float alpha = 1.0f - f / 10.0f;
        for (int i = 0; i < 3; ++i) {
            poseStack.mulPose(Axis.YP.rotationDegrees(f * 10.0f));
            poseStack.scale(scale, scale, scale);
            poseStack.translate(0.0f, scale - 1.0f, 0.0f);
            this.body.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int)((int)(alpha * 255.0f)), (int)255, (int)255, (int)255));
        }
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(GustCollider entity) {
        return TEXTURE;
    }
}

