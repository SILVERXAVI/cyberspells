/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.CrossbowItem
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.item.weapons;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AutoloaderCrossbow
extends CrossbowItem {
    public static final String LOADING = "is_loading";
    public static final String LOADING_TIMESTAMP = "load_timestamp";

    public AutoloaderCrossbow(Item.Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pHand) {
        ItemStack itemstack = player.getItemInHand(pHand);
        if (AutoloaderCrossbow.isCharged((ItemStack)itemstack)) {
            this.performShooting(pLevel, (LivingEntity)player, pHand, itemstack, 3.0f, 1.0f, null);
            if (!player.getProjectile(itemstack).isEmpty()) {
                AutoloaderCrossbow.startLoading(player, itemstack);
            } else {
                player.playSound(SoundEvents.ITEM_BREAK, 0.75f, 1.5f);
            }
            return InteractionResultHolder.consume((Object)itemstack);
        }
        if (AutoloaderCrossbow.isLoading(itemstack)) {
            if (player.isCrouching()) {
                AutoloaderCrossbow.setLoadingTicks(itemstack, 0);
                AutoloaderCrossbow.setLoading(itemstack, false);
            }
        } else {
            if (!player.getProjectile(itemstack).isEmpty()) {
                AutoloaderCrossbow.startLoading(player, itemstack);
                return InteractionResultHolder.consume((Object)itemstack);
            }
            return InteractionResultHolder.fail((Object)itemstack);
        }
        return InteractionResultHolder.pass((Object)itemstack);
    }

    public static void startLoading(Player player, ItemStack itemstack) {
        AutoloaderCrossbow.setLoading(itemstack, true);
        AutoloaderCrossbow.setLoadingTicks(itemstack, 0);
    }

    public void inventoryTick(@NotNull ItemStack itemstack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        AutoloaderCrossbow.handleTicking(itemstack, pLevel, pEntity);
        super.inventoryTick(itemstack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        int i = AutoloaderCrossbow.getLoadingTicks(stack);
        AutoloaderCrossbow.handleTicking(stack, entity.level, (Entity)entity);
        if (i != AutoloaderCrossbow.getLoadingTicks(stack)) {
            ItemStack cloneStack = stack.copy();
            entity.setItem(cloneStack);
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    protected static void handleTicking(ItemStack itemStack, Level level, @NotNull Entity entity) {
        if (!level.isClientSide && AutoloaderCrossbow.isLoading(itemStack)) {
            float f;
            int i = AutoloaderCrossbow.getLoadingTicks(itemStack);
            float f2 = i;
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                f = AutoloaderCrossbow.getChargeDuration(itemStack, livingEntity);
            } else {
                f = 75.0f;
            }
            if (f2 > f) {
                SoundSource soundsource;
                AutoloaderCrossbow.setLoading(itemStack, false);
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity)entity;
                    if (!AutoloaderCrossbow.isCharged((ItemStack)itemStack)) {
                        AutoloaderCrossbow.tryLoadProjectiles((LivingEntity)livingEntity, (ItemStack)itemStack);
                    }
                }
                SoundSource soundSource = soundsource = entity instanceof Player ? SoundSource.PLAYERS : SoundSource.BLOCKS;
                if (AutoloaderCrossbow.isCharged((ItemStack)itemStack)) {
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundsource, 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.5f + 1.0f) + 0.2f);
                } else {
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_BREAK, soundsource, 1.0f, 1.7f);
                }
            }
            AutoloaderCrossbow.setLoadingTicks(itemStack, ++i);
        }
    }

    public static int getChargeDuration(ItemStack pCrossbowStack, LivingEntity entity) {
        return (entity == null ? 25 : CrossbowItem.getChargeDuration((ItemStack)pCrossbowStack, (LivingEntity)entity)) * 3;
    }

    public static boolean isLoading(ItemStack pCrossbowStack) {
        return pCrossbowStack.has(ComponentRegistry.CROSSBOW_LOAD_STATE) && ((LoadStateComponent)pCrossbowStack.get(ComponentRegistry.CROSSBOW_LOAD_STATE)).isLoading();
    }

    public static void setLoading(ItemStack pCrossbowStack, boolean isLoading) {
        LoadStateComponent.set(pCrossbowStack, ((LoadStateComponent)pCrossbowStack.getOrDefault(ComponentRegistry.CROSSBOW_LOAD_STATE, (Object)new LoadStateComponent(false, 0))).setLoading(isLoading));
    }

    public static int getLoadingTicks(ItemStack pCrossbowStack) {
        return pCrossbowStack.has(ComponentRegistry.CROSSBOW_LOAD_STATE) ? ((LoadStateComponent)pCrossbowStack.get(ComponentRegistry.CROSSBOW_LOAD_STATE)).loadTimestamp() : 0;
    }

    public static void setLoadingTicks(ItemStack pCrossbowStack, int timestamp) {
        LoadStateComponent.set(pCrossbowStack, ((LoadStateComponent)pCrossbowStack.getOrDefault(ComponentRegistry.CROSSBOW_LOAD_STATE, (Object)new LoadStateComponent(false, 0))).setTimestamp(timestamp));
    }

    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltip, TooltipFlag pFlag) {
        TooltipsUtils.addShiftTooltip(pTooltip, List.of(Component.translatable((String)"item.irons_spellbooks.autoloader_crossbow.desc").withStyle(ChatFormatting.YELLOW)));
        super.appendHoverText(pStack, context, pTooltip, pFlag);
    }

    public record LoadStateComponent(boolean isLoading, int loadTimestamp) {
        public static final Codec<LoadStateComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Codec.BOOL.optionalFieldOf(AutoloaderCrossbow.LOADING, (Object)false).forGetter(LoadStateComponent::isLoading), (App)Codec.INT.optionalFieldOf(AutoloaderCrossbow.LOADING_TIMESTAMP, (Object)0).forGetter(LoadStateComponent::loadTimestamp)).apply((Applicative)builder, LoadStateComponent::new));
        public static final StreamCodec<FriendlyByteBuf, LoadStateComponent> STREAM_CODEC = StreamCodec.of((buf, data) -> {
            buf.writeBoolean(data.isLoading);
            buf.writeInt(data.loadTimestamp);
        }, buf -> new LoadStateComponent(buf.readBoolean(), buf.readInt()));

        public static void set(ItemStack stack, LoadStateComponent data) {
            stack.set(ComponentRegistry.CROSSBOW_LOAD_STATE, (Object)data);
        }

        public LoadStateComponent setLoading(boolean loading) {
            return new LoadStateComponent(loading, this.loadTimestamp);
        }

        public LoadStateComponent setTimestamp(int timestamp) {
            return new LoadStateComponent(this.isLoading, timestamp);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof LoadStateComponent)) return false;
            LoadStateComponent loadStateComponent = (LoadStateComponent)obj;
            if (loadStateComponent.isLoading != this.isLoading) return false;
            if (loadStateComponent.loadTimestamp != this.loadTimestamp) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return this.loadTimestamp * 10 + (this.isLoading ? 1 : 0);
        }
    }
}

