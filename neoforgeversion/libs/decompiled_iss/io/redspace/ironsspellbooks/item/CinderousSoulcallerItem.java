/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ItemParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.ai.village.poi.PoiManager
 *  net.minecraft.world.entity.ai.village.poi.PoiManager$Occupancy
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.PoiTypeRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CinderousSoulcallerItem
extends Item {
    public CinderousSoulcallerItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel)level;
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                player.getCooldowns().addCooldown((Item)ItemRegistry.CINDEROUS_SOULCALLER.get(), 80);
                BlockPos playerBlockPos = player.blockPosition();
                PoiManager poimanager = serverlevel.getPoiManager();
                Optional keystone = poimanager.findClosest(poi -> Objects.equals(poi.getKey(), PoiTypeRegistry.CINDEROUS_KEYSTONE_POI.getKey()), playerBlockPos, 22, PoiManager.Occupancy.ANY);
                if (keystone.isPresent() && playerBlockPos.getY() + 2 >= ((BlockPos)keystone.get()).getY()) {
                    BlockPos keystonePos = (BlockPos)keystone.get();
                    AABB exclusiveRange = AABB.ofSize((Vec3)keystonePos.getCenter(), (double)80.0, (double)80.0, (double)80.0);
                    if (level.getEntitiesOfClass(FireBossEntity.class, exclusiveRange).isEmpty()) {
                        if (!player.getAbilities().instabuild) {
                            Vec3 particlePos = player.getEyePosition().add(player.getForward().scale(0.6)).subtract(0.0, 0.3, 0.0);
                            MagicManager.spawnParticles((Level)serverlevel, (ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, itemStack), particlePos.x, particlePos.y, particlePos.z, 9, 0.15, 0.15, 0.15, 0.08, false);
                            itemStack.shrink(1);
                            player.setItemInHand(hand, itemStack);
                        }
                        Vec3 center = keystonePos.getCenter().add(0.0, 0.6, 0.0);
                        float yRot = Utils.getAngle(center.x, center.z, player.getX(), player.getZ()) * 57.295776f;
                        FireBossEntity fireBoss = (FireBossEntity)((EntityType)EntityRegistry.FIRE_BOSS.get()).create((Level)serverlevel);
                        fireBoss.moveTo(center);
                        fireBoss.setYRot(yRot + 90.0f);
                        fireBoss.triggerSpawnAnim();
                        fireBoss.finalizeSpawn((ServerLevelAccessor)serverlevel, level.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
                        level.addFreshEntity((Entity)fireBoss);
                        this.tollEffects(serverlevel, player.position(), true);
                    } else {
                        serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"item.irons_spellbooks.cinderous_soulcaller.failure.in_progress").withStyle(ChatFormatting.GOLD)));
                        this.tollEffects(serverlevel, player.position(), false);
                    }
                } else {
                    serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket((Component)Component.translatable((String)"item.irons_spellbooks.cinderous_soulcaller.failure.no_soul").withStyle(ChatFormatting.GOLD)));
                    this.tollEffects(serverlevel, player.position(), false);
                }
            }
        }
        player.swing(hand);
        return InteractionResultHolder.consume((Object)itemStack);
    }

    public void tollEffects(ServerLevel serverLevel, Vec3 usePosition, boolean success) {
        serverLevel.playSound(null, usePosition.x, usePosition.y, usePosition.z, success ? SoundRegistry.SOULCALLER_TOLL_SUCCESS : SoundRegistry.SOULCALLER_TOLL_FAILURE, SoundSource.PLAYERS, 6.0f, 1.0f);
        MagicManager.spawnParticles((Level)serverLevel, new BlastwaveParticleOptions(1.0f, 0.6f, 0.3f, 16.0f), usePosition.x, usePosition.y, usePosition.z, 0, 0.0, 0.0, 0.0, 0.0, false);
    }
}

