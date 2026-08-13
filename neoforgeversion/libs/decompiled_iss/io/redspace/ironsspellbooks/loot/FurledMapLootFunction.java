/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  com.mojang.serialization.codecs.RecordCodecBuilder$Instance
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction
 *  net.minecraft.world.level.storage.loot.functions.LootItemFunctionType
 *  net.minecraft.world.level.storage.loot.predicates.LootItemCondition
 */
package io.redspace.ironsspellbooks.loot;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.registries.LootRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class FurledMapLootFunction
extends LootItemConditionalFunction {
    private final String destination;
    private final String translation;
    private final Optional<String> dimension;
    public static final MapCodec<FurledMapLootFunction> CODEC = RecordCodecBuilder.mapCodec(builder -> FurledMapLootFunction.commonFields((RecordCodecBuilder.Instance)builder).and(builder.group((App)Codec.STRING.fieldOf("destination").forGetter(FurledMapLootFunction::getDestination), (App)Codec.STRING.fieldOf("description_translation").forGetter(FurledMapLootFunction::getTranslation), (App)Codec.STRING.optionalFieldOf("dimension").forGetter(FurledMapLootFunction::getDimension))).apply((Applicative)builder, FurledMapLootFunction::new));

    public Optional<String> getDimension() {
        return this.dimension;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getTranslation() {
        return this.translation;
    }

    protected FurledMapLootFunction(List<LootItemCondition> lootConditions, String destination, String translation, Optional<String> dimension) {
        super(lootConditions);
        this.destination = destination;
        this.translation = translation;
        this.dimension = dimension;
    }

    protected ItemStack run(ItemStack itemStack, LootContext lootContext) {
        if (itemStack.getItem() instanceof FurledMapItem) {
            if (this.dimension.isPresent()) {
                return FurledMapItem.of(ResourceLocation.parse((String)this.destination), (ResourceKey<Level>)ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)ResourceLocation.parse((String)this.dimension.get())), Component.translatable((String)this.translation));
            }
            return FurledMapItem.of(ResourceLocation.parse((String)this.destination), Component.translatable((String)this.translation));
        }
        return itemStack;
    }

    public LootItemFunctionType getType() {
        return LootRegistry.SET_FURLED_MAP_FUNCTION.get();
    }
}

