/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.fml.ModList
 */
package com.perigrine3.createcybernetics.compat.curios;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

public final class CuriosCompat {
    private static final String CURIOS_MODID = "curios";
    private static boolean attemptedInit = false;
    private static boolean available = false;
    private static Method curiosApi_getCuriosInventory;
    private static Method curiosHandler_getCurios;
    private static Method curiosHandler_getStacksHandler;
    private static Method optional_isPresent;
    private static Method optional_get;
    private static Method itemHandler_getSlots;
    private static Method itemHandler_getStackInSlot;
    private static Method stacksHandler_getStacks;
    private static Method neoItemHandler_getSlots;
    private static Method neoItemHandler_getStackInSlot;

    private CuriosCompat() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(CURIOS_MODID);
    }

    public static Map<String, List<ItemStack>> getAllCuriosBySlot(LivingEntity entity) {
        if (entity == null) {
            return Map.of();
        }
        if (!CuriosCompat.isLoaded()) {
            return Map.of();
        }
        if (!CuriosCompat.initReflection()) {
            return Map.of();
        }
        Object curiosInv = CuriosCompat.getCuriosInventory(entity);
        if (curiosInv == null) {
            return Map.of();
        }
        Object curiosMapObj = CuriosCompat.invoke(curiosHandler_getCurios, curiosInv, new Object[0]);
        if (!(curiosMapObj instanceof Map)) {
            return Map.of();
        }
        Map curiosMap = (Map)curiosMapObj;
        LinkedHashMap<String, List<ItemStack>> out = new LinkedHashMap<String, List<ItemStack>>();
        for (Map.Entry e : curiosMap.entrySet()) {
            Object key = e.getKey();
            Object slotInv = e.getValue();
            if (!(key instanceof String)) continue;
            String slotId = (String)key;
            if (slotInv == null) continue;
            List<ItemStack> stacks = CuriosCompat.readAllStacks(slotInv);
            out.put(slotId, stacks);
        }
        return out;
    }

    public static List<ItemStack> getCuriosInSlot(LivingEntity entity, String slotId) {
        if (entity == null || slotId == null || slotId.isEmpty()) {
            return List.of();
        }
        if (!CuriosCompat.isLoaded()) {
            return List.of();
        }
        if (!CuriosCompat.initReflection()) {
            return List.of();
        }
        Object curiosInv = CuriosCompat.getCuriosInventory(entity);
        if (curiosInv == null) {
            return List.of();
        }
        Object opt = CuriosCompat.invoke(curiosHandler_getStacksHandler, curiosInv, slotId);
        Object slotInv = CuriosCompat.unwrapOptional(opt);
        if (slotInv == null) {
            return List.of();
        }
        return CuriosCompat.readAllStacks(slotInv);
    }

    public static boolean hasCurio(LivingEntity entity, String slotId, Item item) {
        if (item == null) {
            return false;
        }
        return CuriosCompat.findFirstCurio(entity, slotId, st -> st.is(item)).isPresent();
    }

    public static Optional<ItemStack> findFirstCurio(LivingEntity entity, String slotId, Predicate<ItemStack> predicate) {
        if (predicate == null) {
            return Optional.empty();
        }
        for (ItemStack st : CuriosCompat.getCuriosInSlot(entity, slotId)) {
            if (st.isEmpty() || !predicate.test(st)) continue;
            return Optional.of(st);
        }
        return Optional.empty();
    }

    public static Optional<FoundCurio> findFirstCurioAnywhere(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (entity == null || predicate == null) {
            return Optional.empty();
        }
        Map<String, List<ItemStack>> all = CuriosCompat.getAllCuriosBySlot(entity);
        for (Map.Entry<String, List<ItemStack>> entry : all.entrySet()) {
            String slotId = entry.getKey();
            for (ItemStack st : entry.getValue()) {
                if (st.isEmpty() || !predicate.test(st)) continue;
                return Optional.of(new FoundCurio(slotId, st));
            }
        }
        return Optional.empty();
    }

    private static boolean initReflection() {
        if (attemptedInit) {
            return available;
        }
        attemptedInit = true;
        try {
            Class<?> stacksHandlerClass;
            Class<?> curiosApi = Class.forName("top.theillusivec4.curios.api.CuriosApi");
            curiosApi_getCuriosInventory = curiosApi.getMethod("getCuriosInventory", LivingEntity.class);
            optional_isPresent = Optional.class.getMethod("isPresent", new Class[0]);
            optional_get = Optional.class.getMethod("get", new Class[0]);
            Class<?> iCuriosItemHandler = Class.forName("top.theillusivec4.curios.api.type.capability.ICuriosItemHandler");
            curiosHandler_getCurios = iCuriosItemHandler.getMethod("getCurios", new Class[0]);
            curiosHandler_getStacksHandler = iCuriosItemHandler.getMethod("getStacksHandler", String.class);
            try {
                stacksHandlerClass = Class.forName("top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler");
            }
            catch (Throwable ignored) {
                stacksHandlerClass = Class.forName("top.theillusivec4.curios.api.type.inventory.ICurioStackHandler");
            }
            try {
                itemHandler_getSlots = stacksHandlerClass.getMethod("getSlots", new Class[0]);
                itemHandler_getStackInSlot = stacksHandlerClass.getMethod("getStackInSlot", Integer.TYPE);
            }
            catch (Throwable ignored) {
                itemHandler_getSlots = null;
                itemHandler_getStackInSlot = null;
            }
            try {
                stacksHandler_getStacks = stacksHandlerClass.getMethod("getStacks", new Class[0]);
            }
            catch (Throwable ignored) {
                stacksHandler_getStacks = null;
            }
            try {
                Class<?> neoIItemHandler = Class.forName("net.neoforged.neoforge.items.IItemHandler");
                neoItemHandler_getSlots = neoIItemHandler.getMethod("getSlots", new Class[0]);
                neoItemHandler_getStackInSlot = neoIItemHandler.getMethod("getStackInSlot", Integer.TYPE);
            }
            catch (Throwable ignored) {
                neoItemHandler_getSlots = null;
                neoItemHandler_getStackInSlot = null;
            }
            available = true;
            return true;
        }
        catch (Throwable t) {
            available = false;
            return false;
        }
    }

    private static Object getCuriosInventory(LivingEntity entity) {
        Object opt = CuriosCompat.invokeStatic(curiosApi_getCuriosInventory, entity);
        return CuriosCompat.unwrapOptional(opt);
    }

    private static Object unwrapOptional(Object opt) {
        if (!(opt instanceof Optional)) {
            return null;
        }
        Optional o = (Optional)opt;
        return o.orElse(null);
    }

    private static List<ItemStack> readAllStacks(Object slotInv) {
        Object slotsObj;
        Object stacksObj;
        Object slotsObj2;
        if (slotInv == null) {
            return List.of();
        }
        if (itemHandler_getSlots != null && itemHandler_getStackInSlot != null && (slotsObj2 = CuriosCompat.invoke(itemHandler_getSlots, slotInv, new Object[0])) instanceof Integer) {
            Integer slots = (Integer)slotsObj2;
            ArrayList<ItemStack> out = new ArrayList<ItemStack>();
            for (int i = 0; i < slots; ++i) {
                ItemStack st;
                Object stObj = CuriosCompat.invoke(itemHandler_getStackInSlot, slotInv, i);
                if (!(stObj instanceof ItemStack) || (st = (ItemStack)stObj).isEmpty()) continue;
                out.add(st);
            }
            return out;
        }
        if (stacksHandler_getStacks != null && neoItemHandler_getSlots != null && neoItemHandler_getStackInSlot != null && (stacksObj = CuriosCompat.invoke(stacksHandler_getStacks, slotInv, new Object[0])) != null && (slotsObj = CuriosCompat.invoke(neoItemHandler_getSlots, stacksObj, new Object[0])) instanceof Integer) {
            Integer slots = (Integer)slotsObj;
            ArrayList<ItemStack> out = new ArrayList<ItemStack>();
            for (int i = 0; i < slots; ++i) {
                ItemStack st;
                Object stObj = CuriosCompat.invoke(neoItemHandler_getStackInSlot, stacksObj, i);
                if (!(stObj instanceof ItemStack) || (st = (ItemStack)stObj).isEmpty()) continue;
                out.add(st);
            }
            return out;
        }
        return List.of();
    }

    private static Object invokeStatic(Method m, Object ... args) {
        if (m == null) {
            return null;
        }
        try {
            return m.invoke(null, args);
        }
        catch (Throwable t) {
            return null;
        }
    }

    private static Object invoke(Method m, Object target, Object ... args) {
        if (m == null || target == null) {
            return null;
        }
        try {
            return m.invoke(target, args);
        }
        catch (Throwable t) {
            return null;
        }
    }

    public record FoundCurio(String slotId, ItemStack stack) {
    }
}

