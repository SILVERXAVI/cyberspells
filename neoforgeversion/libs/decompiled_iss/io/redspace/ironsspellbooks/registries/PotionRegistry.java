/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.Potions
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class PotionRegistry {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create((ResourceKey)Registries.POTION, (String)"irons_spellbooks");
    public static final DeferredHolder<Potion, Potion> INSTANT_MANA_ONE = POTIONS.register("instant_mana_one", () -> new Potion("mana", new MobEffectInstance[]{new MobEffectInstance(MobEffectRegistry.INSTANT_MANA)}));
    public static final DeferredHolder<Potion, Potion> INSTANT_MANA_TWO = POTIONS.register("instant_mana_two", () -> new Potion("mana", new MobEffectInstance[]{new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 0, 1)}));
    public static final DeferredHolder<Potion, Potion> INSTANT_MANA_THREE = POTIONS.register("instant_mana_three", () -> new Potion("mana", new MobEffectInstance[]{new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 0, 2)}));
    public static final DeferredHolder<Potion, Potion> INSTANT_MANA_FOUR = POTIONS.register("instant_mana_four", () -> new Potion("mana", new MobEffectInstance[]{new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 0, 3)}));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

    @SubscribeEvent
    public static void addRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addMix(Potions.AWKWARD, (Item)ItemRegistry.ARCANE_ESSENCE.get(), INSTANT_MANA_ONE);
        event.getBuilder().addMix(INSTANT_MANA_ONE, Items.GLOWSTONE_DUST, INSTANT_MANA_TWO);
        event.getBuilder().addMix(INSTANT_MANA_TWO, Items.AMETHYST_SHARD, INSTANT_MANA_THREE);
        event.getBuilder().addMix(INSTANT_MANA_THREE, Items.AMETHYST_CLUSTER, INSTANT_MANA_FOUR);
    }
}

