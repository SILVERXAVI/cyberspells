/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.Holder
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.item.armor;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record UpgradeOrbType(Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation, Optional<ItemStack> containerItem) {
    private static final Codec<ItemStack> ITEM_OR_ITEMSTACK_CODEC = Codec.withAlternative((Codec)ItemStack.STRICT_CODEC, (Codec)BuiltInRegistries.ITEM.holderByNameCodec().xmap(ItemStack::new, ItemStack::getItemHolder));
    public static final Codec<UpgradeOrbType> CODEC = RecordCodecBuilder.create(builder -> builder.group((App)BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(UpgradeOrbType::attribute), (App)Codec.DOUBLE.fieldOf("amount").forGetter(UpgradeOrbType::amount), (App)AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(UpgradeOrbType::operation), (App)ITEM_OR_ITEMSTACK_CODEC.optionalFieldOf("containerItem").forGetter(UpgradeOrbType::containerItem)).apply((Applicative)builder, UpgradeOrbType::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeOrbType> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.holderRegistry((ResourceKey)Registries.ATTRIBUTE), type -> type.attribute, (StreamCodec)ByteBufCodecs.DOUBLE, type -> type.amount, (StreamCodec)AttributeModifier.Operation.STREAM_CODEC, type -> type.operation, (StreamCodec)ByteBufCodecs.optional((StreamCodec)ByteBufCodecs.fromCodec(ITEM_OR_ITEMSTACK_CODEC)), type -> type.containerItem, UpgradeOrbType::new);

    public UpgradeOrbType(Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation, Holder<Item> container) {
        this(attribute, amount, operation, Optional.of(new ItemStack(container)));
    }
}

