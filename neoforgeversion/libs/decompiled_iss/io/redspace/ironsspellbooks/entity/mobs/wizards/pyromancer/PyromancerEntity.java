/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  it.unimi.dsi.fastutil.ints.IntList
 *  javax.annotation.Nullable
 *  net.minecraft.core.component.DataComponents
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
 *  net.minecraft.world.item.component.FireworkExplosion
 *  net.minecraft.world.item.component.FireworkExplosion$Shape
 *  net.minecraft.world.item.component.Fireworks
 *  net.minecraft.world.item.trading.ItemCost
 *  net.minecraft.world.item.trading.MerchantOffer
 *  net.minecraft.world.item.trading.MerchantOffers
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer;

import com.google.common.collect.Sets;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.FocusOnTradingPlayerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.player.AdditionalWanderingTrades;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class PyromancerEntity
extends NeutralWizard
implements IMerchantWizard {
    @javax.annotation.Nullable
    private Player tradingPlayer;
    @javax.annotation.Nullable
    protected MerchantOffers offers;
    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    private long lastRestockCheckDayTime;
    private static final List<VillagerTrades.ItemListing> fillerOffers = List.of(new AdditionalWanderingTrades.SimpleBuy(16, new ItemCost((ItemLike)Items.CANDLE, 4), 1, 1), new AdditionalWanderingTrades.SimpleSell(8, new ItemStack((ItemLike)Items.CANDLE, 4), 10, 14), new AdditionalWanderingTrades.SimpleSell(8, new ItemStack((ItemLike)Items.FIRE_CHARGE, 3), 9, 13), new AdditionalWanderingTrades.SimpleSell(12, new ItemStack((ItemLike)Items.LANTERN, 3), 6, 10), new AdditionalWanderingTrades.SimpleBuy(16, new ItemCost((ItemLike)Items.HONEY_BOTTLE, 2), 3, 5), new AdditionalWanderingTrades.SimpleBuy(16, new ItemCost((ItemLike)Items.BLAZE_ROD, 3), 4, 6), new AdditionalWanderingTrades.SimpleSell(5, PyromancerEntity.createFireworkStack(), 3, 4));

    public PyromancerEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 25;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FocusOnTradingPlayerGoal<PyromancerEntity>(this));
        this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(2, (Goal)new WizardAttackGoal(this, 1.25, 25, 50).setSpells(List.of(SpellRegistry.FIREBOLT_SPELL.get(), SpellRegistry.FIREBOLT_SPELL.get(), SpellRegistry.FIREBOLT_SPELL.get(), SpellRegistry.FIRE_BREATH_SPELL.get(), SpellRegistry.BLAZE_STORM_SPELL.get()), List.of(), List.of(SpellRegistry.BURNING_DASH_SPELL.get()), List.of()).setDrinksPotions().setSingleUseSpell(SpellRegistry.MAGMA_BOMB_SPELL.get(), 80, 200, 4, 6));
        this.goalSelector.addGoal(3, (Goal)new PatrolNearLocationGoal(this, 30.0f, 0.75));
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.goalSelector.addGoal(10, (Goal)new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isHostileTowards));
        this.targetSelector.addGoal(5, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)ItemRegistry.PYROMANCER_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)ItemRegistry.PYROMANCER_CHESTPLATE.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0f);
        this.setDropChance(EquipmentSlot.CHEST, 0.0f);
    }

    public boolean fireImmune() {
        return true;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 24.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public Optional<SoundEvent> getAngerSound() {
        return Optional.of((SoundEvent)SoundRegistry.TRADER_NO.get());
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
            this.offers.add((Object)new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.FIRE.get()), 0.0f, 0.25f).getOffer((Entity)this, this.random));
            if (this.random.nextFloat() < 0.8f) {
                this.offers.add((Object)new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.FIRE.get()), 0.3f, 0.7f).getOffer((Entity)this, this.random));
            }
            if (this.random.nextFloat() < 0.8f) {
                this.offers.add((Object)new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.FIRE.get()), 0.8f, 1.0f).getOffer((Entity)this, this.random));
            }
            this.offers.add((Object)new AdditionalWanderingTrades.SimpleSell(3, new ItemStack((ItemLike)ItemRegistry.FIRE_ALE.get()), 12, 16).getOffer((Entity)this, this.random));
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 24), Optional.empty(), FurledMapItem.of(IronsSpellbooks.id("mangrove_hut"), Component.translatable((String)"item.irons_spellbooks.alchemical_trade_route")), 0, 1, 5, 10.0f));
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)ItemRegistry.CHAINED_BOOK.get(), 4), Optional.empty(), ((Item)ItemRegistry.FIRE_RUNE.get()).getDefaultInstance(), 0, 1, 5, 0.1f));
            this.offers.removeIf(Objects::isNull);
            this.setLastRestockGameTime(this.level.getGameTime());
        }
        return this.offers;
    }

    private Collection<MerchantOffer> createRandomOffers(int min, int max) {
        HashSet set = Sets.newHashSet();
        int fillerTrades = this.random.nextIntBetweenInclusive(min, max);
        for (int i = 0; i < 10 && set.size() < fillerTrades; ++i) {
            set.add(this.random.nextInt(fillerOffers.size()));
        }
        ArrayList<MerchantOffer> offers = new ArrayList<MerchantOffer>();
        for (Integer integer : set) {
            offers.add(fillerOffers.get(integer).getOffer((Entity)this, this.random));
        }
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

    private static ItemStack createFireworkStack() {
        ItemStack rocket = new ItemStack((ItemLike)Items.FIREWORK_ROCKET, 5);
        rocket.set(DataComponents.FIREWORKS, (Object)new Fireworks(3, List.of(new FireworkExplosion(FireworkExplosion.Shape.BURST, IntList.of((int)11743535, (int)15435844, (int)14602026), IntList.of(), true, true))));
        return rocket;
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

