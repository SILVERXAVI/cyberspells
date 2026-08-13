/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Registry
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.data.worldgen.BootstrapContext
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.RegistryFixedCodec
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.item.Item
 *  net.neoforged.neoforge.registries.DataPackRegistryEvent$NewRegistry
 */
package io.redspace.ironsspellbooks.registries;

import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class UpgradeOrbTypeRegistry {
    public static final ResourceKey<Registry<UpgradeOrbType>> UPGRADE_ORB_REGISTRY_KEY = ResourceKey.createRegistryKey((ResourceLocation)IronsSpellbooks.id("upgrade_orb_type"));
    public static final Codec<Holder<UpgradeOrbType>> UPGRADE_ORB_REGISTRY_CODEC = RegistryFixedCodec.create(UPGRADE_ORB_REGISTRY_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<UpgradeOrbType>> UPGRADE_ORB_REGISTRY_STREAM_CODEC = ByteBufCodecs.holderRegistry(UPGRADE_ORB_REGISTRY_KEY);
    public static ResourceKey<UpgradeOrbType> FIRE_SPELL_POWER = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("fire_power"));
    public static ResourceKey<UpgradeOrbType> ICE_SPELL_POWER = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("ice_power"));
    public static ResourceKey<UpgradeOrbType> LIGHTNING_SPELL_POWER = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("lightning_power"));
    public static ResourceKey<UpgradeOrbType> HOLY_SPELL_POWER = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("holy_power"));
    public static ResourceKey<UpgradeOrbType> ENDER_SPELL_POWER = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("ender_power"));
    public static ResourceKey<UpgradeOrbType> BLOOD_SPELL_POWER = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("blood_power"));
    public static ResourceKey<UpgradeOrbType> EVOCATION_SPELL_POWER = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("evocation_power"));
    public static ResourceKey<UpgradeOrbType> NATURE_SPELL_POWER = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("nature_power"));
    public static ResourceKey<UpgradeOrbType> COOLDOWN = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("cooldown"));
    public static ResourceKey<UpgradeOrbType> SPELL_RESISTANCE = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("spell_resistance"));
    public static ResourceKey<UpgradeOrbType> MANA = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("mana"));
    public static ResourceKey<UpgradeOrbType> ATTACK_DAMAGE = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("melee_damage"));
    public static ResourceKey<UpgradeOrbType> ATTACK_SPEED = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("melee_speed"));
    public static ResourceKey<UpgradeOrbType> HEALTH = ResourceKey.create(UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)IronsSpellbooks.id("health"));

    public static Registry<UpgradeOrbType> upgradeTypeRegistry(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(UPGRADE_ORB_REGISTRY_KEY);
    }

    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(UPGRADE_ORB_REGISTRY_KEY, UpgradeOrbType.CODEC, UpgradeOrbType.CODEC);
    }

    public static void bootstrap(BootstrapContext<UpgradeOrbType> bootstrap) {
        bootstrap.register(FIRE_SPELL_POWER, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.FIRE_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.FIRE_UPGRADE_ORB));
        bootstrap.register(ICE_SPELL_POWER, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.ICE_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.ICE_UPGRADE_ORB));
        bootstrap.register(LIGHTNING_SPELL_POWER, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.LIGHTNING_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.LIGHTNING_UPGRADE_ORB));
        bootstrap.register(HOLY_SPELL_POWER, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.HOLY_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.HOLY_UPGRADE_ORB));
        bootstrap.register(ENDER_SPELL_POWER, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.ENDER_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.ENDER_UPGRADE_ORB));
        bootstrap.register(BLOOD_SPELL_POWER, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.BLOOD_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.BLOOD_UPGRADE_ORB));
        bootstrap.register(EVOCATION_SPELL_POWER, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.EVOCATION_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.EVOCATION_UPGRADE_ORB));
        bootstrap.register(NATURE_SPELL_POWER, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.NATURE_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.NATURE_UPGRADE_ORB));
        bootstrap.register(COOLDOWN, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.COOLDOWN_REDUCTION, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.COOLDOWN_UPGRADE_ORB));
        bootstrap.register(SPELL_RESISTANCE, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.SPELL_RESIST, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, (Holder<Item>)ItemRegistry.PROTECTION_UPGRADE_ORB));
        bootstrap.register(MANA, (Object)new UpgradeOrbType((Holder<Attribute>)AttributeRegistry.MAX_MANA, 50.0, AttributeModifier.Operation.ADD_VALUE, (Holder<Item>)ItemRegistry.MANA_UPGRADE_ORB));
        bootstrap.register(ATTACK_DAMAGE, (Object)new UpgradeOrbType((Holder<Attribute>)Attributes.ATTACK_DAMAGE, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, Optional.empty()));
        bootstrap.register(ATTACK_SPEED, (Object)new UpgradeOrbType((Holder<Attribute>)Attributes.ATTACK_SPEED, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, Optional.empty()));
        bootstrap.register(HEALTH, (Object)new UpgradeOrbType((Holder<Attribute>)Attributes.MAX_HEALTH, 2.0, AttributeModifier.Operation.ADD_VALUE, Optional.empty()));
    }
}

