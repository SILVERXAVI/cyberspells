/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.advancements.Advancement$Builder
 *  net.minecraft.advancements.AdvancementHolder
 *  net.minecraft.advancements.AdvancementType
 *  net.minecraft.advancements.critereon.PlayerTrigger$TriggerInstance
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.neoforged.neoforge.common.data.AdvancementProvider
 *  net.neoforged.neoforge.common.data.AdvancementProvider$AdvancementGenerator
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 */
package com.perigrine3.createcybernetics.datagen;

import com.perigrine3.createcybernetics.advancement.triggers.BodysnatcherTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.BonelessTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.BonesAndAllTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.ChestPainsTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.ChromeJunkieTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.CogitoErgoSumTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.CorticalStackTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.CyberpsychosisTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.DavidMartinezSpecialTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.DestroyerOfWorldsTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.DeusExMachinaTrigger;
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
import com.perigrine3.createcybernetics.advancement.triggers.ThoughtlessTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.ThoughtsNotFoundTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.UpgradedTrigger;
import com.perigrine3.createcybernetics.advancement.triggers.WeakFleshTrigger;
import com.perigrine3.createcybernetics.block.ModBlocks;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ModAdvancementProvider
extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new DashAdvancements()));
    }

    private static final class DashAdvancements
    implements AdvancementProvider.AdvancementGenerator {
        private static final ResourceLocation MACHINE_BG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/block/blankmachine.png");
        private static final ResourceLocation TITANIUM_BG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/block/titanium_block.png");
        private static final ResourceLocation RAW_TITANIUM_BG = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/block/raw_titanium_block.png");

        private DashAdvancements() {
        }

        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            AdvancementHolder root = Advancement.Builder.advancement().display(ModItems.BASECYBERWARE_CYBEREYES, (Component)Component.translatable((String)"advancement.cybernetics"), (Component)Component.translatable((String)"advancement.cybernetics.desc"), MACHINE_BG, AdvancementType.TASK, true, true, false).addCriterion("tick", PlayerTrigger.TriggerInstance.tick()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/root"), existingFileHelper);
            AdvancementHolder firstScavenged = Advancement.Builder.advancement().parent(root).display(ModItems.SCAVENGED_LEFTARM, (Component)Component.translatable((String)"advancement.first_scavenged_cyberware"), (Component)Component.translatable((String)"advancement.first_scavenged_cyberware.desc"), MACHINE_BG, AdvancementType.TASK, true, false, false).addCriterion("first_scavenged_cyberware", FirstScavengedCyberwareTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/first_scavenged_cyberware"), existingFileHelper);
            AdvancementHolder firstCyberware = Advancement.Builder.advancement().parent(firstScavenged).display(ModItems.BASECYBERWARE_LEFTARM, (Component)Component.translatable((String)"advancement.first_cyberware"), (Component)Component.translatable((String)"advancement.first_cyberware.desc"), MACHINE_BG, AdvancementType.TASK, true, true, false).addCriterion("first_cyberware", FirstCyberwareTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/first_cyberware"), existingFileHelper);
            AdvancementHolder firstRipperdocVisit = Advancement.Builder.advancement().parent(firstCyberware).display((ItemLike)ModBlocks.ROBOSURGEON.get(), (Component)Component.translatable((String)"advancement.first_ripperdoc_visit"), (Component)Component.translatable((String)"advancement.first_ripperdoc_visit.desc"), MACHINE_BG, AdvancementType.TASK, true, true, false).addCriterion("first_ripperdoc_visit", FirstRipperdocVisitTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/first_ripperdoc_visit"), existingFileHelper);
            AdvancementHolder goingPsycho = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.ARMUPGRADES_CRAFTHANDS.get(), (Component)Component.translatable((String)"advancement.cyberpsychosis"), (Component)Component.translatable((String)"advancement.cyberpsychosis.desc"), MACHINE_BG, AdvancementType.TASK, true, false, false).addCriterion("cyberpsychosis", CyberpsychosisTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/cyberpsychosis"), existingFileHelper);
            AdvancementHolder chromeJunkie = Advancement.Builder.advancement().parent(goingPsycho).display((ItemLike)ModItems.NEUROPOZYNE_AUTOINJECTOR.get(), (Component)Component.translatable((String)"advancement.chrome_junkie"), (Component)Component.translatable((String)"advancement.chrome_junkie.desc"), MACHINE_BG, AdvancementType.TASK, true, false, false).addCriterion("chrome_junkie", ChromeJunkieTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/chrome_junkie"), existingFileHelper);
            AdvancementHolder psychoDeath = Advancement.Builder.advancement().parent(goingPsycho).display((ItemLike)((Block)ModBlocks.SURGERY_CHAMBER_BOTTOM.get()).asItem(), (Component)Component.translatable((String)"advancement.over_the_edge"), (Component)Component.translatable((String)"advancement.over_the_edge.desc"), MACHINE_BG, AdvancementType.TASK, true, false, false).addCriterion("over_the_edge", OverTheEdgeTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/over_the_edge"), existingFileHelper);
            AdvancementHolder fullBorg = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.SKINUPGRADES_METALPLATING.get(), (Component)Component.translatable((String)"advancement.weak_flesh"), (Component)Component.translatable((String)"advancement.weak_flesh.desc"), MACHINE_BG, AdvancementType.TASK, true, true, false).addCriterion("weak_flesh", WeakFleshTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/weak_flesh"), existingFileHelper);
            AdvancementHolder survivedDeath = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.HEARTUPGRADES_DEFIBRILLATOR.get(), (Component)Component.translatable((String)"advancement.deus_ex_machina"), (Component)Component.translatable((String)"advancement.deus_ex_machina.desc"), MACHINE_BG, AdvancementType.TASK, true, false, false).addCriterion("deus_ex_machina", DeusExMachinaTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/deus_ex_machina"), existingFileHelper);
            AdvancementHolder spiderMan = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)Blocks.COBWEB.asItem(), (Component)Component.translatable((String)"advancement.spider_man"), (Component)Component.translatable((String)"advancement.spider_man.desc"), MACHINE_BG, AdvancementType.TASK, true, false, false).addCriterion("spider_man", DeusExMachinaTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/spider_man"), existingFileHelper);
            AdvancementHolder corticalStack = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)Blocks.COBWEB.asItem(), (Component)Component.translatable((String)"advancement.cortical_stack"), (Component)Component.translatable((String)"advancement.cortical_stack.desc"), MACHINE_BG, AdvancementType.TASK, true, false, false).addCriterion("cortical_stack", CorticalStackTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/cortical_stack"), existingFileHelper);
            AdvancementHolder brainRemoved = Advancement.Builder.advancement().display((ItemLike)ModItems.BODYPART_BRAIN.get(), (Component)Component.translatable((String)"advancement.thoughtless"), (Component)Component.translatable((String)"advancement.thoughtless.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("thoughtless", ThoughtlessTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/thoughtless"), existingFileHelper);
            AdvancementHolder heartRemoved = Advancement.Builder.advancement().parent(brainRemoved).display((ItemLike)ModItems.BODYPART_HEART.get(), (Component)Component.translatable((String)"advancement.chest_pains"), (Component)Component.translatable((String)"advancement.chest_pains.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("chest_pains", ChestPainsTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/chest_pains"), existingFileHelper);
            AdvancementHolder liverRemoved = Advancement.Builder.advancement().parent(heartRemoved).display((ItemLike)ModItems.BODYPART_LIVER.get(), (Component)Component.translatable((String)"advancement.liver_removed"), (Component)Component.translatable((String)"advancement.liver_removed.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("liver_removed", LiverRemovedTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/liver_removed"), existingFileHelper);
            AdvancementHolder skinRemoved = Advancement.Builder.advancement().parent(liverRemoved).display((ItemLike)ModItems.BODYPART_SKIN.get(), (Component)Component.translatable((String)"advancement.skin_removed"), (Component)Component.translatable((String)"advancement.skin_removed.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("skin_removed", SkinRemovedTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/skin_removed"), existingFileHelper);
            AdvancementHolder lungsRemoved = Advancement.Builder.advancement().parent(skinRemoved).display((ItemLike)ModItems.BODYPART_LUNGS.get(), (Component)Component.translatable((String)"advancement.lungs_removed"), (Component)Component.translatable((String)"advancement.lungs_removed.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("lungs_removed", LungsRemovedTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/lungs_removed"), existingFileHelper);
            AdvancementHolder skeletonRemoved = Advancement.Builder.advancement().parent(lungsRemoved).display((ItemLike)ModItems.BODYPART_SKELETON.get(), (Component)Component.translatable((String)"advancement.boneless"), (Component)Component.translatable((String)"advancement.boneless.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("boneless", BonelessTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/boneless"), existingFileHelper);
            AdvancementHolder muscleRemoved = Advancement.Builder.advancement().parent(skeletonRemoved).display((ItemLike)ModItems.BODYPART_MUSCLE.get(), (Component)Component.translatable((String)"advancement.missing_muscle"), (Component)Component.translatable((String)"advancement.missing_muscle.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("missing_muscle", MissingMuscleTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/missing_muscle"), existingFileHelper);
            AdvancementHolder limbsRemoved = Advancement.Builder.advancement().parent(muscleRemoved).display((ItemLike)ModItems.BODYPART_LEFTARM.get(), (Component)Component.translatable((String)"advancement.missing_limbs"), (Component)Component.translatable((String)"advancement.missing_limbs.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("missing_limbs", FleshWoundTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/missing_limbs"), existingFileHelper);
            AdvancementHolder davidMartinez = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.BONEUPGRADES_SANDEVISTAN.get(), (Component)Component.translatable((String)"advancement.david_special"), (Component)Component.translatable((String)"advancement.david_special.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("david_special", DavidMartinezSpecialTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/david_special"), existingFileHelper);
            AdvancementHolder kungFu = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.DATA_SHARD_RED.get(), (Component)Component.translatable((String)"advancement.kung_fu"), (Component)Component.translatable((String)"advancement.kung_fu.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("kung_fu", KungFuTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/kung_fu"), existingFileHelper);
            AdvancementHolder armCannon = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.ARMUPGRADES_ARMCANNON.get(), (Component)Component.translatable((String)"advancement.upgraded"), (Component)Component.translatable((String)"advancement.upgraded.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("upgraded", UpgradedTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/upgraded"), existingFileHelper);
            AdvancementHolder wolverineClaws = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.ARMUPGRADES_CLAWS.get(), (Component)Component.translatable((String)"advancement.snikt"), (Component)Component.translatable((String)"advancement.snikt.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("snikt", SniktTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/snikt"), existingFileHelper);
            AdvancementHolder digitalBrain = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.BRAINUPGRADES_CYBERBRAIN.get(), (Component)Component.translatable((String)"advancement.cogito_ergo_sum"), (Component)Component.translatable((String)"advancement.cogito_ergo_sum.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("cogito_ergo_sum", CogitoErgoSumTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/cogito_ergo_sum"), existingFileHelper);
            AdvancementHolder brainOff = Advancement.Builder.advancement().parent(digitalBrain).display((ItemLike)ModItems.BRAINUPGRADES_CYBERBRAIN.get(), (Component)Component.translatable((String)"advancement.thoughts_not_found"), (Component)Component.translatable((String)"advancement.thoughts_not_found.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("thoughts_not_found", ThoughtsNotFoundTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/thoughts_not_found"), existingFileHelper);
            AdvancementHolder prettyPink = Advancement.Builder.advancement().parent(fullBorg).display((ItemLike)ModItems.BRAINUPGRADES_CYBERBRAIN.get(), (Component)Component.translatable((String)"advancement.pretty_in_pink"), (Component)Component.translatable((String)"advancement.pretty_in_pink.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("pretty_in_pink", PrettyInPinkTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/pretty_in_pink"), existingFileHelper);
            AdvancementHolder letsDance = Advancement.Builder.advancement().parent(root).display((ItemLike)ModItems.BODYPART_BRAIN.get(), (Component)Component.translatable((String)"advancement.lets_dance"), (Component)Component.translatable((String)"advancement.lets_dance.desc"), TITANIUM_BG, AdvancementType.TASK, true, true, false).addCriterion("lets_dance", LetsDanceTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/lets_dance"), existingFileHelper);
            AdvancementHolder bodySnatcher = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.FACEPLATE.get(), (Component)Component.translatable((String)"advancement.bodysnatcher"), (Component)Component.translatable((String)"advancement.bodysnatcher.desc"), TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("bodysnatcher", BodysnatcherTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/bodysnatcher"), existingFileHelper);
            AdvancementHolder destroyerOfWorlds = Advancement.Builder.advancement().parent(firstRipperdocVisit).display((ItemLike)ModItems.HEARTUPGRADES_CREEPERHEART.get(), (Component)Component.translatable((String)"advancement.destroyer_of_worlds"), (Component)Component.translatable((String)"advancement.destroyer_of_worlds.desc"), TITANIUM_BG, AdvancementType.TASK, true, true, false).addCriterion("destroyer_of_worlds", DestroyerOfWorldsTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/destroyer_of_worlds"), existingFileHelper);
            AdvancementHolder boneMarrow = Advancement.Builder.advancement().display((ItemLike)ModItems.BONE_MARROW.get(), (Component)Component.translatable((String)"advancement.bones_and_all"), (Component)Component.translatable((String)"advancement.bones_and_all.desc"), RAW_TITANIUM_BG, AdvancementType.TASK, true, false, false).addCriterion("bones_and_all", BonesAndAllTrigger.Instance.any()).save(saver, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberware/bones_and_all"), existingFileHelper);
        }
    }
}

