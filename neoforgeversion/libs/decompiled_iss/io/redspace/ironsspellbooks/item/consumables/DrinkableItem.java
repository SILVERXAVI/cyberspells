/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.advancements.CriteriaTriggers
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ItemUtils
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.UseAnim
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.gameevent.GameEvent
 */
package io.redspace.ironsspellbooks.item.consumables;

import java.util.List;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class DrinkableItem
extends Item {
    private final BiConsumer<ItemStack, LivingEntity> drinkAction;
    private final Item returnItem;
    private final boolean showDesc;

    public DrinkableItem(Item.Properties pProperties, BiConsumer<ItemStack, LivingEntity> drinkAction, @Nullable Item returnItem, boolean showDescription) {
        super(pProperties);
        this.drinkAction = drinkAction;
        this.returnItem = returnItem;
        this.showDesc = showDescription;
    }

    public DrinkableItem(Item.Properties pProperties, BiConsumer<ItemStack, LivingEntity> drinkAction) {
        this(pProperties, drinkAction, null, true);
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        Player player;
        Player player2 = player = pEntityLiving instanceof Player ? (Player)pEntityLiving : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, pStack);
        }
        if (!pLevel.isClientSide) {
            this.drinkAction.accept(pStack, pEntityLiving);
        }
        if (player != null && !player.getAbilities().instabuild) {
            pStack.shrink(1);
        }
        if (!(this.returnItem == null || player != null && player.getAbilities().instabuild)) {
            if (pStack.isEmpty()) {
                return new ItemStack((ItemLike)this.returnItem);
            }
            if (player != null) {
                player.getInventory().add(new ItemStack((ItemLike)this.returnItem));
            }
        }
        pEntityLiving.gameEvent((Holder)GameEvent.DRINK);
        return pStack;
    }

    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly((Level)pLevel, (Player)pPlayer, (InteractionHand)pHand);
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
        if (this.showDesc) {
            pTooltipComponents.add((Component)Component.empty());
            pTooltipComponents.add((Component)Component.translatable((String)"potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
            pTooltipComponents.add((Component)Component.translatable((String)(this.getDescriptionId() + ".desc")).withStyle(ChatFormatting.BLUE));
        }
    }
}

