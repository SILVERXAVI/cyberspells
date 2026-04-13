/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.perigrine3.createcybernetics.mixin;

import com.google.common.collect.ImmutableMap;
import com.perigrine3.createcybernetics.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={VillagerHostilesSensor.class})
public class VillagerHostilesSensorMixin {
    @Shadow
    @Final
    @Mutable
    private static ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES;

    @Inject(method={"<clinit>"}, at={@At(value="TAIL")})
    private static void createcybernetics$addCustomHostiles(CallbackInfo ci) {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        builder.putAll(ACCEPTABLE_DISTANCE_FROM_HOSTILES);
        builder.put(ModEntities.SMASHER.get(), (Object)Float.valueOf(20.0f));
        builder.put(ModEntities.CYBERZOMBIE.get(), (Object)Float.valueOf(12.0f));
        builder.put(ModEntities.CYBERSKELETON.get(), (Object)Float.valueOf(12.0f));
        ACCEPTABLE_DISTANCE_FROM_HOSTILES = builder.build();
    }
}

