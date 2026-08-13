/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  it.unimi.dsi.fastutil.objects.Object2IntMap$Entry
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager
 *  net.neoforged.neoforge.common.util.INBTSerializable
 *  org.jetbrains.annotations.UnknownNullability
 */
package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.data.IronsDataStorage;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.worldgen.ClearPortalFrameDataProcessor;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class PocketDimensionManager
implements INBTSerializable<CompoundTag> {
    public static final ResourceKey<Level> POCKET_DIMENSION = ResourceKey.create((ResourceKey)Registries.DIMENSION, (ResourceLocation)IronsSpellbooks.id("pocket_dimension"));
    public static final ResourceLocation POCKET_ROOM_STRUCTURE = IronsSpellbooks.id("pocket_room");
    public static final int POCKET_SPACING = 256;
    private static final String UUID_KEY = "uuid";
    private static final String INT_ID_KEY = "pocket_id";
    private static final String ID_MAP_KEY = "ids";
    private static final String NEXT_ID_KEY = "next_id";
    public static final PocketDimensionManager INSTANCE = new PocketDimensionManager();
    private int nextId;
    private final Object2IntMap<UUID> ids = new Object2IntOpenHashMap();

    public void remove(UUID uuid) {
        this.ids.remove((Object)uuid);
        IronsDataStorage.INSTANCE.setDirty();
    }

    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        ListTag entries = new ListTag();
        for (Object2IntMap.Entry entry : this.ids.object2IntEntrySet()) {
            CompoundTag tagEntry = new CompoundTag();
            tagEntry.putUUID(UUID_KEY, (UUID)entry.getKey());
            tagEntry.putInt(INT_ID_KEY, entry.getIntValue());
            entries.add((Object)tagEntry);
        }
        compoundTag.put(ID_MAP_KEY, (Tag)entries);
        compoundTag.putInt(NEXT_ID_KEY, this.nextId);
        return compoundTag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        ListTag entries = nbt.getList(ID_MAP_KEY, 10);
        int nextId = nbt.getInt(NEXT_ID_KEY);
        for (Tag tag : entries) {
            try {
                CompoundTag compoundTag = (CompoundTag)tag;
                UUID uuid = compoundTag.getUUID(UUID_KEY);
                int pocketId = compoundTag.getInt(INT_ID_KEY);
                this.ids.put((Object)uuid, pocketId);
            }
            catch (Exception e) {
                IronsSpellbooks.LOGGER.error("Failed to parse PocketDimensionManager id entry: {}: {}", (Object)tag, (Object)e.getMessage());
            }
        }
        this.nextId = nextId;
    }

    public int idFor(UUID uuid) {
        if (!this.ids.containsKey((Object)uuid)) {
            this.ids.put((Object)uuid, this.nextId);
            ++this.nextId;
            IronsDataStorage.INSTANCE.setDirty();
        }
        return this.ids.getInt((Object)uuid);
    }

    public int idFor(Player player) {
        return this.idFor(player.getUUID());
    }

    public BlockPos structurePosForId(int pocketDimensionId) {
        return BlockPos.containing((double)0.0, (double)0.0, (double)(256 * pocketDimensionId));
    }

    public BlockPos structurePosForPlayer(Player player) {
        return this.structurePosForId(this.idFor(player));
    }

    public BlockPos findPortalForStructure(ServerLevel pocketDimension, BlockPos blockPos) {
        BlockPos defaultPos = blockPos.south(10).east(7).above(2);
        if (pocketDimension.getBlockState(defaultPos).is(BlockRegistry.POCKET_PORTAL_FRAME)) {
            return defaultPos;
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < 32; ++y) {
                    BlockPos pos = blockPos.south(x).east(z).above(y);
                    if (!pocketDimension.getBlockState(pos).is(BlockRegistry.POCKET_PORTAL_FRAME)) continue;
                    return pos;
                }
            }
        }
        return defaultPos;
    }

    public boolean maybeGeneratePocketRoom(ServerPlayer player) {
        ServerLevel serverLevel = player.serverLevel();
        BlockPos structurePos = this.structurePosForPlayer((Player)player);
        ServerLevel pocketLevel = serverLevel.getServer().getLevel(POCKET_DIMENSION);
        BlockState blockState = pocketLevel.getBlockState(structurePos);
        if (blockState.isAir() && !blockState.is(Blocks.BARRIER)) {
            StructureTemplateManager structureTemplateManager = pocketLevel.getStructureManager();
            StructureTemplate structureTemplate = structureTemplateManager.getOrCreate(POCKET_ROOM_STRUCTURE);
            StructurePlaceSettings placementSettings = new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(true).addProcessor((StructureProcessor)new ClearPortalFrameDataProcessor());
            structureTemplate.placeInWorld((ServerLevelAccessor)pocketLevel, structurePos, structurePos, placementSettings, pocketLevel.getRandom(), 2);
            return true;
        }
        return false;
    }

    public void tick(Level level) {
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        if (!serverLevel.dimension().equals(POCKET_DIMENSION)) {
            return;
        }
        if (serverLevel.getGameTime() % 100L == 0L) {
            serverLevel.players().forEach(player -> {
                if (!player.isCreative() && !player.isSpectator()) {
                    int pocketX = (int)(player.getX() / 256.0) * 256;
                    int pocketZ = (int)(player.getZ() / 256.0) * 256;
                    if (player.getX() < (double)pocketX || player.getX() > (double)(pocketX + 16) || player.getZ() < (double)pocketZ || player.getZ() > (double)(pocketZ + 16)) {
                        BlockPos blockPos = this.structurePosForPlayer((Player)player);
                        BlockPos portalPos = this.findPortalForStructure(serverLevel, blockPos);
                        player.resetFallDistance();
                        player.moveTo(portalPos.getBottomCenter());
                    }
                }
            });
        }
    }
}

