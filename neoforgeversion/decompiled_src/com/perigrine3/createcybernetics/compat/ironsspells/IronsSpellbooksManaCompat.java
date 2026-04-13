/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.redspace.ironsspellbooks.api.magic.MagicData
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.fml.ModList
 */
package com.perigrine3.createcybernetics.compat.ironsspells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

public final class IronsSpellbooksManaCompat {
    public static final String MODID = "irons_spellbooks";

    private IronsSpellbooksManaCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MODID);
    }

    public static boolean drainMana(LivingEntity target, float amount) {
        float oldMana;
        float newMana;
        if (!IronsSpellbooksManaCompat.isLoaded()) {
            return false;
        }
        if (target == null || amount <= 0.0f) {
            return false;
        }
        MagicData data = MagicData.getPlayerMagicData((LivingEntity)target);
        if (data == null) {
            return false;
        }
        if (target instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)target;
            data.setServerPlayer(sp);
        }
        if ((newMana = Math.max(0.0f, (oldMana = data.getMana()) - amount)) == oldMana) {
            return false;
        }
        data.setMana(newMana);
        return true;
    }
}

