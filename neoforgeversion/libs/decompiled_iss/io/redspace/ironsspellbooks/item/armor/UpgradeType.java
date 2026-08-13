/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.item.Item
 */
package io.redspace.ironsspellbooks.item.armor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public interface UpgradeType {
    public static final Map<ResourceLocation, UpgradeType> UPGRADE_REGISTRY = new HashMap<ResourceLocation, UpgradeType>();

    public static void registerUpgrade(UpgradeType upgrade) {
        UPGRADE_REGISTRY.put(upgrade.getId(), upgrade);
    }

    public static Optional<UpgradeType> getUpgrade(ResourceLocation key) {
        UpgradeType upgradeType = UPGRADE_REGISTRY.get(key);
        return upgradeType == null ? Optional.empty() : Optional.of(upgradeType);
    }

    public Holder<Attribute> getAttribute();

    public AttributeModifier.Operation getOperation();

    public float getAmountPerUpgrade();

    public ResourceLocation getId();

    public Optional<Holder<Item>> getContainerItem();
}

