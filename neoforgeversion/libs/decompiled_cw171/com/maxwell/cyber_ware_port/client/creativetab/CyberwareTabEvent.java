/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.CreativeModeTab$ItemDisplayParameters
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ScreenEvent$Closing
 *  net.neoforged.neoforge.client.event.ScreenEvent$Init$Post
 *  net.neoforged.neoforge.client.event.ScreenEvent$Render$Post
 */
package com.maxwell.cyber_ware_port.client.creativetab;

import com.maxwell.cyber_ware_port.CyberWare;
import com.maxwell.cyber_ware_port.client.creativetab.CyberwareSideTabButton;
import com.maxwell.cyber_ware_port.common.CyberwareTabState;
import com.maxwell.cyber_ware_port.init.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid="cyber_ware_port", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public class CyberwareTabEvent {
    private static final ResourceLocation TAB_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port".toLowerCase(), (String)"textures/gui/extended_tabs.png");
    private static final List<CyberwareSideTabButton> customTabs = new ArrayList<CyberwareSideTabButton>();
    private static boolean isReloading = false;
    private static Field cachedSelectedTabField = null;

    @SubscribeEvent
    public static void onScreenInitPost(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof CreativeModeInventoryScreen) {
            CreativeModeInventoryScreen screen2 = (CreativeModeInventoryScreen)screen;
            customTabs.clear();
            int guiLeft = screen2.getGuiLeft();
            int guiTop = screen2.getGuiTop();
            int buttonX = guiLeft - 21;
            CyberwareSideTabButton btn1 = new CyberwareSideTabButton(buttonX, guiTop + 8, 17, 17, btn -> {
                if (CyberwareTabState.currentPage != 1) {
                    CyberwareTabState.currentPage = 1;
                    CyberwareTabEvent.reloadScreen();
                }
            });
            CyberwareSideTabButton btn2 = new CyberwareSideTabButton(buttonX, guiTop + 31, 17, 17, btn -> {
                if (CyberwareTabState.currentPage != 0) {
                    CyberwareTabState.currentPage = 0;
                    CyberwareTabEvent.reloadScreen();
                }
            });
            event.addListener((GuiEventListener)btn1);
            event.addListener((GuiEventListener)btn2);
            customTabs.add(btn1);
            customTabs.add(btn2);
            CyberwareTabEvent.updateVisibility(screen2);
        }
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        CreativeModeInventoryScreen screen;
        Screen screen2 = event.getScreen();
        if (screen2 instanceof CreativeModeInventoryScreen && CyberwareTabEvent.updateVisibility(screen = (CreativeModeInventoryScreen)screen2)) {
            int guiLeft = screen.getGuiLeft();
            int guiTop = screen.getGuiTop();
            int panelX = guiLeft - 28;
            event.getGuiGraphics().pose().pushPose();
            event.getGuiGraphics().pose().translate(0.0f, 0.0f, 100.0f);
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)TAB_TEXTURE);
            RenderSystem.enableBlend();
            event.getGuiGraphics().blit(TAB_TEXTURE, panelX, guiTop, 0.0f, 0.0f, 28, 128, 256, 256);
            RenderSystem.disableBlend();
            event.getGuiGraphics().pose().popPose();
        }
    }

    @SubscribeEvent
    public static void onScreenClosing(ScreenEvent.Closing event) {
        if (event.getScreen() instanceof CreativeModeInventoryScreen && !isReloading) {
            CyberwareTabState.currentPage = 0;
        }
    }

    private static void reloadScreen() {
        isReloading = true;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            CreativeModeTab tab = (CreativeModeTab)ModItems.CW_TABS.get();
            tab.buildContents(new CreativeModeTab.ItemDisplayParameters(mc.player.connection.enabledFeatures(), ((Boolean)mc.options.operatorItemsTab().get()).booleanValue(), (HolderLookup.Provider)mc.player.level().registryAccess()));
            mc.setScreen((Screen)new CreativeModeInventoryScreen(mc.player, mc.player.connection.enabledFeatures(), ((Boolean)mc.options.operatorItemsTab().get()).booleanValue()));
        }
        isReloading = false;
    }

    private static CreativeModeTab getSelectedTab(CreativeModeInventoryScreen screen) {
        try {
            if (cachedSelectedTabField == null) {
                for (Field field : CreativeModeInventoryScreen.class.getDeclaredFields()) {
                    if (field.getType() != CreativeModeTab.class || !Modifier.isStatic(field.getModifiers())) continue;
                    field.setAccessible(true);
                    cachedSelectedTabField = field;
                    break;
                }
            }
            if (cachedSelectedTabField != null) {
                return (CreativeModeTab)cachedSelectedTabField.get(null);
            }
        }
        catch (Exception e) {
            CyberWare.LOGGER.error("Failed to get selected tab via reflection", (Throwable)e);
        }
        return null;
    }

    private static boolean updateVisibility(CreativeModeInventoryScreen screen) {
        CreativeModeTab selectedTab = CyberwareTabEvent.getSelectedTab(screen);
        if (selectedTab == null) {
            return false;
        }
        boolean isMyTab = selectedTab == ModItems.CW_TABS.get();
        for (CyberwareSideTabButton btn : customTabs) {
            btn.visible = isMyTab;
            btn.active = isMyTab;
        }
        return isMyTab;
    }
}

