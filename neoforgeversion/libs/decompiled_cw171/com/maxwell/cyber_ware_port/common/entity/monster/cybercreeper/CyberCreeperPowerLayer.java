/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.EnergySwirlLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 */
package com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper;

import com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper.CyberCreeperEntity;
import com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper.CyberCreeperModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class CyberCreeperPowerLayer
extends EnergySwirlLayer<CyberCreeperEntity, CyberCreeperModel> {
    private static final ResourceLocation POWER_LOCATION = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/entity/creeper/creeper_armor.png");
    private final CyberCreeperModel model;

    public CyberCreeperPowerLayer(RenderLayerParent<CyberCreeperEntity, CyberCreeperModel> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer);
        this.model = new CyberCreeperModel(pModelSet.bakeLayer(CyberCreeperModel.ARMOR_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, CyberCreeperEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isPowered()) {
            super.render(poseStack, buffer, packedLight, (Entity)entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }

    protected float xOffset(float pTickCount) {
        return pTickCount * 0.01f;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<CyberCreeperEntity> model() {
        return this.model;
    }
}

