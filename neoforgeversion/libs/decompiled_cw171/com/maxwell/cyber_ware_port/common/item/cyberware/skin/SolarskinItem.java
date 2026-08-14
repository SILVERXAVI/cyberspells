/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.attachment.AttachmentType
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.skin;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;

public class SolarskinItem
extends CyberwareItem {
    private static final int GENERATION_AMOUNT = 2;

    public SolarskinItem() {
        super(new CyberwareItem.Builder(4, RobosurgeonBlockEntity.SLOT_SKIN).maxInstall(4).energy(0, 2, 0, ICyberware.StackingRule.LINEAR));
    }

    @Override
    public void onSystemTick(LivingEntity wearer, ItemStack stack) {
        int unitGeneration;
        int totalGeneration;
        boolean isDaytime;
        Level level = wearer.level();
        long time = level.getDayTime() % 24000L;
        boolean bl = isDaytime = time < 12500L || time > 23500L;
        if (level.dimensionType().hasSkyLight() && isDaytime && !level.isRaining() && level.canSeeSky(wearer.blockPosition()) && (totalGeneration = (unitGeneration = this.getEnergyGeneration(stack)) * stack.getCount()) > 0) {
            CyberwareUserData data = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            data.receiveEnergy(totalGeneration, false);
        }
    }
}

