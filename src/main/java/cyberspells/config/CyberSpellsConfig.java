package cyberspells.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CyberSpellsConfig {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;

        public static final ForgeConfigSpec.DoubleValue FIRE_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue FIRE_RUNE_RESIST;
        public static final ForgeConfigSpec.DoubleValue ICE_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue ICE_RUNE_RESIST;
        public static final ForgeConfigSpec.DoubleValue LIGHTNING_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue LIGHTNING_RUNE_RESIST;
        public static final ForgeConfigSpec.DoubleValue HOLY_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue HOLY_RUNE_RESIST;
        public static final ForgeConfigSpec.DoubleValue ENDER_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue ENDER_RUNE_RESIST;
        public static final ForgeConfigSpec.DoubleValue BLOOD_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue BLOOD_RUNE_RESIST;
        public static final ForgeConfigSpec.DoubleValue EVOCATION_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue EVOCATION_RUNE_RESIST;
        public static final ForgeConfigSpec.DoubleValue NATURE_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue NATURE_RUNE_RESIST;

        public static final ForgeConfigSpec.DoubleValue ARCANE_RUNE_MANA;
        public static final ForgeConfigSpec.DoubleValue ARCANE_RUNE_REGEN;
        public static final ForgeConfigSpec.DoubleValue COOLDOWN_RUNE_CDR;
        public static final ForgeConfigSpec.DoubleValue COOLDOWN_RUNE_CAST;
        public static final ForgeConfigSpec.DoubleValue PROTECTION_RUNE_RESIST;
        public static final ForgeConfigSpec.DoubleValue PROTECTION_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue BLANK_RUNE_POWER;
        public static final ForgeConfigSpec.DoubleValue BLANK_RUNE_RESIST;

        public static final ForgeConfigSpec.IntValue ARM_SLOTS;
        public static final ForgeConfigSpec.IntValue LEG_SLOTS;
        public static final ForgeConfigSpec.IntValue HEART_SLOTS;

        public static final ForgeConfigSpec.DoubleValue ARM_ESSENCE;
        public static final ForgeConfigSpec.DoubleValue LEG_ESSENCE;
        public static final ForgeConfigSpec.DoubleValue HEART_ESSENCE;

        static {
                BUILDER.push("Rune Attributes");

                FIRE_RUNE_POWER = BUILDER.comment("Fire Rune Spell Power Bonus")
                                .defineInRange("fire_rune_power", 0.05, 0.0, 100.0);
                FIRE_RUNE_RESIST = BUILDER.comment("Fire Rune Magic Resist Bonus")
                                .defineInRange("fire_rune_resist", 0.05, 0.0, 100.0);

                ICE_RUNE_POWER = BUILDER.comment("Ice Rune Spell Power Bonus")
                                .defineInRange("ice_rune_power", 0.05, 0.0, 100.0);
                ICE_RUNE_RESIST = BUILDER.comment("Ice Rune Magic Resist Bonus")
                                .defineInRange("ice_rune_resist", 0.05, 0.0, 100.0);

                LIGHTNING_RUNE_POWER = BUILDER.comment("Lightning Rune Spell Power Bonus")
                                .defineInRange("lightning_rune_power", 0.05, 0.0, 100.0);
                LIGHTNING_RUNE_RESIST = BUILDER.comment("Lightning Rune Magic Resist Bonus")
                                .defineInRange("lightning_rune_resist", 0.05, 0.0, 100.0);

                HOLY_RUNE_POWER = BUILDER.comment("Holy Rune Spell Power Bonus")
                                .defineInRange("holy_rune_power", 0.05, 0.0, 100.0);
                HOLY_RUNE_RESIST = BUILDER.comment("Holy Rune Magic Resist Bonus")
                                .defineInRange("holy_rune_resist", 0.05, 0.0, 100.0);

                ENDER_RUNE_POWER = BUILDER.comment("Ender Rune Spell Power Bonus")
                                .defineInRange("ender_rune_power", 0.05, 0.0, 100.0);
                ENDER_RUNE_RESIST = BUILDER.comment("Ender Rune Magic Resist Bonus")
                                .defineInRange("ender_rune_resist", 0.05, 0.0, 100.0);

                BLOOD_RUNE_POWER = BUILDER.comment("Blood Rune Spell Power Bonus")
                                .defineInRange("blood_rune_power", 0.05, 0.0, 100.0);
                BLOOD_RUNE_RESIST = BUILDER.comment("Blood Rune Magic Resist Bonus")
                                .defineInRange("blood_rune_resist", 0.05, 0.0, 100.0);

                EVOCATION_RUNE_POWER = BUILDER.comment("Evocation Rune Spell Power Bonus")
                                .defineInRange("evocation_rune_power", 0.05, 0.0, 100.0);
                EVOCATION_RUNE_RESIST = BUILDER.comment("Evocation Rune Magic Resist Bonus")
                                .defineInRange("evocation_rune_resist", 0.05, 0.0, 100.0);

                NATURE_RUNE_POWER = BUILDER.comment("Nature Rune Spell Power Bonus")
                                .defineInRange("nature_rune_power", 0.05, 0.0, 100.0);
                NATURE_RUNE_RESIST = BUILDER.comment("Nature Rune Magic Resist Bonus")
                                .defineInRange("nature_rune_resist", 0.05, 0.0, 100.0);

                ARCANE_RUNE_MANA = BUILDER.comment("Arcane Rune Max Mana Bonus")
                                .defineInRange("arcane_rune_max_mana", 50.0, 0.0, 1000.0);
                ARCANE_RUNE_REGEN = BUILDER.comment("Arcane Rune Mana Regen Bonus")
                                .defineInRange("arcane_rune_mana_regen", 0.05, 0.0, 100.0);

                COOLDOWN_RUNE_CDR = BUILDER.comment("Cooldown Rune Cooldown Reduction Bonus")
                                .defineInRange("cooldown_rune_cdr", 0.05, 0.0, 100.0);
                COOLDOWN_RUNE_CAST = BUILDER.comment("Cooldown Rune Cast Time Reduction Bonus")
                                .defineInRange("cooldown_rune_cast_time", 0.05, 0.0, 100.0);

                PROTECTION_RUNE_RESIST = BUILDER.comment("Protection Rune Spell Resistance Bonus")
                                .defineInRange("protection_rune_resist", 0.05, 0.0, 100.0);

                PROTECTION_RUNE_POWER = BUILDER.comment("Protection Rune Spell Power Bonus")
                                .defineInRange("protection_rune_power", 0.05, 0.0, 100.0);

                BLANK_RUNE_POWER = BUILDER.comment("Blank Rune Eldritch Power Bonus")
                                .defineInRange("blank_rune_power", 0.05, 0.0, 100.0);
                BLANK_RUNE_RESIST = BUILDER.comment("Blank Rune Eldritch Resist Bonus")
                                .defineInRange("blank_rune_resist", 0.05, 0.0, 100.0);
                BUILDER.pop(); // Pop Rune Attributes

                BUILDER.push("Slot Counts");
                ARM_SLOTS = BUILDER.comment("Number of slots for Cybernetic Rune Arms (Max 5)")
                                .defineInRange("arm_slots", 3, 1, 5);
                LEG_SLOTS = BUILDER.comment("Number of slots for Cybernetic Rune Legs (Max 5)")
                                .defineInRange("leg_slots", 3, 1, 5);
                HEART_SLOTS = BUILDER.comment("Number of slots for Cybernetic Rune Heart (Max 5)")
                                .defineInRange("heart_slots", 2, 1, 5);
                BUILDER.pop(); // Pop Slot Counts

                BUILDER.push("Essence Costs");
                ARM_ESSENCE = BUILDER.comment("Essence cost for Cybernetic Rune Arms")
                                .defineInRange("arm_essence_cost", 10.0, 0.0, 100.0);
                LEG_ESSENCE = BUILDER.comment("Essence cost for Cybernetic Rune Legs")
                                .defineInRange("leg_essence_cost", 10.0, 0.0, 100.0);
                HEART_ESSENCE = BUILDER.comment("Essence cost for Cybernetic Rune Heart")
                                .defineInRange("heart_essence_cost", 15.0, 0.0, 100.0);
                BUILDER.pop(); // Pop Essence Costs

                SPEC = BUILDER.build();
        }
}
