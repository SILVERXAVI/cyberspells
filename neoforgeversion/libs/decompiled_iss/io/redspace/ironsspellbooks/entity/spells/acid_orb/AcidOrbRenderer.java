/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
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
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.entity.spells.acid_orb;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.acid_orb.AcidOrb;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class AcidOrbRenderer
extends EntityRenderer<AcidOrb> {
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"acid_orb_model"), "main");
    private static ResourceLocation ORB_TEXTURE = IronsSpellbooks.id("textures/entity/acid_orb/acid_orb.png");
    private static ResourceLocation[] SWIRL_TEXTURES = new ResourceLocation[]{IronsSpellbooks.id("textures/entity/acid_orb/swirl_0.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_1.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_2.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_3.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_4.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_5.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_6.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_7.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_8.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_9.png"), IronsSpellbooks.id("textures/entity/acid_orb/swirl_10.png")};
    private final ModelPart orb;
    private final ModelPart swirl;

    public AcidOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
        this.orb = modelpart.getChild("orb");
        this.swirl = modelpart.getChild("swirl");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("orb", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f), PartPose.ZERO);
        partdefinition.addOrReplaceChild("swirl", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f), PartPose.ZERO);
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)8, (int)8);
    }

    public void render(AcidOrb entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0, entity.getBoundingBox().getYsize() * 0.5, 0.0);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        Vec3 motion = entity.getDeltaMovement();
        float xRot = -((float)(Mth.atan2((double)motion.horizontalDistance(), (double)motion.y) * 57.2957763671875) - 90.0f);
        float yRot = -((float)(Mth.atan2((double)motion.z, (double)motion.x) * 57.2957763671875) + 90.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getTextureLocation(entity)));
        this.orb.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
        float f = (float)entity.tickCount + partialTicks;
        float swirlX = Mth.cos((float)(0.08f * f)) * 180.0f;
        float swirlY = Mth.sin((float)(0.08f * f)) * 180.0f;
        float swirlZ = Mth.cos((float)(0.08f * f + 5464.0f)) * 180.0f;
        poseStack.mulPose(Axis.XP.rotationDegrees(swirlX));
        poseStack.mulPose(Axis.YP.rotationDegrees(swirlY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(swirlZ));
        consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)this.getSwirlTextureLocation(entity)));
        poseStack.scale(1.15f, 1.15f, 1.15f);
        this.swirl.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render((Entity)entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(AcidOrb entity) {
        return ORB_TEXTURE;
    }

    private ResourceLocation getSwirlTextureLocation(AcidOrb entity) {
        int frame = entity.tickCount / 2 % SWIRL_TEXTURES.length;
        return SWIRL_TEXTURES[frame];
    }
}

