/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.entity.player.PlayerRenderer
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Post
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Pre
 */
package com.perigrine3.createcybernetics.client.render;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
public final class CyberwareLimbHider {
    private static final Map<Integer, VisibilitySnapshot> SNAPSHOTS = new HashMap<Integer, VisibilitySnapshot>();
    private static final String ENTITY_HOLO_SNAPSHOT_KEY = "cc_holo_snapshot";

    private CyberwareLimbHider() {
    }

    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof AbstractClientPlayer)) {
            return;
        }
        AbstractClientPlayer player = (AbstractClientPlayer)livingEntity;
        LivingEntityRenderer livingEntityRenderer = event.getRenderer();
        if (!(livingEntityRenderer instanceof PlayerRenderer)) {
            return;
        }
        PlayerRenderer renderer = (PlayerRenderer)livingEntityRenderer;
        EntityModel entityModel = renderer.getModel();
        if (!(entityModel instanceof PlayerModel)) {
            return;
        }
        PlayerModel model = (PlayerModel)entityModel;
        PlayerCyberwareData data = player.hasData(ModAttachments.CYBERWARE) ? (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE) : null;
        SNAPSHOTS.put(player.getId(), VisibilitySnapshot.capture(model));
        if (data != null) {
            boolean hasLeftArm = data.hasAnyTagged(ModTags.Items.LEFTARM_ITEMS, CyberwareSlot.LARM);
            boolean hasRightArm = data.hasAnyTagged(ModTags.Items.RIGHTARM_ITEMS, CyberwareSlot.RARM);
            boolean hasLeftLeg = data.hasAnyTagged(ModTags.Items.LEFTLEG_ITEMS, CyberwareSlot.LLEG);
            boolean hasRightLeg = data.hasAnyTagged(ModTags.Items.RIGHTLEG_ITEMS, CyberwareSlot.RLEG);
            CyberwareLimbHider.apply(model, event, hasLeftArm, hasRightArm, hasLeftLeg, hasRightLeg);
            return;
        }
        CompoundTag snap = player.getPersistentData().getCompound(ENTITY_HOLO_SNAPSHOT_KEY);
        if (snap.isEmpty()) {
            return;
        }
    }

    private static void apply(PlayerModel<?> model, RenderLivingEvent.Pre<?, ?> event, boolean hasLeftArm, boolean hasRightArm, boolean hasLeftLeg, boolean hasRightLeg) {
        CyberwareLimbHider.setLeftArmVisible(model, hasLeftArm);
        CyberwareLimbHider.setRightArmVisible(model, hasRightArm);
        CyberwareLimbHider.setLeftLegVisible(model, hasLeftLeg);
        CyberwareLimbHider.setRightLegVisible(model, hasRightLeg);
        if (!hasLeftLeg && !hasRightLeg) {
            event.getPoseStack().translate(0.0, -0.75, 0.0);
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        VisibilitySnapshot snap = SNAPSHOTS.remove(player.getId());
        if (snap == null) {
            return;
        }
        LivingEntityRenderer livingEntityRenderer = event.getRenderer();
        if (!(livingEntityRenderer instanceof PlayerRenderer)) {
            return;
        }
        PlayerRenderer renderer = (PlayerRenderer)livingEntityRenderer;
        EntityModel entityModel = renderer.getModel();
        if (!(entityModel instanceof PlayerModel)) {
            return;
        }
        PlayerModel model = (PlayerModel)entityModel;
        snap.restore(model);
    }

    public static void setLeftArmVisible(PlayerModel<?> model, boolean visible) {
        model.leftArm.visible = visible;
        model.leftSleeve.visible = visible;
    }

    public static void setRightArmVisible(PlayerModel<?> model, boolean visible) {
        model.rightArm.visible = visible;
        model.rightSleeve.visible = visible;
    }

    public static void setLeftLegVisible(PlayerModel<?> model, boolean visible) {
        model.leftLeg.visible = visible;
        model.leftPants.visible = visible;
    }

    public static void setRightLegVisible(PlayerModel<?> model, boolean visible) {
        model.rightLeg.visible = visible;
        model.rightPants.visible = visible;
    }

    private static final class VisibilitySnapshot {
        private final boolean leftArm;
        private final boolean rightArm;
        private final boolean leftLeg;
        private final boolean rightLeg;
        private final boolean leftSleeve;
        private final boolean rightSleeve;
        private final boolean leftPants;
        private final boolean rightPants;

        private VisibilitySnapshot(boolean leftArm, boolean rightArm, boolean leftLeg, boolean rightLeg, boolean leftSleeve, boolean rightSleeve, boolean leftPants, boolean rightPants) {
            this.leftArm = leftArm;
            this.rightArm = rightArm;
            this.leftLeg = leftLeg;
            this.rightLeg = rightLeg;
            this.leftSleeve = leftSleeve;
            this.rightSleeve = rightSleeve;
            this.leftPants = leftPants;
            this.rightPants = rightPants;
        }

        static VisibilitySnapshot capture(PlayerModel<?> model) {
            return new VisibilitySnapshot(model.leftArm.visible, model.rightArm.visible, model.leftLeg.visible, model.rightLeg.visible, model.leftSleeve.visible, model.rightSleeve.visible, model.leftPants.visible, model.rightPants.visible);
        }

        void restore(PlayerModel<?> model) {
            model.leftArm.visible = this.leftArm;
            model.rightArm.visible = this.rightArm;
            model.leftLeg.visible = this.leftLeg;
            model.rightLeg.visible = this.rightLeg;
            model.leftSleeve.visible = this.leftSleeve;
            model.rightSleeve.visible = this.rightSleeve;
            model.leftPants.visible = this.leftPants;
            model.rightPants.visible = this.rightPants;
        }
    }
}

