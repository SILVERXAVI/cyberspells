/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal
 *  net.minecraft.world.entity.npc.VillagerTrades$ItemListing
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.trading.ItemCost
 *  net.minecraft.world.item.trading.MerchantOffer
 *  net.minecraft.world.item.trading.MerchantOffers
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.cryomancer;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.FocusOnTradingPlayerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.player.AdditionalWanderingTrades;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class CryomancerEntity
extends NeutralWizard
implements IMerchantWizard {
    @javax.annotation.Nullable
    private Player tradingPlayer;
    @javax.annotation.Nullable
    protected MerchantOffers offers;
    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    private long lastRestockCheckDayTime;
    private static final List<VillagerTrades.ItemListing> fillerOffers = List.of();

    public CryomancerEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 25;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FocusOnTradingPlayerGoal<CryomancerEntity>(this));
        this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(2, (Goal)new SpellBarrageGoal(this, SpellRegistry.ICE_BLOCK_SPELL.get(), 3, 6, 100, 250, 1));
        this.goalSelector.addGoal(3, (Goal)new WizardAttackGoal(this, 1.25, 50, 75).setSpells(List.of(SpellRegistry.ICICLE_SPELL.get(), SpellRegistry.ICICLE_SPELL.get(), SpellRegistry.ICICLE_SPELL.get(), SpellRegistry.CONE_OF_COLD_SPELL.get()), List.of(SpellRegistry.COUNTERSPELL_SPELL.get()), List.of(SpellRegistry.FROST_STEP_SPELL.get()), List.of()).setSingleUseSpell(SpellRegistry.SUMMON_POLAR_BEAR_SPELL.get(), 80, 400, 3, 6).setDrinksPotions());
        this.goalSelector.addGoal(4, (Goal)new PatrolNearLocationGoal(this, 30.0f, 0.75));
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.goalSelector.addGoal(10, (Goal)new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
        this.targetSelector.addGoal(5, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)ItemRegistry.CRYOMANCER_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)ItemRegistry.CRYOMANCER_CHESTPLATE.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0f);
        this.setDropChance(EquipmentSlot.CHEST, 0.0f);
    }

    public boolean canFreeze() {
        return false;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 24.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        boolean preventTrade;
        boolean bl = preventTrade = this.isAggressive() || !this.level.isClientSide && this.getOffers().isEmpty();
        if (!preventTrade) {
            if (!this.level.isClientSide && !this.getOffers().isEmpty()) {
                if (this.shouldRestock()) {
                    this.restock();
                }
                this.startTrading(pPlayer);
            }
            return InteractionResult.sidedSuccess((boolean)this.level.isClientSide);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    private void startTrading(Player pPlayer) {
        this.setTradingPlayer(pPlayer);
        this.openTradingScreen(pPlayer, this.getDisplayName(), 0);
    }

    @Override
    public int getRestocksToday() {
        return this.numberOfRestocksToday;
    }

    @Override
    public void setRestocksToday(int restocks) {
        this.numberOfRestocksToday = restocks;
    }

    @Override
    public long getLastRestockGameTime() {
        return this.lastRestockGameTime;
    }

    @Override
    public void setLastRestockGameTime(long time) {
        this.lastRestockGameTime = time;
    }

    @Override
    public long getLastRestockCheckDayTime() {
        return this.lastRestockCheckDayTime;
    }

    @Override
    public void setLastRestockCheckDayTime(long time) {
        this.lastRestockCheckDayTime = time;
    }

    @Override
    public Level level() {
        return this.level;
    }

    public void setTradingPlayer(@Nullable Player pTradingPlayer) {
        this.tradingPlayer = pTradingPlayer;
    }

    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.offers.addAll(this.createRandomOffers(2, 3));
            if (this.random.nextFloat() < 0.25f) {
                this.offers.add((Object)new AdditionalWanderingTrades.InkBuyTrade((InkItem)((Object)ItemRegistry.INK_COMMON.get())).getOffer((Entity)this, this.random));
            }
            if (this.random.nextFloat() < 0.25f) {
                this.offers.add((Object)new AdditionalWanderingTrades.InkBuyTrade((InkItem)((Object)ItemRegistry.INK_UNCOMMON.get())).getOffer((Entity)this, this.random));
            }
            if (this.random.nextFloat() < 0.25f) {
                this.offers.add((Object)new AdditionalWanderingTrades.InkBuyTrade((InkItem)((Object)ItemRegistry.INK_RARE.get())).getOffer((Entity)this, this.random));
            }
            this.offers.add((Object)new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.ICE.get()), 0.0f, 0.25f).getOffer((Entity)this, this.random));
            if (this.random.nextFloat() < 0.8f) {
                this.offers.add((Object)new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.ICE.get()), 0.3f, 0.7f).getOffer((Entity)this, this.random));
            }
            if (this.random.nextFloat() < 0.8f) {
                this.offers.add((Object)new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.ICE.get()), 0.8f, 1.0f).getOffer((Entity)this, this.random));
            }
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 32), Optional.empty(), this.random.nextBoolean() ? FurledMapItem.of(IronsSpellbooks.id("impaled_icebreaker"), Component.translatable((String)"item.irons_spellbooks.failed_arctic_voyage_map")) : FurledMapItem.of(IronsSpellbooks.id("ice_spider_den"), Component.translatable((String)"item.irons_spellbooks.ice_spider_den_map")), 0, 1, 5, 10.0f));
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)ItemRegistry.FIRE_ALE.get(), 4), Optional.empty(), ((Item)ItemRegistry.MUSIC_DISC_WHISPERS_OF_ICE.get()).getDefaultInstance(), 0, 1, 5, 0.1f));
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)ItemRegistry.ICY_FANG.get(), 2), Optional.empty(), ((Item)ItemRegistry.ICE_RUNE.get()).getDefaultInstance(), 0, 1, 5, 0.1f));
            this.offers.removeIf(Objects::isNull);
            this.setLastRestockGameTime(this.level.getGameTime());
        }
        return this.offers;
    }

    private Collection<MerchantOffer> createRandomOffers(int min, int max) {
        ArrayList<MerchantOffer> offers = new ArrayList<MerchantOffer>();
        return offers;
    }

    public void overrideOffers(MerchantOffers pOffers) {
    }

    public void notifyTrade(MerchantOffer pOffer) {
        pOffer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
    }

    public void notifyTradeUpdated(ItemStack pStack) {
        if (!this.level.isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
            this.playSound(this.getTradeUpdatedSound(!pStack.isEmpty()), this.getSoundVolume(), this.getVoicePitch());
        }
    }

    protected SoundEvent getTradeUpdatedSound(boolean pIsYesSound) {
        return pIsYesSound ? (SoundEvent)SoundRegistry.TRADER_YES.get() : (SoundEvent)SoundRegistry.TRADER_NO.get();
    }

    public SoundEvent getNotifyTradeSound() {
        return (SoundEvent)SoundRegistry.TRADER_YES.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.serializeMerchant(pCompound, this.offers, this.lastRestockGameTime, this.numberOfRestocksToday);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.deserializeMerchant(pCompound, c -> {
            this.offers = c;
        });
    }
}

