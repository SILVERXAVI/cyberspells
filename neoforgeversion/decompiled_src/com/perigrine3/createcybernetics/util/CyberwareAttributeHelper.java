/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.neoforged.neoforge.common.NeoForgeMod
 *  net.neoforged.neoforge.registries.DeferredHolder
 */
package com.perigrine3.createcybernetics.util;

import com.perigrine3.createcybernetics.CreateCybernetics;
import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CyberwareAttributeHelper {
    private static final Map<String, AttributeModifierData> MODIFIER_REGISTRY = new HashMap<String, AttributeModifierData>();

    public static void registerModifier(String id, AttributeModifierData data) {
        MODIFIER_REGISTRY.put(id, data);
    }

    public static void registerModifierDynamicAttribute(String id, ResourceLocation attributeId, ResourceLocation modifierName, double amount, AttributeModifier.Operation op) {
        CyberwareAttributeHelper.registerModifier(id, new AttributeModifierData(attributeId, modifierName, amount, op));
    }

    public static void applyModifier(LivingEntity entity, String modifierId) {
        AttributeModifierData data = MODIFIER_REGISTRY.get(modifierId);
        if (data == null) {
            CreateCybernetics.LOGGER.error("Attempted to apply unknown modifier: " + modifierId);
            return;
        }
        Holder<Attribute> attr = data.resolveAttribute();
        if (attr == null) {
            return;
        }
        CyberwareAttributeHelper.removeModifier(entity, modifierId);
        AttributeInstance inst = entity.getAttribute(attr);
        if (inst == null) {
            return;
        }
        inst.addOrReplacePermanentModifier(new AttributeModifier(data.name, data.amount, data.operation));
    }

    public static void removeModifier(LivingEntity entity, String modifierId) {
        AttributeModifierData data = MODIFIER_REGISTRY.get(modifierId);
        if (data == null) {
            CreateCybernetics.LOGGER.error("Attempted to remove unknown modifier: " + modifierId);
            return;
        }
        Holder<Attribute> attr = data.resolveAttribute();
        if (attr == null) {
            return;
        }
        AttributeInstance inst = entity.getAttribute(attr);
        if (inst == null) {
            return;
        }
        inst.removeModifier(data.name);
    }

    public static boolean hasModifier(LivingEntity entity, String modifierId) {
        AttributeModifierData data = MODIFIER_REGISTRY.get(modifierId);
        if (data == null) {
            return false;
        }
        Holder<Attribute> attr = data.resolveAttribute();
        if (attr == null) {
            return false;
        }
        AttributeInstance inst = entity.getAttribute(attr);
        return inst != null && inst.getModifier(data.name) != null;
    }

    static {
        Holder maxHealthAttribute = Attributes.MAX_HEALTH;
        Holder armorAttribute = Attributes.ARMOR;
        Holder armorToughnessAttribute = Attributes.ARMOR_TOUGHNESS;
        Holder oxygenBonusAttribute = Attributes.OXYGEN_BONUS;
        Holder speedAttribute = Attributes.MOVEMENT_SPEED;
        Holder knockbackResistAttribute = Attributes.KNOCKBACK_RESISTANCE;
        Holder jumpStrengthAttribute = Attributes.JUMP_STRENGTH;
        Holder attackDamageAttribute = Attributes.ATTACK_DAMAGE;
        Holder attackSpeedAttribute = Attributes.ATTACK_SPEED;
        Holder attackKnockbackAttribute = Attributes.ATTACK_KNOCKBACK;
        Holder luckAttribute = Attributes.LUCK;
        Holder blockReachAttribute = Attributes.BLOCK_INTERACTION_RANGE;
        Holder entityReachAttribute = Attributes.ENTITY_INTERACTION_RANGE;
        Holder stepHeightAttribute = Attributes.STEP_HEIGHT;
        Holder gravityAttribute = Attributes.GRAVITY;
        Holder scaleAttribute = Attributes.SCALE;
        Holder flyingSpeedAttribute = Attributes.FLYING_SPEED;
        Holder blockBreakSpeedAttribute = Attributes.BLOCK_BREAK_SPEED;
        Holder safeFallDistanceAttribute = Attributes.SAFE_FALL_DISTANCE;
        Holder burningTimeAttribute = Attributes.BURNING_TIME;
        Holder underwaterMiningAttribute = Attributes.SUBMERGED_MINING_SPEED;
        Holder waterMovementEfficiency = Attributes.WATER_MOVEMENT_EFFICIENCY;
        Holder miningSpeedAttribute = Attributes.MINING_EFFICIENCY;
        Holder crouchSpeedAttribute = Attributes.SNEAKING_SPEED;
        Holder swimSpeedAttribute = NeoForgeMod.SWIM_SPEED;
        DeferredHolder<Attribute, Attribute> xpMultiplierAttribute = ModAttributes.XP_GAIN_MULTIPLIER;
        DeferredHolder<Attribute, Attribute> oreMultiplierAttribute = ModAttributes.ORE_DROP_MULTIPLIER;
        DeferredHolder<Attribute, Attribute> hagglingAttribute = ModAttributes.HAGGLING;
        DeferredHolder<Attribute, Attribute> arrowInaccuracyAttribute = ModAttributes.ARROW_INACCURACY;
        DeferredHolder<Attribute, Attribute> breedingMultiplierAttribute = ModAttributes.BREEDING_MULTIPLIER;
        DeferredHolder<Attribute, Attribute> cropMultiplierAttribute = ModAttributes.CROP_MULTIPLIER;
        DeferredHolder<Attribute, Attribute> elytraSpeedAttribute = ModAttributes.ELYTRA_SPEED;
        DeferredHolder<Attribute, Attribute> elytraHandlingAttribute = ModAttributes.ELYTRA_HANDLING;
        DeferredHolder<Attribute, Attribute> insomniaAttribute = ModAttributes.INSOMNIA;
        DeferredHolder<Attribute, Attribute> enderDamageAttribute = ModAttributes.ENDER_PEARL_DAMAGE;
        CyberwareAttributeHelper.registerModifier("cyberleg_speed1", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberleg_speed_boost1"), 0.005, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyberleg_jump1", new AttributeModifierData((Holder<Attribute>)jumpStrengthAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberleg_jump_boost1"), 0.025, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyberleg_speed2", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberleg_speed_boost2"), 0.005, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyberleg_jump2", new AttributeModifierData((Holder<Attribute>)jumpStrengthAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberleg_jump_boost2"), 0.025, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyberarm_strength1", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberarm_strength_boost1"), 0.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyberarm_blockbreak1", new AttributeModifierData((Holder<Attribute>)blockBreakSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberarm_blockbreak_speed1"), 0.25, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyberarm_strength2", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberarm_strength_boost2"), 0.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyberarm_blockbreak2", new AttributeModifierData((Holder<Attribute>)blockBreakSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberarm_blockbreak_speed2"), 0.25, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("linear_frame_health", new AttributeModifierData((Holder<Attribute>)maxHealthAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"linear_frame_health_boost"), 8.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("linear_frame_knockback_resist", new AttributeModifierData((Holder<Attribute>)knockbackResistAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"linear_frame_knockback_resistance"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("linear_frame_blockbreak", new AttributeModifierData((Holder<Attribute>)blockBreakSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"linear_frame_blockbreak_speed"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("linear_frame_speed", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"linear_frame_speed_stifle"), -0.02, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("subdermalarmor_armor_1", new AttributeModifierData((Holder<Attribute>)armorAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"subdermal_armor_boost_1"), 4.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("subdermalarmor_armor_2", new AttributeModifierData((Holder<Attribute>)armorAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"subdermal_armor_boost_2"), 4.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("subdermalarmor_armor_3", new AttributeModifierData((Holder<Attribute>)armorAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"subdermal_armor_boost_3"), 4.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("bonelacing_health_1", new AttributeModifierData((Holder<Attribute>)maxHealthAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"bonelacing_health_boost_1"), 4.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("bonelacing_health_2", new AttributeModifierData((Holder<Attribute>)maxHealthAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"bonelacing_health_boost_2"), 4.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("bonelacing_health_3", new AttributeModifierData((Holder<Attribute>)maxHealthAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"bonelacing_health_boost_3"), 4.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("boneflex_fall_1", new AttributeModifierData((Holder<Attribute>)safeFallDistanceAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"boneflex_fall_save_1"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("boneflex_fall_2", new AttributeModifierData((Holder<Attribute>)safeFallDistanceAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"boneflex_fall_save_2"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("boneflex_fall_3", new AttributeModifierData((Holder<Attribute>)safeFallDistanceAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"boneflex_fall_save_3"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("fall_bracer_fall_1", new AttributeModifierData((Holder<Attribute>)safeFallDistanceAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"fall_bracer_fall_save_1"), 11.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("fall_bracer_fall_2", new AttributeModifierData((Holder<Attribute>)safeFallDistanceAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"fall_bracer_fall_save_2"), 11.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("pneumatic_wrist_block1", new AttributeModifierData((Holder<Attribute>)blockReachAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"pneumatic_wrist_block_reach1"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("pneumatic_wrist_entity1", new AttributeModifierData((Holder<Attribute>)entityReachAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"pneumatic_wrist_entity_reach1"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("pneumatic_wrist_knockback1", new AttributeModifierData((Holder<Attribute>)attackKnockbackAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"pneumatic_wrist_knockback_bonus1"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("pneumatic_wrist_block2", new AttributeModifierData((Holder<Attribute>)blockReachAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"pneumatic_wrist_block_reach2"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("pneumatic_wrist_entity2", new AttributeModifierData((Holder<Attribute>)entityReachAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"pneumatic_wrist_entity_reach2"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("pneumatic_wrist_knockback2", new AttributeModifierData((Holder<Attribute>)attackKnockbackAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"pneumatic_wrist_knockback_bonus2"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("oxygen_tank_oxygen", new AttributeModifierData((Holder<Attribute>)oxygenBonusAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"oxygen_tank_oxygen_bonus"), 10.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("propeller_swim_1", new AttributeModifierData((Holder<Attribute>)swimSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"propeller_swim_speed_1"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("propeller_swim_2", new AttributeModifierData((Holder<Attribute>)swimSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"propeller_swim_speed_2"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("isothermal_burning", new AttributeModifierData((Holder<Attribute>)burningTimeAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"isothermal_burning_time"), -0.95, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("ravager_tendons_size", new AttributeModifierData((Holder<Attribute>)scaleAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"ravager_tendons_size_increase"), 0.2, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("ravager_tendons_strength", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"ravager_tendons_attack_boost"), 5.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("ravager_tendons_knockback_resist", new AttributeModifierData((Holder<Attribute>)knockbackResistAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"ravager_tendons_knockback_resistance"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("ravager_tendons_knockback", new AttributeModifierData((Holder<Attribute>)attackKnockbackAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"ravager_tendons_attack_knockback"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("hyperoxygenation_speed_1", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"hyperoxygenation_speed_boost_1"), 0.03, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("hyperoxygenation_speed_2", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"hyperoxygenation_speed_boost_2"), 0.03, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("hyperoxygenation_speed_3", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"hyperoxygenation_speed_boost_3"), 0.03, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("synthmuscle_strength", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"synthmuscle_strength_boost"), 2.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("synthmuscle_size", new AttributeModifierData((Holder<Attribute>)scaleAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"synthmuscle_size_boost"), 0.1, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("synthmuscle_knockback_resist", new AttributeModifierData((Holder<Attribute>)knockbackResistAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"synthmuscle_knockback_resistance"), 1.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("synthmuscle_knockback", new AttributeModifierData((Holder<Attribute>)attackKnockbackAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"synthmuscle_attack_knockback"), 1.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("synthmuscle_speed", new AttributeModifierData((Holder<Attribute>)attackKnockbackAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"synthmuscle_speed_boost"), 0.15, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("synthmuscle_jump", new AttributeModifierData((Holder<Attribute>)jumpStrengthAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"synthmuscle_jump_boost"), 0.1, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("reinforced_knuckles_damage1", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"reinforced_knuckles_damage_boost1"), 1.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("reinforced_knuckles_damage2", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"reinforced_knuckles_damage_boost2"), 1.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("calves_sprint", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"calves_sprint_boost"), 0.05, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("gyrobladder_speed", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"gyrobladder_speed_stifle"), -0.75, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("claws_attack1", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"claws_attack_boost1"), 2.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("claws_attack2", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"claws_attack_boost2"), 2.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("sandevistan_speed", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"sandevistan_speed_boost"), 0.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("sandevistan_stepheight", new AttributeModifierData((Holder<Attribute>)stepHeightAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"sandevistan_stepheight_boost"), 2.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("sandevistan_jump", new AttributeModifierData((Holder<Attribute>)stepHeightAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"sandevistan_jump_boost"), 2.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyberbrain_learn", new AttributeModifierData((Holder<Attribute>)xpMultiplierAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberbrain_learn_boost"), 2.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyberbrain_insomnia", new AttributeModifierData((Holder<Attribute>)insomniaAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberbrain_insomnia"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("redshard_strength", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"redshard_strength_boost"), 2.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("redshard_speed", new AttributeModifierData((Holder<Attribute>)attackSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"redshard_speed_boost"), 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        CyberwareAttributeHelper.registerModifier("redshard_knockback", new AttributeModifierData((Holder<Attribute>)attackKnockbackAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"redshard_knockback_boost"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("orangeshard_ore", new AttributeModifierData((Holder<Attribute>)oreMultiplierAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"orangeshard_ore_multiplier"), 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        CyberwareAttributeHelper.registerModifier("orangeshard_mining", new AttributeModifierData((Holder<Attribute>)miningSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"orangeshard_mining_speed"), 0.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("yellowshard_haggling", new AttributeModifierData((Holder<Attribute>)hagglingAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"yellowshard_haggling_boost"), 2.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("greenshard_xp", new AttributeModifierData((Holder<Attribute>)xpMultiplierAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"greenshard_xp_multiplier"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("cyanshard_aim", new AttributeModifierData((Holder<Attribute>)arrowInaccuracyAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyanshard_aim_bot"), -1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("blueshard_swim", new AttributeModifierData((Holder<Attribute>)swimSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"blueshard_swim_speed"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("blueshard_mining", new AttributeModifierData((Holder<Attribute>)underwaterMiningAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"blueshard_mining_speed"), 1.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("blueshard_movement", new AttributeModifierData((Holder<Attribute>)waterMovementEfficiency, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"blueshard_movement_speed"), 4.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("blueshard_oxygen", new AttributeModifierData((Holder<Attribute>)oxygenBonusAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"blueshard_oxygen_boost"), 7.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        CyberwareAttributeHelper.registerModifier("purpleshard_pearl", new AttributeModifierData((Holder<Attribute>)enderDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"purpleshard_pearl_negate"), -1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("pinkshard_breeding", new AttributeModifierData((Holder<Attribute>)breedingMultiplierAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"pinkshard_breeding_multiplier"), 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        CyberwareAttributeHelper.registerModifier("brownshard_crops", new AttributeModifierData((Holder<Attribute>)cropMultiplierAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"brownshard_crops_multiplier"), 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        CyberwareAttributeHelper.registerModifier("grayshard_speed", new AttributeModifierData((Holder<Attribute>)cropMultiplierAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"grayshard_speed_boost"), 5.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("grayshard_handling", new AttributeModifierData((Holder<Attribute>)cropMultiplierAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"grayshard_handling_boost"), 5.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("blackshard_crouch", new AttributeModifierData((Holder<Attribute>)crouchSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"blackshard_crouch_speed"), 2.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("blackshard_sprint", new AttributeModifierData((Holder<Attribute>)crouchSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"blackshard_crouch_sprint"), 3.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("gemini_attackstrength", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"gemini_attackstrength_add"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("gemini_attackspeed", new AttributeModifierData((Holder<Attribute>)miningSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"gemini_attackspeed_add"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("gemini_miningstrength", new AttributeModifierData((Holder<Attribute>)miningSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"gemini_miningstrength_add"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("gemini_speed", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"gemini_speed_add"), 0.02, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("samson_attackstrength", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"samson_attackstrength_add"), 2.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("samson_miningstrength", new AttributeModifierData((Holder<Attribute>)miningSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"samson_miningstrength_add"), 2.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("samson_durability", new AttributeModifierData((Holder<Attribute>)armorAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"samson_durability_add"), 8.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("samson_watermove", new AttributeModifierData((Holder<Attribute>)waterMovementEfficiency, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"samson_watermove_subtract"), -0.75, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("samson_weight", new AttributeModifierData((Holder<Attribute>)gravityAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"samson_weight_add"), 0.1, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("eclipse_speed", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"eclipse_speed_add"), 0.1, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("eclipse_sprintspeed", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"eclipse_sprintspeed_add"), 0.2, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("eclipse_crouchspeed", new AttributeModifierData((Holder<Attribute>)crouchSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"eclipse_crouchspeed_add"), 0.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("spyder_crouchspeed", new AttributeModifierData((Holder<Attribute>)crouchSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"spyder_crouchspeed_add"), 0.5, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("spyder_jumpheight", new AttributeModifierData((Holder<Attribute>)jumpStrengthAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"spyder_jumpheight_add"), 0.1, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("wingman_elytraspeed", new AttributeModifierData((Holder<Attribute>)elytraSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"wingman_elytraspeed_add"), 1.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("wingman_elytrahandling", new AttributeModifierData((Holder<Attribute>)elytraHandlingAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"wingman_elytrahandling_add"), 4.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("aquarius_movement", new AttributeModifierData((Holder<Attribute>)waterMovementEfficiency, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"aquarius_movement_add"), 5.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("aquarius_mining", new AttributeModifierData((Holder<Attribute>)underwaterMiningAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"aquarius_mining_add"), 2.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("aquarius_swim", new AttributeModifierData((Holder<Attribute>)swimSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"aquarius_swim_add"), 5.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("dymond_miningspeed", new AttributeModifierData((Holder<Attribute>)miningSpeedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dymond_miningspeed_add"), 3.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("dymond_weight", new AttributeModifierData((Holder<Attribute>)gravityAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dymond_weight_add"), 0.01, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("dragoon_weight", new AttributeModifierData((Holder<Attribute>)gravityAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dragoon_weight_add"), 0.1, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("dragoon_size", new AttributeModifierData((Holder<Attribute>)scaleAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dragoon_size_add"), 0.3, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("dragoon_attack", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dragoon_attack_add"), 7.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("dragoon_resist", new AttributeModifierData((Holder<Attribute>)knockbackResistAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dragoon_resist_add"), 5.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("dragoon_knockback", new AttributeModifierData((Holder<Attribute>)attackKnockbackAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dragoon_knockback_add"), 5.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("dragoon_jump", new AttributeModifierData((Holder<Attribute>)jumpStrengthAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"dragoon_jump_add"), 5.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("copernicus_oxygen", new AttributeModifierData((Holder<Attribute>)oxygenBonusAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"copernicus_oxygen_add"), 20.0, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("genos_speed", new AttributeModifierData((Holder<Attribute>)speedAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"genos_sprintspeed"), 0.05, AttributeModifier.Operation.ADD_VALUE));
        CyberwareAttributeHelper.registerModifier("genos_strength", new AttributeModifierData((Holder<Attribute>)attackDamageAttribute, ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"genos_strength_add"), 4.0, AttributeModifier.Operation.ADD_VALUE));
    }

    private static class AttributeModifierData {
        final Holder<Attribute> attribute;
        final ResourceLocation attributeId;
        final ResourceLocation name;
        final double amount;
        final AttributeModifier.Operation operation;

        AttributeModifierData(Holder<Attribute> attribute, ResourceLocation name, double amount, AttributeModifier.Operation operation) {
            this.attribute = attribute;
            this.attributeId = null;
            this.name = name;
            this.amount = amount;
            this.operation = operation;
        }

        AttributeModifierData(ResourceLocation attributeId, ResourceLocation name, double amount, AttributeModifier.Operation operation) {
            this.attribute = null;
            this.attributeId = attributeId;
            this.name = name;
            this.amount = amount;
            this.operation = operation;
        }

        @Nullable
        Holder<Attribute> resolveAttribute() {
            if (this.attribute != null) {
                return this.attribute;
            }
            if (this.attributeId == null) {
                return null;
            }
            ResourceKey key = ResourceKey.create((ResourceKey)Registries.ATTRIBUTE, (ResourceLocation)this.attributeId);
            return BuiltInRegistries.ATTRIBUTE.getHolder(key).map(h -> h).orElse(null);
        }
    }
}

