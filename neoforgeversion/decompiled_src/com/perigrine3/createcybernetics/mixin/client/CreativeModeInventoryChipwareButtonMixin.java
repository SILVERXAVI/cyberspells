/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
 *  net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen$ItemPickerMenu
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.perigrine3.createcybernetics.mixin.client;

import com.perigrine3.createcybernetics.network.payload.OpenChipwareMiniPayload;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={CreativeModeInventoryScreen.class})
public abstract class CreativeModeInventoryChipwareButtonMixin
extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    @Unique
    private Button cc$chipwareBtn;

    protected CreativeModeInventoryChipwareButtonMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component title) {
        super((AbstractContainerMenu)menu, inventory, title);
    }

    @Inject(method={"init"}, at={@At(value="TAIL")})
    private void cc$addChipwareButton(CallbackInfo ci) {
        int x = this.leftPos + 127;
        int y = this.topPos + 7;
        this.cc$chipwareBtn = Button.builder((Component)Component.translatable((String)"gui.chipware.button"), b -> PacketDistributor.sendToServer((CustomPacketPayload)new OpenChipwareMiniPayload(), (CustomPacketPayload[])new CustomPacketPayload[0])).bounds(x, y, 48, 15).build();
        this.addRenderableWidget((GuiEventListener)this.cc$chipwareBtn);
        this.cc$updateChipwareButtonVisibility();
    }

    @Inject(method={"containerTick"}, at={@At(value="TAIL")})
    private void cc$tickButtonVisibility(CallbackInfo ci) {
        this.cc$updateChipwareButtonVisibility();
    }

    @Unique
    private void cc$updateChipwareButtonVisibility() {
        boolean open;
        if (this.cc$chipwareBtn == null) {
            return;
        }
        this.cc$chipwareBtn.visible = open = ((CreativeModeInventoryScreen)this).isInventoryOpen();
        this.cc$chipwareBtn.active = open;
    }
}

