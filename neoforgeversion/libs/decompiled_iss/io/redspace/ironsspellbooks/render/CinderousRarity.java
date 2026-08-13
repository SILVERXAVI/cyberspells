/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Rarity
 *  net.neoforged.fml.common.asm.enumextension.EnumProxy
 */
package io.redspace.ironsspellbooks.render;

import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class CinderousRarity {
    public static final EnumProxy<Rarity> CINDEROUS_RARITY_PROXY = new EnumProxy(Rarity.class, new Object[]{-1, "irons_spellbooks:cinderous", style -> style.withColor(15881518)});
}

