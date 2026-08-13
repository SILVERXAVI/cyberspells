/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.npc.VillagerTrades$ItemListing
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.alchemy.Potions
 *  net.minecraft.world.item.component.BundleContents
 *  net.minecraft.world.item.trading.ItemCost
 *  net.minecraft.world.item.trading.MerchantOffer
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.storage.loot.LootParams
 *  net.minecraft.world.level.storage.loot.LootParams$Builder
 *  net.minecraft.world.level.storage.loot.LootTable
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.village.WandererTradesEvent
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.player;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class AdditionalWanderingTrades {
    public static final int INK_SALE_PRICE_PER_RARITY = 8;
    public static final int INK_BUY_PRICE_PER_RARITY = 5;

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void addWanderingTrades(WandererTradesEvent event) {
        if (ServerConfigs.SPEC.isLoaded() && !((Boolean)ServerConfigs.ADDITIONAL_WANDERING_TRADER_TRADES.get()).booleanValue()) {
            return;
        }
        List<VillagerTrades.ItemListing> additionalGenericTrades = List.of(new RandomScrollTrade(new SpellFilter()), new RandomScrollTrade(new SpellFilter()), new RandomScrollTrade(new SpellFilter()), new InkBuyTrade((InkItem)((Object)ItemRegistry.INK_COMMON.get())), new InkBuyTrade((InkItem)((Object)ItemRegistry.INK_UNCOMMON.get())), new InkBuyTrade((InkItem)((Object)ItemRegistry.INK_RARE.get())), new InkBuyTrade((InkItem)((Object)ItemRegistry.INK_EPIC.get())), new InkBuyTrade((InkItem)((Object)ItemRegistry.INK_LEGENDARY.get())), new InkSellTrade((InkItem)((Object)ItemRegistry.INK_COMMON.get())), new InkSellTrade((InkItem)((Object)ItemRegistry.INK_UNCOMMON.get())), new InkSellTrade((InkItem)((Object)ItemRegistry.INK_RARE.get())), new InkSellTrade((InkItem)((Object)ItemRegistry.INK_EPIC.get())), new InkSellTrade((InkItem)((Object)ItemRegistry.INK_LEGENDARY.get())), new RandomCurioTrade());
        List<ScrollPouchTrade> additionalRareTrades = List.of(SimpleTrade.of((trader, random) -> new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 64 - random.nextIntBetweenInclusive(1, 8)), Optional.of(new ItemCost((ItemLike)Items.ECHO_SHARD, random.nextIntBetweenInclusive(1, 3))), new ItemStack((ItemLike)ItemRegistry.LOST_KNOWLEDGE_FRAGMENT.get()), 8, 0, 0.05f)), SimpleTrade.of((trader, random) -> new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 64), Optional.of(new ItemCost((ItemLike)Items.EMERALD, random.nextIntBetweenInclusive(48, 64))), new ItemStack((ItemLike)ItemRegistry.HITHER_THITHER_WAND.get()), 1, 0, 0.05f)), new RandomCurioTrade(), new RandomCurioTrade(), new RandomCurioTrade(), new ScrollPouchTrade(), new ScrollPouchTrade());
        event.getGenericTrades().addAll(additionalGenericTrades.stream().filter(Objects::nonNull).toList());
        event.getRareTrades().addAll(additionalRareTrades.stream().filter(Objects::nonNull).toList());
    }

    public static class RandomScrollTrade
    implements VillagerTrades.ItemListing {
        protected final Optional<ItemCost> price2;
        protected final ItemStack forSale;
        protected final int maxTrades;
        protected final int xp;
        protected final float priceMult;
        protected final SpellFilter spellFilter;
        protected float minQuality;
        protected float maxQuality;

        public RandomScrollTrade(SpellFilter spellFilter) {
            this.spellFilter = spellFilter;
            this.price2 = Optional.empty();
            this.forSale = new ItemStack((ItemLike)ItemRegistry.SCROLL.get());
            this.maxTrades = 1;
            this.xp = 5;
            this.priceMult = 0.05f;
            this.minQuality = 0.0f;
            this.maxQuality = 1.0f;
        }

        public RandomScrollTrade(SpellFilter filter, float minQuality, float maxQuality) {
            this(filter);
            this.minQuality = minQuality;
            this.maxQuality = maxQuality;
        }

        @Nullable
        public MerchantOffer getOffer(Entity pTrader, RandomSource random) {
            AbstractSpell spell = this.spellFilter.getRandomSpell(random);
            if (spell == SpellRegistry.none()) {
                return null;
            }
            int level = random.nextIntBetweenInclusive(1 + (int)((float)spell.getMaxLevel() * this.minQuality), (int)((float)(spell.getMaxLevel() - 1) * this.maxQuality) + 1);
            ISpellContainer.createScrollContainer(spell, level, this.forSale);
            ItemCost price = new ItemCost((ItemLike)Items.EMERALD, spell.getRarity(level).getValue() * 5 + random.nextIntBetweenInclusive(4, 7) + level);
            return new MerchantOffer(price, this.price2, this.forSale, this.maxTrades, this.xp, this.priceMult);
        }
    }

    public static class InkBuyTrade
    extends SimpleTrade {
        public InkBuyTrade(InkItem item) {
            super((Entity trader, RandomSource random) -> {
                boolean emeralds = random.nextBoolean();
                return new MerchantOffer(new ItemCost((ItemLike)item, 1), new ItemStack((ItemLike)(emeralds ? Items.EMERALD : (ItemLike)ItemRegistry.ARCANE_ESSENCE.get()), 5 * item.getRarity().getValue() / (emeralds ? 1 : 2) + random.nextIntBetweenInclusive(2, 3)), 8, 1, 0.05f);
            });
        }
    }

    public static class InkSellTrade
    extends SimpleTrade {
        public InkSellTrade(InkItem item) {
            super((Entity trader, RandomSource random) -> new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 8 * item.getRarity().getValue() + random.nextIntBetweenInclusive(2, 3)), new ItemStack((ItemLike)item), 4, 1, 0.05f));
        }
    }

    static class RandomCurioTrade
    extends SimpleTrade {
        private RandomCurioTrade() {
            super((trader, random) -> {
                LootParams context;
                LootTable loottable;
                ObjectArrayList items;
                if (!trader.level.isClientSide && !(items = (loottable = trader.level.getServer().reloadableRegistries().getLootTable(ResourceKey.create((ResourceKey)Registries.LOOT_TABLE, (ResourceLocation)IronsSpellbooks.id("magic_items/basic_curios")))).getRandomItems(context = new LootParams.Builder((ServerLevel)trader.level).create(LootContextParamSets.EMPTY))).isEmpty()) {
                    ItemStack forSale = (ItemStack)items.get(0);
                    ItemCost cost = new ItemCost((ItemLike)Items.EMERALD, random.nextIntBetweenInclusive(14, 25));
                    return new MerchantOffer(cost, forSale, 1, 5, 0.5f);
                }
                return null;
            });
        }
    }

    public static class SimpleTrade
    implements VillagerTrades.ItemListing {
        final BiFunction<Entity, RandomSource, MerchantOffer> getOffer;

        protected SimpleTrade(BiFunction<Entity, RandomSource, MerchantOffer> getOffer) {
            this.getOffer = getOffer;
        }

        public static SimpleTrade of(BiFunction<Entity, RandomSource, MerchantOffer> getOffer) {
            return new SimpleTrade(getOffer);
        }

        public MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
            return this.getOffer.apply(pTrader, pRandom);
        }
    }

    static class ScrollPouchTrade
    extends SimpleTrade {
        private ScrollPouchTrade() {
            super((trader, random) -> {
                LootParams context;
                LootTable loottable;
                ObjectArrayList items;
                if (!trader.level.isClientSide && !(items = (loottable = trader.level.getServer().reloadableRegistries().getLootTable(ResourceKey.create((ResourceKey)Registries.LOOT_TABLE, (ResourceLocation)IronsSpellbooks.id("magic_items/scroll_pouch")))).getRandomItems(context = new LootParams.Builder((ServerLevel)trader.level).create(LootContextParamSets.EMPTY))).isEmpty()) {
                    int quality = 0;
                    for (ItemStack stack : items) {
                        if (!(stack.getItem() instanceof Scroll)) continue;
                        quality += ISpellContainer.get(stack).getSpellAtIndex(0).getRarity().getValue() + 1;
                    }
                    ItemStack forSale = new ItemStack((ItemLike)Items.BUNDLE);
                    forSale.set(DataComponents.ITEM_NAME, (Object)Component.translatable((String)"item.irons_spellbooks.scroll_pouch"));
                    forSale.set(DataComponents.BUNDLE_CONTENTS, (Object)new BundleContents((List)items));
                    ItemCost cost = new ItemCost((ItemLike)Items.EMERALD, quality * 4 + random.nextIntBetweenInclusive(8, 16));
                    return new MerchantOffer(cost, forSale, 1, 5, 0.5f);
                }
                return null;
            });
        }
    }

    public static class PotionSellTrade
    extends SimpleTrade {
        public PotionSellTrade(@Nullable Potion potion) {
            super((Entity trader, RandomSource random) -> {
                List<Potion> potions;
                Potion potion1 = potion;
                if (potion1 == null && !(potions = BuiltInRegistries.POTION.stream().filter(p -> !p.getEffects().isEmpty()).toList()).isEmpty()) {
                    potion1 = potions.get(random.nextInt(potions.size()));
                }
                if (potion1 == null) {
                    potion1 = (Potion)Potions.AWKWARD.value();
                }
                int amplifier = 0;
                int duration = 0;
                List effects = potion1.getEffects();
                if (!effects.isEmpty()) {
                    MobEffectInstance effect = (MobEffectInstance)effects.getFirst();
                    amplifier = effect.getAmplifier();
                    duration = effect.getDuration() / 1200;
                }
                ItemStack potionStack = new ItemStack((ItemLike)Items.POTION);
                potionStack.set(DataComponents.POTION_CONTENTS, (Object)new PotionContents(BuiltInRegistries.POTION.wrapAsHolder((Object)potion1)));
                return new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, random.nextIntBetweenInclusive(12, 16) + random.nextIntBetweenInclusive(4, 6) * amplifier + duration), potionStack, 3, 1, 0.05f);
            });
        }
    }

    public static class ExilirSellTrade
    extends SimpleTrade {
        public ExilirSellTrade(boolean onlyLesser, boolean onlyGreater) {
            super((trader, random) -> {
                List<Item> lesser = List.of((Item)ItemRegistry.EVASION_ELIXIR.get(), (Item)ItemRegistry.OAKSKIN_ELIXIR.get(), (Item)ItemRegistry.INVISIBILITY_ELIXIR.get());
                List<Item> greater = List.of((Item)ItemRegistry.GREATER_EVASION_ELIXIR.get(), (Item)ItemRegistry.GREATER_OAKSKIN_ELIXIR.get(), (Item)ItemRegistry.GREATER_INVISIBILITY_ELIXIR.get(), (Item)ItemRegistry.GREATER_HEALING_POTION.get());
                boolean isGreater = onlyLesser ? false : (onlyGreater ? true : random.nextBoolean());
                Item item = isGreater ? greater.get(random.nextInt(greater.size())) : lesser.get(random.nextInt(lesser.size()));
                return new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 10 + random.nextIntBetweenInclusive(4, 8) * (isGreater ? 2 : 1)), new ItemStack((ItemLike)item), 3, 1, 0.05f);
            });
        }
    }

    public static class ExilirBuyTrade
    extends SimpleTrade {
        public ExilirBuyTrade(boolean onlyLesser, boolean onlyGreater) {
            super((trader, random) -> {
                List<Item> lesser = List.of((Item)ItemRegistry.EVASION_ELIXIR.get(), (Item)ItemRegistry.OAKSKIN_ELIXIR.get(), (Item)ItemRegistry.INVISIBILITY_ELIXIR.get());
                List<Item> greater = List.of((Item)ItemRegistry.GREATER_EVASION_ELIXIR.get(), (Item)ItemRegistry.GREATER_OAKSKIN_ELIXIR.get(), (Item)ItemRegistry.GREATER_INVISIBILITY_ELIXIR.get(), (Item)ItemRegistry.GREATER_HEALING_POTION.get());
                boolean isGreater = onlyLesser ? false : (onlyGreater ? true : random.nextBoolean());
                Item item = isGreater ? greater.get(random.nextInt(greater.size())) : lesser.get(random.nextInt(lesser.size()));
                return new MerchantOffer(new ItemCost((ItemLike)item), new ItemStack((ItemLike)Items.EMERALD, 6 + random.nextIntBetweenInclusive(3, 6) * (isGreater ? 2 : 1)), 6, 1, 0.05f);
            });
        }
    }

    public static class SimpleSell
    extends SimpleTrade {
        public SimpleSell(int tradeCount, ItemStack sell, int minEmeralds, int maxEmeralds) {
            super((trader, random) -> new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, random.nextIntBetweenInclusive(minEmeralds, maxEmeralds)), sell, tradeCount, 0, 0.05f));
        }
    }

    public static class SimpleBuy
    extends SimpleTrade {
        public SimpleBuy(int tradeCount, ItemCost buy, int minEmeralds, int maxEmeralds) {
            super((trader, random) -> new MerchantOffer(buy, new ItemStack((ItemLike)Items.EMERALD, random.nextIntBetweenInclusive(minEmeralds, maxEmeralds)), tradeCount, 0, 0.05f));
        }
    }
}

