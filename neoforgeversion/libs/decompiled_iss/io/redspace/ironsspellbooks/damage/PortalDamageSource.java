/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.damage;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PortalDamageSource
extends DamageSource {
    public PortalDamageSource(Holder<DamageType> pType, @Nullable Entity pEntity) {
        super(pType, pEntity);
    }

    @NotNull
    public Component getLocalizedDeathMessage(@NotNull LivingEntity pLivingEntity) {
        return Component.translatable((String)"death.attack.unstable_portal_owner", (Object[])new Object[]{pLivingEntity.getDisplayName()});
    }
}

