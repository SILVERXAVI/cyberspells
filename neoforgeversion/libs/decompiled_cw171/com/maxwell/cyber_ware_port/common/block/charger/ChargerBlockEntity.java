/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.capabilities.Capabilities$EnergyStorage
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.energy.EnergyStorage
 *  net.neoforged.neoforge.energy.IEnergyStorage
 */
package com.maxwell.cyber_ware_port.common.block.charger;

import com.maxwell.cyber_ware_port.api.event.CyberwareEvents;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ChargerBlockEntity
extends BlockEntity {
    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(1000000, 10000);
    private boolean isDrainMode = false;

    public ChargerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super((BlockEntityType)ModBlockEntities.CHARGER.get(), pPos, pBlockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) {
            return;
        }
        this.handlePlayerEnergyTransfer(level, pos);
        if (this.isDrainMode && this.energyStorage.getEnergyStored() > 0) {
            this.distributeEnergy(level, pos);
        }
    }

    private void handlePlayerEnergyTransfer(Level level, BlockPos pos) {
        AABB area = new AABB(pos).inflate(0.2, 1.0, 0.2);
        List players = level.getEntitiesOfClass(Player.class, area);
        for (Player player : players) {
            IEnergyStorage userData;
            CyberwareEvents.Recharge event = new CyberwareEvents.Recharge((LivingEntity)player, this, this.isDrainMode);
            NeoForge.EVENT_BUS.post((Event)event);
            if (event.isCanceled() || (userData = (IEnergyStorage)player.getCapability(Capabilities.EnergyStorage.ENTITY, null)) == null) continue;
            int maxTransfer = 10000;
            if (this.isDrainMode) {
                int space;
                int extracted = userData.extractEnergy(maxTransfer, true);
                int toReceive = Math.min(extracted, space = this.energyStorage.getMaxEnergyStored() - this.energyStorage.getEnergyStored());
                if (toReceive <= 0) continue;
                userData.extractEnergy(toReceive, false);
                this.modifyEnergy(toReceive);
                continue;
            }
            int available = this.energyStorage.getEnergyStored();
            int received = userData.receiveEnergy(Math.min(available, maxTransfer), true);
            if (received <= 0) continue;
            this.modifyEnergy(-received);
            userData.receiveEnergy(received, false);
        }
    }

    private void distributeEnergy(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.energyStorage.getEnergyStored() <= 0) break;
            BlockPos targetPos = pos.relative(direction);
            IEnergyStorage targetStorage = (IEnergyStorage)level.getCapability(Capabilities.EnergyStorage.BLOCK, targetPos, (Object)direction.getOpposite());
            if (targetStorage == null || !targetStorage.canReceive()) continue;
            int extracted = this.energyStorage.extractEnergy(10000, true);
            int received = targetStorage.receiveEnergy(extracted, false);
            this.energyStorage.extractEnergy(received, false);
        }
    }

    private void modifyEnergy(int amount) {
        int current = this.energyStorage.getEnergyStored();
        int capacity = this.energyStorage.getMaxEnergyStored();
        int next = Math.max(0, Math.min(current + amount, capacity));
        this.energyStorage.setEnergyInternal(next);
        this.setChanged();
    }

    public void toggleMode(Player player) {
        boolean bl = this.isDrainMode = !this.isDrainMode;
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        }
        if (this.isDrainMode) {
            player.sendSystemMessage((Component)Component.literal((String)"Charger Mode: DRAIN (Player -> Network)"));
        } else {
            player.sendSystemMessage((Component)Component.literal((String)"Charger Mode: CHARGE (Network -> Player)"));
        }
        this.setChanged();
    }

    public IEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("Energy", this.energyStorage.serializeNBT(pRegistries));
        pTag.putBoolean("IsDrainMode", this.isDrainMode);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("Energy")) {
            this.energyStorage.deserializeNBT(pRegistries, pTag.get("Energy"));
        }
        this.isDrainMode = pTag.getBoolean("IsDrainMode");
    }

    private class CustomEnergyStorage
    extends EnergyStorage {
        public CustomEnergyStorage(int capacity, int maxTransfer) {
            super(capacity, maxTransfer);
        }

        public void setEnergyInternal(int energy) {
            this.energy = energy;
        }

        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (ChargerBlockEntity.this.isDrainMode) {
                return 0;
            }
            int ret = super.receiveEnergy(maxReceive, simulate);
            if (ret > 0 && !simulate) {
                ChargerBlockEntity.this.setChanged();
            }
            return ret;
        }

        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!ChargerBlockEntity.this.isDrainMode) {
                return 0;
            }
            int ret = super.extractEnergy(maxExtract, simulate);
            if (ret > 0 && !simulate) {
                ChargerBlockEntity.this.setChanged();
            }
            return ret;
        }

        public boolean canReceive() {
            return !ChargerBlockEntity.this.isDrainMode;
        }

        public boolean canExtract() {
            return ChargerBlockEntity.this.isDrainMode;
        }
    }
}

