package cyberspells.logic;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import cyberspells.items.RuneHolder;
import java.util.UUID;

public class RuneAttributeManager {
        private static final Map<String, String> RUNE_TO_NAME = new HashMap<>();

        private static final Map<String, Map<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue>> RUNE_TO_ATTRIBUTES = new HashMap<>();

        static {
                // Register runes with their config values
                register("irons_spellbooks:fire_rune", "Fire Rune",
                                "irons_spellbooks:fire_spell_power",
                                cyberspells.config.CyberSpellsConfig.FIRE_RUNE_POWER,
                                "irons_spellbooks:fire_magic_resist",
                                cyberspells.config.CyberSpellsConfig.FIRE_RUNE_RESIST);
                register("irons_spellbooks:ice_rune", "Ice Rune",
                                "irons_spellbooks:ice_spell_power", cyberspells.config.CyberSpellsConfig.ICE_RUNE_POWER,
                                "irons_spellbooks:ice_magic_resist",
                                cyberspells.config.CyberSpellsConfig.ICE_RUNE_RESIST);
                register("irons_spellbooks:lightning_rune", "Lightning Rune",
                                "irons_spellbooks:lightning_spell_power",
                                cyberspells.config.CyberSpellsConfig.LIGHTNING_RUNE_POWER,
                                "irons_spellbooks:lightning_magic_resist",
                                cyberspells.config.CyberSpellsConfig.LIGHTNING_RUNE_RESIST);
                register("irons_spellbooks:holy_rune", "Holy Rune",
                                "irons_spellbooks:holy_spell_power",
                                cyberspells.config.CyberSpellsConfig.HOLY_RUNE_POWER,
                                "irons_spellbooks:holy_magic_resist",
                                cyberspells.config.CyberSpellsConfig.HOLY_RUNE_RESIST);
                register("irons_spellbooks:ender_rune", "Ender Rune",
                                "irons_spellbooks:ender_spell_power",
                                cyberspells.config.CyberSpellsConfig.ENDER_RUNE_POWER,
                                "irons_spellbooks:ender_magic_resist",
                                cyberspells.config.CyberSpellsConfig.ENDER_RUNE_RESIST);
                register("irons_spellbooks:blood_rune", "Blood Rune",
                                "irons_spellbooks:blood_spell_power",
                                cyberspells.config.CyberSpellsConfig.BLOOD_RUNE_POWER,
                                "irons_spellbooks:blood_magic_resist",
                                cyberspells.config.CyberSpellsConfig.BLOOD_RUNE_RESIST);
                register("irons_spellbooks:evocation_rune", "Evocation Rune",
                                "irons_spellbooks:evocation_spell_power",
                                cyberspells.config.CyberSpellsConfig.EVOCATION_RUNE_POWER,
                                "irons_spellbooks:evocation_magic_resist",
                                cyberspells.config.CyberSpellsConfig.EVOCATION_RUNE_RESIST);
                register("irons_spellbooks:nature_rune", "Nature Rune",
                                "irons_spellbooks:nature_spell_power",
                                cyberspells.config.CyberSpellsConfig.NATURE_RUNE_POWER,
                                "irons_spellbooks:nature_magic_resist",
                                cyberspells.config.CyberSpellsConfig.NATURE_RUNE_RESIST);
                register("irons_spellbooks:arcane_rune", "Arcane Rune",
                                "irons_spellbooks:max_mana", cyberspells.config.CyberSpellsConfig.ARCANE_RUNE_MANA,
                                "irons_spellbooks:mana_regen", cyberspells.config.CyberSpellsConfig.ARCANE_RUNE_REGEN);
                register("irons_spellbooks:cooldown_rune", "Cooldown Rune",
                                "irons_spellbooks:cooldown_reduction",
                                cyberspells.config.CyberSpellsConfig.COOLDOWN_RUNE_CDR,
                                "irons_spellbooks:cast_time_reduction",
                                cyberspells.config.CyberSpellsConfig.COOLDOWN_RUNE_CAST);
                register("irons_spellbooks:protection_rune", "Protection Rune",
                                "irons_spellbooks:spell_resist",
                                cyberspells.config.CyberSpellsConfig.PROTECTION_RUNE_RESIST,
                                "irons_spellbooks:spell_power",
                                cyberspells.config.CyberSpellsConfig.PROTECTION_RUNE_POWER);
                register("irons_spellbooks:blank_rune", "Blank Rune",
                                "irons_spellbooks:eldritch_spell_power",
                                cyberspells.config.CyberSpellsConfig.BLANK_RUNE_POWER,
                                "irons_spellbooks:eldritch_magic_resist",
                                cyberspells.config.CyberSpellsConfig.BLANK_RUNE_RESIST);
        }

