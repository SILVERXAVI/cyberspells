/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  net.minecraft.client.KeyMapping
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
 *  net.neoforged.neoforge.client.settings.IKeyConflictContext
 *  net.neoforged.neoforge.client.settings.KeyConflictContext
 */
package io.redspace.ironsspellbooks.player;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

@EventBusSubscriber(value={Dist.CLIENT}, modid="irons_spellbooks", bus=EventBusSubscriber.Bus.MOD)
public final class KeyMappings {
    public static final String KEY_BIND_GENERAL_CATEGORY = "key.irons_spellbooks.group_1";
    public static final String KEY_BIND_QUICK_CAST_CATEGORY = "key.irons_spellbooks.group_2";
    public static final KeyMapping SPELL_WHEEL_KEYMAP = new KeyMapping(KeyMappings.getResourceName("spell_wheel"), (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 82, "key.irons_spellbooks.group_1");
    public static final KeyMapping SPELL_WHEEL_TOGGLE_KEYMAP = new KeyMapping(KeyMappings.getResourceName("spell_wheel_toggle"), (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), "key.irons_spellbooks.group_1");
    public static final KeyMapping SPELLBOOK_CAST_ACTIVE_KEYMAP = new KeyMapping(KeyMappings.getResourceName("spellbook_cast"), (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 86, "key.irons_spellbooks.group_1");
    public static final KeyMapping SPELLBAR_SCROLL_MODIFIER_KEYMAP = new KeyMapping(KeyMappings.getResourceName("spell_bar_modifier"), (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 342, "key.irons_spellbooks.group_1");
    public static final List<KeyMapping> QUICK_CAST_MAPPINGS = KeyMappings.createQuickCastKeybinds();

    private static String getResourceName(String name) {
        return String.format("key.irons_spellbooks.%s", name);
    }

    @SubscribeEvent
    public static void onRegisterKeybinds(RegisterKeyMappingsEvent event) {
        event.register(SPELL_WHEEL_KEYMAP);
        event.register(SPELL_WHEEL_TOGGLE_KEYMAP);
        event.register(SPELLBOOK_CAST_ACTIVE_KEYMAP);
        event.register(SPELLBAR_SCROLL_MODIFIER_KEYMAP);
        QUICK_CAST_MAPPINGS.forEach(arg_0 -> ((RegisterKeyMappingsEvent)event).register(arg_0));
    }

    private static List<KeyMapping> createQuickCastKeybinds() {
        ArrayList<KeyMapping> qcm = new ArrayList<KeyMapping>();
        for (int i = 1; i <= 15; ++i) {
            qcm.add(new KeyMapping(KeyMappings.getResourceName(String.format("spell_quick_cast_%d", i)), (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), KEY_BIND_QUICK_CAST_CATEGORY));
        }
        return qcm;
    }
}

