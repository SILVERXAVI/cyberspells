/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
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
import com.perigrine3.createcybernetics.effect.ModEffects;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class EnergyController {
    private static final Map<Class<?>, Boolean> OVERRIDES_SHOULD_GENERATE = new ConcurrentHashMap();

    private EnergyController() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        if (EnergyController.hasEmpEffect(player)) {
            data.setEnergyStored(player, 0);
            for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
                CyberwareSlot slot = entry.getKey();
                InstalledCyberware[] installedCyberwareArray = entry.getValue();
                if (installedCyberwareArray == null) continue;
                for (int idx = 0; idx < installedCyberwareArray.length; ++idx) {
                    ICyberwareItem item;
                    String paidKey;
                    InstalledCyberware cw = installedCyberwareArray[idx];
                    if (cw == null) continue;
                    ItemStack itemStack = cw.getItem();
                    if (itemStack == null || itemStack.isEmpty()) {
                        cw.setPowered(false);
                        continue;
                    }
                    Item item2 = itemStack.getItem();
                    if (item2 instanceof ICyberwareItem && (paidKey = (item = (ICyberwareItem)item2).getActivationPaidNbtKey(player, itemStack, slot)) != null && !paidKey.isBlank()) {
                        String persistentKey = EnergyController.buildActivationPersistentKey(paidKey, slot, idx);
                        player.getPersistentData().remove(persistentKey);
                    }
                    cw.setPowered(false);
                }
            }
            return;
        }
        data.clampEnergyToCapacity(player);
        boolean onCharger = EnergyController.isOnChargerBlock(player);
        int chargerCharge = 0;
        if (onCharger) {
            for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
                CyberwareSlot slot = entry.getKey();
                InstalledCyberware[] arr = entry.getValue();
                if (arr == null) continue;
                for (int i = 0; i < arr.length; ++i) {
                    int req;
                    ICyberwareItem item;
                    Item item3;
                    ItemStack stack;
                    InstalledCyberware cw = arr[i];
                    if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !((item3 = stack.getItem()) instanceof ICyberwareItem) || !(item = (ICyberwareItem)item3).acceptsChargerEnergy(player, stack, slot) || (req = item.getChargerEnergyReceivePerTick(player, stack, slot)) <= 0) continue;
                    chargerCharge += req;
                }
            }
        }
        int tickGenerated = 0;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] installedCyberwareArray = entry.getValue();
            if (installedCyberwareArray == null) continue;
            for (int idx = 0; idx < installedCyberwareArray.length; ++idx) {
                ICyberwareItem item;
                int gen;
                Item item4;
                ItemStack stack;
                InstalledCyberware cw = installedCyberwareArray[idx];
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !((item4 = stack.getItem()) instanceof ICyberwareItem) || (gen = (item = (ICyberwareItem)item4).getEnergyGeneratedPerTick(player, stack, slot)) <= 0 || !EnergyController.shouldGenerate(item, player, stack, slot)) continue;
                tickGenerated += gen;
            }
        }
        MutableInt mutableInt = new MutableInt(tickGenerated);
        MutableInt mainsPool = new MutableInt(onCharger ? Integer.MAX_VALUE : 0);
        for (Map.Entry entry : data.getAll().entrySet()) {
            CyberwareSlot slot = (CyberwareSlot)((Object)entry.getKey());
            InstalledCyberware[] arr = (InstalledCyberware[])entry.getValue();
            if (arr == null) continue;
            for (int idx = 0; idx < arr.length; ++idx) {
                Item item;
                ItemStack stack;
                InstalledCyberware cw = arr[idx];
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !((item = stack.getItem()) instanceof ICyberwareItem)) continue;
                ICyberwareItem item5 = (ICyberwareItem)item;
                boolean powered = true;
                int use = item5.getEnergyUsedPerTick(player, stack, slot);
                if (use > 0) {
                    powered = EnergyController.tryPayEnergy(data, mainsPool, mutableInt, use);
                }
                if (powered && item5.shouldConsumeActivationEnergyThisTick(player, stack, slot)) {
                    int actCost = item5.getEnergyActivationCost(player, stack, slot);
                    if (actCost > 0) {
                        String paidKey = item5.getActivationPaidNbtKey(player, stack, slot);
                        if (paidKey == null || paidKey.isBlank()) {
                            powered = EnergyController.tryPayEnergy(data, mainsPool, mutableInt, actCost);
                        } else {
                            String persistentKey = EnergyController.buildActivationPersistentKey(paidKey, slot, idx);
                            CompoundTag ptag = player.getPersistentData();
                            boolean alreadyPaid = ptag.getBoolean(persistentKey);
                            if (!alreadyPaid) {
                                if (EnergyController.tryPayEnergy(data, mainsPool, mutableInt, actCost)) {
                                    ptag.putBoolean(persistentKey, true);
                                } else {
                                    powered = false;
                                }
                            }
                        }
                    }
                } else {
                    String paidKey = item5.getActivationPaidNbtKey(player, stack, slot);
                    if (paidKey != null && !paidKey.isBlank()) {
                        String persistentKey = EnergyController.buildActivationPersistentKey(paidKey, slot, idx);
                        player.getPersistentData().remove(persistentKey);
                    }
                }
                cw.setPowered(powered);
            }
        }
        int genLeftover = mutableInt.value;
        if (genLeftover > 0 && EnergyController.canAcceptGeneratedSurplus(player, data)) {
            data.receiveEnergy(player, genLeftover);
        }
        if (onCharger && chargerCharge > 0) {
            data.receiveEnergy(player, chargerCharge);
        }
        data.clampEnergyToCapacity(player);
    }

    private static boolean tryPayEnergy(PlayerCyberwareData data, MutableInt mainsPool, MutableInt genPool, int amount) {
        if (amount <= 0) {
            return true;
        }
        int fromMains = Math.min(mainsPool.value, amount);
        mainsPool.value -= fromMains;
        if ((amount -= fromMains) <= 0) {
            return true;
        }
        int fromGen = Math.min(genPool.value, amount);
        genPool.value -= fromGen;
        if ((amount -= fromGen) <= 0) {
            return true;
        }
        return data.tryConsumeEnergy(amount);
    }

    private static boolean canAcceptGeneratedSurplus(Player player, PlayerCyberwareData data) {
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int idx = 0; idx < arr.length; ++idx) {
                ICyberwareItem item;
                Item item2;
                ItemStack stack;
                InstalledCyberware cw = arr[idx];
                if (cw == null || (stack = cw.getItem()) == null || stack.isEmpty() || !((item2 = stack.getItem()) instanceof ICyberwareItem) || !(item = (ICyberwareItem)item2).acceptsGeneratedEnergy(player, stack, slot)) continue;
                return true;
            }
        }
        return false;
    }

    private static boolean shouldGenerate(ICyberwareItem item, Player player, ItemStack stack, CyberwareSlot slot) {
        if (!EnergyController.overridesShouldGenerate(item.getClass())) {
            return true;
        }
        return item.shouldGenerateEnergyThisTick(player, stack, slot);
    }

    private static boolean overridesShouldGenerate(Class<?> cls) {
        return OVERRIDES_SHOULD_GENERATE.computeIfAbsent(cls, c -> {
            try {
                Method m = c.getMethod("shouldGenerateEnergyThisTick", Player.class, ItemStack.class, CyberwareSlot.class);
                return m.getDeclaringClass() != ICyberwareItem.class;
            }
            catch (NoSuchMethodException e) {
                return false;
            }
        });
    }

    private static String buildActivationPersistentKey(String key, CyberwareSlot slot, int index) {
        return "cc_energy_actpaid_" + key + "_" + slot.name() + "_" + index;
    }

    private static boolean isOnChargerBlock(Player player) {
        Level level = player.level();
        BlockPos below = player.blockPosition().below();
        return level.getBlockState(below).is((Block)ModBlocks.CHARGING_BLOCK.get());
    }

    private static boolean hasEmpEffect(Player player) {
        return player.hasEffect(ModEffects.EMP);
    }

    private static final class MutableInt {
        int value;

        MutableInt(int value) {
            this.value = value;
        }
    }
}

