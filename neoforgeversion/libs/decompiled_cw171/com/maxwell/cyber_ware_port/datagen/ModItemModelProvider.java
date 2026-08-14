/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.data.PackOutput
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.neoforged.neoforge.client.model.generators.ItemModelBuilder
 *  net.neoforged.neoforge.client.model.generators.ItemModelProvider
 *  net.neoforged.neoforge.client.model.generators.ModelFile
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  net.neoforged.neoforge.registries.DeferredHolder
 */
package com.maxwell.cyber_ware_port.datagen;

import com.maxwell.cyber_ware_port.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModItemModelProvider
extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "cyber_ware_port", existingFileHelper);
    }

    protected void registerModels() {
        this.simpleItem(ModItems.BLUEPRINT);
        this.simpleItem(ModItems.EXP_CAPSULE);
        this.simpleItem(ModItems.CREATIVE_BATTERY);
        this.simpleItem(ModItems.COMPONENT_ACTUATOR);
        this.simpleItem(ModItems.COMPONENT_REACTOR);
        this.simpleItem(ModItems.COMPONENT_TITANIUM);
        this.simpleItem(ModItems.COMPONENT_SSC);
        this.simpleItem(ModItems.COMPONENT_PLATING);
        this.simpleItem(ModItems.COMPONENT_FIBEROPTICS);
        this.simpleItem(ModItems.COMPONENT_FULLERENE);
        this.simpleItem(ModItems.COMPONENT_SYNTHNERVES);
        this.simpleItem(ModItems.COMPONENT_STORAGE);
        this.simpleItem(ModItems.COMPONENT_MICROELECTRIC);
        this.simpleItem(ModItems.NEUROPOZYNE);
        this.withExistingParent("component_box", ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"block/component_box"));
        this.katanaItem(ModItems.KATANA);
        this.simpleItem(ModItems.HUMAN_BRAIN);
        this.simpleItem(ModItems.HUMAN_HEART);
        this.simpleItem(ModItems.HUMAN_STOMACH);
        this.simpleItem(ModItems.HUMAN_SKIN);
        this.simpleItem(ModItems.HUMAN_MUSCLE);
        this.simpleItem(ModItems.HUMAN_BONE);
        this.simpleItem(ModItems.HUMAN_EYES);
        this.simpleItem(ModItems.HUMAN_LUNGS);
        this.simpleItem(ModItems.HUMAN_LEFT_ARM);
        this.simpleItem(ModItems.HUMAN_RIGHT_ARM);
        this.simpleItem(ModItems.HUMAN_LEFT_HAND);
        this.simpleItem(ModItems.HUMAN_RIGHT_HAND);
        this.simpleItem(ModItems.HUMAN_LEFT_LEG);
        this.simpleItem(ModItems.HUMAN_RIGHT_LEG);
        this.simpleItem(ModItems.HUMAN_LEFT_FOOT);
        this.simpleItem(ModItems.HUMAN_RIGHT_FOOT);
        this.cyberwareItem(ModItems.CYBER_EYE);
        this.cyberwareItem(ModItems.LOW_LIGHT_VISION);
        this.cyberwareItem(ModItems.LIQUID_REFRACTION);
        this.cyberwareItem(ModItems.HUDJACK);
        this.cyberwareItem(ModItems.TARGETING_OVERLAY);
        this.cyberwareItem(ModItems.DISTANCE_ENHANCER);
        this.cyberwareItem(ModItems.COMPRESSED_OXYGEN);
        this.cyberwareItem(ModItems.HYPER_OXYGENATION);
        this.cyberwareItem(ModItems.RAPID_FIRE_FLYWHEEL);
        this.cyberwareItem(ModItems.IMPLANTED_SPURS);
        this.cyberwareItem(ModItems.FINE_MANIPULATORS);
        this.cyberwareItem(ModItems.LIVER_FILTER);
        this.cyberwareItem(ModItems.METABOLIC_GENERATOR);
        this.cyberwareItem(ModItems.INTERNAL_BATTERY);
        this.cyberwareItem(ModItems.ADRENALINE_PUMP);
        this.cyberwareItem(ModItems.DENSE_BATTERY);
        this.cyberwareItem(ModItems.CORTICAL_STACK);
        this.cyberwareItem(ModItems.ENDER_JAMMER);
        this.cyberwareItem(ModItems.CONSCIOUSNESS_TRANSMITTER);
        this.cyberwareItem(ModItems.NEURAL_CONTEXTUALIZER);
        this.cyberwareItem(ModItems.THREAT_MATRIX);
        this.cyberwareItem(ModItems.CRANIAL_BROADCASTER);
        this.cyberwareItem(ModItems.CARDIOMECHANIC_PUMP);
        this.cyberwareItem(ModItems.INTERNAL_DEFIBRILLATOR);
        this.cyberwareItem(ModItems.PLATELET_DISPATCHER);
        this.cyberwareItem(ModItems.STEM_CELL_SYNTHESIZER);
        this.cyberwareItem(ModItems.CARDIOVASCULAR_COUPLER);
        this.cyberwareItem(ModItems.SOLARSKIN);
        this.cyberwareItem(ModItems.SUBDERMAL_SPIKES);
        this.cyberwareItem(ModItems.SYNTHETIC_SKIN);
        this.cyberwareItem(ModItems.TARGETED_IMMUNOSUPPRESSANT);
        this.cyberwareItem(ModItems.WIRED_REFLEXES);
        this.cyberwareItem(ModItems.MYOMER_MUSCLE);
        this.cyberwareItem(ModItems.BONELACING);
        this.cyberwareItem(ModItems.CITRATE_ENHANCEMENT);
        this.cyberwareItem(ModItems.MARROW_BATTERY);
        this.cyberwareItem(ModItems.CYBER_ARM_LEFT);
        this.cyberwareItem(ModItems.CYBER_ARM_RIGHT);
        this.cyberwareItem(ModItems.RETRACTABLE_CLAWS);
        this.cyberwareItem(ModItems.REINFORCED_FIST);
        this.cyberwareItem(ModItems.CYBER_LEG_LEFT);
        this.cyberwareItem(ModItems.CYBER_LEG_RIGHT);
        this.cyberwareItem(ModItems.LINEAR_ACTUATORS);
        this.cyberwareItem(ModItems.FALL_BRACERS);
        this.cyberwareItem(ModItems.AQUATIC_PROPULSION);
        this.cyberwareItem(ModItems.DEPLOYABLE_WHEELS);
    }

    private void simpleItem(DeferredHolder<Item, ? extends Item> item) {
        String path = item.getId().getPath();
        ((ItemModelBuilder)this.withExistingParent(path, ResourceLocation.withDefaultNamespace((String)"item/generated"))).texture("layer0", ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)("item/" + path)));
    }

    private void cyberwareItem(DeferredHolder<Item, ? extends Item> item) {
        String path = item.getId().getPath();
        ResourceLocation standardTexture = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)("item/" + path));
        ResourceLocation scavengedTexture = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)("item/" + path + "_scavenged"));
        ItemModelBuilder scavengedModel = (ItemModelBuilder)((ItemModelBuilder)this.withExistingParent(path + "_scavenged", ResourceLocation.withDefaultNamespace((String)"item/generated"))).texture("layer0", scavengedTexture);
        ((ItemModelBuilder)((ItemModelBuilder)this.withExistingParent(path, ResourceLocation.withDefaultNamespace((String)"item/generated"))).texture("layer0", standardTexture)).override().predicate(ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"is_scavenged"), 1.0f).model((ModelFile)scavengedModel).end();
    }

    private void katanaItem(DeferredHolder<Item, ? extends Item> item) {
        String path = item.getId().getPath();
        ((ItemModelBuilder)((ItemModelBuilder)this.withExistingParent(path, ResourceLocation.withDefaultNamespace((String)"item/handheld"))).texture("layer0", ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)("item/" + path)))).transforms().transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0.0f, -90.0f, 55.0f).translation(0.0f, 6.5f, 0.5f).scale(0.85f).end().transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0.0f, 90.0f, 55.0f).translation(0.0f, 6.5f, 0.5f).scale(0.85f).end().transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0.0f, -90.0f, 25.0f).translation(1.13f, 3.2f, 1.13f).scale(0.68f).end().transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0.0f, 90.0f, 25.0f).translation(1.13f, 3.2f, 1.13f).scale(0.68f).end().end();
    }
}

