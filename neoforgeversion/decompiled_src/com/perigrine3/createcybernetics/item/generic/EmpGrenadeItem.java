/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomModelData
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.Level
 */
package com.perigrine3.createcybernetics.item.generic;

import com.perigrine3.createcybernetics.entity.projectile.EmpGrenadeProjectile;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class EmpGrenadeItem
extends Item {
    public EmpGrenadeItem(Item.Properties props) {
        super(props);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return this.throwGrenade(level, player, hand);
    }

    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        InteractionResultHolder<ItemStack> res = this.throwGrenade(context.getLevel(), player, context.getHand());
        return res.getResult();
    }

    private InteractionResultHolder<ItemStack> throwGrenade(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.6f, 0.9f + level.getRandom().nextFloat() * 0.2f);
        if (!level.isClientSide) {
            ItemStack projStack = held.copyWithCount(1);
            projStack.set(DataComponents.CUSTOM_MODEL_DATA, (Object)new CustomModelData(1));
            EmpGrenadeProjectile grenade = new EmpGrenadeProjectile(level, (LivingEntity)player);
            grenade.setItem(projStack);
            grenade.shootFromRotation((Entity)player, player.getXRot(), player.getYRot(), 0.0f, 1.25f, 0.85f);
            level.addFreshEntity((Entity)grenade);
        }
        if (!player.getAbilities().instabuild) {
            held.shrink(1);
        }
        player.getCooldowns().addCooldown((Item)this, 10);
        return InteractionResultHolder.sidedSuccess((Object)held, (boolean)level.isClientSide);
    }
}

