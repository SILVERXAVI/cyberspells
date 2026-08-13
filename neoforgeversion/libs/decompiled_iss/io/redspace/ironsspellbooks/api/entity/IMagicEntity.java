/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.api.entity;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

public interface IMagicEntity {
    public MagicData getMagicData();

    public void setSyncedSpellData(SyncedSpellData var1);

    public boolean isCasting();

    public void initiateCastSpell(AbstractSpell var1, int var2);

    public void cancelCast();

    public void castComplete();

    public void notifyDangerousProjectile(Projectile var1);

    public boolean setTeleportLocationBehindTarget(int var1);

    public void setBurningDashDirectionData();

    @Deprecated(forRemoval=true)
    public ItemStack getItemBySlot(EquipmentSlot var1);

    public boolean isDrinkingPotion();

    public boolean getHasUsedSingleAttack();

    public void setHasUsedSingleAttack(boolean var1);

    public void startDrinkingPotion();
}

