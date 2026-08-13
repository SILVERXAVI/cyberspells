/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.effect.MobEffectInstance
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
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.entity.monster.piglin.AbstractPiglin
 *  net.minecraft.world.entity.monster.piglin.PiglinAi
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
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.alchemist;

import com.google.common.collect.Sets;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.AlchemistAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.FocusOnTradingPlayerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.player.AdditionalWanderingTrades;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ApothecaristEntity
extends NeutralWizard
implements IMerchantWizard {
    @javax.annotation.Nullable
    private Player tradingPlayer;
    @javax.annotation.Nullable
    protected MerchantOffers offers;
    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    private long lastRestockCheckDayTime;
    private static final List<MerchantOffer> fillerOffers = List.of(new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 4), Optional.empty(), new ItemStack((ItemLike)Items.MAGMA_CREAM, 1), 0, 8, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 6), Optional.empty(), new ItemStack((ItemLike)Items.HONEY_BOTTLE, 2), 0, 8, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 10), Optional.empty(), new ItemStack((ItemLike)Items.NETHER_WART, 5), 0, 5, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 3), Optional.empty(), new ItemStack((ItemLike)Items.GLOWSTONE_DUST), 0, 8, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 3), Optional.empty(), new ItemStack((ItemLike)Items.REDSTONE), 0, 8, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 2), Optional.empty(), new ItemStack((ItemLike)Items.GLOW_INK_SAC), 0, 8, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 4), Optional.empty(), new ItemStack((ItemLike)Items.HONEYCOMB), 0, 8, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 7), Optional.empty(), new ItemStack((ItemLike)Items.FERMENTED_SPIDER_EYE, 2), 0, 8, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 12), Optional.empty(), new ItemStack((ItemLike)Items.RABBIT_FOOT, 1), 0, 3, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 9), Optional.empty(), new ItemStack((ItemLike)Items.GLISTERING_MELON_SLICE, 2), 0, 4, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 12), Optional.empty(), new ItemStack((ItemLike)Items.CRIMSON_FUNGUS, 4), 0, 4, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 12), Optional.empty(), new ItemStack((ItemLike)Items.WARPED_FUNGUS, 4), 0, 4, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.APPLE, 12), Optional.empty(), new ItemStack((ItemLike)Items.EMERALD, 6), 0, 6, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.BEETROOT, 10), Optional.empty(), new ItemStack((ItemLike)Items.EMERALD, 8), 0, 6, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.CARROT, 6), Optional.empty(), new ItemStack((ItemLike)Items.EMERALD, 4), 0, 6, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.PORKCHOP, 6), Optional.empty(), new ItemStack((ItemLike)Items.EMERALD, 6), 0, 6, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.DRAGON_BREATH, 1), Optional.empty(), new ItemStack((ItemLike)ItemRegistry.ARCANE_ESSENCE.get(), 8), 0, 8, 5, 0.01f), new MerchantOffer(new ItemCost((ItemLike)Items.AXOLOTL_BUCKET, 1), Optional.empty(), new ItemStack((ItemLike)Items.EMERALD, 16), 0, 1, 5, 0.01f));

    public ApothecaristEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 25;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FocusOnTradingPlayerGoal<ApothecaristEntity>(this));
        this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(2, (Goal)new AlchemistAttackGoal(this, 1.25, 30, 70, 12.0f, 0.5f).setSpells((List)List.of(SpellRegistry.FANG_STRIKE_SPELL.get(), SpellRegistry.FANG_STRIKE_SPELL.get(), SpellRegistry.ACID_ORB_SPELL.get(), SpellRegistry.POISON_BREATH_SPELL.get(), SpellRegistry.STOMP_SPELL.get(), SpellRegistry.POISON_ARROW_SPELL.get()), (List)List.of(SpellRegistry.ROOT_SPELL.get()), List.of(), (List)List.of(SpellRegistry.OAKSKIN_SPELL.get(), SpellRegistry.STOMP_SPELL.get())).setDrinksPotions().setSingleUseSpell(SpellRegistry.FIREFLY_SWARM_SPELL.get(), 80, 200, 4, 6).setSpellQuality(0.25f, 0.6f));
        this.goalSelector.addGoal(3, (Goal)new PatrolNearLocationGoal(this, 30.0f, 0.75));
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.goalSelector.addGoal(10, (Goal)new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractPiglin.class, true));
        this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isHostileTowards));
        this.targetSelector.addGoal(5, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.swingTime > 0) {
            --this.swingTime;
        }
    }

    public void swing(InteractionHand pHand) {
        this.swingTime = 10;
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)ItemRegistry.PLAGUED_CHESTPLATE.get()));
        this.setDropChance(EquipmentSlot.CHEST, 0.0f);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.INDIRECT_MAGIC) && pSource.getEntity() == this) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public boolean canBeAffected(MobEffectInstance pEffectInstance) {
        return !AlchemistAttackGoal.ATTACK_POTIONS.contains(pEffectInstance.getEffect());
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 24.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.tickCount % 60 == 0) {
            this.level.getEntitiesOfClass(AbstractPiglin.class, this.getBoundingBox().inflate(this.getAttributeValue(Attributes.FOLLOW_RANGE))).forEach(piggy -> {
                if (PiglinAi.getAngerTarget((AbstractPiglin)piggy).isEmpty() && TargetingConditions.forCombat().test((LivingEntity)piggy, (LivingEntity)this)) {
                    PiglinAi.setAngerTarget((AbstractPiglin)piggy, (LivingEntity)this);
                }
            });
        }
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
            this.offers.addAll(this.createRandomOffers(3, 4));
            if (this.random.nextFloat() < 0.25f) {
                this.offers.add((Object)new AdditionalWanderingTrades.InkBuyTrade((InkItem)((Object)ItemRegistry.INK_UNCOMMON.get())).getOffer((Entity)this, this.random));
            }
            if (this.random.nextFloat() < 0.25f) {
                this.offers.add((Object)new AdditionalWanderingTrades.InkBuyTrade((InkItem)((Object)ItemRegistry.INK_RARE.get())).getOffer((Entity)this, this.random));
            }
            if (this.random.nextFloat() < 0.25f) {
                this.offers.add((Object)new AdditionalWanderingTrades.InkBuyTrade((InkItem)((Object)ItemRegistry.INK_EPIC.get())).getOffer((Entity)this, this.random));
            }
            if (this.random.nextFloat() < 0.5f) {
                this.offers.add((Object)new AdditionalWanderingTrades.ExilirBuyTrade(true, false).getOffer((Entity)this, this.random));
            }
            int j = this.random.nextIntBetweenInclusive(1, 3);
            for (int i = 0; i < j; ++i) {
                this.offers.add((Object)(this.random.nextBoolean() ? new AdditionalWanderingTrades.PotionSellTrade(null).getOffer((Entity)this, this.random) : new AdditionalWanderingTrades.ExilirSellTrade(true, false).getOffer((Entity)this, this.random)));
            }
            this.offers.add((Object)new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.NATURE.get()), 0.0f, 0.4f).getOffer((Entity)this, this.random));
            if (this.random.nextFloat() < 0.65f) {
                this.offers.add((Object)new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(SchoolRegistry.NATURE.get()), 0.5f, 0.9f).getOffer((Entity)this, this.random));
            }
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 16), Optional.empty(), new ItemStack((ItemLike)ItemRegistry.NETHERWARD_TINCTURE.get(), 1), 0, 8, 5, 0.01f));
            Item greaterElixir = (Item)List.of(ItemRegistry.GREATER_EVASION_ELIXIR, ItemRegistry.GREATER_OAKSKIN_ELIXIR, ItemRegistry.GREATER_INVISIBILITY_ELIXIR, ItemRegistry.GREATER_HEALING_POTION).get(this.random.nextInt(4)).get();
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)greaterElixir, 4), Optional.empty(), ((Item)ItemRegistry.NATURE_RUNE.get()).getDefaultInstance(), 0, 1, 5, 0.1f));
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
            offers.add(fillerOffers.get(integer));
        }
        return offers;
    }

    public void overrideOffers(MerchantOffers pOffers) {
    }

    public int getAmbientSoundInterval() {
        return 200;
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
        return pIsYesSound ? SoundEvents.PIGLIN_ADMIRING_ITEM : SoundEvents.PIGLIN_JEALOUS;
    }

    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.PIGLIN_ADMIRING_ITEM;
    }

    @Override
    public Optional<SoundEvent> getAngerSound() {
        return Optional.of(SoundEvents.PIGLIN_BRUTE_ANGRY);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PIGLIN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15f, 1.0f);
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

