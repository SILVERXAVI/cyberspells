/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.Connection
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.block.entity;

import com.perigrine3.createcybernetics.block.entity.ModBlockEntities;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HoloprojectorBlockEntity
extends BlockEntity {
    public static final String NBT_PROJECTION_MODE = "ProjectionMode";
    private static final String NBT_ITEM = "ProjectedItem";
    private static final String NBT_PLAYER_UUID = "ProjectedPlayerUUID";
    private static final String NBT_PLAYER_NAME = "ProjectedPlayerName";
    private static final String NBT_CYBERWARE_SNAPSHOT = "ProjectedCyberwareSnapshot";
    private static final String NBT_ENTITY_TYPE = "ProjectedEntityType";
    private static final String NBT_ENTITY_NAME = "ProjectedEntityName";
    private static final String NBT_ENTITY_NBT = "ProjectedEntityNbt";
    private static final String TAG_ITEM_ALPHA = "cc_item_alpha";
    private static final String TAG_PLAYER_ALPHA = "cc_player_alpha";
    private float itemAlpha = 0.75f;
    private float playerAlpha = 0.75f;
    private ProjectionMode mode = ProjectionMode.NONE;
    private ItemStack projectedStack = ItemStack.EMPTY;
    @Nullable
    private UUID projectedPlayerUuid = null;
    private String projectedPlayerName = "";
    private CompoundTag projectedCyberwareSnapshot = new CompoundTag();
    private String projectedEntityTypeId = "";
    private String projectedEntityName = "";
    private CompoundTag projectedEntityNbt = new CompoundTag();

    public float getItemAlpha() {
        return this.itemAlpha;
    }

    public float getPlayerAlpha() {
        return this.playerAlpha;
    }

    public void setItemAlpha(float a) {
        this.itemAlpha = Mth.clamp((float)a, (float)0.0f, (float)1.0f);
        this.setChangedAndSync();
    }

    public void setPlayerAlpha(float a) {
        this.playerAlpha = Mth.clamp((float)a, (float)0.0f, (float)1.0f);
        this.setChangedAndSync();
    }

    public HoloprojectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOLOPROJECTOR_BLOCKENTITY.get(), pos, state);
    }

    public ProjectionMode getMode() {
        return this.mode;
    }

    public ItemStack getProjectedStack() {
        return this.projectedStack;
    }

    @Nullable
    public UUID getProjectedPlayerUuid() {
        return this.projectedPlayerUuid;
    }

    public String getProjectedPlayerName() {
        return this.projectedPlayerName;
    }

    public CompoundTag getProjectedCyberwareSnapshot() {
        return this.projectedCyberwareSnapshot;
    }

    public String getProjectedEntityTypeId() {
        return this.projectedEntityTypeId;
    }

    public String getProjectedEntityName() {
        return this.projectedEntityName;
    }

    public CompoundTag getProjectedEntityNbt() {
        return this.projectedEntityNbt;
    }

    public void setProjectedItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            this.clearProjection();
            return;
        }
        this.mode = ProjectionMode.ITEM;
        this.projectedStack = stack.copy();
        this.projectedStack.setCount(1);
        this.projectedPlayerUuid = null;
        this.projectedPlayerName = "";
        this.projectedCyberwareSnapshot = new CompoundTag();
        this.projectedEntityTypeId = "";
        this.projectedEntityName = "";
        this.projectedEntityNbt = new CompoundTag();
        this.setChangedAndSync();
    }

    public void setProjectedPlayer(UUID uuid, String name) {
        if (uuid == null) {
            this.clearProjection();
            return;
        }
        this.mode = ProjectionMode.PLAYER;
        this.projectedPlayerUuid = uuid;
        this.projectedPlayerName = name == null ? "" : name;
        this.projectedStack = ItemStack.EMPTY;
        this.projectedEntityTypeId = "";
        this.projectedEntityName = "";
        this.projectedEntityNbt = new CompoundTag();
        this.setChangedAndSync();
    }

    public void setProjectedPlayerFrom(ServerPlayer player) {
        if (player == null) {
            this.clearProjection();
            return;
        }
        this.mode = ProjectionMode.PLAYER;
        this.projectedPlayerUuid = player.getUUID();
        this.projectedPlayerName = player.getGameProfile().getName();
        this.projectedStack = ItemStack.EMPTY;
        this.projectedEntityTypeId = "";
        this.projectedEntityName = "";
        this.projectedEntityNbt = new CompoundTag();
        this.projectedCyberwareSnapshot = new CompoundTag();
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data != null) {
            CompoundTag snap = data.serializeNBT((HolderLookup.Provider)player.registryAccess());
            this.projectedCyberwareSnapshot = snap != null ? snap : new CompoundTag();
        }
        this.setChangedAndSync();
    }

    public void setProjectedEntity(String typeId, @Nullable CompoundTag nbt, @Nullable String name) {
        if (typeId == null || typeId.isBlank()) {
            this.clearProjection();
            return;
        }
        this.mode = ProjectionMode.ENTITY;
        this.projectedEntityTypeId = typeId;
        this.projectedEntityName = name == null ? "" : name;
        this.projectedEntityNbt = nbt == null ? new CompoundTag() : nbt.copy();
        this.projectedStack = ItemStack.EMPTY;
        this.projectedPlayerUuid = null;
        this.projectedPlayerName = "";
        this.projectedCyberwareSnapshot = new CompoundTag();
        this.setChangedAndSync();
    }

    public void clearProjection() {
        this.mode = ProjectionMode.NONE;
        this.projectedStack = ItemStack.EMPTY;
        this.projectedPlayerUuid = null;
        this.projectedPlayerName = "";
        this.projectedCyberwareSnapshot = new CompoundTag();
        this.projectedEntityTypeId = "";
        this.projectedEntityName = "";
        this.projectedEntityNbt = new CompoundTag();
        this.setChangedAndSync();
    }

    private void setChangedAndSync() {
        this.setChanged();
        if (this.level == null) {
            return;
        }
        if (!this.level.isClientSide) {
            this.level.blockEntityChanged(this.worldPosition);
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat(TAG_ITEM_ALPHA, this.itemAlpha);
        tag.putFloat(TAG_PLAYER_ALPHA, this.playerAlpha);
        tag.putString(NBT_PROJECTION_MODE, this.mode.name());
        if (this.mode == ProjectionMode.ITEM && !this.projectedStack.isEmpty()) {
            tag.put(NBT_ITEM, this.projectedStack.save(registries));
        }
        if (this.mode == ProjectionMode.PLAYER && this.projectedPlayerUuid != null) {
            tag.putString(NBT_PLAYER_UUID, this.projectedPlayerUuid.toString());
            if (!this.projectedPlayerName.isEmpty()) {
                tag.putString(NBT_PLAYER_NAME, this.projectedPlayerName);
            }
            if (this.projectedCyberwareSnapshot != null && !this.projectedCyberwareSnapshot.isEmpty()) {
                tag.put(NBT_CYBERWARE_SNAPSHOT, (Tag)this.projectedCyberwareSnapshot.copy());
            }
        }
        if (this.mode == ProjectionMode.ENTITY && this.projectedEntityTypeId != null && !this.projectedEntityTypeId.isBlank()) {
            tag.putString(NBT_ENTITY_TYPE, this.projectedEntityTypeId);
            if (!this.projectedEntityName.isEmpty()) {
                tag.putString(NBT_ENTITY_NAME, this.projectedEntityName);
            }
            if (this.projectedEntityNbt != null && !this.projectedEntityNbt.isEmpty()) {
                tag.put(NBT_ENTITY_NBT, (Tag)this.projectedEntityNbt.copy());
            }
        }
    }

    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.itemAlpha = tag.contains(TAG_ITEM_ALPHA, 5) ? tag.getFloat(TAG_ITEM_ALPHA) : 0.75f;
        this.playerAlpha = tag.contains(TAG_PLAYER_ALPHA, 5) ? tag.getFloat(TAG_PLAYER_ALPHA) : 0.75f;
        this.itemAlpha = Mth.clamp((float)this.itemAlpha, (float)0.0f, (float)1.0f);
        this.playerAlpha = Mth.clamp((float)this.playerAlpha, (float)0.0f, (float)1.0f);
        this.mode = HoloprojectorBlockEntity.readMode(tag.getString(NBT_PROJECTION_MODE));
        this.projectedStack = ItemStack.EMPTY;
        this.projectedPlayerUuid = null;
        this.projectedPlayerName = "";
        this.projectedCyberwareSnapshot = new CompoundTag();
        this.projectedEntityTypeId = "";
        this.projectedEntityName = "";
        this.projectedEntityNbt = new CompoundTag();
        if (this.mode == ProjectionMode.ITEM && tag.contains(NBT_ITEM, 10)) {
            CompoundTag itemTag = tag.getCompound(NBT_ITEM);
            this.projectedStack = ItemStack.parse((HolderLookup.Provider)registries, (Tag)itemTag).orElse(ItemStack.EMPTY);
            if (this.projectedStack.isEmpty()) {
                this.mode = ProjectionMode.NONE;
            }
        }
        if (this.mode == ProjectionMode.PLAYER && tag.contains(NBT_PLAYER_UUID, 8)) {
            try {
                this.projectedPlayerUuid = UUID.fromString(tag.getString(NBT_PLAYER_UUID));
            }
            catch (IllegalArgumentException ignored) {
                this.projectedPlayerUuid = null;
                this.mode = ProjectionMode.NONE;
            }
            String string = this.projectedPlayerName = tag.contains(NBT_PLAYER_NAME, 8) ? tag.getString(NBT_PLAYER_NAME) : "";
            if (tag.contains(NBT_CYBERWARE_SNAPSHOT, 10)) {
                this.projectedCyberwareSnapshot = tag.getCompound(NBT_CYBERWARE_SNAPSHOT).copy();
            }
        }
        if (this.mode == ProjectionMode.ENTITY) {
            this.projectedEntityTypeId = tag.contains(NBT_ENTITY_TYPE, 8) ? tag.getString(NBT_ENTITY_TYPE) : "";
            this.projectedEntityName = tag.contains(NBT_ENTITY_NAME, 8) ? tag.getString(NBT_ENTITY_NAME) : "";
            this.projectedEntityNbt = tag.contains(NBT_ENTITY_NBT, 10) ? tag.getCompound(NBT_ENTITY_NBT).copy() : new CompoundTag();
            if (this.projectedEntityTypeId.isBlank()) {
                this.mode = ProjectionMode.NONE;
            }
        }
    }

    private static ProjectionMode readMode(String raw) {
        if (raw == null || raw.isBlank()) {
            return ProjectionMode.NONE;
        }
        try {
            return ProjectionMode.valueOf(raw);
        }
        catch (IllegalArgumentException ignored) {
            return ProjectionMode.NONE;
        }
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }

    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        this.loadAdditional(tag, registries);
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            this.loadAdditional(tag, registries);
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }

    public static enum ProjectionMode {
        NONE,
        ITEM,
        PLAYER,
        ENTITY;

    }
}

