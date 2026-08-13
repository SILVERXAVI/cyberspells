/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.api.item.curios;

import com.google.common.collect.HashMultimap;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record AffinityData(Map<ResourceLocation, Integer> affinityData) {
    @Deprecated(forRemoval=true)
    public static final Codec<AffinityData> SINGLE_CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Codec.STRING.fieldOf("id").forGetter(data -> data.affinityData.keySet().stream().findFirst().orElse(IronsSpellbooks.id("none")).toString()), (App)Codec.INT.optionalFieldOf("bonus", (Object)1).forGetter(data -> data.affinityData.values().stream().findFirst().orElse(1))).apply((Applicative)builder, (s, i) -> new AffinityData(Map.of(ResourceLocation.parse((String)s), i))));
    public static final Codec<AffinityData> MULTI_CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Codec.unboundedMap((Codec)ResourceLocation.CODEC, (Codec)Codec.INT).fieldOf("bonuses").forGetter(AffinityData::affinityData)).apply((Applicative)builder, AffinityData::new));
    public static final Codec<AffinityData> CODEC = Codec.withAlternative(MULTI_CODEC, SINGLE_CODEC);
    public static final StreamCodec<ByteBuf, AffinityData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
    public static final AffinityData NONE = new AffinityData(Map.of());

    private AffinityData(String id) {
        this(Map.of(ResourceLocation.parse((String)id), 1));
    }

    public AffinityData(AbstractSpell spell) {
        this(spell.getSpellId());
    }

    public static AffinityData getAffinityData(ItemStack stack) {
        return stack.has(ComponentRegistry.AFFINITY_COMPONENT) ? (AffinityData)stack.get(ComponentRegistry.AFFINITY_COMPONENT) : NONE;
    }

    public static void setAffinityData(ItemStack stack, AbstractSpell spell) {
        AffinityData.set(stack, new AffinityData(spell));
    }

    public static void setAffinityData(ItemStack stack, AbstractSpell spell, int bonus) {
        AffinityData.set(stack, new AffinityData(Map.of(spell.getSpellResource(), bonus)));
    }

    public static void set(ItemStack stack, AffinityData data) {
        stack.set(ComponentRegistry.AFFINITY_COMPONENT, (Object)data);
    }

    public static boolean hasAffinityData(ItemStack itemStack) {
        return itemStack.has(ComponentRegistry.AFFINITY_COMPONENT);
    }

    @Deprecated(forRemoval=true)
    public AbstractSpell getSpell() {
        return this.affinityData.keySet().stream().findFirst().map(SpellRegistry::getSpell).orElse(SpellRegistry.none());
    }

    public int getBonusFor(AbstractSpell spell) {
        return this.affinityData.getOrDefault(spell.getSpellResource(), 0);
    }

    public boolean hasBonusFor(AbstractSpell spell) {
        return this.getBonusFor(spell) != 0;
    }

    public String getNameForItem() {
        return this.getSpell() == SpellRegistry.none() ? Component.translatable((String)"tooltip.irons_spellbooks.no_affinity").getString() : this.getSpell().getSchoolType().getDisplayName().getString();
    }

    public List<MutableComponent> getDescriptionComponent() {
        HashMultimap byLevel = HashMultimap.create();
        this.affinityData.forEach((key, value) -> byLevel.put(value, (Object)SpellRegistry.getSpell(key)));
        return byLevel.keySet().stream().map(key -> {
            MutableComponent spellListComponent = Component.literal((String)"").withStyle(ChatFormatting.YELLOW);
            List spells = byLevel.get(key).stream().toList();
            for (int i = 0; i < spells.size(); ++i) {
                AbstractSpell spell = (AbstractSpell)spells.get(i);
                spellListComponent.append((Component)Component.translatable((String)spell.getComponentId()).withStyle(spell.getSchoolType().getDisplayName().getStyle()));
                if (i == spells.size() - 1) continue;
                spellListComponent.append(", ");
            }
            return key == 1 ? Component.translatable((String)"tooltip.irons_spellbooks.enhance_spell_level", (Object[])new Object[]{spellListComponent}).withStyle(ChatFormatting.YELLOW) : Component.translatable((String)"tooltip.irons_spellbooks.enhance_spell_level_plural", (Object[])new Object[]{key, spellListComponent}).withStyle(ChatFormatting.YELLOW);
        }).toList();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof AffinityData)) return false;
        AffinityData affinityData = (AffinityData)obj;
        if (!affinityData.affinityData.equals(this.affinityData)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.affinityData.hashCode();
    }
}

