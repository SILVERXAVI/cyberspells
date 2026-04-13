/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
 *  net.minecraft.world.inventory.Slot
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.perigrine3.createcybernetics.mixin.client;

import com.perigrine3.createcybernetics.screen.slot.DataShardSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets={"net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen$SlotWrapper"})
public abstract class CreativeSlotWrapperHideChipwareMixin {
    @Shadow
    @Final
    Slot target;

    @Inject(method={"isActive"}, at={@At(value="HEAD")}, cancellable=true)
    private void cc$hideChipwareSlotsInCreative(CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen cms;
        Screen screen = Minecraft.getInstance().screen;
        if (!(screen instanceof CreativeModeInventoryScreen) || !(cms = (CreativeModeInventoryScreen)screen).isInventoryOpen()) {
            return;
        }
        if (this.target instanceof DataShardSlot) {
            cir.setReturnValue((Object)false);
        }
    }
}

