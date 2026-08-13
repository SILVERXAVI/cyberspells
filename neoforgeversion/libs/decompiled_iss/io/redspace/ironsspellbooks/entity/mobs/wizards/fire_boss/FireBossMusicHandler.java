/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.util.IMusicHandler;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.FadeableSoundInstance;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class FireBossMusicHandler
implements IMusicHandler {
    static final Instrument[][] MUSIC = new Instrument[][]{{Instrument.BELLS_A, Instrument.DRUMS, Instrument.BACKTRACK, Instrument.MELODY_A}, {Instrument.DRUMS, Instrument.BACKTRACK}, {Instrument.DRUMS, Instrument.BACKTRACK, Instrument.MELODY_A}, {Instrument.DRUMS, Instrument.BACKTRACK}, {Instrument.BELLS_B, Instrument.DRUMS, Instrument.BACKTRACK, Instrument.MELODY_A}, {Instrument.DRUMS, Instrument.BACKTRACK}, {Instrument.BELLS_B, Instrument.DRUMS, Instrument.BACKTRACK, Instrument.MELODY_B}, {Instrument.DRUMS, Instrument.BACKTRACK}, {Instrument.BELLS_A, Instrument.MELODY_A}, new Instrument[0], {Instrument.BACKTRACK, Instrument.MELODY_A}, {Instrument.BACKTRACK}, {Instrument.BELLS_B, Instrument.DRUMS, Instrument.BACKTRACK, Instrument.MELODY_A}, {Instrument.DRUMS, Instrument.BACKTRACK}, {Instrument.BELLS_B, Instrument.MELODY_B}};
    Set<FadeableSoundInstance> layers = new HashSet<FadeableSoundInstance>();
    static int musicIndex;
    static final int SECTION_LENGTH_TICKS = 160;
    int timer;
    int runningTicks;
    boolean starting;
    final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
    boolean withIntro;
    FadeableSoundInstance melodyA = new FadeableSoundInstance((SoundEvent)SoundRegistry.MUSIC_FIRE_BOSS_MELODY_A.get(), SoundSource.RECORDS, false);
    FadeableSoundInstance melodyB = new FadeableSoundInstance((SoundEvent)SoundRegistry.MUSIC_FIRE_BOSS_MELODY_B.get(), SoundSource.RECORDS, false);
    FadeableSoundInstance bellsA = new FadeableSoundInstance((SoundEvent)SoundRegistry.MUSIC_FIRE_BOSS_BELLS_A.get(), SoundSource.RECORDS, false);
    FadeableSoundInstance bellsB = new FadeableSoundInstance((SoundEvent)SoundRegistry.MUSIC_FIRE_BOSS_BELLS_B.get(), SoundSource.RECORDS, false);
    FadeableSoundInstance backtrack;
    FadeableSoundInstance drums = new FadeableSoundInstance((SoundEvent)SoundRegistry.MUSIC_FIRE_BOSS_DRUMS.get(), SoundSource.RECORDS, false);

    public FireBossMusicHandler() {
        this(false);
    }

    public FireBossMusicHandler(boolean withIntro) {
        this.backtrack = new FadeableSoundInstance((SoundEvent)SoundRegistry.MUSIC_FIRE_BOSS_BACKTRACK.get(), SoundSource.RECORDS, false);
        this.withIntro = withIntro;
    }

    private void addLayer(FadeableSoundInstance soundInstance) {
        this.layers.stream().filter(sound -> sound.isStopped() || !this.soundManager.isActive((SoundInstance)sound)).toList().forEach(this.layers::remove);
        this.soundManager.play((SoundInstance)soundInstance);
        this.layers.add(soundInstance);
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
        this.layers.forEach(sound -> {
            sound.triggerStart();
            if (!this.soundManager.isActive((SoundInstance)sound)) {
                this.soundManager.play((SoundInstance)sound);
            }
        });
    }

    @Override
    public void init() {
        this.soundManager.stop(null, SoundSource.MUSIC);
        musicIndex = -1;
        this.starting = true;
        if (this.withIntro) {
            this.timer = 0;
            this.backtrack.fadeIn(320);
            this.addLayer(this.backtrack);
        } else {
            this.timer = 140;
        }
    }

    @Override
    public void stop() {
        this.stopLayers();
    }

    @Override
    public void tick() {
        ++this.runningTicks;
        if (++this.timer >= 159) {
            this.timer = 0;
            musicIndex = (musicIndex + 1) % MUSIC.length;
            this.playCurrentSheet();
        }
    }

    private void playCurrentSheet() {
        Instrument[] instruments;
        this.starting = false;
        IronsSpellbooks.LOGGER.debug("FIRE BOSS MUSIC {}/{}\t{}", new Object[]{musicIndex + 1, MUSIC.length, (double)this.runningTicks / 20.0});
        for (Instrument instrument : instruments = MUSIC[musicIndex]) {
            IronsSpellbooks.LOGGER.debug("\tplaying {}", (Object)instrument.toString());
            FadeableSoundInstance sound = instrument.sound.apply(this);
            sound.unstop();
            this.addLayer(sound);
        }
    }

    @Override
    public boolean isDone() {
        if (this.starting) {
            return false;
        }
        for (FadeableSoundInstance soundInstance : this.layers) {
            if (soundInstance.isStopped() || !this.soundManager.isActive((SoundInstance)soundInstance)) continue;
            return false;
        }
        return true;
    }

    static enum Instrument {
        MELODY_A(m -> m.melodyA),
        MELODY_B(m -> m.melodyB),
        BELLS_A(m -> m.bellsA),
        BELLS_B(m -> m.bellsB),
        DRUMS(m -> m.drums),
        BACKTRACK(m -> m.backtrack);

        final Function<FireBossMusicHandler, FadeableSoundInstance> sound;

        private Instrument(Function<FireBossMusicHandler, FadeableSoundInstance> sound) {
            this.sound = sound;
        }
    }
}

