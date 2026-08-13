/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.alchemy.PotionContents
 */
package io.redspace.ironsspellbooks.item.consumables;

import io.redspace.ironsspellbooks.effect.CustomDescriptionMobEffect;
import io.redspace.ironsspellbooks.item.consumables.DrinkableItem;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;

public class SimpleElixir
extends DrinkableItem {
    private final Supplier<MobEffectInstance> potionEffect;
    boolean foilOverride;

    public SimpleElixir(Item.Properties pProperties, Supplier<MobEffectInstance> potionEffect) {
        super(pProperties, SimpleElixir::applyEffect, Items.GLASS_BOTTLE, true);
        this.potionEffect = potionEffect;
    }

    public SimpleElixir(Item.Properties pProperties, Supplier<MobEffectInstance> potionEffect, boolean foil) {
        this(pProperties, potionEffect);
        this.foilOverride = foil;
    }

    public MobEffectInstance getMobEffect() {
        return this.potionEffect.get();
    }

    private static void applyEffect(ItemStack itemStack, LivingEntity livingEntity) {
        Item item = itemStack.getItem();
        if (item instanceof SimpleElixir) {
            SimpleElixir elixir = (SimpleElixir)item;
            if (elixir.potionEffect.get() != null) {
                livingEntity.addEffect(elixir.potionEffect.get());
            }
        }
    }

    public boolean isFoil(ItemStack pStack) {
        return super.isFoil(pStack) || this.foilOverride;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        List<MobEffectInstance> iterable = List.of(this.getMobEffect());
        PotionContents.addPotionTooltip(iterable, pTooltipComponents::add, (float)1.0f, (float)context.tickRate());
        Object object = this.potionEffect.get().getEffect().value();
        if (object instanceof CustomDescriptionMobEffect) {
            CustomDescriptionMobEffect customDescriptionMobEffect = (CustomDescriptionMobEffect)object;
            CustomDescriptionMobEffect.handleCustomPotionTooltip(pStack, pTooltipComponents, false, this.potionEffect.get(), customDescriptionMobEffect);
        }
    }
}

