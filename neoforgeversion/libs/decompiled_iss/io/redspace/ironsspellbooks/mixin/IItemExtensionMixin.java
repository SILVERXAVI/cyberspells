/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.neoforge.common.extensions.IItemStackExtension
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={IItemStackExtension.class}, remap=false, priority=0)
public interface IItemExtensionMixin {
    @Inject(method={"canElytraFly"}, at={@At(value="RETURN")}, cancellable=true, remap=false)
    default public void canElytraFly(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.hasEffect(MobEffectRegistry.ANGEL_WINGS)) {
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(method={"elytraFlightTick"}, at={@At(value="RETURN")}, cancellable=true, remap=false)
    default public void elytraFlightTick(LivingEntity entity, int flightTicks, CallbackInfoReturnable<Boolean> cir) {
        if (entity.hasEffect(MobEffectRegistry.ANGEL_WINGS)) {
            cir.setReturnValue((Object)true);
        }
    }
}

