/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.ElytraModel
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.ElytraLayer
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Items
 */
package com.perigrine3.createcybernetics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.client.CyberElytraClient;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class CyberElytraRenderLayer
extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final ElytraModel<AbstractClientPlayer> cyberElytraModel;
    private final ElytraLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> vanillaHelper;

    public CyberElytraRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, EntityModelSet modelSet) {
        super(parent);
        this.cyberElytraModel = new ElytraModel(modelSet.bakeLayer(CyberElytraClient.CYBER_ELYTRA_LAYER));
        this.vanillaHelper = new ElytraLayer(parent, modelSet);
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!CyberElytraRenderLayer.shouldRenderCyberElytra(player)) {
            return;
        }
        if (player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 0.125f);
        EntityModel parentModel = this.getParentModel();
        parentModel.copyPropertiesTo(this.cyberElytraModel);
        this.cyberElytraModel.setupAnim((LivingEntity)player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ResourceLocation tex = this.vanillaHelper.getElytraTexture(Items.ELYTRA.getDefaultInstance(), (LivingEntity)player);
        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)tex));
        this.cyberElytraModel.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, 0x999999);
        poseStack.popPose();
    }

    private static boolean shouldRenderCyberElytra(AbstractClientPlayer player) {
        if (player == null) {
            return false;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        if (ModItems.BONEUPGRADES_ELYTRA != null) {
            Item item = (Item)ModItems.BONEUPGRADES_ELYTRA.get();
            if (!(item instanceof ICyberwareItem)) {
                return false;
            }
            ICyberwareItem cw = (ICyberwareItem)item;
            return cw.isEnabledByWheel((Player)player);
        }
        return false;
    }
}

