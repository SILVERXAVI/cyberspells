/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.projectile.Projectile
 */
package io.redspace.ironsspellbooks.entity.spells.fireball;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;

public class FireballRenderer
extends EntityRenderer<Projectile> {
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"fireball_model"), "main");
    public static final ResourceLocation BASE_TEXTURE = IronsSpellbooks.id("textures/entity/fireball/fireball_core.png");
    public static final ResourceLocation[] FIRE_TEXTURES = new ResourceLocation[]{IronsSpellbooks.id("textures/entity/fireball/fire_0.png"), IronsSpellbooks.id("textures/entity/fireball/fire_1.png"), IronsSpellbooks.id("textures/entity/fireball/fire_2.png"), IronsSpellbooks.id("textures/entity/fireball/fire_3.png")};
    protected final ModelPart body;
    protected final ModelPart outline;
    protected final float scale;

    public FireballRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
        this.outline = modelpart.getChild("outline");
        this.scale = scale;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f), PartPose.ZERO);
        partdefinition.addOrReplaceChild("outline", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 16.0f), PartPose.ZERO);
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)48, (int)24);
    }

    public void render(Projectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0, entity.getBoundingBox().getYsize() * 0.5, 0.0);
        poseStack.scale(this.scale, this.scale, this.scale);
        if (entity instanceof AbstractMagicProjectile) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp((float)partialTicks, (float)entity.yRotO, (float)entity.getYRot()) + 180.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp((float)partialTicks, (float)entity.xRotO, (float)entity.getXRot())));
        } else {
            poseStack.mulPose(Axis.YP.rotationDegrees(-Mth.lerp((float)partialTicks, (float)entity.yRotO, (float)entity.getYRot())));
            poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp((float)partialTicks, (float)entity.xRotO, (float)entity.getXRot())));
        }
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getTextureLocation(entity)));
        this.body.render(poseStack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY);
        consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getFireTextureLocation(entity)));
        poseStack.scale(1.15f, 1.15f, 1.15f);
        this.outline.render(poseStack, consumer, 0xF000F0, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(Projectile entity) {
        return BASE_TEXTURE;
    }

    public ResourceLocation getFireTextureLocation(Projectile entity) {
        int frame = entity.tickCount / 2 % FIRE_TEXTURES.length;
        return FIRE_TEXTURES[frame];
    }
}

