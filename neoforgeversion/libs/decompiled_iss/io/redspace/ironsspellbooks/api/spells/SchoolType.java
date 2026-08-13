/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class SchoolType {
    final ResourceLocation id;
    final TagKey<Item> focus;
    final Component displayName;
    final Style displayStyle;
    final Holder<Attribute> powerAttribute;
    final Holder<Attribute> resistanceAttribute;
    final Holder<SoundEvent> defaultCastSound;
    final ResourceKey<DamageType> damageType;
    final boolean requiresLearning;
    final boolean allowLooting;

    public SchoolType(ResourceLocation id, TagKey<Item> focus, Component displayName, Holder<Attribute> powerAttribute, Holder<Attribute> resistanceAttribute, Holder<SoundEvent> defaultCastSound, ResourceKey<DamageType> damageType, boolean requiresLearning, boolean allowLooting) {
        this.id = id;
        this.focus = focus;
        this.displayName = displayName;
        this.displayStyle = displayName.getStyle();
        this.powerAttribute = powerAttribute;
        this.resistanceAttribute = resistanceAttribute;
        this.defaultCastSound = defaultCastSound;
        this.damageType = damageType;
        this.requiresLearning = requiresLearning;
        this.allowLooting = allowLooting;
    }

    public SchoolType(ResourceLocation id, TagKey<Item> focus, Component displayName, Holder<Attribute> powerAttribute, Holder<Attribute> resistanceAttribute, Holder<SoundEvent> defaultCastSound, ResourceKey<DamageType> damageType) {
        this(id, focus, displayName, powerAttribute, resistanceAttribute, defaultCastSound, damageType, false, true);
    }

    public double getResistanceFor(LivingEntity livingEntity) {
        return livingEntity.getAttributes().hasAttribute(this.resistanceAttribute) ? livingEntity.getAttributeValue(this.resistanceAttribute) : 1.0;
    }

    public double getPowerFor(LivingEntity livingEntity) {
        return livingEntity.getAttributes().hasAttribute(this.powerAttribute) ? livingEntity.getAttributeValue(this.powerAttribute) : 1.0;
    }

    public SoundEvent getCastSound() {
        return (SoundEvent)this.defaultCastSound.value();
    }

    public ResourceKey<DamageType> getDamageType() {
        return this.damageType;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public boolean isFocus(ItemStack itemStack) {
        return itemStack.is(this.focus);
    }

    public TagKey<Item> getFocus() {
        return this.focus;
    }

    public Vector3f getTargetingColor() {
        return Utils.deconstructRGB(this.displayStyle.getColor().getValue());
    }
}

