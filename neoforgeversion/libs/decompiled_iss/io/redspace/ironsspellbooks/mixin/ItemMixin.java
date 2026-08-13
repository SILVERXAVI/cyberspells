/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.api.item.UpgradeData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Item.class})
public abstract class ItemMixin {
    @Inject(method={"getName"}, at={@At(value="TAIL")}, cancellable=true)
    public void getHoverName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
        if (UpgradeData.hasUpgradeData(stack)) {
            cir.setReturnValue((Object)Component.translatable((String)"tooltip.irons_spellbooks.upgrade_plus_format", (Object[])new Object[]{cir.getReturnValue(), UpgradeData.getUpgradeData(stack).getTotalUpgrades()}));
        }
    }
}

