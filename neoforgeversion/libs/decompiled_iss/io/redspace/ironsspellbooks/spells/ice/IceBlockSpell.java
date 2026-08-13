/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.spells.ice;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.ice_block.IceBlockProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class IceBlockSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"ice_block");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.ICE_RESOURCE).setMaxLevel(6).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getSpellPower(spellLevel, (Entity)caster), 1)}));
    }

    public IceBlockSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 14;
        this.spellPowerPerLevel = 2;
        this.castTime = 30;
        this.baseManaCost = 40;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of((SoundEvent)SoundRegistry.ICE_BLOCK_CAST.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, this, 48, 0.35f, false);
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TargetEntityCastData castTargetingData;
        Vec3 spawn = null;
        LivingEntity target = null;
        int spawnheight = 4;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetEntityCastData && (target = (castTargetingData = (TargetEntityCastData)iCastData).getTarget((ServerLevel)level)) != null) {
            spawn = target.position();
            spawnheight += (int)(target.getBbHeight() * 0.5f);
        }
        if (spawn == null) {
            HitResult raycast = Utils.raycastForEntity(level, (Entity)entity, 32.0f, true, 0.25f);
            if (raycast.getType() == HitResult.Type.ENTITY) {
                spawn = ((EntityHitResult)raycast).getEntity().position();
                Entity entity2 = ((EntityHitResult)raycast).getEntity();
                if (entity2 instanceof LivingEntity) {
                    LivingEntity livingEntity;
                    target = livingEntity = (LivingEntity)entity2;
                }
            } else {
                spawn = raycast.getLocation().subtract(entity.getForward().normalize());
            }
        }
        IceBlockProjectile iceBlock = new IceBlockProjectile(level, entity, target);
        iceBlock.moveTo(this.raiseWithCollision(spawn, spawnheight, level));
        if (!level.noBlockCollision((Entity)iceBlock, iceBlock.getBoundingBox())) {
            iceBlock.noPhysics = true;
        }
        iceBlock.setAirTime(target == null ? 25 : 35);
        iceBlock.setDamage(this.getDamage(spellLevel, entity));
        level.addFreshEntity((Entity)iceBlock);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private Vec3 raiseWithCollision(Vec3 start, int blocks, Level level) {
        Vec3 raised;
        for (int i = 0; i < blocks && level.getBlockState(BlockPos.containing((Position)(raised = start.add(0.0, 1.0, 0.0)))).isAir(); ++i) {
            start = raised;
        }
        return start;
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFreezeTicks(100).setIFrames(0);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, (Entity)entity);
    }
}

