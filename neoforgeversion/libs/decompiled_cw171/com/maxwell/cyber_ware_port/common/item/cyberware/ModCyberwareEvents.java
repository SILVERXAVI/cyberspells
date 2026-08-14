/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.AABB
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.event.AddReloadListenerEvent
 *  net.neoforged.neoforge.event.RegisterCommandsEvent
 *  net.neoforged.neoforge.event.entity.EntityTeleportEvent
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Pre
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Tick
 *  net.neoforged.neoforge.event.entity.living.LivingEvent$LivingJumpEvent
 *  net.neoforged.neoforge.event.entity.living.LivingFallEvent
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Applicable
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Applicable$Result
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$HarvestCheck
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 *  net.neoforged.neoforge.items.ItemStackHandler
 */
package com.maxwell.cyber_ware_port.common.item.cyberware;

import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.api.json.CyberwareDataManager;
import com.maxwell.cyber_ware_port.api.json.MobDataManager;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.command.CyberwareCommands;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.mojang.brigadier.CommandDispatcher;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.items.ItemStackHandler;

@EventBusSubscriber(modid="cyber_ware_port", bus=EventBusSubscriber.Bus.GAME)
public class ModCyberwareEvents {
    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
        event.addListener((PreparableReloadListener)new CyberwareDataManager());
        event.addListener((PreparableReloadListener)new MobDataManager());
    }

    private static void dispatch(LivingEntity entity, BiConsumer<ICyberware, ItemStack> action) {
        if (entity == null) {
            return;
        }
        CyberwareUserData data = (CyberwareUserData)entity.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        ItemStackHandler handler = data.getInstalledCyberware();
        for (int i = 0; i < handler.getSlots(); ++i) {
            ItemStack stack = handler.getStackInSlot(i);
            ICyberware cw = CyberwareAPI.getCyberware(stack);
            if (stack.isEmpty() || cw == null || !CyberwareUserData.isItemPowered(data, cw, stack)) continue;
            action.accept(cw, stack);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event == null) {
            return;
        }
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity)entity;
            ModCyberwareEvents.dispatch(attacker, (cw, stack) -> cw.onLivingDamagePre(event, (ItemStack)stack, attacker));
        }
        ModCyberwareEvents.dispatch(event.getEntity(), (cw, stack) -> cw.onLivingDamagePre(event, (ItemStack)stack, event.getEntity()));
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)player;
            CyberwareUserData data = (CyberwareUserData)sp.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            data.tick(sp);
            ModCyberwareEvents.dispatch((LivingEntity)sp, (cw, stack) -> cw.onPlayerTick(event, (ItemStack)stack, (LivingEntity)sp));
        }
    }

    @SubscribeEvent
    public static void onEntityTeleport(EntityTeleportEvent event) {
        double targetX = event.getTargetX();
        double targetY = event.getTargetY();
        double targetZ = event.getTargetZ();
        double range = 16.0;
        AABB searchArea = new AABB(targetX - range, targetY - range, targetZ - range, targetX + range, targetY + range, targetZ + range);
        List players = event.getEntity().level().getEntitiesOfClass(Player.class, searchArea);
        for (Player player : players) {
            ModCyberwareEvents.dispatch((LivingEntity)player, (cw, stack) -> cw.onEntityTeleport(event, (ItemStack)stack, (LivingEntity)player));
            if (!event.isCanceled()) continue;
            return;
        }
    }

    @SubscribeEvent
    public static void onItemUseTick(LivingEntityUseItemEvent.Tick event) {
        ModCyberwareEvents.dispatch(event.getEntity(), (cw, stack) -> cw.onItemUseTick(event, (ItemStack)stack, event.getEntity()));
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            ModCyberwareEvents.dispatch((LivingEntity)player, (cw, stack) -> {
                if (!event.isCanceled()) {
                    cw.onLivingIncomingDamage(event, (ItemStack)stack, (LivingEntity)player);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ModCyberwareEvents.dispatch((LivingEntity)event.getEntity(), (cw, stack) -> cw.onLeftClickBlock(event, (ItemStack)stack, (LivingEntity)event.getEntity()));
    }

    @SubscribeEvent
    public static void onPotionApplicable(MobEffectEvent.Applicable event) {
        ModCyberwareEvents.dispatch(event.getEntity(), (cw, stack) -> {
            if (event.getResult() != MobEffectEvent.Applicable.Result.DO_NOT_APPLY) {
                cw.onPotionApplicable(event, (ItemStack)stack, event.getEntity());
            }
        });
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        ModCyberwareEvents.dispatch(event.getEntity(), (cw, stack) -> {
            if (!event.isCanceled()) {
                cw.onLivingDeath(event, (ItemStack)stack, event.getEntity());
            }
        });
    }

    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        ModCyberwareEvents.dispatch((LivingEntity)event.getEntity(), (cw, stack) -> cw.onHarvestCheck(event, (ItemStack)stack, (LivingEntity)event.getEntity()));
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        ModCyberwareEvents.dispatch((LivingEntity)event.getEntity(), (cw, stack) -> cw.onBreakSpeed(event, (ItemStack)stack, (LivingEntity)event.getEntity()));
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        ModCyberwareEvents.dispatch(event.getEntity(), (cw, stack) -> cw.onLivingFall(event, (ItemStack)stack, event.getEntity()));
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        ModCyberwareEvents.dispatch(event.getEntity(), (cw, stack) -> cw.onLivingJump(event, (ItemStack)stack, event.getEntity()));
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CyberwareCommands.register((CommandDispatcher<CommandSourceStack>)event.getDispatcher());
    }
}

