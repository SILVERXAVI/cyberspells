/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.perigrine3.createcybernetics.mixin;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.IChipwareSlotsMenu;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.mixin.AbstractContainerMenuAccessor;
import com.perigrine3.createcybernetics.mixin.AbstractContainerMenuInvoker;
import com.perigrine3.createcybernetics.screen.container.ChipwareContainer;
import com.perigrine3.createcybernetics.screen.slot.DataShardSlot;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.function.BooleanSupplier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={InventoryMenu.class})
public abstract class InventoryMenuChipwareSlotsMixin
implements IChipwareSlotsMenu {
    @Shadow
    private Player owner;
    @Unique
    private static final int CC_SLOT_X = 77;
    @Unique
    private static final int CC_SLOT_Y0 = 8;
    @Unique
    private static final int CC_SLOT_SPACING = 18;
    @Unique
    private Container cc_chipInv;
    @Unique
    private int cc_chipStart = -1;

    @Inject(method={"<init>(Lnet/minecraft/world/entity/player/Inventory;ZLnet/minecraft/world/entity/player/Player;)V"}, at={@At(value="TAIL")})
    private void cc$initChipwareSlots(Inventory inv, boolean active, Player ownerParam, CallbackInfo ci) {
        this.cc_chipInv = ownerParam.level().isClientSide ? new SimpleContainer(2) : new ChipwareContainer(ownerParam);
        this.cc_chipStart = ((AbstractContainerMenuAccessor)((Object)this)).cc$getSlots().size();
        BooleanSupplier activeCheck = this::cc$chipwareSlotsActive;
        ((AbstractContainerMenuInvoker)((Object)this)).cc$invokeAddSlot(new DataShardSlot(this.cc_chipInv, 0, 77, 8, activeCheck));
        ((AbstractContainerMenuInvoker)((Object)this)).cc$invokeAddSlot(new DataShardSlot(this.cc_chipInv, 1, 77, 26, activeCheck));
    }

    @Override
    public boolean cc$chipwareSlotsActive() {
        PlayerCyberwareData data;
        Player p = this.owner;
        if (p == null) {
            return false;
        }
        PlayerCyberwareData playerCyberwareData = data = !p.level().isClientSide || p.hasData(ModAttachments.CYBERWARE) ? (PlayerCyberwareData)p.getData(ModAttachments.CYBERWARE) : null;
        if (data == null) {
            return false;
        }
        return data.hasSpecificItem((Item)ModItems.BRAINUPGRADES_CHIPWARESLOTS.get(), CyberwareSlot.BRAIN);
    }

    @Inject(method={"quickMoveStack"}, at={@At(value="HEAD")}, cancellable=true)
    private void cc$quickMoveIntoChipSlots(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        boolean moved;
        if (player != null && (player.isCreative() || player.getAbilities().instabuild)) {
            return;
        }
        if (this.cc_chipStart < 0) {
            return;
        }
        if (!this.cc$chipwareSlotsActive()) {
            return;
        }
        Slot slot = ((InventoryMenu)this).getSlot(index);
        if (slot == null || !slot.hasItem()) {
            return;
        }
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        int chipStart = this.cc_chipStart;
        int chipEnd = chipStart + 2;
        int PLAYER_INV_START = 9;
        int PLAYER_INV_END_EXCL = 45;
        AbstractContainerMenuInvoker invoker = (AbstractContainerMenuInvoker)((Object)this);
        if (index >= chipStart && index < chipEnd) {
            moved = invoker.cc$invokeMoveItemStackTo(stack, 9, 45, false);
        } else if (stack.is(ModTags.Items.DATA_SHARDS)) {
            moved = invoker.cc$invokeMoveItemStackTo(stack, chipStart, chipEnd, false);
        } else {
            return;
        }
        if (!moved) {
            cir.setReturnValue((Object)ItemStack.EMPTY);
            return;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == copy.getCount()) {
            cir.setReturnValue((Object)ItemStack.EMPTY);
            return;
        }
        slot.onTake(player, stack);
        cir.setReturnValue((Object)copy);
    }
}

