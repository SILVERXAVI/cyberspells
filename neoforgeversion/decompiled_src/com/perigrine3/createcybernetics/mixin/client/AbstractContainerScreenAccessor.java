/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package com.perigrine3.createcybernetics.mixin.client;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={AbstractContainerScreen.class})
public interface AbstractContainerScreenAccessor {
    @Accessor(value="leftPos")
    public int cc$getLeftPos();

    @Accessor(value="topPos")
    public int cc$getTopPos();

    @Accessor(value="menu")
    public AbstractContainerMenu cc$getMenu();
}

