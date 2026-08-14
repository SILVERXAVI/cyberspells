/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.lower_organs;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;

public class MetabolicGeneratorItem
extends CyberwareItem {
    public MetabolicGeneratorItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_STOMACH).maxInstall(2).energy(0, 200, 200, ICyberware.StackingRule.LINEAR));
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
        if (!(wearer instanceof Player)) {
            return;
        }
        Player player = (Player)wearer;
        if (player.tickCount % 20 != 0) {
            return;
        }
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (data.getEnergyStored() < data.getMaxEnergyStored()) {
            int genAmount = 200;
            if (player.getFoodData().getFoodLevel() > 6) {
                if (player.getFoodData().getSaturationLevel() > 0.0f) {
                    if (data.receiveEnergy(genAmount, true) == genAmount) {
                        player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() - 1.0f);
                        data.receiveEnergy(genAmount, false);
                    }
                } else if (data.receiveEnergy(genAmount, true) == genAmount) {
                    player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
                    data.receiveEnergy(genAmount, false);
                }
            }
        }
    }
}

