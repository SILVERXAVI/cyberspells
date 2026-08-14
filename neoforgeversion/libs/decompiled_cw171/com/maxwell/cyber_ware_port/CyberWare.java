/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  net.minecraft.core.component.DataComponentType
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.fml.ModContainer
 *  net.neoforged.fml.common.Mod
 *  net.neoforged.fml.config.IConfigSpec
 *  net.neoforged.fml.config.ModConfig$Type
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.slf4j.Logger
 */
package com.maxwell.cyber_ware_port;

import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.network.A_PacketHandler;
import com.maxwell.cyber_ware_port.config.CyberwareConfig;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import com.maxwell.cyber_ware_port.init.ModBlocks;
import com.maxwell.cyber_ware_port.init.ModDataComponents;
import com.maxwell.cyber_ware_port.init.ModEntities;
import com.maxwell.cyber_ware_port.init.ModItems;
import com.maxwell.cyber_ware_port.init.ModMenuTypes;
import com.maxwell.cyber_ware_port.init.ModRecipes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;

@Mod(value="cyber_ware_port")
public class CyberWare {
    public static final String MODID = "cyber_ware_port";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> GHOST_COMPONENT = ModDataComponents.GHOST_COMPONENT;
    public static final Logger LOGGER = LogUtils.getLogger();

    public CyberWare(IEventBus modEventBus, ModContainer modContainer) {
        ModDataComponents.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModEntities.register(modEventBus);
        CyberwareCapabilityProvider.register(modEventBus);
        A_PacketHandler.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, (IConfigSpec)CyberwareConfig.COMMON_CONFIG);
    }
}

