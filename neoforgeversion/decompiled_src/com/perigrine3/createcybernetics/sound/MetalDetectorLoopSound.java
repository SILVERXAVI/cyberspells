/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.player.Player
 */
package com.perigrine3.createcybernetics.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class MetalDetectorLoopSound
extends AbstractTickableSoundInstance {
    private final Player player;
    private float targetVolume = 1.0f;

    public MetalDetectorLoopSound(Player player, SoundEvent soundEvent) {
        super(soundEvent, SoundSource.PLAYERS, player.getRandom());
        this.player = player;
        this.looping = true;
        this.delay = 0;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.volume = 1.0f;
        this.pitch = 1.0f;
    }

    public void setTargetVolume(float volume) {
        this.targetVolume = volume;
    }

    public void tick() {
        if (this.player == null || this.player.isRemoved() || !this.player.isAlive()) {
            this.stop();
            return;
        }
        this.x = this.player.getX();
        this.y = this.player.getY();
        this.z = this.player.getZ();
        this.volume = this.targetVolume;
    }
}

