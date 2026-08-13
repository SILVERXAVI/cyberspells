/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.monster.Zombie
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.entity.mobs;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class CatacombsZombie
extends Zombie {
    public CatacombsZombie(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(EntityType.ZOMBIE, pLevel);
        if (this.random.nextFloat() < 0.2f) {
            switch (this.random.nextIntBetweenInclusive(1, 4)) {
                case 1: {
                    this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE));
                    break;
                }
                case 2: {
                    this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1));
                    break;
                }
                case 3: {
                    this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                    break;
                }
                case 4: {
                    this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Integer.MAX_VALUE));
                }
            }
        }
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(random, pDifficulty);
        Item[] leather = new Item[]{Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET};
        Item[] chain = new Item[]{Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET};
        Item[] iron = new Item[]{Items.IRON_BOOTS, Items.IRON_LEGGINGS, Items.IRON_CHESTPLATE, Items.IRON_HELMET};
        float power = random.nextFloat();
        ItemStack[] equipment = new ItemStack[4];
        for (int i = 0; i < 4; ++i) {
            float stray;
            equipment[i] = random.nextFloat() > 0.6f ? ItemStack.EMPTY : ((double)(power + (stray = (random.nextFloat() - 0.5f) / 3.0f)) > 0.85 ? new ItemStack((ItemLike)iron[i]) : ((double)(power + stray) > 0.45 ? new ItemStack((ItemLike)chain[i]) : new ItemStack((ItemLike)leather[i])));
        }
        this.setItemSlot(EquipmentSlot.FEET, equipment[0]);
        this.setItemSlot(EquipmentSlot.LEGS, equipment[1]);
        this.setItemSlot(EquipmentSlot.CHEST, equipment[2]);
        this.setItemSlot(EquipmentSlot.HEAD, equipment[3]);
        if (random.nextFloat() < 0.01f) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)Items.CYAN_BANNER));
        }
        this.setDropChance(EquipmentSlot.FEET, 0.0f);
        this.setDropChance(EquipmentSlot.LEGS, 0.0f);
        this.setDropChance(EquipmentSlot.CHEST, 0.0f);
        this.setDropChance(EquipmentSlot.HEAD, 0.0f);
    }
}

