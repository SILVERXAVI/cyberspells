/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.stats.Stats
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow$Pickup
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.UseAnim
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentEffectComponents
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.entity.spells.thrown_spear.ThrownSpear;
import io.redspace.ironsspellbooks.item.weapons.ExtendedWeaponTier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class TwilightGaleItem
extends MagicSwordItem {
    public TwilightGaleItem(Tier pTier, Item.Properties pProperties, SpellDataRegistryHolder[] spellDataRegistryHolders) {
        super(pTier, pProperties, spellDataRegistryHolders);
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (TwilightGaleItem.isTooDamagedToUse(itemstack)) {
            return InteractionResultHolder.fail((Object)itemstack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume((Object)itemstack);
    }

    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.supportsEnchantment(stack, enchantment) || enchantment.is(Enchantments.LOYALTY) || enchantment.is(Enchantments.CHANNELING);
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player) {
            Player player = (Player)entityLiving;
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (i >= 8 && !TwilightGaleItem.isTooDamagedToUse(stack)) {
                Holder holder = EnchantmentHelper.pickHighestLevel((ItemStack)stack, (DataComponentType)EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
                if (!level.isClientSide) {
                    stack.hurtAndBreak(1, (LivingEntity)player, LivingEntity.getSlotForHand((InteractionHand)entityLiving.getUsedItemHand()));
                    double damage = 1.0f + ExtendedWeaponTier.TWILIGHT_GALE.damage;
                    if (stack.equals(player.getWeaponItem())) {
                        damage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    }
                    ThrownSpear throwntrident = new ThrownSpear(level, stack, damage);
                    throwntrident.setOwner((Entity)player);
                    throwntrident.moveTo(player.getEyePosition());
                    throwntrident.shootFromRotation((Entity)player, player.getXRot(), player.getYRot(), 0.0f, 2.5f, 0.5f);
                    if (player.hasInfiniteMaterials()) {
                        throwntrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }
                    level.addFreshEntity((Entity)throwntrident);
                    if (!player.hasInfiniteMaterials()) {
                        player.getCooldowns().addCooldown(stack.getItem(), 200);
                    }
                    level.playSound(null, (Entity)throwntrident, (SoundEvent)holder.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
                }
                player.awardStat(Stats.ITEM_USED.get((Object)this));
            }
        }
    }

    private static boolean isTooDamagedToUse(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }
}

