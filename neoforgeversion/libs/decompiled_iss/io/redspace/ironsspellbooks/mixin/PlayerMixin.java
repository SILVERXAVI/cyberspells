/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.player.PlayerModelPart
 *  net.minecraft.world.item.Item
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.item.armor.IDisableHat;
import io.redspace.ironsspellbooks.item.armor.IDisableJacket;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Player.class})
public class PlayerMixin {
    @Inject(method={"canEat"}, at={@At(value="RETURN")}, cancellable=true)
    void canEatForGluttony(boolean pCanAlwaysEat, CallbackInfoReturnable<Boolean> cir) {
        if (((Player)this).hasEffect(MobEffectRegistry.GLUTTONY)) {
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(method={"isModelPartShown"}, at={@At(value="RETURN")}, cancellable=true)
    void irons_spellbooks$hideJacketLayers(PlayerModelPart part, CallbackInfoReturnable<Boolean> cir) {
        if (((Boolean)cir.getReturnValue()).booleanValue()) {
            Player self = (Player)this;
            switch (part) {
                case HAT: {
                    cir.setReturnValue((Object)(!(self.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof IDisableHat) ? 1 : 0));
                    break;
                }
                case JACKET: 
                case LEFT_SLEEVE: 
                case RIGHT_SLEEVE: {
                    IDisableJacket chestplate;
                    Item item = self.getItemBySlot(EquipmentSlot.CHEST).getItem();
                    if (!(item instanceof IDisableJacket) || !(chestplate = (IDisableJacket)item).disableForSlot(EquipmentSlot.CHEST)) break;
                    cir.setReturnValue((Object)false);
                    break;
                }
                case LEFT_PANTS_LEG: 
                case RIGHT_PANTS_LEG: {
                    IDisableJacket boots;
                    IDisableJacket leggings;
                    Item item = self.getItemBySlot(EquipmentSlot.LEGS).getItem();
                    if ((!(item instanceof IDisableJacket) || !(leggings = (IDisableJacket)item).disableForSlot(EquipmentSlot.LEGS)) && (!((item = self.getItemBySlot(EquipmentSlot.FEET).getItem()) instanceof IDisableJacket) || !(boots = (IDisableJacket)item).disableForSlot(EquipmentSlot.FEET))) break;
                    cir.setReturnValue((Object)false);
                }
            }
        }
    }
}

