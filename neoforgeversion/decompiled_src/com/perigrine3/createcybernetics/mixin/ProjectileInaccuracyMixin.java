/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.entity.projectile.Projectile
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArgs
 *  org.spongepowered.asm.mixin.injection.invoke.arg.Args
 */
package com.perigrine3.createcybernetics.mixin;

import com.perigrine3.createcybernetics.common.attributes.ModAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={Projectile.class})
public abstract class ProjectileInaccuracyMixin {
    @ModifyArgs(method={"shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V"))
    private void cc$scaleInaccuracyInShootFromRotation(Args args, Entity shooter, float xRot, float yRot, float roll, float velocity, float inaccuracy) {
        float baseInacc;
        float newInacc;
        if (!(this instanceof AbstractArrow)) {
            return;
        }
        if (!(shooter instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity)shooter;
        double mult = living.getAttributeValue(ModAttributes.ARROW_INACCURACY);
        if (!Double.isFinite(mult) || mult < 0.0) {
            mult = 1.0;
        }
        if ((newInacc = (float)((double)(baseInacc = ((Float)args.get(4)).floatValue()) * mult)) < 0.0f) {
            newInacc = 0.0f;
        }
        args.set(4, (Object)Float.valueOf(newInacc));
    }
}

