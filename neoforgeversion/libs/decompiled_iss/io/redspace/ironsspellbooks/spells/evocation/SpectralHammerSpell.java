/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.GameType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammer;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpectralHammerSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"spectral_hammer");
    private static final int distance = 16;
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(5).setCooldownSeconds(2.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.dimensions", (Object[])new Object[]{1 + this.getRadius(spellLevel, caster) * 2, 1 + this.getRadius(spellLevel, caster) * 2, this.getDepth(spellLevel, caster) + 1}), Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{16}));
    }

    public SpectralHammerSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 15;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        BlockHitResult blockHitResult;
        boolean success;
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            if (serverPlayer.gameMode.getGameModeForPlayer() == GameType.ADVENTURE) {
                serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_adventure").withStyle(ChatFormatting.RED)));
                return false;
            }
        }
        boolean bl = success = (blockHitResult = Utils.getTargetBlock(level, entity, ClipContext.Fluid.NONE, 16.0)).getType() == HitResult.Type.BLOCK && level.getBlockState(blockHitResult.getBlockPos()).is(ModTags.SPECTRAL_HAMMER_MINEABLE);
        if (!success && entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_spectral_hammer").withStyle(ChatFormatting.RED)));
        }
        return success;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        BlockHitResult blockPosition = Utils.getTargetBlock(world, entity, ClipContext.Fluid.NONE, 16.0);
        Direction face = blockPosition.getDirection();
        int radius = this.getRadius(spellLevel, entity);
        int depth = this.getDepth(spellLevel, entity);
        SpectralHammer spectralHammer = new SpectralHammer(world, entity, blockPosition, depth, radius);
        Vec3 position = Vec3.atCenterOf((Vec3i)blockPosition.getBlockPos());
        if (!face.getAxis().isVertical()) {
            position = position.subtract(0.0, 2.0, 0.0).subtract(entity.getForward().normalize().scale(1.5));
        } else if (face == Direction.DOWN) {
            position = position.subtract(0.0, 3.0, 0.0);
        }
        spectralHammer.setPos(position.x, position.y, position.z);
        world.addFreshEntity((Entity)spectralHammer);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDepth(int spellLevel, LivingEntity caster) {
        return (int)this.getSpellPower(spellLevel, (Entity)caster);
    }

    private int getRadius(int spellLevel, LivingEntity caster) {
        return (int)Math.max(this.getSpellPower(spellLevel, (Entity)caster) * 0.5f, 1.0f);
    }
}

