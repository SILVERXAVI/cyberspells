/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.common.extensions.IMenuTypeExtension
 *  net.neoforged.neoforge.network.IContainerFactory
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.maxwell.cyber_ware_port.init;

import com.maxwell.cyber_ware_port.common.container.BlueprintChestMenu;
import com.maxwell.cyber_ware_port.common.container.ComponentBoxMenu;
import com.maxwell.cyber_ware_port.common.container.CyberwareWorkbenchMenu;
import com.maxwell.cyber_ware_port.common.container.RobosurgeonMenu;
import com.maxwell.cyber_ware_port.common.container.ScannerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create((ResourceKey)Registries.MENU, (String)"cyber_ware_port");
    public static final DeferredHolder<MenuType<?>, MenuType<RobosurgeonMenu>> ROBO_SURGEON_MENU = ModMenuTypes.registerMenuType("robosurgeon_menu", RobosurgeonMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<CyberwareWorkbenchMenu>> CYBERWARE_WORKBENCH_MENU = ModMenuTypes.registerMenuType("cyberware_workbench_menu", CyberwareWorkbenchMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ScannerMenu>> SCANNER_MENU = ModMenuTypes.registerMenuType("scanner_menu", ScannerMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ComponentBoxMenu>> COMPONENT_BOX_MENU = ModMenuTypes.registerMenuType("component_menu", ComponentBoxMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<BlueprintChestMenu>> BLUEPRINT_CHEST_MENU = ModMenuTypes.registerMenuType("blueprint_chest_menu", BlueprintChestMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create((IContainerFactory)factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}

