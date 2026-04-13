package cyberspells.events;

import cyberspells.items.CyberRuneItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "cyberspells")
public class ModEvents {
    /*
     * @SubscribeEvent
     * public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
     * // Disabled as native ICyberware.getAttributeModifiers logic should work now
     * that Rune IDs are fixed.
     * }
     */
}
