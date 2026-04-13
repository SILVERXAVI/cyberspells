package cyberspells.registration;

import cyberspells.CyberSpellsMod;
import cyberspells.menu.RuneInfuserMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU,
            CyberSpellsMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<RuneInfuserMenu>> RUNE_INFUSER_MENU = MENUS.register(
            "rune_infuser_menu",
            () -> IMenuTypeExtension.create(RuneInfuserMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
