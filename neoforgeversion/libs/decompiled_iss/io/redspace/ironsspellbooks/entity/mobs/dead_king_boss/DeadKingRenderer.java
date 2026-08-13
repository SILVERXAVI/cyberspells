/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingEmissiveLayer;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingModel;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class DeadKingRenderer
extends AbstractSpellCastingMobRenderer {
    public DeadKingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DeadKingModel());
        this.addRenderLayer(new DeadKingEmissiveLayer(this));
    }

    @Override
    public void render(AbstractSpellCastingMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof DeadKingBoss) {
            DeadKingBoss king = (DeadKingBoss)entity;
            this.getGeoModel().getBone("left_leg").ifPresent(bone -> bone.setHidden(king.isPhase(DeadKingBoss.Phases.FinalPhase)));
            this.getGeoModel().getBone("right_leg").ifPresent(bone -> bone.setHidden(king.isPhase(DeadKingBoss.Phases.FinalPhase)));
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public void preRender(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (animatable instanceof DeadKingBoss) {
            poseStack.scale(1.3f, 1.3f, 1.3f);
        }
        super.preRender(poseStack, (Entity)animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(AbstractSpellCastingMob animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return animatable.isInvisible() ? RenderType.entityTranslucent((ResourceLocation)texture) : RenderType.entityCutoutNoCull((ResourceLocation)texture);
    }

    @Override
    protected void adjustHandItemRendering(PoseStack poseStack, ItemStack itemStack, AbstractSpellCastingMob animatable, float partialTick, boolean offhand) {
        if (itemStack.is((Item)ItemRegistry.BLOOD_STAFF.get())) {
            poseStack.translate(0.0, 0.25, 0.0);
        }
    }
}

