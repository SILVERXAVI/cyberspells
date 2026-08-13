/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 */
package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class ExtendedWeaponTier
implements Tier,
IronsWeaponTier {
    public static ExtendedWeaponTier HELLRAZOR = new ExtendedWeaponTier(2031, 12.0f, -2.6f, 16, (TagKey<Block>)BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.NETHERITE_SCRAP}), new AttributeContainer[0]);
    public static ExtendedWeaponTier LEGIONNAIRE_FLAMBERGE = new ExtendedWeaponTier(2031, 10.0f, -2.5f, 4, (TagKey<Block>)BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.NETHERITE_SCRAP}), new AttributeContainer((Holder<Attribute>)Attributes.ARMOR, 4.0, AttributeModifier.Operation.ADD_VALUE));
    public static ExtendedWeaponTier DECREPIT_FLAMBERGE = new ExtendedWeaponTier(1000, 10.0f, -2.7f, 4, (TagKey<Block>)BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.NETHERITE_SCRAP}), new AttributeContainer((Holder<Attribute>)Attributes.ARMOR, 4.0, AttributeModifier.Operation.ADD_VALUE));
    public static ExtendedWeaponTier DECREPIT_SCYTHE = new ExtendedWeaponTier(1000, 10.0f, -2.6f, 4, (TagKey<Block>)BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.NETHERITE_SCRAP}), new AttributeContainer[0]);
    public static ExtendedWeaponTier DREADSWORD = new ExtendedWeaponTier(1061, 6.0f, -2.4f, 14, (TagKey<Block>)BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.ARCANE_INGOT.get()}), new AttributeContainer[0]);
    public static ExtendedWeaponTier MISERY = new ExtendedWeaponTier(1061, 7.0f, -2.1f, 14, (TagKey<Block>)BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.ARCANE_INGOT.get()}), new AttributeContainer[0]);
    public static ExtendedWeaponTier METAL_MAGEHUNTER = new ExtendedWeaponTier(1561, 6.0f, -2.4f, 12, (TagKey<Block>)BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.ARCANE_INGOT.get()}), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_RESIST, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static ExtendedWeaponTier CRYSTAL_MAGEHUNTER = new ExtendedWeaponTier(1561, 6.0f, -2.4f, 12, (TagKey<Block>)BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.DIAMOND}), new AttributeContainer((Holder<Attribute>)AttributeRegistry.SPELL_RESIST, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static ExtendedWeaponTier SPELLBREAKER = new ExtendedWeaponTier(2031, 9.0f, -2.2f, 12, (TagKey<Block>)BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.DIAMOND}), new AttributeContainer((Holder<Attribute>)AttributeRegistry.COOLDOWN_REDUCTION, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static ExtendedWeaponTier TRUTHSEEKER = new ExtendedWeaponTier(2031, 11.0f, -3.0f, 10, (TagKey<Block>)BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.ARCANE_INGOT.get()}), new AttributeContainer[0]);
    public static ExtendedWeaponTier CLAYMORE = new ExtendedWeaponTier(1000, 9.0f, -2.7f, 8, (TagKey<Block>)BlockTags.INCORRECT_FOR_IRON_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.IRON_INGOT}), new AttributeContainer[0]);
    public static ExtendedWeaponTier AMETHYST_RAPIER = new ExtendedWeaponTier(2031, 7.0f, -1.7f, 16, (TagKey<Block>)BlockTags.INCORRECT_FOR_DIAMOND_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.AMETHYST_SHARD}), new AttributeContainer[0]);
    public static ExtendedWeaponTier ICE_GREATSWORD = new ExtendedWeaponTier(2031, 15.0f, -3.1f, 16, (TagKey<Block>)BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.ICE}), new AttributeContainer[0]);
    public static ExtendedWeaponTier TWILIGHT_GALE = new ExtendedWeaponTier(2031, 12.0f, -2.6f, 16, (TagKey<Block>)BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{(ItemLike)ItemRegistry.LIGHTNING_BOTTLE.get()}), new AttributeContainer[0]);
    public static ExtendedWeaponTier VOID_ICHOR = new ExtendedWeaponTier(2031, 8.0f, -2.0f, 12, (TagKey<Block>)BlockTags.INCORRECT_FOR_NETHERITE_TOOL, () -> Ingredient.of((ItemLike[])new ItemLike[]{Items.DIAMOND}), new AttributeContainer((Holder<Attribute>)AttributeRegistry.COOLDOWN_REDUCTION, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    int uses;
    float damage;
    float speed;
    int enchantmentValue;
    TagKey<Block> incorrectBlocksForDrops;
    Supplier<Ingredient> repairIngredient;
    AttributeContainer[] attributes;

    public ExtendedWeaponTier(int uses, float damage, float speed, int enchantmentValue, TagKey<Block> incorrectBlocksForDrops, Supplier<Ingredient> repairIngredient, AttributeContainer ... attributes) {
        this.uses = uses;
        this.damage = damage;
        this.speed = speed;
        this.enchantmentValue = enchantmentValue;
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.repairIngredient = repairIngredient;
        this.attributes = attributes;
    }

    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public AttributeContainer[] getAdditionalAttributes() {
        return this.attributes;
    }
}

