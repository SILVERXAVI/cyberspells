/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.projectile.Projectile
 */
package io.redspace.ironsspellbooks.api.entity;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.registries.DataAttachmentRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;

public interface NoopMagicEntity
extends IMagicEntity {
    @Override
    default public MagicData getMagicData() {
        return (MagicData)((Entity)this).getData(DataAttachmentRegistry.MAGIC_DATA);
    }

    @Override
    default public void setSyncedSpellData(SyncedSpellData syncedSpellData) {
    }

    @Override
    default public boolean isCasting() {
        return false;
    }

    @Override
    default public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
    }

    @Override
    default public void cancelCast() {
    }

    @Override
    default public void castComplete() {
    }

    @Override
    default public void notifyDangerousProjectile(Projectile projectile) {
    }

    @Override
    default public boolean setTeleportLocationBehindTarget(int distance) {
        return false;
    }

    @Override
    default public void setBurningDashDirectionData() {
    }

    @Override
    default public boolean isDrinkingPotion() {
        return false;
    }

    @Override
    default public boolean getHasUsedSingleAttack() {
        return false;
    }

    @Override
    default public void setHasUsedSingleAttack(boolean bool) {
    }

    @Override
    default public void startDrinkingPotion() {
    }
}

