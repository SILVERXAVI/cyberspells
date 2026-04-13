/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Camera
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.phys.Vec3
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.perigrine3.createcybernetics.mixin;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.util.ModTags;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Camera.class}, remap=false)
public abstract class PlayerEyeHeightMixin {
    @Shadow
    private Vec3 position;

    @Inject(method={"setup"}, at={@At(value="TAIL")})
    private void createcybernetics$applyLegCameraSink(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (!(entity instanceof LocalPlayer)) {
            return;
        }
        LocalPlayer localPlayer = (LocalPlayer)entity;
        PlayerCyberwareData data = (PlayerCyberwareData)localPlayer.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        double sink = PlayerEyeHeightMixin.computeLegSink(data);
        if (sink <= 0.0) {
            return;
        }
        this.position = this.position.add(0.0, -sink, 0.0);
    }

    private static double computeLegSink(PlayerCyberwareData data) {
        boolean hasLeftLeg = data.hasAnyTagged(ModTags.Items.LEFTLEG_ITEMS, CyberwareSlot.LLEG);
        boolean hasRightLeg = data.hasAnyTagged(ModTags.Items.RIGHTLEG_ITEMS, CyberwareSlot.RLEG);
        if (!hasLeftLeg && !hasRightLeg) {
            return 0.75;
        }
        return 0.0;
    }
}

