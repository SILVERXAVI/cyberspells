/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.network.particles.TeleportParticlesPacket;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.network.PacketDistributor;

public class TeleportSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"teleport");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(5).setCooldownSeconds(3.0).build();

    public TeleportSpell() {
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 10;
        this.baseManaCost = 20;
        this.manaCostPerLevel = 5;
        this.castTime = 0;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.ENDERMAN_TELEPORT);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 potentialTarget;
        TeleportData teleportData = (TeleportData)playerMagicData.getAdditionalCastData();
        Vec3 dest = null;
        if (teleportData != null && (potentialTarget = teleportData.getTeleportTargetPosition()) != null) {
            dest = potentialTarget;
        }
        if (dest == null) {
            dest = TeleportSpell.findTeleportLocation(level, entity, this.getDistance(spellLevel, entity));
        }
        PacketDistributor.sendToPlayersTrackingEntityAndSelf((Entity)entity, (CustomPacketPayload)new TeleportParticlesPacket(entity.position(), dest), (CustomPacketPayload[])new CustomPacketPayload[0]);
        if (entity.isPassenger()) {
            entity.stopRiding();
        }
        Utils.handleSpellTeleport(this, (Entity)entity, dest);
        entity.resetFallDistance();
        playerMagicData.resetAdditionalCastData();
        entity.playSound(this.getCastFinishSound().get(), 2.0f, 1.0f);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public static Vec3 findTeleportLocation(Level level, LivingEntity entity, float maxDistance) {
        BlockHitResult blockHitResult = Utils.getTargetBlock(level, entity, ClipContext.Fluid.NONE, maxDistance);
        return TeleportSpell.solveTeleportDestination(level, entity, blockHitResult.getBlockPos(), blockHitResult.getLocation());
    }

    public static Vec3 solveTeleportDestination(Level level, LivingEntity entity, BlockPos blockPos, Vec3 vec3) {
        boolean los;
        BlockPos pos = blockPos;
        Vec3 bbOffset = entity.getForward().normalize().multiply((double)(entity.getBbWidth() / 3.0f), 0.0, (double)(entity.getBbHeight() / 3.0f));
        Vec3 bbImpact = vec3.subtract(bbOffset);
        double ledgeY = level.clip((ClipContext)new ClipContext((Vec3)Vec3.atBottomCenterOf((Vec3i)pos).add((double)0.0, (double)3.0, (double)0.0), (Vec3)Vec3.atBottomCenterOf((Vec3i)pos), (ClipContext.Block)ClipContext.Block.COLLIDER, (ClipContext.Fluid)ClipContext.Fluid.NONE, (CollisionContext)CollisionContext.empty())).getLocation().y;
        boolean isAir = level.getBlockState(new BlockPos(new Vec3i(pos.getX(), (int)ledgeY, pos.getZ())).above()).isAir();
        boolean bl = los = level.clip(new ClipContext(bbImpact, bbImpact.add(0.0, ledgeY - (double)pos.getY(), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)entity)).getType() == HitResult.Type.MISS;
        if (isAir && los && Math.abs(ledgeY - (double)pos.getY()) <= 3.0) {
            return new Vec3((double)pos.getX() + 0.5, ledgeY + 0.001, (double)pos.getZ() + 0.5);
        }
        return level.clip(new ClipContext(bbImpact, bbImpact.add(0.0, (double)(-entity.getBbHeight()), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)entity)).getLocation().add(0.0, 0.001, 0.0);
    }

    public static void particleCloud(Level level, Vec3 pos) {
        if (level.isClientSide) {
            double width = 0.5;
            float height = 1.0f;
            for (int i = 0; i < 55; ++i) {
                double x = pos.x + Utils.random.nextDouble() * width * 2.0 - width;
                double y = pos.y + (double)height + Utils.random.nextDouble() * (double)height * 1.2 * 2.0 - (double)height * 1.2;
                double z = pos.z + Utils.random.nextDouble() * width * 2.0 - width;
                double dx = Utils.random.nextDouble() * 0.1 * (double)(Utils.random.nextBoolean() ? 1 : -1);
                double dy = Utils.random.nextDouble() * 0.1 * (double)(Utils.random.nextBoolean() ? 1 : -1);
                double dz = Utils.random.nextDouble() * 0.1 * (double)(Utils.random.nextBoolean() ? 1 : -1);
                level.addParticle((ParticleOptions)ParticleTypes.PORTAL, true, x, y, z, dx, dy, dz);
            }
        }
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return (float)(Utils.softCapFormula(this.getEntityPowerMultiplier(sourceEntity)) * (double)this.getSpellPower(spellLevel, null));
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{Utils.stringTruncation(this.getDistance(spellLevel, caster), 1)}));
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return AnimationHolder.none();
    }

    public static class TeleportData
    implements ICastData {
        private Vec3 teleportTargetPosition;

        public TeleportData(Vec3 teleportTargetPosition) {
            this.teleportTargetPosition = teleportTargetPosition;
        }

        public void setTeleportTargetPosition(Vec3 targetPosition) {
            this.teleportTargetPosition = targetPosition;
        }

        public Vec3 getTeleportTargetPosition() {
            return this.teleportTargetPosition;
        }

        @Override
        public void reset() {
        }
    }
}

