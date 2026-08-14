/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.Holder
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeMap
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.common.util.INBTSerializable
 *  net.neoforged.neoforge.energy.IEnergyStorage
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.NotNull
 */
package com.maxwell.cyber_ware_port.common.capability;

import com.maxwell.cyber_ware_port.api.event.CyberwareRejectionEvent;
import com.maxwell.cyber_ware_port.api.event.CyberwareToleranceEvent;
import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.common.network.SyncCyberwareDataPacket;
import com.maxwell.cyber_ware_port.common.util.CyberwareBodyStatus;
import com.maxwell.cyber_ware_port.config.CyberwareConfig;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.ArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class CyberwareUserData
implements INBTSerializable<CompoundTag>,
IEnergyStorage {
    public boolean hasCyberLeftArm = false;
    public boolean hasCyberRightArm = false;
    public boolean hasCyberLeftLeg = false;
    public boolean hasCyberRightLeg = false;
    private boolean isInitialized = false;
    private boolean isPowered = true;
    private boolean needsCapacityUpdate = true;
    private int respawnGracePeriod = 0;
    private int maxTolerance = (Integer)CyberwareConfig.MAX_TOLERANCE.get();
    private int toleranceImmunityTime = 0;
    private int currentEnergy = 0;
    private int maxEnergy = 0;
    private int lastProduction = 0;
    private int lastConsumption = 0;
    private int empTicks = 0;
    private final ItemStackHandler installedCyberware = new ItemStackHandler(RobosurgeonBlockEntity.TOTAL_SLOTS){

        protected void onContentsChanged(int slot) {
            CyberwareUserData.this.updateBodyStatus();
            CyberwareUserData.this.needsCapacityUpdate = true;
        }
    };

    public int getEmpTicks() {
        return this.empTicks;
    }

    public void setEmpTicks(int ticks) {
        this.empTicks = ticks;
        this.needsCapacityUpdate = true;
    }

    public int getImmunityTime() {
        return this.toleranceImmunityTime;
    }

    public boolean isPowered() {
        return this.isPowered && this.empTicks <= 0;
    }

    public static boolean isItemPowered(CyberwareUserData data, ICyberware cw, ItemStack stack) {
        if (!cw.isActive(stack)) {
            return false;
        }
        if (data.getEmpTicks() > 0) {
            return false;
        }
        if (cw.hasEnergyProperties(stack) && cw.getEnergyConsumption(stack) > 0) {
            return data.isPowered();
        }
        return true;
    }

    public void applyImmunity(int ticks) {
        this.toleranceImmunityTime = Math.max(this.toleranceImmunityTime, ticks);
    }

    public boolean hasCyberLeftArm() {
        return this.hasCyberLeftArm;
    }

    public boolean hasCyberRightArm() {
        return this.hasCyberRightArm;
    }

    public boolean hasCyberLeftLeg() {
        return this.hasCyberLeftLeg;
    }

    public boolean hasCyberRightLeg() {
        return this.hasCyberRightLeg;
    }

    public int getLastProduction() {
        return this.lastProduction;
    }

    public int getLastConsumption() {
        return this.lastConsumption;
    }

    public int getMaxTolerance(LivingEntity entity) {
        CyberwareToleranceEvent event = new CyberwareToleranceEvent(entity, this.maxTolerance);
        NeoForge.EVENT_BUS.post((Event)event);
        return event.getNewTolerance();
    }

    public int getTolerance(LivingEntity entity) {
        int consumed = 0;
        for (int i = 0; i < this.installedCyberware.getSlots(); ++i) {
            ItemStack stack = this.installedCyberware.getStackInSlot(i);
            ICyberware cyberware = CyberwareAPI.getCyberware(stack);
            if (cyberware == null) continue;
            consumed += cyberware.getEssenceCost(stack) * stack.getCount();
        }
        return this.getMaxTolerance(entity) - consumed;
    }

    public void recalculateCapacity(ServerPlayer player) {
        float oldMaxHealth = player.getHealth();
        float oldMaxHealthVal = player.getMaxHealth();
        float healthRatio = oldMaxHealthVal > 0.0f ? oldMaxHealth / oldMaxHealthVal : 1.0f;
        AttributeMap attributeMap = player.getAttributes();
        for (AttributeInstance instance : attributeMap.getSyncableAttributes()) {
            ArrayList toRemove = new ArrayList();
            instance.getModifiers().forEach(mod -> {
                if (mod.id().getPath().startsWith("cyberware_slot_")) {
                    toRemove.add(mod.id());
                }
            });
            toRemove.forEach(arg_0 -> ((AttributeInstance)instance).removeModifier(arg_0));
        }
        int totalCapacity = 0;
        for (int i = 0; i < this.installedCyberware.getSlots(); ++i) {
            boolean consumesEnergy;
            int slotIndex = i;
            ItemStack stack = this.installedCyberware.getStackInSlot(i);
            ICyberware cyberware = CyberwareAPI.getCyberware(stack);
            if (cyberware == null) continue;
            int count = stack.getCount();
            if (cyberware.hasEnergyProperties(stack)) {
                totalCapacity += cyberware.getEnergyStorage(stack) * count;
            }
            if (!cyberware.isActive(stack)) continue;
            boolean bl = consumesEnergy = cyberware.hasEnergyProperties(stack) && cyberware.getEnergyConsumption(stack) > 0;
            if (consumesEnergy && !this.isPowered()) continue;
            cyberware.getAttributeModifiers(stack).forEach((attribute, originalModifier) -> {
                AttributeInstance instance = attributeMap.getInstance(attribute);
                if (instance != null) {
                    ResourceLocation slotId = originalModifier.id().withSuffix("_slot_" + slotIndex);
                    AttributeModifier newModifier = new AttributeModifier(slotId, originalModifier.amount() * (double)count, originalModifier.operation());
                    instance.removeModifier(slotId);
                    instance.addTransientModifier(newModifier);
                }
            });
        }
        this.maxEnergy = totalCapacity;
        if (this.currentEnergy > this.maxEnergy) {
            this.currentEnergy = this.maxEnergy;
        }
        player.setHealth(player.getMaxHealth() * Math.min(healthRatio, 1.0f));
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!this.canReceive()) {
            return 0;
        }
        int energyReceived = Math.min(this.maxEnergy - this.currentEnergy, maxReceive);
        if (!simulate) {
            this.currentEnergy += energyReceived;
        }
        return energyReceived;
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract()) {
            return 0;
        }
        int energyExtracted = Math.min(this.currentEnergy, maxExtract);
        if (!simulate) {
            this.currentEnergy -= energyExtracted;
        }
        return energyExtracted;
    }

    public int getEnergyStored() {
        return this.currentEnergy;
    }

    public int getMaxEnergyStored() {
        return this.maxEnergy;
    }

    public boolean canExtract() {
        return this.maxEnergy > 0;
    }

    public boolean canReceive() {
        return this.maxEnergy > 0;
    }

    public void tick(ServerPlayer player) {
        if (this.toleranceImmunityTime > 0) {
            --this.toleranceImmunityTime;
        }
        if (this.respawnGracePeriod > 0) {
            --this.respawnGracePeriod;
        }
        if (this.empTicks > 0) {
            --this.empTicks;
        }
        if (this.needsCapacityUpdate) {
            this.recalculateCapacity(player);
            this.needsCapacityUpdate = false;
        }
        CyberwareBodyStatus status = new CyberwareBodyStatus(this.installedCyberware);
        this.checkSurvival(player, status);
        this.checkRejection(player);
        for (int i = 0; i < this.installedCyberware.getSlots(); ++i) {
            ItemStack stack = this.installedCyberware.getStackInSlot(i);
            ICyberware cyberware = CyberwareAPI.getCyberware(stack);
            if (cyberware == null || !CyberwareUserData.isItemPowered(this, cyberware, stack)) continue;
            cyberware.onSystemTick((LivingEntity)player, stack);
        }
        if (player.tickCount % 20 == 0) {
            this.processPowerTick(player);
        }
    }

    private void checkSurvival(ServerPlayer player, CyberwareBodyStatus status) {
        int air;
        if (!status.hasPart(BodyPartType.BRAIN)) {
            this.killPlayer(player, "cyberware.brainless");
            return;
        }
        if (!status.hasPart(BodyPartType.HEART)) {
            this.killPlayer(player, "cyberware.heartless");
            return;
        }
        if (!status.hasPart(BodyPartType.MUSCLE)) {
            this.killPlayer(player, "cyberware.nomuscles");
            return;
        }
        if (!status.hasPart(BodyPartType.BONES)) {
            this.killPlayer(player, "cyberware.cyberware_missing_bone");
            return;
        }
        if (!status.hasPart(BodyPartType.EYES)) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
        }
        if (!status.hasPart(BodyPartType.LUNGS) && (air = player.getAirSupply()) > -20) {
            player.setAirSupply(air - 1);
            if (air <= 0 && player.tickCount % 20 == 0) {
                player.hurt(player.damageSources().drown(), 2.0f);
            }
        }
        if (!status.hasPart(BodyPartType.STOMACH)) {
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 1, false, false));
        }
        if (status.getLegCount() == 0) {
            player.setForcedPose(Pose.SWIMMING);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, false, false));
        } else {
            player.setForcedPose(null);
        }
    }

    private void checkRejection(ServerPlayer player) {
        int currentTolerance = this.getTolerance((LivingEntity)player);
        if (currentTolerance <= 0) {
            if (this.respawnGracePeriod <= 0) {
                this.killPlayer(player, "cyberware.noessence");
            } else if (player.tickCount % 400 == 0) {
                player.sendSystemMessage((Component)Component.translatable((String)"cyberware.message.critical_condition").withStyle(ChatFormatting.RED));
            }
            return;
        }
        if (this.toleranceImmunityTime > 0) {
            return;
        }
        int rejectionThreshold = (Integer)CyberwareConfig.CRITICAL_ESSENCE.get();
        if (currentTolerance < rejectionThreshold) {
            CyberwareRejectionEvent event = new CyberwareRejectionEvent((LivingEntity)player, currentTolerance);
            NeoForge.EVENT_BUS.post((Event)event);
            if (event.isCanceled()) {
                return;
            }
            if (player.tickCount % 100 == 0) {
                player.setHealth(player.getHealth() - 2.0f);
            }
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 1, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1, false, false));
            if (player.getRandom().nextFloat() < 0.01f && !player.getMainHandItem().isEmpty()) {
                ItemStack stackToDrop = player.getMainHandItem().copy();
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                player.drop(stackToDrop, false, false);
            }
        }
    }

    private void processPowerTick(ServerPlayer player) {
        int totalProduction = 0;
        int totalConsumption = 0;
        for (int i = 0; i < this.installedCyberware.getSlots(); ++i) {
            ItemStack stack = this.installedCyberware.getStackInSlot(i);
            ICyberware cw = CyberwareAPI.getCyberware(stack);
            if (cw == null || !cw.hasEnergyProperties(stack)) continue;
            int count = stack.getCount();
            ICyberware.StackingRule rule = cw.getStackingEnergyRule(stack);
            totalProduction += rule.calculate(cw.getEnergyGeneration(stack), count);
            totalConsumption += rule.calculate(cw.getEnergyConsumption(stack), count);
        }
        this.lastProduction = totalProduction;
        this.lastConsumption = totalConsumption;
        boolean currentlyPowered = false;
        if (this.maxEnergy > 0) {
            this.receiveEnergy(totalProduction, false);
            boolean bl = currentlyPowered = this.currentEnergy >= totalConsumption;
            if (totalConsumption > 0) {
                if (currentlyPowered) {
                    this.extractEnergy(totalConsumption, false);
                } else {
                    this.currentEnergy = 0;
                }
            }
        } else {
            currentlyPowered = totalConsumption == 0 || totalProduction >= totalConsumption;
            this.currentEnergy = 0;
        }
        if (this.isPowered != currentlyPowered) {
            this.isPowered = currentlyPowered;
            this.recalculateCapacity(player);
        }
        this.syncToClient(player);
    }

    private void updateBodyStatus() {
        this.hasCyberLeftArm = this.isCyberwareInstalled((Item)ModItems.CYBER_ARM_LEFT.get());
        this.hasCyberRightArm = this.isCyberwareInstalled((Item)ModItems.CYBER_ARM_RIGHT.get());
        this.hasCyberLeftLeg = this.isCyberwareInstalled((Item)ModItems.CYBER_LEG_LEFT.get());
        this.hasCyberRightLeg = this.isCyberwareInstalled((Item)ModItems.CYBER_LEG_RIGHT.get());
    }

    public void syncToClient(ServerPlayer player) {
        if (player == null) {
            return;
        }
        CompoundTag tag = this.serializeNBT((HolderLookup.Provider)player.registryAccess());
        tag.putInt("MaxTolerance", this.getMaxTolerance((LivingEntity)player));
        PacketDistributor.sendToPlayersTrackingEntityAndSelf((Entity)player, (CustomPacketPayload)new SyncCyberwareDataPacket(tag, player.getId()), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public void ensureEssentialPartsAfterDeath() {
        boolean hasBrain = false;
        boolean hasHeart = false;
        boolean hasStomach = false;
        boolean hasSkin = false;
        boolean hasMuscle = false;
        boolean hasBone = false;
        for (int i = 0; i < this.installedCyberware.getSlots(); ++i) {
            ItemStack stack = this.installedCyberware.getStackInSlot(i);
            ICyberware cw = CyberwareAPI.getCyberware(stack);
            if (stack.isEmpty() || cw == null) continue;
            BodyPartType type = cw.getBodyPartType(stack);
            if (type == BodyPartType.BRAIN) {
                hasBrain = true;
            }
            if (type == BodyPartType.HEART) {
                hasHeart = true;
            }
            if (type == BodyPartType.STOMACH) {
                hasStomach = true;
            }
            if (type == BodyPartType.SKIN) {
                hasSkin = true;
            }
            if (type == BodyPartType.MUSCLE) {
                hasMuscle = true;
            }
            if (type != BodyPartType.BONES) continue;
            hasBone = true;
        }
        if (!hasBrain) {
            this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_BRAIN, new ItemStack((ItemLike)ModItems.HUMAN_BRAIN.get()));
        }
        if (!hasHeart) {
            this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_HEART, new ItemStack((ItemLike)ModItems.HUMAN_HEART.get()));
        }
        if (!hasStomach) {
            this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_STOMACH, new ItemStack((ItemLike)ModItems.HUMAN_STOMACH.get()));
        }
        if (!hasSkin) {
            this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_SKIN, new ItemStack((ItemLike)ModItems.HUMAN_SKIN.get()));
        }
        if (!hasMuscle) {
            this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_MUSCLE, new ItemStack((ItemLike)ModItems.HUMAN_MUSCLE.get()));
        }
        if (!hasBone) {
            this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_BONES, new ItemStack((ItemLike)ModItems.HUMAN_BONE.get()));
        }
    }

    public void resetToHuman() {
        for (int i = 0; i < this.installedCyberware.getSlots(); ++i) {
            this.installedCyberware.setStackInSlot(i, ItemStack.EMPTY);
        }
        this.isInitialized = false;
        this.currentEnergy = 0;
        this.fillWithHumanParts();
    }

    private void killPlayer(ServerPlayer player, final String suffix) {
        Holder fellOutOfWorldHolder = player.damageSources().fellOutOfWorld().typeHolder();
        DamageSource source = new DamageSource(this, fellOutOfWorldHolder){

            @NotNull
            public Component getLocalizedDeathMessage(@NotNull LivingEntity entity) {
                return Component.translatable((String)("death.attack." + suffix), (Object[])new Object[]{entity.getDisplayName()});
            }
        };
        player.hurt(source, Float.MAX_VALUE);
    }

    public void copyFrom(CyberwareUserData other) {
        for (int i = 0; i < this.installedCyberware.getSlots(); ++i) {
            this.installedCyberware.setStackInSlot(i, other.installedCyberware.getStackInSlot(i).copy());
        }
        this.maxTolerance = other.maxTolerance;
        this.currentEnergy = other.currentEnergy;
        this.maxEnergy = other.maxEnergy;
        this.isInitialized = other.isInitialized;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.put("InstalledCyberware", (Tag)this.installedCyberware.serializeNBT(provider));
        tag.putInt("MaxTolerance", this.maxTolerance);
        tag.putBoolean("IsInitialized", this.isInitialized);
        tag.putInt("ImmunityTime", this.toleranceImmunityTime);
        tag.putInt("MaxEnergy", this.maxEnergy);
        tag.putInt("CurrentEnergy", this.currentEnergy);
        tag.putInt("LastProd", this.lastProduction);
        tag.putInt("LastCons", this.lastConsumption);
        tag.putInt("EmpTicks", this.empTicks);
        tag.putBoolean("IsPowered", this.isPowered);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("InstalledCyberware")) {
            this.installedCyberware.deserializeNBT(provider, nbt.getCompound("InstalledCyberware"));
        }
        this.maxTolerance = nbt.contains("MaxTolerance") ? nbt.getInt("MaxTolerance") : ((Integer)CyberwareConfig.MAX_TOLERANCE.get()).intValue();
        this.isInitialized = nbt.getBoolean("IsInitialized");
        this.maxEnergy = nbt.getInt("MaxEnergy");
        this.currentEnergy = nbt.getInt("CurrentEnergy");
        this.lastProduction = nbt.getInt("LastProd");
        this.lastConsumption = nbt.getInt("LastCons");
        this.toleranceImmunityTime = nbt.getInt("ImmunityTime");
        if (nbt.contains("EmpTicks")) {
            this.empTicks = nbt.getInt("EmpTicks");
        }
        if (nbt.contains("IsPowered")) {
            this.isPowered = nbt.getBoolean("IsPowered");
        }
        this.updateBodyStatus();
    }

    public void fillWithHumanParts() {
        if (this.isInitialized) {
            return;
        }
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_BRAIN, new ItemStack((ItemLike)ModItems.HUMAN_BRAIN.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_HEART, new ItemStack((ItemLike)ModItems.HUMAN_HEART.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_STOMACH, new ItemStack((ItemLike)ModItems.HUMAN_STOMACH.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_SKIN, new ItemStack((ItemLike)ModItems.HUMAN_SKIN.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_MUSCLE, new ItemStack((ItemLike)ModItems.HUMAN_MUSCLE.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_BONES, new ItemStack((ItemLike)ModItems.HUMAN_BONE.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_EYES, new ItemStack((ItemLike)ModItems.HUMAN_EYES.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_LUNGS, new ItemStack((ItemLike)ModItems.HUMAN_LUNGS.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_ARMS, new ItemStack((ItemLike)ModItems.HUMAN_LEFT_ARM.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_ARMS + 1, new ItemStack((ItemLike)ModItems.HUMAN_RIGHT_ARM.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_HANDS, new ItemStack((ItemLike)ModItems.HUMAN_RIGHT_HAND.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_HANDS + 1, new ItemStack((ItemLike)ModItems.HUMAN_LEFT_HAND.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_LEGS, new ItemStack((ItemLike)ModItems.HUMAN_LEFT_LEG.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_LEGS + 1, new ItemStack((ItemLike)ModItems.HUMAN_RIGHT_LEG.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_BOOTS, new ItemStack((ItemLike)ModItems.HUMAN_RIGHT_FOOT.get()));
        this.installedCyberware.setStackInSlot(RobosurgeonBlockEntity.SLOT_BOOTS + 1, new ItemStack((ItemLike)ModItems.HUMAN_LEFT_FOOT.get()));
        this.isInitialized = true;
        this.updateBodyStatus();
    }

    public boolean isCyberwareInstalled(Item item) {
        for (int i = 0; i < this.installedCyberware.getSlots(); ++i) {
            if (!this.installedCyberware.getStackInSlot(i).is(item)) continue;
            return true;
        }
        return false;
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }

    public void setRespawnGracePeriod(int ticks) {
        this.respawnGracePeriod = ticks;
    }

    public ItemStackHandler getInstalledCyberware() {
        return this.installedCyberware;
    }
}

