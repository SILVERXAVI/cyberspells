/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.LightningBolt
 *  net.minecraft.world.entity.monster.Creeper
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.config.ServerConfigs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Creeper.class})
public class CreeperMixin {
    @Inject(method={"thunderHit"}, at={@At(value="HEAD")})
    void betterThunderHit(ServerLevel pLevel, LightningBolt pLightning, CallbackInfo ci) {
        Creeper self;
        if (((Boolean)ServerConfigs.BETTER_CREEPER_THUNDERHIT.get()).booleanValue() && !(self = (Creeper)this).isPowered()) {
            self.heal(self.getMaxHealth());
        }
    }
}

