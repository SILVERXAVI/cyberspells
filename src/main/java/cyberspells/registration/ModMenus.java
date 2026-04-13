package cyberspells.registration;

import cyberspells.CyberSpellsMod;
import cyberspells.menu.RuneInfuserMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            CyberSpellsMod.MODID);

    public static final RegistryObject<MenuType<RuneInfuserMenu>> RUNE_INFUSER_MENU = MENUS.register(
            "rune_infuser_menu",
            () -> IForgeMenuType.create(RuneInfuserMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
