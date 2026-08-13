/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.api.item.UpgradeData;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.UpgradeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class ArcaneAnvilJeiRecipe {
    @NotNull
    Type type;
    @Nullable
    Item leftItem;
    @Nullable
    Item rightItem;
    @Nullable
    AbstractSpell spell;
    @Nullable
    int level;

    public ArcaneAnvilJeiRecipe(Item leftItem, Item rightItem) {
        this.leftItem = leftItem;
        this.rightItem = rightItem;
        this.type = Type.Item_Upgrade;
    }

    public ArcaneAnvilJeiRecipe(Item leftItem, AbstractSpell spell) {
        this.leftItem = leftItem;
        this.spell = spell;
        this.type = Type.Imbue;
    }

    public ArcaneAnvilJeiRecipe(AbstractSpell spell, int baseLevel) {
        this.spell = spell;
        this.level = baseLevel;
        this.type = Type.Scroll_Upgrade;
    }

    public ArcaneAnvilJeiRecipe(AbstractSpell spell) {
        this.spell = spell;
        this.type = Type.Affinity_Ring_Attune;
    }

    public Tuple<List<ItemStack>, List<ItemStack>, List<ItemStack>> getRecipeItems() {
        return switch (this.type.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> {
                ItemStack scroll1 = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
                ItemStack scroll2 = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
                ItemStack ink = new ItemStack((ItemLike)InkItem.getInkForRarity(this.spell.getRarity(this.level + 1)));
                ISpellContainer.createScrollContainer(this.spell, this.level, scroll1);
                ISpellContainer.createScrollContainer(this.spell, this.level + 1, scroll2);
                yield new Tuple(List.of(scroll1), List.of(ink), List.of(scroll2));
            }
            case 2 -> {
                Tuple tuple = new Tuple(new ArrayList(), new ArrayList(), new ArrayList());
                ((List)tuple.a).add(new ItemStack((ItemLike)this.leftItem));
                SpellRegistry.getEnabledSpells().forEach(spell -> IntStream.rangeClosed(spell.getMinLevel(), spell.getMaxLevel()).forEach(i -> {
                    ItemStack scroll = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
                    ISpellContainer.createScrollContainer(spell, i, scroll);
                    ItemStack result = new ItemStack((ItemLike)this.leftItem);
                    ISpellContainer.createScrollContainer(spell, i, result);
                    ((List)tuple.b).add(scroll);
                    ((List)tuple.c).add(result);
                }));
                yield tuple;
            }
            case 1 -> {
                Tuple tuple = new Tuple(new ArrayList(), new ArrayList(), new ArrayList());
                ((List)tuple.a).add(new ItemStack((ItemLike)this.leftItem));
                ItemStack upgradeStack = new ItemStack((ItemLike)this.rightItem);
                ItemStack result = new ItemStack((ItemLike)this.leftItem);
                UpgradeData.set(result, UpgradeData.NONE.addUpgrade(result, (Holder<UpgradeOrbType>)Minecraft.getInstance().level.registryAccess().holderOrThrow((ResourceKey)upgradeStack.get(ComponentRegistry.UPGRADE_ORB_TYPE)), UpgradeUtils.getRelevantEquipmentSlot(result)));
                ((List)tuple.b).add(upgradeStack);
                ((List)tuple.c).add(result);
                yield tuple;
            }
            case 3 -> {
                Tuple tuple = new Tuple(new ArrayList(), new ArrayList(), new ArrayList());
                ItemStack result = new ItemStack((ItemLike)ItemRegistry.AFFINITY_RING.get());
                AffinityData.set(result, new AffinityData(this.spell));
                SpellRegistry.getEnabledSpells().forEach(randomSpell -> {
                    ItemStack baseRing = new ItemStack((ItemLike)ItemRegistry.AFFINITY_RING.get());
                    AffinityData.set(baseRing, new AffinityData((AbstractSpell)randomSpell));
                    ((List)tuple.a).add(baseRing);
                });
                IntStream.rangeClosed(this.spell.getMinLevel(), this.spell.getMaxLevel()).forEach(i -> {
                    ItemStack scroll = new ItemStack(ItemRegistry.SCROLL);
                    ISpellContainer.createScrollContainer(this.spell, i, scroll);
                    ((List)tuple.b).add(scroll);
                });
                ((List)tuple.c).add(result);
                yield tuple;
            }
        };
    }

    static enum Type {
        Scroll_Upgrade,
        Item_Upgrade,
        Imbue,
        Affinity_Ring_Attune;

    }

    public record Tuple<A, B, C>(A a, B b, C c) {
    }
}

