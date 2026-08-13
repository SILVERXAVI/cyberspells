/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 */
package io.redspace.ironsspellbooks.item.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface IArmorCapeProvider {
    public ResourceLocation getCapeResourceLocation();

    public static class CapeData {
        public double xCloakO;
        public double yCloakO;
        public double zCloakO;
        public double xCloak;
        public double yCloak;
        public double zCloak;
        public float bob;
        public float oBob;
        public int lastTick;

        public void moveCloak(LivingEntity livingEntity) {
            this.oBob = this.bob;
            float f = livingEntity.onGround() && !livingEntity.isDeadOrDying() ? (float)Math.min(0.1, livingEntity.getDeltaMovement().horizontalDistance()) : 0.0f;
            this.bob += (f - this.bob) * 0.4f;
            this.xCloakO = this.xCloak;
            this.yCloakO = this.yCloak;
            this.zCloakO = this.zCloak;
            double d0 = livingEntity.getX() - this.xCloak;
            double d1 = livingEntity.getY() - this.yCloak;
            double d2 = livingEntity.getZ() - this.zCloak;
            double d3 = 10.0;
            if (d0 > 10.0) {
                this.xCloakO = this.xCloak = livingEntity.getX();
            }
            if (d2 > 10.0) {
                this.zCloakO = this.zCloak = livingEntity.getZ();
            }
            if (d1 > 10.0) {
                this.yCloakO = this.yCloak = livingEntity.getY();
            }
            if (d0 < -10.0) {
                this.xCloakO = this.xCloak = livingEntity.getX();
            }
            if (d2 < -10.0) {
                this.zCloakO = this.zCloak = livingEntity.getZ();
            }
            if (d1 < -10.0) {
                this.yCloakO = this.yCloak = livingEntity.getY();
            }
            this.xCloak += d0 * 0.25;
            this.zCloak += d2 * 0.25;
            this.yCloak += d1 * 0.25;
        }
    }
}

