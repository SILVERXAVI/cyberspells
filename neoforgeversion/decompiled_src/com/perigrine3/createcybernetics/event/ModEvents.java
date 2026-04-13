/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.alchemy.PotionBrewing$Builder
 *  net.minecraft.world.item.alchemy.Potions
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent
 */
package com.perigrine3.createcybernetics.event;

import com.perigrine3.createcybernetics.event.custom.CyberwareSurgeryEvent;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.potion.ModPotions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public class ModEvents {
    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, (Item)ModItems.DATURA_FLOWER.get(), ModPotions.NEUROPOZYNE);
    }

    @SubscribeEvent
    public static void onCyberwareInstalled(CyberwareSurgeryEvent event) {
    }
}

