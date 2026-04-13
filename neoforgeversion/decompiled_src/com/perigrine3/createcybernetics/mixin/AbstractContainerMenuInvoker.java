/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package com.perigrine3.createcybernetics.mixin;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={AbstractContainerMenu.class})
public interface AbstractContainerMenuInvoker {
    @Invoker(value="addSlot")
    public Slot cc$invokeAddSlot(Slot var1);

    @Invoker(value="moveItemStackTo")
    public boolean cc$invokeMoveItemStackTo(ItemStack var1, int var2, int var3, boolean var4);
}

