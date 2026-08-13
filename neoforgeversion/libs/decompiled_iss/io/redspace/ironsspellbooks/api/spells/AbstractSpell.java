/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.AtomicDouble
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.GameType
 *  net.minecraft.world.level.Level
 *  net.neoforged.bus.api.Event
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.joml.Vector3f
 *  top.theillusivec4.curios.api.CuriosApi
 */
package io.redspace.ironsspellbooks.api.spells;

import com.google.common.util.concurrent.AtomicDouble;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.network.casting.OnCastFinishedPacket;
import io.redspace.ironsspellbooks.network.casting.OnCastStartedPacket;
import io.redspace.ironsspellbooks.network.casting.OnClientCastPacket;
import io.redspace.ironsspellbooks.network.casting.UpdateCastingStatePacket;
import io.redspace.ironsspellbooks.player.ClientInputEvents;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.spells.NoneSpell;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.CuriosApi;

public abstract class AbstractSpell {
    public static final Style ELDRITCH_OBFUSCATED_STYLE = Style.EMPTY.withObfuscated(Boolean.valueOf(true)).withFont(ResourceLocation.withDefaultNamespace((String)"alt"));
    private String spellID = null;
    private String deathMessageId = null;
    private String spellName = null;
    protected int baseManaCost;
    protected int manaCostPerLevel;
    protected int baseSpellPower;
    protected int spellPowerPerLevel;
    protected int castTime;
    private volatile List<Double> rarityWeights;
    private final int maxRarity = SpellRarity.LEGENDARY.getValue();

    public final String getSpellName() {
        if (this.spellName == null) {
            ResourceLocation resourceLocation = Objects.requireNonNull(this.getSpellResource());
            this.spellName = resourceLocation.getPath().intern();
        }
        return this.spellName;
    }

    public final String getSpellId() {
        if (this.spellID == null) {
            ResourceLocation resourceLocation = Objects.requireNonNull(this.getSpellResource());
            this.spellID = resourceLocation.toString().intern();
        }
        return this.spellID;
    }

    public final ResourceLocation getSpellIconResource() {
        return ResourceLocation.fromNamespaceAndPath((String)this.getSpellResource().getNamespace(), (String)("textures/gui/spell_icons/" + this.getSpellName() + ".png"));
    }

