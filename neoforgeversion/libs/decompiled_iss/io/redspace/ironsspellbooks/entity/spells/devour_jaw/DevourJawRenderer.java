/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.EvokerFangsModel
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 */
package io.redspace.ironsspellbooks.entity.spells.devour_jaw;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.devour_jaw.DevourJaw;
import java.util.Objects;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class DevourJawRenderer
extends EntityRenderer<DevourJaw> {
    private final DevourJawModel model;

    public DevourJawRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new DevourJawModel(pContext.bakeLayer(ModelLayers.EVOKER_FANGS));
    }

    public void render(DevourJaw entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (entity.tickCount < entity.waitTime) {
            return;
        }
        float f = (float)entity.tickCount + partialTicks;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.scale(1.85f, 1.85f, 1.85f);
        this.model.setupAnim(entity, f, 0.0f, 0.0f, entity.getYRot(), entity.getXRot());
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, multiBufferSource, light);
    }

    public ResourceLocation getTextureLocation(DevourJaw pEntity) {
        return IronsSpellbooks.id("textures/entity/devour_jaw.png");
    }

    static class DevourJawModel
    extends EvokerFangsModel<DevourJaw> {
        private final ModelPart root;
        private final ModelPart base;
        private final ModelPart upperJaw;
        private final ModelPart lowerJaw;

        public DevourJawModel(ModelPart pRoot) {
            super(pRoot);
            this.root = pRoot;
            this.base = pRoot.getChild("base");
            this.upperJaw = pRoot.getChild("upper_jaw");
            this.lowerJaw = pRoot.getChild("lower_jaw");
        }

        public void setupAnim(DevourJaw entity, float time, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            Objects.requireNonNull(entity);
            float interval = entity.warmupTime - entity.waitTime;
            float f = Mth.clamp((float)((time -= 5.0f) / interval), (float)0.0f, (float)1.0f);
            f = 1.0f - f * f * f * f;
            this.upperJaw.zRot = (float)Math.PI - f * 0.35f * (float)Math.PI;
            this.lowerJaw.zRot = (float)Math.PI + f * 0.35f * (float)Math.PI;
            float f2 = time / interval;
            f2 = 0.5f * Mth.cos((float)(1.5707964f * (f2 - 1.0f))) + 0.5f;
            f2 *= f2;
            this.lowerJaw.y = this.upperJaw.y = -18.0f * f2 + 16.0f;
            this.base.y = this.upperJaw.y;
        }
    }
}

