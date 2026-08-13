/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.inventory.BookViewScreen$BookAccess
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.item.ILecternPlaceable;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BookViewScreen.BookAccess.class})
public class ClientBookAccessMixin {
    @Inject(method={"Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;fromItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;"}, remap=false, at={@At(value="HEAD")}, cancellable=true)
    private static void modifyLecternContents(ItemStack stack, CallbackInfoReturnable<BookViewScreen.BookAccess> cir) {
        Item item = stack.getItem();
        if (item instanceof ILecternPlaceable) {
            ILecternPlaceable lecternPlaceable = (ILecternPlaceable)item;
            cir.setReturnValue((Object)new BookViewScreen.BookAccess(lecternPlaceable.getPages(stack)));
        }
    }
}

