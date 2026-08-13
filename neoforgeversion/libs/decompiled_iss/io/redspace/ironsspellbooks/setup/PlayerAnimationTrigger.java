/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.kosmx.playerAnim.api.layered.IAnimation
 *  dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
 *  dev.kosmx.playerAnim.api.layered.ModifierLayer
 *  dev.kosmx.playerAnim.core.data.KeyframeAnimation
 *  dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
 *  dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.fml.loading.FMLLoader
 *  net.neoforged.neoforge.client.event.ClientChatReceivedEvent
 */
package io.redspace.ironsspellbooks.setup;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.CursedArmorStandModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;

@EventBusSubscriber(modid="irons_spellbooks", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public class PlayerAnimationTrigger {
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        String str;
        if (!FMLLoader.isProduction() && (str = event.getMessage().getString()).contains("armorstand")) {
            int id = 0;
            int i = str.indexOf(91);
            double[] ad = new double[3];
            for (int c = 0; c < 100; ++c) {
                int j = str.indexOf(44, i + 1);
                if (j < 0) {
                    ad[id] = Double.parseDouble(str.substring(i + 1, str.indexOf(93)));
                    break;
                }
                ad[id++] = Double.parseDouble(str.substring(i + 1, j));
                i = j;
            }
            CursedArmorStandModel.rightArmPos = ad;
        }
        if (event.getMessage().contains((Component)Component.literal((String)"waving"))) {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(event.getSender());
            if (player == null) {
                return;
            }
            ModifierLayer animation = (ModifierLayer)PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer)((AbstractClientPlayer)player)).get(ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"animation"));
            if (animation != null) {
                animation.setAnimation((IAnimation)new KeyframeAnimationPlayer((KeyframeAnimation)PlayerAnimationRegistry.getAnimation((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"waving"))));
            }
        }
    }
}

