/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Items
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.render.AngelWingsModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class AngelWingsLayer<T extends LivingEntity, M extends EntityModel<T>>
extends RenderLayer<T, M> {
    private static final ResourceLocation WINGS_LOCATION = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/angel_wings.png");
    private final AngelWingsModel<T> angelWingsModel = new AngelWingsModel(Minecraft.getInstance().getEntityModels().bakeLayer(AngelWingsModel.ANGEL_WINGS_LAYER));

    public AngelWingsLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (this.shouldRender(pLivingEntity)) {
            ResourceLocation resourcelocation = this.getAngelWingsTexture(pLivingEntity);
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.0, 0.0, 0.125);
            this.getParentModel().copyPropertiesTo(this.angelWingsModel);
            this.angelWingsModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer((MultiBufferSource)pBuffer, (RenderType)RenderType.energySwirl((ResourceLocation)resourcelocation, (float)0.0f, (float)0.0f), (boolean)false);
            this.angelWingsModel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY);
            pMatrixStack.popPose();
        }
    }

    public boolean shouldRender(T entity) {
        return !entity.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) && entity.hasEffect(MobEffectRegistry.ANGEL_WINGS);
    }

    public ResourceLocation getAngelWingsTexture(T entity) {
        return WINGS_LOCATION;
    }
}

