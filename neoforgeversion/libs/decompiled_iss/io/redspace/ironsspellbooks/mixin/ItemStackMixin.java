/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.PatchedDataComponentMap
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemStack.class})
public abstract class ItemStackMixin {
    @Inject(method={"Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V"}, at={@At(value="TAIL")})
    public void init(ItemLike itemLike, int pCount, PatchedDataComponentMap pComponents, CallbackInfo ci) {
        if (itemLike instanceof IPresetSpellContainer) {
            IPresetSpellContainer iPresetSpellContainer = (IPresetSpellContainer)itemLike;
            iPresetSpellContainer.initializeSpellContainer((ItemStack)this);
        }
    }
}

