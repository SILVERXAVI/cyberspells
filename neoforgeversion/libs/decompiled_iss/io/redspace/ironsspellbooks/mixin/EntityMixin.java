/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public abstract class EntityMixin {
    @Inject(method={"isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    public void isAlliedTo(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        IMagicSummon summon;
        Entity self = (Entity)this;
        if (entity instanceof IMagicSummon && (summon = (IMagicSummon)entity).getSummoner() != null) {
            cir.setReturnValue((Object)(self.isAlliedTo(summon.getSummoner()) || self.equals((Object)summon.getSummoner()) ? 1 : 0));
        }
    }

    @Inject(method={"isInvisibleTo"}, at={@At(value="HEAD")}, cancellable=true)
    public void isInvisibleTo(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (ItemRegistry.INVISIBILITY_RING.get().isEquippedBy((LivingEntity)player)) {
            cir.setReturnValue((Object)false);
        }
    }
}

