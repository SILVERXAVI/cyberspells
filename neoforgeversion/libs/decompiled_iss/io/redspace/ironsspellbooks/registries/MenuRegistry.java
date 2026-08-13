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
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.gui.arcane_anvil.ArcaneAnvilMenu;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableMenu;
import io.redspace.ironsspellbooks.gui.scroll_forge.ScrollForgeMenu;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuRegistry {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create((ResourceKey)Registries.MENU, (String)"irons_spellbooks");
    public static final Supplier<MenuType<InscriptionTableMenu>> INSCRIPTION_TABLE_MENU = MenuRegistry.registerMenuType(InscriptionTableMenu::new, "inscription_table_menu");
    public static final Supplier<MenuType<ScrollForgeMenu>> SCROLL_FORGE_MENU = MenuRegistry.registerMenuType(ScrollForgeMenu::new, "scroll_forge_menu");
    public static final Supplier<MenuType<ArcaneAnvilMenu>> ARCANE_ANVIL_MENU = MenuRegistry.registerMenuType(ArcaneAnvilMenu::new, "arcane_anvil_menu");

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IMenuTypeExtension.create((IContainerFactory)factory));
    }
}

