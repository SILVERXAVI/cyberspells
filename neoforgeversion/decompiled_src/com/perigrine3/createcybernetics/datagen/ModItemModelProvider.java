/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.data.PackOutput
 *  net.minecraft.world.item.Item
 *  net.neoforged.neoforge.client.model.generators.ItemModelProvider
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 */
package com.perigrine3.createcybernetics.datagen;

import com.perigrine3.createcybernetics.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider
extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "createcybernetics", existingFileHelper);
    }

    protected void registerModels() {
        this.basicItem((Item)ModItems.TITANIUMINGOT.get());
        this.basicItem((Item)ModItems.TITANIUMSHEET.get());
        this.basicItem((Item)ModItems.TITANIUMNUGGET.get());
        this.basicItem((Item)ModItems.RAWTITANIUM.get());
        this.basicItem((Item)ModItems.CRUSHEDTITANIUM.get());
        this.basicItem((Item)ModItems.EYEUPGRADEBASE.get());
        this.basicItem((Item)ModItems.TITANIUM_HAND.get());
        this.basicItem((Item)ModItems.HOLOIMPRINT_CHIP.get());
        this.basicItem((Item)ModItems.FRONTAL_LOBE.get());
        this.basicItem((Item)ModItems.PARIETAL_LOBE.get());
        this.basicItem((Item)ModItems.TEMPORAL_LOBE.get());
        this.basicItem((Item)ModItems.OCCIPITAL_LOBE.get());
        this.basicItem((Item)ModItems.CEREBELLUM.get());
        this.basicItem((Item)ModItems.NEUROPOZYNE_AUTOINJECTOR.get());
        this.basicItem((Item)ModItems.EMPTY_AUTOINJECTOR.get());
        this.basicItem((Item)ModItems.XP_CAPSULE.get());
        this.basicItem((Item)ModItems.FACEPLATE.get());
        this.basicItem((Item)ModItems.NETHERITE_QPU.get());
        this.basicItem((Item)ModItems.DATURA_FLOWER.get());
        this.basicItem((Item)ModItems.DATURA_SEED_POD.get());
        this.basicItem((Item)ModItems.COPPER_UPGRADE_TEMPLATE.get());
        this.basicItem((Item)ModItems.IRON_UPGRADE_TEMPLATE.get());
        this.basicItem((Item)ModItems.GOLD_UPGRADE_TEMPLATE.get());
        this.basicItem((Item)ModItems.MUSIC_DISC_CYBERPSYCHO.get());
        this.basicItem((Item)ModItems.MUSIC_DISC_NEON_OVERLORDS.get());
        this.basicItem((Item)ModItems.MUSIC_DISC_NEUROHACK.get());
        this.basicItem((Item)ModItems.MUSIC_DISC_THE_GRID.get());
        this.basicItem((Item)ModItems.COOKED_BRAIN.get());
        this.basicItem((Item)ModItems.COOKED_HEART.get());
        this.basicItem((Item)ModItems.COOKED_LIVER.get());
        this.basicItem((Item)ModItems.BONE_MARROW.get());
        if (ModItems.ANDOUILLE_SAUSAGE != null && ModItems.ROASTED_ANDOUILLE != null && ModItems.GROUND_OFFAL != null && ModItems.BRAIN_STEW != null) {
            this.basicItem((Item)ModItems.ANDOUILLE_SAUSAGE.get());
            this.basicItem((Item)ModItems.ROASTED_ANDOUILLE.get());
            this.basicItem((Item)ModItems.GROUND_OFFAL.get());
            this.basicItem((Item)ModItems.BRAIN_STEW.get());
        }
        this.withExistingParent(ModItems.SMASHER_SPAWN_EGG.getId().getPath(), this.mcLoc("item/template_spawn_egg"));
        this.withExistingParent(ModItems.CYBERZOMBIE_SPAWN_EGG.getId().getPath(), this.mcLoc("item/template_spawn_egg"));
        this.withExistingParent(ModItems.CYBERSKELETON_SPAWN_EGG.getId().getPath(), this.mcLoc("item/template_spawn_egg"));
        this.basicItem((Item)ModItems.COMPONENT_ACTUATOR.get());
        this.basicItem((Item)ModItems.COMPONENT_FIBEROPTICS.get());
        this.basicItem((Item)ModItems.COMPONENT_WIRING.get());
        this.basicItem((Item)ModItems.COMPONENT_DIODES.get());
        this.basicItem((Item)ModItems.COMPONENT_PLATING.get());
        this.basicItem((Item)ModItems.COMPONENT_GRAPHICSCARD.get());
        this.basicItem((Item)ModItems.COMPONENT_SSD.get());
        this.basicItem((Item)ModItems.COMPONENT_STORAGE.get());
        this.basicItem((Item)ModItems.COMPONENT_SYNTHNERVES.get());
        this.basicItem((Item)ModItems.COMPONENT_MESH.get());
        if (ModItems.COMPONENT_LED != null && ModItems.COMPONENT_TITANIUMROD != null) {
            this.basicItem((Item)ModItems.COMPONENT_LED.get());
            this.basicItem((Item)ModItems.COMPONENT_TITANIUMROD.get());
        }
        this.basicItem((Item)ModItems.BODYPART_RIGHTLEG.get());
        this.basicItem((Item)ModItems.BODYPART_LEFTLEG.get());
        this.basicItem((Item)ModItems.BODYPART_RIGHTARM.get());
        this.basicItem((Item)ModItems.BODYPART_LEFTARM.get());
        this.basicItem((Item)ModItems.BODYPART_SKELETON.get());
        this.basicItem((Item)ModItems.BODYPART_BRAIN.get());
        this.basicItem((Item)ModItems.BODYPART_EYEBALLS.get());
        this.basicItem((Item)ModItems.BODYPART_HEART.get());
        this.basicItem((Item)ModItems.BODYPART_LUNGS.get());
        this.basicItem((Item)ModItems.BODYPART_LIVER.get());
        this.basicItem((Item)ModItems.BODYPART_INTESTINES.get());
        this.basicItem((Item)ModItems.BODYPART_MUSCLE.get());
        this.basicItem((Item)ModItems.BODYPART_SKIN.get());
        this.basicItem((Item)ModItems.BODYPART_GUARDIANRETINA.get());
        this.basicItem((Item)ModItems.BODYPART_WARDENESOPHAGUS.get());
        this.basicItem((Item)ModItems.BODYPART_GYROSCOPICBLADDER.get());
        this.basicItem((Item)ModItems.BODYPART_SPINNERETTE.get());
        this.basicItem((Item)ModItems.BODYPART_FIREGLAND.get());
        this.basicItem((Item)ModItems.BODYPART_GILLS.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_LINEARFRAME.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_LEFTARM_COPPERPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_RIGHTARM_COPPERPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_LEFTLEG_COPPERPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_RIGHTLEG_COPPERPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_LEFTARM_IRONPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_RIGHTARM_IRONPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_LEFTLEG_IRONPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_RIGHTLEG_IRONPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_LEFTARM_GOLDPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_RIGHTARM_GOLDPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_LEFTLEG_GOLDPLATED.get());
        this.basicItem((Item)ModItems.BASECYBERWARE_RIGHTLEG_GOLDPLATED.get());
        this.basicItem((Item)ModItems.EYEUPGRADES_HUDLENS.get());
        if (ModItems.EYEUPGRADES_NAVIGATIONCHIP != null) {
            this.basicItem((Item)ModItems.EYEUPGRADES_NAVIGATIONCHIP.get());
        }
        this.basicItem((Item)ModItems.EYEUPGRADES_HUDJACK.get());
        this.basicItem((Item)ModItems.EYEUPGRADES_NIGHTVISION.get());
        this.basicItem((Item)ModItems.EYEUPGRADES_TARGETING.get());
        this.basicItem((Item)ModItems.EYEUPGRADES_UNDERWATERVISION.get());
        this.basicItem((Item)ModItems.EYEUPGRADES_ZOOM.get());
        this.basicItem((Item)ModItems.EYEUPGRADES_TRAJECTORYCALCULATOR.get());
        this.basicItem((Item)ModItems.ARMUPGRADES_ARMCANNON.get());
        this.basicItem((Item)ModItems.ARMUPGRADES_FLYWHEEL.get());
        this.basicItem((Item)ModItems.ARMUPGRADES_CLAWS.get());
        this.basicItem((Item)ModItems.ARMUPGRADES_CRAFTHANDS.get());
        this.basicItem((Item)ModItems.ARMUPGRADES_DRILLFIST.get());
        this.basicItem((Item)ModItems.ARMUPGRADES_FIRESTARTER.get());
        this.basicItem((Item)ModItems.ARMUPGRADES_PNEUMATICWRIST.get());
        this.basicItem((Item)ModItems.ARMUPGRADES_REINFORCEDKNUCKLES.get());
        this.basicItem((Item)ModItems.LEGUPGRADES_METALDETECTOR.get());
        this.basicItem((Item)ModItems.LEGUPGRADES_ANKLEBRACERS.get());
        this.basicItem((Item)ModItems.LEGUPGRADES_JUMPBOOST.get());
        this.basicItem((Item)ModItems.LEGUPGRADES_PROPELLERS.get());
        this.basicItem((Item)ModItems.LEGUPGRADES_SPURS.get());
        this.basicItem((Item)ModItems.BONEUPGRADES_BONEBATTERY.get());
        this.basicItem((Item)ModItems.BONEUPGRADES_BONEFLEX.get());
        this.basicItem((Item)ModItems.BONEUPGRADES_BONELACING.get());
        if (ModItems.BONEUPGRADES_ELYTRA != null) {
            this.basicItem((Item)ModItems.BONEUPGRADES_ELYTRA.get());
        }
        this.basicItem((Item)ModItems.BONEUPGRADES_PIEZO.get());
        this.basicItem((Item)ModItems.BONEUPGRADES_SPINALINJECTOR.get());
        this.basicItem((Item)ModItems.BONEUPGRADES_SANDEVISTAN.get());
        this.basicItem((Item)ModItems.BONEUPGRADES_CYBERSKULL.get());
        this.basicItem((Item)ModItems.BRAINUPGRADES_CYBERBRAIN.get());
        this.basicItem((Item)ModItems.BRAINUPGRADES_EYEOFDEFENDER.get());
        if (ModItems.BRAINUPGRADES_CONSCIOUSNESSTRANSMITTER != null && ModItems.BRAINUPGRADES_CORTICALSTACK != null) {
            this.basicItem((Item)ModItems.BRAINUPGRADES_CONSCIOUSNESSTRANSMITTER.get());
            this.basicItem((Item)ModItems.BRAINUPGRADES_CORTICALSTACK.get());
        }
        this.basicItem((Item)ModItems.BRAINUPGRADES_ENDERJAMMER.get());
        this.basicItem((Item)ModItems.BRAINUPGRADES_MATRIX.get());
        this.basicItem((Item)ModItems.BRAINUPGRADES_NEURALCONTEXTUALIZER.get());
        this.basicItem((Item)ModItems.BRAINUPGRADES_CYBERDECK.get());
        this.basicItem((Item)ModItems.BRAINUPGRADES_IDEM.get());
        this.basicItem((Item)ModItems.BRAINUPGRADES_CHIPWARESLOTS.get());
        if (ModItems.BRAINUPGRADES_SPELLJAMMER != null) {
            this.basicItem((Item)ModItems.BRAINUPGRADES_SPELLJAMMER.get());
        }
        this.basicItem((Item)ModItems.HEARTUPGRADES_CYBERHEART.get());
        this.basicItem((Item)ModItems.HEARTUPGRADES_COUPLER.get());
        this.basicItem((Item)ModItems.HEARTUPGRADES_CREEPERHEART.get());
        this.basicItem((Item)ModItems.HEARTUPGRADES_DEFIBRILLATOR.get());
        this.basicItem((Item)ModItems.HEARTUPGRADES_STEMCELL.get());
        this.basicItem((Item)ModItems.HEARTUPGRADES_PLATELETS.get());
        this.basicItem((Item)ModItems.LUNGSUPGRADES_HYPEROXYGENATION.get());
        this.basicItem((Item)ModItems.LUNGSUPGRADES_OXYGEN.get());
        this.basicItem((Item)ModItems.ORGANSUPGRADES_ADRENALINE.get());
        this.basicItem((Item)ModItems.ORGANSUPGRADES_BATTERY.get());
        this.basicItem((Item)ModItems.ORGANSUPGRADES_DIAMONDWAFERSTACK.get());
        this.basicItem((Item)ModItems.ORGANSUPGRADES_DUALISTICCONVERTER.get());
        this.basicItem((Item)ModItems.ORGANSUPGRADES_LIVERFILTER.get());
        this.basicItem((Item)ModItems.ORGANSUPGRADES_MAGICCATALYST.get());
        this.basicItem((Item)ModItems.ORGANSUPGRADES_METABOLIC.get());
        this.basicItem((Item)ModItems.ORGANSUPGRADES_DENSEBATTERY.get());
        this.basicItem((Item)ModItems.ORGANSUPGRADES_HEATENGINE.get());
        if (ModItems.ORGANSUPGRADES_MANABATTERY != null) {
            this.basicItem((Item)ModItems.ORGANSUPGRADES_MANABATTERY.get());
        }
        this.basicItem((Item)ModItems.SKINUPGRADES_ARTERIALTURBINE.get());
        this.basicItem((Item)ModItems.SKINUPGRADES_CHROMATOPHORES.get());
        this.basicItem((Item)ModItems.SKINUPGRADES_SYNTHSKIN.get());
        this.basicItem((Item)ModItems.SKINUPGRADES_IMMUNO.get());
        this.basicItem((Item)ModItems.SKINUPGRADES_FACEPLATE.get());
        this.basicItem((Item)ModItems.SKINUPGRADES_NETHERITEPLATING.get());
        this.basicItem((Item)ModItems.SKINUPGRADES_SOLARSKIN.get());
        this.basicItem((Item)ModItems.SKINUPGRADES_SUBDERMALARMOR.get());
        this.basicItem((Item)ModItems.SKINUPGRADES_SUBDERMALSPIKES.get());
        this.basicItem((Item)ModItems.SKINUPGRADES_SYNTHETICSETULES.get());
        if (ModItems.SKINUPGRADES_SWEAT != null) {
            this.basicItem((Item)ModItems.SKINUPGRADES_SWEAT.get());
        }
        if (ModItems.SKINUPGRADES_MANASKIN != null) {
            this.basicItem((Item)ModItems.SKINUPGRADES_MANASKIN.get());
        }
        this.basicItem((Item)ModItems.MUSCLEUPGRADES_SYNTHMUSCLE.get());
        this.basicItem((Item)ModItems.MUSCLEUPGRADES_WIREDREFLEXES.get());
        if (ModItems.WETWARE_BLUBBER != null) {
            this.basicItem((Item)ModItems.WETWARE_BLUBBER.get());
        }
        this.basicItem((Item)ModItems.WETWARE_FIREBREATHINGLUNGS.get());
        this.basicItem((Item)ModItems.WETWARE_WATERBREATHINGLUNGS.get());
        this.basicItem((Item)ModItems.WETWARE_GUARDIANEYE.get());
        this.basicItem((Item)ModItems.WETWARE_POLARBEARFUR.get());
        this.basicItem((Item)ModItems.WETWARE_RAVAGERTENDONS.get());
        this.basicItem((Item)ModItems.WETWARE_SCULKLUNGS.get());
        this.basicItem((Item)ModItems.WETWARE_TACTICALINKSAC.get());
        this.basicItem((Item)ModItems.WETWARE_AEROSTASISGYROBLADDER.get());
        this.basicItem((Item)ModItems.WETWARE_GRASSFEDSTOMACH.get());
        this.basicItem((Item)ModItems.WETWARE_WEBSHOOTINGINTESTINES.get());
        this.basicItem((Item)ModItems.WETWARE_WEBSHOOTING_RIGHTARM.get());
        this.basicItem((Item)ModItems.WETWARE_WEBSHOOTING_LEFTARM.get());
        this.basicItem((Item)ModItems.WETWARE_SPIDEREYES.get());
        this.basicItem((Item)ModItems.SCAVENGED_RIGHTLEG.get());
        this.basicItem((Item)ModItems.SCAVENGED_LEFTLEG.get());
        this.basicItem((Item)ModItems.SCAVENGED_RIGHTARM.get());
        this.basicItem((Item)ModItems.SCAVENGED_LEFTARM.get());
        this.basicItem((Item)ModItems.SCAVENGED_CYBEREYES.get());
        this.basicItem((Item)ModItems.SCAVENGED_LINEARFRAME.get());
        this.basicItem((Item)ModItems.SCAVENGED_HUDLENS.get());
        if (ModItems.SCAVENGED_NAVIGATIONCHIP != null) {
            this.basicItem((Item)ModItems.SCAVENGED_NAVIGATIONCHIP.get());
        }
        this.basicItem((Item)ModItems.SCAVENGED_HUDJACK.get());
        this.basicItem((Item)ModItems.SCAVENGED_NIGHTVISION.get());
        this.basicItem((Item)ModItems.SCAVENGED_TARGETING.get());
        this.basicItem((Item)ModItems.SCAVENGED_UNDERWATERVISION.get());
        this.basicItem((Item)ModItems.SCAVENGED_ZOOM.get());
        this.basicItem((Item)ModItems.SCAVENGED_TRAJECTORYCALCULATOR.get());
        this.basicItem((Item)ModItems.SCAVENGED_ARMCANNON.get());
        this.basicItem((Item)ModItems.SCAVENGED_FLYWHEEL.get());
        this.basicItem((Item)ModItems.SCAVENGED_CLAWS.get());
        this.basicItem((Item)ModItems.SCAVENGED_CRAFTHANDS.get());
        this.basicItem((Item)ModItems.SCAVENGED_DRILLFIST.get());
        this.basicItem((Item)ModItems.SCAVENGED_FIRESTARTER.get());
        this.basicItem((Item)ModItems.SCAVENGED_PNEUMATICWRIST.get());
        this.basicItem((Item)ModItems.SCAVENGED_REINFORCEDKNUCKLES.get());
        this.basicItem((Item)ModItems.SCAVENGED_METALDETECTOR.get());
        this.basicItem((Item)ModItems.SCAVENGED_ANKLEBRACERS.get());
        this.basicItem((Item)ModItems.SCAVENGED_JUMPBOOST.get());
        this.basicItem((Item)ModItems.SCAVENGED_PROPELLERS.get());
        this.basicItem((Item)ModItems.SCAVENGED_SPURS.get());
        this.basicItem((Item)ModItems.SCAVENGED_OCELOTPAWS.get());
        this.basicItem((Item)ModItems.SCAVENGED_BONEBATTERY.get());
        this.basicItem((Item)ModItems.SCAVENGED_BONEFLEX.get());
        this.basicItem((Item)ModItems.SCAVENGED_BONELACING.get());
        if (ModItems.SCAVENGED_ELYTRA != null) {
            this.basicItem((Item)ModItems.SCAVENGED_ELYTRA.get());
        }
        this.basicItem((Item)ModItems.SCAVENGED_PIEZO.get());
        this.basicItem((Item)ModItems.SCAVENGED_SPINALINJECTOR.get());
        this.basicItem((Item)ModItems.SCAVENGED_SANDEVISTAN.get());
        this.basicItem((Item)ModItems.SCAVENGED_EYEOFDEFENDER.get());
        if (ModItems.SCAVENGED_CONSCIOUSNESSTRANSMITTER != null && ModItems.SCAVENGED_CORTICALSTACK != null) {
            this.basicItem((Item)ModItems.SCAVENGED_CONSCIOUSNESSTRANSMITTER.get());
            this.basicItem((Item)ModItems.SCAVENGED_CORTICALSTACK.get());
        }
        this.basicItem((Item)ModItems.SCAVENGED_ENDERJAMMER.get());
        this.basicItem((Item)ModItems.SCAVENGED_MATRIX.get());
        this.basicItem((Item)ModItems.SCAVENGED_NEURALCONTEXTUALIZER.get());
        this.basicItem((Item)ModItems.SCAVENGED_CYBERDECK.get());
        this.basicItem((Item)ModItems.SCAVENGED_IDEM.get());
        this.basicItem((Item)ModItems.SCAVENGED_CHIPWARESLOTS.get());
        this.basicItem((Item)ModItems.SCAVENGED_CYBERHEART.get());
        this.basicItem((Item)ModItems.SCAVENGED_COUPLER.get());
        this.basicItem((Item)ModItems.SCAVENGED_CREEPERHEART.get());
        this.basicItem((Item)ModItems.SCAVENGED_DEFIBRILLATOR.get());
        this.basicItem((Item)ModItems.SCAVENGED_STEMCELL.get());
        this.basicItem((Item)ModItems.SCAVENGED_PLATELETS.get());
        this.basicItem((Item)ModItems.SCAVENGED_HYPEROXYGENATION.get());
        this.basicItem((Item)ModItems.SCAVENGED_OXYGEN.get());
        this.basicItem((Item)ModItems.SCAVENGED_ADRENALINE.get());
        this.basicItem((Item)ModItems.SCAVENGED_BATTERY.get());
        this.basicItem((Item)ModItems.SCAVENGED_DIAMONDWAFERSTACK.get());
        this.basicItem((Item)ModItems.SCAVENGED_DUALISTICCONVERTER.get());
        this.basicItem((Item)ModItems.SCAVENGED_LIVERFILTER.get());
        this.basicItem((Item)ModItems.SCAVENGED_METABOLIC.get());
        this.basicItem((Item)ModItems.SCAVENGED_DENSEBATTERY.get());
        this.basicItem((Item)ModItems.SCAVENGED_HEATENGINE.get());
        this.basicItem((Item)ModItems.SCAVENGED_ARTERIALTURBINE.get());
        this.basicItem((Item)ModItems.SCAVENGED_CHROMATOPHORES.get());
        this.basicItem((Item)ModItems.SCAVENGED_SYNTHSKIN.get());
        this.basicItem((Item)ModItems.SCAVENGED_IMMUNO.get());
        this.basicItem((Item)ModItems.SCAVENGED_FACEPLATE.get());
        this.basicItem((Item)ModItems.SCAVENGED_NETHERITEPLATING.get());
        this.basicItem((Item)ModItems.SCAVENGED_SOLARSKIN.get());
        this.basicItem((Item)ModItems.SCAVENGED_SUBDERMALARMOR.get());
        this.basicItem((Item)ModItems.SCAVENGED_SUBDERMALSPIKES.get());
        this.basicItem((Item)ModItems.SCAVENGED_SYNTHETICSETULES.get());
        this.basicItem((Item)ModItems.SCAVENGED_METALPLATING.get());
        if (ModItems.SCAVENGED_SWEAT != null) {
            this.basicItem((Item)ModItems.SCAVENGED_SWEAT.get());
        }
        this.basicItem((Item)ModItems.SCAVENGED_SYNTHMUSCLE.get());
        this.basicItem((Item)ModItems.SCAVENGED_WIREDREFLEXES.get());
        this.basicItem((Item)ModItems.DATA_SHARD_RED.get());
        this.basicItem((Item)ModItems.DATA_SHARD_ORANGE.get());
        this.basicItem((Item)ModItems.DATA_SHARD_YELLOW.get());
        this.basicItem((Item)ModItems.DATA_SHARD_GREEN.get());
        this.basicItem((Item)ModItems.DATA_SHARD_CYAN.get());
        this.basicItem((Item)ModItems.DATA_SHARD_BLUE.get());
        this.basicItem((Item)ModItems.DATA_SHARD_PURPLE.get());
        this.basicItem((Item)ModItems.DATA_SHARD_PINK.get());
        this.basicItem((Item)ModItems.DATA_SHARD_BROWN.get());
        this.basicItem((Item)ModItems.DATA_SHARD_GRAY.get());
        this.basicItem((Item)ModItems.DATA_SHARD_BLACK.get());
        this.basicItem((Item)ModItems.DATA_SHARD_BIOCHIP.get());
    }
}

