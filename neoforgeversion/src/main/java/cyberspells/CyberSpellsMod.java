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

    @net.neoforged.fml.common.EventBusSubscriber(modid = CyberSpellsMod.MODID, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.MOD, value = net.neoforged.api.distmarker.Dist.CLIENT)
    public static class ClientModEvents {
        @net.neoforged.bus.api.SubscribeEvent
        public static void onClientSetup(net.neoforged.fml.event.lifecycle.FMLClientSetupEvent event) {
            if (net.neoforged.fml.ModList.get().isLoaded("createcybernetics")) {
                CCRendererRegistrar.register();
            }
        }
    }

    public static class CCRendererRegistrar {
        public static void register() {
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(cyberspells.client.RuneFirstPersonRenderer.class);
        }
    }
}