    @Deprecated(forRemoval=true)
    public int getMinRarity() {
        return SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.MIN_RARITY).getValue();
    }

    public int getMaxLevel() {
        return SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.MAX_LEVEL);
    }

    public int getMinLevel() {
        return 1;
    }

    public MutableComponent getDisplayName(Player player) {
        boolean obfuscateName = player != null && this.obfuscateStats(player);
        return Component.translatable((String)this.getComponentId()).withStyle(obfuscateName ? ELDRITCH_OBFUSCATED_STYLE : Style.EMPTY);
    }

    public String getComponentId() {
        return String.format("spell.%s.%s", this.getSpellResource().getNamespace(), this.getSpellName());
    }

    public Component getLockedMessage() {
        return Component.translatable((String)"ui.irons_spellbooks.unlearned_error");
    }

    public abstract ResourceLocation getSpellResource();

    public abstract DefaultConfig getDefaultConfig();

    public abstract CastType getCastType();

    public SchoolType getSchoolType() {
        return SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.SCHOOL);
    }

    public Vector3f getTargetingColor() {
        return this.getSchoolType().getTargetingColor();
    }

    public final int getLevelFor(int level, @Nullable LivingEntity caster) {
        int addition = 0;
        if (caster != null) {
            addition = CuriosApi.getCuriosInventory((LivingEntity)caster).map(inv -> inv.findCurios(AffinityData::hasAffinityData).stream().mapToInt(slot -> AffinityData.getAffinityData(slot.stack()).getBonusFor(this)).sum()).orElse(0);
        }
        ModifySpellLevelEvent levelEvent = new ModifySpellLevelEvent(this, caster, level, level + addition);
        NeoForge.EVENT_BUS.post((Event)levelEvent);
        return levelEvent.getLevel();
    }

    public int getManaCost(int level) {
        return (int)((double)(this.baseManaCost + this.manaCostPerLevel * (level - 1)) * SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.MANA_MULTIPLIER));
    }

    public int getSpellCooldown() {
        return (int)(SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.COOLDOWN_IN_SECONDS) * 20.0);
    }

    public int getCastTime(int spellLevel) {
        if (this.getCastType() == CastType.INSTANT) {
            return 0;
        }
        return this.castTime;
    }

    public ICastDataSerializable getEmptyCastData() {
        return null;
    }

    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(this.defaultCastSound());
    }

    public AnimationHolder getCastStartAnimation() {
        return switch (this.getCastType()) {
            case CastType.INSTANT -> SpellAnimations.ANIMATION_INSTANT_CAST;
            case CastType.CONTINUOUS -> SpellAnimations.ANIMATION_CONTINUOUS_CAST;
            case CastType.LONG -> SpellAnimations.ANIMATION_LONG_CAST;
            default -> AnimationHolder.none();
        };
    }

    public AnimationHolder getCastFinishAnimation() {
        return switch (this.getCastType()) {
            case CastType.LONG -> SpellAnimations.ANIMATION_LONG_CAST_FINISH;
            case CastType.INSTANT -> AnimationHolder.pass();
            default -> AnimationHolder.none();
        };
    }

    public float getSpellPower(int spellLevel, @Nullable Entity sourceEntity) {
        double entitySpellPowerModifier = 1.0;
        double entitySchoolPowerModifier = 1.0;
        float configPowerModifier = SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.POWER_MULTIPLIER).floatValue();
        if (sourceEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)sourceEntity;
            entitySpellPowerModifier = (float)livingEntity.getAttributeValue(AttributeRegistry.SPELL_POWER);
            entitySchoolPowerModifier = this.getSchoolType().getPowerFor(livingEntity);
        }
        return (float)((double)(this.baseSpellPower + this.spellPowerPerLevel * (spellLevel - 1)) * entitySpellPowerModifier * entitySchoolPowerModifier * (double)configPowerModifier);
    }

    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 0;
    }

    public float getEntityPowerMultiplier(@Nullable LivingEntity entity) {
        float base = SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.POWER_MULTIPLIER).floatValue();
        if (entity == null) {
            return base;
        }
        float entitySpellPowerModifier = (float)entity.getAttributeValue(AttributeRegistry.SPELL_POWER);
        double entitySchoolPowerModifier = this.getSchoolType().getPowerFor(entity);
        return (float)((double)(base * entitySpellPowerModifier) * entitySchoolPowerModifier);
    }

    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        double entityCastTimeModifier = 1.0;
        if (entity != null) {
            entityCastTimeModifier = this.getCastType() != CastType.CONTINUOUS ? 2.0 - Utils.softCapFormula(entity.getAttributeValue(AttributeRegistry.CAST_TIME_REDUCTION)) : entity.getAttributeValue(AttributeRegistry.CAST_TIME_REDUCTION);
        }
        return Math.round((float)this.getCastTime(spellLevel) * (float)entityCastTimeModifier);
    }

    public boolean attemptInitiateCast(ItemStack stack, int spellLevel, Level level, Player player, CastSource castSource, boolean triggerCooldown, String castingEquipmentSlot) {
        if (level.isClientSide) {
            return false;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        MagicData playerMagicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
        if (!playerMagicData.isCasting()) {
            CastResult castResult = this.canBeCastedBy(spellLevel, castSource, playerMagicData, (Player)serverPlayer);
            if (castResult.message != null) {
                serverPlayer.connection.send((Packet)new ClientboundSetActionBarTextPacket(castResult.message));
            }
            if (!castResult.isSuccess() || !this.checkPreCastConditions(level, spellLevel, (LivingEntity)serverPlayer, playerMagicData) || ((SpellPreCastEvent)NeoForge.EVENT_BUS.post((Event)new SpellPreCastEvent(player, this.getSpellId(), spellLevel, this.getSchoolType(), castSource))).isCanceled()) {
                return false;
            }
            if (serverPlayer.isUsingItem()) {
                serverPlayer.stopUsingItem();
            }
            int effectiveCastTime = this.getEffectiveCastTime(spellLevel, (LivingEntity)player);
            playerMagicData.initiateCast(this, spellLevel, effectiveCastTime, castSource, castingEquipmentSlot);
            playerMagicData.setPlayerCastingItem(stack);
            this.onServerPreCast(player.level, spellLevel, (LivingEntity)player, playerMagicData);
            PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new UpdateCastingStatePacket(this.getSpellId(), spellLevel, effectiveCastTime, castSource, castingEquipmentSlot), (CustomPacketPayload[])new CustomPacketPayload[0]);
            PacketDistributor.sendToPlayersTrackingEntityAndSelf((Entity)serverPlayer, (CustomPacketPayload)new OnCastStartedPacket(serverPlayer.getUUID(), this.getSpellId(), spellLevel), (CustomPacketPayload[])new CustomPacketPayload[0]);
            return true;
        }
        Utils.serverSideCancelCast(serverPlayer);
        return false;
    }

    public void castSpell(Level world, int spellLevel, ServerPlayer serverPlayer, CastSource castSource, boolean triggerCooldown) {
        MagicData magicData = MagicData.getPlayerMagicData((LivingEntity)serverPlayer);
        PlayerRecasts playerRecasts = magicData.getPlayerRecasts();
        boolean playerAlreadyHasRecast = playerRecasts.hasRecastForSpell(this.getSpellId());
        SpellOnCastEvent event = new SpellOnCastEvent((Player)serverPlayer, this.getSpellId(), spellLevel, this.getManaCost(spellLevel), this.getSchoolType(), castSource);
        NeoForge.EVENT_BUS.post((Event)event);
        if (castSource.consumesMana() && !playerAlreadyHasRecast && (!serverPlayer.isCreative() || ((Boolean)ServerConfigs.CREATIVE_MANA_COST.get()).booleanValue())) {
            float newMana = Math.max(magicData.getMana() - (float)event.getManaCost(), 0.0f);
            magicData.setMana(newMana);
            PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new SyncManaPacket(magicData), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
        this.onCast(world, event.getSpellLevel(), (LivingEntity)serverPlayer, castSource, magicData);
        boolean playerHasRecastsLeft = playerRecasts.hasRecastForSpell(this.getSpellId());
        if (playerAlreadyHasRecast && playerHasRecastsLeft) {
            playerRecasts.decrementRecastCount(this.getSpellId());
        } else if (!playerHasRecastsLeft && triggerCooldown && (!serverPlayer.isCreative() || ((Boolean)ServerConfigs.CREATIVE_COOLDOWN.get()).booleanValue())) {
            MagicHelper.MAGIC_MANAGER.addCooldown(serverPlayer, this, castSource);
        }
        PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)new OnClientCastPacket(this.getSpellId(), spellLevel, castSource, magicData.getAdditionalCastData()), (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        MagicHelper.MAGIC_MANAGER.addCooldown(serverPlayer, this, recastInstance.getCastSource());
    }

    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        this.playSound(this.getCastFinishSound(), (Entity)entity);
    }

    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        this.playSound(this.getCastFinishSound(), (Entity)entity);
    }

    public CastResult canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player) {
        float playerMana;
        if (((Boolean)ServerConfigs.DISABLE_ADVENTURE_MODE_CASTING.get()).booleanValue() && player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            if (serverPlayer.gameMode.getGameModeForPlayer() == GameType.ADVENTURE) {
                return new CastResult(CastResult.Type.FAILURE, (Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_adventure").withStyle(ChatFormatting.RED));
            }
        }
        boolean hasEnoughMana = (playerMana = playerMagicData.getMana()) - (float)this.getManaCost(spellLevel) >= 0.0f;
        boolean isSpellOnCooldown = playerMagicData.getPlayerCooldowns().isOnCooldown(this);
        boolean hasRecastForSpell = playerMagicData.getPlayerRecasts().hasRecastForSpell(this.getSpellId());
        if (this.requiresLearning() && !this.isLearned(player)) {
            return new CastResult(CastResult.Type.FAILURE, (Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_unlearned").withStyle(ChatFormatting.RED));
        }
        if (castSource == CastSource.SCROLL && this.getRecastCount(spellLevel, (LivingEntity)player) > 0) {
            return new CastResult(CastResult.Type.FAILURE, (Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_scroll", (Object[])new Object[]{this.getDisplayName(player)}).withStyle(ChatFormatting.RED));
        }
        if (!(castSource != CastSource.SPELLBOOK && castSource != CastSource.SWORD || !isSpellOnCooldown || player.isCreative() && !((Boolean)ServerConfigs.CREATIVE_COOLDOWN.get()).booleanValue())) {
            return new CastResult(CastResult.Type.FAILURE, (Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_cooldown", (Object[])new Object[]{this.getDisplayName(player)}).withStyle(ChatFormatting.RED));
        }
        if (!(hasRecastForSpell || !castSource.consumesMana() || hasEnoughMana || player.isCreative() && !((Boolean)ServerConfigs.CREATIVE_MANA_COST.get()).booleanValue())) {
            return new CastResult(CastResult.Type.FAILURE, (Component)Component.translatable((String)"ui.irons_spellbooks.cast_error_mana", (Object[])new Object[]{this.getDisplayName(player)}).withStyle(ChatFormatting.RED));
        }
        return new CastResult(CastResult.Type.SUCCESS);
    }

    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return true;
    }

    public void playSound(Optional<SoundEvent> sound, Entity entity) {
        sound.ifPresent(soundEvent -> entity.playSound(soundEvent, 2.0f, 0.9f + Utils.random.nextFloat() * 0.2f));
    }

    private SoundEvent defaultCastSound() {
        return this.getSchoolType().getCastSound();
    }

    public void onServerCastComplete(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
        playerMagicData.resetCastingState();
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            PacketDistributor.sendToPlayersTrackingEntityAndSelf((Entity)serverPlayer, (CustomPacketPayload)new OnCastFinishedPacket(serverPlayer.getUUID(), this.getSpellId(), cancelled), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
    }

    public void onClientPreCast(Level level, int spellLevel, LivingEntity entity, InteractionHand hand, @Nullable MagicData playerMagicData) {
        if (this.getCastType().immediatelySuppressRightClicks() && ClientInputEvents.isUseKeyDown) {
            ClientSpellCastHelper.setSuppressRightClicks(true);
        }
        this.playSound(this.getCastStartSound(), (Entity)entity);
    }

    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        this.playSound(this.getCastStartSound(), (Entity)entity);
    }

    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
    }

    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return false;
    }

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AbstractSpell) {
            AbstractSpell other = (AbstractSpell)obj;
            return this.getSpellResource().equals((Object)other.getSpellResource());
        }
        return false;
    }

    public int hashCode() {
        return this.getSpellResource().hashCode();
    }

    public void resetRarityWeights() {
        this.rarityWeights = null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void initializeRarityWeights() {
        NoneSpell noneSpell = SpellRegistry.none();
        synchronized (noneSpell) {
            if (this.rarityWeights == null) {
                int minRarity = this.getMinRarity();
                int maxRarity = this.getMaxRarity();
                List<Double> rarityRawConfig = SpellRarity.getRawRarityConfig();
                List<Double> rarityConfig = SpellRarity.getRarityConfig();
                if (minRarity != 0) {
                    List<Double> subList = rarityRawConfig.subList(minRarity, maxRarity + 1);
                    double subtotal = subList.stream().reduce(0.0, Double::sum);
                    List<Double> rarityRawWeights = subList.stream().map(item -> item / subtotal * (1.0 - subtotal) + item).toList();
                    AtomicDouble counter = new AtomicDouble();
                    this.rarityWeights = new ArrayList<Double>();
                    rarityRawWeights.forEach(item -> this.rarityWeights.add(counter.addAndGet(item.doubleValue())));
                } else {
                    this.rarityWeights = rarityConfig;
                }
            }
        }
    }

    public SpellRarity getRarity(int level) {
        if (this.rarityWeights == null) {
            this.initializeRarityWeights();
        }
        int maxLevel = this.getMaxLevel();
        int maxRarity = this.getMaxRarity();
        if (maxLevel == 1) {
            return SpellRarity.values()[this.getMinRarity()];
        }
        if (level >= maxLevel) {
            return SpellRarity.LEGENDARY;
        }
        double percentOfMaxLevel = (double)level / (double)maxLevel;
        int lookupOffset = maxRarity + 1 - this.rarityWeights.size();
        for (int i = 0; i < this.rarityWeights.size(); ++i) {
            if (!(percentOfMaxLevel <= this.rarityWeights.get(i))) continue;
            return SpellRarity.values()[i + lookupOffset];
        }
        return SpellRarity.COMMON;
    }

    public String getDeathMessageId() {
        if (this.deathMessageId == null) {
            this.deathMessageId = this.getSpellId().replace(':', '.');
        }
        return this.deathMessageId;
    }

    public final SpellDamageSource getDamageSource(Entity attacker) {
        return this.getDamageSource(attacker, attacker);
    }

    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return SpellDamageSource.source(projectile, attacker, this);
    }

    public boolean isEnabled() {
        return SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.ENABLED);
    }

    public int getMaxRarity() {
        return this.maxRarity;
    }

    public int getMinLevelForRarity(SpellRarity rarity) {
        if (this.rarityWeights == null) {
            this.initializeRarityWeights();
        }
        int minRarity = this.getMinRarity();
        int maxLevel = this.getMaxLevel();
        if (rarity.getValue() < minRarity) {
            return 0;
        }
        if (rarity.getValue() == minRarity) {
            return 1;
        }
        return (int)(this.rarityWeights.get(rarity.getValue() - (1 + minRarity)) * (double)maxLevel) + 1;
    }

    public boolean allowLooting() {
        return this.getSchoolType().allowLooting;
    }

    public boolean canBeCraftedBy(Player player) {
        return !this.requiresLearning() || this.isLearned(player);
    }

    public boolean allowCrafting() {
        return SpellConfigManager.getSpellConfigValue(this, SpellConfigParameter.ALLOW_CRAFTING);
    }

    public boolean obfuscateStats(@Nullable Player player) {
        return this.requiresLearning() && !this.isLearned(player);
    }

    public boolean isLearned(@Nullable Player player) {
        if (player == null) {
            return false;
        }
        if (player.level.isClientSide) {
            return ClientMagicData.getSyncedSpellData((LivingEntity)player).isSpellLearned(this);
        }
        return MagicData.getPlayerMagicData((LivingEntity)player).getSyncedData().isSpellLearned(this);
    }

    public boolean requiresLearning() {
        return this.getSchoolType().requiresLearning;
    }

    public boolean canBeInterrupted(@Nullable Player player) {
        return this.getCastType() == CastType.LONG && !ItemRegistry.CONCENTRATION_AMULET.get().isEquippedBy((LivingEntity)player);
    }

    public boolean stopSoundOnCancel() {
        return false;
    }
}

