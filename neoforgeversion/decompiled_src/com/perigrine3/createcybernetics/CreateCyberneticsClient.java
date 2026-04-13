/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.IExtensionPoint
 *  net.neoforged.fml.ModContainer
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.Mod
 *  net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
 *  net.neoforged.neoforge.client.gui.ConfigurationScreen
 *  net.neoforged.neoforge.client.gui.IConfigScreenFactory
 */
package com.perigrine3.createcybernetics;

import com.perigrine3.createcybernetics.CreateCybernetics;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value="createcybernetics", dist={Dist.CLIENT})
@EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT})
public class CreateCyberneticsClient {
    public CreateCyberneticsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (IExtensionPoint)((IConfigScreenFactory)ConfigurationScreen::new));
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        CreateCybernetics.LOGGER.info("HELLO FROM CLIENT SETUP");
        CreateCybernetics.LOGGER.info("MINECRAFT NAME >> {}", (Object)Minecraft.getInstance().getUser().getName());
    }
}

