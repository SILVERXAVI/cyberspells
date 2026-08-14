/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Pre
 */
package com.maxwell.cyber_ware_port.common.item.cyberware.skin;

import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class SubdermalSpikesItem
extends CyberwareItem {
    public SubdermalSpikesItem() {
        super(new CyberwareItem.Builder(5, RobosurgeonBlockEntity.SLOT_SKIN).maxInstall(1));
    }

    @Override
    public void onLivingDamagePre(LivingDamageEvent.Pre event, ItemStack stack, LivingEntity wearer) {
        if (event == null) {
            return;
        }
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity)entity;
            if (event.getSource().getDirectEntity() != attacker) {
                return;
            }
            if (attacker != wearer) {
                float damageAmount = 2.0f;
                attacker.hurt(wearer.damageSources().thorns((Entity)wearer), damageAmount);
            }
        }
    }
}

