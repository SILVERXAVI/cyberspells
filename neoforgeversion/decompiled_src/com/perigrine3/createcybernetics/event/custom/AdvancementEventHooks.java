/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent$Finish
 *  net.neoforged.neoforge.event.entity.living.MobEffectEvent$Added
 *  net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent$Post
 *  net.neoforged.neoforge.event.tick.ServerTickEvent$Post
 */
package com.perigrine3.createcybernetics.event.custom;

import com.perigrine3.createcybernetics.advancement.ModCriteria;
import com.perigrine3.createcybernetics.advancement.triggers.BodysnatcherTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.BonelessTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.BonesAndAllTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.ChestPainsTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.ChromeJunkieTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.CogitoErgoSumTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.CorticalStackTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.CyberpsychosisTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.DavidMartinezSpecialTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.FirstCyberwareTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.FirstRipperdocVisitTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.FirstScavengedCyberwareTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.FleshWoundTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.KungFuTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.LetsDanceTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.LiverRemovedTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.LungsRemovedTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.MissingMuscleTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.OverTheEdgeTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.PrettyInPinkTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.SkinRemovedTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.SniktTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.SpiderManTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.ThoughtlessTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.TooMuchTooFastTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.UpgradedTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.WeakFleshTrigger;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.damage.ModDamageTypes;
import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.event.custom.CyberwareSurgeryEvent;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
public final class AdvancementEventHooks {
    private AdvancementEventHooks() {
    }

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Post event) {
        Player player = event.getPlayer();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player2 = (ServerPlayer)player;
        ItemStack pickedUp = event.getOriginalStack();
        if (pickedUp.isEmpty()) {
            return;
        }
        if (pickedUp.is(ModTags.Items.SCAVENGED_CYBERWARE)) {
            ((FirstScavengedCyberwareTrigger)((Object)ModCriteria.FIRST_SCAVENGED_CYBERWARE.get())).trigger(player2);
        }
        if (pickedUp.is(ModTags.Items.CYBERWARE_ITEM)) {
            ((FirstCyberwareTrigger)((Object)ModCriteria.FIRST_CYBERWARE.get())).trigger(player2);
        }
    }

    @SubscribeEvent
    public static void onMobEffectAdded(MobEffectEvent.Added event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer)livingEntity;
        MobEffectInstance inst = event.getEffectInstance();
        if (inst == null) {
            return;
        }
        if (inst.getEffect() == ModEffects.CYBERWARE_REJECTION) {
            ((CyberpsychosisTrigger)((Object)ModCriteria.CYBERPSYCHOSIS.get())).trigger(player);
        }
        if (inst.getEffect() == ModEffects.NEUROPOZYNE) {
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            int count = data.incrementNeuropozyneApplyCount();
            player.syncData(ModAttachments.CYBERWARE);
            if (count >= 100) {
                ((ChromeJunkieTrigger)((Object)ModCriteria.CHROME_JUNKIE.get())).trigger(player);
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity livingEntity;
        PlayerCyberwareData data;
        LivingEntity livingEntity2 = event.getEntity();
        if (!(livingEntity2 instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer)livingEntity2;
        if (event.getSource().is(ModDamageTypes.SURGERY)) {
            ((TooMuchTooFastTrigger)((Object)ModCriteria.TOO_MUCH_TOO_FAST.get())).trigger(player);
        }
        if (event.getSource().is(ModDamageTypes.CYBERWARE_REJECTION)) {
            ((OverTheEdgeTrigger)((Object)ModCriteria.OVER_THE_EDGE.get())).trigger(player);
        }
        if (event.getSource().is(ModDamageTypes.DAVIDS_DEMISE)) {
            ((DavidMartinezSpecialTrigger)((Object)ModCriteria.DAVID_SPECIAL.get())).trigger(player);
        }
        if ((data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE)) == null) {
            return;
        }
        if (ModItems.BRAINUPGRADES_CORTICALSTACK != null && data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_CORTICALSTACK.get(), CyberwareSlot.BRAIN)) {
            ((CorticalStackTrigger)((Object)ModCriteria.CORTICAL_STACK.get())).trigger(player);
        }
        if (!((livingEntity = event.getEntity()) instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer victim = (ServerPlayer)livingEntity;
        Entity entity = event.getSource().getEntity();
        if (!(entity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer killer = (ServerPlayer)entity;
        if (killer == victim) {
            return;
        }
        PlayerCyberwareData killerData = (PlayerCyberwareData)killer.getData(ModAttachments.CYBERWARE);
        PlayerCyberwareData victimData = (PlayerCyberwareData)victim.getData(ModAttachments.CYBERWARE);
        boolean killerFullyOrganic = !killerData.hasAnyTagged(ModTags.Items.CYBERWARE_ITEM, CyberwareSlot.values());
        boolean victimFullBorgActive = AdvancementEventHooks.isFullBorgSetActive(victimData);
        if (killerFullyOrganic && victimFullBorgActive) {
            ((LetsDanceTrigger)((Object)ModCriteria.LETS_DANCE.get())).trigger(killer);
        }
    }

    @SubscribeEvent
    public static void onSurgery(CyberwareSurgeryEvent event) {
        int tint;
        ServerPlayer player = event.getPlayer();
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        if (data.hasAnyTagged(ModTags.Items.CYBERWARE_ITEM, CyberwareSlot.values())) {
            ((FirstRipperdocVisitTrigger)((Object)ModCriteria.FIRST_RIPPERDOC_VISIT.get())).trigger(player);
        }
        if (AdvancementEventHooks.isFullBorgSetActive((PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE))) {
            ((WeakFleshTrigger)((Object)ModCriteria.WEAK_FLESH.get())).trigger(player);
        }
        if (data.hasSpecificItem((Item)ModItems.WETWARE_WEBSHOOTING_RIGHTARM.get(), CyberwareSlot.RARM) || data.hasSpecificItem((Item)ModItems.WETWARE_WEBSHOOTING_LEFTARM.get(), CyberwareSlot.LARM) || data.hasSpecificItem((Item)ModItems.WETWARE_WEBSHOOTINGINTESTINES.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_SYNTHETICSETULES.get(), CyberwareSlot.SKIN)) {
            ((SpiderManTrigger)((Object)ModCriteria.SPIDER_MAN.get())).trigger(player);
        }
        if (!data.hasAnyTagged(ModTags.Items.BRAIN_ITEMS, CyberwareSlot.BRAIN)) {
            ((ThoughtlessTrigger)((Object)ModCriteria.THOUGHTLESS.get())).trigger(player);
        }
        if (!data.hasAnyTagged(ModTags.Items.HEART_ITEMS, CyberwareSlot.HEART)) {
            ((ChestPainsTrigger)((Object)ModCriteria.CHEST_PAINS.get())).trigger(player);
        }
        if (!data.hasAnyTagged(ModTags.Items.LIVER_ITEMS, CyberwareSlot.ORGANS)) {
            ((LiverRemovedTrigger)((Object)ModCriteria.LIVER_REMOVED.get())).trigger(player);
        }
        if (!data.hasAnyTagged(ModTags.Items.SKIN_ITEMS, CyberwareSlot.SKIN)) {
            ((SkinRemovedTrigger)((Object)ModCriteria.SKIN_REMOVED.get())).trigger(player);
        }
        if (!data.hasAnyTagged(ModTags.Items.LUNGS_ITEMS, CyberwareSlot.LUNGS)) {
            ((LungsRemovedTrigger)((Object)ModCriteria.LUNGS_REMOVED.get())).trigger(player);
        }
        if (!data.hasAnyTagged(ModTags.Items.BONE_ITEMS, CyberwareSlot.BONE)) {
            ((BonelessTrigger)((Object)ModCriteria.BONELESS.get())).trigger(player);
        }
        if (!data.hasAnyTagged(ModTags.Items.MUSCLE_ITEMS, CyberwareSlot.MUSCLE)) {
            ((MissingMuscleTrigger)((Object)ModCriteria.MISSING_MUSCLE.get())).trigger(player);
        }
        if (!(data.hasAnyTagged(ModTags.Items.LEFTARM_ITEMS, CyberwareSlot.LARM) || data.hasAnyTagged(ModTags.Items.RIGHTARM_ITEMS, CyberwareSlot.RARM) || data.hasAnyTagged(ModTags.Items.LEFTLEG_ITEMS, CyberwareSlot.LLEG) || data.hasAnyTagged(ModTags.Items.RIGHTLEG_ITEMS, CyberwareSlot.RLEG))) {
            ((FleshWoundTrigger)((Object)ModCriteria.FLESH_WOUND.get())).trigger(player);
        }
        if (data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.isDyed((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && (tint = data.dyeColor((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN)) == -816214) {
            ((PrettyInPinkTrigger)((Object)ModCriteria.PRETTY_IN_PINK.get())).trigger(player);
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            PlayerCyberwareData data;
            if (!player.hasData(ModAttachments.CYBERWARE) || (data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE)) == null) continue;
            if (data.hasChipwareShardExact((Item)ModItems.DATA_SHARD_RED.get())) {
                ((KungFuTrigger)((Object)ModCriteria.KUNG_FU.get())).trigger(player);
            }
            if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_ARMCANNON.get(), new CyberwareSlot[0])) {
                ((UpgradedTrigger)((Object)ModCriteria.UPGRADED.get())).trigger(player);
            }
            if (data.hasSpecificItem((Item)ModItems.ARMUPGRADES_CLAWS.get(), new CyberwareSlot[0])) {
                ((SniktTrigger)((Object)ModCriteria.SNIKT.get())).trigger(player);
            }
            if (data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_CYBERBRAIN.get(), new CyberwareSlot[0])) {
                ((CogitoErgoSumTrigger)((Object)ModCriteria.COGITO_ERGO_SUM.get())).trigger(player);
            }
            if (!data.hasSpecificItem((Item)ModItems.SKINUPGRADES_FACEPLATE.get(), new CyberwareSlot[0])) continue;
            ((BodysnatcherTrigger)((Object)ModCriteria.BODYSNATCHER.get())).trigger(player);
        }
    }

    @SubscribeEvent
    public static void onFinish(LivingEntityUseItemEvent.Finish event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer)livingEntity;
        ItemStack eaten = event.getItem();
        if (eaten.isEmpty()) {
            return;
        }
        if (eaten.is((Item)ModItems.BONE_MARROW.get())) {
            ((BonesAndAllTrigger)((Object)ModCriteria.BONES_AND_ALL.get())).trigger(player);
        }
    }

    private static boolean isFullBorgSetActive(PlayerCyberwareData data) {
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasMultipleSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE, 3) && data.hasMultipleSpecificItem((Item)ModItems.SKINUPGRADES_SUBDERMALARMOR.get(), CyberwareSlot.SKIN, 3) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM)) {
            return true;
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LUNGSUPGRADES_HYPEROXYGENATION.get(), CyberwareSlot.LUNGS, 3) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_OCELOTPAWS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_CHROMATOPHORES.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_SANDEVISTAN.get(), CyberwareSlot.BONE)) {
            return true;
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES, 3) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_OCELOTPAWS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_CHROMATOPHORES.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_SYNTHETICSETULES.get(), CyberwareSlot.SKIN)) {
            return true;
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_CYBERSKULL.get(), CyberwareSlot.BONE) && data.hasSpecificItem(ModItems.BONEUPGRADES_ELYTRA != null ? (Item)ModItems.BONEUPGRADES_ELYTRA.get() : null, CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG)) {
            return true;
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_PROPELLERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_UNDERWATERVISION.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.LUNGSUPGRADES_OXYGEN.get(), CyberwareSlot.LUNGS) && data.hasSpecificItem((Item)ModItems.WETWARE_WATERBREATHINGLUNGS.get(), CyberwareSlot.LUNGS)) {
            return true;
        }
        if (data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.LEGUPGRADES_METALDETECTOR.get(), CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_MATRIX.get(), CyberwareSlot.BRAIN) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_DRILLFIST.get(), CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM)) {
            return true;
        }
        return data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTARM.get(), CyberwareSlot.RARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTARM.get(), CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_RIGHTLEG.get(), CyberwareSlot.RLEG) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LEFTLEG.get(), CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.SKINUPGRADES_METALPLATING.get(), CyberwareSlot.SKIN) && data.hasSpecificItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get(), CyberwareSlot.MUSCLE) && data.hasSpecificItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get(), CyberwareSlot.HEART) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get(), CyberwareSlot.BONE) && data.hasSpecificItem((Item)ModItems.BASECYBERWARE_CYBEREYES.get(), CyberwareSlot.EYES) && data.hasMultipleSpecificItem((Item)ModItems.BONEUPGRADES_BONELACING.get(), CyberwareSlot.BONE, 3) && data.hasMultipleSpecificItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get(), 2, CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasMultipleSpecificItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get(), 2, CyberwareSlot.RLEG, CyberwareSlot.LLEG) && data.hasSpecificItem((Item)ModItems.ARMUPGRADES_ARMCANNON.get(), CyberwareSlot.RARM, CyberwareSlot.LARM) && data.hasSpecificItem((Item)ModItems.EYEUPGRADES_TARGETING.get(), CyberwareSlot.EYES) && data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_MATRIX.get(), CyberwareSlot.BRAIN) && data.hasSpecificItem((Item)ModItems.BONEUPGRADES_SANDEVISTAN.get(), CyberwareSlot.BONE);
    }
}

