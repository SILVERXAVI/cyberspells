/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.common.NeoForge
 *  org.jetbrains.annotations.Nullable
 *  software.bernie.geckolib.animatable.GeoAnimatable
 */
package io.redspace.ironsspellbooks.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonedEntitiesCastData;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class RaiseDeadSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"raise_dead");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.BLOOD_RESOURCE).setMaxLevel(6).setCooldownSeconds(150.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.summon_count", (Object[])new Object[]{this.getSummonCount(spellLevel, caster)}));
    }

    public RaiseDeadSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 30;
        this.baseManaCost = 50;
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
        return Optional.of((SoundEvent)SoundRegistry.RAISE_DEAD_START.value());
    }

    public int getSummonCount(int spellLevel, LivingEntity caster) {
        return spellLevel + 2;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable)) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PlayerRecasts recasts = playerMagicData.getPlayerRecasts();
        if (!recasts.hasRecastForSpell(this)) {
            SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();
            int summonTime = 12000;
            int count = this.getSummonCount(spellLevel, entity);
            float radius = 1.5f + 0.185f * (float)count;
            for (int i = 0; i < count; ++i) {
                boolean isSkeleton = Utils.random.nextDouble() < 0.3;
                ItemStack[] equipment = this.getEquipment(this.getSpellPower(spellLevel, (Entity)entity), Utils.random);
                GeoAnimatable undead = isSkeleton ? new SummonedSkeleton(world, entity, true) : new SummonedZombie(world, entity, true);
                undead.finalizeSpawn((ServerLevelAccessor)((ServerLevel)world), world.getCurrentDifficultyAt(undead.getOnPos()), MobSpawnType.MOB_SUMMONED, null);
                this.equip((Mob)undead, equipment);
                float yrot = 6.281f / (float)count * (float)i + entity.getYRot() * ((float)Math.PI / 180);
                Vec3 spawn = Utils.moveToRelativeGroundLevel(world, entity.getEyePosition().add(new Vec3((double)(radius * Mth.cos((float)yrot)), 0.0, (double)(radius * Mth.sin((float)yrot)))), 10);
                undead.setPos(spawn.x, spawn.y, spawn.z);
                undead.setYRot(entity.getYRot());
                undead.setOldPosAndRot();
                Monster creature = (Monster)((SpellSummonEvent)NeoForge.EVENT_BUS.post(new SpellSummonEvent<SummonedSkeleton>(entity, (SummonedSkeleton)undead, this.spellId, spellLevel))).getCreature();
                world.addFreshEntity((Entity)creature);
                SummonManager.initSummon((Entity)entity, (Entity)creature, summonTime, summonedEntitiesCastData);
            }
            RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, this.getRecastCount(spellLevel, entity), summonTime, castSource, summonedEntitiesCastData);
            recasts.addRecast(recastInstance, playerMagicData);
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), (SoundEvent)SoundRegistry.RAISE_DEAD_FINISH.get(), entity.getSoundSource(), 2.0f, 0.9f + Utils.random.nextFloat() * 0.2f);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private void equip(Mob mob, ItemStack[] equipment) {
        mob.setItemSlot(EquipmentSlot.FEET, equipment[0]);
        mob.setItemSlot(EquipmentSlot.LEGS, equipment[1]);
        mob.setItemSlot(EquipmentSlot.CHEST, equipment[2]);
        mob.setItemSlot(EquipmentSlot.HEAD, equipment[3]);
        mob.setDropChance(EquipmentSlot.FEET, 0.0f);
        mob.setDropChance(EquipmentSlot.LEGS, 0.0f);
        mob.setDropChance(EquipmentSlot.CHEST, 0.0f);
        mob.setDropChance(EquipmentSlot.HEAD, 0.0f);
    }

    private ItemStack[] getEquipment(float power, RandomSource random) {
        Item[] leather = new Item[]{Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET};
        Item[] chain = new Item[]{Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET};
        Item[] iron = new Item[]{Items.IRON_BOOTS, Items.IRON_LEGGINGS, Items.IRON_CHESTPLATE, Items.IRON_HELMET};
        int minQuality = 12;
        int maxQuality = this.getMaxLevel() * this.spellPowerPerLevel + 15;
        ItemStack[] result = new ItemStack[4];
        for (int i = 0; i < 4; ++i) {
            float quality = Mth.clamp((float)((power + (float)random.nextIntBetweenInclusive(-3, 8) - (float)minQuality) / (float)(maxQuality - minQuality)), (float)0.0f, (float)0.95f);
            if (random.nextDouble() < (double)(quality * quality)) {
                if ((double)quality > 0.85) {
                    result[i] = new ItemStack((ItemLike)iron[i]);
                    continue;
                }
                if ((double)quality > 0.65) {
                    result[i] = new ItemStack((ItemLike)chain[i]);
                    continue;
                }
                if ((double)quality > 0.15) {
                    result[i] = new ItemStack((ItemLike)leather[i]);
                    continue;
                }
                result[i] = ItemStack.EMPTY;
                continue;
            }
            result[i] = ItemStack.EMPTY;
        }
        return result;
    }
}

