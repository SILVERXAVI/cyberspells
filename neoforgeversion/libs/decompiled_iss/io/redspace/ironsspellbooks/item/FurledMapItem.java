/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderSet
 *  net.minecraft.core.HolderSet$Direct
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentSerialization
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.MapItem
 *  net.minecraft.world.item.component.ItemLore
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.saveddata.maps.MapDecorationTypes
 *  net.minecraft.world.level.saveddata.maps.MapItemSavedData
 */
package io.redspace.ironsspellbooks.item;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class FurledMapItem
extends Item {
    public static final ResourceKey<Level> OVERWORLD = ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)ResourceLocation.withDefaultNamespace((String)"overworld"));
    public static final ResourceKey<Level> NETHER = ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)ResourceLocation.withDefaultNamespace((String)"the_nether"));

    public FurledMapItem() {
        super(ItemPropertiesHelper.material().stacksTo(1));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel)level;
            level.playSound(null, (Entity)player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0f, 1.0f);
            ItemStack itemStack = player.getItemInHand(hand);
            player.getCooldowns().addCooldown(itemStack.getItem(), 50);
            if (itemStack.has(ComponentRegistry.FURLED_MAP_COMPONENT)) {
                Pair pair;
                FurledMapData furledMapData = (FurledMapData)itemStack.get(ComponentRegistry.FURLED_MAP_COMPONENT);
                ResourceKey structureResourceKey = ResourceKey.create((ResourceKey)Registries.STRUCTURE, (ResourceLocation)furledMapData.destinationResource);
                Optional<HolderSet.Direct> holder = serverlevel.registryAccess().registryOrThrow(Registries.STRUCTURE).getHolder(structureResourceKey).map(xva$0 -> HolderSet.direct((Holder[])new Holder[]{xva$0}));
                if (furledMapData.dimension().isPresent()) {
                    ResourceKey<Level> dimensionRestriction = furledMapData.dimension().get();
                    if (!serverlevel.dimension().equals(dimensionRestriction)) {
                        ((ServerPlayer)player).connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"item.irons_spellbooks.furled_map.dimension_fail").withStyle(ChatFormatting.RED)));
                        return InteractionResultHolder.fail((Object)itemStack);
                    }
                }
                if (holder.isPresent() && (pair = serverlevel.getChunkSource().getGenerator().findNearestMapStructure(serverlevel, (HolderSet)holder.get(), player.blockPosition(), 100, ((Boolean)ServerConfigs.FURLED_MAPS_SKIP_CHUNKS.get()).booleanValue())) != null) {
                    BlockPos blockpos = (BlockPos)pair.getFirst();
                    ItemStack mapStack = MapItem.create((Level)serverlevel, (int)blockpos.getX(), (int)blockpos.getZ(), (byte)2, (boolean)true, (boolean)true);
                    MapItem.renderBiomePreviewMap((ServerLevel)serverlevel, (ItemStack)mapStack);
                    MapItemSavedData.addTargetDecoration((ItemStack)mapStack, (BlockPos)blockpos, (String)"red_x", (Holder)MapDecorationTypes.RED_X);
                    furledMapData.descriptionOverride.ifPresent(component -> mapStack.set(DataComponents.CUSTOM_NAME, component));
                    FurledMapItem.replaceItem(player, mapStack, hand);
                    return InteractionResultHolder.sidedSuccess((Object)itemStack, (boolean)level.isClientSide);
                }
            }
            FurledMapItem.replaceItem(player, new ItemStack((ItemLike)Items.MAP), hand);
        }
        return super.use(level, player, hand);
    }

    private static void replaceItem(Player player, ItemStack itemStack, InteractionHand hand) {
        boolean flag = player.getAbilities().instabuild;
        if (!flag) {
            player.setItemInHand(hand, itemStack);
        } else {
            player.getInventory().add(itemStack);
        }
    }

    public static ItemStack of(ResourceLocation structure, MutableComponent descriptor) {
        ItemStack itemStack = new ItemStack((ItemLike)ItemRegistry.FURLED_MAP.get());
        FurledMapData.set(itemStack, new FurledMapData(structure, Optional.empty(), Optional.of(descriptor)));
        FurledMapData.setLoreHelper(itemStack, (Component)Component.translatable((String)"item.irons_spellbooks.furled_map_descriptor_framing", (Object[])new Object[]{descriptor}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
        return itemStack;
    }

    public static ItemStack of(ResourceLocation structure, ResourceKey<Level> exclusiveDimension, MutableComponent descriptor) {
        return FurledMapItem.of(structure, exclusiveDimension, descriptor, false);
    }

    public static ItemStack of(ResourceLocation structure, ResourceKey<Level> exclusiveDimension, MutableComponent descriptor, boolean ancient) {
        ItemStack itemStack = new ItemStack(ancient ? (ItemLike)ItemRegistry.ANCIENT_FURLED_MAP.get() : (ItemLike)ItemRegistry.FURLED_MAP.get());
        FurledMapData.set(itemStack, new FurledMapData(structure, Optional.of(exclusiveDimension), Optional.of(descriptor)));
        FurledMapData.setLoreHelper(itemStack, (Component)Component.translatable((String)"item.irons_spellbooks.furled_map_descriptor_framing", (Object[])new Object[]{descriptor}).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
        return itemStack;
    }

    public record FurledMapData(ResourceLocation destinationResource, Optional<ResourceKey<Level>> dimension, Optional<Component> descriptionOverride) {
        public static final Codec<FurledMapData> CODEC = RecordCodecBuilder.create(builder -> builder.group((App)ResourceLocation.CODEC.fieldOf("destination").forGetter(FurledMapData::destinationResource), (App)ResourceKey.codec((ResourceKey)Registries.DIMENSION).optionalFieldOf("dimension").forGetter(FurledMapData::dimension), (App)ComponentSerialization.CODEC.optionalFieldOf("descriptionOverride").forGetter(FurledMapData::descriptionOverride)).apply((Applicative)builder, FurledMapData::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, ResourceLocation> RESOURCELOCATION_STREAM_CODEC = StreamCodec.of((buf, loc) -> buf.writeUtf(loc.toString()), buf -> ResourceLocation.parse((String)buf.readUtf()));
        public static final StreamCodec<RegistryFriendlyByteBuf, FurledMapData> STREAM_CODEC = StreamCodec.composite(RESOURCELOCATION_STREAM_CODEC, FurledMapData::destinationResource, (StreamCodec)ByteBufCodecs.optional((StreamCodec)ResourceKey.streamCodec((ResourceKey)Registries.DIMENSION)), FurledMapData::dimension, (StreamCodec)ByteBufCodecs.optional((StreamCodec)ComponentSerialization.STREAM_CODEC), FurledMapData::descriptionOverride, FurledMapData::new);

        public static boolean has(ItemStack stack) {
            return stack.has(ComponentRegistry.FURLED_MAP_COMPONENT);
        }

        public static FurledMapData get(ItemStack stack) {
            return (FurledMapData)stack.get(ComponentRegistry.FURLED_MAP_COMPONENT);
        }

        public static void set(ItemStack stack, FurledMapData data) {
            stack.set(ComponentRegistry.FURLED_MAP_COMPONENT, (Object)data);
        }

        public static void setLoreHelper(ItemStack stack, Component line) {
            stack.set(DataComponents.LORE, (Object)new ItemLore(List.of(line)));
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof FurledMapData)) return false;
            FurledMapData data = (FurledMapData)obj;
            if (!data.destinationResource.equals((Object)this.destinationResource)) return false;
            if (!data.descriptionOverride.equals(this.descriptionOverride)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return this.destinationResource.hashCode() + this.descriptionOverride.hashCode() * 31;
        }
    }
}

