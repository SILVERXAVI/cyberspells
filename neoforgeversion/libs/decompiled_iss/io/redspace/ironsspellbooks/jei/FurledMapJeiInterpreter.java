/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
 *  mezz.jei.api.ingredients.subtypes.UidContext
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FurledMapJeiInterpreter
implements ISubtypeInterpreter<ItemStack> {
    @Nullable
    public Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.get(ComponentRegistry.FURLED_MAP_COMPONENT);
    }

    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return "null";
    }
}

