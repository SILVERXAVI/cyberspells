/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.util;

import io.redspace.ironsspellbooks.entity.spells.portal.PortalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NBT {
    public static CompoundTag writePortalPos(PortalPos globalPos) {
        CompoundTag tag = new CompoundTag();
        tag.putString("res", globalPos.dimension().location().toString());
        CompoundTag posTag = NBT.writeVec3Pos(globalPos.pos());
        tag.put("pos", (Tag)posTag);
        tag.putFloat("rot", globalPos.rotation());
        return tag;
    }

    public static PortalPos readPortalPos(CompoundTag compoundTag) {
        ResourceLocation resourcelocation = ResourceLocation.parse((String)compoundTag.getString("res"));
        CompoundTag posTag = (CompoundTag)compoundTag.get("pos");
        Vec3 pos = NBT.readVec3(posTag);
        ResourceKey resourceKey = ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)resourcelocation);
        float rotation = compoundTag.getFloat("rot");
        return PortalPos.of((ResourceKey<Level>)resourceKey, pos, rotation);
    }

    public static Vec3 readVec3(CompoundTag pTag) {
        return new Vec3(pTag.getDouble("X"), pTag.getDouble("Y"), pTag.getDouble("Z"));
    }

    public static CompoundTag writeVec3Pos(Vec3 pPos) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putDouble("X", pPos.x);
        compoundtag.putDouble("Y", pPos.y);
        compoundtag.putDouble("Z", pPos.z);
        return compoundtag;
    }
}

