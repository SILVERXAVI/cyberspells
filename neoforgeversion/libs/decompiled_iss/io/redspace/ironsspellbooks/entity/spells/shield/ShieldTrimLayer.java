/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.phys.Vec2
 */
package io.redspace.ironsspellbooks.entity.spells.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldModel;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldRenderer;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldTrimModel;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

public class ShieldTrimLayer
extends RenderLayer<ShieldEntity, ShieldModel> {
    private static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/shield/shield_trim.png");
    private final ShieldTrimModel model;

    public ShieldTrimLayer(RenderLayerParent<ShieldEntity, ShieldModel> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.model = new ShieldTrimModel(context.bakeLayer(ShieldTrimModel.LAYER_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ShieldEntity entity, float pLimbSwing, float pLimbSwingAmount, float partialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        Vec2 offset = ShieldRenderer.getEnergySwirlOffset(entity, partialTicks, 3456);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.energySwirl((ResourceLocation)TEXTURE, (float)0.0f, (float)0.0f));
        this.model.renderToBuffer(poseStack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY, RenderHelper.colorf(0.65f, 0.65f, 0.65f));
    }
}

