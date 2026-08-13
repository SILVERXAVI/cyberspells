/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.neoforged.neoforge.common.util.INBTSerializable
 */
package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.network.ISerializable;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface ICastDataSerializable
extends ICastData,
ISerializable,
INBTSerializable<CompoundTag> {
}

