/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells.evocation;

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
import io.redspace.ironsspellbooks.entity.spells.creeper_head.CreeperHeadProjectile;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class ChainCreeperSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"chain_creeper");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(6).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getSpellPower(spellLevel, (Entity)caster), 1)}), Component.translatable((String)"ui.irons_spellbooks.projectile_count", (Object[])new Object[]{this.getCount(spellLevel, caster)}));
    }

    public ChainCreeperSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 0;
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
        return Optional.of(SoundEvents.CREEPER_PRIMED);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.EVOKER_CAST_SPELL);
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, this, 48, 0.25f, false);
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 spawn = null;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetEntityCastData) {
            TargetEntityCastData castTargetingData = (TargetEntityCastData)iCastData;
            spawn = castTargetingData.getTargetPosition((ServerLevel)level);
        }
        if (spawn == null) {
            HitResult raycast = Utils.raycastForEntity(level, (Entity)entity, 32.0f, true);
            spawn = raycast.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)raycast).getEntity().position() : Utils.moveToRelativeGroundLevel(level, raycast.getLocation().subtract(entity.getForward().normalize()).add(0.0, 2.0, 0.0), 5);
        }
        ChainCreeperSpell.summonCreeperRing(level, entity, spawn.add(0.0, 0.5, 0.0), this.getDamage(spellLevel, entity), this.getCount(spellLevel, entity));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public static void summonCreeperRing(Level level, LivingEntity owner, Vec3 origin, float damage, int count) {
        if (count < 3) {
            count = 3;
        }
        int degreesPerCreeper = 360 / count;
        for (int i = 0; i < count; ++i) {
            Vec3 motion = new Vec3(0.0, 0.0, 0.4 + (double)((float)count * 0.015f));
            motion = motion.xRot(1.3089969f);
            motion = motion.yRot((float)(degreesPerCreeper * i) * ((float)Math.PI / 180));
            CreeperHeadProjectile head = new CreeperHeadProjectile(owner, level, motion, damage);
            head.setChainOnKill(true);
            head.setChainCount(count - 2);
            Vec3 spawn = origin.add(motion.multiply(1.0, 0.0, 1.0).normalize().scale((double)0.6f));
            Vec2 angle = Utils.rotationFromDirection(motion);
            head.moveTo(spawn.x, spawn.y - head.getBoundingBox().getYsize() / 2.0, spawn.z, angle.y, angle.x);
            level.addFreshEntity((Entity)head);
        }
    }

    private int getCount(int spellLevel, LivingEntity entity) {
        return 3 + spellLevel - 1;
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, (Entity)entity);
    }
}

