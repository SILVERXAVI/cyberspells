/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.api.item.IScroll;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Scroll
extends Item
implements IScroll {
    public Scroll() {
        super(new Item.Properties().rarity(Rarity.UNCOMMON));
    }

    @NotNull
    private SpellData getSpellSlotFromStack(ItemStack itemStack) {
        return ISpellContainer.getOrCreate(itemStack).getSpellAtIndex(0);
    }

    protected void removeScrollAfterCast(ServerPlayer serverPlayer, ItemStack stack) {
        if (!serverPlayer.isCreative()) {
            stack.shrink(1);
        }
    }

    public static void attemptRemoveScrollAfterCast(ServerPlayer serverPlayer) {
        ItemStack potentialScroll = MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getPlayerCastingItem();
        Item item = potentialScroll.getItem();
        if (item instanceof Scroll) {
            Scroll scroll = (Scroll)item;
            scroll.removeScrollAfterCast(serverPlayer, potentialScroll);
        }
    }

    @Nullable
    public String getCreatorModId(ItemStack itemStack) {
        AbstractSpell spell = this.getSpellSlotFromStack(itemStack).getSpell();
        ResourceLocation id = SpellRegistry.REGISTRY.getKey((Object)spell);
        return id == null ? super.getCreatorModId(itemStack) : id.getNamespace();
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        String castingSlot;
        ItemStack stack = player.getItemInHand(hand);
        SpellData spellSlot = this.getSpellSlotFromStack(stack);
        AbstractSpell spell = spellSlot.getSpell();
        if (level.isClientSide) {
            if (ClientMagicData.isCasting()) {
                return InteractionResultHolder.consume((Object)stack);
            }
            if (!ClientMagicData.getSyncedSpellData((LivingEntity)player).isSpellLearned(spell)) {
                return InteractionResultHolder.pass((Object)stack);
            }
            return InteractionResultHolder.consume((Object)stack);
        }
        String string = castingSlot = hand.ordinal() == 0 ? SpellSelectionManager.MAINHAND : SpellSelectionManager.OFFHAND;
        if (spell.attemptInitiateCast(stack, spell.getLevelFor(spellSlot.getLevel(), (LivingEntity)player), level, player, CastSource.SCROLL, false, castingSlot)) {
            return InteractionResultHolder.consume((Object)stack);
        }
        return InteractionResultHolder.fail((Object)stack);
    }

    @NotNull
    public Component getName(@NotNull ItemStack itemStack) {
        return this.getSpellSlotFromStack(itemStack).getDisplayName();
    }

    public void appendHoverText(@NotNull ItemStack itemStack, Item.TooltipContext context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, context, lines, flag);
        MinecraftInstanceHelper.ifPlayerPresent(player -> lines.addAll(TooltipsUtils.formatScrollTooltip(itemStack, player)));
    }
}

