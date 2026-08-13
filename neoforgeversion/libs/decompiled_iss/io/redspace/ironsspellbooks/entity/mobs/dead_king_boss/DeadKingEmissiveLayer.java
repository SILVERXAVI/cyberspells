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
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.GeoRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingCorpseEntity;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class DeadKingEmissiveLayer
extends GeoRenderLayer<AbstractSpellCastingMob> {
    public static final ResourceLocation TEXTURE_NORMAL = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/dead_king/dead_king_glowing.png");
    public static final ResourceLocation TEXTURE_ENRAGED = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/dead_king/dead_king_enraged_glowing.png");

    public DeadKingEmissiveLayer(GeoEntityRenderer renderer) {
        super((GeoRenderer)renderer);
    }

    public static ResourceLocation currentTexture(AbstractSpellCastingMob entity) {
        DeadKingBoss boss;
        return entity instanceof DeadKingBoss && (boss = (DeadKingBoss)entity).isPhase(DeadKingBoss.Phases.FinalPhase) ? TEXTURE_ENRAGED : TEXTURE_NORMAL;
    }

    public static ResourceLocation currentModel(AbstractSpellCastingMob deadKingBoss) {
        return DeadKingModel.MODEL;
    }

    public static RenderType renderType(ResourceLocation resourceLocation) {
        return RenderType.energySwirl((ResourceLocation)resourceLocation, (float)0.0f, (float)0.0f);
    }

    public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable instanceof DeadKingCorpseEntity || animatable.isInvisible()) {
            return;
        }
        BakedGeoModel model = this.getGeoModel().getBakedModel(DeadKingEmissiveLayer.currentModel(animatable));
        poseStack.pushPose();
        renderType = DeadKingEmissiveLayer.renderType(DeadKingEmissiveLayer.currentTexture(animatable));
        VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
        this.getRenderer().actuallyRender(poseStack, (GeoAnimatable)animatable, model, renderType, bufferSource, vertexconsumer, true, partialTick, 0xF000F0, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }
}

