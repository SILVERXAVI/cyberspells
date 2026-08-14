/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.capabilities.Capabilities$EnergyStorage
 *  net.neoforged.neoforge.capabilities.Capabilities$ItemHandler
 *  net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
 *  net.neoforged.neoforge.energy.IEnergyStorage
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NeoForgeRegistries$Keys
 */
package com.maxwell.cyber_ware_port.common.capability;

import com.maxwell.cyber_ware_port.common.block.cwb.CyberwareWorkbenchBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@EventBusSubscriber(modid="cyber_ware_port", bus=EventBusSubscriber.Bus.MOD)
public class CyberwareCapabilityProvider {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create((ResourceKey)NeoForgeRegistries.Keys.ATTACHMENT_TYPES, (String)"cyber_ware_port");
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<CyberwareUserData>> CYBERWARE_DATA = ATTACHMENT_TYPES.register("cyberware_data", () -> AttachmentType.serializable(CyberwareUserData::new).copyOnDeath().build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(Capabilities.EnergyStorage.ENTITY, EntityType.PLAYER, (player, side) -> (IEnergyStorage)player.getData((AttachmentType)CYBERWARE_DATA.get()));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)ModBlockEntities.SCANNER.get(), (be, side) -> be.getExposedHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)ModBlockEntities.ROBO_SURGEON.get(), (be, side) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)ModBlockEntities.COMPONENT_BOX.get(), (be, side) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)ModBlockEntities.BLUEPRINT_CHEST.get(), (be, side) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType)ModBlockEntities.CYBERWARE_WORKBENCH.get(), CyberwareWorkbenchBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, (BlockEntityType)ModBlockEntities.CHARGER.get(), (be, side) -> be.getEnergyStorage());
    }
}

