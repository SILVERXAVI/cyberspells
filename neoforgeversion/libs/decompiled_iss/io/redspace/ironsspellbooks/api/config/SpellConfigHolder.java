/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonObject
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.JsonOps
 */
package io.redspace.ironsspellbooks.api.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpellConfigHolder {
    private final Map<SpellConfigParameter<?>, Object> defaultConfig = new HashMap();
    private final Map<SpellConfigParameter<?>, Object> config = new HashMap();

    public <T> void set(SpellConfigParameter<T> paramtype, T parameter) {
        this.config.put(paramtype, parameter);
    }

    public <T> void setDefaultValue(SpellConfigParameter<T> paramtype, T parameter) {
        this.defaultConfig.put(paramtype, parameter);
    }

    public <T> T get(SpellConfigParameter<T> paramtype) {
        if (this.config.containsKey(paramtype)) {
            return (T)this.config.get(paramtype);
        }
        return (T)this.defaultConfig.getOrDefault(paramtype, paramtype.defaultValue().get());
    }

    public <T> Optional<T> getDefaultValue(SpellConfigParameter<T> paramtype) {
        return Optional.ofNullable(this.defaultConfig.get(paramtype));
    }

    public <T> boolean isDefault(SpellConfigParameter<T> parameter) {
        return !this.config.containsKey(parameter);
    }

    public <T> JsonObject toJson(Gson gson) {
        JsonObject json = new JsonObject();
        for (Map.Entry<SpellConfigParameter<?>, Object> entry : this.config.entrySet()) {
            SpellConfigParameter<?> param = entry.getKey();
            Object value = entry.getValue();
            Codec<?> codec = param.datatype();
            DataResult result = codec.encodeStart((DynamicOps)JsonOps.INSTANCE, value);
            json.add(param.key().toString(), gson.toJsonTree(result.getOrThrow()));
        }
        return json;
    }
}

