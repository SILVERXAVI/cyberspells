/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.alchemy.Potions
 *  net.neoforged.neoforge.fluids.FluidStack
 */
package io.redspace.ironsspellbooks.fluids;

import io.redspace.ironsspellbooks.fluids.SimpleClientFluidType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.fluids.FluidStack;

public class PotionClientFluidType
extends SimpleClientFluidType {
    public PotionClientFluidType(ResourceLocation texture) {
        super(texture);
    }

    public int getTintColor(FluidStack stack) {
        return (stack.has(DataComponents.POTION_CONTENTS) ? ((PotionContents)stack.get(DataComponents.POTION_CONTENTS)).getColor() : PotionContents.getColor((Holder)Potions.WATER)) | 0xFF000000;
    }
}

