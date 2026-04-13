/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.Model
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.resources.PlayerSkin$Model
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.perigrine3.createcybernetics.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.client.model.AttachmentAnchor;
import com.perigrine3.createcybernetics.client.model.CalfPropellerAttachmentModel;
import com.perigrine3.createcybernetics.client.model.ClawAttachmentModel;
import com.perigrine3.createcybernetics.client.model.DrillFistAttachmentModel;
import com.perigrine3.createcybernetics.client.model.GuardianEyeAttachmentModel;
import com.perigrine3.createcybernetics.client.model.OcelotPawsAttachmentModel;
import com.perigrine3.createcybernetics.client.model.PlayerAttachment;
import com.perigrine3.createcybernetics.client.model.PlayerAttachmentState;
import com.perigrine3.createcybernetics.client.model.SpursAttachmentModel;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class PlayerAttachmentManager {
    private static final Map<UUID, PlayerAttachmentState> STATES = new HashMap<UUID, PlayerAttachmentState>();
    private static final ResourceLocation CLAWS_ITEM_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"armupgrades_claws");
    public static final ResourceLocation CLAWS_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/claws.png");
    private static ClawAttachmentModel CLAW_MODEL;
    private static final ResourceLocation DRILL_FIST_ITEM_ID;
    public static final ResourceLocation DRILL_FIST_TEXTURE;
    private static DrillFistAttachmentModel DRILL_MODEL;
    private static final ResourceLocation OCELOT_PAWS_ITEM_ID;
    public static final ResourceLocation OCELOT_PAWS_TEXTURE;
    public static final ResourceLocation OCELOT_PAWS_DYED;
    private static OcelotPawsAttachmentModel PAWS_MODEL;
    private static final ResourceLocation CALF_PROPELLER_ITEM_ID;
    public static final ResourceLocation CALF_PROPELLER_TEXTURE;
    private static CalfPropellerAttachmentModel CALF_PROPELLER_MODEL;
    private static final ResourceLocation SPUR_ITEM_ID;
    public static final ResourceLocation SPUR_TEXTURE;
    private static SpursAttachmentModel SPUR_MODEL;
    private static final ResourceLocation GUARDIAN_EYE_ITEM_ID;
    public static final ResourceLocation GUARDIAN_EYE_TEXTURE;
    private static GuardianEyeAttachmentModel GUARDIAN_EYE_MODEL;

    private PlayerAttachmentManager() {
    }

    public static ClawAttachmentModel clawsModel() {
        if (CLAW_MODEL == null) {
            ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(ClawAttachmentModel.LAYER);
            CLAW_MODEL = new ClawAttachmentModel(baked);
        }
        return CLAW_MODEL;
    }

    private static Item clawsItemOrNull() {
        if (!BuiltInRegistries.ITEM.containsKey(CLAWS_ITEM_ID)) {
            return null;
        }
        Item item = (Item)BuiltInRegistries.ITEM.get(CLAWS_ITEM_ID);
        return item == null ? null : item;
    }

    public static DrillFistAttachmentModel drillModel() {
        if (DRILL_MODEL == null) {
            ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(DrillFistAttachmentModel.LAYER);
            DRILL_MODEL = new DrillFistAttachmentModel(baked);
        }
        return DRILL_MODEL;
    }

    private static Item drillFistItemOrNull() {
        if (!BuiltInRegistries.ITEM.containsKey(DRILL_FIST_ITEM_ID)) {
            return null;
        }
        Item item = (Item)BuiltInRegistries.ITEM.get(DRILL_FIST_ITEM_ID);
        return item == null ? null : item;
    }

    public static OcelotPawsAttachmentModel pawsModel() {
        if (PAWS_MODEL == null) {
            ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(OcelotPawsAttachmentModel.LAYER);
            PAWS_MODEL = new OcelotPawsAttachmentModel(baked);
        }
        return PAWS_MODEL;
    }

    private static Item ocelotPawsItemOrNull() {
        if (!BuiltInRegistries.ITEM.containsKey(OCELOT_PAWS_ITEM_ID)) {
            return null;
        }
        Item item = (Item)BuiltInRegistries.ITEM.get(OCELOT_PAWS_ITEM_ID);
        return item == null ? null : item;
    }

    public static CalfPropellerAttachmentModel calfPropellerModel() {
        if (CALF_PROPELLER_MODEL == null) {
            ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(CalfPropellerAttachmentModel.LAYER);
            CALF_PROPELLER_MODEL = new CalfPropellerAttachmentModel(baked);
        }
        return CALF_PROPELLER_MODEL;
    }

    private static Item calfPropellerItemOrNull() {
        if (!BuiltInRegistries.ITEM.containsKey(CALF_PROPELLER_ITEM_ID)) {
            return null;
        }
        Item item = (Item)BuiltInRegistries.ITEM.get(CALF_PROPELLER_ITEM_ID);
        return item == null ? null : item;
    }

    public static SpursAttachmentModel spurModel() {
        if (SPUR_MODEL == null) {
            ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(SpursAttachmentModel.LAYER);
            SPUR_MODEL = new SpursAttachmentModel(baked);
        }
        return SPUR_MODEL;
    }

    private static Item spurItemOrNull() {
        if (!BuiltInRegistries.ITEM.containsKey(SPUR_ITEM_ID)) {
            return null;
        }
        Item item = (Item)BuiltInRegistries.ITEM.get(SPUR_ITEM_ID);
        return item == null ? null : item;
    }

    public static GuardianEyeAttachmentModel guardianEyeModel() {
        if (GUARDIAN_EYE_MODEL == null) {
            ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(GuardianEyeAttachmentModel.LAYER);
            GUARDIAN_EYE_MODEL = new GuardianEyeAttachmentModel(baked);
        }
        return GUARDIAN_EYE_MODEL;
    }

    private static Item guardianEyeItemOrNull() {
        if (!BuiltInRegistries.ITEM.containsKey(GUARDIAN_EYE_ITEM_ID)) {
            return null;
        }
        Item item = (Item)BuiltInRegistries.ITEM.get(GUARDIAN_EYE_ITEM_ID);
        return item == null ? null : item;
    }

    public static PlayerAttachmentState getState(AbstractClientPlayer player) {
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return null;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return null;
        }
        PlayerAttachmentState state = STATES.computeIfAbsent(player.getUUID(), id -> new PlayerAttachmentState());
        state.clear();
        Item clawsItem = PlayerAttachmentManager.clawsItemOrNull();
        Item drillItem = PlayerAttachmentManager.drillFistItemOrNull();
        Item pawsItem = PlayerAttachmentManager.ocelotPawsItemOrNull();
        Item calfPropellerItem = PlayerAttachmentManager.calfPropellerItemOrNull();
        Item spurItem = PlayerAttachmentManager.spurItemOrNull();
        Item guardianEyeItem = PlayerAttachmentManager.guardianEyeItemOrNull();
        if (clawsItem == null && drillItem == null && pawsItem == null && calfPropellerItem == null && spurItem == null && guardianEyeItem == null) {
            return state;
        }
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            AttachmentAnchor anchor;
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null || (anchor = PlayerAttachmentManager.mapSlotToAnchor(slot)) == null) continue;
            for (int idx = 0; idx < arr.length; ++idx) {
                ItemStack stack;
                InstalledCyberware cw = arr[idx];
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !data.isEnabled(slot, idx)) continue;
                if (clawsItem != null && stack.is(clawsItem)) {
                    state.add(new ClawAttachment(anchor));
                    continue;
                }
                if (drillItem != null && stack.is(drillItem)) {
                    state.add(new DrillFistAttachment(anchor));
                }
                if (pawsItem != null && stack.is(pawsItem)) {
                    state.add(new OcelotPawsAttachment(anchor));
                }
                if (calfPropellerItem != null && stack.is(calfPropellerItem)) {
                    state.add(new CalfPropellerAttachment(anchor));
                }
                if (spurItem != null && stack.is(spurItem)) {
                    state.add(new SpursAttachment(anchor));
                }
                if (guardianEyeItem == null || !stack.is(guardianEyeItem) || !player.isCrouching()) continue;
                state.add(new GuardianEyeAttachment(anchor));
            }
        }
        return state;
    }

    private static AttachmentAnchor mapSlotToAnchor(CyberwareSlot slot) {
        if (slot == CyberwareSlot.LARM) {
            return AttachmentAnchor.LEFT_ARM;
        }
        if (slot == CyberwareSlot.RARM) {
            return AttachmentAnchor.RIGHT_ARM;
        }
        if (slot == CyberwareSlot.LLEG) {
            return AttachmentAnchor.LEFT_LEG;
        }
        if (slot == CyberwareSlot.RLEG) {
            return AttachmentAnchor.RIGHT_LEG;
        }
        if (slot == CyberwareSlot.EYES) {
            return AttachmentAnchor.HEAD;
        }
        return null;
    }

    public static void applyKnuckleClawTransform(PoseStack pose, AttachmentAnchor armAnchor) {
        pose.translate(0.0, 0.6, 0.0);
        pose.translate(0.15f, 0.0f, 0.0f);
        pose.mulPose(Axis.YP.rotationDegrees(90.0f));
        if (armAnchor == AttachmentAnchor.LEFT_ARM) {
            pose.translate(-0.0168f, 0.0f, -0.3f);
            pose.mulPose(Axis.ZP.rotationDegrees(10.0f));
            pose.mulPose(Axis.YP.rotationDegrees(-180.0f));
        } else if (armAnchor == AttachmentAnchor.RIGHT_ARM) {
            pose.translate(0.0172f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(-10.0f));
        }
        pose.scale(1.0f, 1.0f, 1.0f);
    }

    public static void applyDrillFistTransform(PoseStack pose, AttachmentAnchor armAnchor) {
        pose.translate(0.07f, -0.25f, 0.45f);
        pose.mulPose(Axis.YP.rotationDegrees(90.0f));
        pose.scale(1.15f, 1.15f, 1.15f);
        if (armAnchor == AttachmentAnchor.LEFT_ARM) {
            pose.translate(-0.02f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
            pose.mulPose(Axis.YP.rotationDegrees(-180.0f));
        } else if (armAnchor == AttachmentAnchor.RIGHT_ARM) {
            pose.translate(0.8f, 0.0f, -0.1f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
        }
        pose.scale(1.1f, 1.1f, 1.1f);
    }

    public static void applyOcelotPawsTransform(PoseStack pose, AttachmentAnchor legAnchor) {
        pose.translate(0.0f, 0.77f, 0.0f);
        pose.mulPose(Axis.YP.rotationDegrees(0.0f));
        if (legAnchor == AttachmentAnchor.LEFT_LEG) {
            pose.translate(0.0f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
            pose.mulPose(Axis.YP.rotationDegrees(0.0f));
        } else if (legAnchor == AttachmentAnchor.RIGHT_LEG) {
            pose.translate(0.0f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
        }
        pose.scale(1.0f, 1.0f, 1.0f);
    }

    public static void applyCalfPropellerTransform(PoseStack pose, AttachmentAnchor legAnchor) {
        pose.translate(0.0f, -0.7f, -0.45f);
        pose.mulPose(Axis.XN.rotationDegrees(-25.0f));
        pose.mulPose(Axis.YP.rotationDegrees(0.0f));
        if (legAnchor == AttachmentAnchor.LEFT_LEG) {
            pose.translate(0.0f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
            pose.mulPose(Axis.YP.rotationDegrees(0.0f));
        } else if (legAnchor == AttachmentAnchor.RIGHT_LEG) {
            pose.translate(0.0f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
        }
        pose.scale(1.0f, 1.0f, 1.0f);
    }

    public static void applySpursTransform(PoseStack pose, AttachmentAnchor legAnchor) {
        pose.translate(0.0f, 0.0f, 0.0f);
        pose.mulPose(Axis.XN.rotationDegrees(0.0f));
        pose.mulPose(Axis.YP.rotationDegrees(0.0f));
        if (legAnchor == AttachmentAnchor.LEFT_LEG) {
            pose.translate(0.0f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
            pose.mulPose(Axis.YP.rotationDegrees(0.0f));
        } else if (legAnchor == AttachmentAnchor.RIGHT_LEG) {
            pose.translate(0.0f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
        }
        pose.scale(1.0f, 1.0f, 1.0f);
    }

    public static void applyGuardianEyeTransform(PoseStack pose, AttachmentAnchor legAnchor) {
        pose.translate(0.0f, -0.25f, -0.205f);
        pose.mulPose(Axis.XN.rotationDegrees(0.0f));
        pose.mulPose(Axis.YP.rotationDegrees(0.0f));
        if (legAnchor == AttachmentAnchor.LEFT_LEG) {
            pose.translate(0.0f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
            pose.mulPose(Axis.YP.rotationDegrees(0.0f));
        } else if (legAnchor == AttachmentAnchor.RIGHT_LEG) {
            pose.translate(0.0f, 0.0f, 0.0f);
            pose.mulPose(Axis.ZP.rotationDegrees(0.0f));
        }
        pose.scale(1.0f, 1.0f, 1.0f);
    }

    static {
        DRILL_FIST_ITEM_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"armupgrades_drillfist");
        DRILL_FIST_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/drill_fist.png");
        OCELOT_PAWS_ITEM_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"legupgrades_ocelotpaws");
        OCELOT_PAWS_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/ocelot_paws.png");
        OCELOT_PAWS_DYED = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/ocelot_paws_dyed.png");
        CALF_PROPELLER_ITEM_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"legupgrades_propellers");
        CALF_PROPELLER_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/calf_propeller.png");
        SPUR_ITEM_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"legupgrades_spurs");
        SPUR_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/spurs.png");
        GUARDIAN_EYE_ITEM_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"wetware_guardianeye");
        GUARDIAN_EYE_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/entity/guardian_eye.png");
    }

    private static final class ClawAttachment
    implements PlayerAttachment {
        private final AttachmentAnchor anchor;

        private ClawAttachment(AttachmentAnchor anchor) {
            this.anchor = anchor;
        }

        @Override
        public AttachmentAnchor anchor() {
            return this.anchor;
        }

        @Override
        public ResourceLocation texture(PlayerSkin.Model modelType) {
            return CLAWS_TEXTURE;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return PlayerAttachmentManager.clawsModel();
        }

        @Override
        public int color() {
            return -1;
        }

        @Override
        public boolean thirdPersonOnly() {
            return true;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            PlayerAttachmentManager.applyKnuckleClawTransform(poseStack, this.anchor);
        }
    }

    private static final class DrillFistAttachment
    implements PlayerAttachment {
        private final AttachmentAnchor anchor;

        private DrillFistAttachment(AttachmentAnchor anchor) {
            this.anchor = anchor;
        }

        @Override
        public AttachmentAnchor anchor() {
            return this.anchor;
        }

        @Override
        public ResourceLocation texture(PlayerSkin.Model modelType) {
            return DRILL_FIST_TEXTURE;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return PlayerAttachmentManager.drillModel();
        }

        @Override
        public int color() {
            return -1;
        }

        @Override
        public boolean thirdPersonOnly() {
            return true;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            PlayerAttachmentManager.applyDrillFistTransform(poseStack, this.anchor);
        }
    }

    private static final class OcelotPawsAttachment
    implements PlayerAttachment {
        private final AttachmentAnchor anchor;
        @Nullable
        private AbstractClientPlayer renderPlayer;

        private OcelotPawsAttachment(AttachmentAnchor anchor) {
            this.anchor = anchor;
        }

        @Override
        public AttachmentAnchor anchor() {
            return this.anchor;
        }

        private CyberwareSlot slotForThisSide() {
            return this.anchor == AttachmentAnchor.LEFT_LEG ? CyberwareSlot.LLEG : CyberwareSlot.RLEG;
        }

        @Nullable
        private PlayerCyberwareData dataOrNull() {
            AbstractClientPlayer p;
            Object object = p = this.renderPlayer != null ? this.renderPlayer : Minecraft.getInstance().player;
            if (p == null || !p.hasData(ModAttachments.CYBERWARE)) {
                return null;
            }
            return (PlayerCyberwareData)p.getData(ModAttachments.CYBERWARE);
        }

        @Override
        public ResourceLocation texture(PlayerSkin.Model modelType) {
            CyberwareSlot slot;
            PlayerCyberwareData data = this.dataOrNull();
            if (data == null) {
                return OCELOT_PAWS_TEXTURE;
            }
            Item item = (Item)ModItems.LEGUPGRADES_OCELOTPAWS.get();
            return data.isDyed(item, slot = this.slotForThisSide()) ? OCELOT_PAWS_DYED : OCELOT_PAWS_TEXTURE;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return PlayerAttachmentManager.pawsModel();
        }

        @Override
        public int color() {
            CyberwareSlot slot;
            PlayerCyberwareData data = this.dataOrNull();
            if (data == null) {
                return -1;
            }
            Item item = (Item)ModItems.LEGUPGRADES_OCELOTPAWS.get();
            if (!data.isDyed(item, slot = this.slotForThisSide())) {
                return -1;
            }
            return data.dyeColor(item, slot);
        }

        @Override
        public boolean thirdPersonOnly() {
            return true;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            this.renderPlayer = player;
            PlayerAttachmentManager.applyOcelotPawsTransform(poseStack, this.anchor);
        }
    }

    private static final class CalfPropellerAttachment
    implements PlayerAttachment {
        private final AttachmentAnchor anchor;

        private CalfPropellerAttachment(AttachmentAnchor anchor) {
            this.anchor = anchor;
        }

        @Override
        public AttachmentAnchor anchor() {
            return this.anchor;
        }

        @Override
        public ResourceLocation texture(PlayerSkin.Model modelType) {
            return CALF_PROPELLER_TEXTURE;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return PlayerAttachmentManager.calfPropellerModel();
        }

        @Override
        public int color() {
            return -1;
        }

        @Override
        public boolean thirdPersonOnly() {
            return true;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            PlayerAttachmentManager.applyCalfPropellerTransform(poseStack, this.anchor);
        }
    }

    private static final class SpursAttachment
    implements PlayerAttachment {
        private final AttachmentAnchor anchor;

        private SpursAttachment(AttachmentAnchor anchor) {
            this.anchor = anchor;
        }

        @Override
        public AttachmentAnchor anchor() {
            return this.anchor;
        }

        @Override
        public ResourceLocation texture(PlayerSkin.Model modelType) {
            return SPUR_TEXTURE;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return PlayerAttachmentManager.spurModel();
        }

        @Override
        public int color() {
            return -1;
        }

        @Override
        public boolean thirdPersonOnly() {
            return true;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            PlayerAttachmentManager.applySpursTransform(poseStack, this.anchor);
        }
    }

    private static final class GuardianEyeAttachment
    implements PlayerAttachment {
        private final AttachmentAnchor anchor;

        private GuardianEyeAttachment(AttachmentAnchor anchor) {
            this.anchor = anchor;
        }

        @Override
        public AttachmentAnchor anchor() {
            return this.anchor;
        }

        @Override
        public ResourceLocation texture(PlayerSkin.Model modelType) {
            return GUARDIAN_EYE_TEXTURE;
        }

        @Override
        public Model model(PlayerSkin.Model modelType) {
            return PlayerAttachmentManager.guardianEyeModel();
        }

        @Override
        public int color() {
            return -1;
        }

        @Override
        public boolean thirdPersonOnly() {
            return true;
        }

        @Override
        public void setupPose(PoseStack poseStack, AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> parentModel, PlayerSkin.Model modelType, float partialTick) {
            PlayerAttachmentManager.applyGuardianEyeTransform(poseStack, this.anchor);
        }
    }
}

