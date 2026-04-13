/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.CameraType
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.HumanoidModel$ArmPose
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.player.PlayerRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.WalkAnimationState
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.perigrine3.createcybernetics.client.skin.SkinHighlight;
import com.perigrine3.createcybernetics.client.skin.SkinModifier;
import com.perigrine3.createcybernetics.client.skin.SkinModifierManager;
import com.perigrine3.createcybernetics.client.skin.SkinModifierState;
import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.network.payload.SandevistanSnapshotC2SPayload;
import com.perigrine3.createcybernetics.network.payload.SandevistanSnapshotPayload;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT})
public final class SandevistanMirageTrail {
    private static final int RENDER_SNAPSHOTS = 241;
    private static final int TRAIL_LIFETIME_TICKS = 80;
    private static final int MAX_BODY_ALPHA = 130;
    private static final float MIRAGE_MODEL_SCALE = 0.9375f;
    private static final double RENDER_DELAY_TICKS = 0.75;
    private static final double SAMPLE_PERIOD_TICKS = 1.0;
    private static final float MIN_CAMERA_DIST = 0.55f;
    private static final float MIN_CAMERA_DIST_SQR = 0.3025f;
    private static final int MAX_SNAPSHOTS = Math.max((int)Math.ceil(80.0) + 8, 241 + (int)Math.ceil(0.75) + 8);
    private static final Map<UUID, Deque<Snapshot>> TRAILS = new HashMap<UUID, Deque<Snapshot>>();
    private static final int FULL_BRIGHT = 0xF000F0;

