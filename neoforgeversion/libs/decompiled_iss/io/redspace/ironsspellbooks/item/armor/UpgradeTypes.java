/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.item.Item
 */
package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.UpgradeType;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

@Deprecated(forRemoval=true)
public enum UpgradeTypes implements UpgradeType
{
    FIRE_SPELL_POWER("fire_power", (Holder<Item>)ItemRegistry.FIRE_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.FIRE_SPELL_POWER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    ICE_SPELL_POWER("ice_power", (Holder<Item>)ItemRegistry.ICE_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.ICE_SPELL_POWER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    LIGHTNING_SPELL_POWER("lightning_power", (Holder<Item>)ItemRegistry.LIGHTNING_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.LIGHTNING_SPELL_POWER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    HOLY_SPELL_POWER("holy_power", (Holder<Item>)ItemRegistry.HOLY_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.HOLY_SPELL_POWER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    ENDER_SPELL_POWER("ender_power", (Holder<Item>)ItemRegistry.ENDER_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.ENDER_SPELL_POWER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    BLOOD_SPELL_POWER("blood_power", (Holder<Item>)ItemRegistry.BLOOD_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.BLOOD_SPELL_POWER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    EVOCATION_SPELL_POWER("evocation_power", (Holder<Item>)ItemRegistry.EVOCATION_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.EVOCATION_SPELL_POWER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    NATURE_SPELL_POWER("nature_power", (Holder<Item>)ItemRegistry.NATURE_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.NATURE_SPELL_POWER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    COOLDOWN("cooldown", (Holder<Item>)ItemRegistry.COOLDOWN_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.COOLDOWN_REDUCTION, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    SPELL_RESISTANCE("spell_resistance", (Holder<Item>)ItemRegistry.PROTECTION_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.SPELL_RESIST, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    MANA("mana", (Holder<Item>)ItemRegistry.MANA_UPGRADE_ORB, (Holder<Attribute>)AttributeRegistry.MAX_MANA, AttributeModifier.Operation.ADD_VALUE, 50.0f),
    ATTACK_DAMAGE("melee_damage", Optional.empty(), (Holder<Attribute>)Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    ATTACK_SPEED("melee_speed", Optional.empty(), (Holder<Attribute>)Attributes.ATTACK_SPEED, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0.05f),
    HEALTH("health", Optional.empty(), (Holder<Attribute>)Attributes.MAX_HEALTH, AttributeModifier.Operation.ADD_VALUE, 2.0f);

    final Holder<Attribute> attribute;
    final AttributeModifier.Operation operation;
    final float amountPerUpgrade;
    final ResourceLocation id;
    final Optional<Holder<Item>> containerItem;

    private UpgradeTypes(String key, Holder<Item> containerItem, Holder<Attribute> attribute, AttributeModifier.Operation operation, float amountPerUpgrade) {
        this(key, Optional.of(containerItem), attribute, operation, amountPerUpgrade);
    }

    private UpgradeTypes(String key, Optional<Holder<Item>> containerItem, Holder<Attribute> attribute, AttributeModifier.Operation operation, float amountPerUpgrade) {
        this.id = IronsSpellbooks.id(key);
        this.attribute = attribute;
        this.operation = operation;
        this.amountPerUpgrade = amountPerUpgrade;
        this.containerItem = containerItem;
        UpgradeType.registerUpgrade(this);
    }

    @Override
    public Holder<Attribute> getAttribute() {
        return this.attribute;
    }

    @Override
    public AttributeModifier.Operation getOperation() {
        return this.operation;
    }

    @Override
    public float getAmountPerUpgrade() {
        return this.amountPerUpgrade;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public Optional<Holder<Item>> getContainerItem() {
        return this.containerItem;
    }
}

