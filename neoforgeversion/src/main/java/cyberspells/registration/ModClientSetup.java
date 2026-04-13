package cyberspells.registration;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import cyberspells.CyberSpellsMod;
import cyberspells.client.RuneInfuserScreen;
import cyberspells.client.RuneSkinLayer;

@EventBusSubscriber(modid = CyberSpellsMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientSetup {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.RUNE_INFUSER_MENU.get(), RuneInfuserScreen::new);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skinModel : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skinModel);
            if (renderer != null) {
                renderer.addLayer(new RuneSkinLayer(renderer));
            }
        }
    }
}
