/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  net.minecraft.world.item.Item
 *  net.neoforged.bus.api.Event
 */
package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronInteraction;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;

@Deprecated(forRemoval=true)
public class AlchemistCauldronBuildInteractionsEvent
extends Event {
    private final Object2ObjectOpenHashMap<Item, AlchemistCauldronInteraction> interactionMap;

    public AlchemistCauldronBuildInteractionsEvent(Object2ObjectOpenHashMap<Item, AlchemistCauldronInteraction> interactionMap) {
        this.interactionMap = interactionMap;
    }

    public void addInteraction(Item item, AlchemistCauldronInteraction interaction) {
        IronsSpellbooks.LOGGER.warn("Another Mod is trying to add an Alchemist Cauldron interaction! This no longer works!");
    }

    public void addSimpleBottleEmptyInteraction(Item item) {
        IronsSpellbooks.LOGGER.warn("Another Mod is trying to add an Alchemist Cauldron interaction! This no longer works!");
    }
}

