/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.GameType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BaseFireBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.level.gameevent.GameEvent$Context
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.common.CommonHooks
 */
package io.redspace.ironsspellbooks.spells.nature;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

public class TouchDigSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"touch_dig");
    private static final int distance = 8;
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.NATURE_RESOURCE).setMaxLevel(3).setCooldownSeconds(0.5).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.harvest_level", (Object[])new Object[]{Component.translatable((String)this.getHarvestLevel((double)((double)this.getSpellPower((int)spellLevel, (Entity)caster))).descriptionId)}), Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{8}));
    }

    public TouchDigSpell() {
        this.baseManaCost = 15;
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 0;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.TOUCH_DIG_CAST.get());
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

    private HarvestData getHarvestLevel(double spellPower) {
        if (spellPower >= 15.0) {
            return HarvestData.NETHERITE;
        }
        if (spellPower >= 13.0) {
            return HarvestData.DIAMOND;
        }
        return HarvestData.IRON;
    }

    private boolean canBreak(Level level, BlockPos blockPos, double spellPower) {
        BlockState blockState = level.getBlockState(blockPos);
        return blockState.getDestroySpeed((BlockGetter)level, blockPos) >= 0.0f && !blockState.is(this.getHarvestLevel((double)spellPower).cantHarvest);
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        BlockHitResult blockHitResult;
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            if (serverPlayer.gameMode.getGameModeForPlayer() == GameType.ADVENTURE) {
                serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_adventure").withStyle(ChatFormatting.RED)));
                return false;
            }
        }
        if ((blockHitResult = Utils.getTargetBlock(level, entity, ClipContext.Fluid.NONE, 8.0)).getType() != HitResult.Type.BLOCK) {
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_target_block").withStyle(ChatFormatting.RED)));
            }
            return false;
        }
        if (!this.canBreak(level, blockHitResult.getBlockPos(), this.getSpellPower(spellLevel, (Entity)entity))) {
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_harvest_level").withStyle(ChatFormatting.RED)));
            }
            return false;
        }
        return true;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        block3: {
            BlockHitResult blockhit;
            block4: {
                blockhit = Utils.getTargetBlock(world, entity, ClipContext.Fluid.NONE, 8.0);
                Vec3 vec = blockhit.getLocation();
                Vec3 particle = entity.getEyePosition().subtract(0.0, 0.1, 0.0);
                int count = (int)vec.distanceTo(particle) * 2;
                for (int i = 0; i < count; ++i) {
                    Vec3 pos = vec.add(particle.subtract(vec).scale((double)i / (double)count));
                    MagicManager.spawnParticles(world, (ParticleOptions)ParticleTypes.CRIT, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0, false);
                }
                MagicManager.spawnParticles(world, (ParticleOptions)ParticleTypes.CRIT, vec.x, vec.y, vec.z, 25, 0.0, 0.0, 0.0, 0.2, false);
                if (!this.canBreak(world, blockhit.getBlockPos(), this.getSpellPower(spellLevel, (Entity)entity))) break block3;
                if (!(entity instanceof ServerPlayer)) break block4;
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                if (CommonHooks.fireBlockBreak((Level)world, (GameType)serverPlayer.gameMode.getGameModeForPlayer(), (ServerPlayer)serverPlayer, (BlockPos)blockhit.getBlockPos(), (BlockState)world.getBlockState(blockhit.getBlockPos())).isCanceled()) break block3;
            }
            this.doDestroyBlock(world, blockhit.getBlockPos(), entity);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private void doDestroyBlock(Level level, BlockPos pos, LivingEntity livingEntity) {
        BlockState blockstate = level.getBlockState(pos);
        if (!blockstate.isAir()) {
            FluidState fluidstate = level.getFluidState(pos);
            if (!(blockstate.getBlock() instanceof BaseFireBlock)) {
                level.levelEvent(2001, pos, Block.getId((BlockState)blockstate));
            }
            BlockEntity blockentity = blockstate.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            Block.dropResources((BlockState)blockstate, (Level)level, (BlockPos)pos, (BlockEntity)blockentity, (Entity)livingEntity, (ItemStack)livingEntity.getMainHandItem());
            if (level.setBlock(pos, fluidstate.createLegacyBlock(), 3)) {
                level.gameEvent((Holder)GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of((Entity)livingEntity, (BlockState)blockstate));
            }
        }
    }

    record HarvestData(TagKey<Block> cantHarvest, String descriptionId) {
        static HarvestData NETHERITE = new HarvestData((TagKey<Block>)BlockTags.INCORRECT_FOR_NETHERITE_TOOL, "ui.irons_spellbooks.harvest_level.netherite");
        static HarvestData DIAMOND = new HarvestData((TagKey<Block>)BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "ui.irons_spellbooks.harvest_level.diamond");
        static HarvestData IRON = new HarvestData((TagKey<Block>)BlockTags.INCORRECT_FOR_IRON_TOOL, "ui.irons_spellbooks.harvest_level.iron");
    }
}

