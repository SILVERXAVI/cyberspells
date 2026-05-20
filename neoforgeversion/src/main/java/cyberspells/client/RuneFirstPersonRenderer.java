package cyberspells.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import cyberspells.CyberSpellsMod;
import cyberspells.items.RuneHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.ItemStack;
import com.perigrine3.createcybernetics.client.skin.SkinRenderTypes;
import java.util.List;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderArmEvent;

public class RuneFirstPersonRenderer {

    @SubscribeEvent
    public static void onRenderArm(RenderArmEvent event) {
        AbstractClientPlayer player = event.getPlayer();
        PlayerCyberwareData data = player.getData(ModAttachments.CYBERWARE);
        if (data == null)
            return;

        // Hide if Synthskin is installed
        if (data.isInstalled(ModItems.SKINUPGRADES_SYNTHSKIN.get(), CyberwareSlot.SKIN) ||
                data.isInstalled(ModItems.SCAVENGED_SYNTHSKIN.get(), CyberwareSlot.SKIN))
            return;

        HumanoidArm arm = event.getArm();
        CyberwareSlot slot = (arm == HumanoidArm.LEFT) ? CyberwareSlot.LARM : CyberwareSlot.RARM;

        com.perigrine3.createcybernetics.api.InstalledCyberware installed = data.get(slot, 0);
        if (installed == null)
            return;

        ItemStack stack = installed.getItem();
        if (stack != null && !stack.isEmpty() && stack.getItem() instanceof RuneHolder) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.getEntityRenderDispatcher().getRenderer(player) instanceof PlayerRenderer renderer) {
                PlayerModel<AbstractClientPlayer> model = renderer.getModel();
                boolean slim = player.getSkin().model() == net.minecraft.client.resources.PlayerSkin.Model.SLIM;

                String side = (arm == HumanoidArm.LEFT) ? "left" : "right";
                String modelSuffix = slim ? "arm_slim" : "arm_wide";
                ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(CyberSpellsMod.MODID,
                        "textures/entity/skin/rune_" + side + modelSuffix + ".png");

                PoseStack poseStack = event.getPoseStack();
                MultiBufferSource buffer = event.getMultiBufferSource();
                int packedLight = event.getPackedLight();

                ModelPart armPart = (arm == HumanoidArm.LEFT) ? model.leftArm : model.rightArm;
                ModelPart sleevePart = (arm == HumanoidArm.LEFT) ? model.leftSleeve : model.rightSleeve;

                VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(texture));

                // Preservation and reset of model state to prevent misalignment
                float prevAttackTime = model.attackTime;
                boolean prevCrouching = model.crouching;
                float prevSwimAmount = model.swimAmount;
                net.minecraft.client.model.HumanoidModel.ArmPose prevRightPose = model.rightArmPose;
                net.minecraft.client.model.HumanoidModel.ArmPose prevLeftPose = model.leftArmPose;
                boolean prevRightArmVis = model.rightArm.visible;
                boolean prevRightSleeveVis = model.rightSleeve.visible;
                boolean prevLeftArmVis = model.leftArm.visible;
                boolean prevLeftSleeveVis = model.leftSleeve.visible;

                poseStack.pushPose();
                try {
                    poseStack.scale(1.002F, 1.002F, 1.002F);

                    model.attackTime = 0.0f;
                    model.crouching = false;
                    model.swimAmount = 0.0f;
                    model.rightArmPose = net.minecraft.client.model.HumanoidModel.ArmPose.EMPTY;
                    model.leftArmPose = net.minecraft.client.model.HumanoidModel.ArmPose.EMPTY;
                    
                    model.rightArm.visible = (arm == HumanoidArm.RIGHT);
                    model.rightSleeve.visible = (arm == HumanoidArm.RIGHT);
                    model.leftArm.visible = (arm == HumanoidArm.LEFT);
                    model.leftSleeve.visible = (arm == HumanoidArm.LEFT);

                    model.setupAnim(player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

                    armPart.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, -1);
                    sleevePart.render(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, -1);

                    // DYE TINT PASS
                    if (stack.has(DataComponents.DYED_COLOR)) {
                        net.minecraft.world.item.component.DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);
                        if (dyedColor != null) {
                            int color = dyedColor.rgb() | 0xFF000000;
                            ResourceLocation dyeTexture = ResourceLocation.fromNamespaceAndPath(CyberSpellsMod.MODID,
                                    "textures/entity/skin/rune_" + side + modelSuffix + "_dye.png");
                            VertexConsumer dyeConsumer = buffer.getBuffer(RenderType.entityTranslucent(dyeTexture));
                            armPart.render(poseStack, dyeConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
                            sleevePart.render(poseStack, dyeConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
                        }
                    }

                    // ARMOR TRIM PASS
                    ArmorTrim trim = stack.get(DataComponents.TRIM);
                    if (trim != null) {
                        String patternName = trim.pattern().value().assetId().getPath();
                        int color = com.perigrine3.createcybernetics.client.TrimColorPresets.colorFor(trim.material());
                        if (color != -1) {
                            color = color | 0xFF000000;
                            String trimPart = side + (slim ? "_slim" : "_wide");
                            ResourceLocation trimLoc = ResourceLocation.fromNamespaceAndPath("createcybernetics",
                                    "textures/entity/trims/" + patternName + "_" + trimPart + ".png");
                            VertexConsumer trimConsumer = buffer.getBuffer(RenderType.entityTranslucent(trimLoc));
                            armPart.render(poseStack, trimConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
                            sleevePart.render(poseStack, trimConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
                        }
                    }

                    // ENCHANTMENT GLINT PASS
                    List<String> runes = ((RuneHolder) stack.getItem()).getRunes(stack);
                    if (runes != null && !runes.isEmpty()) {
                        VertexConsumer glintConsumer = buffer.getBuffer(RenderType.entityGlint());
                        armPart.render(poseStack, glintConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
                        sleevePart.render(poseStack, glintConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
                    }
                } finally {
                    model.attackTime = prevAttackTime;
                    model.crouching = prevCrouching;
                    model.swimAmount = prevSwimAmount;
                    model.rightArmPose = prevRightPose;
                    model.leftArmPose = prevLeftPose;
                    model.rightArm.visible = prevRightArmVis;
                    model.rightSleeve.visible = prevRightSleeveVis;
                    model.leftArm.visible = prevLeftArmVis;
                    model.leftSleeve.visible = prevLeftSleeveVis;
                    poseStack.popPose();
                }
            }
        }
    }
}
