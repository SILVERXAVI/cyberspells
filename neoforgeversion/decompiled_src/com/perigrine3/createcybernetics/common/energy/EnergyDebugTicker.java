/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.common.energy;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class EnergyDebugTicker {
    private static final String PKEY_ENERGY_DEBUG = "cc_energy_debug_enabled";
    private static final Map<Class<?>, Method> ENERGY_GETTER_CACHE = new ConcurrentHashMap();
    private static final Map<Class<?>, Field> ENERGY_FIELD_CACHE = new ConcurrentHashMap();

    private EnergyDebugTicker() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        String remainingStr;
        String inputStr;
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        if (!player.getPersistentData().getBoolean(PKEY_ENERGY_DEBUG)) {
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        int stored = EnergyDebugTicker.readStoredEnergySafe(data);
        int capacity = data.getTotalEnergyCapacity(player);
        boolean onCharger = EnergyDebugTicker.isOnChargerBlock(player);
        int generated = 0;
        int required = 0;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int idx = 0; idx < arr.length; ++idx) {
                int act;
                int use;
                Item item;
                ItemStack stack;
                InstalledCyberware cw = arr[idx];
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !((item = stack.getItem()) instanceof ICyberwareItem)) continue;
                ICyberwareItem item2 = (ICyberwareItem)item;
                int gen = item2.getEnergyGeneratedPerTick(player, stack, slot);
                if (gen > 0) {
                    generated += gen;
                }
                if ((use = item2.getEnergyUsedPerTick(player, stack, slot)) > 0) {
                    required += use;
                }
                if (!item2.shouldConsumeActivationEnergyThisTick(player, stack, slot) || (act = item2.getEnergyActivationCost(player, stack, slot)) <= 0) continue;
                required += act;
            }
        }
        if (onCharger) {
            inputStr = "\u221e";
            remainingStr = "\u221e";
        } else {
            int input = stored + generated;
            int remaining = input - required;
            if (remaining < 0) {
                remaining = 0;
            }
            inputStr = Integer.toString(input);
            remainingStr = Integer.toString(remaining);
        }
        String line = "Capacity: [ " + stored + "/" + capacity + " ] Consumption: [ " + inputStr + "-" + required + "=" + remainingStr + " ]";
        player.displayClientMessage((Component)Component.literal((String)line), true);
    }

    private static boolean isOnChargerBlock(Player player) {
        Level level = player.level();
        BlockPos below = player.blockPosition().below();
        return level.getBlockState(below).is((Block)ModBlocks.CHARGING_BLOCK.get());
    }

    private static int readStoredEnergySafe(PlayerCyberwareData data) {
        try {
            Field f;
            Method m = ENERGY_GETTER_CACHE.computeIfAbsent(data.getClass(), cls -> {
                Method found = EnergyDebugTicker.findNoArgIntGetter(cls, "getEnergyStored", "getStoredEnergy", "getEnergy", "getEnergyAmount");
                if (found != null) {
                    found.setAccessible(true);
                    return found;
                }
                return null;
            });
            if (m != null) {
                Object v = m.invoke((Object)data, new Object[0]);
                if (v instanceof Integer) {
                    Integer i = (Integer)v;
                    return i;
                }
                if (v instanceof Number) {
                    Number n = (Number)v;
                    return n.intValue();
                }
            }
            if ((f = ENERGY_FIELD_CACHE.computeIfAbsent(data.getClass(), cls -> {
                Field ff = EnergyDebugTicker.findIntField(cls, "energyStored", "storedEnergy", "energy", "power", "currentEnergy");
                if (ff != null) {
                    ff.setAccessible(true);
                }
                return ff;
            })) != null) {
                return f.getInt(data);
            }
            return 0;
        }
        catch (Throwable t) {
            return 0;
        }
    }

    private static Method findNoArgIntGetter(Class<?> cls, String ... names) {
        for (String name : names) {
            try {
                Method m = cls.getMethod(name, new Class[0]);
                Class<?> rt = m.getReturnType();
                if (rt != Integer.TYPE && !Number.class.isAssignableFrom(rt)) continue;
                return m;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
        }
        return null;
    }

    private static Field findIntField(Class<?> cls, String ... names) {
        for (String name : names) {
            try {
                Field f = cls.getDeclaredField(name);
                if (f.getType() != Integer.TYPE) continue;
                return f;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
        }
        return null;
    }
}

