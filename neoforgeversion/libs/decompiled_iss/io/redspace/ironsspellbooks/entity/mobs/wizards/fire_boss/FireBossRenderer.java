/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.model.ModelResourceLocation
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemDisplayContext
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.util.Color
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossFireballChargeLayer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossFlameLayer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossModel;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossSoulLayer;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.Color;

public class FireBossRenderer
extends AbstractSpellCastingMobRenderer {
    static final int deathFadeTime = 120;

    public FireBossRenderer(EntityRendererProvider.Context context) {
        super(context, new FireBossModel());
        this.shadowRadius = 0.65f;
        this.addRenderLayer(new FireBossSoulLayer(this));
        this.addRenderLayer(new FireBossFlameLayer(this));
        this.addRenderLayer(new FireBossFireballChargeLayer(this, context));
    }

    @Override
    public void render(AbstractSpellCastingMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        FireBossEntity fireBossEntity;
        if (entity instanceof FireBossEntity && (fireBossEntity = (FireBossEntity)entity).isSpawning()) {
            float f = fireBossEntity.getSpawnWalkPercent(partialTick);
            if (f == 0.0f) {
                return;
            }
            this.shadowRadius = Mth.lerp((float)f, (float)2.0f, (float)0.65f);
            this.shadowStrength = Mth.lerp((float)f, (float)0.0f, (float)1.0f);
        } else {
            this.shadowStrength = 1.0f;
            this.shadowRadius = 0.65f;
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, Math.clamp((long)(packedLight + 100), (int)0, (int)240));
    }

    public void applyRenderLayersForBone(PoseStack poseStack, AbstractSpellCastingMob animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        FireBossEntity fireBoss;
        super.applyRenderLayersForBone(poseStack, (GeoAnimatable)animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        if (bone.getName().equals("bipedHandLeft") && animatable instanceof FireBossEntity && (fireBoss = (FireBossEntity)animatable).spectralDaggerActive()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            poseStack.translate(-0.375, 0.1, -1.0);
            Minecraft.getInstance().getItemRenderer().render(((Item)ItemRegistry.HELLRAZOR.get()).getDefaultInstance(), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, true, poseStack, bufferSource, packedLight, packedOverlay, Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone((ResourceLocation)IronsSpellbooks.id("item/fiery_dagger"))));
            poseStack.popPose();
        }
    }

    public int getPackedOverlay(AbstractSpellCastingMob animatable, float u, float partialTick) {
        return OverlayTexture.NO_OVERLAY;
    }

    public Color getRenderColor(AbstractSpellCastingMob animatable, float partialTick, int packedLight) {
        FireBossEntity fireBoss;
        Color color = super.getRenderColor((Entity)animatable, partialTick, packedLight);
        float f = 1.0f;
        if (animatable.deathTime > 40) {
            f = Mth.clamp((float)((float)(160 - animatable.deathTime) / 120.0f), (float)0.0f, (float)1.0f);
        } else if (animatable instanceof FireBossEntity && (fireBoss = (FireBossEntity)animatable).isSpawning()) {
            f = fireBoss.getSpawnWalkPercent(partialTick);
        }
        if (!animatable.isInvisible() && f != 1.0f) {
            color = new Color(RenderHelper.colorf(1.0f, 1.0f, 1.0f, f));
        }
        return color;
    }

    @Override
    public RenderType getRenderType(AbstractSpellCastingMob animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        FireBossEntity fireBoss;
        if (animatable.isDeadOrDying() || animatable instanceof FireBossEntity && ((fireBoss = (FireBossEntity)animatable).isSpawning() || fireBoss.isDespawning())) {
            return RenderType.entityTranslucent((ResourceLocation)texture);
        }
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }

    protected float getDeathMaxRotation(AbstractSpellCastingMob animatable) {
        return 0.0f;
    }
}

