/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingAmbienceSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class DeadKingAmbienceSoundManager {
    private final Vec3 vec3;
    @OnlyIn(value=Dist.CLIENT)
    private DeadKingAmbienceSoundInstance soundInstance;

    protected DeadKingAmbienceSoundManager(Vec3 origin) {
        this.vec3 = origin;
    }

    public void trigger() {
        if (((Boolean)ClientConfigs.ENABLE_BOSS_MUSIC.get()).booleanValue() && (this.soundInstance == null || this.soundInstance.isStopped())) {
            this.soundInstance = new DeadKingAmbienceSoundInstance(this.vec3);
            Minecraft.getInstance().getSoundManager().play((SoundInstance)this.soundInstance);
        }
    }

    public void triggerStop() {
        if (this.soundInstance != null) {
            this.soundInstance.triggerStop();
        }
    }
}

