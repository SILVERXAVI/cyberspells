package cyberspells.items;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.item.cyberware.arm.CyberarmItem;
import cyberspells.config.CyberSpellsConfig;
import cyberspells.logic.RuneAttributeManager;
import cyberspells.registration.ModDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;
import java.util.Set;
import java.util.Collections;

// Cyberware port imports
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class CyberRuneArmItem extends CyberarmItem implements RuneHolder, ICyberware {
    private final String partName;

    public CyberRuneArmItem(Properties properties, String partName) {
        super(properties, 10,
                partName.contains("left") ? CyberwareSlot.LARM : CyberwareSlot.RARM);
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
        return CyberSpellsConfig.ARM_SLOTS.get();
    }

    @Override
    public List<String> getRunes(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.RUNES.get(), List.of());
    }

    @Override
    public int getHumanityCost() {
        return CyberSpellsConfig.ARM_ESSENCE.get().intValue();
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(partName.contains("left") ? CyberwareSlot.LARM : CyberwareSlot.RARM);
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

    // ICyberware implementations
    @Override
    public int getEssenceCost(ItemStack stack) {
        return getHumanityCost();
    }

    @Override
    public int getSlot(ItemStack stack) {
        // ARMS slot start index is 14 * 9 = 126
        return 126;
    }

    @Override
    public boolean isPristine(ItemStack stack) {
        return stack.getOrDefault((net.minecraft.core.component.DataComponentType<Boolean>) com.maxwell.cyber_ware_port.init.ModDataComponents.PRISTINE.get(), true);
    }

    @Override
    public void setPristine(ItemStack stack, boolean pristine) {
        stack.set((net.minecraft.core.component.DataComponentType<Boolean>) com.maxwell.cyber_ware_port.init.ModDataComponents.PRISTINE.get(), pristine);
    }

    @Override
    public int getMaxInstallAmount(ItemStack stack) {
        return 1;
    }

    @Override
    public Set<net.minecraft.world.item.Item> getPrerequisites(ItemStack stack) {
        return Collections.emptySet();
    }

    @Override
    public boolean hasEnergyProperties(ItemStack stack) {
        return false;
    }

    @Override
    public int getEnergyConsumption(ItemStack stack) {
        return 0;
    }

    @Override
    public int getEnergyGeneration(ItemStack stack) {
        return 0;
    }

    @Override
    public int getEnergyStorage(ItemStack stack) {
        return 0;
    }

    @Override
    public ICyberware.StackingRule getStackingEnergyRule(ItemStack stack) {
        return ICyberware.StackingRule.STATIC;
    }

    @Override
    public BodyPartType getBodyPartType(ItemStack stack) {
        return partName.contains("left") ? BodyPartType.ARM_LEFT : BodyPartType.ARM_RIGHT;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        return ArrayListMultimap.create();
    }

    @Override
    public void onInstalled(LivingEntity entity, ItemStack stack) {
        // Resolve interface conflict between ICyberware and ICyberwareItem
    }

    @Override
    public void onRemoved(LivingEntity entity, ItemStack stack) {
        // Resolve interface conflict between ICyberware and ICyberwareItem
    }
}
