package cyberspells.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.item.ModItems;
import cyberspells.CyberSpellsMod;
import cyberspells.items.RuneHolder;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.armortrim.ArmorTrim;
import com.perigrine3.createcybernetics.client.skin.SkinRenderTypes;
import javax.annotation.Nonnull;
import java.util.List;

public class RuneSkinLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public RuneSkinLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int packedLight,
            @Nonnull AbstractClientPlayer player,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw,
            float headPitch) {
        PlayerCyberwareData data = player.getData(ModAttachments.CYBERWARE);
        if (data == null)
            return;

        // Hide runes if Synthskin is active
        if (data.isInstalled(ModItems.SKINUPGRADES_SYNTHSKIN.get(), CyberwareSlot.SKIN) ||
                data.isInstalled(ModItems.SCAVENGED_SYNTHSKIN.get(), CyberwareSlot.SKIN))
            return;

        renderLimb(poseStack, buffer, packedLight, player, data, CyberwareSlot.LARM, "left");
        renderLimb(poseStack, buffer, packedLight, player, data, CyberwareSlot.RARM, "right");
        renderLimb(poseStack, buffer, packedLight, player, data, CyberwareSlot.LLEG, "left");
        renderLimb(poseStack, buffer, packedLight, player, data, CyberwareSlot.RLEG, "right");
    }

    private void renderLimb(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
            PlayerCyberwareData data, CyberwareSlot slot, String side) {
        com.perigrine3.createcybernetics.api.InstalledCyberware installed = data.get(slot, 0);
        if (installed == null)
            return;
        ItemStack stack = installed.getItem();
        if (stack == null || stack.isEmpty())
            return;

        if (stack.getItem() instanceof RuneHolder runeHolder) {
            boolean slim = player.getSkin().model() == net.minecraft.client.resources.PlayerSkin.Model.SLIM;
            String part = slot.name().contains("LEG") ? "leg" : "arm";
            String modelSuffix = slot.name().contains("LEG") ? "" : (slim ? "_slim" : "_wide");

            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(CyberSpellsMod.MODID,
                    "textures/entity/skin/rune_" + side + part + modelSuffix + ".png");

            poseStack.pushPose();
            // Removed scale offsets as they caused texture separation/detachment issues
            // Rendering at 1.0F scale to ensure perfect alignment with base model
            float baseScale = 1.0F;

            poseStack.scale(baseScale, baseScale, baseScale);

            PlayerModel<AbstractClientPlayer> model = this.getParentModel();

            // Save current visibility to restore it later
            boolean oldHead = model.head.visible;
            boolean oldHat = model.hat.visible;
            boolean oldBody = model.body.visible;
            boolean oldJacket = model.jacket.visible;
            boolean oldLArm = model.leftArm.visible;
            boolean oldLSleeve = model.leftSleeve.visible;
            boolean oldRArm = model.rightArm.visible;
            boolean oldRSleeve = model.rightSleeve.visible;
            boolean oldLLeg = model.leftLeg.visible;
            boolean oldLPants = model.leftPants.visible;
            boolean oldRLeg = model.rightLeg.visible;
            boolean oldRPants = model.rightPants.visible;

            // Hide everything else so we don't render ghostly boxes on parts that don't
            // have rune patterns
            model.setAllVisible(false);
            // Always show body/jacket for arms/legs as requested for chest textures
            model.body.visible = true;
            model.jacket.visible = true;

            if (slot == CyberwareSlot.LARM) {
                model.leftArm.visible = true;
                model.leftSleeve.visible = true;
            } else if (slot == CyberwareSlot.RARM) {
                model.rightArm.visible = true;
                model.rightSleeve.visible = true;
            } else if (slot == CyberwareSlot.LLEG) {
                model.leftLeg.visible = true;
                model.leftPants.visible = true;
            } else if (slot == CyberwareSlot.RLEG) {
                model.rightLeg.visible = true;
                model.rightPants.visible = true;
            }

            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(texture));

            // Use renderToBuffer to handle the complex layout and chest patterns
            // efficiently
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);

            // DYE TINT PASS
            if (stack.has(DataComponents.DYED_COLOR)) {
                net.minecraft.world.item.component.DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);
                if (dyedColor != null) {
                    int color = dyedColor.rgb() | 0xFF000000;
                    ResourceLocation dyeTexture = ResourceLocation.fromNamespaceAndPath(CyberSpellsMod.MODID,
                            "textures/entity/skin/rune_" + side + part + modelSuffix + "_dye.png");
                    VertexConsumer dyeConsumer = buffer.getBuffer(RenderType.entityTranslucent(dyeTexture));
                    model.renderToBuffer(poseStack, dyeConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
                }
            }

            // ARMOR TRIM PASS
            ArmorTrim trim = stack.get(DataComponents.TRIM);
            if (trim != null) {
                String patternName = trim.pattern().value().assetId().getPath();
                int color = com.perigrine3.createcybernetics.client.TrimColorPresets.colorFor(trim.material());
                if (color != -1) {
                    color = color | 0xFF000000; // Ensure full alpha
                    String trimPart = slot.name().contains("LEG") ? (side + "_leg")
                            : (side + (slim ? "_slim" : "_wide"));
                    ResourceLocation trimLoc = ResourceLocation.fromNamespaceAndPath("createcybernetics",
                            "textures/entity/trims/" + patternName + "_" + trimPart + ".png");
                    VertexConsumer trimConsumer = buffer.getBuffer(RenderType.entityTranslucent(trimLoc));
                    model.renderToBuffer(poseStack, trimConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
                }
            }

            // ENCHANTMENT GLINT PASS
            List<String> runes = runeHolder.getRunes(stack);
            if (runes != null && !runes.isEmpty()) {
                VertexConsumer glintConsumer = buffer.getBuffer(RenderType.entityGlint());
                model.renderToBuffer(poseStack, glintConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
            }

            // Restore original visibility
            model.head.visible = oldHead;
            model.hat.visible = oldHat;
            model.body.visible = oldBody;
            model.jacket.visible = oldJacket;
            model.leftArm.visible = oldLArm;
            model.leftSleeve.visible = oldLSleeve;
            model.rightArm.visible = oldRArm;
            model.rightSleeve.visible = oldRSleeve;
            model.leftLeg.visible = oldLLeg;
            model.leftPants.visible = oldLPants;
            model.rightLeg.visible = oldRLeg;
            model.rightPants.visible = oldRPants;

            poseStack.popPose();
        }
    }
}
