/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.compat.ModCompats;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.CyberwareAttributeHelper;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.Map;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class FullBorgHandler {
    private FullBorgHandler() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        boolean aquariusModel;
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player2 = (ServerPlayer)player;
        if (!player2.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player2.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        boolean geminiModel = FullBorgHandler.isGemini(data);
        if (geminiModel) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "gemini_attackstrength");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "gemini_attackspeed");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "gemini_miningstrength");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "gemini_speed");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "gemini_attackstrength");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "gemini_attackspeed");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "gemini_miningstrength");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "gemini_speed");
        }
        boolean samsonModel = FullBorgHandler.isSamson(data);
        if (samsonModel) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "samson_attackstrength");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "samson_miningstrength");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "samson_durability");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "samson_watermove");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "samson_weight");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "samson_attackstrength");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "samson_miningstrength");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "samson_durability");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "samson_watermove");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "samson_weight");
        }
        boolean eclipseModel = FullBorgHandler.isEclipse(data);
        if (eclipseModel) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "eclipse_crouchspeed");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "eclipse_speed");
            if (player2.isSprinting()) {
                CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "eclipse_sprintspeed");
            } else {
                CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "eclipse_sprintspeed");
            }
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "eclipse_crouchspeed");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "eclipse_speed");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "eclipse_sprintspeed");
        }
        boolean spyderModel = FullBorgHandler.isSpyder(data);
        if (spyderModel) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "spyder_crouchspeed");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "spyder_jumpheight");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "spyder_crouchspeed");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "spyder_jumpheight");
        }
        if (ModCompats.isInstalled("caelus")) {
            boolean wingmanModel = FullBorgHandler.isWingman(data);
            if (wingmanModel) {
                CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "wingman_elytraspeed");
                CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "wingman_elytrahandling");
                if (player2.isShiftKeyDown()) {
                    CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "wingman_elytraspeed");
                }
            } else {
                CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "wingman_elytraspeed");
                CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "wingman_elytrahandling");
            }
        }
        if (aquariusModel = FullBorgHandler.isAquarius(data)) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "aquarius_movement");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "aquarius_mining");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "aquarius_swim");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "aquarius_movement");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "aquarius_mining");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "aquarius_swim");
        }
        boolean dymondModel = FullBorgHandler.isDymond(data);
        if (dymondModel) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "dymond_miningspeed");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "dymond_weight");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "dymond_miningspeed");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "dymond_weight");
        }
        boolean dragoonModel = FullBorgHandler.isDragoon(data);
        if (dragoonModel) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "dragoon_weight");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "dragoon_size");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "dragoon_attack");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "dragoon_resist");
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "dragoon_knockback");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "dragoon_weight");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "dragoon_size");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "dragoon_attack");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "dragoon_resist");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "dragoon_knockback");
        }
        boolean copernicusModel = FullBorgHandler.isCopernicus(data);
        if (copernicusModel) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "copernicus_oxygen");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "copernicus_oxygen");
        }
        boolean genosModel = FullBorgHandler.isGenos(data);
        if (genosModel) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "genos_strength");
            if (player2.isSprinting()) {
                CyberwareAttributeHelper.applyModifier((LivingEntity)player2, "genos_speed");
            } else {
                CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "genos_speed");
            }
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "genos_strength");
            CyberwareAttributeHelper.removeModifier((LivingEntity)player2, "genos_speed");
        }
    }

    public static boolean isGemini(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_SYNTHSKIN.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE);
    }

    public static boolean isSamson(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasMultipleSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE, 3) && data.hasMultipleSpecificItem((Item)ModItems.SKINUPGRADES_SUBDERMALARMOR.get(), CyberwareSlot.SKIN, 3) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM);
    }

    public static boolean isEclipse(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LUNGSUPGRADES_HYPEROXYGENATION.get(), CyberwareSlot.LUNGS, 3) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_OCELOTPAWS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_CHROMATOPHORES.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_SANDEVISTAN.get(), CyberwareSlot.BONE);
    }

    public static boolean isSpyder(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES, 3) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_OCELOTPAWS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_CHROMATOPHORES.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_SYNTHETICSETULES.get(), CyberwareSlot.SKIN);
    }

    public static boolean isWingman(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        if (ModItems.BONEUPGRADES_ELYTRA != null) {
            return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_CYBERSKULL.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_ELYTRA.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG);
        }
        return false;
    }

    public static boolean isAquarius(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_PROPELLERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_UNDERWATERVISION.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.LUNGSUPGRADES_OXYGEN.get(), CyberwareSlot.LUNGS) && data.hasSpecificItem((Item)ModItems.WETWARE_WATERBREATHINGLUNGS.get(), CyberwareSlot.LUNGS);
    }

    public static boolean isDymond(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.LEGUPGRADES_METALDETECTOR.get(), CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_MATRIX.get(), CyberwareSlot.BRAIN) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_DRILLFIST.get(), CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM);
    }

    public static boolean isDragoon(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasMultipleSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE, 3) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_ARMCANNON.get(), CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_TARGETING.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_MATRIX.get(), CyberwareSlot.BRAIN) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_SANDEVISTAN.get(), CyberwareSlot.BONE);
    }

    public static boolean isCopernicus(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LUNGSUPGRADES_OXYGEN.get(), CyberwareSlot.LUNGS, 3) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_SOLARSKIN.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_NETHERITEPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_ZOOM.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_HUDJACK.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_CRAFTHANDS.get(), CyberwareSlot.LARM, CyberwareSlot.RARM);
    }

    public static boolean isGenos(PlayerCyberwareData data) {
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasMultipleSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE, 2) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.LUNGSUPGRADES_HYPEROXYGENATION.get(), 3, CyberwareSlot.LUNGS) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_ARMCANNON.get(), CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_TARGETING.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_HUDJACK.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_WIREDREFLEXES.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_MATRIX.get(), CyberwareSlot.BRAIN);
    }

    public static boolean isFullBorg(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return false;
        }
        if (FullBorgHandler.isGemini(data)) {
            return true;
        }
        if (FullBorgHandler.isSamson(data)) {
            return true;
        }
        if (FullBorgHandler.isEclipse(data)) {
            return true;
        }
        if (FullBorgHandler.isSpyder(data)) {
            return true;
        }
        if (ModCompats.isInstalled("caelus") && FullBorgHandler.isWingman(data)) {
            return true;
        }
        if (FullBorgHandler.isAquarius(data)) {
            return true;
        }
        if (FullBorgHandler.isDymond(data)) {
            return true;
        }
        if (FullBorgHandler.isDragoon(data)) {
            return true;
        }
        if (FullBorgHandler.isCopernicus(data)) {
            return true;
        }
        return FullBorgHandler.isGenos(data);
    }

    public static boolean hasAnyImplantsAtAll(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return false;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data.getAll() == null || data.getAll().isEmpty()) {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "irons_lightning_weakness");
            return false;
        }
        boolean any = false;
        for (Map.Entry<CyberwareSlot, InstalledCyberware[]> entry : data.getAll().entrySet()) {
            CyberwareSlot slot = entry.getKey();
            InstalledCyberware[] arr = entry.getValue();
            if (arr == null) continue;
            for (int i = 0; i < arr.length; ++i) {
                ItemStack st;
                InstalledCyberware installed = arr[i];
                if (installed == null || (st = installed.getItem()) == null || st.isEmpty() || !st.is(ModTags.Items.CYBERWARE_ITEM)) continue;
                any = true;
                break;
            }
            if (!any) continue;
            break;
        }
        if (any) {
            CyberwareAttributeHelper.applyModifier((LivingEntity)player, "irons_lightning_weakness");
        } else {
            CyberwareAttributeHelper.removeModifier((LivingEntity)player, "irons_lightning_weakness");
        }
        return any;
    }
}

