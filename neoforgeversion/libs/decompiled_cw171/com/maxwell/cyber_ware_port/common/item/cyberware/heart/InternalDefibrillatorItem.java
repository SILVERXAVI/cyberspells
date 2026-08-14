/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.heart;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.config.CyberwareConfig;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.items.ItemStackHandler;

public class InternalDefibrillatorItem
extends CyberwareItem {
    public InternalDefibrillatorItem() {
        super(new CyberwareItem.Builder(10, RobosurgeonBlockEntity.SLOT_HEART).maxInstall(1).requires(new Supplier[]{ModItems.CARDIOMECHANIC_PUMP}).energy(0, 0, 0, ICyberware.StackingRule.STATIC).eventCost(500));
    }

    @Override
    public void onLivingDeath(LivingDeathEvent event, ItemStack stack, LivingEntity wearer) {
        if (wearer.level().isClientSide()) {
            return;
        }
        CyberwareUserData data = (CyberwareUserData)wearer.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (this.tryConsumeEventEnergy(data, stack)) {
            event.setCanceled(true);
            wearer.setHealth(wearer.getMaxHealth() * 0.5f);
            wearer.level().playSound(null, wearer.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
            if (((Boolean)CyberwareConfig.CONSUME_DEFIBRILLATOR_ON_USE.get()).booleanValue()) {
                ItemStackHandler handler = data.getInstalledCyberware();
                for (int i = 0; i < handler.getSlots(); ++i) {
                    ItemStack stackInSlot = handler.getStackInSlot(i);
                    if (stackInSlot.isEmpty() || !stackInSlot.is((Item)this)) continue;
                    handler.setStackInSlot(i, ItemStack.EMPTY);
                    break;
                }
            }
            if (wearer instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)wearer;
                data.recalculateCapacity(serverPlayer);
                data.syncToClient(serverPlayer);
            }
        }
    }
}

