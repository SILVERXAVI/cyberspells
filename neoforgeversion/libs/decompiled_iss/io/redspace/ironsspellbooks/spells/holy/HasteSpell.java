/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 */
package io.redspace.ironsspellbooks.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.TargetedTargetAreaCastData;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HasteSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"haste");
    private static final int MAX_TARGETS = 5;
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.EPIC).setSchoolResource(SchoolRegistry.HOLY_RESOURCE).setMaxLevel(4).setCooldownSeconds(80.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.hastened", (Object[])new Object[]{Utils.stringTruncation((float)(1 + this.getAmplifier(spellLevel, caster)) * 0.1f * 100.0f, 1)}), Component.translatable((String)"ui.irons_spellbooks.effect_length", (Object[])new Object[]{Utils.timeFromTicks(this.getDuration(spellLevel, caster), 1)}), Component.translatable((String)"ui.irons_spellbooks.max_victims", (Object[])new Object[]{5}));
    }

    public HasteSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 30;
        this.spellPowerPerLevel = 5;
        this.castTime = 30;
        this.baseManaCost = 50;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of((SoundEvent)SoundRegistry.CLOUD_OF_REGEN_LOOP.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (!Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35f, false)) {
            playerMagicData.setAdditionalCastData(new TargetEntityCastData(entity));
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.spell_target_success_self", (Object[])new Object[]{this.getDisplayName((Player)serverPlayer)}).withStyle(ChatFormatting.GREEN)));
            }
        }
        float radius = 3.0f;
        LivingEntity target = ((TargetEntityCastData)playerMagicData.getAdditionalCastData()).getTarget((ServerLevel)level);
        TargetedAreaEntity area = TargetedAreaEntity.createTargetAreaEntity(level, target.position(), radius, Utils.packRGB(this.getTargetingColor()), (Entity)target);
        playerMagicData.setAdditionalCastData(new TargetedTargetAreaCastData(target, area));
        return true;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TargetedTargetAreaCastData targetData;
        LivingEntity targetEntity;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetedTargetAreaCastData && (targetEntity = (targetData = (TargetedTargetAreaCastData)iCastData).getTarget((ServerLevel)world)) != null) {
            float radius = 3.0f;
            AtomicInteger targets = new AtomicInteger(0);
            targetEntity.level.getEntitiesOfClass(LivingEntity.class, targetEntity.getBoundingBox().inflate((double)radius)).forEach(victim -> {
                if (targets.get() < 5 && victim.distanceToSqr((Entity)targetEntity) < (double)(radius * radius) && Utils.shouldHealEntity(entity, victim)) {
                    victim.addEffect(new MobEffectInstance(MobEffectRegistry.HASTENED, this.getDuration(spellLevel, entity), this.getAmplifier(spellLevel, entity)));
                    targets.incrementAndGet();
                }
            });
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public int getAmplifier(int spellLevel, LivingEntity caster) {
        return spellLevel - 1;
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return (int)(this.getSpellPower(spellLevel, (Entity)caster) * 20.0f);
    }
}

