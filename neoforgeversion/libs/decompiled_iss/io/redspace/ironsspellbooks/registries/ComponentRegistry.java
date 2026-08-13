/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.component.DataComponentType$Builder
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.util.Unit
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 */
package io.redspace.ironsspellbooks.registries;

import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.api.item.UpgradeData;
import io.redspace.ironsspellbooks.api.item.WaywardCompassData;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.fluids.PotionFluid;
import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.item.weapons.AutoloaderCrossbow;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ComponentRegistry {
    private static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create((ResourceKey)Registries.DATA_COMPONENT_TYPE, (String)"irons_spellbooks");
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AffinityData>> AFFINITY_COMPONENT = ComponentRegistry.register("affinity_data", builder -> builder.persistent(AffinityData.CODEC).networkSynchronized(AffinityData.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FurledMapItem.FurledMapData>> FURLED_MAP_COMPONENT = ComponentRegistry.register("furled_map_data", builder -> builder.persistent(FurledMapItem.FurledMapData.CODEC).networkSynchronized(FurledMapItem.FurledMapData.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UpgradeData>> UPGRADE_DATA = ComponentRegistry.register("upgrade_data", builder -> builder.persistent(UpgradeData.CODEC).networkSynchronized(UpgradeData.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ISpellContainer>> SPELL_CONTAINER = ComponentRegistry.register("spell_container", builder -> builder.persistent(SpellContainer.CODEC).networkSynchronized(SpellContainer.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WaywardCompassData>> WAYWARD_COMPASS = ComponentRegistry.register("wayward_compass", builder -> builder.persistent(WaywardCompassData.CODEC).networkSynchronized(WaywardCompassData.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AutoloaderCrossbow.LoadStateComponent>> CROSSBOW_LOAD_STATE = ComponentRegistry.register("crossbow_load_state", builder -> builder.persistent(AutoloaderCrossbow.LoadStateComponent.CODEC).networkSynchronized(AutoloaderCrossbow.LoadStateComponent.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> CASTING_IMPLEMENT = ComponentRegistry.register("casting_implement", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit((Object)Unit.INSTANCE)).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> MULTIHAND_WEAPON = ComponentRegistry.register("multihand_weapon", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit((Object)Unit.INSTANCE)).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> CLOTHING_VARIANT = ComponentRegistry.register("clothing_variant", builder -> builder.persistent((Codec)Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceKey<UpgradeOrbType>>> UPGRADE_ORB_TYPE = ComponentRegistry.register("upgrade_orb_type", builder -> builder.persistent(ResourceKey.codec(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY)).networkSynchronized(ByteBufCodecs.fromCodec((Codec)ResourceKey.codec(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY))).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PotionFluid.BottleType>> POTION_BOTTLE_TYPE = ComponentRegistry.register("potion_bottle_type", builder -> builder.persistent(PotionFluid.BottleType.CODEC).networkSynchronized(PotionFluid.BottleType.STREAM_CODEC).cacheEncoding());

    public static void register(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String pName, UnaryOperator<DataComponentType.Builder<T>> pBuilder) {
        return COMPONENTS.register(pName, () -> ((DataComponentType.Builder)pBuilder.apply(DataComponentType.builder())).build());
    }
}

