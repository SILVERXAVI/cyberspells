/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.effect.CustomDescriptionMobEffect;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class InstantManaEffect
extends CustomDescriptionMobEffect {
    public static final int manaPerAmplifier = 25;
    public static final float manaPerAmplifierPercent = 0.05f;

    public InstantManaEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public Component getDescriptionLine(MobEffectInstance instance) {
        int amp = instance.getAmplifier() + 1;
        int addition = amp * 25;
        int percent = (int)((float)amp * 0.05f * 100.0f);
        return Component.translatable((String)"tooltip.irons_spellbooks.instant_mana_description", (Object[])new Object[]{addition, percent}).withStyle(ChatFormatting.BLUE);
    }

    public boolean isInstantenous() {
        return true;
    }

    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity livingEntity, int pAmplifier, double pHealth) {
        int i = pAmplifier + 1;
        int maxMana = (int)livingEntity.getAttributeValue(AttributeRegistry.MAX_MANA);
        int manaAdd = (int)((float)(i * 25) + (float)maxMana * ((float)i * 0.05f));
        MagicData pmg = MagicData.getPlayerMagicData(livingEntity);
        pmg.setMana(pmg.getMana() + (float)manaAdd);
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncManaPacket(pmg), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        this.applyInstantenousEffect(null, null, livingEntity, pAmplifier, livingEntity.getHealth());
        return true;
    }
}

