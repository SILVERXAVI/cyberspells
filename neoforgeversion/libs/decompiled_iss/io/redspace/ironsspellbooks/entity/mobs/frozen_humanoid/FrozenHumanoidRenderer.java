/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.SimpleTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.render.IExtendedSimpleTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FrozenHumanoidRenderer
extends LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/frozen_humanoid.png");
    private static final ResourceLocation TEXTURE_ALT = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"textures/entity/frozen_humanoid_alt.png");
    final EntityModel<LivingEntity> originalModel;
    boolean rectangular = false;

    public FrozenHumanoidRenderer(EntityRendererProvider.Context context) {
        super(context, (EntityModel)new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER)), 0.36f);
        this.originalModel = new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER));
    }

    public void render(LivingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        LivingEntity entityToRender = entity;
        this.rectangular = false;
        if (entity instanceof FrozenHumanoid) {
            FrozenHumanoid frozenHumanoid = (FrozenHumanoid)entity;
            if (frozenHumanoid.entityToCopy != null) {
                EntityRenderer entityRenderer = (EntityRenderer)Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(frozenHumanoid.entityToCopy);
                Entity fakeEntity = frozenHumanoid.entityToCopy.create((Level)Minecraft.getInstance().level);
                if (fakeEntity instanceof LivingEntity) {
                    LivingEntity livingFakeEntity = (LivingEntity)fakeEntity;
                    FrozenHumanoid.copyEntityVisualProperties(livingFakeEntity, entity);
                    entityToRender = livingFakeEntity;
                }
                if (entityRenderer instanceof LivingEntityRenderer) {
                    LivingEntityRenderer renderer = (LivingEntityRenderer)entityRenderer;
                    this.model = renderer.getModel();
                    ResourceLocation texturelocation = renderer.getTextureLocation(fakeEntity);
                    AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(texturelocation);
                    if (texture instanceof SimpleTexture) {
                        this.rectangular = ((IExtendedSimpleTexture)texture).irons_spellbooks$isRectangular();
                    }
                }
            }
        }
        try {
            super.render(entityToRender, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
        catch (Exception e) {
            IronsSpellbooks.LOGGER.error("Failed to render Ice Shadow of {}: {}", ((FrozenHumanoid)entity).entityToCopy, (Object)e.getMessage());
            ((FrozenHumanoid)entity).entityToCopy = null;
        }
        this.rectangular = false;
        this.model = this.originalModel;
    }

    protected boolean shouldShowName(LivingEntity entity) {
        double d0 = this.entityRenderDispatcher.distanceToSqr((Entity)entity);
        float f = entity.isCrouching() ? 32.0f : 64.0f;
        return d0 >= (double)(f * f) ? false : entity.isCustomNameVisible();
    }

    public ResourceLocation getTextureLocation(LivingEntity pEntity) {
        return this.rectangular ? TEXTURE_ALT : TEXTURE;
    }

    protected float getBob(LivingEntity pLivingBase, float pPartialTick) {
        return 0.0f;
    }
}

