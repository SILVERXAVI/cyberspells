/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec3
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EmpEffect
extends MobEffect {
    private static final ResourceLocation CYBERZOMBIE_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberzombie");
    private static final ResourceLocation CYBERSKELETON_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"cyberskeleton");
    private static final ResourceLocation SMASHER_ID = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"smasher");

    public EmpEffect() {
        super(MobEffectCategory.HARMFUL, -11881473);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            Mob mob;
            if (entity instanceof Player) {
                PlayerCyberwareData data;
                Player player = (Player)entity;
                if (player.hasData(ModAttachments.CYBERWARE) && (data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE)) != null) {
                    data.setEnergyStored(player, 0);
                }
            } else if (entity instanceof Mob && EmpEffect.isEmpImmobilizedMob(mob = (Mob)entity)) {
                EmpEffect.freezeMob(mob);
            }
        }
        return true;
    }

    private static boolean isEmpImmobilizedMob(Mob mob) {
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey((Object)mob.getType());
        if (key == null) {
            return false;
        }
        return key.equals((Object)CYBERZOMBIE_ID) || key.equals((Object)CYBERSKELETON_ID) || key.equals((Object)SMASHER_ID);
    }

    private static void freezeMob(Mob mob) {
        mob.getNavigation().stop();
        mob.setTarget(null);
        Vec3 v = mob.getDeltaMovement();
        if (v.x != 0.0 || v.z != 0.0) {
            mob.setDeltaMovement(0.0, v.y, 0.0);
            mob.hurtMarked = true;
        }
        mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY(), mob.getZ(), 0.0);
        mob.setSprinting(false);
    }
}

