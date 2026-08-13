/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.lightning;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MultiTargetEntityCastData;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.thunderstep.ThunderstepProjectile;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ThunderStepSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"thunder_step");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(5).setCooldownSeconds(8.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getSpellPower(spellLevel, (Entity)caster), 1)}));
    }

    public ThunderStepSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 75;
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
        return new MultiTargetEntityCastData(new Entity[0]);
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!playerMagicData.getPlayerRecasts().hasRecastForSpell(this)) {
            ThunderstepProjectile orb = new ThunderstepProjectile(level, (Entity)entity);
            orb.shoot(entity.getLookAngle());
            orb.moveTo(entity.getEyePosition());
            level.addFreshEntity((Entity)orb);
            RecastInstance recast = new RecastInstance(this.getSpellId(), spellLevel, 2, 100, castSource, new MultiTargetEntityCastData(new Entity[]{orb}));
            playerMagicData.getPlayerRecasts().addRecast(recast, playerMagicData);
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onRecastFinished(ServerPlayer entity, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        MultiTargetEntityCastData targetData;
        super.onRecastFinished(entity, recastInstance, recastResult, castDataSerializable);
        ServerLevel serverlevel = entity.serverLevel();
        if (castDataSerializable instanceof MultiTargetEntityCastData && !(targetData = (MultiTargetEntityCastData)castDataSerializable).getTargets().isEmpty()) {
            Entity orb = serverlevel.getEntity((UUID)targetData.getTargets().getFirst());
            if (orb == null) {
                return;
            }
            if (!recastResult.isFailure()) {
                Vec3 dest = TeleportSpell.solveTeleportDestination((Level)serverlevel, (LivingEntity)entity, orb.blockPosition(), orb.position());
                Vec3 travel = dest.subtract(entity.position());
                if (travel.lengthSqr() < 1024.0) {
                    this.zapEntitiesBetween((LivingEntity)entity, recastInstance.getSpellLevel(), dest);
                    for (int i = 0; i < 7; ++i) {
                        Vec3 random1 = Utils.getRandomVec3(0.5).multiply((double)entity.getBbWidth(), (double)entity.getBbHeight(), (double)entity.getBbWidth());
                        Vec3 random2 = Utils.getRandomVec3(0.8f).multiply((double)entity.getBbWidth(), (double)entity.getBbHeight(), (double)entity.getBbWidth());
                        float yOffset = (float)i / 7.0f * entity.getBbHeight();
                        Vec3 midpoint = entity.position().add(travel.scale(0.5)).add(random2);
                        serverlevel.sendParticles((ParticleOptions)new ZapParticleOption(random1.add(entity.getX(), entity.getY() + (double)yOffset, entity.getZ())), midpoint.x, midpoint.y, midpoint.z, 1, 0.0, 0.0, 0.0, 0.0);
                        serverlevel.sendParticles((ParticleOptions)new ZapParticleOption(random1.scale(-1.0).add(dest.x, dest.y + (double)yOffset, dest.z)), midpoint.x, midpoint.y, midpoint.z, 1, 0.0, 0.0, 0.0, 0.0);
                    }
                }
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }
                Utils.handleSpellTeleport(this, (Entity)entity, dest);
                entity.resetFallDistance();
            }
            orb.discard();
        }
    }

    private void zapEntitiesBetween(LivingEntity caster, int spellLevel, Vec3 blockEnd) {
        Vec3 start = caster.getEyePosition();
        Vec3 end = blockEnd.add(0.0, (double)caster.getEyeHeight(), 0.0);
        AABB range = caster.getBoundingBox().expandTowards(end.subtract(start));
        List entities = caster.level.getEntities((Entity)caster, range);
        for (Entity target : entities) {
            Vec3 height = new Vec3(0.0, (double)caster.getEyeHeight(), 0.0);
            if (Utils.checkEntityIntersecting(target, start, end, 1.0f).getType() == HitResult.Type.MISS && Utils.checkEntityIntersecting(target, start.subtract(height), end.subtract(height), 1.0f).getType() == HitResult.Type.MISS) continue;
            DamageSources.applyDamage(target, this.getDamage(spellLevel, caster), this.getDamageSource((Entity)caster));
        }
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return this.getSpellPower(spellLevel, (Entity)sourceEntity);
    }

    private float getDamage(int spellLevel, LivingEntity sourceEntity) {
        return this.getSpellPower(spellLevel, (Entity)sourceEntity);
    }
}

