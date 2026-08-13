/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.api.util.IMusicHandler;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.FadeableSoundInstance;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class DeadKingMusicHandler
implements IMusicHandler {
    static final SoundSource SOUND_SOURCE = SoundSource.RECORDS;
    static final int FIRST_PHASE_MELODY_LENGTH_MILIS = 28790;
    static final int INTRO_LENGTH_MILIS = 17600;
    DeadKingBoss boss;
    final int entityid;
    final SoundManager soundManager;
    FadeableSoundInstance beginSound;
    FadeableSoundInstance firstPhaseMelody;
    FadeableSoundInstance firstPhaseAccent;
    FadeableSoundInstance secondPhaseMelody;
    FadeableSoundInstance transitionMusic;
    Set<FadeableSoundInstance> layers = new HashSet<FadeableSoundInstance>();
    private long lastMilisPlayed;
    private boolean hasPlayedIntro;
    DeadKingBoss.Phases stage;
    boolean finishing = false;

    public DeadKingMusicHandler(DeadKingBoss boss) {
        this.boss = boss;
        this.entityid = boss.getId();
        this.soundManager = Minecraft.getInstance().getSoundManager();
        this.stage = DeadKingBoss.Phases.values()[boss.getPhase()];
        this.beginSound = new FadeableSoundInstance((SoundEvent)SoundRegistry.DEAD_KING_MUSIC_INTRO.get(), SOUND_SOURCE, false);
        this.firstPhaseMelody = new FadeableSoundInstance((SoundEvent)SoundRegistry.DEAD_KING_FIRST_PHASE_MELODY.get(), SOUND_SOURCE, true);
        this.firstPhaseAccent = new FadeableSoundInstance((SoundEvent)SoundRegistry.DEAD_KING_FIRST_PHASE_ACCENT_01.get(), SOUND_SOURCE, false);
        this.secondPhaseMelody = new FadeableSoundInstance((SoundEvent)SoundRegistry.DEAD_KING_SECOND_PHASE_MELODY_ALT.get(), SOUND_SOURCE, true);
        this.transitionMusic = new FadeableSoundInstance((SoundEvent)SoundRegistry.DEAD_KING_SUSPENSE.get(), SOUND_SOURCE, false);
    }

    @Override
    public void init() {
        this.soundManager.stop(null, SoundSource.MUSIC);
        switch (this.stage) {
            case FirstPhase: {
                this.addLayer(this.beginSound);
                this.lastMilisPlayed = System.currentTimeMillis();
                break;
            }
            case FinalPhase: {
                this.initSecondPhase();
            }
        }
    }

    @Override
    public void stop() {
        this.stopLayers();
        this.finishing = true;
    }

    @Override
    public void tick() {
        if (this.isDone() || this.finishing) {
            return;
        }
        if (this.boss.isDeadOrDying() || this.boss.isRemoved()) {
            this.stopLayers();
            this.finishing = true;
            return;
        }
        DeadKingBoss.Phases bossPhase = DeadKingBoss.Phases.values()[this.boss.getPhase()];
        switch (bossPhase) {
            case FirstPhase: {
                if (!this.hasPlayedIntro) {
                    if (this.soundManager.isActive((SoundInstance)this.beginSound) && this.lastMilisPlayed + 17600L >= System.currentTimeMillis()) break;
                    this.hasPlayedIntro = true;
                    this.layers.remove((Object)this.beginSound);
                    this.initFirstPhase();
                    break;
                }
                if (this.lastMilisPlayed + 57580L >= System.currentTimeMillis()) break;
                this.playAccent(this.firstPhaseAccent);
                break;
            }
            case Transitioning: {
                if (this.stage == DeadKingBoss.Phases.Transitioning) break;
                this.stage = DeadKingBoss.Phases.Transitioning;
                this.stopLayers();
                this.addLayer(this.transitionMusic);
                break;
            }
            case FinalPhase: {
                if (this.stage == DeadKingBoss.Phases.FinalPhase) break;
                this.stage = DeadKingBoss.Phases.FinalPhase;
                this.initSecondPhase();
            }
        }
    }

    @Override
    public boolean isDone() {
        for (FadeableSoundInstance soundInstance : this.layers) {
            if (soundInstance.isStopped() || !this.soundManager.isActive((SoundInstance)soundInstance)) continue;
            return false;
        }
        return true;
    }

    private void addLayer(FadeableSoundInstance soundInstance) {
        this.layers.stream().filter(sound -> sound.isStopped() || !this.soundManager.isActive((SoundInstance)sound)).toList().forEach(this.layers::remove);
        this.soundManager.play((SoundInstance)soundInstance);
        this.layers.add(soundInstance);
    }

    private void playAccent(FadeableSoundInstance soundInstance) {
        this.lastMilisPlayed = System.currentTimeMillis();
        this.addLayer(soundInstance);
    }

    public void stopLayers() {
        this.layers.forEach(FadeableSoundInstance::triggerStop);
    }

    @Override
    public void hardStop() {
        this.layers.forEach(arg_0 -> ((SoundManager)this.soundManager).stop(arg_0));
    }

    @Override
    public void triggerResume() {
        if (Minecraft.getInstance().level != null) {
            DeadKingBoss deadKingBoss;
            Entity entity = Minecraft.getInstance().level.getEntity(this.entityid);
            DeadKingBoss deadKingBoss2 = this.boss = entity instanceof DeadKingBoss ? (deadKingBoss = (DeadKingBoss)entity) : this.boss;
        }
        if (!this.boss.isRemoved()) {
            this.layers.forEach(sound -> {
                sound.triggerStart();
                if (!this.soundManager.isActive((SoundInstance)sound)) {
                    this.soundManager.play((SoundInstance)sound);
                }
            });
        }
    }

    private void initFirstPhase() {
        this.addLayer(this.firstPhaseMelody);
        this.playAccent(this.firstPhaseAccent);
    }

    private void initSecondPhase() {
        this.addLayer(this.secondPhaseMelody);
    }
}

