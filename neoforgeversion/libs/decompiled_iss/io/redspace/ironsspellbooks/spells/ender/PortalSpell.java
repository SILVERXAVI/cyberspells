/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Position
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlock;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlockEntity;
import io.redspace.ironsspellbooks.capabilities.magic.PocketDimensionManager;
import io.redspace.ironsspellbooks.capabilities.magic.PortalManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalData;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalEntity;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalPos;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PortalSpell
extends AbstractSpell {
    public static final int PORTAL_RECAST_COUNT = 2;
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"portal");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(3).setCooldownSeconds(180.0).build();

    public PortalSpell() {
        this.baseSpellPower = 300;
        this.spellPowerPerLevel = 120;
        this.baseManaCost = 200;
        this.manaCostPerLevel = 10;
        this.castTime = 0;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new PortalData();
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    public static BlockHitResult getTargetBlock(Level level, LivingEntity entity, ClipContext.Fluid clipContext, double reach) {
        Vec3 rotation = entity.getLookAngle().normalize().scale(reach);
        Vec3 pos = entity.getEyePosition();
        Vec3 dest = rotation.add(pos);
        return level.clip(new ClipContext(pos, dest, ClipContext.Block.OUTLINE, clipContext, (Entity)entity));
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        ICastDataSerializable iCastDataSerializable;
        if (level.dimension().equals(PocketDimensionManager.POCKET_DIMENSION)) {
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_dimension").withStyle(ChatFormatting.RED)));
            }
            return false;
        }
        RecastInstance recast = playerMagicData.getPlayerRecasts().getRecastInstance(this.getSpellId());
        if (recast != null && (iCastDataSerializable = recast.getCastData()) instanceof PortalData) {
            PortalFrameBlockEntity portalFrame;
            BlockEntity blockEntity;
            BlockHitResult blockHitResult;
            PortalData portalData = (PortalData)iCastDataSerializable;
            if (portalData.isBlock && ((blockHitResult = PortalSpell.getTargetBlock(level, entity, ClipContext.Fluid.NONE, this.getCastDistance(spellLevel, entity))).getType() == HitResult.Type.MISS || !((blockEntity = level.getBlockEntity(blockHitResult.getBlockPos())) instanceof PortalFrameBlockEntity) || (portalFrame = (PortalFrameBlockEntity)blockEntity).isPortalConnected())) {
                if (entity instanceof ServerPlayer) {
                    ServerPlayer serverPlayer = (ServerPlayer)entity;
                    serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.portal_target_failure").withStyle(ChatFormatting.RED)));
                }
                return false;
            }
        }
        return super.checkPreCastConditions(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (level instanceof ServerLevel) {
                PortalFrameBlockEntity portalFrame;
                BlockEntity blockEntity;
                boolean canHitBlock;
                ServerLevel serverLevel = (ServerLevel)level;
                RecastInstance recastInstance = playerMagicData.getPlayerRecasts().hasRecastForSpell(this.getSpellId()) ? playerMagicData.getPlayerRecasts().getRecastInstance(this.getSpellId()) : null;
                BlockHitResult blockHitResult = PortalSpell.getTargetBlock(level, entity, ClipContext.Fluid.NONE, this.getCastDistance(spellLevel, entity));
                boolean bl = canHitBlock = recastInstance == null || ((PortalData)recastInstance.getCastData()).isBlock;
                if (canHitBlock && blockHitResult.getType() != HitResult.Type.MISS && (blockEntity = level.getBlockEntity(blockHitResult.getBlockPos())) instanceof PortalFrameBlockEntity && !(portalFrame = (PortalFrameBlockEntity)blockEntity).isPortalConnected()) {
                    this.handleBlockPortal(recastInstance, spellLevel, castSource, playerMagicData, player, blockHitResult, portalFrame);
                } else {
                    this.handleEntityPortal(recastInstance, level, spellLevel, entity, castSource, playerMagicData, player, serverLevel, blockHitResult);
                }
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void handleBlockPortal(@Nullable RecastInstance recastInstance, int spellLevel, CastSource castSource, MagicData playerMagicData, Player player, BlockHitResult blockHitResult, PortalFrameBlockEntity portalFrame) {
        Vec3 portalLocation = portalFrame.getPortalLocation();
        float portalRotation = ((Direction)portalFrame.getBlockState().getValue((Property)PortalFrameBlock.FACING)).toYRot();
        if (recastInstance != null) {
            PortalData portalData = (PortalData)recastInstance.getCastData();
            if (portalData.globalPos1 != null & portalData.portalEntityId1 != null) {
                portalData.globalPos2 = PortalPos.of((ResourceKey<Level>)player.level.dimension(), portalLocation, portalRotation);
                portalData.portalEntityId2 = portalFrame.getUUID();
                PortalManager.INSTANCE.addPortalData(portalData.portalEntityId1, portalData);
                PortalManager.INSTANCE.addPortalData(portalData.portalEntityId2, portalData);
                portalFrame.setChanged();
            }
        } else {
            PortalData portalData = new PortalData();
            portalData.isBlock = true;
            portalData.globalPos1 = PortalPos.of((ResourceKey<Level>)player.level.dimension(), portalLocation, portalRotation);
            portalData.portalEntityId1 = portalFrame.getUUID();
            PortalManager.INSTANCE.addPortalData(portalData.portalEntityId1, portalData);
            portalFrame.setChanged();
            playerMagicData.getPlayerRecasts().addRecast(new RecastInstance(this.getSpellId(), spellLevel, 2, this.getRecastDuration(spellLevel, (LivingEntity)player), castSource, portalData), playerMagicData);
        }
    }

    private void handleEntityPortal(@Nullable RecastInstance recastInstance, Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData, Player player, ServerLevel serverLevel, BlockHitResult blockHitResult) {
        Vec3 hitResultPos = blockHitResult.getLocation().subtract(entity.getForward().normalize().multiply(0.25, 0.0, 0.25));
        Vec3 portalLocation = level.clip(new ClipContext(hitResultPos, hitResultPos.add(0.0, (double)(-entity.getBbHeight() - 1.0f), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)entity)).getLocation().add(0.0, 0.076, 0.0);
        float portalRotation = 90.0f + Utils.getAngle(portalLocation.x, portalLocation.z, entity.getX(), entity.getZ()) * 57.295776f;
        if (recastInstance != null) {
            PortalData portalData = (PortalData)recastInstance.getCastData();
            if (portalData.globalPos1 != null & portalData.portalEntityId1 != null) {
                PortalEntity firstPortalEntity;
                portalData.globalPos2 = PortalPos.of((ResourceKey<Level>)player.level.dimension(), portalLocation, portalRotation);
                portalData.setPortalDuration(this.getPortalDuration(spellLevel, (LivingEntity)player));
                PortalEntity secondPortalEntity = this.setupPortalEntity((Level)serverLevel, portalData, player, portalLocation, portalRotation);
                secondPortalEntity.setPortalConnected();
                portalData.portalEntityId2 = secondPortalEntity.getUUID();
                PortalManager.INSTANCE.addPortalData(portalData.portalEntityId1, portalData);
                PortalManager.INSTANCE.addPortalData(portalData.portalEntityId2, portalData);
                ServerLevel firstPortalLevel = serverLevel.getServer().getLevel(portalData.globalPos1.dimension());
                if (firstPortalLevel != null && (firstPortalEntity = (PortalEntity)firstPortalLevel.getEntity(portalData.portalEntityId1)) != null) {
                    firstPortalEntity.setPortalConnected();
                    firstPortalEntity.setTicksToLive(portalData.ticksToLive);
                }
            }
        } else {
            PortalData portalData = new PortalData();
            portalData.setPortalDuration(this.getRecastDuration(spellLevel, (LivingEntity)player) + 10);
            PortalEntity portalEntity = this.setupPortalEntity(level, portalData, player, portalLocation, portalRotation);
            portalData.globalPos1 = PortalPos.of((ResourceKey<Level>)player.level.dimension(), portalLocation, portalRotation);
            portalData.portalEntityId1 = portalEntity.getUUID();
            playerMagicData.getPlayerRecasts().addRecast(new RecastInstance(this.getSpellId(), spellLevel, 2, this.getRecastDuration(spellLevel, (LivingEntity)player), castSource, portalData), playerMagicData);
        }
    }

    private PortalEntity setupPortalEntity(Level level, PortalData portalData, Player owner, Vec3 spawnPos, float rotation) {
        PortalEntity portalEntity = new PortalEntity(level, portalData);
        portalEntity.setOwnerUUID(owner.getUUID());
        portalEntity.moveTo(spawnPos);
        portalEntity.setYRot(rotation);
        level.addFreshEntity((Entity)portalEntity);
        return portalEntity;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (recastResult != RecastResult.USED_ALL_RECASTS && castDataSerializable instanceof PortalData) {
            ServerLevel level;
            MinecraftServer server;
            PortalData portalData = (PortalData)castDataSerializable;
            if (portalData.portalEntityId1 != null && portalData.globalPos1 != null && (server = serverPlayer.getServer()) != null && (level = server.getLevel(portalData.globalPos1.dimension())) != null) {
                if (portalData.isBlock) {
                    BlockPos block = BlockPos.containing((Position)portalData.globalPos1.pos());
                    if (level.isLoaded(block)) {
                        level.getBlockEntity(block, (BlockEntityType)BlockRegistry.PORTAL_FRAME_BLOCK_ENTITY.get()).ifPresent(PortalFrameBlockEntity::setChanged);
                    }
                } else {
                    Entity portal1 = level.getEntity(portalData.portalEntityId1);
                    if (portal1 != null) {
                        portal1.discard();
                    }
                }
                PortalManager.INSTANCE.removePortalData(portalData.portalEntityId1);
            }
        }
        super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
    }

    public int getRecastDuration(int spellLevel, LivingEntity caster) {
        return 2400;
    }

    public int getPortalDuration(int spellLevel, LivingEntity caster) {
        return (int)(this.getSpellPower(spellLevel, (Entity)caster) * 20.0f);
    }

    private float getCastDistance(int spellLevel, LivingEntity sourceEntity) {
        return 48.0f;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.cast_range", (Object[])new Object[]{Utils.stringTruncation(this.getCastDistance(spellLevel, caster), 1)}), Component.translatable((String)"ui.irons_spellbooks.portal_duration", (Object[])new Object[]{Utils.timeFromTicks(this.getPortalDuration(spellLevel, caster), 2)}));
    }
}

