/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import io.redspace.ironsspellbooks.api.magic.IMagicManager;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.network.casting.SyncCooldownPacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

public class MagicManager
implements IMagicManager {
    public static final int MANA_REGEN_TICKS = 10;
    public static final int CONTINUOUS_CAST_TICK_INTERVAL = 10;

    public boolean regenPlayerMana(ServerPlayer serverPlayer, MagicData playerMagicData) {
        int playerMaxMana = (int)serverPlayer.getAttributeValue(AttributeRegistry.MAX_MANA);
        float mana = playerMagicData.getMana();
        if (mana != (float)playerMaxMana) {
            float playerManaRegenMultiplier = (float)serverPlayer.getAttributeValue(AttributeRegistry.MANA_REGEN);
            float increment = (float)playerMaxMana * playerManaRegenMultiplier * 0.01f * ((Double)ServerConfigs.MANA_REGEN_MULTIPLIER.get()).floatValue();
            playerMagicData.setMana(Mth.clamp((float)(playerMagicData.getMana() + increment), (float)0.0f, (float)playerMaxMana));
            return true;
        }
        return false;
    }

    public void tick(Level level) {
        boolean doManaRegen = level.getServer().getTickCount() % 10 == 0;
        level.players().stream().toList().forEach(player -> {
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                MagicData playerMagicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
                playerMagicData.getPlayerCooldowns().tick(1);
                playerMagicData.getPlayerRecasts().tick(2);
                if (playerMagicData.isCasting()) {
                    playerMagicData.handleCastDuration();
                    AbstractSpell spell = SpellRegistry.getSpell(playerMagicData.getCastingSpellId());
                    if (spell.getCastType() == CastType.LONG && !serverPlayer.isUsingItem() || spell.getCastType() == CastType.INSTANT) {
                        if (playerMagicData.getCastDurationRemaining() <= 0) {
                            spell.castSpell(serverPlayer.level, playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData.getCastSource(), true);
                            if (playerMagicData.getCastSource() == CastSource.SCROLL) {
                                Scroll.attemptRemoveScrollAfterCast(serverPlayer);
                            }
                            spell.onServerCastComplete(serverPlayer.level, playerMagicData.getCastingSpellLevel(), (LivingEntity)serverPlayer, playerMagicData, false);
                        }
                    } else if (spell.getCastType() == CastType.CONTINUOUS && (playerMagicData.getCastDurationRemaining() + 1) % 10 == 0) {
                        if (playerMagicData.getCastDurationRemaining() < 10 || playerMagicData.getCastSource().consumesMana() && playerMagicData.getMana() - (float)(spell.getManaCost(playerMagicData.getCastingSpellLevel()) * 2) < 0.0f) {
                            spell.castSpell(serverPlayer.level, playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData.getCastSource(), true);
                            if (playerMagicData.getCastSource() == CastSource.SCROLL) {
                                Scroll.attemptRemoveScrollAfterCast(serverPlayer);
                            }
                            spell.onServerCastComplete(serverPlayer.level, playerMagicData.getCastingSpellLevel(), (LivingEntity)serverPlayer, playerMagicData, false);
                        } else {
                            spell.castSpell(serverPlayer.level, playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData.getCastSource(), false);
                        }
                    }
                    if (playerMagicData.isCasting()) {
                        spell.onServerCastTick(serverPlayer.level, playerMagicData.getCastingSpellLevel(), (LivingEntity)serverPlayer, playerMagicData);
                    }
                }
                if (doManaRegen && this.regenPlayerMana(serverPlayer, playerMagicData)) {
                    PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncManaPacket(playerMagicData), (CustomPacketPayload[])new CustomPacketPayload[0]);
                }
            }
        });
    }

    @Override
    public void addCooldown(ServerPlayer serverPlayer, AbstractSpell spell, CastSource castSource) {
        int effectiveCooldown = MagicManager.getEffectiveSpellCooldown(spell, (Player)serverPlayer, castSource);
        SpellCooldownAddedEvent.Pre pre = (SpellCooldownAddedEvent.Pre)NeoForge.EVENT_BUS.post((Event)new SpellCooldownAddedEvent.Pre(effectiveCooldown, spell, (Player)serverPlayer, castSource));
        if (castSource == CastSource.SCROLL || pre.isCanceled()) {
            return;
        }
        effectiveCooldown = pre.getEffectiveCooldown();
        MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getPlayerCooldowns().addCooldown(spell, effectiveCooldown);
        PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncCooldownPacket(spell.getSpellId(), effectiveCooldown), (CustomPacketPayload[])new CustomPacketPayload[0]);
        NeoForge.EVENT_BUS.post((Event)new SpellCooldownAddedEvent.Post(effectiveCooldown, spell, (Player)serverPlayer, castSource));
    }

    public void clearCooldowns(ServerPlayer serverPlayer) {
        MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getPlayerCooldowns().clearCooldowns();
        MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getPlayerCooldowns().syncToPlayer(serverPlayer);
    }

    public static int getEffectiveSpellCooldown(AbstractSpell spell, Player player, CastSource castSource) {
        double playerCooldownModifier = player.getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION);
        float itemCoolDownModifer = 1.0f;
        if (castSource == CastSource.SWORD) {
            itemCoolDownModifer = ((Double)ServerConfigs.SWORDS_CD_MULTIPLIER.get()).floatValue();
        }
        return (int)((double)spell.getSpellCooldown() * (2.0 - Utils.softCapFormula(playerCooldownModifier)) * (double)itemCoolDownModifer);
    }

    public static void spawnParticles(Level level, ParticleOptions particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
        level.getServer().getPlayerList().getPlayers().forEach(player -> ((ServerLevel)level).sendParticles(player, particle, force, x, y, z, count, deltaX, deltaY, deltaZ, speed));
    }
}

