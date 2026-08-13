/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.Item
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public class ClientEntityMixin {
    @Inject(method={"getTeamColor"}, at={@At(value="HEAD")}, cancellable=true)
    public void changeGlowOutline(CallbackInfoReturnable<Integer> cir) {
        if (ClientMagicData.getActiveSummons().contains(((Entity)this).getUUID())) {
            cir.setReturnValue((Object)ClientConfigs.summonGlowColor);
        } else if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(MobEffectRegistry.PLANAR_SIGHT)) {
            cir.setReturnValue((Object)7095029);
        } else {
            Entity entity = (Entity)this;
            if (entity instanceof ItemEntity) {
                ItemEntity item = (ItemEntity)entity;
                if (item.getItem().is((Item)ItemRegistry.DRAGONSKIN.get())) {
                    cir.setReturnValue((Object)13769983);
                }
                if (item.getItem().is((Item)ItemRegistry.LIGHTNING_ROD_STAFF.get())) {
                    cir.setReturnValue((Object)0x55FFFF);
                }
            }
        }
    }
}

