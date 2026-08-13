/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
 *  mezz.jei.api.ingredients.subtypes.UidContext
 *  net.minecraft.core.component.DataComponents
 *  net.neoforged.neoforge.fluids.FluidStack
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class PotionFluidInterpreter
implements ISubtypeInterpreter<FluidStack> {
    @Nullable
    public Object getSubtypeData(FluidStack ingredient, UidContext context) {
        return ingredient.get(DataComponents.POTION_CONTENTS);
    }

    public String getLegacyStringSubtypeInfo(FluidStack ingredient, UidContext context) {
        return "null";
    }
}

