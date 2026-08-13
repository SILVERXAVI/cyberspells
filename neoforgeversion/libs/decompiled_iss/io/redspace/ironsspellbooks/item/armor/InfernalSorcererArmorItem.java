/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  software.bernie.geckolib.renderer.GeoArmorRenderer
 */
package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.ImmolateEffect;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.entity.armor.InfernalSorcererArmorModel;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class InfernalSorcererArmorItem
extends ImbuableChestplateArmorItem {
    public static final int COOLDOWN_TICKS = 20;

    public InfernalSorcererArmorItem(ArmorItem.Type type, Item.Properties settings) {
        super((Holder<ArmorMaterial>)ArmorMaterialRegistry.INFERNAL_SORCERER, type, settings, new AttributeContainer((Holder<Attribute>)AttributeRegistry.MAX_MANA, 150.0, AttributeModifier.Operation.ADD_VALUE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add((Component)Component.translatable((String)"tooltip.irons_spellbooks.passive_ability_no_cooldown", (Object[])new Object[]{Component.literal((String)Utils.timeFromTicks(Utils.applyCooldownReduction(20, (LivingEntity)MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.AQUA)}).withStyle(ChatFormatting.DARK_PURPLE));
        tooltipComponents.add((Component)Component.literal((String)" ").append((Component)Component.translatable((String)(this.getDescriptionId() + ".desc"))).withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltipComponents.add((Component)Component.literal((String)" ").append((Component)Component.translatable((String)(this.getDescriptionId() + ".immolate.desc"), (Object[])new Object[]{Component.literal((String)Utils.stringTruncation(ImmolateEffect.damageFor((Entity)MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.RED)})).withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer(new InfernalSorcererArmorModel());
    }
}

