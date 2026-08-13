/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Minecraft.class})
public class MinecraftMixin {
    @Inject(method={"shouldEntityAppearGlowing"}, at={@At(value="RETURN")}, cancellable=true)
    public void irons_spellbooks$changeGlowOutline(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getInstance().player == null || pEntity == null || ((Boolean)cir.getReturnValue()).booleanValue()) {
            return;
        }
        if (((Boolean)ClientConfigs.SUMMONS_GLOW.get()).booleanValue() && ClientMagicData.getActiveSummons().contains(pEntity.getUUID())) {
            cir.setReturnValue((Object)true);
        } else if (Minecraft.getInstance().player.hasEffect(MobEffectRegistry.PLANAR_SIGHT) && pEntity instanceof LivingEntity && Mth.abs((float)((float)(pEntity.getY() - Minecraft.getInstance().player.getY()))) < 18.0f) {
            cir.setReturnValue((Object)true);
        }
    }
}

