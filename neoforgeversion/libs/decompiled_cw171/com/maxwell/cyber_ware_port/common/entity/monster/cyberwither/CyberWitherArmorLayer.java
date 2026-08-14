/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.EnergySwirlLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 */
package com.maxwell.cyber_ware_port.common.entity.monster.cyberwither;

import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherBoss;
import com.maxwell.cyber_ware_port.common.entity.monster.cyberwither.CyberWitherModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CyberWitherArmorLayer
extends EnergySwirlLayer<CyberWitherBoss, CyberWitherModel> {
    private static final ResourceLocation WITHER_ARMOR_LOCATION = ResourceLocation.withDefaultNamespace((String)"textures/entity/wither/wither_armor.png");
    private final CyberWitherModel model;

    public CyberWitherArmorLayer(RenderLayerParent<CyberWitherBoss, CyberWitherModel> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer);
        this.model = new CyberWitherModel(pModelSet.bakeLayer(ModelLayers.WITHER_ARMOR));
    }

    protected float xOffset(float pTickCount) {
        return Mth.cos((float)(pTickCount * 0.02f)) * 3.0f;
    }

    protected ResourceLocation getTextureLocation() {
        return WITHER_ARMOR_LOCATION;
    }

    protected EntityModel<CyberWitherBoss> model() {
        return this.model;
    }
}

