/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class PassiveAbilityCurio
extends SimpleDescriptiveCurio {
    public PassiveAbilityCurio(Item.Properties properties, String slotIdentifier) {
        super(properties, slotIdentifier);
        this.descriptionStyle = Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE);
    }

    protected abstract int getCooldownTicks();

    public boolean tryProcCooldown(Player player) {
        if (player.getCooldowns().isOnCooldown((Item)this)) {
            return false;
        }
        player.getCooldowns().addCooldown((Item)this, this.getCooldownTicks((LivingEntity)player));
        return true;
    }

    public int getCooldownTicks(@Nullable LivingEntity livingEntity) {
        return Utils.applyCooldownReduction(this.getCooldownTicks(), livingEntity);
    }

    @Override
    public List<Component> getDescriptionLines(ItemStack stack) {
        return List.of(Component.literal((String)" ").append((Component)Component.translatable((String)"tooltip.irons_spellbooks.passive_ability", (Object[])new Object[]{Component.literal((String)Utils.timeFromTicks(this.getCooldownTicks((LivingEntity)MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.LIGHT_PURPLE)}).withStyle(ChatFormatting.DARK_PURPLE)), this.getDescription(stack));
    }
}

