package cyberspells.items;

import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import cyberspells.logic.RuneAttributeManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CyberRuneHeartItem extends CyberRuneItem {
    public CyberRuneHeartItem(Properties properties, String partName) {
        super(properties, partName);
    }

    @Override
    public String getPartName() {
        return partName;
    }

    public void applyEffects(Player player, ItemStack stack) {
        // Deprecated: Attributes are handled via getAttributeModifiers
    }

    @Override
    public com.google.common.collect.Multimap<net.minecraft.world.entity.ai.attributes.Attribute, net.minecraft.world.entity.ai.attributes.AttributeModifier> getAttributeModifiers(
            ItemStack stack) {
        com.google.common.collect.Multimap<net.minecraft.world.entity.ai.attributes.Attribute, net.minecraft.world.entity.ai.attributes.AttributeModifier> map = com.google.common.collect.HashMultimap
                .create();

        List<String> runes = getRunes(stack);
        for (int i = 0; i < runes.size(); i++) {
            String runeId = runes.get(i);
            RuneAttributeManager.addAttributeToMap(map, runeId,
                    UUID.nameUUIDFromBytes((partName + "_" + i).getBytes()), "CyberRune " + partName);
        }
        return map;
    }

    @Override
    public int getSlot(ItemStack stack) {
        System.out.println("DEBUG: CyberRuneHeartItem.getSlot()");
        return 18;
    }

    // ICyberware Implementation

    @Override
    public boolean isPristine(ItemStack stack) {
        return true;
    }

    @Override
    public void setPristine(ItemStack stack, boolean pristine) {
    }

    @Override
    public int getMaxInstallAmount(ItemStack stack) {
        return 1;
    }

    @Override
    public Set<Item> getPrerequisites(ItemStack stack) {
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
    public StackingRule getStackingEnergyRule(ItemStack stack) {
        return StackingRule.STATIC;
    }

    @Override
    public BodyPartType getBodyPartType(ItemStack stack) {
        return BodyPartType.HEART;
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable net.minecraft.world.level.Level level,
            List<net.minecraft.network.chat.Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        RuneAttributeManager.appendTooltip(stack, tooltip);
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
