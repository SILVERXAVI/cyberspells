/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.advancements.Criterion
 *  net.minecraft.advancements.CriterionTriggerInstance
 *  net.minecraft.advancements.critereon.ContextAwarePredicate
 *  net.minecraft.advancements.critereon.EntityPredicate
 *  net.minecraft.advancements.critereon.SimpleCriterionTrigger
 *  net.minecraft.advancements.critereon.SimpleCriterionTrigger$SimpleInstance
 *  net.minecraft.server.level.ServerPlayer
 */
package com.perigrine3.createcybernetics.advancement.triggers;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.perigrine3.createcybernetics.advancement.ModCriteria;
import java.util.Optional;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public final class CorticalStackTrigger
extends SimpleCriterionTrigger<Instance> {
    public void trigger(ServerPlayer player) {
        this.trigger(player, inst -> inst.matches());
    }

    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public record Instance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance
    {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(b -> b.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player)).apply((Applicative)b, Instance::new));

        public boolean matches() {
            return true;
        }

        public static Criterion<Instance> any() {
            return ((CorticalStackTrigger)((Object)ModCriteria.CORTICAL_STACK.get())).createCriterion((CriterionTriggerInstance)new Instance(Optional.empty()));
        }
    }
}