        private static void register(String runeId, String name, Object... attrSpecs) {
                RUNE_TO_NAME.put(runeId, name);
                Map<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> map = new HashMap<>();
                for (int i = 0; i < attrSpecs.length; i += 2) {
                        map.put((String) attrSpecs[i],
                                        (net.neoforged.neoforge.common.ModConfigSpec.DoubleValue) attrSpecs[i + 1]);
                }
                RUNE_TO_ATTRIBUTES.put(runeId, map);
        }

        public static void appendTooltip(ItemStack stack, List<Component> tooltip) {
                if (stack.getItem() instanceof cyberspells.items.RuneHolder holder) {
                        List<String> runes = holder.getRunes(stack);
                        if (!runes.isEmpty()) {
                                tooltip.add(Component.literal("§6Runes:"));
                                for (String runeId : runes) {
                                        String name = RUNE_TO_NAME.getOrDefault(runeId, runeId);
                                        tooltip.add(Component.literal(" §7- " + name));

                                        Map<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> attrs = RUNE_TO_ATTRIBUTES
                                                        .get(runeId);
                                        if (attrs != null) {
                                                for (Map.Entry<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> entry : attrs
                                                                .entrySet()) {
                                                        String attrName = entry.getKey()
                                                                        .replace("irons_spellbooks:", "")
                                                                        .replace("_", " ");
                                                        double val = entry.getValue().get();
                                                        String unit = entry.getKey().contains("max_mana") ? "" : "%";
                                                        tooltip.add(Component.literal(
                                                                        "   §8" + attrName + ": §a+" + val + unit));
                                                }
                                        }
                                }
                        }
                }
        }

