/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.client.skin.SkinHighlight;
import com.perigrine3.createcybernetics.client.skin.SkinModifierManager;
import com.perigrine3.createcybernetics.client.skin.SkinModifierState;
import com.perigrine3.createcybernetics.client.skin.SkinRenderTypes;
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

public final class SkinHighlightLayer
extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public SkinHighlightLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
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
        if (!SkinHighlightLayer.shouldRenderOverlaysFor(player)) {
            return;
        }
        SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
        if (state == null || !state.hasHighlights()) {
            return;
        }
        PlayerSkin.Model modelType = player.getSkin().model();
        RenderSystem.enableBlend();
        try {
            for (SkinHighlight highlight : state.getHighlights()) {
                int color;
                RenderType rt;
                int light;
                if (highlight == null) continue;
                ResourceLocation tex = highlight.getTexture(modelType);
                boolean emissive = highlight.isEmissive();
                boolean tintOnEmissive = highlight.tintOnEmissive();
                if (emissive) {
                    light = 0xF000F0;
                    if (tintOnEmissive) {
                        rt = SkinRenderTypes.emissiveTinted(tex);
                        color = highlight.getColor();
                    } else {
                        rt = RenderType.entityTranslucent((ResourceLocation)tex);
                        color = -1;
                    }
                } else {
                    light = packedLight;
                    rt = RenderType.entityTranslucent((ResourceLocation)tex);
                    color = highlight.getColor();
                }
                VertexConsumer vc = buffer.getBuffer(rt);
                ((PlayerModel)this.getParentModel()).renderToBuffer(poseStack, vc, light, OverlayTexture.NO_OVERLAY, color);
            }
        }
        finally {
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }
}

