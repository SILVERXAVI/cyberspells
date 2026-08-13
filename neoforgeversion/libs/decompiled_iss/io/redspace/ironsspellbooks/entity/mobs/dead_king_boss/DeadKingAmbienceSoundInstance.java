/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance$Attenuation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class DeadKingAmbienceSoundInstance
extends AbstractTickableSoundInstance {
    public static final int SOUND_RANGE_SQR = 400;
    public static final int MAX_VOLUME_RANGE_SQR = 144;
    private static final float END_TRANSITION_TIME = 0.01f;
    final Vec3 vec3;
    boolean ending = false;
    boolean triggerEnd = false;

    protected DeadKingAmbienceSoundInstance(Vec3 vec3) {
        super((SoundEvent)SoundRegistry.DEAD_KING_AMBIENCE.get(), SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0f;
        this.vec3 = vec3;
    }

    public void tick() {
        if (this.triggerEnd) {
            if (!this.ending) {
                this.ending = true;
            }
            this.volume -= 0.01f;
        } else {
            MinecraftInstanceHelper.ifPlayerPresent(player -> {
                double d = player.distanceToSqr(this.vec3);
                this.volume = 1.0f - (float)Mth.clamp((double)((d - 144.0) / 400.0), (double)0.0, (double)1.0);
            });
        }
        if (this.volume <= 0.0f) {
            this.stop();
        }
    }

    public boolean canStartSilent() {
        return true;
    }

    public void triggerStop() {
        this.triggerEnd = true;
    }
}

