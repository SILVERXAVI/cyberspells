/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.item.consumables;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.CooldownInstance;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.item.consumables.DrinkableItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CastersTea
extends DrinkableItem {
    public CastersTea(Item.Properties pProperties) {
        super(pProperties, CastersTea::onConsume, null, true);
    }

    private static void onConsume(ItemStack itemStack, LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            PlayerCooldowns cooldowns = MagicData.getPlayerMagicData(livingEntity).getPlayerCooldowns();
            cooldowns.getSpellCooldowns().forEach((key, value) -> cooldowns.decrementCooldown((CooldownInstance)value, (int)((float)value.getSpellCooldown() * 0.15f)));
            cooldowns.syncToPlayer(serverPlayer);
        }
    }
}

