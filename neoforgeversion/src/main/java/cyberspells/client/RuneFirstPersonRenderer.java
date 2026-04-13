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

@EventBusSubscriber(modid = CyberSpellsMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
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

                poseStack.pushPose();
                // Reduced scale to 1.002F to prevent the "separation" gap caused by 1.1F
                poseStack.scale(1.002F, 1.002F, 1.002F);

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

                poseStack.popPose();
            }
        }
    }
}
