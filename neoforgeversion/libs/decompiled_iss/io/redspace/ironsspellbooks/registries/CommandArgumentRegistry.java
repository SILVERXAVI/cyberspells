/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.synchronization.ArgumentTypeInfo
 *  net.minecraft.commands.synchronization.ArgumentTypeInfos
 *  net.minecraft.commands.synchronization.SingletonArgumentInfo
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.command.SpellArgument;
import java.util.function.Supplier;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CommandArgumentRegistry {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create((ResourceKey)Registries.COMMAND_ARGUMENT_TYPE, (String)"irons_spellbooks");
    private static final Supplier<SingletonArgumentInfo<SpellArgument>> SPELL_COMMAND_ARGUMENT_TYPE = ARGUMENT_TYPES.register("spell", () -> (SingletonArgumentInfo)ArgumentTypeInfos.registerByClass(SpellArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(SpellArgument::spellArgument)));

    public static void register(IEventBus modEventBus) {
        ARGUMENT_TYPES.register(modEventBus);
    }
}

