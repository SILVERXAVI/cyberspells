/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.wall_of_fire.WallOfFireEntity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WallOfFireSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"wall_of_fire");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.FIRE_RESOURCE).setMaxLevel(5).setCooldownSeconds(30.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.aoe_damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.distance", (Object[])new Object[]{Utils.stringTruncation(this.getWallLength(spellLevel, caster), 1)}));
    }

    public WallOfFireSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 30;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new FireWallData(this, 0.0f);
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 3;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getPlayerRecasts().hasRecastForSpell(this)) {
            RecastInstance recast = playerMagicData.getPlayerRecasts().getRecastInstance(this.getSpellId());
            FireWallData fireWallData = (FireWallData)recast.getCastData();
            this.addAnchor(fireWallData, world, entity, recast);
        } else {
            FireWallData fireWallData = new FireWallData(this, this.getWallLength(spellLevel, entity));
            RecastInstance recast = new RecastInstance(this.getSpellId(), spellLevel, this.getRecastCount(spellLevel, entity), 40, castSource, fireWallData);
            this.addAnchor(fireWallData, world, entity, recast);
            playerMagicData.getPlayerRecasts().addRecast(recast, playerMagicData);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onRecastFinished(ServerPlayer entity, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (!recastResult.isFailure()) {
            Level level = entity.level;
            FireWallData fireWallData = (FireWallData)recastInstance.getCastData();
            if (fireWallData.anchorPoints.size() == 1) {
                this.addAnchor(fireWallData, level, (LivingEntity)entity, recastInstance);
            }
            if (fireWallData.anchorPoints.size() > 0) {
                WallOfFireEntity fireWall = new WallOfFireEntity(level, (Entity)entity, fireWallData.anchorPoints, this.getDamage(recastInstance.getSpellLevel(), (LivingEntity)entity));
                Vec3 origin = fireWallData.anchorPoints.get(0);
                for (int i = 1; i < fireWallData.anchorPoints.size(); ++i) {
                    origin.add(fireWallData.anchorPoints.get(i));
                }
                origin.scale((double)(1.0f / (float)fireWallData.anchorPoints.size()));
                fireWall.setPos(origin);
                level.addFreshEntity((Entity)fireWall);
            }
        }
        super.onRecastFinished(entity, recastInstance, recastResult, castDataSerializable);
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFireTicks(80);
    }

    private float getWallLength(int spellLevel, LivingEntity entity) {
        return 10.0f + (float)(spellLevel * 3) * this.getEntityPowerMultiplier(entity);
    }

    private float getDamage(int spellLevel, LivingEntity sourceEntity) {
        return this.getSpellPower(spellLevel, (Entity)sourceEntity);
    }

    public void addAnchor(FireWallData fireWallData, Level level, LivingEntity entity, RecastInstance recastInstance) {
        Vec3 anchor = Utils.getTargetBlock(level, entity, ClipContext.Fluid.ANY, 20.0).getLocation();
        anchor = this.setOnGround(anchor, level);
        List<Vec3> anchorPoints = fireWallData.anchorPoints;
        if (anchorPoints.size() == 0) {
            anchorPoints.add(anchor);
        } else {
            float maxDistance;
            int i = anchorPoints.size();
            float distance = (float)anchorPoints.get(i - 1).distanceTo(anchor);
            if (distance <= (maxDistance = fireWallData.maxTotalDistance - fireWallData.accumulatedDistance)) {
                fireWallData.accumulatedDistance += distance;
                anchorPoints.add(anchor);
            } else {
                anchor = anchorPoints.get(i - 1).add(anchor.subtract(anchorPoints.get(i - 1)).normalize().scale((double)maxDistance));
                anchor = this.setOnGround(anchor, level);
                anchorPoints.add(anchor);
                if (entity instanceof ServerPlayer) {
                    ServerPlayer serverPlayer = (ServerPlayer)entity;
                    if (recastInstance.getRemainingRecasts() > 0) {
                        MagicData.getPlayerMagicData((LivingEntity)serverPlayer).getPlayerRecasts().removeRecast(recastInstance, RecastResult.USED_ALL_RECASTS);
                    }
                }
            }
        }
        MagicManager.spawnParticles(level, (ParticleOptions)ParticleTypes.FLAME, anchor.x, anchor.y + 1.5, anchor.z, 5, 0.05, 0.25, 0.05, 0.0, true);
    }

    private Vec3 setOnGround(Vec3 in, Level level) {
        if (level.getBlockState(BlockPos.containing((double)in.x, (double)(in.y + 0.5), (double)in.z)).isAir()) {
            for (int i = 0; i < 15; ++i) {
                if (level.getBlockState(BlockPos.containing((double)in.x, (double)(in.y - (double)i), (double)in.z)).isAir()) continue;
                return new Vec3(in.x, in.y - (double)i + 1.0, in.z);
            }
            return new Vec3(in.x, in.y - 15.0, in.z);
        }
        double y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, (int)in.x, (int)in.z);
        return new Vec3(in.x, y, in.z);
    }

    public class FireWallData
    implements ICastDataSerializable {
        private Entity castingEntity;
        public List<Vec3> anchorPoints = new ArrayList<Vec3>();
        public float maxTotalDistance;
        public float accumulatedDistance;
        public int ticks;

        FireWallData(WallOfFireSpell this$0, float maxTotalDistance) {
            this.maxTotalDistance = maxTotalDistance;
        }

        @Override
        public void reset() {
        }

        @Override
        public void writeToBuffer(FriendlyByteBuf buffer) {
            buffer.writeInt(this.anchorPoints.size());
            for (Vec3 vec : this.anchorPoints) {
                buffer.writeFloat((float)vec.x);
                buffer.writeFloat((float)vec.y);
                buffer.writeFloat((float)vec.z);
            }
        }

        @Override
        public void readFromBuffer(FriendlyByteBuf buffer) {
            this.anchorPoints = new ArrayList<Vec3>();
            int length = buffer.readInt();
            for (int i = 0; i < length; ++i) {
                this.anchorPoints.add(new Vec3((double)buffer.readFloat(), (double)buffer.readFloat(), (double)buffer.readFloat()));
            }
        }

        public CompoundTag serializeNBT(HolderLookup.Provider provider) {
            CompoundTag compoundTag = new CompoundTag();
            ListTag anchors = new ListTag();
            for (Vec3 vec : this.anchorPoints) {
                CompoundTag anchor = new CompoundTag();
                anchor.putFloat("x", (float)vec.x);
                anchor.putFloat("y", (float)vec.y);
                anchor.putFloat("z", (float)vec.z);
                anchors.add((Object)anchor);
            }
            compoundTag.put("Anchors", (Tag)anchors);
            return compoundTag;
        }

        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
            this.anchorPoints = new ArrayList<Vec3>();
            if (nbt.contains("Anchors", 9)) {
                ListTag anchors = (ListTag)nbt.get("Anchors");
                for (Tag tag : anchors) {
                    if (!(tag instanceof CompoundTag)) continue;
                    CompoundTag anchor = (CompoundTag)tag;
                    this.anchorPoints.add(new Vec3(anchor.getDouble("x"), anchor.getDouble("y"), anchor.getDouble("z")));
                }
            }
        }
    }
}

