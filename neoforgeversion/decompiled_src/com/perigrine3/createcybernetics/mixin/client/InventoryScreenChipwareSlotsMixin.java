/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.perigrine3.createcybernetics.mixin.client;

import com.perigrine3.createcybernetics.api.IChipwareSlotsMenu;
import com.perigrine3.createcybernetics.mixin.client.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InventoryScreen.class})
public abstract class InventoryScreenChipwareSlotsMixin {
    private static final int CC_SLOT_X = 77;
    private static final int CC_SLOT_Y0 = 8;
    private static final int CC_SLOT_SPACING = 18;

    @Inject(method={"renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V"}, at={@At(value="TAIL")})
    private void cc$renderChipwareSlotBackgrounds(GuiGraphics gg, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        IChipwareSlotsMenu chip;
        AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor)((Object)this);
        AbstractContainerMenu menu = accessor.cc$getMenu();
        if (!(menu instanceof IChipwareSlotsMenu) || !(chip = (IChipwareSlotsMenu)menu).cc$chipwareSlotsActive()) {
            return;
        }
        int leftPos = accessor.cc$getLeftPos();
        int topPos = accessor.cc$getTopPos();
        int sx = leftPos + 77;
        int sy0 = topPos + 8;
        int sy1 = sy0 + 18;
        InventoryScreenChipwareSlotsMixin.cc$drawSlotBackground(gg, sx, sy0);
        InventoryScreenChipwareSlotsMixin.cc$drawSlotBackground(gg, sx, sy1);
    }

    private static void cc$drawSlotBackground(GuiGraphics gg, int x, int y) {
        int left = x - 1;
        int top = y - 1;
        int right = x + 17;
        int bottom = y + 17;
        gg.fill(left, top, right, bottom, -536541430);
        gg.fill(left, top, right, top + 1, -11881473);
        gg.fill(left, bottom - 1, right, bottom, -11881473);
        gg.fill(left, top, left + 1, bottom, -11881473);
        gg.fill(right - 1, top, right, bottom, -11881473);
    }
}

