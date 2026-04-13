/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 */
package com.perigrine3.createcybernetics.client.skin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.client.skin.SkinModifier;
import com.perigrine3.createcybernetics.client.skin.SkinModifierManager;
import com.perigrine3.createcybernetics.client.skin.SkinModifierState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class SkinLayerHandler
extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public SkinLayerHandler(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    private static boolean shouldRenderOverlaysFor(AbstractClientPlayer target) {
        Player viewer;
        Minecraft mc = Minecraft.getInstance();
        Entity cam = mc.getCameraEntity();
        return !(cam instanceof Player ? target.isInvisibleTo(viewer = (Player)cam) : target.isInvisible());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!SkinLayerHandler.shouldRenderOverlaysFor(player)) {
            return;
        }
        SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
        if (state == null || !state.hasModifiers()) {
            return;
        }
        PlayerModel model = (PlayerModel)this.getParentModel();
        boolean prevHat = model.hat.visible;
        boolean prevJacket = model.jacket.visible;
        boolean prevLeftSleeve = model.leftSleeve.visible;
        boolean prevRightSleeve = model.rightSleeve.visible;
        boolean prevLeftPants = model.leftPants.visible;
        boolean prevRightPants = model.rightPants.visible;
        model.hat.visible = true;
        model.jacket.visible = true;
        model.leftSleeve.visible = true;
        model.rightSleeve.visible = true;
        model.leftPants.visible = true;
        model.rightPants.visible = true;
        try {
            for (SkinModifier modifier : state.getModifiers()) {
                poseStack.pushPose();
                poseStack.scale(1.0f, 1.0f, 1.0f);
                PlayerSkin.Model modelType = player.getSkin().model();
                ResourceLocation texture = modifier.getTexture(modelType);
                int color = modifier.getColor();
                VertexConsumer baseVc = buffer.getBuffer(RenderType.entityTranslucent((ResourceLocation)texture));
                model.renderToBuffer(poseStack, baseVc, packedLight, OverlayTexture.NO_OVERLAY, color);
                if (modifier.hasGlint()) {
                    VertexConsumer glintVc = buffer.getBuffer(RenderType.entityGlint());
                    model.renderToBuffer(poseStack, glintVc, packedLight, OverlayTexture.NO_OVERLAY, -1);
                }
                poseStack.popPose();
            }
        }
        finally {
            model.hat.visible = prevHat;
            model.jacket.visible = prevJacket;
            model.leftSleeve.visible = prevLeftSleeve;
            model.rightSleeve.visible = prevRightSleeve;
            model.leftPants.visible = prevLeftPants;
            model.rightPants.visible = prevRightPants;
        }
    }
}

