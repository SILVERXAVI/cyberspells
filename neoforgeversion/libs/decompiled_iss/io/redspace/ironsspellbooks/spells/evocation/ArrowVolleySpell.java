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
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
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
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.ArrowVolleyEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ArrowVolleySpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"arrow_volley");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(6).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 1)}), Component.translatable((String)"ui.irons_spellbooks.projectile_count", (Object[])new Object[]{this.getCount(spellLevel, caster)}));
    }

    public ArrowVolleySpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 8;
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
        return Optional.of((SoundEvent)SoundRegistry.ARROW_VOLLEY_PREPARE.get());
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
        Vec3 targetLocation = null;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetEntityCastData) {
            TargetEntityCastData castTargetingData = (TargetEntityCastData)iCastData;
            targetLocation = castTargetingData.getTargetPosition((ServerLevel)level);
        }
        if (targetLocation == null) {
            targetLocation = Utils.raycastForEntity(level, (Entity)entity, 100.0f, true).getLocation();
        }
        Vec3 backward = new Vec3(targetLocation.x - entity.getX(), 0.0, targetLocation.z - entity.getZ()).normalize().scale(-4.0);
        Vec3 raycastTarget = Utils.moveToRelativeGroundLevel(level, targetLocation.add(0.0, 2.0, 0.0), 4).add(backward).add(0.0, 6.0, 0.0);
        Vec3 spawnLocation = Utils.raycastForBlock(level, targetLocation, raycastTarget, ClipContext.Fluid.NONE).getLocation();
        spawnLocation = spawnLocation.subtract(targetLocation).scale((double)0.9f).add(targetLocation);
        float dx = Mth.sqrt((float)((float)((spawnLocation.x - targetLocation.x) * (spawnLocation.x - targetLocation.x) + (spawnLocation.z - targetLocation.z) * (spawnLocation.z - targetLocation.z))));
        float arrowAngleX = dx == 0.0f ? 70.0f : (float)(Mth.atan2((double)dx, (double)(spawnLocation.y - targetLocation.y)) * 57.2957763671875);
        float arrowAngleY = entity.getX() == targetLocation.x && entity.getZ() == targetLocation.z ? (entity.getYRot() - 90.0f) * ((float)Math.PI / 180) : Utils.getAngle(entity.getX(), entity.getZ(), targetLocation.x, targetLocation.z);
        ArrowVolleyEntity arrowVolleyEntity = new ArrowVolleyEntity((EntityType<? extends Projectile>)((EntityType)EntityRegistry.ARROW_VOLLEY_ENTITY.get()), level);
        arrowVolleyEntity.moveTo(spawnLocation);
        arrowVolleyEntity.setYRot(arrowAngleY * 57.295776f + 90.0f);
        arrowVolleyEntity.setXRot(arrowAngleX + 25.0f);
        arrowVolleyEntity.setDamage(this.getDamage(spellLevel, entity));
        arrowVolleyEntity.setArrowsPerRow(this.getArrowsPerRow(spellLevel, entity));
        arrowVolleyEntity.setRows(this.getRows(spellLevel, entity));
        arrowVolleyEntity.setOwner((Entity)entity);
        level.addFreshEntity((Entity)arrowVolleyEntity);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getCount(int spellLevel, LivingEntity entity) {
        return this.getRows(spellLevel, entity) * this.getArrowsPerRow(spellLevel, entity);
    }

    private int getRows(int spellLevel, LivingEntity entity) {
        return 4 + spellLevel;
    }

    private int getArrowsPerRow(int spellLevel, LivingEntity entity) {
        return 5 + spellLevel / 2;
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, (Entity)entity) * 0.25f;
    }

    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setIFrames(0);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }
}

