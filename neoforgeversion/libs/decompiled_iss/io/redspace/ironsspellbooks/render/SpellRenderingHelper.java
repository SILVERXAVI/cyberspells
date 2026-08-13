/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.spells.CastingMobAimingData;
import io.redspace.ironsspellbooks.spells.blood.RayOfSiphoningSpell;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(value=Dist.CLIENT)
public class SpellRenderingHelper {
    public static final ResourceLocation SOLID = IronsSpellbooks.id("textures/entity/ray/solid.png");
    public static final ResourceLocation BEACON = IronsSpellbooks.id("textures/entity/ray/beacon_beam.png");
    public static final ResourceLocation STRAIGHT_GLOW = IronsSpellbooks.id("textures/entity/ray/ribbon_glow.png");
    public static final ResourceLocation TWISTING_GLOW = IronsSpellbooks.id("textures/entity/ray/twisting_glow.png");

    public static void renderSpellHelper(SyncedSpellData spellData, LivingEntity castingMob, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
        if (SpellRegistry.RAY_OF_SIPHONING_SPELL.get().getSpellId().equals(spellData.getCastingSpellId())) {
            SpellRenderingHelper.renderRayOfSiphoning(castingMob, poseStack, bufferSource, partialTicks);
        }
    }

    public static void renderRayOfSiphoning(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
        Vec3 rayEndPos;
        Mob mob;
        ICastData iCastData;
        poseStack.pushPose();
        poseStack.translate(0.0f, entity.getEyeHeight() * 0.8f, 0.0f);
        PoseStack.Pose pose = poseStack.last();
        Vec3 start = Vec3.ZERO;
        if (entity instanceof Mob && (iCastData = MagicData.getPlayerMagicData((LivingEntity)(mob = (Mob)entity)).getAdditionalCastData()) instanceof CastingMobAimingData) {
            CastingMobAimingData aimingData = (CastingMobAimingData)iCastData;
            rayEndPos = aimingData.getAimPosition(partialTicks);
        } else {
            rayEndPos = Utils.raycastForEntity(entity.level(), (Entity)entity, RayOfSiphoningSpell.getRange(0), true).getLocation();
        }
        float distance = (float)entity.getEyePosition().distanceTo(rayEndPos);
        float radius = 0.12f;
        int r = 178;
        int g = 0;
        int b = 0;
        int a = 255;
        float deltaTicks = (float)entity.tickCount + partialTicks;
        float deltaUV = -deltaTicks % 10.0f;
        float max = Mth.frac((float)(deltaUV * 0.2f - (float)Mth.floor((float)(deltaUV * 0.1f))));
        float min = -1.0f + max;
        Vec3 dir = entity.getLookAngle().normalize();
        float dx = (float)dir.x;
        float dz = (float)dir.z;
        float yRot = (float)Mth.atan2((double)dz, (double)dx) - 1.5707f;
        float dxz = Mth.sqrt((float)(dx * dx + dz * dz));
        float dy = (float)dir.y;
        float xRot = (float)Mth.atan2((double)dy, (double)dxz);
        poseStack.mulPose(Axis.YP.rotation(-yRot));
        poseStack.mulPose(Axis.XP.rotation(-xRot));
        for (float j = 1.0f; j <= distance; j += 0.5f) {
            Vec3 wiggle = new Vec3((double)(Mth.sin((float)(deltaTicks * 0.8f)) * 0.02f), (double)(Mth.sin((float)(deltaTicks * 0.8f + 100.0f)) * 0.02f), (double)(Mth.cos((float)(deltaTicks * 0.8f)) * 0.02f));
            Vec3 end = new Vec3(0.0, 0.0, (double)Math.min(j, distance)).add(wiggle);
            VertexConsumer inner = bufferSource.getBuffer(RenderType.entityTranslucent((ResourceLocation)BEACON, (boolean)true));
            SpellRenderingHelper.drawHull(start, end, radius, radius, pose, inner, r, g, b, a, min, max);
            VertexConsumer outer = bufferSource.getBuffer(RenderType.entityTranslucent((ResourceLocation)TWISTING_GLOW));
            SpellRenderingHelper.drawQuad(start, end, radius * 4.0f, 0.0f, pose, outer, r, g, b, a, min, max);
            SpellRenderingHelper.drawQuad(start, end, 0.0f, radius * 4.0f, pose, outer, r, g, b, a, min, max);
            start = end;
        }
        poseStack.popPose();
    }

    public static void drawHull(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        SpellRenderingHelper.drawQuad(from.subtract(0.0, (double)(height * 0.5f), 0.0), to.subtract(0.0, (double)(height * 0.5f), 0.0), width, 0.0f, pose, consumer, r, g, b, a, uvMin, uvMax);
        SpellRenderingHelper.drawQuad(from.add(0.0, (double)(height * 0.5f), 0.0), to.add(0.0, (double)(height * 0.5f), 0.0), width, 0.0f, pose, consumer, r, g, b, a, uvMin, uvMax);
        SpellRenderingHelper.drawQuad(from.subtract((double)(width * 0.5f), 0.0, 0.0), to.subtract((double)(width * 0.5f), 0.0, 0.0), 0.0f, height, pose, consumer, r, g, b, a, uvMin, uvMax);
        SpellRenderingHelper.drawQuad(from.add((double)(width * 0.5f), 0.0, 0.0), to.add((double)(width * 0.5f), 0.0, 0.0), 0.0f, height, pose, consumer, r, g, b, a, uvMin, uvMax);
    }

    public static void drawQuad(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float halfWidth = width * 0.5f;
        float halfHeight = height * 0.5f;
        consumer.addVertex(poseMatrix, (float)from.x - halfWidth, (float)from.y - halfHeight, (float)from.z).setColor(r, g, b, a).setUv(0.0f, uvMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, (float)from.x + halfWidth, (float)from.y + halfHeight, (float)from.z).setColor(r, g, b, a).setUv(1.0f, uvMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, (float)to.x + halfWidth, (float)to.y + halfHeight, (float)to.z).setColor(r, g, b, a).setUv(1.0f, uvMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(0.0f, 1.0f, 0.0f);
        consumer.addVertex(poseMatrix, (float)to.x - halfWidth, (float)to.y - halfHeight, (float)to.z).setColor(r, g, b, a).setUv(0.0f, uvMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(240).setNormal(0.0f, 1.0f, 0.0f);
    }
}

