/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.world.entity.EquipmentSlot
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.util.RenderUtil
 */
package io.redspace.ironsspellbooks.entity.armor.pumpkin;

import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.PumpkinArmorItem;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.util.RenderUtil;

public class PumpkinArmorRenderer
extends GenericCustomArmorRenderer<PumpkinArmorItem> {
    public GeoBone bodyHeadLayerBone = null;

    public PumpkinArmorRenderer(GeoModel<PumpkinArmorItem> model) {
        super(model);
    }

    @Nullable
    public GeoBone getBodyHeadLayerBone() {
        return this.model.getBone("armorBodyHeadLayer").orElse(null);
    }

    @Override
    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel == bakedModel) {
            this.bodyHeadLayerBone = this.getBodyHeadLayerBone();
        }
        super.grabRelevantBones(bakedModel);
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        if (currentSlot == EquipmentSlot.CHEST) {
            this.setBoneVisible(this.bodyHeadLayerBone, true);
        }
    }

    @Override
    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
        super.applyBoneVisibilityByPart(currentSlot, currentPart, model);
        if (currentPart == model.body && currentSlot == EquipmentSlot.CHEST) {
            this.setBoneVisible(this.bodyHeadLayerBone, true);
        }
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        super.applyBaseTransformations(baseModel);
        if (this.bodyHeadLayerBone != null) {
            ModelPart bodyPart = baseModel.head;
            RenderUtil.matchModelPartRot((ModelPart)bodyPart, (GeoBone)this.bodyHeadLayerBone);
            this.bodyHeadLayerBone.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
        }
    }

    @Override
    public void setAllVisible(boolean pVisible) {
        super.setAllVisible(pVisible);
        this.setBoneVisible(this.bodyHeadLayerBone, pVisible);
    }
}

