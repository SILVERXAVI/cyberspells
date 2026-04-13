/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.PauseScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentContents
 *  net.minecraft.network.chat.contents.TranslatableContents
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ScreenEvent$Init$Post
 */
package com.perigrine3.createcybernetics.client.gui;

import com.perigrine3.createcybernetics.client.gui.CCIconButton;
import com.perigrine3.createcybernetics.client.gui.CybereyeSkinConfigScreen;
import com.perigrine3.createcybernetics.client.gui.HudLayoutScreen;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class CCPauseMenuButtons {
    private static final int ICON_W = 20;
    private static final int GAP_PX = 4;
    private static final String KEY_OPTIONS = "menu.options";
    private static final String KEY_MODS_FML = "fml.menu.mods";
    private static final String KEY_MODS_NEOFORGE = "neoforge.menu.mods";
    private static final String CC_MARKER_ID = "createcybernetics_pause_buttons";

    private CCPauseMenuButtons() {
    }

    @SubscribeEvent
    public static void onScreenInitPost(ScreenEvent.Init.Post event) {
        int y;
        int x;
        Screen screen = event.getScreen();
        if (!(screen instanceof PauseScreen)) {
            return;
        }
        PauseScreen pause = (PauseScreen)screen;
        if (CCPauseMenuButtons.alreadyHasOurButtons(event)) {
            return;
        }
        AbstractWidget optionsBtn = CCPauseMenuButtons.findButtonByTranslatableKey(event, KEY_OPTIONS);
        AbstractWidget modsBtn = CCPauseMenuButtons.findButtonByTranslatableKey(event, KEY_MODS_FML);
        if (modsBtn == null) {
            modsBtn = CCPauseMenuButtons.findButtonByTranslatableKey(event, KEY_MODS_NEOFORGE);
        }
        if (modsBtn == null) {
            modsBtn = CCPauseMenuButtons.findButtonByTextFallback(event, "mods");
        }
        if (optionsBtn != null) {
            x = optionsBtn.getX() - 24;
            y = optionsBtn.getY();
            CCIconButton hudBtn = new CCIconButton(x, y, 20, optionsBtn.getHeight(), CCIconButton.Icon.HUD_LAYOUT, (Component)Component.translatable((String)"gui.createcybernetics.pause.hud_layout"), CC_MARKER_ID, () -> CCPauseMenuButtons.openHudLayout(pause));
            event.addListener((GuiEventListener)hudBtn);
        }
        if (modsBtn != null) {
            x = modsBtn.getX() - 24;
            y = modsBtn.getY();
            CCIconButton eyeBtn = new CCIconButton(x, y, 20, modsBtn.getHeight(), CCIconButton.Icon.CYBEREYE_SKIN, (Component)Component.translatable((String)"gui.createcybernetics.pause.cybereye_skin"), CC_MARKER_ID, () -> CCPauseMenuButtons.openCybereyeSkin(pause));
            event.addListener((GuiEventListener)eyeBtn);
        }
    }

    private static boolean alreadyHasOurButtons(ScreenEvent.Init.Post event) {
        for (GuiEventListener child : event.getListenersList()) {
            CCIconButton b;
            if (!(child instanceof CCIconButton) || !CC_MARKER_ID.equals((b = (CCIconButton)child).cc$getMarkerId())) continue;
            return true;
        }
        return false;
    }

    private static void openHudLayout(PauseScreen parent) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) {
            return;
        }
        mc.setScreen((Screen)new HudLayoutScreen((Screen)parent));
    }

    private static void openCybereyeSkin(PauseScreen parent) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) {
            return;
        }
        mc.setScreen((Screen)new CybereyeSkinConfigScreen((Screen)parent));
    }

    private static AbstractWidget findButtonByTranslatableKey(ScreenEvent.Init.Post event, String key) {
        for (GuiEventListener child : event.getListenersList()) {
            AbstractWidget w;
            String k;
            if (!(child instanceof AbstractWidget) || (k = CCPauseMenuButtons.getTranslatableKey((w = (AbstractWidget)child).getMessage())) == null || !k.equals(key)) continue;
            return w;
        }
        return null;
    }

    private static AbstractWidget findButtonByTextFallback(ScreenEvent.Init.Post event, String needleLower) {
        for (GuiEventListener child : event.getListenersList()) {
            AbstractWidget w;
            String s;
            if (!(child instanceof AbstractWidget) || !(s = (w = (AbstractWidget)child).getMessage() != null ? w.getMessage().getString() : "").toLowerCase(Locale.ROOT).contains(needleLower)) continue;
            return w;
        }
        return null;
    }

    private static String getTranslatableKey(Component c) {
        if (c == null) {
            return null;
        }
        ComponentContents componentContents = c.getContents();
        if (componentContents instanceof TranslatableContents) {
            TranslatableContents tc = (TranslatableContents)componentContents;
            return tc.getKey();
        }
        return null;
    }
}

