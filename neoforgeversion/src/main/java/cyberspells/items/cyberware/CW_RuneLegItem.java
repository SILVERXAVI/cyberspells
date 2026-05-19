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

public class CW_RuneLegItem extends CyberwareItem implements RuneHolder {
    private final String partName;

    public CW_RuneLegItem(int essenceCost, int slotId, String partName) {
        super(new CyberwareItem.Builder(essenceCost, slotId)
                .bodyPart(partName.contains("left") ? BodyPartType.LEG_LEFT : BodyPartType.LEG_RIGHT)
                .maxInstall(1));
        this.partName = partName;
    }

    @Override
    public String getPartName() {
        return partName;
    }

    @Override
    public int getEssenceCost(ItemStack stack) {
        return CyberSpellsConfig.LEG_ESSENCE.get().intValue();
    }

    @Override
    public int getMaxRuneSlots() {
        return CyberSpellsConfig.LEG_SLOTS.get();
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

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, List<net.minecraft.network.chat.Component> tooltipComponents, net.minecraft.world.item.TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        cyberspells.logic.RuneAttributeManager.appendTooltip(stack, tooltipComponents);
    }
}