    private SandevistanMirageTrail() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        boolean active;
        Object e;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Iterator<Map.Entry<UUID, Deque<Snapshot>>> it = TRAILS.entrySet().iterator();
        while (it.hasNext()) {
            e = it.next();
            Deque q = (Deque)e.getValue();
            if (q == null || q.isEmpty()) {
                it.remove();
                continue;
            }
            for (Snapshot s : q) {
                ++s.ageTicks;
            }
            while (!q.isEmpty() && ((Snapshot)q.peekFirst()).ageTicks > 80) {
                q.pollFirst();
            }
            if (!q.isEmpty()) continue;
            it.remove();
        }
        e = mc.player;
        if (!(e instanceof AbstractClientPlayer)) {
            return;
        }
        Object self = e;
        if (!self.isAlive()) {
            return;
        }
        boolean bl = active = self.hasEffect(ModEffects.SANDEVISTAN_EFFECT) && self.isSprinting();
        if (!active) {
            return;
        }
        SandevistanMirageTrail.sendSnapshotToServer((AbstractClientPlayer)self);
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        PoseStack poseStack = event.getPoseStack();
        if (poseStack == null) {
            return;
        }
        Vec3 camPos = event.getCamera().getPosition();
        float partial = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        for (AbstractClientPlayer p : mc.level.players()) {
            EntityRenderer er;
            AbstractClientPlayer player;
            Deque<Snapshot> q;
            if (!(p instanceof AbstractClientPlayer) || (q = TRAILS.get((player = p).getUUID())) == null || q.isEmpty() || !((er = mc.getEntityRenderDispatcher().getRenderer((Entity)player)) instanceof PlayerRenderer)) continue;
            PlayerRenderer playerRenderer = (PlayerRenderer)er;
            SandevistanMirageTrail.renderTrailForPlayer(poseStack, (MultiBufferSource)buffer, playerRenderer, player, q, camPos, partial);
        }
        buffer.endBatch();
    }

    public static void acceptNetworkSnapshot(SandevistanSnapshotPayload p) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Player ent = mc.level.getPlayerByUUID(p.playerId());
        if (!(ent instanceof AbstractClientPlayer)) {
            return;
        }
        AbstractClientPlayer player = (AbstractClientPlayer)ent;
        SkinSnapshot skin = SandevistanMirageTrail.captureSkinSnapshot(player);
        Snapshot snap = new Snapshot(new Vec3(p.x(), p.y(), p.z()), p.bodyYaw(), p.crouching(), p.headX(), p.headY(), p.headZ(), p.bodyX(), p.bodyY(), p.bodyZ(), p.rArmX(), p.rArmY(), p.rArmZ(), p.lArmX(), p.lArmY(), p.lArmZ(), p.rLegX(), p.rLegY(), p.rLegZ(), p.lLegX(), p.lLegY(), p.lLegZ(), p.ageInTicks(), skin);
        Deque q = TRAILS.computeIfAbsent(p.playerId(), k -> new ArrayDeque());
        q.addLast(snap);
        while (q.size() > MAX_SNAPSHOTS) {
            q.pollFirst();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void sendSnapshotToServer(AbstractClientPlayer player) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        EntityRenderer er = mc.getEntityRenderDispatcher().getRenderer((Entity)player);
        if (!(er instanceof PlayerRenderer)) {
            return;
        }
        PlayerRenderer pr = (PlayerRenderer)er;
        PlayerModel model = (PlayerModel)pr.getModel();
        HumanoidModel.ArmPose prevRightPose = model.rightArmPose;
        HumanoidModel.ArmPose prevLeftPose = model.leftArmPose;
        boolean prevCrouching = model.crouching;
        float prevSwimAmount = model.swimAmount;
        float prevAttackTime = model.attackTime;
        float shX = model.head.xRot;
        float shY = model.head.yRot;
        float shZ = model.head.zRot;
        float sbX = model.body.xRot;
        float sbY = model.body.yRot;
        float sbZ = model.body.zRot;
        float sraX = model.rightArm.xRot;
        float sraY = model.rightArm.yRot;
        float sraZ = model.rightArm.zRot;
        float slaX = model.leftArm.xRot;
        float slaY = model.leftArm.yRot;
        float slaZ = model.leftArm.zRot;
        float srlX = model.rightLeg.xRot;
        float srlY = model.rightLeg.yRot;
        float srlZ = model.rightLeg.zRot;
        float sllX = model.leftLeg.xRot;
        float sllY = model.leftLeg.yRot;
        float sllZ = model.leftLeg.zRot;
        try {
            model.attackTime = 0.0f;
            model.crouching = player.isCrouching();
            model.swimAmount = 0.0f;
            model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
            model.leftArmPose = HumanoidModel.ArmPose.EMPTY;
            float limbSwing = SandevistanMirageTrail.safeWalkPosition(player, 1.0f);
            float limbSwingAmount = SandevistanMirageTrail.safeWalkSpeed(player, 1.0f);
            float bodyYaw = player.getYRot();
            float headYaw = player.getYHeadRot();
            float pitch = player.getXRot();
            float netHeadYaw = SandevistanMirageTrail.wrapDegrees(headYaw - bodyYaw);
            float ageInTicks = player.tickCount;
            model.setupAnim((LivingEntity)player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, pitch);
            SandevistanSnapshotC2SPayload payload = new SandevistanSnapshotC2SPayload(player.getX(), player.getY(), player.getZ(), bodyYaw, player.isCrouching(), model.head.xRot, model.head.yRot, model.head.zRot, model.body.xRot, model.body.yRot, model.body.zRot, model.rightArm.xRot, model.rightArm.yRot, model.rightArm.zRot, model.leftArm.xRot, model.leftArm.yRot, model.leftArm.zRot, model.rightLeg.xRot, model.rightLeg.yRot, model.rightLeg.zRot, model.leftLeg.xRot, model.leftLeg.yRot, model.leftLeg.zRot, ageInTicks);
            PacketDistributor.sendToServer((CustomPacketPayload)payload, (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
        finally {
            model.rightArmPose = prevRightPose;
            model.leftArmPose = prevLeftPose;
            model.crouching = prevCrouching;
            model.swimAmount = prevSwimAmount;
            model.attackTime = prevAttackTime;
            model.head.xRot = shX;
            model.head.yRot = shY;
            model.head.zRot = shZ;
            model.body.xRot = sbX;
            model.body.yRot = sbY;
            model.body.zRot = sbZ;
            model.rightArm.xRot = sraX;
            model.rightArm.yRot = sraY;
            model.rightArm.zRot = sraZ;
            model.leftArm.xRot = slaX;
            model.leftArm.yRot = slaY;
            model.leftArm.zRot = slaZ;
            model.rightLeg.xRot = srlX;
            model.rightLeg.yRot = srlY;
            model.rightLeg.zRot = srlZ;
            model.leftLeg.xRot = sllX;
            model.leftLeg.yRot = sllY;
            model.leftLeg.zRot = sllZ;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void renderTrailForPlayer(PoseStack poseStack, MultiBufferSource buffer, PlayerRenderer playerRenderer, AbstractClientPlayer player, Deque<Snapshot> q, Vec3 camPos, float partial) {
        PlayerModel model = (PlayerModel)playerRenderer.getModel();
        Minecraft mc = Minecraft.getInstance();
        boolean isLocalFirstPerson = mc.player != null && mc.player.getUUID().equals(player.getUUID()) && mc.options.getCameraType() == CameraType.FIRST_PERSON;
        HumanoidModel.ArmPose prevRightPose = model.rightArmPose;
        HumanoidModel.ArmPose prevLeftPose = model.leftArmPose;
        boolean prevCrouching = model.crouching;
        float prevSwimAmount = model.swimAmount;
        float prevAttackTime = model.attackTime;
        boolean prevHat = model.hat.visible;
        boolean prevJacket = model.jacket.visible;
        boolean prevLeftSleeve = model.leftSleeve.visible;
        boolean prevRightSleeve = model.rightSleeve.visible;
        boolean prevLeftPants = model.leftPants.visible;
        boolean prevRightPants = model.rightPants.visible;
        boolean prevYoung = model.young;
        float shX = model.head.xRot;
        float shY = model.head.yRot;
        float shZ = model.head.zRot;
        float sbX = model.body.xRot;
        float sbY = model.body.yRot;
        float sbZ = model.body.zRot;
        float sraX = model.rightArm.xRot;
        float sraY = model.rightArm.yRot;
        float sraZ = model.rightArm.zRot;
        float slaX = model.leftArm.xRot;
        float slaY = model.leftArm.yRot;
        float slaZ = model.leftArm.zRot;
        float srlX = model.rightLeg.xRot;
        float srlY = model.rightLeg.yRot;
        float srlZ = model.rightLeg.zRot;
        float sllX = model.leftLeg.xRot;
        float sllY = model.leftLeg.yRot;
        float sllZ = model.leftLeg.zRot;
        try {
            model.young = false;
            model.hat.visible = true;
            model.jacket.visible = true;
            model.leftSleeve.visible = true;
            model.rightSleeve.visible = true;
            model.leftPants.visible = true;
            model.rightPants.visible = true;
            Snapshot[] arr = q.toArray(new Snapshot[0]);
            int delaySamples = (int)Math.ceil(0.75);
            int endExclusive = Math.max(0, arr.length - delaySamples);
            if (endExclusive <= 0) {
                return;
            }
            int start = Math.max(0, endExclusive - 241);
            int renderCount = endExclusive - start;
            if (renderCount <= 0) {
                return;
            }
            float scaleAttr = (float)player.getAttributeValue(Attributes.SCALE);
            scaleAttr = Mth.clamp((float)scaleAttr, (float)0.0625f, (float)16.0f);
            float mirageScale = 0.9375f * scaleAttr;
            for (int i = start; i < endExclusive; ++i) {
                Vec3 toCam;
                int aBody;
                Snapshot s = arr[i];
                float t = (float)s.ageTicks / 80.0f;
                if (t < 0.0f || t > 1.0f) continue;
                float alpha = 1.0f - t;
                if ((aBody = (int)((alpha *= alpha) * 130.0f)) <= 0) continue;
                float indexNorm = (float)(i - start) / (float)Math.max(1, renderCount - 1);
                float baseHue = ((float)player.tickCount + partial) * 0.01f % 1.0f;
                float hue = (baseHue + indexNorm) % 1.0f;
                int rgb = Mth.hsvToRgb((float)hue, (float)0.9f, (float)1.0f);
                int hueR = rgb >> 16 & 0xFF;
                int hueG = rgb >> 8 & 0xFF;
                int hueB = rgb & 0xFF;
                Vec3 renderPos = s.pos;
                if (isLocalFirstPerson && (toCam = renderPos.subtract(camPos)).lengthSqr() < (double)0.3025f) continue;
                model.attackTime = 0.0f;
                model.crouching = s.crouching;
                model.swimAmount = 0.0f;
                model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
                model.leftArmPose = HumanoidModel.ArmPose.EMPTY;
                SandevistanMirageTrail.applyPoseToModel(model, s);
                double dx = renderPos.x - camPos.x;
                double dy = renderPos.y - camPos.y;
                double dz = renderPos.z - camPos.z;
                poseStack.pushPose();
                try {
                    poseStack.translate(dx, dy, dz);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - s.bodyYawDeg));
                    poseStack.scale(-1.0f, -1.0f, 1.0f);
                    poseStack.scale(mirageScale, mirageScale, mirageScale);
                    poseStack.translate(0.0f, -1.501f / mirageScale, 0.0f);
                    int packedLight = LevelRenderer.getLightColor((BlockAndTintGetter)player.level(), (BlockPos)BlockPos.containing((double)renderPos.x, (double)renderPos.y, (double)renderPos.z));
                    model.hat.visible = true;
                    model.jacket.visible = true;
                    model.leftSleeve.visible = true;
                    model.rightSleeve.visible = true;
                    model.leftPants.visible = true;
                    model.rightPants.visible = true;
                    SandevistanMirageTrail.applyHideVanilla(model, s.skin);
                    int bodyColor = FastColor.ARGB32.color((int)aBody, (int)hueR, (int)hueG, (int)hueB);
                    ResourceLocation skin = player.getSkin().texture();
                    VertexConsumer vcBody = buffer.getBuffer(RenderType.entityTranslucent((ResourceLocation)skin));
                    model.renderToBuffer(poseStack, vcBody, packedLight, OverlayTexture.NO_OVERLAY, bodyColor);
                    if (s.skin.passes.length <= 0) continue;
                    for (OverlayPass pass : s.skin.passes) {
                        if (pass == null || pass.texture == null) continue;
                        int hueTinted = SandevistanMirageTrail.applyHueTint(pass.argb, hueR, hueG, hueB);
                        int faded = SandevistanMirageTrail.multiplyAlpha(hueTinted, aBody);
                        int light = pass.fullBright ? 0xF000F0 : packedLight;
                        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent((ResourceLocation)pass.texture));
                        model.renderToBuffer(poseStack, vc, light, OverlayTexture.NO_OVERLAY, faded);
                    }
                    continue;
                }
                finally {
                    poseStack.popPose();
                }
            }
        }
        finally {
            model.rightArmPose = prevRightPose;
            model.leftArmPose = prevLeftPose;
            model.crouching = prevCrouching;
            model.swimAmount = prevSwimAmount;
            model.attackTime = prevAttackTime;
            model.hat.visible = prevHat;
            model.jacket.visible = prevJacket;
            model.leftSleeve.visible = prevLeftSleeve;
            model.rightSleeve.visible = prevRightSleeve;
            model.leftPants.visible = prevLeftPants;
            model.rightPants.visible = prevRightPants;
            model.young = prevYoung;
            model.head.xRot = shX;
            model.head.yRot = shY;
            model.head.zRot = shZ;
            model.body.xRot = sbX;
            model.body.yRot = sbY;
            model.body.zRot = sbZ;
            model.rightArm.xRot = sraX;
            model.rightArm.yRot = sraY;
            model.rightArm.zRot = sraZ;
            model.leftArm.xRot = slaX;
            model.leftArm.yRot = slaY;
            model.leftArm.zRot = slaZ;
            model.rightLeg.xRot = srlX;
            model.rightLeg.yRot = srlY;
            model.rightLeg.zRot = srlZ;
            model.leftLeg.xRot = sllX;
            model.leftLeg.yRot = sllY;
            model.leftLeg.zRot = sllZ;
        }
    }

    private static void applyPoseToModel(PlayerModel<?> model, Snapshot s) {
        model.head.xRot = s.headX;
        model.head.yRot = s.headY;
        model.head.zRot = s.headZ;
        model.body.xRot = s.bodyX;
        model.body.yRot = s.bodyY;
        model.body.zRot = s.bodyZ;
        model.rightArm.xRot = s.rArmX;
        model.rightArm.yRot = s.rArmY;
        model.rightArm.zRot = s.rArmZ;
        model.leftArm.xRot = s.lArmX;
        model.leftArm.yRot = s.lArmY;
        model.leftArm.zRot = s.lArmZ;
        model.rightLeg.xRot = s.rLegX;
        model.rightLeg.yRot = s.rLegY;
        model.rightLeg.zRot = s.rLegZ;
        model.leftLeg.xRot = s.lLegX;
        model.leftLeg.yRot = s.lLegY;
        model.leftLeg.zRot = s.lLegZ;
        model.hat.copyFrom(model.head);
        model.jacket.copyFrom(model.body);
        model.leftSleeve.copyFrom(model.leftArm);
        model.rightSleeve.copyFrom(model.rightArm);
        model.leftPants.copyFrom(model.leftLeg);
        model.rightPants.copyFrom(model.rightLeg);
    }

    private static SkinSnapshot captureSkinSnapshot(AbstractClientPlayer player) {
        int tint;
        ResourceLocation tex;
        int hiCount;
        SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
        if (state == null) {
            return SkinSnapshot.empty();
        }
        List<SkinModifier> mods = state.getModifiers();
        List<SkinHighlight> highs = state.getHighlights();
        int modCount = mods == null ? 0 : mods.size();
        int n = hiCount = highs == null ? 0 : highs.size();
        if (modCount == 0 && hiCount == 0) {
            return SkinSnapshot.empty();
        }
        OverlayPass[] passes = new OverlayPass[modCount + hiCount];
        boolean hideHat = false;
        boolean hideJacket = false;
        boolean hideLeftSleeve = false;
        boolean hideRightSleeve = false;
        boolean hideLeftPants = false;
        boolean hideRightPants = false;
        int idx = 0;
        if (mods != null) {
            for (SkinModifier m : mods) {
                if (m == null) continue;
                tex = SandevistanMirageTrail.extractTextureForPlayer(m, player);
                tint = SandevistanMirageTrail.extractTint(m, -1);
                Object hideSet = SandevistanMirageTrail.extractHideVanillaSet(m);
                if (hideSet instanceof Iterable) {
                    Iterable it = (Iterable)hideSet;
                    for (Object o : it) {
                        String n2;
                        if (o == null || (n2 = SandevistanMirageTrail.enumName(o)) == null) continue;
                        if ("HAT".equals(n2)) {
                            hideHat = true;
                        }
                        if ("JACKET".equals(n2)) {
                            hideJacket = true;
                        }
                        if ("LEFT_SLEEVE".equals(n2)) {
                            hideLeftSleeve = true;
                        }
                        if ("RIGHT_SLEEVE".equals(n2)) {
                            hideRightSleeve = true;
                        }
                        if ("LEFT_PANTS".equals(n2)) {
                            hideLeftPants = true;
                        }
                        if (!"RIGHT_PANTS".equals(n2)) continue;
                        hideRightPants = true;
                    }
                }
                if (SandevistanMirageTrail.extractBoolean(m, false, "hideAllVanillaLayers", "isHideAllVanillaLayers", "getHideAllVanillaLayers")) {
                    hideHat = true;
                    hideJacket = true;
                    hideLeftSleeve = true;
                    hideRightSleeve = true;
                    hideLeftPants = true;
                    hideRightPants = true;
                }
                passes[idx++] = new OverlayPass(tex, tint, false);
            }
        }
        if (highs != null) {
            for (SkinHighlight h : highs) {
                if (h == null) continue;
                tex = SandevistanMirageTrail.extractTextureForPlayer(h, player);
                tint = SandevistanMirageTrail.extractTint(h, -1);
                passes[idx++] = new OverlayPass(tex, tint, true);
            }
        }
        if (idx != passes.length) {
            OverlayPass[] compact = new OverlayPass[idx];
            System.arraycopy(passes, 0, compact, 0, idx);
            passes = compact;
        }
        return new SkinSnapshot(passes, hideHat, hideJacket, hideLeftSleeve, hideRightSleeve, hideLeftPants, hideRightPants);
    }

    private static void applyHideVanilla(PlayerModel<?> model, SkinSnapshot skin) {
        if (skin == null) {
            return;
        }
        if (skin.hideHat) {
            model.hat.visible = false;
        }
        if (skin.hideJacket) {
            model.jacket.visible = false;
        }
        if (skin.hideLeftSleeve) {
            model.leftSleeve.visible = false;
        }
        if (skin.hideRightSleeve) {
            model.rightSleeve.visible = false;
        }
        if (skin.hideLeftPants) {
            model.leftPants.visible = false;
        }
        if (skin.hideRightPants) {
            model.rightPants.visible = false;
        }
    }

    private static int multiplyAlpha(int argb, int alphaMul0to255) {
        alphaMul0to255 = Mth.clamp((int)alphaMul0to255, (int)0, (int)255);
        int a = FastColor.ARGB32.alpha((int)argb);
        int newA = a * alphaMul0to255 / 255;
        return argb & 0xFFFFFF | newA << 24;
    }

    private static int applyHueTint(int argb, int hueR, int hueG, int hueB) {
        int a = FastColor.ARGB32.alpha((int)argb);
        int r = FastColor.ARGB32.red((int)argb);
        int g = FastColor.ARGB32.green((int)argb);
        int b = FastColor.ARGB32.blue((int)argb);
        int outR = r * hueR / 255;
        int outG = g * hueG / 255;
        int outB = b * hueB / 255;
        return FastColor.ARGB32.color((int)a, (int)outR, (int)outG, (int)outB);
    }

    private static String enumName(Object enumValue) {
        if (enumValue instanceof Enum) {
            Enum e = (Enum)enumValue;
            return e.name();
        }
        try {
            String s;
            Method m = enumValue.getClass().getMethod("name", new Class[0]);
            Object r = m.invoke(enumValue, new Object[0]);
            return r instanceof String ? (s = (String)r) : null;
        }
        catch (Throwable ignored) {
            return null;
        }
    }

    private static Object extractHideVanillaSet(Object obj) {
        Object r = SandevistanMirageTrail.extractByMethod(obj, "getHideVanilla", "hideVanilla", "getHideVanillaLayers", "hideVanillaLayers");
        if (r != null) {
            return r;
        }
        return SandevistanMirageTrail.extractByField(obj, "hideVanilla", "hideVanillaLayers", "hiddenVanilla", "hideLayers");
    }

    private static ResourceLocation extractTextureForPlayer(Object obj, AbstractClientPlayer player) {
        ResourceLocation r;
        boolean slim;
        boolean bl = slim = player.getSkin().model() == PlayerSkin.Model.SLIM;
        if (slim ? (r = SandevistanMirageTrail.extractResourceLocation(obj, "getSlimTexture", "slimTexture", "slim", "getTextureSlim", "textureSlim")) != null : (r = SandevistanMirageTrail.extractResourceLocation(obj, "getWideTexture", "wideTexture", "wide", "getTextureWide", "textureWide")) != null) {
            return r;
        }
        ResourceLocation generic = SandevistanMirageTrail.extractResourceLocation(obj, "getTexture", "texture");
        if (generic != null) {
            return generic;
        }
        if (slim) {
            ResourceLocation r2 = SandevistanMirageTrail.extractResourceLocationField(obj, "slimTexture", "slim", "textureSlim");
            if (r2 != null) {
                return r2;
            }
            return SandevistanMirageTrail.extractResourceLocationField(obj, "wideTexture", "wide", "textureWide");
        }
        ResourceLocation r3 = SandevistanMirageTrail.extractResourceLocationField(obj, "wideTexture", "wide", "textureWide");
        if (r3 != null) {
            return r3;
        }
        return SandevistanMirageTrail.extractResourceLocationField(obj, "slimTexture", "slim", "textureSlim");
    }

    private static int extractTint(Object obj, int def) {
        Object r = SandevistanMirageTrail.extractByMethod(obj, "getTint", "tint", "getColor", "color", "getArgb", "argb");
        if (r instanceof Integer) {
            Integer i = (Integer)r;
            return i;
        }
        r = SandevistanMirageTrail.extractByField(obj, "tint", "color", "argb", "tintColor");
        if (r instanceof Integer) {
            Integer i = (Integer)r;
            return i;
        }
        return def;
    }

    private static boolean extractBoolean(Object obj, boolean def, String ... names) {
        Object r = SandevistanMirageTrail.extractByMethod(obj, names);
        if (r instanceof Boolean) {
            Boolean b = (Boolean)r;
            return b;
        }
        r = SandevistanMirageTrail.extractByField(obj, names);
        if (r instanceof Boolean) {
            Boolean b = (Boolean)r;
            return b;
        }
        return def;
    }

    private static Object extractByMethod(Object obj, String ... names) {
        for (String n : names) {
            try {
                Method m = obj.getClass().getMethod(n, new Class[0]);
                m.setAccessible(true);
                return m.invoke(obj, new Object[0]);
            }
            catch (Throwable throwable) {
            }
        }
        return null;
    }

    private static Object extractByField(Object obj, String ... names) {
        for (String n : names) {
            try {
                Field f = obj.getClass().getDeclaredField(n);
                f.setAccessible(true);
                return f.get(obj);
            }
            catch (Throwable throwable) {
            }
        }
        return null;
    }

    private static ResourceLocation extractResourceLocation(Object obj, String ... methodNames) {
        for (String n : methodNames) {
            try {
                Method m = obj.getClass().getMethod(n, new Class[0]);
                m.setAccessible(true);
                Object r = m.invoke(obj, new Object[0]);
                if (!(r instanceof ResourceLocation)) continue;
                ResourceLocation rl = (ResourceLocation)r;
                return rl;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    private static ResourceLocation extractResourceLocationField(Object obj, String ... fieldNames) {
        for (String n : fieldNames) {
            try {
                Field f = obj.getClass().getDeclaredField(n);
                f.setAccessible(true);
                Object r = f.get(obj);
                if (!(r instanceof ResourceLocation)) continue;
                ResourceLocation rl = (ResourceLocation)r;
                return rl;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    private static float safeWalkPosition(AbstractClientPlayer player, float partial) {
        try {
            float f;
            WalkAnimationState wa = player.walkAnimation;
            Method m = wa.getClass().getMethod("position", Float.TYPE);
            Object r = m.invoke((Object)wa, Float.valueOf(partial));
            if (r instanceof Float) {
                Float f2 = (Float)r;
                f = f2.floatValue();
            } else {
                f = 0.0f;
            }
            return f;
        }
        catch (Throwable ignored1) {
            try {
                float f;
                WalkAnimationState wa = player.walkAnimation;
                Method m = wa.getClass().getMethod("position", new Class[0]);
                Object r = m.invoke((Object)wa, new Object[0]);
                if (r instanceof Float) {
                    Float f3 = (Float)r;
                    f = f3.floatValue();
                } else {
                    f = 0.0f;
                }
                return f;
            }
            catch (Throwable ignored2) {
                return 0.0f;
            }
        }
    }

    private static float safeWalkSpeed(AbstractClientPlayer player, float partial) {
        try {
            float f;
            WalkAnimationState wa = player.walkAnimation;
            Method m = wa.getClass().getMethod("speed", Float.TYPE);
            Object r = m.invoke((Object)wa, Float.valueOf(partial));
            if (r instanceof Float) {
                Float f2 = (Float)r;
                f = f2.floatValue();
            } else {
                f = 0.0f;
            }
            return f;
        }
        catch (Throwable ignored1) {
            try {
                float f;
                WalkAnimationState wa = player.walkAnimation;
                Method m = wa.getClass().getMethod("speed", new Class[0]);
                Object r = m.invoke((Object)wa, new Object[0]);
                if (r instanceof Float) {
                    Float f3 = (Float)r;
                    f = f3.floatValue();
                } else {
                    f = 0.0f;
                }
                return f;
            }
            catch (Throwable ignored2) {
                return 0.0f;
            }
        }
    }

    private static float wrapDegrees(float deg) {
        if ((deg %= 360.0f) >= 180.0f) {
            deg -= 360.0f;
        }
        if (deg < -180.0f) {
            deg += 360.0f;
        }
        return deg;
    }

    private static final class Snapshot {
        final Vec3 pos;
        final float bodyYawDeg;
        final boolean crouching;
        final float headX;
        final float headY;
        final float headZ;
        final float bodyX;
        final float bodyY;
        final float bodyZ;
        final float rArmX;
        final float rArmY;
        final float rArmZ;
        final float lArmX;
        final float lArmY;
        final float lArmZ;
        final float rLegX;
        final float rLegY;
        final float rLegZ;
        final float lLegX;
        final float lLegY;
        final float lLegZ;
        final float ageInTicks;
        final SkinSnapshot skin;
        int ageTicks;

        Snapshot(Vec3 pos, float bodyYawDeg, boolean crouching, float headX, float headY, float headZ, float bodyX, float bodyY, float bodyZ, float rArmX, float rArmY, float rArmZ, float lArmX, float lArmY, float lArmZ, float rLegX, float rLegY, float rLegZ, float lLegX, float lLegY, float lLegZ, float ageInTicks, SkinSnapshot skin) {
            this.pos = pos;
            this.bodyYawDeg = bodyYawDeg;
            this.crouching = crouching;
            this.headX = headX;
            this.headY = headY;
            this.headZ = headZ;
            this.bodyX = bodyX;
            this.bodyY = bodyY;
            this.bodyZ = bodyZ;
            this.rArmX = rArmX;
            this.rArmY = rArmY;
            this.rArmZ = rArmZ;
            this.lArmX = lArmX;
            this.lArmY = lArmY;
            this.lArmZ = lArmZ;
            this.rLegX = rLegX;
            this.rLegY = rLegY;
            this.rLegZ = rLegZ;
            this.lLegX = lLegX;
            this.lLegY = lLegY;
            this.lLegZ = lLegZ;
            this.ageInTicks = ageInTicks;
            this.skin = skin == null ? SkinSnapshot.empty() : skin;
            this.ageTicks = 0;
        }
    }

    private static final class SkinSnapshot {
        final OverlayPass[] passes;
        final boolean hideHat;
        final boolean hideJacket;
        final boolean hideLeftSleeve;
        final boolean hideRightSleeve;
        final boolean hideLeftPants;
        final boolean hideRightPants;

        SkinSnapshot(OverlayPass[] passes, boolean hideHat, boolean hideJacket, boolean hideLeftSleeve, boolean hideRightSleeve, boolean hideLeftPants, boolean hideRightPants) {
            this.passes = passes == null ? new OverlayPass[]{} : passes;
            this.hideHat = hideHat;
            this.hideJacket = hideJacket;
            this.hideLeftSleeve = hideLeftSleeve;
            this.hideRightSleeve = hideRightSleeve;
            this.hideLeftPants = hideLeftPants;
            this.hideRightPants = hideRightPants;
        }

        static SkinSnapshot empty() {
            return new SkinSnapshot(new OverlayPass[0], false, false, false, false, false, false);
        }
    }

    private static final class OverlayPass {
        final ResourceLocation texture;
        final int argb;
        final boolean fullBright;

        OverlayPass(ResourceLocation texture, int argb, boolean fullBright) {
            this.texture = texture;
            this.argb = argb;
            this.fullBright = fullBright;
        }
    }
}

