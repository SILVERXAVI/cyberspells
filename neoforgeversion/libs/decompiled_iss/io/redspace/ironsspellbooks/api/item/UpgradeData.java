/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  it.unimi.dsi.fastutil.Pair
 *  it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderGetter
 *  net.minecraft.core.Registry
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.resources.RegistryOps
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.item.ItemStack
 */
package io.redspace.ironsspellbooks.api.item;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public record UpgradeData(Map<Holder<UpgradeOrbType>, Integer> upgrades, String upgradedSlot) {
    public static final String Upgrades = "ISBUpgrades";
    public static final String UPGRADE_TYPE = "id";
    public static final String SLOT = "slot";
    public static final String COUNT = "count";
    public static final String UPGRADES = "upgrades";
    public static final UpgradeData NONE = new UpgradeData((Map<Holder<UpgradeOrbType>, Integer>)ImmutableMap.of(), EquipmentSlot.MAINHAND.getName());
    @Deprecated(forRemoval=true)
    private static final Codec<ObjectObjectImmutablePair<String, Integer>> ELEMENT_CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Codec.STRING.fieldOf(UPGRADE_TYPE).forGetter(it.unimi.dsi.fastutil.Pair::left), (App)Codec.INT.fieldOf(COUNT).forGetter(it.unimi.dsi.fastutil.Pair::right)).apply((Applicative)builder, ObjectObjectImmutablePair::new));
    public static final Codec<UpgradeData> REAL_CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Codec.STRING.fieldOf(SLOT).forGetter(UpgradeData::getUpgradedSlot), (App)Codec.unboundedMap(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_CODEC, (Codec)Codec.INT).fieldOf(UPGRADES).forGetter(UpgradeData::upgrades)).apply((Applicative)builder, (slot, list) -> new UpgradeData((Map<Holder<UpgradeOrbType>, Integer>)list, (String)slot)));
    @Deprecated(forRemoval=true)
    private static final Codec<UpgradeData> DEPRECATED_CODEC = RecordCodecBuilder.create(builder -> builder.group((App)Codec.STRING.fieldOf(SLOT).forGetter(UpgradeData::getUpgradedSlot), (App)Codec.list(ELEMENT_CODEC).fieldOf(UPGRADES).forGetter(data -> data.upgrades().entrySet().stream().map(entry -> new ObjectObjectImmutablePair((Object)((Holder)entry.getKey()).getKey().location().toString(), (Object)((Integer)entry.getValue()))).toList())).apply((Applicative)builder, (slot, list) -> new UpgradeData((Map<Holder<UpgradeOrbType>, Integer>)UpgradeData.parseCodec(list), (String)slot)));
    public static final Codec<UpgradeData> CODEC = Codec.withAlternative(REAL_CODEC, (Codec)Codec.of(UpgradeData::deprecatedEncodeWrapper, UpgradeData::deprecatedDecodeWrapper));
    @Deprecated(forRemoval=true)
    private static DynamicOps<?> ops;
    public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeData> STREAM_CODEC;

    @Deprecated(forRemoval=true)
    private static <T> DataResult<T> deprecatedEncodeWrapper(UpgradeData input, DynamicOps<T> ops, T prefix) {
        return DEPRECATED_CODEC.encode((Object)input, ops, prefix);
    }

    @Deprecated(forRemoval=true)
    private static <T> DataResult<Pair<UpgradeData, T>> deprecatedDecodeWrapper(DynamicOps<T> ops, T input) {
        UpgradeData.ops = ops;
        DataResult result = DEPRECATED_CODEC.decode(ops, input);
        UpgradeData.ops = null;
        return result;
    }

    @Deprecated(forRemoval=true)
    private static ImmutableMap<Holder<UpgradeOrbType>, Integer> parseCodec(List<ObjectObjectImmutablePair<String, Integer>> data) {
        DynamicOps<?> dynamicOps = ops;
        if (dynamicOps instanceof RegistryOps) {
            RegistryOps ops = (RegistryOps)dynamicOps;
            HolderGetter reg = (HolderGetter)ops.getter(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY).get();
            ImmutableMap.Builder map = ImmutableMap.builder();
            for (it.unimi.dsi.fastutil.Pair pair : data) {
                ResourceLocation upgradeKey = ResourceLocation.parse((String)((String)pair.left()));
                reg.get(ResourceKey.create(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, (ResourceLocation)upgradeKey)).ifPresent(upgrade -> map.put(upgrade, (Object)((Integer)pair.right())));
            }
            return map.build();
        }
        return ImmutableMap.of();
    }

    public static UpgradeData getUpgradeData(ItemStack itemStack) {
        if (!itemStack.has(ComponentRegistry.UPGRADE_DATA)) {
            return NONE;
        }
        return (UpgradeData)itemStack.get(ComponentRegistry.UPGRADE_DATA);
    }

    public static boolean hasUpgradeData(ItemStack stack) {
        return stack.has(ComponentRegistry.UPGRADE_DATA);
    }

    public static void removeUpgradeData(ItemStack itemstack) {
        itemstack.remove(ComponentRegistry.UPGRADE_DATA);
    }

    public UpgradeData addUpgrade(ItemStack stack, Holder<UpgradeOrbType> upgradeType, String slot) {
        if (this == NONE) {
            ImmutableMap.Builder map = ImmutableMap.builder();
            map.put(upgradeType, (Object)1);
            UpgradeData upgrade = new UpgradeData((Map<Holder<UpgradeOrbType>, Integer>)map.build(), slot);
            UpgradeData.set(stack, upgrade);
            return upgrade;
        }
        ImmutableMap.Builder map = ImmutableMap.builder();
        if (this.upgrades.containsKey(upgradeType)) {
            map.put(upgradeType, (Object)(this.upgrades.get(upgradeType) + 1));
            map.putAll(this.upgrades.entrySet().stream().filter(entry -> entry.getKey() != upgradeType).toList());
        } else {
            map.put(upgradeType, (Object)1);
            map.putAll(this.upgrades);
        }
        UpgradeData upgrade = new UpgradeData((Map<Holder<UpgradeOrbType>, Integer>)map.build(), this.upgradedSlot);
        UpgradeData.set(stack, upgrade);
        return upgrade;
    }

    public static void set(ItemStack stack, UpgradeData data) {
        stack.set(ComponentRegistry.UPGRADE_DATA, (Object)data);
    }

    public int getTotalUpgrades() {
        int count = 0;
        for (Map.Entry<Holder<UpgradeOrbType>, Integer> upgradeInstance : this.upgrades.entrySet()) {
            count += upgradeInstance.getValue().intValue();
        }
        return count;
    }

    public String getUpgradedSlot() {
        return this.upgradedSlot;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UpgradeData)) return false;
        UpgradeData upgradeData = (UpgradeData)obj;
        if (!this.upgradedSlot.equals(upgradeData.upgradedSlot)) return false;
        if (!this.upgrades.equals(upgradeData.upgrades)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.upgradedSlot.hashCode() * 31 + this.upgrades.hashCode();
    }

    static {
        STREAM_CODEC = StreamCodec.of((buf, data) -> {
            buf.writeUtf(data.upgradedSlot);
            Set<Map.Entry<Holder<UpgradeOrbType>, Integer>> entries = data.upgrades.entrySet();
            buf.writeInt(entries.size());
            for (Map.Entry<Holder<UpgradeOrbType>, Integer> entry : entries) {
                if (entry.getKey().getKey() == null) continue;
                buf.writeResourceLocation(entry.getKey().getKey().location());
                buf.writeInt(entry.getValue().intValue());
            }
        }, buf -> {
            Registry<UpgradeOrbType> registry = UpgradeOrbTypeRegistry.upgradeTypeRegistry(buf.registryAccess());
            String slot = buf.readUtf();
            int i = buf.readInt();
            ImmutableMap.Builder upgrades = ImmutableMap.builder();
            for (int j = 0; j < i; ++j) {
                ResourceLocation upgradeKey = buf.readResourceLocation();
                int c = buf.readInt();
                Optional.ofNullable((UpgradeOrbType)registry.get(upgradeKey)).ifPresent(upgrade -> upgrades.put((Object)registry.wrapAsHolder(upgrade), (Object)c));
            }
            return new UpgradeData((Map<Holder<UpgradeOrbType>, Integer>)upgrades.build(), slot);
        });
    }
}

