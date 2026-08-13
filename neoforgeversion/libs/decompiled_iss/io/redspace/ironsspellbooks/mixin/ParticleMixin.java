/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.particle.ClientShieldHelper;
import java.util.List;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={Particle.class})
public class ParticleMixin {
    @Shadow
    private AABB bb;

    @ModifyArg(method={"move"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/Entity;collideBoundingBox(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/level/Level;Ljava/util/List;)Lnet/minecraft/world/phys/Vec3;"), index=4)
    private List<VoxelShape> mixin(List<VoxelShape> in) {
        return ClientShieldHelper.getShieldsFor(this.bb.inflate(0.25));
    }
}

