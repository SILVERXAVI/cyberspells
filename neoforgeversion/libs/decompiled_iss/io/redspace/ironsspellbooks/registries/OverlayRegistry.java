/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
 *  net.neoforged.neoforge.client.gui.VanillaGuiLayers
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.gui.overlays.CastBarOverlay;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import io.redspace.ironsspellbooks.gui.overlays.RecastOverlay;
import io.redspace.ironsspellbooks.gui.overlays.ScreenEffectsOverlay;
import io.redspace.ironsspellbooks.gui.overlays.ScreenTooltipOverlay;
import io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay;
import io.redspace.ironsspellbooks.gui.overlays.SpellWheelOverlay;
import net.minecraft.client.gui.LayeredDraw;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid="irons_spellbooks", bus=EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
public class OverlayRegistry {
    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.CROSSHAIR, IronsSpellbooks.id("cast_bar"), (LayeredDraw.Layer)CastBarOverlay.instance);
        event.registerAbove(VanillaGuiLayers.AIR_LEVEL, IronsSpellbooks.id("mana_overlay"), (LayeredDraw.Layer)ManaBarOverlay.instance);
        event.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, IronsSpellbooks.id("spell_bar"), (LayeredDraw.Layer)SpellBarOverlay.instance);
        event.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, IronsSpellbooks.id("recast_bar"), (LayeredDraw.Layer)RecastOverlay.instance);
        event.registerAboveAll(IronsSpellbooks.id("spell_wheel"), (LayeredDraw.Layer)SpellWheelOverlay.instance);
        event.registerAboveAll(IronsSpellbooks.id("screen_effects"), (LayeredDraw.Layer)ScreenEffectsOverlay.instance);
        event.registerAboveAll(IronsSpellbooks.id("screen_tooltip"), (LayeredDraw.Layer)ScreenTooltipOverlay.instance);
    }
}

