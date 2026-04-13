/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.JukeboxSong
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package com.perigrine3.createcybernetics.sound;

import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create((Registry)BuiltInRegistries.SOUND_EVENT, (String)"createcybernetics");
    public static final Supplier<SoundEvent> METAL_DETECTOR_BEEPS = ModSounds.registerSoundEvent("metal_detector_beeps");
    public static final Supplier<SoundEvent> RETRACTABLE_CLAWS_SNIKT = ModSounds.registerSoundEvent("retractable_claws_snikt");
    public static final Supplier<SoundEvent> SANDY_STARTUP = ModSounds.registerSoundEvent("sandy_startup");
    public static final Supplier<SoundEvent> METAL_CRUSHING = ModSounds.registerSoundEvent("metal_crushing");
    public static final Supplier<SoundEvent> GLITCHY = ModSounds.registerSoundEvent("glitchy");
    public static final Supplier<SoundEvent> CYBERPSYCHO = ModSounds.registerSoundEvent("cyberpsycho");
    public static final ResourceKey<JukeboxSong> CYBERPSYCHO_KEY = ModSounds.createSong("cyberpsycho");
    public static final Supplier<SoundEvent> NEON_OVERLORDS = ModSounds.registerSoundEvent("neon_overlords");
    public static final ResourceKey<JukeboxSong> NEON_OVERLORDS_KEY = ModSounds.createSong("neon_overlords");
    public static final Supplier<SoundEvent> NEUROHACK = ModSounds.registerSoundEvent("neurohack");
    public static final ResourceKey<JukeboxSong> NEUROHACK_KEY = ModSounds.createSong("neurohack");
    public static final Supplier<SoundEvent> THE_GRID = ModSounds.registerSoundEvent("the_grid");
    public static final ResourceKey<JukeboxSong> THE_GRID_KEY = ModSounds.createSong("the_grid");

    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create((ResourceKey)Registries.JUKEBOX_SONG, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)name));
    }

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent((ResourceLocation)id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

