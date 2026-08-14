/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.cranium;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class CorticalStackItem
extends CyberwareItem {
    public CorticalStackItem() {
        super(new CyberwareItem.Builder(10, RobosurgeonBlockEntity.SLOT_BRAIN).maxInstall(1).incompatible(new Supplier[]{ModItems.CONSCIOUSNESS_TRANSMITTER}));
    }

    @Override
    public void onLivingDeath(LivingDeathEvent event, ItemStack stack, LivingEntity wearer) {
        if (wearer instanceof Player) {
            int totalXp;
            Player player = (Player)wearer;
            if (!player.level().isClientSide && (totalXp = this.getTotalXp(player)) > 0) {
                ItemStack capsule = new ItemStack((ItemLike)ModItems.EXP_CAPSULE.get());
                CompoundTag tag = new CompoundTag();
                tag.putInt("xp", totalXp);
                capsule.set(DataComponents.CUSTOM_DATA, (Object)CustomData.of((CompoundTag)tag));
                player.drop(capsule, true);
                player.totalExperience = 0;
                player.experienceLevel = 0;
                player.experienceProgress = 0.0f;
            }
        }
    }

    private int getTotalXp(Player player) {
        return (int)((float)(player.experienceLevel * 7) + player.experienceProgress * (float)player.getXpNeededForNextLevel());
    }
}

