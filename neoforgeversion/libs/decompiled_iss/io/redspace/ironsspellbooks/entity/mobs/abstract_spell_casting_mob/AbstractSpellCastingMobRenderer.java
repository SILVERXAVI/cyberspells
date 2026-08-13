/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.Potions
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.renderer.GeoEntityRenderer
 */
package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.HumanoidRenderer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.render.ChargeSpellLayer;
import io.redspace.ironsspellbooks.render.EnergySwirlLayer;
import io.redspace.ironsspellbooks.render.GeoSpinAttackLayer;
import io.redspace.ironsspellbooks.render.GlowingEyesLayer;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import io.redspace.ironsspellbooks.render.SpellTargetingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public abstract class AbstractSpellCastingMobRenderer
extends HumanoidRenderer<AbstractSpellCastingMob> {
    private ResourceLocation textureResource;

    public AbstractSpellCastingMobRenderer(EntityRendererProvider.Context renderManager, AbstractSpellCastingMobModel model) {
        super(renderManager, model);
        this.shadowRadius = 0.5f;
        this.addRenderLayer(new EnergySwirlLayer.Geo((GeoEntityRenderer<AbstractSpellCastingMob>)this, EnergySwirlLayer.EVASION_TEXTURE, (Holder<MobEffect>)MobEffectRegistry.EVASION));
        this.addRenderLayer(new EnergySwirlLayer.Geo((GeoEntityRenderer<AbstractSpellCastingMob>)this, EnergySwirlLayer.CHARGE_TEXTURE, (Holder<MobEffect>)MobEffectRegistry.CHARGED));
        this.addRenderLayer(new ChargeSpellLayer.Geo(this));
        this.addRenderLayer(new GlowingEyesLayer.Geo(this));
        this.addRenderLayer(new SpellTargetingLayer.Geo(this));
        this.addRenderLayer(new GeoSpinAttackLayer(this));
    }

    public static ItemStack makePotion(AbstractSpellCastingMob entity) {
        ItemStack healthPotion = new ItemStack((ItemLike)Items.POTION);
        return Utils.setPotion(healthPotion, (Holder<Potion>)(entity.isInvertedHealAndHarm() ? Potions.HARMING : Potions.HEALING));
    }

    @Override
    public void render(AbstractSpellCastingMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        SpellRenderingHelper.renderSpellHelper(ClientMagicData.getSyncedSpellData((LivingEntity)entity), (LivingEntity)entity, poseStack, bufferSource, partialTick);
    }

    @Override
    public RenderType getRenderType(AbstractSpellCastingMob animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return animatable.isInvisible() ? RenderType.entityTranslucent((ResourceLocation)texture) : super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}

