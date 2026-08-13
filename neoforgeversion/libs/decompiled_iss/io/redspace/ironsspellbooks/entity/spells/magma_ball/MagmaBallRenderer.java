/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.spells.magma_ball;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.acid_orb.AcidOrbRenderer;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireBomb;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MagmaBallRenderer
extends EntityRenderer<FireBomb> {
    private static ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/fireball/magma.png");
    private static ResourceLocation[] SWIRL_TEXTURES = new ResourceLocation[]{IronsSpellbooks.id("textures/entity/fireball/swirl_0.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_1.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_2.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_3.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_4.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_5.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_6.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_7.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_8.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_9.png"), IronsSpellbooks.id("textures/entity/fireball/swirl_10.png")};
    private final ModelPart orb;
    private final ModelPart swirl;

    public MagmaBallRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(AcidOrbRenderer.MODEL_LAYER_LOCATION);
        this.orb = modelpart.getChild("orb");
        this.swirl = modelpart.getChild("swirl");
    }

    public void render(FireBomb entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0, entity.getBoundingBox().getYsize() * 0.5, 0.0);
        Vec3 motion = entity.getDeltaMovement();
        float xRot = -((float)(Mth.atan2((double)motion.horizontalDistance(), (double)motion.y) * 57.2957763671875) - 90.0f);
        float yRot = -((float)(Mth.atan2((double)motion.z, (double)motion.x) * 57.2957763671875) + 90.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        float f = (float)entity.tickCount + partialTicks;
        float swirlX = Mth.cos((float)(0.08f * f)) * 130.0f;
        float swirlY = Mth.sin((float)(0.08f * f)) * 130.0f;
        float swirlZ = Mth.cos((float)(0.08f * f + 5464.0f)) * 130.0f;
        poseStack.mulPose(Axis.XP.rotationDegrees(swirlX));
        poseStack.mulPose(Axis.YP.rotationDegrees(swirlY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(swirlZ));
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getTextureLocation(entity)));
        this.orb.render(poseStack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY);
        poseStack.mulPose(Axis.XP.rotationDegrees(swirlX));
        poseStack.mulPose(Axis.YP.rotationDegrees(swirlY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(swirlZ));
        consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getSwirlTextureLocation(entity)));
        poseStack.scale(1.15f, 1.15f, 1.15f);
        this.swirl.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(FireBomb entity) {
        return TEXTURE;
    }

    private ResourceLocation getSwirlTextureLocation(FireBomb entity) {
        int frame = entity.tickCount % SWIRL_TEXTURES.length;
        return SWIRL_TEXTURES[frame];
    }
}

