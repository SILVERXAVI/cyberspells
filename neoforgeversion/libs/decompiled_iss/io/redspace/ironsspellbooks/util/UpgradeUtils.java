/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.ItemAttributeModifiers$Entry
 *  top.theillusivec4.curios.api.CuriosApi
 *  top.theillusivec4.curios.api.type.capability.ICurioItem
 */
package io.redspace.ironsspellbooks.util;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.item.UpgradeData;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class UpgradeUtils {
    public static final Map<EquipmentSlot, UUID> UPGRADE_UUIDS_BY_SLOT = Map.of(EquipmentSlot.HEAD, UUID.fromString("f6c19678-1c70-4d41-ad19-cd84d8610242"), EquipmentSlot.CHEST, UUID.fromString("8d02c916-b0eb-4d17-8414-329b4bd38ae7"), EquipmentSlot.LEGS, UUID.fromString("3739c748-98d4-4a2d-9c25-3b4dec74823d"), EquipmentSlot.FEET, UUID.fromString("41cede88-7881-42dd-aac3-d6ab4b56b1f2"), EquipmentSlot.MAINHAND, UUID.fromString("c3865ad7-1f35-46d4-8b4b-a6b934a1a896"), EquipmentSlot.OFFHAND, UUID.fromString("c508430e-7497-42a9-9a9c-1a324dccca54"));

    public static String getRelevantEquipmentSlot(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ICurioItem) {
            ICurioItem curioItem = (ICurioItem)item;
            Set tags = CuriosApi.getCuriosHelper().getCurioTags((Item)curioItem);
            Optional slot = tags.stream().findFirst();
            if (slot.isPresent()) {
                return (String)slot.get();
            }
        } else {
            item = itemStack.getItem();
            if (item instanceof ArmorItem) {
                ArmorItem armorItem = (ArmorItem)item;
                return armorItem.getEquipmentSlot().getName();
            }
        }
        return EquipmentSlot.MAINHAND.getName();
    }

    public static UUID UUIDForSlot(EquipmentSlot slot) {
        return UPGRADE_UUIDS_BY_SLOT.get(slot);
    }

    public static void handleAttributeEvent(List<ItemAttributeModifiers.Entry> modifiers, UpgradeData upgradeData, BiConsumer<Holder<Attribute>, AttributeModifier> addCallback, BiConsumer<Holder<Attribute>, AttributeModifier> removeCallback, String slotId) {
        Map<Holder<UpgradeOrbType>, Integer> upgrades = upgradeData.upgrades();
        for (Map.Entry<Holder<UpgradeOrbType>, Integer> entry : upgrades.entrySet()) {
            Holder<UpgradeOrbType> holder = entry.getKey();
            UpgradeOrbType upgradeType = (UpgradeOrbType)holder.value();
            if (holder.getKey() == null) continue;
            int count = entry.getValue();
            double baseAmount = UpgradeUtils.collectAndRemovePreexistingAttribute(modifiers, upgradeType.attribute(), upgradeType.operation(), removeCallback);
            addCallback.accept(upgradeType.attribute(), new AttributeModifier(IronsSpellbooks.id(String.format("%s_upgrade_%s", slotId, holder.getKey().location().getPath())), baseAmount + upgradeType.amount() * (double)count, upgradeType.operation()));
        }
    }

    public static double collectAndRemovePreexistingAttribute(List<ItemAttributeModifiers.Entry> modifiers, Holder<Attribute> key, AttributeModifier.Operation operationToMatch, BiConsumer<Holder<Attribute>, AttributeModifier> removeCallback) {
        for (ItemAttributeModifiers.Entry entry : modifiers) {
            if (!entry.attribute().equals(key) || !entry.modifier().operation().equals((Object)operationToMatch)) continue;
            removeCallback.accept(key, entry.modifier());
            return entry.modifier().amount();
        }
        return 0.0;
    }
}

