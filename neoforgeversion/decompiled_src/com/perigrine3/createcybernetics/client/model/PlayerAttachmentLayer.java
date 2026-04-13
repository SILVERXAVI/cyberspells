/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.Model
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.ModelPart
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
package com.perigrine3.createcybernetics.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.client.model.AttachmentAnchor;
import com.perigrine3.createcybernetics.client.model.PlayerAttachment;
import com.perigrine3.createcybernetics.client.model.PlayerAttachmentManager;
import com.perigrine3.createcybernetics.client.model.PlayerAttachmentState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
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

public final class PlayerAttachmentLayer
extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public PlayerAttachmentLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        PlayerAttachmentState state = PlayerAttachmentManager.getState(player);
        if (state == null || state.isEmpty()) {
            return;
        }
        PlayerModel pm = (PlayerModel)this.getParentModel();
        PlayerSkin.Model modelType = player.getSkin().model();
        Minecraft mc = Minecraft.getInstance();
        Entity cam = mc.getCameraEntity();
        boolean isFirstPerson = mc.options.getCameraType().isFirstPerson();
        boolean isLocalViewTarget = cam == player;
        boolean isGuiRender = mc.screen != null;
        boolean viewerCanSee = PlayerAttachmentLayer.shouldRenderToViewer(player);
        for (PlayerAttachment att : state.all()) {
            ModelPart anchorPart;
            if (att.respectsInvisibility() && !viewerCanSee || att.thirdPersonOnly() && isFirstPerson && isLocalViewTarget && !isGuiRender || (anchorPart = PlayerAttachmentLayer.resolveAnchor(pm, att.anchor())) == null) continue;
            poseStack.pushPose();
            try {
                anchorPart.translateAndRotate(poseStack);
                att.setupPose(poseStack, player, (PlayerModel<AbstractClientPlayer>)pm, modelType, partialTick);
                ResourceLocation tex = att.texture(modelType);
                Model m = att.model(modelType);
                RenderType rt = m.renderType(tex);
                VertexConsumer vc = buffer.getBuffer(rt);
                m.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, att.color());
            }
            finally {
                poseStack.popPose();
            }
        }
    }

    private static ModelPart resolveAnchor(PlayerModel<?> pm, AttachmentAnchor a) {
        return switch (a) {
            default -> throw new MatchException(null, null);
            case AttachmentAnchor.HEAD -> pm.head;
            case AttachmentAnchor.BODY -> pm.body;
            case AttachmentAnchor.RIGHT_ARM -> pm.rightArm;
            case AttachmentAnchor.LEFT_ARM -> pm.leftArm;
            case AttachmentAnchor.RIGHT_LEG -> pm.rightLeg;
            case AttachmentAnchor.LEFT_LEG -> pm.leftLeg;
        };
    }

    private static boolean shouldRenderToViewer(AbstractClientPlayer target) {
        Minecraft mc = Minecraft.getInstance();
        Entity cam = mc.getCameraEntity();
        if (cam instanceof Player) {
            Player viewer = (Player)cam;
            return !target.isInvisibleTo(viewer);
        }
        return !target.isInvisible();
    }
}

