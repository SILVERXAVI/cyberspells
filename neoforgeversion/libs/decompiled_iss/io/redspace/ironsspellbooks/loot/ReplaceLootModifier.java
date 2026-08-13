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
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
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
import io.redspace.ironsspellbooks.IronsSpellbooks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class ReplaceLootModifier
extends LootModifier {
    public static final Supplier<MapCodec<ReplaceLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(builder -> ReplaceLootModifier.codecStart((RecordCodecBuilder.Instance)builder).and(builder.group((App)Codec.STRING.fieldOf("key").forGetter(m -> m.resourceLocationKey), (App)Codec.DOUBLE.fieldOf("chanceToReplace").forGetter(m -> m.chanceToReplace))).apply((Applicative)builder, ReplaceLootModifier::new)));
    private final String resourceLocationKey;
    private final double chanceToReplace;

    protected ReplaceLootModifier(LootItemCondition[] conditionsIn, String resourceLocationKey, double chanceToReplace) {
        super(conditionsIn);
        this.resourceLocationKey = resourceLocationKey;
        this.chanceToReplace = Mth.clamp((double)chanceToReplace, (double)0.0, (double)1.0);
    }

    @NotNull
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        double roll = context.getRandom().nextDouble();
        IronsSpellbooks.LOGGER.debug("InjectPoolLootModifier.doApply {}: {} < {}", new Object[]{this.resourceLocationKey, roll, this.chanceToReplace});
        if (roll < this.chanceToReplace) {
            ResourceLocation path = ResourceLocation.parse((String)this.resourceLocationKey);
            LootTable lootTable = context.getLevel().getServer().reloadableRegistries().getLootTable(ResourceKey.create((ResourceKey)Registries.LOOT_TABLE, (ResourceLocation)path));
            ObjectArrayList objectarraylist = new ObjectArrayList();
            lootTable.getRandomItemsRaw(context, arg_0 -> ((ObjectArrayList)objectarraylist).add(arg_0));
            return objectarraylist;
        }
        return generatedLoot;
    }

    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}

