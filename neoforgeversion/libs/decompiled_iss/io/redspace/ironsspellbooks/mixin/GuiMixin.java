/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.world.entity.player.Player
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Gui.class})
public class GuiMixin {
    @Inject(method={"isExperienceBarVisible"}, at={@At(value="HEAD")}, cancellable=true)
    public void irons_spellbooks$disableXpBar(CallbackInfoReturnable<Boolean> cir) {
        if (ClientConfigs.MANA_BAR_ANCHOR.get() == ManaBarOverlay.Anchor.XP && Minecraft.getInstance().player != null && ManaBarOverlay.shouldShowManaBar((Player)Minecraft.getInstance().player)) {
            cir.setReturnValue((Object)false);
        }
    }
}

