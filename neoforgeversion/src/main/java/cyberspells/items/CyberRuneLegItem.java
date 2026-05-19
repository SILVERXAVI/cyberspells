package cyberspells.items;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.item.cyberware.leg.CyberlegItem;
import cyberspells.config.CyberSpellsConfig;
import cyberspells.logic.RuneAttributeManager;
import cyberspells.registration.ModDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;
import java.util.Set;

public class CyberRuneLegItem extends CyberlegItem implements RuneHolder {
    private final String partName;

    public CyberRuneLegItem(Properties properties, String partName) {
        super(properties, 0,
                partName.contains("left") ? CyberwareSlot.LLEG : CyberwareSlot.RLEG);
        this.partName = partName;
    }

    @Override
    public boolean requiresEnergyToFunction(LivingEntity entity, ItemStack installedStack, CyberwareSlot slot) {
        return false;
    }

    @Override
    public int getEnergyUsedPerTick(LivingEntity entity, ItemStack installedStack, CyberwareSlot slot) {
        return 0;
    }

    @Override
    public String getPartName() {
        return partName;
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
    public int getHumanityCost() {
        return CyberSpellsConfig.LEG_ESSENCE.get().intValue();
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(partName.contains("left") ? CyberwareSlot.LLEG : CyberwareSlot.RLEG);
    }

    @Override
    public void onTick(LivingEntity entity, ItemStack stack, CyberwareSlot slot, int slotIndex) {
        super.onTick(entity, stack, slot, slotIndex);

        if (!entity.level().isClientSide && entity instanceof Player player) {
            RuneAttributeManager.manageAttributes(player, stack, partName);
        }
    }

    @Override
    public void onRemoved(LivingEntity entity) {
        super.onRemoved(entity);
        if (!entity.level().isClientSide && entity instanceof Player player) {
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

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, List<net.minecraft.network.chat.Component> tooltipComponents, net.minecraft.world.item.TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(net.minecraft.network.chat.Component.translatable("tooltip.cyberspells." + partName).withStyle(net.minecraft.ChatFormatting.GRAY));
        cyberspells.logic.RuneAttributeManager.appendTooltip(stack, tooltipComponents);
    }
}
