package cyberspells.logic;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class RuneAttributeManager {
    // Map<RuneID, Map<AttributeID, DoubleValue>>
    private static final Map<String, Map<String, net.minecraftforge.common.ForgeConfigSpec.DoubleValue>> RUNE_TO_ATTRIBUTES = new HashMap<>();
    private static final Map<String, String> RUNE_TO_NAME = new HashMap<>();

    static {
        // Helper to populate the map with config values
        register("irons_spellbooks:fire_rune", "Fire Rune",
                "irons_spellbooks:fire_spell_power", cyberspells.config.CyberSpellsConfig.FIRE_RUNE_POWER,
                "irons_spellbooks:fire_magic_resist", cyberspells.config.CyberSpellsConfig.FIRE_RUNE_RESIST);
        register("irons_spellbooks:ice_rune", "Ice Rune",
                "irons_spellbooks:ice_spell_power", cyberspells.config.CyberSpellsConfig.ICE_RUNE_POWER,
                "irons_spellbooks:ice_magic_resist", cyberspells.config.CyberSpellsConfig.ICE_RUNE_RESIST);
        register("irons_spellbooks:lightning_rune", "Lightning Rune",
                "irons_spellbooks:lightning_spell_power", cyberspells.config.CyberSpellsConfig.LIGHTNING_RUNE_POWER,
                "irons_spellbooks:lightning_magic_resist", cyberspells.config.CyberSpellsConfig.LIGHTNING_RUNE_RESIST);
        register("irons_spellbooks:holy_rune", "Holy Rune",
                "irons_spellbooks:holy_spell_power", cyberspells.config.CyberSpellsConfig.HOLY_RUNE_POWER,
                "irons_spellbooks:holy_magic_resist", cyberspells.config.CyberSpellsConfig.HOLY_RUNE_RESIST);
        register("irons_spellbooks:ender_rune", "Ender Rune",
                "irons_spellbooks:ender_spell_power", cyberspells.config.CyberSpellsConfig.ENDER_RUNE_POWER,
                "irons_spellbooks:ender_magic_resist", cyberspells.config.CyberSpellsConfig.ENDER_RUNE_RESIST);
        register("irons_spellbooks:blood_rune", "Blood Rune",
                "irons_spellbooks:blood_spell_power", cyberspells.config.CyberSpellsConfig.BLOOD_RUNE_POWER,
                "irons_spellbooks:blood_magic_resist", cyberspells.config.CyberSpellsConfig.BLOOD_RUNE_RESIST);
        register("irons_spellbooks:evocation_rune", "Evocation Rune",
                "irons_spellbooks:evocation_spell_power", cyberspells.config.CyberSpellsConfig.EVOCATION_RUNE_POWER,
                "irons_spellbooks:evocation_magic_resist", cyberspells.config.CyberSpellsConfig.EVOCATION_RUNE_RESIST);
        register("irons_spellbooks:nature_rune", "Nature Rune",
                "irons_spellbooks:nature_spell_power", cyberspells.config.CyberSpellsConfig.NATURE_RUNE_POWER,
                "irons_spellbooks:nature_magic_resist", cyberspells.config.CyberSpellsConfig.NATURE_RUNE_RESIST);
        register("irons_spellbooks:arcane_rune", "Arcane Rune",
                "irons_spellbooks:max_mana", cyberspells.config.CyberSpellsConfig.ARCANE_RUNE_MANA,
                "irons_spellbooks:mana_regen", cyberspells.config.CyberSpellsConfig.ARCANE_RUNE_REGEN);
        register("irons_spellbooks:cooldown_rune", "Cooldown Rune",
                "irons_spellbooks:cooldown_reduction", cyberspells.config.CyberSpellsConfig.COOLDOWN_RUNE_CDR,
                "irons_spellbooks:cast_time_reduction", cyberspells.config.CyberSpellsConfig.COOLDOWN_RUNE_CAST);
        register("irons_spellbooks:protection_rune", "Protection Rune",
                "irons_spellbooks:spell_resist", cyberspells.config.CyberSpellsConfig.PROTECTION_RUNE_RESIST,
                "irons_spellbooks:spell_power", cyberspells.config.CyberSpellsConfig.PROTECTION_RUNE_POWER);
        register("irons_spellbooks:blank_rune", "Blank Rune",
                "irons_spellbooks:eldritch_spell_power", cyberspells.config.CyberSpellsConfig.BLANK_RUNE_POWER,
                "irons_spellbooks:eldritch_magic_resist", cyberspells.config.CyberSpellsConfig.BLANK_RUNE_RESIST);
    }

    private static void register(String rune, String name, Object... attributesAndValues) {
        Map<String, net.minecraftforge.common.ForgeConfigSpec.DoubleValue> attrMap = new HashMap<>();
        for (int i = 0; i < attributesAndValues.length; i += 2) {
            String attrId = (String) attributesAndValues[i];
            net.minecraftforge.common.ForgeConfigSpec.DoubleValue value = (net.minecraftforge.common.ForgeConfigSpec.DoubleValue) attributesAndValues[i
                    + 1];
            attrMap.put(attrId, value);
        }
        RUNE_TO_ATTRIBUTES.put(rune, attrMap);
        RUNE_TO_NAME.put(rune, name);
    }

    public static void appendTooltip(net.minecraft.world.item.ItemStack stack,
            java.util.List<net.minecraft.network.chat.Component> tooltip) {
        if (stack.getItem() instanceof cyberspells.items.RuneHolder holder) {
            java.util.List<String> runes = holder.getRunes(stack);
            if (!runes.isEmpty()) {
                tooltip.add(net.minecraft.network.chat.Component.literal("§6Runes:"));
                for (String runeId : runes) {
                    String name = RUNE_TO_NAME.getOrDefault(runeId, runeId);
                    tooltip.add(net.minecraft.network.chat.Component.literal(" §7- " + name));

                    Map<String, net.minecraftforge.common.ForgeConfigSpec.DoubleValue> attrs = RUNE_TO_ATTRIBUTES
                            .get(runeId);
                    if (attrs != null) {
                        for (Map.Entry<String, net.minecraftforge.common.ForgeConfigSpec.DoubleValue> entry : attrs
                                .entrySet()) {
                            String attrName = entry.getKey().replace("irons_spellbooks:", "").replace("_", " ");
                            double val = entry.getValue().get();
                            // If max mana, it's flat, not %
                            String unit = entry.getKey().contains("max_mana") ? "" : "%";
                            tooltip.add(net.minecraft.network.chat.Component
                                    .literal("   §8" + attrName + ": §a+" + val + unit));
                        }
                    }
                }
            }
        }
    }

    public static void applyRuneEffects(Player player, String runeId, UUID modifierId, String sourceName) {
        Map<String, net.minecraftforge.common.ForgeConfigSpec.DoubleValue> attributes = RUNE_TO_ATTRIBUTES.get(runeId);
        if (attributes != null) {
            for (Map.Entry<String, net.minecraftforge.common.ForgeConfigSpec.DoubleValue> entry : attributes
                    .entrySet()) {
                String attributeId = entry.getKey();
                Double value = entry.getValue().get();

                Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeId));
                if (attribute != null) {
                    if (player.getAttribute(attribute).getModifier(modifierId) == null) {
                        AttributeModifier modifier = new AttributeModifier(modifierId, sourceName, value,
                                AttributeModifier.Operation.ADDITION);
                        player.getAttribute(attribute).addPermanentModifier(modifier);
                    }
                }
            }
        }
    }

    public static void updateAttributeModifiers(Player player, net.minecraftforge.items.ItemStackHandler handler) {
        java.util.Set<UUID> activeUUIDs = new java.util.HashSet<>();

        // 1. Identify active runes
        for (int i = 0; i < handler.getSlots(); i++) {
            net.minecraft.world.item.ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof cyberspells.items.RuneHolder runeHolder) {
                java.util.List<String> runes = runeHolder.getRunes(stack);
                String partName = runeHolder.getPartName();
                for (int j = 0; j < runes.size(); j++) {
                    String runeId = runes.get(j);
                    UUID modifierId = UUID.nameUUIDFromBytes((partName + "_" + j).getBytes());
                    String sourceName = "CyberRune " + partName;

                    activeUUIDs.add(modifierId);
                    applyRuneEffects(player, runeId, modifierId, sourceName);
                }
            }
        }

        // 2. Cleanup orphaned modifiers
        java.util.Set<String> allAttributes = new java.util.HashSet<>();
        for (Map<String, net.minecraftforge.common.ForgeConfigSpec.DoubleValue> map : RUNE_TO_ATTRIBUTES.values()) {
            allAttributes.addAll(map.keySet());
        }

        for (String attrId : allAttributes) {
            Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attrId));
            if (attr != null && player.getAttributes().hasAttribute(attr)) {
                net.minecraft.world.entity.ai.attributes.AttributeInstance instance = player.getAttribute(attr);
                java.util.List<AttributeModifier> toRemove = new java.util.ArrayList<>();
                for (AttributeModifier mod : instance.getModifiers()) {
                    if (mod.getName().startsWith("CyberRune ") && !activeUUIDs.contains(mod.getId())) {
                        toRemove.add(mod);
                    }
                }
                for (AttributeModifier mod : toRemove) {
                    instance.removeModifier(mod);
                }
            }
        }
    }

    public static void addAttributeToMap(com.google.common.collect.Multimap<Attribute, AttributeModifier> map,
            String runeId, UUID modifierId, String sourceName) {
        Map<String, net.minecraftforge.common.ForgeConfigSpec.DoubleValue> attributes = RUNE_TO_ATTRIBUTES.get(runeId);
        if (attributes != null) {
            for (Map.Entry<String, net.minecraftforge.common.ForgeConfigSpec.DoubleValue> entry : attributes
                    .entrySet()) {
                String attributeId = entry.getKey();
                Double value = entry.getValue().get();

                Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeId));
                if (attribute != null) {
                    AttributeModifier modifier = new AttributeModifier(modifierId, sourceName, value,
                            AttributeModifier.Operation.ADDITION);
                    map.put(attribute, modifier);
                }
            }
        }
    }
}
