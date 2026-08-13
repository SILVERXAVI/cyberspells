/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ShieldItem
 *  net.minecraft.world.item.SwordItem
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 *  software.bernie.geckolib.renderer.GeoRenderer
 *  software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 *  software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer
 *  software.bernie.geckolib.util.ClientUtil
 *  software.bernie.geckolib.util.RenderUtil
 */
package io.redspace.ironsspellbooks.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.render.ArmorCapeLayer;
import javax.annotation.Nonnull;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;
import software.bernie.geckolib.util.ClientUtil;
import software.bernie.geckolib.util.RenderUtil;

public class HumanoidRenderer<T extends Mob>
extends GeoEntityRenderer<T> {
    private static final String LEFT_HAND = "bipedHandLeft";
    private static final String RIGHT_HAND = "bipedHandRight";
    private static final String LEFT_BOOT = "armorBipedLeftFoot";
    private static final String RIGHT_BOOT = "armorBipedRightFoot";
    private static final String LEFT_BOOT_2 = "armorBipedLeftFoot2";
    private static final String RIGHT_BOOT_2 = "armorBipedRightFoot2";
    private static final String LEFT_ARMOR_LEG = "armorBipedLeftLeg";
    private static final String RIGHT_ARMOR_LEG = "armorBipedRightLeg";
    private static final String LEFT_ARMOR_LEG_2 = "armorBipedLeftLeg2";
    private static final String RIGHT_ARMOR_LEG_2 = "armorBipedRightLeg2";
    private static final String CHESTPLATE = "armorBipedBody";
    private static final String RIGHT_SLEEVE = "armorBipedRightArm";
    private static final String LEFT_SLEEVE = "armorBipedLeftArm";
    private static final String HELMET = "armorBipedHead";
    private final RenderLayer hardCodedCapeLayer = new ArmorCapeLayer(null, poseStack -> {});

    public HumanoidRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        this.addRenderLayer((GeoRenderLayer)new ItemArmorGeoLayer<T>(this, (GeoRenderer)this){

            @javax.annotation.Nullable
            protected ItemStack getArmorItemForBone(GeoBone bone, T animatable) {
                return switch (bone.getName()) {
                    case HumanoidRenderer.LEFT_BOOT, HumanoidRenderer.RIGHT_BOOT, HumanoidRenderer.LEFT_BOOT_2, HumanoidRenderer.RIGHT_BOOT_2 -> this.bootsStack;
                    case HumanoidRenderer.LEFT_ARMOR_LEG, HumanoidRenderer.RIGHT_ARMOR_LEG, HumanoidRenderer.LEFT_ARMOR_LEG_2, HumanoidRenderer.RIGHT_ARMOR_LEG_2 -> this.leggingsStack;
                    case HumanoidRenderer.CHESTPLATE, HumanoidRenderer.RIGHT_SLEEVE, HumanoidRenderer.LEFT_SLEEVE -> this.chestplateStack;
                    case HumanoidRenderer.HELMET -> this.helmetStack;
                    default -> null;
                };
            }

            @Nonnull
            protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, T animatable) {
                return switch (bone.getName()) {
                    case HumanoidRenderer.LEFT_BOOT, HumanoidRenderer.RIGHT_BOOT, HumanoidRenderer.LEFT_BOOT_2, HumanoidRenderer.RIGHT_BOOT_2 -> EquipmentSlot.FEET;
                    case HumanoidRenderer.LEFT_ARMOR_LEG, HumanoidRenderer.RIGHT_ARMOR_LEG, HumanoidRenderer.LEFT_ARMOR_LEG_2, HumanoidRenderer.RIGHT_ARMOR_LEG_2 -> EquipmentSlot.LEGS;
                    case HumanoidRenderer.RIGHT_SLEEVE -> {
                        if (!animatable.isLeftHanded()) {
                            yield EquipmentSlot.MAINHAND;
                        }
                        yield EquipmentSlot.OFFHAND;
                    }
                    case HumanoidRenderer.LEFT_SLEEVE -> {
                        if (animatable.isLeftHanded()) {
                            yield EquipmentSlot.OFFHAND;
                        }
                        yield EquipmentSlot.MAINHAND;
                    }
                    case HumanoidRenderer.CHESTPLATE -> EquipmentSlot.CHEST;
                    case HumanoidRenderer.HELMET -> EquipmentSlot.HEAD;
                    default -> super.getEquipmentSlotForBone(bone, stack, animatable);
                };
            }

            @Nonnull
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, T animatable, HumanoidModel<?> baseModel) {
                return switch (bone.getName()) {
                    case HumanoidRenderer.LEFT_BOOT, HumanoidRenderer.LEFT_BOOT_2, HumanoidRenderer.LEFT_ARMOR_LEG, HumanoidRenderer.LEFT_ARMOR_LEG_2 -> baseModel.leftLeg;
                    case HumanoidRenderer.RIGHT_BOOT, HumanoidRenderer.RIGHT_BOOT_2, HumanoidRenderer.RIGHT_ARMOR_LEG, HumanoidRenderer.RIGHT_ARMOR_LEG_2 -> baseModel.rightLeg;
                    case HumanoidRenderer.RIGHT_SLEEVE -> baseModel.rightArm;
                    case HumanoidRenderer.LEFT_SLEEVE -> baseModel.leftArm;
                    case HumanoidRenderer.CHESTPLATE -> baseModel.body;
                    case HumanoidRenderer.HELMET -> baseModel.head;
                    default -> super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
                };
            }
        });
        this.addRenderLayer((GeoRenderLayer)new BlockAndItemGeoLayer<T>((GeoRenderer)this){

            @javax.annotation.Nullable
            protected ItemStack getStackForBone(GeoBone bone, T animatable) {
                if (animatable instanceof AbstractSpellCastingMob) {
                    AbstractSpellCastingMob castingMob = (AbstractSpellCastingMob)animatable;
                    String boneName = bone.getName();
                    if (HumanoidRenderer.this.isBoneMainHand(castingMob, boneName)) {
                        if (castingMob.isDrinkingPotion()) {
                            return AbstractSpellCastingMobRenderer.makePotion(castingMob);
                        }
                        if (HumanoidRenderer.this.shouldWeaponBeSheathed(castingMob) && castingMob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem) {
                            return ItemStack.EMPTY;
                        }
                    }
                    if (boneName.equals("torso") && HumanoidRenderer.this.shouldWeaponBeSheathed(castingMob) && castingMob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem) {
                        return castingMob.getItemBySlot(EquipmentSlot.MAINHAND);
                    }
                }
                return switch (bone.getName()) {
                    case HumanoidRenderer.LEFT_HAND -> {
                        if (animatable.isLeftHanded()) {
                            yield animatable.getMainHandItem();
                        }
                        yield animatable.getOffhandItem();
                    }
                    case HumanoidRenderer.RIGHT_HAND -> {
                        if (animatable.isLeftHanded()) {
                            yield animatable.getOffhandItem();
                        }
                        yield animatable.getMainHandItem();
                    }
                    default -> null;
                };
            }

            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
                return switch (bone.getName()) {
                    case HumanoidRenderer.RIGHT_HAND, "torso" -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    case HumanoidRenderer.LEFT_HAND -> ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
                    default -> ItemDisplayContext.NONE;
                };
            }

            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                boolean offhand;
                poseStack.translate(0.0, 0.0, -0.0625);
                poseStack.translate(0.0, -0.0625, 0.0);
                boolean bl = offhand = stack == animatable.getOffhandItem();
                if (!offhand) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, -0.25);
                    }
                } else {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, 0.25);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
                    }
                }
                if (animatable instanceof AbstractSpellCastingMob) {
                    AbstractSpellCastingMob mob = (AbstractSpellCastingMob)animatable;
                    if (bone.getName().equals("torso") && HumanoidRenderer.this.shouldWeaponBeSheathed(mob)) {
                        float hipOffset = animatable.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? 0.25f : 0.325f;
                        poseStack.translate(animatable.isLeftHanded() ? (double)hipOffset : (double)(-hipOffset), 0.0, -0.4);
                        poseStack.mulPose(Axis.XP.rotationDegrees(215.0f));
                        poseStack.scale(0.85f, 0.85f, 0.85f);
                    }
                }
                HumanoidRenderer.this.adjustHandItemRendering(poseStack, stack, animatable, partialTick, offhand);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    protected boolean isBoneMainHand(AbstractSpellCastingMob entity, String boneName) {
        return entity.isLeftHanded() && boneName.equals(LEFT_HAND) || !entity.isLeftHanded() && boneName.equals(RIGHT_HAND);
    }

    protected boolean shouldWeaponBeSheathed(AbstractSpellCastingMob entity) {
        return entity.shouldSheathSword() && !entity.isAggressive();
    }

    protected void adjustHandItemRendering(PoseStack poseStack, ItemStack stack, T animatable, float partialTick, boolean offhand) {
    }

    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.isInvisible() && entity.isInvisibleTo(ClientUtil.getClientPlayer())) {
            return;
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        float f = Mth.rotLerp((float)partialTick, (float)((Mob)entity).yBodyRotO, (float)((Mob)entity).yBodyRot);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - f));
        if (((Mob)entity).deathTime > 0) {
            float deathRotation = ((float)((Mob)entity).deathTime + partialTick - 1.0f) / 20.0f * 1.6f;
            poseStack.mulPose(Axis.ZP.rotationDegrees(Math.min(Mth.sqrt((float)deathRotation), 1.0f) * this.getDeathMaxRotation((Entity)entity)));
        }
        this.model.getBone("torso").ifPresent(bone -> RenderUtil.prepMatrixForBone((PoseStack)poseStack, (GeoBone)bone));
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(0.0f, -1.501f, 0.0f);
        this.hardCodedCapeLayer.render(poseStack, bufferSource, packedLight, entity, 0.0f, 0.0f, partialTick, 0.0f, 0.0f, 0.0f);
        poseStack.popPose();
    }

    @Nullable
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return animatable.isInvisible() ? RenderType.itemEntityTranslucentCull((ResourceLocation)texture) : RenderType.entityCutoutNoCull((ResourceLocation)texture, (boolean)false);
    }
}

