/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.HolderSet$Named
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.tags.TagKey
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.animal.AbstractFish
 *  net.minecraft.world.entity.animal.Cow
 *  net.minecraft.world.entity.animal.Sheep
 *  net.minecraft.world.entity.animal.horse.AbstractHorse
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.ElderGuardian
 *  net.minecraft.world.entity.monster.Ghast
 *  net.minecraft.world.entity.monster.Guardian
 *  net.minecraft.world.entity.monster.Pillager
 *  net.minecraft.world.entity.monster.Witch
 *  net.minecraft.world.entity.monster.warden.Warden
 *  net.minecraft.world.entity.npc.Villager
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.EnchantedItemInUse
 *  net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package com.perigrine3.createcybernetics.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.Optional;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public record HarvesterEnchantmentEffect() implements EnchantmentEntityEffect
{
    public static final MapCodec<HarvesterEnchantmentEffect> CODEC = MapCodec.unit(HarvesterEnchantmentEffect::new);

    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
        if (enchantmentLevel <= 0) {
            return;
        }
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity)entity;
        if (living.getHealth() > 0.0f) {
            return;
        }
        int count = switch (enchantmentLevel) {
            case 1 -> 1;
            case 2 -> {
                if (HarvesterEnchantmentEffect.isMonsterSpecialDrop(entity)) {
                    yield 1;
                }
                yield 2;
            }
            case 3 -> {
                if (HarvesterEnchantmentEffect.isMonsterSpecialDrop(entity)) {
                    yield 1;
                }
                yield 3;
            }
            default -> HarvesterEnchantmentEffect.isMonsterSpecialDrop(entity) ? 1 : 4;
        };
        ItemStack fixed = HarvesterEnchantmentEffect.fixedDropFor(entity);
        if (!fixed.isEmpty()) {
            HarvesterEnchantmentEffect.spawn(serverLevel, vec3, fixed);
            return;
        }
        Optional opt = BuiltInRegistries.ITEM.getTag(HarvesterEnchantmentEffect.tagFor(entity));
        if (opt.isEmpty()) {
            return;
        }
        HolderSet.Named tagSet = (HolderSet.Named)opt.get();
        int size = tagSet.size();
        if (size <= 0) {
            return;
        }
        RandomSource rand = serverLevel.getRandom();
        for (int i = 0; i < count; ++i) {
            Item item = (Item)tagSet.get(rand.nextInt(size)).value();
            if (item == null) continue;
            HarvesterEnchantmentEffect.spawn(serverLevel, vec3, new ItemStack((ItemLike)item));
        }
    }

    private static boolean isMonsterSpecialDrop(Entity entity) {
        return entity instanceof Warden || entity instanceof Guardian || entity instanceof ElderGuardian || entity instanceof Ghast || entity instanceof EnderDragon;
    }

    private static ItemStack fixedDropFor(Entity entity) {
        if (entity instanceof Warden) {
            return new ItemStack((ItemLike)ModItems.BODYPART_WARDENESOPHAGUS.get());
        }
        if (entity instanceof Guardian || entity instanceof ElderGuardian) {
            return new ItemStack((ItemLike)ModItems.BODYPART_GUARDIANRETINA.get());
        }
        if (entity instanceof Ghast) {
            return new ItemStack((ItemLike)ModItems.BODYPART_GYROSCOPICBLADDER.get());
        }
        if (entity instanceof EnderDragon) {
            return new ItemStack((ItemLike)ModItems.WETWARE_FIREBREATHINGLUNGS.get());
        }
        return ItemStack.EMPTY;
    }

    private static TagKey<Item> tagFor(Entity entity) {
        if (entity instanceof AbstractFish) {
            return ModTags.Items.FISH_BODYPART_DROPS;
        }
        if (entity instanceof AbstractHorse || entity instanceof Sheep || entity instanceof Cow) {
            return ModTags.Items.GRASSFED_BODYPART_DROPS;
        }
        if (entity instanceof Villager || entity instanceof Pillager || entity instanceof Witch) {
            return ModTags.Items.HUMANOID_BODYPART_DROPS;
        }
        return ModTags.Items.BODYPART_DROPS;
    }

    private static void spawn(ServerLevel level, Vec3 pos, ItemStack stack) {
        ItemEntity drop = new ItemEntity((Level)level, pos.x, pos.y, pos.z, stack);
        drop.setDefaultPickUpDelay();
        drop.setDeltaMovement(drop.getDeltaMovement().add(0.0, 0.15, 0.0));
        level.addFreshEntity((Entity)drop);
    }

    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}

