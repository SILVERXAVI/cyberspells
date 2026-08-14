/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponentType$Builder
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.DeferredRegister$DataComponents
 */
package com.maxwell.cyber_ware_port.init;

import com.mojang.serialization.Codec;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents((String)"cyber_ware_port");
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> GHOST_COMPONENT = ModDataComponents.register("ghost", builder -> builder.networkSynchronized(ByteBufCodecs.BOOL).persistent((Codec)Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> PRISTINE = ModDataComponents.register("pristine", builder -> builder.networkSynchronized(ByteBufCodecs.BOOL).persistent((Codec)Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ACTIVE = ModDataComponents.register("active", builder -> builder.networkSynchronized(ByteBufCodecs.BOOL).persistent((Codec)Codec.BOOL));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return COMPONENTS.registerComponentType(name, builder);
    }

    public static void register(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }
}

