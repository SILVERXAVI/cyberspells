/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3f
 */
package io.redspace.ironsspellbooks.spells.holy;

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
import io.redspace.ironsspellbooks.entity.spells.HealingAoe;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class HealingCircleSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"healing_circle");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.HOLY_RESOURCE).setMaxLevel(10).setCooldownSeconds(25.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.aoe_healing", (Object[])new Object[]{Utils.stringTruncation(this.getHealing(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.radius", (Object[])new Object[]{Utils.stringTruncation(this.getRadius(spellLevel, caster), 1)}), Component.translatable((String)"ui.irons_spellbooks.duration", (Object[])new Object[]{Utils.timeFromTicks(this.getDuration(spellLevel, caster), 1)}));
    }

    public HealingCircleSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 1;
        this.castTime = 20;
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
        return Optional.of((SoundEvent)SoundRegistry.CLOUD_OF_REGEN_LOOP.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.15f, false);
        return true;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TargetEntityCastData castTargetingData;
        LivingEntity target;
        Vec3 spawn = null;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetEntityCastData && (target = (castTargetingData = (TargetEntityCastData)iCastData).getTarget((ServerLevel)world)) != null) {
            spawn = target.position();
        }
        if (spawn == null) {
            spawn = Utils.raycastForEntity(world, (Entity)entity, 32.0f, true, 0.15f).getLocation();
            spawn = Utils.moveToRelativeGroundLevel(world, spawn, 6);
        }
        int duration = this.getDuration(spellLevel, entity);
        float radius = this.getRadius(spellLevel, entity);
        HealingAoe aoeEntity = new HealingAoe(world);
        aoeEntity.setOwner((Entity)entity);
        aoeEntity.setCircular();
        aoeEntity.setRadius(radius);
        aoeEntity.setDuration(duration);
        aoeEntity.setDamage(this.getHealing(spellLevel, entity));
        aoeEntity.setPos(spawn);
        world.addFreshEntity((Entity)aoeEntity);
        TargetedAreaEntity visualEntity = TargetedAreaEntity.createTargetAreaEntity(world, spawn, radius, 0xC80000);
        visualEntity.setDuration(duration);
        visualEntity.setOwner((Entity)aoeEntity);
        visualEntity.setShouldFade(true);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getHealing(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, (Entity)caster) * 0.25f;
    }

    private float getRadius(int spellLevel, LivingEntity caster) {
        return 5.0f;
    }

    private int getDuration(int spellLevel, LivingEntity caster) {
        return 200;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_CONTINUOUS_OVERHEAD;
    }

    @Override
    public Vector3f getTargetingColor() {
        return new Vector3f(0.85f, 0.0f, 0.0f);
    }
}

