/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.Vec2
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.shield;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldModel;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldTrimLayer;
import io.redspace.ironsspellbooks.render.RenderHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ShieldRenderer
extends EntityRenderer<ShieldEntity>
implements RenderLayerParent<ShieldEntity, ShieldModel> {
    public static ResourceLocation SPECTRAL_OVERLAY_TEXTURE = IronsSpellbooks.id("textures/entity/shield/shield_overlay.png");
    private static ResourceLocation SIGIL_TEXTURE = IronsSpellbooks.id("textures/block/scroll_forge_sigil.png");
    private final ShieldModel model;
    protected final List<RenderLayer<ShieldEntity, ShieldModel>> layers = new ArrayList<RenderLayer<ShieldEntity, ShieldModel>>();

    public ShieldRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ShieldModel(context.bakeLayer(ShieldModel.LAYER_LOCATION));
        this.layers.add(new ShieldTrimLayer(this, context));
    }

    public void render(ShieldEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
        Vec2 offset = ShieldRenderer.getEnergySwirlOffset(entity, partialTicks);
        VertexConsumer consumer = bufferSource.getBuffer(RenderHelper.CustomerRenderType.magicSwirl(this.getTextureLocation(entity), offset.x, offset.y));
        float width = (float)entity.width * 0.65f;
        poseStack.scale(width, width, width);
        RenderSystem.disableBlend();
        this.model.renderToBuffer(poseStack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY, RenderHelper.colorf(0.65f, 0.65f, 0.65f));
        for (RenderLayer<ShieldEntity, ShieldModel> layer : this.layers) {
            layer.render(poseStack, bufferSource, light, (Entity)entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private static float shittyNoise(float f) {
        return (float)(Math.sin(f / 4.0f) + 2.0 * Math.sin(f / 3.0f) + 3.0 * Math.sin(f / 2.0f) + 4.0 * Math.sin(f)) * 0.25f;
    }

    public static Vec2 getEnergySwirlOffset(ShieldEntity entity, float partialTicks, int offset) {
        float f = ((float)entity.tickCount + partialTicks) * 0.02f;
        return new Vec2(ShieldRenderer.shittyNoise(1.2f * f + (float)offset), ShieldRenderer.shittyNoise(f + 456.0f + (float)offset));
    }

    public static Vec2 getEnergySwirlOffset(ShieldEntity entity, float partialTicks) {
        return ShieldRenderer.getEnergySwirlOffset(entity, partialTicks, 0);
    }

    public ShieldModel getModel() {
        return this.model;
    }

    public ResourceLocation getTextureLocation(ShieldEntity entity) {
        return SPECTRAL_OVERLAY_TEXTURE;
    }
}

