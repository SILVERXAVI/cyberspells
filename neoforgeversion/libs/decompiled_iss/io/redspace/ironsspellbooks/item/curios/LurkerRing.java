/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 */
package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.PassiveAbilityCurio;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class LurkerRing
extends PassiveAbilityCurio {
    public static final int COOLDOWN_IN_TICKS = 300;
    public static final float MULTIPLIER = 1.5f;

    public LurkerRing() {
        super(new Item.Properties().stacksTo(1), Curios.RING_SLOT);
    }

    @Override
    public Component getDescription(ItemStack stack) {
        return Component.literal((String)" ").append((Component)Component.translatable((String)(this.getDescriptionId() + ".desc"), (Object[])new Object[]{50})).withStyle(this.descriptionStyle);
    }

    @Override
    protected int getCooldownTicks() {
        return 300;
    }

    @SubscribeEvent
    public static void handleAbility(LivingIncomingDamageEvent event) {
        ServerPlayer attackingPlayer;
        LurkerRing RING = (LurkerRing)ItemRegistry.LURKER_RING.get();
        Entity entity = event.getSource().getEntity();
        if (entity instanceof ServerPlayer && (attackingPlayer = (ServerPlayer)entity).isInvisible() && RING.isEquippedBy((LivingEntity)attackingPlayer) && RING.tryProcCooldown((Player)attackingPlayer)) {
            event.setAmount(event.getAmount() * 1.5f);
        }
    }
}

