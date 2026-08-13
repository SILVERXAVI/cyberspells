/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.cauldron.CauldronInteraction
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
 *  net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.tags.EntityTypeTags
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.util.StringUtil
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.EquipmentSlotGroup
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.monster.hoglin.Hoglin
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ItemUtils
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.component.ItemAttributeModifiers$Entry
 *  net.minecraft.world.level.CustomSpawner
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.gameevent.GameEvent
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.AnvilUpdateEvent
 *  net.neoforged.neoforge.event.ItemAttributeModifierEvent
 *  net.neoforged.neoforge.event.OnDatapackSyncEvent
 *  net.neoforged.neoforge.event.entity.EntityMountEvent
 *  net.neoforged.neoforge.event.entity.EntityTeleportEvent
 *  net.neoforged.neoforge.event.entity.EntityTeleportEvent$TeleportCommand
 *  net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent
 *  net.neoforged.neoforge.event.entity.ProjectileImpactEvent
 *  net.neoforged.neoforge.event.entity.item.ItemTossEvent
 *  net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent
 *  net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent
 *  net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent
 *  net.neoforged.neoforge.event.entity.living.LivingDamageEvent$Pre
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent
 *  net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 *  net.neoforged.neoforge.event.entity.player.CriticalHitEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerContainerEvent$Open
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$Clone
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerChangedDimensionEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedOutEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerRespawnEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$StartTracking
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$EntityInteractSpecific
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 *  net.neoforged.neoforge.event.level.LevelEvent$Load
 *  net.neoforged.neoforge.event.level.ModifyCustomSpawnersEvent
 *  net.neoforged.neoforge.event.server.ServerStartedEvent
 *  net.neoforged.neoforge.event.server.ServerStoppedEvent
 *  net.neoforged.neoforge.event.tick.EntityTickEvent$Pre
 *  net.neoforged.neoforge.network.PacketDistributor
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  top.theillusivec4.curios.api.CuriosApi
 *  top.theillusivec4.curios.api.event.CurioAttributeModifierEvent
 *  top.theillusivec4.curios.api.event.CurioChangeEvent
 */
package io.redspace.ironsspellbooks.player;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.events.SpellTeleportEvent;
import io.redspace.ironsspellbooks.api.item.UpgradeData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.block.BloodCauldronBlock;
import io.redspace.ironsspellbooks.block.portal_frame.PortalFrameBlockEntity;
import io.redspace.ironsspellbooks.capabilities.magic.CooldownInstance;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.PocketDimensionManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.data.IronsDataStorage;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.effect.AbyssalShroudEffect;
import io.redspace.ironsspellbooks.effect.EvasionEffect;
import io.redspace.ironsspellbooks.effect.IMobEffectEndCallback;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.ImmolateEffect;
import io.redspace.ironsspellbooks.effect.SummonTimer;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.ICritablePartEntity;
import io.redspace.ironsspellbooks.entity.spells.ice_tomb.IceTombEntity;
import io.redspace.ironsspellbooks.entity.spells.root.PreventDismount;
import io.redspace.ironsspellbooks.item.CastingItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.network.EquipmentChangedPacket;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.ModTags;
import io.redspace.ironsspellbooks.util.UpgradeUtils;
import io.redspace.ironsspellbooks.worldgen.IceSpiderPatrolSpawner;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.ModifyCustomSpawnersEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

