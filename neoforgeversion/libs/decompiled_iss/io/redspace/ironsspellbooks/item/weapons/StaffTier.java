/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 */
package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class StaffTier
implements IronsWeaponTier {
    public static StaffTier GRAYBEARD = new StaffTier(2.0f, -3.0f, new AttributeContainer((Holder<Attribute>)AttributeRegistry.MANA_REGEN, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static StaffTier ARTIFICER = new StaffTier(3.0f, -3.0f, new AttributeContainer((Holder<Attribute>)AttributeRegistry.CAST_TIME_REDUCTION, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.COOLDOWN_REDUCTION, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_POWER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static StaffTier ICE_STAFF = new StaffTier(4.0f, -3.0f, new AttributeContainer((Holder<Attribute>)AttributeRegistry.MANA_REGEN, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.ICE_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static StaffTier LIGHTNING_ROD = new StaffTier(4.0f, -3.0f, new AttributeContainer((Holder<Attribute>)AttributeRegistry.COOLDOWN_REDUCTION, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.LIGHTNING_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static StaffTier BLOOD_STAFF = new StaffTier(7.0f, -3.0f, new AttributeContainer((Holder<Attribute>)AttributeRegistry.BLOOD_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SUMMON_DAMAGE, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static StaffTier PYRIUM_STAFF = new StaffTier(8.0f, -2.5f, new AttributeContainer((Holder<Attribute>)AttributeRegistry.FIRE_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.CAST_TIME_REDUCTION, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    float damage;
    float speed;
    AttributeContainer[] attributes;

    public StaffTier(float damage, float speed, AttributeContainer ... attributes) {
        this.damage = damage;
        this.speed = speed;
        this.attributes = attributes;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public AttributeContainer[] getAdditionalAttributes() {
        return this.attributes;
    }
}

