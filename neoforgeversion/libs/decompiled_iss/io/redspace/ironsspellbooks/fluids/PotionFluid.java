/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.core.Holder
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.util.StringRepresentable
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.PotionContents
 *  net.minecraft.world.item.alchemy.Potions
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.neoforge.common.Tags$Fluids
 *  net.neoforged.neoforge.fluids.BaseFlowingFluid$Properties
 *  net.neoforged.neoforge.fluids.FluidStack
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.fluids;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import io.redspace.ironsspellbooks.fluids.NoopFluid;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.FluidRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class PotionFluid
extends NoopFluid {
    public PotionFluid(BaseFlowingFluid.Properties properties) {
        super(properties);
    }

    public static FluidStack of(int amount, PotionContents potionContents, BottleType bottleType) {
        FluidStack fluidStack = new FluidStack(FluidRegistry.POTION_FLUID, amount);
        PotionFluid.addPotionToFluidStack(fluidStack, potionContents);
        fluidStack.set(ComponentRegistry.POTION_BOTTLE_TYPE, (Object)bottleType);
        return fluidStack;
    }

    public static FluidStack of(int amount, Holder<Potion> potion, BottleType bottleType) {
        return PotionFluid.of(amount, new PotionContents(potion), bottleType);
    }

    public static FluidStack addPotionToFluidStack(FluidStack fs, PotionContents potionContents) {
        if (potionContents == PotionContents.EMPTY) {
            fs.remove(DataComponents.POTION_CONTENTS);
            return fs;
        }
        fs.set(DataComponents.POTION_CONTENTS, (Object)potionContents);
        return fs;
    }

    public static FluidStack from(ItemStack stack) {
        if (!stack.has(DataComponents.POTION_CONTENTS)) {
            return FluidStack.EMPTY;
        }
        BottleType type = stack.is(Items.LINGERING_POTION) ? BottleType.LINGERING : (stack.is(Items.SPLASH_POTION) ? BottleType.SPLASH : BottleType.REGULAR);
        FluidStack fs = new FluidStack(FluidRegistry.POTION_FLUID, 250);
        fs.set(DataComponents.POTION_CONTENTS, (Object)((PotionContents)stack.get(DataComponents.POTION_CONTENTS)));
        fs.set(ComponentRegistry.POTION_BOTTLE_TYPE, (Object)type);
        return fs;
    }

    public static ItemStack from(FluidStack stack) {
        if (stack.getAmount() < 250 || !stack.is(Tags.Fluids.WATER) && !stack.has(DataComponents.POTION_CONTENTS)) {
            return ItemStack.EMPTY;
        }
        BottleType type = (BottleType)((Object)stack.getOrDefault(ComponentRegistry.POTION_BOTTLE_TYPE, (Object)BottleType.REGULAR));
        Item item = type == BottleType.LINGERING ? Items.LINGERING_POTION : (type == BottleType.SPLASH ? Items.SPLASH_POTION : Items.POTION);
        ItemStack is = new ItemStack((ItemLike)item);
        is.set(DataComponents.POTION_CONTENTS, (Object)((PotionContents)stack.getOrDefault(DataComponents.POTION_CONTENTS, (Object)new PotionContents(Potions.WATER))));
        return is;
    }

    public static enum BottleType implements StringRepresentable
    {
        REGULAR("regular", "potion"),
        SPLASH("splash", "splash_potion"),
        LINGERING("lingering", "lingering_potion");

        final String id;
        final String descriptionId;
        public static final Codec<BottleType> CODEC;
        public static final StreamCodec<ByteBuf, BottleType> STREAM_CODEC;

        private BottleType(String id, String descriptionId) {
            this.id = id;
            this.descriptionId = descriptionId;
        }

        public String descriptionId() {
            return this.descriptionId;
        }

        @NotNull
        public String getSerializedName() {
            return this.id;
        }

        static {
            CODEC = StringRepresentable.fromEnum(BottleType::values);
            STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
        }
    }
}

