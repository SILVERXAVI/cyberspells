/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffectUtil
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.neoforged.neoforge.fluids.FluidStack
 *  net.neoforged.neoforge.fluids.FluidType
 *  net.neoforged.neoforge.fluids.FluidType$Properties
 */
package io.redspace.ironsspellbooks.fluids;

import io.redspace.ironsspellbooks.fluids.PotionFluid;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

public class PotionFluidType
extends FluidType {
    public PotionFluidType(FluidType.Properties properties) {
        super(properties);
    }

    public String getDescriptionId(FluidStack stack) {
        PotionContents potionContents = (PotionContents)stack.get(DataComponents.POTION_CONTENTS);
        PotionFluid.BottleType bottle = (PotionFluid.BottleType)((Object)stack.getOrDefault(ComponentRegistry.POTION_BOTTLE_TYPE, (Object)PotionFluid.BottleType.REGULAR));
        if (potionContents != null) {
            return Potion.getName((Optional)potionContents.potion(), (String)String.format("item.minecraft.%s.effect.", bottle.descriptionId()));
        }
        return super.getDescriptionId(stack);
    }

    public Component getDescription(FluidStack stack) {
        PotionContents potionContents = (PotionContents)stack.get(DataComponents.POTION_CONTENTS);
        if (potionContents != null && potionContents.hasEffects()) {
            Iterable effects = potionContents.getAllEffects();
            MobEffectInstance primary = (MobEffectInstance)effects.iterator().next();
            MutableComponent component = Component.translatable((String)this.getDescriptionId(stack));
            if (primary.getAmplifier() > 0) {
                component = component.append(" " + this.simpleRomanNumeral(primary.getAmplifier() + 1));
            }
            if (!((MobEffect)primary.getEffect().value()).isInstantenous() && primary.getDuration() > 0) {
                component = component.append(String.format(" (%s)", MobEffectUtil.formatDuration((MobEffectInstance)primary, (float)1.0f, (float)20.0f).getString()));
            }
            return component;
        }
        return super.getDescription(stack);
    }

    private String simpleRomanNumeral(int i) {
        return switch (i) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(i);
        };
    }
}

