package cyberspells;

import cyberspells.registration.ModBlockEntities;
import cyberspells.registration.ModBlocks;
import cyberspells.registration.ModCreativeModeTabs;
import cyberspells.registration.ModItems;
import cyberspells.registration.ModMenus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CyberSpellsMod.MODID)
public class CyberSpellsMod {
    public static final String MODID = "cyberspells";

    public CyberSpellsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMenus.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        net.minecraftforge.fml.ModLoadingContext.get().registerConfig(
                net.minecraftforge.fml.config.ModConfig.Type.COMMON, cyberspells.config.CyberSpellsConfig.SPEC);

        System.out.println("¡CyberSpells cargado correctamente!");
    }
}
