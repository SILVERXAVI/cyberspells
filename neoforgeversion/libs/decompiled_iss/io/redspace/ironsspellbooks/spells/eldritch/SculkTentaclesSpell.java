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
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells.eldritch;

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
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SculkTentaclesSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"sculk_tentacles");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE).setMaxLevel(4).setCooldownSeconds(30.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)}), Component.translatable((String)"ui.irons_spellbooks.radius", (Object[])new Object[]{Utils.stringTruncation((float)this.getRings(spellLevel, caster) * 1.3f, 1)}));
    }

    public SculkTentaclesSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 3;
        this.castTime = 20;
        this.baseManaCost = 150;
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
        return Optional.of((SoundEvent)SoundRegistry.VOID_TENTACLES_START.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.VOID_TENTACLES_FINISH.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.15f, false);
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Player player;
        TargetEntityCastData castTargetingData;
        LivingEntity target;
        int rings = this.getRings(spellLevel, entity);
        int count = 2;
        Vec3 center = null;
        ICastData iCastData = playerMagicData.getAdditionalCastData();
        if (iCastData instanceof TargetEntityCastData && (target = (castTargetingData = (TargetEntityCastData)iCastData).getTarget((ServerLevel)level)) != null) {
            center = target.position();
        }
        if (center == null) {
            center = Utils.raycastForEntity(level, (Entity)entity, 48.0f, true, 0.15f).getLocation();
            center = Utils.moveToRelativeGroundLevel(level, center, 6);
        }
        level.playSound(entity instanceof Player ? (player = (Player)entity) : null, center.x, center.y, center.z, (SoundEvent)SoundRegistry.VOID_TENTACLES_FINISH.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
        for (int r = 0; r < rings; ++r) {
            float tentacles = count + r * 2;
            int i = 0;
            while ((float)i < tentacles) {
                Vec3 random = new Vec3(Utils.getRandomScaled(1.0), Utils.getRandomScaled(1.0), Utils.getRandomScaled(1.0));
                Vec3 spawn = center.add(new Vec3(0.0, 0.0, 1.3 * (double)(r + 1)).yRot(6.281f / tentacles * (float)i)).add(random);
                if (!level.getBlockState(BlockPos.containing((Position)(spawn = Utils.moveToRelativeGroundLevel(level, spawn, 8))).below()).isAir()) {
                    VoidTentacle tentacle = new VoidTentacle(level, entity, this.getDamage(spellLevel, entity));
                    tentacle.moveTo(spawn);
                    tentacle.setYRot(Utils.random.nextInt(360));
                    level.addFreshEntity((Entity)tentacle);
                }
                ++i;
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return (float)this.baseSpellPower * this.getEntityPowerMultiplier(entity);
    }

    private int getRings(int spellLevel, LivingEntity entity) {
        return 1 + spellLevel;
    }
}

