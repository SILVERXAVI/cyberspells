/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Suppliers
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  com.mojang.serialization.codecs.RecordCodecBuilder$Instance
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.LootTable
 *  net.minecraft.world.level.storage.loot.predicates.LootItemCondition
 *  net.neoforged.neoforge.common.loot.IGlobalLootModifier
 *  net.neoforged.neoforge.common.loot.LootModifier
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.loot;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AppendLootModifier
extends LootModifier {
    public static final Supplier<MapCodec<AppendLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(builder -> AppendLootModifier.codecStart((RecordCodecBuilder.Instance)builder).and((App)Codec.STRING.fieldOf("key").forGetter(m -> m.resourceLocationKey)).apply((Applicative)builder, AppendLootModifier::new)));
    private final String resourceLocationKey;

    protected AppendLootModifier(LootItemCondition[] conditionsIn, String resourceLocationKey) {
        super(conditionsIn);
        this.resourceLocationKey = resourceLocationKey;
    }

    @NotNull
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation path = ResourceLocation.parse((String)this.resourceLocationKey);
        LootTable lootTable = context.getLevel().getServer().reloadableRegistries().getLootTable(ResourceKey.create((ResourceKey)Registries.LOOT_TABLE, (ResourceLocation)path));
        ObjectArrayList objectarraylist = new ObjectArrayList();
        lootTable.getRandomItemsRaw(context, arg_0 -> ((ObjectArrayList)objectarraylist).add(arg_0));
        generatedLoot.addAll((ObjectList)objectarraylist);
        return generatedLoot;
    }

    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}

