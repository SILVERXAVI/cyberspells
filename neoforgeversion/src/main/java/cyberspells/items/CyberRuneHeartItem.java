package cyberspells.items;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.item.cyberware.MechanicalHeartItem;
import cyberspells.config.CyberSpellsConfig;
import cyberspells.logic.RuneAttributeManager;
import cyberspells.registration.ModDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.Set;

public class CyberRuneHeartItem extends MechanicalHeartItem implements RuneHolder {
    private final String partName;

    public CyberRuneHeartItem(Properties properties, String partName) {
        super(properties, 10);
        this.partName = partName;
    }

    @Override
    public String getPartName() {
        return partName;
    }

    @Override
    public int getMaxRuneSlots() {
        return CyberSpellsConfig.HEART_SLOTS.get();
    }

    @Override
    public List<String> getRunes(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.RUNES.get(), List.of());
    }

    @Override
    public int getHumanityCost() {
        return CyberSpellsConfig.HEART_ESSENCE.get().intValue();
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.HEART);
    }

    @Override
    public void onTick(Player player, ItemStack stack, CyberwareSlot slot, int slotIndex) {
        super.onTick(player, stack, slot, slotIndex);
        if (!player.level().isClientSide) {
            RuneAttributeManager.manageAttributes(player, stack, partName);
        }
    }

    @Override
    public void onRemoved(Player player) {
        super.onRemoved(player);
        if (!player.level().isClientSide) {
            RuneAttributeManager.removeAttributes(player, partName);
        }
    }

    @Override
    public boolean replacesOrgan() {
        return true;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return getSupportedSlots();
    }
}
