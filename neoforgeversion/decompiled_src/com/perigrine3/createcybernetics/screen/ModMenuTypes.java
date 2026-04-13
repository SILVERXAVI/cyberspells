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
package com.perigrine3.createcybernetics.screen;

import com.perigrine3.createcybernetics.screen.custom.ArmCannonMenu;
import com.perigrine3.createcybernetics.screen.custom.ChipwareMiniMenu;
import com.perigrine3.createcybernetics.screen.custom.EngineeringTableMenu;
import com.perigrine3.createcybernetics.screen.custom.ExpandedInventoryMenu;
import com.perigrine3.createcybernetics.screen.custom.GraftingTableMenu;
import com.perigrine3.createcybernetics.screen.custom.HeatEngineMenu;
import com.perigrine3.createcybernetics.screen.custom.RobosurgeonMenu;
import com.perigrine3.createcybernetics.screen.custom.SpinalInjectorMenu;
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
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create((ResourceKey)Registries.MENU, (String)"createcybernetics");
    public static final DeferredHolder<MenuType<?>, MenuType<RobosurgeonMenu>> ROBOSURGEON_MENU = ModMenuTypes.registerMenuType("robosurgeon_menu", RobosurgeonMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<EngineeringTableMenu>> ENGINEERING_TABLE_MENU = ModMenuTypes.registerMenuType("engineering_table_menu", EngineeringTableMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<GraftingTableMenu>> GRAFTING_TABLE_MENU = ModMenuTypes.registerMenuType("grafting_table_menu", GraftingTableMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ExpandedInventoryMenu>> EXPANDED_INVENTORY_MENU = ModMenuTypes.registerMenuType("expanded_inventory_menu", ExpandedInventoryMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ChipwareMiniMenu>> CHIPWARE_MINI_MENU = ModMenuTypes.registerMenuType("chipware_mini_menu", ChipwareMiniMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<SpinalInjectorMenu>> SPINAL_INJECTOR_MENU = ModMenuTypes.registerMenuType("spinal_injector_menu", SpinalInjectorMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ArmCannonMenu>> ARM_CANNON_MENU = ModMenuTypes.registerMenuType("arm_cannon_menu", ArmCannonMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<HeatEngineMenu>> HEAT_ENGINE_MENU = ModMenuTypes.registerMenuType("heat_engine_menu", HeatEngineMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create((IContainerFactory)factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}

