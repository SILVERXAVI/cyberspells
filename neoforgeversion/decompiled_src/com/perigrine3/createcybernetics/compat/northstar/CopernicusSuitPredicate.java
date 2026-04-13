/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 */
package com.perigrine3.createcybernetics.compat.northstar;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public final class CopernicusSuitPredicate {
    private CopernicusSuitPredicate() {
    }

    public static boolean hasCopernicusSetInstalled(Player player) {
        if (player == null) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LUNGSUPGRADES_OXYGEN.get(), CyberwareSlot.LUNGS, 3) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_SOLARSKIN.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_NETHERITEPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_ZOOM.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_HUDJACK.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_CRAFTHANDS.get(), CyberwareSlot.LARM, CyberwareSlot.RARM);
    }
}

