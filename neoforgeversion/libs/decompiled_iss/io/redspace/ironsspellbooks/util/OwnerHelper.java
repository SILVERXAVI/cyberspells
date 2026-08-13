/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.util;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class OwnerHelper {
    public static LivingEntity getAndCacheOwner(Level level, LivingEntity cachedOwner, UUID summonerUUID) {
        if (cachedOwner != null && cachedOwner.isAlive()) {
            return cachedOwner;
        }
        if (summonerUUID != null && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            Entity entity = serverLevel.getEntity(summonerUUID);
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity;
                cachedOwner = livingEntity = (LivingEntity)entity;
            }
            return cachedOwner;
        }
        return null;
    }

    public static void serializeOwner(CompoundTag compoundTag, UUID ownerUUID) {
        if (ownerUUID != null) {
            compoundTag.putUUID("Summoner", ownerUUID);
        }
    }

    public static UUID deserializeOwner(CompoundTag compoundTag) {
        if (compoundTag.hasUUID("Summoner")) {
            return compoundTag.getUUID("Summoner");
        }
        return null;
    }
}

