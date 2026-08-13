/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.ingredients.IIngredientTypeWithSubtypes
 *  net.minecraft.world.level.material.Fluid
 *  net.neoforged.neoforge.fluids.FluidStack
 */
package io.redspace.ironsspellbooks.jei;

import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidSubtype
implements IIngredientTypeWithSubtypes<Fluid, FluidStack> {
    public Class<? extends FluidStack> getIngredientClass() {
        return FluidStack.class;
    }

    public Class<? extends Fluid> getIngredientBaseClass() {
        return Fluid.class;
    }

    public Fluid getBase(FluidStack ingredient) {
        return ingredient.getFluid();
    }
}

