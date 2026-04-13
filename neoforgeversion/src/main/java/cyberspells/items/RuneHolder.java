package cyberspells.items;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public interface RuneHolder {
    List<String> getRunes(ItemStack stack);

    String getPartName();

    int getMaxRuneSlots();
}
