/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.entity.mobs.goals.melee;

import net.minecraft.world.phys.Vec3;

public class AttackKeyframe {
    private final int timeStamp;
    private final Vec3 lungeVector;
    private final Vec3 extraKnockback;

    public AttackKeyframe(int timeStamp, Vec3 lungeVector, Vec3 extraKnockback) {
        this.timeStamp = timeStamp;
        this.lungeVector = lungeVector;
        this.extraKnockback = extraKnockback;
    }

    public AttackKeyframe(int timeStamp, Vec3 lungeVector) {
        this(timeStamp, lungeVector, Vec3.ZERO);
    }

    public int timeStamp() {
        return this.timeStamp;
    }

    public Vec3 lungeVector() {
        return this.lungeVector;
    }

    public Vec3 extraKnockback() {
        return this.extraKnockback;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AttackKeyframe that = (AttackKeyframe)o;
        if (this.timeStamp != that.timeStamp) {
            return false;
        }
        if (!this.lungeVector.equals((Object)that.lungeVector)) {
            return false;
        }
        return this.extraKnockback.equals((Object)that.extraKnockback);
    }

    public int hashCode() {
        int result = Integer.hashCode(this.timeStamp);
        result = 31 * result + this.lungeVector.hashCode();
        result = 31 * result + this.extraKnockback.hashCode();
        return result;
    }

    public String toString() {
        return "AttackKeyframe{timeStamp=" + this.timeStamp + ", lungeVector=" + String.valueOf(this.lungeVector) + ", extraKnockback=" + String.valueOf(this.extraKnockback) + "}";
    }
}

