/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package io.redspace.ironsspellbooks.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={LivingEntity.class})
public interface LivingEntityAccessor {
    @Invoker(value="setLivingEntityFlag", remap=false)
    public void setLivingEntityFlagInvoker(int var1, boolean var2);
}

