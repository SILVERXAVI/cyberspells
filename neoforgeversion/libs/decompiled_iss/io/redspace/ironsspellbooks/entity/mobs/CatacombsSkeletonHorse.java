/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.animal.horse.SkeletonHorse
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class CatacombsSkeletonHorse
extends SkeletonHorse {
    public CatacombsSkeletonHorse(EntityType<? extends SkeletonHorse> pEntityType, Level level) {
        super(EntityType.SKELETON_HORSE, level);
        this.setTamed(true);
        NecromancerEntity necromancer = (NecromancerEntity)((EntityType)EntityRegistry.NECROMANCER.get()).create(level);
        if (necromancer != null) {
            necromancer.setPersistenceRequired();
            necromancer.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)Items.CHAINMAIL_CHESTPLATE));
            necromancer.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)ItemRegistry.TARNISHED_CROWN.get()));
            necromancer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
            necromancer.startRiding((Entity)this);
        }
    }
}

