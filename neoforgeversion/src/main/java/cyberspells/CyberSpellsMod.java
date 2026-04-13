package cyberspells;

import cyberspells.registration.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(CyberSpellsMod.MODID)
public class CyberSpellsMod {
    public static final String MODID = "cyberspells";

    public CyberSpellsMod(IEventBus modEventBus, net.neoforged.fml.ModContainer container) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMenus.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModDataComponents.register(modEventBus);

        // Register Config (NeoForge pattern)
        container.registerConfig(ModConfig.Type.COMMON, cyberspells.config.CyberSpellsConfig.SPEC);

        System.out.println("CyberSpells for NeoForge 1.21.1 initialized!");
    }
}
