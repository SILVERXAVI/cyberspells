/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.GeoRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.render.RenderHelper;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(value=Dist.CLIENT)
public class FireBossSoulLayer
extends GeoRenderLayer<AbstractSpellCastingMob> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/keeper/keeper_ghost.png");

    public FireBossSoulLayer(GeoEntityRenderer entityRendererIn) {
        super((GeoRenderer)entityRendererIn);
    }

    public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType2, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        int i = animatable.hurtTime + animatable.deathTime;
        if (i <= 0) {
            return;
        }
        float alpha = animatable.deathTime > 0 ? (float)i / 120.0f : (float)i / (float)animatable.hurtDuration * 2.0f;
        float f = (float)animatable.tickCount + partialTick;
        RenderType renderType = RenderType.energySwirl((ResourceLocation)TEXTURE, (float)(f * 0.02f % 1.0f), (float)(f * 0.01f % 1.0f));
        VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
        poseStack.pushPose();
        List bones = bakedModel.topLevelBones();
        this.setArmorVisible(bones, false);
        this.getRenderer().actuallyRender(poseStack, (GeoAnimatable)animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, 0xF000F0, OverlayTexture.NO_OVERLAY, RenderHelper.colorf(0.15f * alpha, 0.02f * alpha, 0.0f * alpha));
        this.setArmorVisible(bones, true);
        poseStack.popPose();
    }

    private void setArmorVisible(List<GeoBone> bones, boolean visible) {
        for (GeoBone bone : bones) {
            if (bone == null) continue;
            if (bone.getName().startsWith("armor")) {
                bone.setHidden(!visible);
            }
            this.setArmorVisible(bone.getChildBones(), visible);
        }
    }
}

