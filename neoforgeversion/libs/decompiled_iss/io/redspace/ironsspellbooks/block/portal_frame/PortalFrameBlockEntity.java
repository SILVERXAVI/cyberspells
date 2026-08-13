/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.Position
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.Connection
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.RelativeMovement
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.portal.DimensionTransition
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.block.portal_frame;

import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlock;
import io.redspace.ironsspellbooks.capabilities.magic.PortalManager;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalData;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalPos;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PortalFrameBlockEntity
extends BlockEntity {
    private PortalId portalId;
    private int color = -1;
    @Nullable
    private UUID ownerUUID = null;
    boolean clientIsConnected;
    private boolean active;
    private int activeCooldown;

    public PortalFrameBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this((BlockEntityType)BlockRegistry.PORTAL_FRAME_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public PortalFrameBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.portalId = PortalFrameBlockEntity.isPrimary(pBlockState) ? new PortalId(Optional.of(UUID.randomUUID())) : new PortalId(Optional.empty());
    }

    public static boolean isPrimary(BlockState blockState) {
        return ((DoubleBlockHalf)blockState.getValue(PortalFrameBlock.HALF)).equals((Object)DoubleBlockHalf.LOWER);
    }

    private void ifNeighborPresent(Consumer<PortalFrameBlockEntity> consumer) {
        BlockEntity e;
        if (this.level != null && (e = this.level.getBlockEntity(this.getBlockPos().relative(((DoubleBlockHalf)this.getBlockState().getValue(PortalFrameBlock.HALF)).getDirectionToOther()))) instanceof PortalFrameBlockEntity) {
            PortalFrameBlockEntity portalFrameBlockEntity = (PortalFrameBlockEntity)e;
            consumer.accept(portalFrameBlockEntity);
        }
    }

    private <T> T ifNeighborPresentExecute(Function<PortalFrameBlockEntity, T> function, T defaultValue) {
        BlockEntity e;
        if (this.level != null && (e = this.level.getBlockEntity(this.getBlockPos().relative(((DoubleBlockHalf)this.getBlockState().getValue(PortalFrameBlock.HALF)).getDirectionToOther()))) instanceof PortalFrameBlockEntity) {
            PortalFrameBlockEntity portalFrameBlockEntity = (PortalFrameBlockEntity)e;
            return function.apply(portalFrameBlockEntity);
        }
        return defaultValue;
    }

    private void ifOtherPortalFramePresent(Consumer<PortalFrameBlockEntity> consumer) {
        Level level;
        PortalData portalData = this.getPortalData();
        if (portalData != null && (level = this.level) instanceof ServerLevel) {
            BlockEntity blockEntity;
            ServerLevel serverLevel = (ServerLevel)level;
            MinecraftServer server = serverLevel.getServer();
            boolean primary = this.getUUID().equals(portalData.portalEntityId1);
            PortalPos otherPos = primary ? portalData.globalPos2 : portalData.globalPos1;
            ServerLevel dimension = server.getLevel(otherPos.dimension());
            BlockPos otherBlockPos = BlockPos.containing((Position)otherPos.pos());
            if (dimension != null && (blockEntity = dimension.getBlockEntity(otherBlockPos)) instanceof PortalFrameBlockEntity) {
                PortalFrameBlockEntity portalFrame = (PortalFrameBlockEntity)blockEntity;
                consumer.accept(portalFrame);
            }
        }
    }

    public boolean isPortalConnected() {
        return this.getPortalData() != null;
    }

    public void breakPortalConnection() {
        this.setColor(-1);
        PortalData portalData = this.getPortalData();
        if (portalData != null) {
            PortalManager.INSTANCE.removePortalData(portalData.portalEntityId1);
            PortalManager.INSTANCE.removePortalData(portalData.portalEntityId2);
            this.ifOtherPortalFramePresent(PortalFrameBlockEntity::setChanged);
            this.setChanged();
        }
    }

    @Nullable
    private PortalData getPortalData() {
        return PortalManager.INSTANCE.getPortalData(this.portalId.uuid(this));
    }

    public Vec3 getPortalLocation() {
        if (PortalFrameBlockEntity.isPrimary(this.getBlockState())) {
            return this.getBlockPos().getBottomCenter();
        }
        return this.getBlockPos().getBottomCenter().subtract(0.0, 1.0, 0.0);
    }

    @Nullable
    public UUID getOwnerUUID() {
        if (PortalFrameBlockEntity.isPrimary(this.getBlockState())) {
            return this.ownerUUID;
        }
        return this.ifNeighborPresentExecute(PortalFrameBlockEntity::getOwnerUUID, null);
    }

    public void setOwnerUUID(UUID ownerUUID) {
        if (PortalFrameBlockEntity.isPrimary(this.getBlockState())) {
            this.ownerUUID = ownerUUID;
        } else {
            this.ifNeighborPresent(tile -> {
                tile.ownerUUID = ownerUUID;
            });
        }
    }

    public void teleport(Entity entity) {
        Level level = entity.level;
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            UUID uuid = this.getUUID();
            PortalManager.INSTANCE.processDelayCooldown(uuid, entity.getUUID(), 1);
            if (PortalManager.INSTANCE.canUsePortal(uuid, entity)) {
                PortalData portalData = PortalManager.INSTANCE.getPortalData(uuid);
                PortalManager.INSTANCE.addPortalCooldown(entity, uuid);
                portalData.getConnectedPortalPos(uuid).ifPresent(portalPos -> {
                    Vec3 destination = portalPos.pos();
                    serverLevel.playSound(null, this.getBlockPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);
                    if (serverLevel.dimension().equals(portalPos.dimension())) {
                        entity.teleportTo(serverLevel, destination.x, destination.y, destination.z, RelativeMovement.ROTATION, portalPos.rotation(), entity.getXRot());
                    } else {
                        MinecraftServer server = serverLevel.getServer();
                        ServerLevel dim = server.getLevel(portalPos.dimension());
                        if (dim != null) {
                            entity.changeDimension(new DimensionTransition(dim, destination, Vec3.ZERO, portalPos.rotation(), entity.getXRot(), DimensionTransition.DO_NOTHING));
                            dim.playSound(null, destination.x, destination.y, destination.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);
                        }
                    }
                });
            }
        }
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(tag, pRegistries);
        if (tag.contains("uuid")) {
            UUID uuid = tag.getUUID("uuid");
            this.portalId = new PortalId(Optional.of(uuid));
        }
        if (tag.contains("owner")) {
            this.ownerUUID = tag.getUUID("owner");
        }
        if (tag.contains("color")) {
            this.color = tag.getInt("color");
        }
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        if (PortalFrameBlockEntity.isPrimary(this.getBlockState())) {
            tag.putInt("color", this.color);
            UUID uuid = this.getUUID();
            if (uuid != null) {
                tag.putUUID("uuid", uuid);
            }
            if (this.ownerUUID != null) {
                tag.putUUID("owner", this.ownerUUID);
            }
        }
    }

    public UUID getUUID() {
        return this.portalId.uuid(this);
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        tag.putBoolean("connected", this.isPortalConnected());
        tag.putInt("color", this.color);
        return tag;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create((BlockEntity)this);
        return packet;
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        this.handleUpdateTag(pkt.getTag(), lookupProvider);
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        this.clientIsConnected = tag.getBoolean("connected");
        this.color = tag.getInt("color");
    }

    public void setChanged() {
        super.setChanged();
        if (PortalFrameBlockEntity.isPrimary(this.getBlockState())) {
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
            }
        } else {
            this.ifNeighborPresent(PortalFrameBlockEntity::setChanged);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState blockState, PortalFrameBlockEntity portalFrameBlockEntity) {
        if (level.getGameTime() % 5L == 0L) {
            PortalManager.INSTANCE.processCooldownTick(portalFrameBlockEntity.getUUID(), -5);
        }
        if (portalFrameBlockEntity.active) {
            portalFrameBlockEntity.active = --portalFrameBlockEntity.activeCooldown > 0;
            level.getEntities((Entity)null, blockState.getShape((BlockGetter)level, pos).bounds().move(pos), ((PortalFrameBlock)blockState.getBlock())::canTeleport).forEach(portalFrameBlockEntity::teleport);
        }
    }

    public void setActive() {
        this.active = true;
        this.activeCooldown = 10;
    }

    public int getColor() {
        if (PortalFrameBlockEntity.isPrimary(this.getBlockState())) {
            return this.color;
        }
        return this.ifNeighborPresentExecute(PortalFrameBlockEntity::getColor, -1);
    }

    private void setColor(int color, boolean updateOther) {
        if (color == this.getColor() || !this.isPortalConnected()) {
            return;
        }
        if (PortalFrameBlockEntity.isPrimary(this.getBlockState())) {
            this.color = color;
            this.setChanged();
            if (updateOther) {
                this.ifOtherPortalFramePresent(frame -> frame.setColor(color, false));
            }
        } else {
            this.ifNeighborPresent(tile -> tile.setColor(color));
        }
    }

    public void setColor(int color) {
        this.setColor(color, true);
    }

    record PortalId(Optional<UUID> _uuid) {
        UUID uuid(PortalFrameBlockEntity portalFrameBlockEntity) {
            UUID uUID;
            BlockEntity blockEntity;
            if (portalFrameBlockEntity.level.isLoaded(portalFrameBlockEntity.getBlockPos().below()) && (blockEntity = portalFrameBlockEntity.level.getBlockEntity(portalFrameBlockEntity.getBlockPos().below())) instanceof PortalFrameBlockEntity) {
                PortalFrameBlockEntity be = (PortalFrameBlockEntity)blockEntity;
                uUID = be.getUUID();
            } else {
                uUID = null;
            }
            return this._uuid.orElse(uUID);
        }
    }
}