        public static void addAttributes(
                        net.minecraft.world.item.component.ItemAttributeModifiers.Builder builder,
                        String runeId, String runeIndex) {
                Map<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> attributes = RUNE_TO_ATTRIBUTES
                                .get(runeId);
                if (attributes != null) {
                        for (Map.Entry<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> entry : attributes
                                        .entrySet()) {
                                String attributeId = entry.getKey();
                                double value = entry.getValue().get();

                                Optional<net.minecraft.core.Holder.Reference<Attribute>> attr = BuiltInRegistries.ATTRIBUTE
                                                .getHolder(ResourceLocation.parse(attributeId));
                                if (attr.isPresent()) {
                                        // Create a unique modifier ID for each attribute to avoid conflicts
                                        ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                                                        "cyberspells",
                                                        "rune_" + runeIndex + "_" + attributeId.replace(":", "_"));

                                        AttributeModifier.Operation operation = attributeId.contains("max_mana")
                                                        ? AttributeModifier.Operation.ADD_VALUE
                                                        : AttributeModifier.Operation.ADD_MULTIPLIED_BASE;

                                        AttributeModifier modifier = new AttributeModifier(modifierId, value,
                                                        operation);
                                        // ANY slot group should allow the attribute to work in custom inventories
                                        builder.add(attr.get(), modifier,
                                                        net.minecraft.world.entity.EquipmentSlotGroup.ANY);
                                }
                        }
                }
        }

        public static void addAttributesToMultimap(
                        com.google.common.collect.Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> map,
                        String runeId, String runeIndex) {
                Map<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> attributes = RUNE_TO_ATTRIBUTES.get(runeId);
                if (attributes != null) {
                        for (Map.Entry<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> entry : attributes.entrySet()) {
                                String attributeId = entry.getKey();
                                double value = entry.getValue().get();

                                Optional<net.minecraft.core.Holder.Reference<Attribute>> attr = BuiltInRegistries.ATTRIBUTE
                                                .getHolder(ResourceLocation.parse(attributeId));
                                if (attr.isPresent()) {
                                        ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                                                        "cyberspells",
                                                        "rune_" + runeIndex + "_" + attributeId.replace(":", "_"));

                                        AttributeModifier.Operation operation = attributeId.contains("max_mana")
                                                        ? AttributeModifier.Operation.ADD_VALUE
                                                        : AttributeModifier.Operation.ADD_MULTIPLIED_BASE;

                                        AttributeModifier modifier = new AttributeModifier(modifierId, value, operation);
                                        map.put(attr.get(), modifier);
                                }
                        }
                }
        }

        public static void manageAttributes(Player player, ItemStack stack, String partName) {
                if (player.level().isClientSide)
                        return;

                if (stack.getItem() instanceof RuneHolder holder) {
                        List<String> runes = holder.getRunes(stack);
                        int maxSlots = holder.getMaxRuneSlots();
                        // For each rune slot, apply its attributes
                        for (int i = 0; i < runes.size(); i++) {
                                if (i < maxSlots) {
                                        String runeId = runes.get(i);
                                        applyRuneAttributes(player, runeId, partName + "_" + i);
                                }
                        }
                        // Remove attributes for empty slots or slots beyond max config
                        for (int i = Math.min(runes.size(), maxSlots); i < 5; i++) {
                                removeRuneAttributes(player, partName + "_" + i);
                        }
                }
        }

        public static void removeAttributes(Player player, String partName) {
                if (player.level().isClientSide)
                        return;
                // Clean up all 5 possible slots to be safe
                for (int i = 0; i < 5; i++) {
                        removeRuneAttributes(player, partName + "_" + i);
                }
        }

        private static void applyRuneAttributes(Player player, String runeId, String suffix) {
                Map<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> attributes = RUNE_TO_ATTRIBUTES
                                .get(runeId);
                if (attributes != null) {
                        for (Map.Entry<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> entry : attributes
                                        .entrySet()) {
                                String attributeId = entry.getKey();
                                double value = entry.getValue().get();

                                Optional<net.minecraft.core.Holder.Reference<Attribute>> attr = BuiltInRegistries.ATTRIBUTE
                                                .getHolder(ResourceLocation.parse(attributeId));
                                if (attr.isPresent()) {
                                        ResourceLocation modifierId = ResourceLocation
                                                        .fromNamespaceAndPath("cyberspells", "rune_"
                                                                        + suffix + "_"
                                                                        + attributeId.replace(":",
                                                                                        "_"));

                                        var instance = player.getAttribute(attr.get());
                                        if (instance != null) {
                                                if (instance.getModifier(modifierId) == null) {
                                                        AttributeModifier.Operation operation = attributeId
                                                                        .contains("max_mana")
                                                                                        ? AttributeModifier.Operation.ADD_VALUE
                                                                                        : AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
                                                        AttributeModifier modifier = new AttributeModifier(modifierId,
                                                                        value, operation);
                                                        instance.addTransientModifier(modifier);
                                                }
                                        }
                                }
                        }
                }
        }

        private static void removeRuneAttributes(Player player, String suffix) {
                // We need to remove all possible modifiers for this suffix
                for (String runeId : RUNE_TO_ATTRIBUTES.keySet()) {
                        Map<String, net.neoforged.neoforge.common.ModConfigSpec.DoubleValue> attributes = RUNE_TO_ATTRIBUTES
                                        .get(runeId);
                        for (String attributeId : attributes.keySet()) {
                                Optional<net.minecraft.core.Holder.Reference<Attribute>> attr = BuiltInRegistries.ATTRIBUTE
                                                .getHolder(ResourceLocation.parse(attributeId));
                                if (attr.isPresent()) {
                                        ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                                                        "cyberspells",
                                                        "rune_" + suffix + "_" + attributeId.replace(":", "_"));
                                        var instance = player.getAttribute(attr.get());
                                        if (instance != null) {
                                                instance.removeModifier(modifierId);
                                        }
                                }
                        }
                }
        }
}
