/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.util.Color
 */
package io.redspace.ironsspellbooks.entity.mobs.keeper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.entity.mobs.keeper.GeoKeeperGhostLayer;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperModel;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.util.Color;

public class KeeperRenderer
extends AbstractSpellCastingMobRenderer {
    public KeeperRenderer(EntityRendererProvider.Context context) {
        super(context, new KeeperModel());
        this.addRenderLayer(new GeoKeeperGhostLayer(this));
        this.shadowRadius = 0.65f;
    }

    @Override
    public void render(AbstractSpellCastingMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        KeeperEntity keeper;
        int light = entity instanceof KeeperEntity && (keeper = (KeeperEntity)entity).isSummoned() ? Math.clamp((long)(packedLight + 100), (int)0, (int)240) : packedLight;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, light);
    }

    public void preRender(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.scale(1.3f, 1.3f, 1.3f);
        super.preRender(poseStack, (Entity)animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(AbstractSpellCastingMob animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent((ResourceLocation)texture);
    }

    public int getPackedOverlay(AbstractSpellCastingMob animatable, float u, float partialTick) {
        return OverlayTexture.pack((int)OverlayTexture.u((float)u), (int)OverlayTexture.v((animatable.deathTime > 0 ? 1 : 0) != 0));
    }

    public Color getRenderColor(AbstractSpellCastingMob animatable, float partialTick, int packedLight) {
        KeeperEntity keeper;
        Color color = super.getRenderColor((Entity)animatable, partialTick, packedLight);
        if (animatable instanceof KeeperEntity && (keeper = (KeeperEntity)animatable).isRising()) {
            color = new Color(RenderHelper.colorf(1.0f, 1.0f, 1.0f, Mth.clamp((float)((float)(25 - keeper.riseAnimTick + 1) / 25.0f), (float)0.0f, (float)1.0f)));
        }
        return color;
    }
}

