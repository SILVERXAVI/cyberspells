/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.neoforge.event.tick.LevelTickEvent$Pre
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.capabilities.magic.PocketDimensionManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class MagicEvents {
    public static final ResourceLocation PLAYER_MAGIC_RESOURCE = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"player_magic");

    public static void onWorldTick(LevelTickEvent.Pre event) {
        if (event.getLevel().isClientSide) {
            return;
        }
        IronsSpellbooks.MAGIC_MANAGER.tick(event.getLevel());
        PocketDimensionManager.INSTANCE.tick(event.getLevel());
    }
}

