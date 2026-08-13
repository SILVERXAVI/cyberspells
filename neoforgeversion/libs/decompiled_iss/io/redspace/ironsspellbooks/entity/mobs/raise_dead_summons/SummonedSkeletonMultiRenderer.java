/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.SkeletonRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.AbstractSkeleton
 */
package io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.entity.mobs.HumanoidRenderer;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons.SummonedSkeletonModel;
import io.redspace.ironsspellbooks.render.SpellTargetingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class SummonedSkeletonMultiRenderer
extends HumanoidRenderer<SummonedSkeleton> {
    SkeletonRenderer vanillaRenderer;
    public static final ResourceLocation TEXTURE_ALT = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/summoned_skeleton_alt.png");

    public SummonedSkeletonMultiRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SummonedSkeletonModel());
        this.vanillaRenderer = new SkeletonRenderer(this, pContext){

            public ResourceLocation getTextureLocation(AbstractSkeleton pEntity) {
                return TEXTURE_ALT;
            }
        };
        this.vanillaRenderer.addLayer(new SpellTargetingLayer.Vanilla(this.vanillaRenderer));
    }

    @Override
    public void render(SummonedSkeleton entity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (entity.isAnimatingRise()) {
            super.render(entity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        } else {
            this.vanillaRenderer.render((LivingEntity)entity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        }
    }
}

