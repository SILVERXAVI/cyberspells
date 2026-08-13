/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMultimap
 *  com.google.common.collect.ImmutableMultimap$Builder
 *  com.google.common.collect.Multimap
 *  net.minecraft.core.Holder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  top.theillusivec4.curios.api.CuriosApi
 *  top.theillusivec4.curios.api.SlotContext
 *  top.theillusivec4.curios.api.type.capability.ICurio$SoundInfo
 *  top.theillusivec4.curios.api.type.capability.ICurioItem
 */
package io.redspace.ironsspellbooks.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CurioBaseItem
extends Item
implements ICurioItem {
    String attributeSlot = "";
    Function<Integer, Multimap<Holder<Attribute>, AttributeModifier>> attributes = null;

    public CurioBaseItem(Item.Properties properties) {
        super(properties);
    }

    public boolean isEquippedBy(@Nullable LivingEntity entity) {
        return entity != null && CuriosApi.getCuriosInventory((LivingEntity)entity).map(inv -> inv.findFirstCurio((Item)this).isPresent()).orElse(false) != false;
    }

    @NotNull
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo((SoundEvent)SoundEvents.ARMOR_EQUIP_CHAIN.value(), 1.0f, 1.0f);
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return slotContext.identifier().equals(this.attributeSlot) ? this.attributes.apply(slotContext.index()) : super.getAttributeModifiers(slotContext, id, stack);
    }

    public CurioBaseItem withAttributes(String slot, AttributeContainer ... attributes) {
        this.attributeSlot = slot;
        this.attributes = index -> {
            ImmutableMultimap.Builder builder = ImmutableMultimap.builder();
            for (AttributeContainer holder : attributes) {
                String id = String.format("%s_%s", this.attributeSlot, index);
                builder.put(holder.attribute(), (Object)holder.createModifier(id));
            }
            return builder.build();
        };
        return this;
    }

    public CurioBaseItem withSpellbookAttributes(AttributeContainer ... attributes) {
        return this.withAttributes(Curios.SPELLBOOK_SLOT, attributes);
    }
}

