/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LightningBolt
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class EnergizedCoreItem
extends Item {
    public EnergizedCoreItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        if (pContext.getPlayer() != null && level.getBlockState(blockPos).is(Blocks.LIGHTNING_ROD)) {
            if (level.isThundering()) {
                if (!level.isClientSide && level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState())) {
                    ItemStack itemstack = pContext.getItemInHand();
                    if (!pContext.getPlayer().getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    pContext.getPlayer().setItemInHand(pContext.getHand(), itemstack);
                    Vec3 center = new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5);
                    this.doLightningBolt(level, center);
                    Level.ExplosionInteraction blockinteraction = (Boolean)ServerConfigs.SPELL_GREIFING.get() != false ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
                    MagicManager.spawnParticles(level, ParticleHelper.ELECTRIC_SPARKS, center.x, center.y + 0.5, center.z, 50, 0.1f, 0.1f, 0.1f, 0.6, false);
                    MagicManager.spawnParticles(level, new BlastwaveParticleOptions(new Vector3f(0.7f, 1.0f, 1.0f), 6.0f), center.x, center.y + 0.15, center.z, 1, 0.0, 0.0, 0.0, 0.0, true);
                    level.explode(null, level.damageSources().lightningBolt(), null, center.x, center.y, center.z, 3.0f, true, blockinteraction);
                    level.playSound(null, blockPos, (SoundEvent)SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 2.0f, 0.6f);
                    ItemEntity itementity = new ItemEntity(level, center.x, center.y + 1.0, center.z, new ItemStack((ItemLike)ItemRegistry.LIGHTNING_ROD_STAFF.get()));
                    itementity.setGlowingTag(true);
                    level.addFreshEntity((Entity)itementity);
                }
            } else if (level.isClientSide) {
                pContext.getPlayer().displayClientMessage((Component)Component.translatable((String)"item.irons_spellbooks.energized_core.failure").withStyle(ChatFormatting.AQUA), true);
            }
            return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        return super.useOn(pContext);
    }

    private void doLightningBolt(Level level, Vec3 pos) {
        LightningBolt lightningBolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.setVisualOnly(true);
        lightningBolt.setDamage(0.0f);
        lightningBolt.setPos(pos);
        level.addFreshEntity((Entity)lightningBolt);
    }
}

