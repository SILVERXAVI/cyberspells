/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import io.redspace.ironsspellbooks.entity.mobs.goals.melee.AttackKeyframe;
import net.minecraft.world.phys.Vec3;

public class FireBossAttackKeyframe
extends AttackKeyframe {
    final SwingData swingData;

    public FireBossAttackKeyframe(int timeStamp, Vec3 lungeVector, SwingData swingData) {
        this(timeStamp, lungeVector, Vec3.ZERO, swingData);
    }

    public FireBossAttackKeyframe(int timeStamp, Vec3 lungeVector, Vec3 extraKnockback, SwingData swingData) {
        super(timeStamp, lungeVector, extraKnockback);
        this.swingData = swingData;
    }

    public record SwingData(boolean vertical, boolean mirrored) {
    }
}

