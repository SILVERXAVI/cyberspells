/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  com.mojang.serialization.codecs.RecordCodecBuilder$Instance
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction
 *  net.minecraft.world.level.storage.loot.functions.LootItemFunctionType
 *  net.minecraft.world.level.storage.loot.predicates.LootItemCondition
 */
package io.redspace.ironsspellbooks.loot;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.item.curios.AffinityRing;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.registries.LootRegistry;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RandomizeRingEnhancementFunction
extends LootItemConditionalFunction {
    final SpellFilter spellFilter;
    public static final MapCodec<RandomizeRingEnhancementFunction> CODEC = RecordCodecBuilder.mapCodec(builder -> RandomizeRingEnhancementFunction.commonFields((RecordCodecBuilder.Instance)builder).and((App)SpellFilter.CODEC.optionalFieldOf("spell_filter", (Object)new SpellFilter()).forGetter(data -> data.spellFilter)).apply((Applicative)builder, RandomizeRingEnhancementFunction::new));

    protected RandomizeRingEnhancementFunction(List<LootItemCondition> lootConditions, SpellFilter spellFilter) {
        super(lootConditions);
        this.spellFilter = spellFilter;
    }

    protected ItemStack run(ItemStack itemStack, LootContext lootContext) {
        if (itemStack.getItem() instanceof AffinityRing) {
            AbstractSpell spell = this.spellFilter.getRandomSpell(lootContext.getRandom(), s -> s.isEnabled() && s.getMaxLevel() > 1);
            AffinityData.setAffinityData(itemStack, spell);
            return spell == SpellRegistry.none() ? ItemStack.EMPTY : itemStack;
        }
        return itemStack;
    }

    public LootItemFunctionType getType() {
        return LootRegistry.RANDOMIZE_SPELL_RING_FUNCTION.get();
    }
}

