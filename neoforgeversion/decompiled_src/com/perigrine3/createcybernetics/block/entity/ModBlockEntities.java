/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.entity.BlockEntityType$Builder
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.perigrine3.createcybernetics.block.entity;

import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.block.entity.EngineeringTableBlockEntity;
import com.perigrine3.createcybernetics.block.entity.HoloprojectorBlockEntity;
import com.perigrine3.createcybernetics.block.entity.RobosurgeonBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create((Registry)BuiltInRegistries.BLOCK_ENTITY_TYPE, (String)"createcybernetics");
    public static final Supplier<BlockEntityType<RobosurgeonBlockEntity>> ROBOSURGEON_BLOCKENTITY = BLOCK_ENTITIES.register("robosurgeon_blockentity", () -> BlockEntityType.Builder.of(RobosurgeonBlockEntity::new, (Block[])new Block[]{(Block)ModBlocks.ROBOSURGEON.get()}).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EngineeringTableBlockEntity>> ENGINEERING_TABLE_BLOCKENTITY = ModBlocks.ENGINEERING_TABLE == null ? null : BLOCK_ENTITIES.register("engineering_table", () -> BlockEntityType.Builder.of(EngineeringTableBlockEntity::new, (Block[])new Block[]{(Block)ModBlocks.ENGINEERING_TABLE.get()}).build(null));
    public static final Supplier<BlockEntityType<HoloprojectorBlockEntity>> HOLOPROJECTOR_BLOCKENTITY = BLOCK_ENTITIES.register("holoprojector_blockentity", () -> BlockEntityType.Builder.of(HoloprojectorBlockEntity::new, (Block[])new Block[]{(Block)ModBlocks.HOLOPROJECTOR.get()}).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