@EventBusSubscriber
public class ServerPlayerEvents {
    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.level.isClientSide) {
            MinecraftInstanceHelper.ifPlayerPresent(localPlayer -> {
                if (ClientMagicData.isCasting() && player.getUUID().equals(localPlayer.getUUID())) {
                    event.setCanceled(true);
                }
            });
        } else {
            MagicData magicData = MagicData.getPlayerMagicData((LivingEntity)player);
            if (magicData.isCasting() && event.getItemStack() != magicData.getPlayerCastingItem()) {
                event.setCanceled(true);
            }
        }
        if (event.isCanceled()) {
            return;
        }
        Level level = player.level;
        InteractionHand hand = event.getHand();
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.has(ComponentRegistry.CASTING_IMPLEMENT)) {
            String castingSlot;
            SpellSelectionManager spellSelectionManager = new SpellSelectionManager(player);
            SpellSelectionManager.SelectionOption selectionOption = spellSelectionManager.getSelection();
            if (selectionOption == null || selectionOption.spellData.equals(SpellData.EMPTY)) {
                return;
            }
            SpellData spellData = selectionOption.spellData;
            int spellLevel = spellData.getSpell().getLevelFor(spellData.getLevel(), (LivingEntity)player);
            if (level.isClientSide()) {
                if (ClientMagicData.isCasting()) {
                    event.setCancellationResult(InteractionResult.CONSUME);
                } else {
                    if (ClientMagicData.getPlayerMana() < spellData.getSpell().getManaCost(spellLevel) || ClientMagicData.getCooldowns().isOnCooldown(spellData.getSpell()) || !ClientMagicData.getSyncedSpellData((LivingEntity)player).isSpellLearned(spellData.getSpell())) {
                        return;
                    }
                    event.setCancellationResult(InteractionResult.CONSUME);
                }
            }
            String string = castingSlot = hand.ordinal() == 0 ? SpellSelectionManager.MAINHAND : SpellSelectionManager.OFFHAND;
            if (spellData.getSpell().attemptInitiateCast(itemStack, spellLevel, level, player, selectionOption.getCastSource(), true, castingSlot)) {
                event.setCancellationResult(InteractionResult.CONSUME);
            } else {
                event.setCancellationResult(InteractionResult.FAIL);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerDropItem(ItemTossEvent event) {
        MagicData magicData;
        ItemStack itemStack = event.getEntity().getItem();
        if (itemStack.getItem() instanceof Scroll && (magicData = MagicData.getPlayerMagicData((LivingEntity)event.getPlayer())).isCasting() && magicData.getCastSource() == CastSource.SCROLL && magicData.getCastType() == CastType.CONTINUOUS) {
            itemStack.shrink(1);
        }
    }

    @SubscribeEvent
    public static void onLevelLoaded(LevelEvent.Load event) {
        ServerLevel serverLevel;
        LevelAccessor levelAccessor = event.getLevel();
        if (levelAccessor instanceof ServerLevel && (serverLevel = (ServerLevel)levelAccessor).dimension() == Level.OVERWORLD) {
            IronsDataStorage.init(serverLevel.getDataStorage());
        }
    }

    @SubscribeEvent
    public static void onServerStoppedEvent(ServerStoppedEvent event) {
        IronsSpellbooks.MCS = null;
        IronsSpellbooks.OVERWORLD = null;
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        IronsSpellbooks.MCS = event.getServer();
        IronsSpellbooks.OVERWORLD = IronsSpellbooks.MCS.overworld();
    }

    @SubscribeEvent
    public static void onLivingEquipmentChangeEvent(LivingEquipmentChangeEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            MagicData playerMagicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
            if (playerMagicData.isCasting() && (event.getFrom().getItem() instanceof CastingItem || event.getTo().getItem() instanceof CastingItem)) {
                Utils.serverSideCancelCast(serverPlayer);
                PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new EquipmentChangedPacket(), (CustomPacketPayload[])new CustomPacketPayload[0]);
                return;
            }
            boolean isFromSpellContainer = ISpellContainer.isSpellContainer(event.getFrom());
            if (isFromSpellContainer && ISpellContainer.get(event.getFrom()).getIndexForSpell(playerMagicData.getCastingSpell().getSpell()) >= 0 && !Utils.isSameItemSameComponentsIgnoreDurability(event.getFrom(), event.getTo())) {
                if (playerMagicData.isCasting()) {
                    Utils.serverSideCancelCast(serverPlayer);
                }
                PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new EquipmentChangedPacket(), (CustomPacketPayload[])new CustomPacketPayload[0]);
            } else if (isFromSpellContainer || ISpellContainer.isSpellContainer(event.getTo())) {
                PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new EquipmentChangedPacket(), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
        }
    }

    @SubscribeEvent
    public static void onCurioChangeEvent(CurioChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            if (ISpellContainer.isSpellContainer(event.getFrom()) || ISpellContainer.isSpellContainer(event.getTo())) {
                PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new EquipmentChangedPacket(), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            Utils.serverSideCancelCast(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerOpenContainer(PlayerContainerEvent.Open event) {
        ServerPlayer serverPlayer;
        MagicData playerMagicData;
        if (event.getEntity().level.isClientSide) {
            return;
        }
        Player player = event.getEntity();
        if (player instanceof ServerPlayer && (playerMagicData = MagicData.getPlayerMagicData((LivingEntity)(serverPlayer = (ServerPlayer)player))).isCasting()) {
            Utils.serverSideCancelCast(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void handleUpgradeModifiers(ItemAttributeModifierEvent event) {
        UpgradeData upgradeData = UpgradeData.getUpgradeData(event.getItemStack());
        if (upgradeData != UpgradeData.NONE) {
            try {
                EquipmentSlot equipmentSlot = EquipmentSlot.byName((String)upgradeData.getUpgradedSlot());
                EquipmentSlotGroup groupSlot = EquipmentSlotGroup.bySlot((EquipmentSlot)equipmentSlot);
                UpgradeUtils.handleAttributeEvent(event.getModifiers(), upgradeData, (atr, mod) -> event.addModifier(atr, mod, groupSlot), (atr, mod) -> event.removeModifier(atr, mod.id()), upgradeData.getUpgradedSlot());
            }
            catch (IllegalArgumentException e) {
                return;
            }
        }
    }

    @SubscribeEvent
    public static void handleCurioUpgradeModifiers(CurioAttributeModifierEvent event) {
        UpgradeData upgradeData = UpgradeData.getUpgradeData(event.getItemStack());
        if (upgradeData != UpgradeData.NONE && upgradeData.getUpgradedSlot().equals(event.getSlotContext().identifier())) {
            List<ItemAttributeModifiers.Entry> list = event.getModifiers().entries().stream().map(entry -> new ItemAttributeModifiers.Entry((Holder)entry.getKey(), (AttributeModifier)entry.getValue(), EquipmentSlotGroup.ANY)).toList();
            UpgradeUtils.handleAttributeEvent(list, upgradeData, (arg_0, arg_1) -> ((CurioAttributeModifierEvent)event).addModifier(arg_0, arg_1), (arg_0, arg_1) -> ((CurioAttributeModifierEvent)event).removeModifier(arg_0, arg_1), event.getSlotContext().identifier());
        }
    }

    @SubscribeEvent
    public static void onExperienceDroppedEvent(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player != null) {
            int ringCount = CuriosApi.getCuriosInventory((LivingEntity)player).map(inventory -> inventory.findCurios((Item)ItemRegistry.EMERALD_STONEPLATE_RING.get()).size()).orElse(0);
            for (int i = 0; i < ringCount; ++i) {
                event.setDroppedExperience((int)((double)event.getDroppedExperience() * 1.25));
            }
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            player = event.getTarget();
            if (player instanceof ServerPlayer) {
                ServerPlayer targetPlayer = (ServerPlayer)player;
                MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getSyncedData().syncToPlayer(targetPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            MagicData playerMagicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
            playerMagicData.getPlayerCooldowns().syncToPlayer(serverPlayer);
            playerMagicData.getPlayerRecasts().syncAllToPlayer();
            playerMagicData.getSyncedData().syncToPlayer(serverPlayer);
            PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncManaPacket(playerMagicData), (CustomPacketPayload[])new CustomPacketPayload[0]);
            CameraShakeManager.doSync(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTrackingEntity(PlayerEvent.StartTracking event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayerRecipient = (ServerPlayer)player;
            Entity entity = event.getTarget();
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                for (MobEffectInstance inst : livingEntity.getActiveEffects()) {
                    if (!(inst.getEffect().value() instanceof ISyncedMobEffect)) continue;
                    serverPlayerRecipient.connection.send((Packet)new ClientboundUpdateMobEffectPacket(livingEntity.getId(), inst, false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.level.isClientSide) {
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                Utils.serverSideCancelCast(serverPlayer);
                MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getPlayerRecasts().removeAll(RecastResult.DEATH);
            }
            entity.getActiveEffects().forEach(mobEffectInstance -> {
                Object patt0$temp = mobEffectInstance.getEffect().value();
                if (patt0$temp instanceof IMobEffectEndCallback) {
                    IMobEffectEndCallback callback = (IMobEffectEndCallback)patt0$temp;
                    callback.onEffectRemoved(entity, mobEffectInstance.getAmplifier());
                }
            });
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onSpellTeleport(SpellTeleportEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            if (ItemRegistry.TELEPORTATION_AMULET.get().isEquippedBy(livingEntity)) {
                livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.EVASION, 60, 0, false, false, true));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer newServerPlayer = (ServerPlayer)player;
            if (event.isWasDeath()) {
                event.getOriginal().getActiveEffects().forEach(effect -> {
                    if (effect.getEffect() instanceof SummonTimer) {
                        newServerPlayer.addEffect(effect, (Entity)newServerPlayer);
                    }
                });
                IronsSpellbooks.LOGGER.debug("onPlayerCloned: copy data: client: {}", (Object)newServerPlayer.level.isClientSide);
                MagicData oldMagicData = MagicData.getPlayerMagicData((LivingEntity)event.getOriginal());
                MagicData newMagicData = MagicData.getPlayerMagicData((LivingEntity)newServerPlayer);
                newMagicData.setSyncedData(oldMagicData.getSyncedData().getPersistentData(newServerPlayer));
                oldMagicData.getPlayerCooldowns().getSpellCooldowns().forEach((spellId, cooldown) -> newMagicData.getPlayerCooldowns().getSpellCooldowns().put((String)spellId, (CooldownInstance)cooldown));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            Utils.serverSideCancelCast(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            serverPlayer.clearFire();
            serverPlayer.setTicksFrozen(0);
            List data = serverPlayer.getEntityData().packDirty();
            if (data != null) {
                serverPlayer.connection.send((Packet)new ClientboundSetEntityDataPacket(serverPlayer.getId(), data));
            }
            Utils.serverSideCancelCast(serverPlayer);
            serverPlayer.getActiveEffects().forEach(effect -> {
                if (effect.getEffect() instanceof SummonTimer) {
                    serverPlayer.server.getPlayerList().sendActivePlayerEffects(serverPlayer);
                }
            });
            MagicData.getPlayerMagicData((LivingEntity)serverPlayer).setMana((int)(serverPlayer.getAttributeValue(AttributeRegistry.MAX_MANA) * (Double)ServerConfigs.MANA_SPAWN_PERCENT.get()));
        }
    }

    @SubscribeEvent
    public static void fixDragonCrits(CriticalHitEvent event) {
        if (event.getTarget().level.isClientSide) {
            return;
        }
        Entity entity = event.getTarget();
        if (entity instanceof ICritablePartEntity) {
            boolean defaultShouldCrit;
            ICritablePartEntity dragonPartEntity = (ICritablePartEntity)entity;
            Entity part = (Entity)dragonPartEntity;
            Player attacker = event.getEntity();
            boolean bl = defaultShouldCrit = (double)attacker.getAttackStrengthScale(0.5f) > 0.9 && attacker.fallDistance > 0.0f && !attacker.onGround() && !attacker.onClimbable() && !attacker.isInWater() && !attacker.hasEffect(MobEffects.BLINDNESS) && !attacker.isPassenger() && !attacker.isSprinting();
            if (defaultShouldCrit) {
                event.setCriticalHit(true);
                if (event.getDamageMultiplier() == 1.0f) {
                    event.setDamageMultiplier(1.5f);
                }
                AABB boundingBox = part.getBoundingBox();
                Vec3 vec3 = boundingBox.getCenter();
                MagicManager.spawnParticles(event.getEntity().level, (ParticleOptions)ParticleTypes.CRIT, vec3.x, vec3.y, vec3.z, 25, boundingBox.getXsize() * 0.6, boundingBox.getYsize() * 0.6, boundingBox.getZsize() * 0.6, 0.0, false);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        Creeper creeper;
        Entity entity;
        LivingEntity livingEntity = event.getEntity();
        if (event.getSource().getEntity() != null && (entity = livingEntity.getVehicle()) instanceof IceTombEntity) {
            IceTombEntity iceTomb = (IceTombEntity)entity;
            if (!DamageSources.isFriendlyFireBetween(event.getSource().getEntity(), (Entity)livingEntity)) {
                event.setCanceled(true);
                iceTomb.hurt(event.getSource(), event.getOriginalAmount());
                return;
            }
        }
        if (livingEntity instanceof ServerPlayer || livingEntity instanceof IMagicEntity) {
            if (ItemRegistry.FIREWARD_RING.get().isEquippedBy(livingEntity) && event.getSource().is(DamageTypeTags.IS_FIRE)) {
                event.getEntity().clearFire();
                event.setCanceled(true);
                return;
            }
            MagicData playerMagicData = MagicData.getPlayerMagicData(livingEntity);
            if (livingEntity.hasEffect(MobEffectRegistry.EVASION)) {
                if (EvasionEffect.doEffect(livingEntity, event.getSource())) {
                    event.setCanceled(true);
                    return;
                }
            } else if (livingEntity.hasEffect(MobEffectRegistry.ABYSSAL_SHROUD) && AbyssalShroudEffect.doEffect(livingEntity, event.getSource())) {
                event.setCanceled(true);
                return;
            }
            if (livingEntity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
                if (playerMagicData.isCasting() && playerMagicData.getCastingSpell().getSpell().canBeInterrupted((Player)serverPlayer) && playerMagicData.getCastDurationRemaining() > 0 && !event.getSource().is(DamageTypeTagGenerator.LONG_CAST_IGNORE) && !playerMagicData.popMarkedPoison()) {
                    Utils.serverSideCancelCast(serverPlayer);
                }
            }
        }
        if (((Boolean)ServerConfigs.BETTER_CREEPER_THUNDERHIT.get()).booleanValue() && event.getSource().is(DamageTypeTags.IS_FIRE) && (entity = event.getEntity()) instanceof Creeper && (creeper = (Creeper)entity).isPowered()) {
            event.setCanceled(true);
            return;
        }
    }

    @SubscribeEvent
    public static void onBeforeDamageTaken(LivingDamageEvent.Pre event) {
        Player player;
        LivingEntity livingAttacker;
        Entity entity;
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof IMagicEntity || livingEntity instanceof ServerPlayer) {
            MagicData playerMagicData = MagicData.getPlayerMagicData(livingEntity);
            if (livingEntity.hasEffect(MobEffectRegistry.HEARTSTOP)) {
                playerMagicData.getSyncedData().addHeartstopDamage(event.getOriginalDamage() * 0.5f);
                event.setNewDamage(0.0f);
            }
        }
        if (event.getSource().is(ISSDamageTypes.FIRE_MAGIC) && (entity = event.getSource().getEntity()) instanceof LivingEntity && (livingAttacker = (LivingEntity)entity).getItemBySlot(EquipmentSlot.CHEST).is(ItemRegistry.INFERNAL_SORCERER_CHESTPLATE) && (!(livingAttacker instanceof Player) || !(player = (Player)livingAttacker).getCooldowns().isOnCooldown((Item)ItemRegistry.INFERNAL_SORCERER_CHESTPLATE.get()))) {
            ImmolateEffect.addImmolateStack(livingEntity, (Entity)livingAttacker);
        }
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity newTarget = event.getNewAboutToBeSetTarget();
        LivingEntity entity = event.getEntity();
        if (newTarget != null) {
            IMagicSummon summon;
            if (newTarget.getType().is(ModTags.VILLAGE_ALLIES) && entity.getType().is(ModTags.VILLAGE_ALLIES)) {
                event.setCanceled(true);
                return;
            }
            if (newTarget instanceof IMagicSummon && (summon = (IMagicSummon)newTarget) instanceof Enemy && !entity.equals((Object)((Mob)newTarget).getTarget())) {
                event.setCanceled(true);
                return;
            }
            if (newTarget.hasEffect(MobEffectRegistry.TRUE_INVISIBILITY)) {
                event.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void preventDismount(EntityMountEvent event) {
        Entity mount = event.getEntityBeingMounted();
        Entity entity = event.getEntity();
        if (!entity.level.isClientSide && event.isDismounting() && mount instanceof PreventDismount) {
            PreventDismount preventDismount = (PreventDismount)mount;
            if (!(mount.isRemoved() || entity.isRemoved() || preventDismount.canEntityDismount(entity))) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        EntityHitResult entityHitResult;
        Entity victim;
        HitResult hitResult = event.getRayTraceResult();
        if (hitResult instanceof EntityHitResult && ((victim = (entityHitResult = (EntityHitResult)hitResult).getEntity()) instanceof IMagicEntity || victim instanceof Player)) {
            LivingEntity livingEntity = (LivingEntity)victim;
            if (livingEntity.hasEffect(MobEffectRegistry.EVASION)) {
                if (EvasionEffect.doEffect(livingEntity, victim.damageSources().indirectMagic((Entity)event.getProjectile(), event.getProjectile().getOwner()))) {
                    event.setCanceled(true);
                }
            } else if (livingEntity.hasEffect(MobEffectRegistry.ABYSSAL_SHROUD) && AbyssalShroudEffect.doEffect(livingEntity, victim.damageSources().indirectMagic((Entity)event.getProjectile(), event.getProjectile().getOwner()))) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void useOnEntityEvent(PlayerInteractEvent.EntityInteractSpecific event) {
        Entity entity = event.getTarget();
        if (entity instanceof Creeper) {
            InteractionHand hand;
            Creeper creeper = (Creeper)entity;
            Player player = event.getEntity();
            ItemStack useItem = player.getItemInHand(hand = event.getHand());
            if (useItem.is(Items.GLASS_BOTTLE) && creeper.isPowered()) {
                creeper.hurt(creeper.damageSources().generic(), 5.0f);
                player.level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0f, 1.0f);
                player.swing(hand);
                player.setItemInHand(hand, ItemUtils.createFilledResult((ItemStack)useItem, (Player)player, (ItemStack)new ItemStack((ItemLike)ItemRegistry.LIGHTNING_BOTTLE.get())));
                event.setCancellationResult(InteractionResultHolder.consume((Object)player.getItemInHand(hand)).getResult());
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void handleResistanceAttributesOnSpawn(FinalizeSpawnEvent event) {
        Mob mob = event.getEntity();
        if (mob.getType().is(EntityTypeTags.UNDEAD)) {
            ServerPlayerEvents.setIfNonNull((LivingEntity)mob, AttributeRegistry.HOLY_MAGIC_RESIST, 0.5);
            ServerPlayerEvents.setIfNonNull((LivingEntity)mob, AttributeRegistry.BLOOD_MAGIC_RESIST, 1.5);
        } else if (mob.getType().is(EntityTypeTags.SENSITIVE_TO_IMPALING)) {
            ServerPlayerEvents.setIfNonNull((LivingEntity)mob, AttributeRegistry.LIGHTNING_MAGIC_RESIST, 0.5);
        }
        if (mob.fireImmune()) {
            ServerPlayerEvents.setIfNonNull((LivingEntity)mob, AttributeRegistry.FIRE_MAGIC_RESIST, 1.5);
        }
        if (mob.getType() == EntityType.BLAZE) {
            ServerPlayerEvents.setIfNonNull((LivingEntity)mob, AttributeRegistry.ICE_MAGIC_RESIST, 0.5);
        }
    }

    private static void setIfNonNull(LivingEntity mob, Holder<Attribute> attribute, double value) {
        AttributeInstance instance = mob.getAttributes().getInstance(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event) {
        BlockPos pos;
        BlockState blockState;
        Entity entity = event.getEntity();
        Level level = entity.level;
        if (!level.isClientSide && entity.tickCount % 40 == 0 && (blockState = entity.level.getBlockState(pos = entity.blockPosition())).is(Blocks.CAULDRON)) {
            BloodCauldronBlock.attemptCookEntity(blockState, entity.level, pos, entity, () -> {
                level.setBlockAndUpdate(pos, ((Block)BlockRegistry.BLOOD_CAULDRON_BLOCK.get()).defaultBlockState());
                level.gameEvent(null, (Holder)GameEvent.BLOCK_CHANGE, pos);
            });
        }
    }

    @SubscribeEvent
    public static void registerPatrolSpawners(ModifyCustomSpawnersEvent event) {
        if (event.getLevel().dimension().equals(Level.OVERWORLD)) {
            event.addCustomSpawner((CustomSpawner)new IceSpiderPatrolSpawner());
        }
    }

    @SubscribeEvent
    public static void onAnvilRecipe(AnvilUpdateEvent event) {
        ItemStack result;
        if (event.getRight().is((Item)ItemRegistry.SHRIVING_STONE.get()) && !(result = Utils.handleShriving(event.getLeft())).isEmpty()) {
            String itemName = event.getName();
            if (itemName != null && !StringUtil.isBlank((String)itemName)) {
                if (!itemName.equals(result.getHoverName().getString())) {
                    result.set(DataComponents.CUSTOM_NAME, (Object)Component.literal((String)itemName));
                }
            } else if (result.has(DataComponents.CUSTOM_NAME)) {
                result.remove(DataComponents.CUSTOM_NAME);
            }
            event.setOutput(result);
            event.setCost(1L);
            event.setMaterialCost(1);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (((Boolean)ServerConfigs.PORTAL_FRAME_RESTRICT_BREAKING.get()).booleanValue() && event.getState().is(BlockRegistry.PORTAL_FRAME)) {
            PortalFrameBlockEntity portalFrameBlockEntity;
            Player player = event.getPlayer();
            BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
            if (blockEntity instanceof PortalFrameBlockEntity && (portalFrameBlockEntity = (PortalFrameBlockEntity)blockEntity).getOwnerUUID() != null && !player.getUUID().equals(portalFrameBlockEntity.getOwnerUUID())) {
                if (player instanceof ServerPlayer) {
                    ServerPlayer serverPlayer = (ServerPlayer)player;
                    serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"ui.irons_spellbooks.portal_break_failure").withStyle(ChatFormatting.RED)));
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void preventBlockPlacement(PlayerInteractEvent.RightClickBlock event) {
        BlockItem blockItem;
        Item item;
        Level level;
        Level level2 = event.getLevel();
        if (level2 instanceof Level && (level = level2).dimension().equals(PocketDimensionManager.POCKET_DIMENSION) && (item = event.getItemStack().getItem()) instanceof BlockItem && (blockItem = (BlockItem)item).getBlock().builtInRegistryHolder().is(ModTags.PREVENT_POCKET_DIMENSION_PLACEMENT)) {
            event.setCanceled(true);
            Player player = event.getEntity();
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                serverPlayer.displayClientMessage((Component)Component.translatable((String)"ui.irons_spellbooks.error_place_block_dimension").withStyle(ChatFormatting.RED), true);
            }
        }
    }

    @SubscribeEvent
    public static void preventPocketDimensionTeleportation(EntityTeleportEvent event) {
        ServerLevel serverLevel;
        Level level = event.getEntity().level;
        if (level instanceof ServerLevel && (serverLevel = (ServerLevel)level).dimension().equals(PocketDimensionManager.POCKET_DIMENSION) && !(event instanceof EntityTeleportEvent.TeleportCommand)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void changeDigSpeed(PlayerEvent.BreakSpeed event) {
        int i;
        Player player = event.getEntity();
        if (player.hasEffect(MobEffectRegistry.HASTENED)) {
            i = 1 + player.getEffect(MobEffectRegistry.HASTENED).getAmplifier();
            event.setNewSpeed(event.getNewSpeed() * Utils.intPow(1.2f, i));
        }
        if (player.hasEffect(MobEffectRegistry.SLOWED)) {
            i = 1 + player.getEffect(MobEffectRegistry.SLOWED).getAmplifier();
            event.setNewSpeed(event.getNewSpeed() * Utils.intPow(0.8f, i));
        }
    }

    @SubscribeEvent
    public static void changeBreedOutcome(BabyEntitySpawnEvent event) {
        AgeableMob ageableMob;
        if (((Boolean)ServerConfigs.HOGLIN_OFFSPRING_PROTECTION.get()).booleanValue() && (ageableMob = event.getChild()) instanceof Hoglin) {
            Hoglin baby = (Hoglin)ageableMob;
            ageableMob = event.getParentA();
            if (ageableMob instanceof Hoglin) {
                Hoglin parent1 = (Hoglin)ageableMob;
                ageableMob = event.getParentB();
                if (ageableMob instanceof Hoglin) {
                    Hoglin parent2 = (Hoglin)ageableMob;
                    double i = (parent1.isImmuneToZombification() ? 0.5 : 0.0) + (parent2.isImmuneToZombification() ? 0.5 : 0.0);
                    if ((double)Utils.random.nextFloat() < i) {
                        baby.setImmuneToZombification(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onChangeDimensions(EntityTravelToDimensionEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level;
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        Entity owner = SummonManager.getOwner(entity);
        if (owner != null) {
            event.setCanceled(true);
            return;
        }
        Set<UUID> summons = SummonManager.getSummons(entity);
        if (!summons.isEmpty()) {
            for (UUID uuid : summons) {
                Entity summon = serverLevel.getEntity(uuid);
                if (summon instanceof IMagicSummon) {
                    IMagicSummon magicSummon = (IMagicSummon)summon;
                    magicSummon.onUnSummon();
                    continue;
                }
                if (summon == null) continue;
                SummonManager.removeSummon(summon);
            }
        }
    }

    @SubscribeEvent
    public static void onDataLoaded(OnDatapackSyncEvent event) {
        Map map = CauldronInteraction.WATER.map();
        for (DeferredHolder<Item, ? extends Item> item : ItemRegistry.getIronsItems()) {
            if (!item.is(ItemTags.DYEABLE)) continue;
            map.put((Item)item.get(), CauldronInteraction.DYED_ITEM);
        }
    }
}

