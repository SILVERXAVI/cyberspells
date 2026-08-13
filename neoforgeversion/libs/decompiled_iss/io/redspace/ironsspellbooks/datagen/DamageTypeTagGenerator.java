/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.data.PackOutput
 *  net.minecraft.data.tags.TagsProvider
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.neoforged.neoforge.common.NeoForgeMod
 *  net.neoforged.neoforge.common.Tags$DamageTypes
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  org.jetbrains.annotations.NotNull
 */
package io.redspace.ironsspellbooks.datagen;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class DamageTypeTagGenerator
extends TagsProvider<DamageType> {
    public static final TagKey<DamageType> BYPASS_EVASION = DamageTypeTagGenerator.create("bypass_evasion");
    public static final TagKey<DamageType> LONG_CAST_IGNORE = DamageTypeTagGenerator.create("long_cast_ignore");
    public static final TagKey<DamageType> FIRE_MAGIC = DamageTypeTagGenerator.create("fire_magic");
    public static final TagKey<DamageType> ICE_MAGIC = DamageTypeTagGenerator.create("ice_magic");
    public static final TagKey<DamageType> LIGHTNING_MAGIC = DamageTypeTagGenerator.create("lightning_magic");
    public static final TagKey<DamageType> HOLY_MAGIC = DamageTypeTagGenerator.create("holy_magic");
    public static final TagKey<DamageType> ENDER_MAGIC = DamageTypeTagGenerator.create("ender_magic");
    public static final TagKey<DamageType> BLOOD_MAGIC = DamageTypeTagGenerator.create("blood_magic");
    public static final TagKey<DamageType> EVOCATION_MAGIC = DamageTypeTagGenerator.create("evocation_magic");
    public static final TagKey<DamageType> ELDRITCH_MAGIC = DamageTypeTagGenerator.create("eldritch_magic");
    public static final TagKey<DamageType> NATURE_MAGIC = DamageTypeTagGenerator.create("nature_magic");

    public DamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, "irons_spellbooks", existingFileHelper);
    }

    private static TagKey<DamageType> create(String name) {
        return TagKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)name));
    }

    protected void addTags(@NotNull HolderLookup.Provider provider) {
        this.tag(FIRE_MAGIC).add(ISSDamageTypes.FIRE_MAGIC);
        this.tag(ICE_MAGIC).add(ISSDamageTypes.ICE_MAGIC);
        this.tag(LIGHTNING_MAGIC).add(ISSDamageTypes.LIGHTNING_MAGIC);
        this.tag(HOLY_MAGIC).add(ISSDamageTypes.HOLY_MAGIC);
        this.tag(ENDER_MAGIC).add(ISSDamageTypes.ENDER_MAGIC);
        this.tag(BLOOD_MAGIC).add(ISSDamageTypes.BLOOD_MAGIC);
        this.tag(EVOCATION_MAGIC).add(ISSDamageTypes.EVOCATION_MAGIC);
        this.tag(ELDRITCH_MAGIC).add(ISSDamageTypes.ELDRITCH_MAGIC);
        this.tag(NATURE_MAGIC).add(ISSDamageTypes.NATURE_MAGIC);
        this.tag(BYPASS_EVASION).add(new ResourceKey[]{DamageTypes.ON_FIRE, DamageTypes.WITHER, DamageTypes.FREEZE, DamageTypes.STARVE, DamageTypes.DROWN, DamageTypes.STALAGMITE, DamageTypes.OUTSIDE_BORDER, DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.DRY_OUT, DamageTypes.IN_WALL, ISSDamageTypes.CAULDRON, ISSDamageTypes.HEARTSTOP});
        this.tag(BYPASS_EVASION).addOptional(NeoForgeMod.POISON_DAMAGE.location());
        this.tag(LONG_CAST_IGNORE).add(new ResourceKey[]{DamageTypes.FREEZE, DamageTypes.STARVE, DamageTypes.ON_FIRE, DamageTypes.WITHER, ISSDamageTypes.HEARTSTOP, DamageTypes.DROWN, DamageTypes.FALL});
        this.tag(LONG_CAST_IGNORE).addOptional(NeoForgeMod.POISON_DAMAGE.location());
        this.tag(Tags.DamageTypes.IS_MAGIC).addTags(new TagKey[]{FIRE_MAGIC, ICE_MAGIC, LIGHTNING_MAGIC, HOLY_MAGIC, ENDER_MAGIC, BLOOD_MAGIC, EVOCATION_MAGIC, ELDRITCH_MAGIC, NATURE_MAGIC});
    }
}

