/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class StaffOfTheNines
extends Item {
    public StaffOfTheNines(Item.Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        if (!level.isClientSide) {
            Vec3 pos = this.getPosition(player.getEyePosition(), 0.425f, 0.275f, 0.1775f, player.getRotationVector());
            MagicManager.spawnParticles(level, (ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y, pos.z, 5, 0.1, 0.1, 0.1, 0.01, false);
            level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 4.0f, 1.5f);
            level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST_FAR, SoundSource.PLAYERS, 6.0f, 1.5f);
            HitResult hit = Utils.raycastForEntity(level, (Entity)player, 64.0f, true, 0.1f);
            if (hit instanceof BlockHitResult) {
                BlockHitResult blockHitResult = (BlockHitResult)hit;
                Vec3 loc = blockHitResult.getLocation();
                MagicManager.spawnParticles(level, (ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(blockHitResult.getBlockPos())), loc.x, loc.y, loc.z, 25, 0.1, 0.1, 0.1, 0.25, true);
            } else if (hit instanceof EntityHitResult) {
                EntityHitResult entityHitResult = (EntityHitResult)hit;
                entityHitResult.getEntity().hurt(level.damageSources().magic(), (float)(10.0 * player.getAttributeValue(AttributeRegistry.SPELL_POWER)));
                Vec3 loc = entityHitResult.getLocation();
                MagicManager.spawnParticles(level, ParticleHelper.BLOOD, loc.x, loc.y, loc.z, 25, 0.1, 0.1, 0.1, 0.25, true);
            }
            CameraShakeManager.addCameraShake(new CameraShakeData(level, 10, player.position(), 5.0f));
            ((ServerPlayer)player).teleportTo((ServerLevel)level, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot() - (float)Utils.random.nextIntBetweenInclusive(6, 9));
        }
        return super.use(level, player, pUsedHand);
    }

    public Vec3 getPosition(Vec3 vec3, float forwards, float up, float left, Vec2 vec2) {
        float f = Mth.cos((float)((vec2.y + 90.0f) * ((float)Math.PI / 180)));
        float f1 = Mth.sin((float)((vec2.y + 90.0f) * ((float)Math.PI / 180)));
        float f2 = Mth.cos((float)(-vec2.x * ((float)Math.PI / 180)));
        float f3 = Mth.sin((float)(-vec2.x * ((float)Math.PI / 180)));
        float f4 = Mth.cos((float)((-vec2.x + 90.0f) * ((float)Math.PI / 180)));
        float f5 = Mth.sin((float)((-vec2.x + 90.0f) * ((float)Math.PI / 180)));
        Vec3 vec31 = new Vec3((double)(f * f2), (double)f3, (double)(f1 * f2));
        Vec3 vec32 = new Vec3((double)(f * f4), (double)f5, (double)(f1 * f4));
        Vec3 vec33 = vec31.cross(vec32).scale(-1.0);
        double d0 = vec31.x * (double)forwards + vec32.x * (double)up + vec33.x * (double)left;
        double d1 = vec31.y * (double)forwards + vec32.y * (double)up + vec33.y * (double)left;
        double d2 = vec31.z * (double)forwards + vec32.z * (double)up + vec33.z * (double)left;
        return new Vec3(vec3.x + d0, vec3.y + d1, vec3.z + d2);
    }
}

