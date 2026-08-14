/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 */
package com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper;

import com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper.CyberCreeperEntity;
import com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper.CyberCreeperModel;
import com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper.CyberCreeperPowerLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CyberCreeperRenderer
extends MobRenderer<CyberCreeperEntity, CyberCreeperModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/entity/cyber_creeper.png");

    public CyberCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, (EntityModel)new CyberCreeperModel(context.bakeLayer(CyberCreeperModel.LAYER_LOCATION)), 0.5f);
        this.addLayer((RenderLayer)new CyberCreeperPowerLayer((RenderLayerParent<CyberCreeperEntity, CyberCreeperModel>)this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(CyberCreeperEntity entity) {
        return TEXTURE;
    }

    protected void scale(CyberCreeperEntity entity, PoseStack poseStack, float partialTick) {
        float swell = entity.getSwelling(partialTick);
        float f1 = 1.0f + Mth.sin((float)(swell * 100.0f)) * swell * 0.01f;
        swell = Mth.clamp((float)swell, (float)0.0f, (float)1.0f);
        swell *= swell;
        swell *= swell;
        float f2 = (1.0f + swell * 0.4f) * f1;
        float f3 = (1.0f + swell * 0.1f) / f1;
        poseStack.scale(f2, f3, f2);
        if (entity.isBaby()) {
            poseStack.scale(0.7f, 0.7f, 0.7f);
        }
    }

    protected float getWhiteOverlayProgress(CyberCreeperEntity entity, float partialTick) {
        float swell = entity.getSwelling(partialTick);
        return (int)(swell * 10.0f) % 2 == 0 ? 0.0f : Mth.clamp((float)swell, (float)0.5f, (float)1.0f);
    }
}

