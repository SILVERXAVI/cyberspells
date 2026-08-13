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

import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class DeadKingBossMusicInstance
extends AbstractTickableSoundInstance {
    final DeadKingBoss boss;
    boolean starting = true;
    boolean ending = false;
    boolean triggerEnd = false;
    int transitionTicks = 40;
    private static final int START_TRANSITION_TIME = 40;
    private static final int END_TRANSITION_TIME = 40;

    protected DeadKingBossMusicInstance(DeadKingBoss boss) {
        super((SoundEvent)SoundRegistry.DEAD_KING_DRUM_LOOP.get(), SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0f;
        this.boss = boss;
        this.starting = true;
        this.transitionTicks = 40;
    }

    public void tick() {
        if (this.transitionTicks > 0) {
            --this.transitionTicks;
        }
        if (this.starting) {
            this.volume = 1.0f - (float)this.transitionTicks / 40.0f;
            if (this.transitionTicks == 0) {
                this.starting = false;
            }
        }
        if (this.triggerEnd || this.boss.isDeadOrDying()) {
            this.starting = false;
            if (!this.ending) {
                this.ending = true;
                this.transitionTicks = 40;
            }
            this.volume = (float)this.transitionTicks / 40.0f;
            if (this.transitionTicks == 0) {
                this.stop();
            }
        }
        if (this.boss.isPhase(DeadKingBoss.Phases.FinalPhase)) {
            this.pitch = 1.75f;
        }
    }

    public boolean canStartSilent() {
        return true;
    }

    public void triggerStop() {
        this.triggerEnd = true;
    }
}

