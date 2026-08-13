/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.bus.api.Event
 */
package io.redspace.ironsspellbooks.api.config;

import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import java.util.function.Consumer;
import net.neoforged.bus.api.Event;

public class RegisterConfigParametersEvent
extends Event {
    private final Consumer<SpellConfigParameter<?>> registrar;

    public RegisterConfigParametersEvent(Consumer<SpellConfigParameter<?>> registrar) {
        this.registrar = registrar;
    }

    public void register(SpellConfigParameter<?> parameterType) {
        this.registrar.accept(parameterType);
    }
}

