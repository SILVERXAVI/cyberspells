/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.Containers
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.phys.AABB
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.items.IItemHandlerModifiable
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.maxwell.cyber_ware_port.common.block.robosurgeon;

import com.maxwell.cyber_ware_port.api.event.CyberwareSurgeryEvent;
import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.BodyRegionEnum;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.surgeon.SurgeryManager;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.surgeon.SurgerySyncHelper;
import com.maxwell.cyber_ware_port.common.block.surgerychamber.SurgeryChamberBlock;
import com.maxwell.cyber_ware_port.common.block.surgerychamber.SurgeryChamberBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.container.RobosurgeonMenu;
import com.maxwell.cyber_ware_port.common.item.base.CyberwareSlotType;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.common.network.SyncSurgeryProgressPacket;
import com.maxwell.cyber_ware_port.init.ModBlockEntities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RobosurgeonBlockEntity
extends BlockEntity
implements MenuProvider {
    public static final int TOTAL_SLOTS = BodyRegionEnum.getTotalSlots();
    public static final int SLOTS_PER_PART = 9;
    public static final int SLOT_EYES = BodyRegionEnum.EYES.getStartSlot();
    public static final int SLOT_BRAIN = BodyRegionEnum.BRAIN.getStartSlot();
    public static final int SLOT_HEART = BodyRegionEnum.HEART.getStartSlot();
    public static final int SLOT_LUNGS = BodyRegionEnum.LUNGS.getStartSlot();
    public static final int SLOT_STOMACH = BodyRegionEnum.STOMACH.getStartSlot();
    public static final int SLOT_SKIN = BodyRegionEnum.SKIN.getStartSlot();
    public static final int SLOT_MUSCLE = BodyRegionEnum.MUSCLE.getStartSlot();
    public static final int SLOT_BONES = BodyRegionEnum.BONES.getStartSlot();
    public static final int SLOT_ARMS = BodyRegionEnum.ARMS.getStartSlot();
    public static final int SLOT_HANDS = BodyRegionEnum.HANDS.getStartSlot();
    public static final int SLOT_LEGS = BodyRegionEnum.LEGS.getStartSlot();
    public static final int SLOT_BOOTS = BodyRegionEnum.BOOTS.getStartSlot();
    private final ItemStackHandler itemHandler = this.createItemHandler();
    private final ContainerData data = this.createContainerData();
    private int progress = 0;
    private int maxProgress = 100;

    public RobosurgeonBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super((BlockEntityType)ModBlockEntities.ROBO_SURGEON.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RobosurgeonBlockEntity entity) {
        if (level.isClientSide) {
            return;
        }
        BlockPos chamberPos = entity.findChamberPos();
        if (chamberPos == null) {
            entity.resetProgress();
            return;
        }
        BlockEntity be = level.getBlockEntity(chamberPos);
        if (!(be instanceof SurgeryChamberBlockEntity)) {
            entity.resetProgress();
            return;
        }
        SurgeryChamberBlockEntity chamber = (SurgeryChamberBlockEntity)be;
        LivingEntity patient = entity.findPatient(chamberPos);
        if (chamber.isOpen() || !(patient instanceof ServerPlayer)) {
            if (entity.progress > 0) {
                ServerPlayer sp;
                entity.resetProgress();
                RobosurgeonBlockEntity.syncProgress(entity, patient instanceof ServerPlayer ? (sp = (ServerPlayer)patient) : null);
            }
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)patient;
        if (entity.needsSurgery(serverPlayer) && entity.checkRequirements(serverPlayer)) {
            ++entity.progress;
            RobosurgeonBlockEntity.setChanged((Level)level, (BlockPos)pos, (BlockState)state);
            RobosurgeonBlockEntity.syncProgress(entity, serverPlayer);
            if (entity.progress % 20 == 0) {
                serverPlayer.hurt(level.damageSources().magic(), 1.0f);
                level.playSound(null, chamberPos, SoundEvents.PLAYER_HURT, SoundSource.PLAYERS, 0.5f, 1.0f);
                if (entity.progress % 40 == 0) {
                    level.playSound(null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.3f, 1.5f);
                    if (entity.progress % 80 == 0) {
                        level.playSound(null, pos, (SoundEvent)SoundEvents.ARMOR_EQUIP_IRON.value(), SoundSource.BLOCKS, 0.2f, 0.8f);
                    }
                }
            }
            if (entity.progress >= entity.maxProgress) {
                entity.performSurgery(serverPlayer);
                level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.5f, 2.0f);
                level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.5f, 1.0f);
                entity.resetProgress();
                RobosurgeonBlockEntity.syncProgress(entity, serverPlayer);
                chamber.setDoorState(true);
            }
        } else if (entity.progress > 0) {
            entity.resetProgress();
            RobosurgeonBlockEntity.syncProgress(entity, serverPlayer);
            chamber.setDoorState(true);
        }
    }

    private static void syncProgress(RobosurgeonBlockEntity entity, @Nullable ServerPlayer player) {
        if (player != null) {
            PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)new SyncSurgeryProgressPacket(entity.progress, entity.maxProgress), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    public void performSurgery(ServerPlayer player) {
        if (!this.checkRequirements(player)) {
            return;
        }
        CyberwareSurgeryEvent.Pre preEvent = new CyberwareSurgeryEvent.Pre((LivingEntity)player, this);
        NeoForge.EVENT_BUS.post((Event)preEvent);
        CyberwareUserData userData = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        SurgeryManager.execute(player, (IItemHandlerModifiable)this.itemHandler, userData.getInstalledCyberware());
        userData.recalculateCapacity(player);
        userData.syncToClient(player);
        this.populateGhostItems(player);
        player.level().playSound(null, player.blockPosition(), SoundEvents.IRON_GOLEM_HURT, SoundSource.PLAYERS, 1.0f, 1.0f);
        NeoForge.EVENT_BUS.post((Event)new CyberwareSurgeryEvent.Post((LivingEntity)player, this));
    }

    public void populateGhostItems(ServerPlayer player) {
        CyberwareUserData userData = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        if (SurgerySyncHelper.updateGhosts(userData.getInstalledCyberware(), (IItemHandlerModifiable)this.itemHandler)) {
            this.setChanged();
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }

    private boolean checkRequirements(ServerPlayer player) {
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        ItemStackHandler playerBody = data.getInstalledCyberware();
        HashMap<Item, Integer> futureCounts = new HashMap<Item, Integer>();
        ArrayList<ItemStack> futureBody = new ArrayList<ItemStack>();
        for (int i = 0; i < TOTAL_SLOTS; ++i) {
            ItemStack finalStack;
            ItemStack table = this.itemHandler.getStackInSlot(i);
            ItemStack itemStack = finalStack = SurgeryManager.isGhost(table) ? playerBody.getStackInSlot(i) : table;
            if (finalStack.isEmpty()) continue;
            futureBody.add(finalStack);
            futureCounts.put(finalStack.getItem(), futureCounts.getOrDefault(finalStack.getItem(), 0) + finalStack.getCount());
        }
        for (ItemStack stack : futureBody) {
            ICyberware cw = this.getCyber(stack);
            if (cw == null) continue;
            if ((Integer)futureCounts.get(stack.getItem()) > cw.getMaxInstallAmount(stack)) {
                return false;
            }
            for (Item req : cw.getPrerequisites(stack)) {
                if (!futureBody.stream().noneMatch(s -> s.is(req))) continue;
                return false;
            }
        }
        return true;
    }

    private boolean isGhost(ItemStack stack) {
        return SurgeryManager.isGhost(stack);
    }

    private ICyberware getCyber(ItemStack stack) {
        return CyberwareAPI.getCyberware(stack);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(this, TOTAL_SLOTS){

            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                ICyberware cw = CyberwareAPI.getCyberware(stack);
                if (cw == null) {
                    return false;
                }
                return CyberwareSlotType.fromId(cw.getSlot(stack)) == CyberwareSlotType.fromId(slot);
            }
        };
    }

    private boolean needsSurgery(ServerPlayer player) {
        CyberwareUserData data = (CyberwareUserData)player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        ItemStackHandler playerBody = data.getInstalledCyberware();
        for (int i = 0; i < TOTAL_SLOTS; ++i) {
            ItemStack table = this.itemHandler.getStackInSlot(i);
            if (SurgeryManager.isGhost(table) || ItemStack.matches((ItemStack)table, (ItemStack)playerBody.getStackInSlot(i))) continue;
            return true;
        }
        return false;
    }

    private BlockPos findChamberPos() {
        if (this.level == null) {
            return null;
        }
        BlockPos below = this.worldPosition.below();
        BlockState state = this.level.getBlockState(below);
        if (state.getBlock() instanceof SurgeryChamberBlock) {
            return state.getValue(SurgeryChamberBlock.HALF) == DoubleBlockHalf.UPPER ? below.below() : below;
        }
        return null;
    }

    private LivingEntity findPatient(BlockPos chamberPos) {
        if (this.level == null) {
            return null;
        }
        AABB box = new AABB(chamberPos).deflate(0.3, 0.1, 0.3).inflate(0.0, 0.9, 0.0);
        List entities = this.level.getEntitiesOfClass(LivingEntity.class, box);
        return entities.isEmpty() ? null : (LivingEntity)entities.get(0);
    }

    private void resetProgress() {
        this.progress = 0;
    }

    @NotNull
    public Component getDisplayName() {
        return Component.translatable((String)"container.cyber_ware_port.robosurgeon");
    }

    @Nullable
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player p) {
        if (p instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)p;
            this.populateGhostItems(sp);
        }
        return new RobosurgeonMenu(id, inv, this, this.data);
    }

    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("inventory", (Tag)this.itemHandler.serializeNBT(provider));
        tag.putInt("progress", this.progress);
    }

    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.itemHandler.deserializeNBT(provider, tag.getCompound("inventory"));
        this.progress = tag.getInt("progress");
    }

    public void drops() {
        if (this.level == null) {
            return;
        }
        SimpleContainer inv = new SimpleContainer(TOTAL_SLOTS);
        for (int i = 0; i < TOTAL_SLOTS; ++i) {
            ItemStack stack = this.itemHandler.getStackInSlot(i);
            if (stack.isEmpty() || this.isGhost(stack)) continue;
            inv.setItem(i, stack);
        }
        Containers.dropContents((Level)this.level, (BlockPos)this.worldPosition, (Container)inv);
    }

    private ContainerData createContainerData() {
        return new ContainerData(){

            public int get(int i) {
                return i == 0 ? RobosurgeonBlockEntity.this.progress : RobosurgeonBlockEntity.this.maxProgress;
            }

            public void set(int i, int v) {
                if (i == 0) {
                    RobosurgeonBlockEntity.this.progress = v;
                } else {
                    RobosurgeonBlockEntity.this.maxProgress = v;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }
}

