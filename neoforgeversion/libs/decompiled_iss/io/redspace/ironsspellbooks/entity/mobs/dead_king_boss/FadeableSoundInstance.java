/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance$Attenuation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class FadeableSoundInstance
extends AbstractTickableSoundInstance {
    boolean starting = false;
    private int transitionTicks;
    private boolean triggerEnd = false;
    private static final int START_TRANSITION_TIME = 40;
    private static final int END_TRANSITION_TIME = 40;
    private int customFadeIn;

    public FadeableSoundInstance(SoundEvent soundEvent, SoundSource source, boolean loop) {
        super(soundEvent, source, SoundInstance.createUnseededRandom());
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.looping = loop;
        this.delay = 0;
        this.volume = 1.0f;
        this.starting = false;
    }

    public void tick() {
        if (this.transitionTicks > 0) {
            --this.transitionTicks;
        }
        if (this.starting) {
            int max = this.customFadeIn > 0 ? this.customFadeIn : 40;
            this.volume = 1.0f - (float)this.transitionTicks / (float)max;
            if (this.transitionTicks == 0) {
                this.starting = false;
                this.customFadeIn = 0;
            }
        }
        if (this.triggerEnd) {
            this.volume = (float)this.transitionTicks / 40.0f;
            if (this.transitionTicks == 0) {
                this.stop();
            }
        }
    }

    public void fadeIn(int ticks) {
        this.customFadeIn = ticks;
        this.transitionTicks = ticks;
        this.starting = true;
        this.volume = 0.0f;
    }

    public void unstop() {
        this.stopped = false;
        this.volume = 1.0f;
    }

    public boolean canStartSilent() {
        return true;
    }

    public void triggerStop() {
        this.triggerEnd = true;
        this.transitionTicks = this.volume < 1.0f ? (int)(40.0f * this.volume) : 40;
    }

    public void triggerStart() {
        this.stopped = false;
        this.triggerEnd = false;
        this.transitionTicks = this.volume < 1.0f ? (int)(40.0f * this.volume) : 40;
        this.starting = true;
    }
}

