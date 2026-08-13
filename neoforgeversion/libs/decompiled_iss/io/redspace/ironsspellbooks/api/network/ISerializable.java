/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 */
package io.redspace.ironsspellbooks.api.network;

import net.minecraft.network.FriendlyByteBuf;

public interface ISerializable {
    public void writeToBuffer(FriendlyByteBuf var1);

    public void readFromBuffer(FriendlyByteBuf var1);
}

