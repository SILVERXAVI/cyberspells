/*
 * Decompiled with CFR 0.152.
 */
package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;

public interface IronsWeaponTier {
    public float getAttackDamageBonus();

    public float getSpeed();

    public AttributeContainer[] getAdditionalAttributes();
}

