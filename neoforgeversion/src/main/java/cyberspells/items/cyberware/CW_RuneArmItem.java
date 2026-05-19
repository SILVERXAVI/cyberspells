package cyberspells.items.cyberware;

import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import cyberspells.config.CyberSpellsConfig;
import cyberspells.items.RuneHolder;
import cyberspells.logic.RuneAttributeManager;
import cyberspells.registration.ModDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;

public class CW_RuneArmItem extends CyberwareItem implements RuneHolder {
    private final String partName;

    public CW_RuneArmItem(int essenceCost, int slotId, String partName) {
        super(new CyberwareItem.Builder(essenceCost, slotId)
                .bodyPart(partName.contains("left") ? BodyPartType.ARM_LEFT : BodyPartType.ARM_RIGHT)
                .maxInstall(1));
        this.partName = partName;
    }

    @Override
    public String getPartName() {
        return partName;
    }

    @Override
    public int getEssenceCost(ItemStack stack) {
        return CyberSpellsConfig.ARM_ESSENCE.get().intValue();
    }

    @Override
    public int getMaxRuneSlots() {
        return CyberSpellsConfig.ARM_SLOTS.get();
    }

    @Override
    public List<String> getRunes(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.RUNES.get(), List.of());
    }

    @Override
    public com.google.common.collect.Multimap<net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute>, net.minecraft.world.entity.ai.attributes.AttributeModifier> getAttributeModifiers(ItemStack stack) {
        com.google.common.collect.Multimap<net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute>, net.minecraft.world.entity.ai.attributes.AttributeModifier> map = com.google.common.collect.ArrayListMultimap.create();
        map.putAll(super.getAttributeModifiers(stack));
        
        List<String> runes = getRunes(stack);
        int maxSlots = getMaxRuneSlots();
        for (int i = 0; i < runes.size() && i < maxSlots; i++) {
            String runeId = runes.get(i);
            cyberspells.logic.RuneAttributeManager.addAttributesToMultimap(map, runeId, partName + "_" + i);
        }
        return map;
    }
}
