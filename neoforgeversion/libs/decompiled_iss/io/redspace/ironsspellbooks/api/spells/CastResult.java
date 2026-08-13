/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.api.spells;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class CastResult {
    public final Type type;
    @Nullable
    public final Component message;

    public CastResult(Type type) {
        this(type, null);
    }

    public CastResult(Type type, Component message) {
        this.type = type;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.type == Type.SUCCESS;
    }

    public static enum Type {
        SUCCESS,
        FAILURE;

    }
}

