/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.fml.event.config.ModConfigEvent$Loading
 *  net.neoforged.fml.event.config.ModConfigEvent$Reloading
 *  net.neoforged.neoforge.common.ModConfigSpec
 *  net.neoforged.neoforge.common.ModConfigSpec$BooleanValue
 *  net.neoforged.neoforge.common.ModConfigSpec$Builder
 *  net.neoforged.neoforge.common.ModConfigSpec$IntValue
 */
package com.perigrine3.createcybernetics;

import com.perigrine3.createcybernetics.ConfigValues;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.IntValue HUMANITY = BUILDER.comment("Base Humanity Value").defineInRange("humanity", 100, 50, 1000);
    public static final ModConfigSpec.BooleanValue KEEP_CYBERWARE = BUILDER.comment("Keep Cyberware on Death").comment("If true, cyberware will not drop and will persist through death.").define("keepCyberware", false);
    public static final ModConfigSpec.BooleanValue SURGERY_DAMAGE_SCALING = BUILDER.comment("Scale Surgery Damage").comment("If true, the surgery chamber will apply damage that scales with the amount of cyberware being removed.").comment("If false, the surgery chamber will apply 10 damage.").define("scaleSurgeryDamage", false);
    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void bake() {
        ConfigValues.BASE_HUMANITY = (Integer)HUMANITY.get();
        ConfigValues.KEEP_CYBERWARE = (Boolean)KEEP_CYBERWARE.get();
    }

    @SubscribeEvent
    public static void onConfigLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) {
            Config.bake();
        }
    }

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) {
            Config.bake();
        }
    }
}

