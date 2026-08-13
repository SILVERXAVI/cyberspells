/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.GoalSelector
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.OpenDoorGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal
 *  net.minecraft.world.entity.ai.navigation.GroundPathNavigation
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.entity.ai.village.poi.PoiManager$Occupancy
 *  net.minecraft.world.entity.ai.village.poi.PoiTypes
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.npc.VillagerData
 *  net.minecraft.world.entity.npc.VillagerDataHolder
 *  net.minecraft.world.entity.npc.VillagerProfession
 *  net.minecraft.world.entity.npc.VillagerType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.Potions
 *  net.minecraft.world.item.trading.ItemCost
 *  net.minecraft.world.item.trading.MerchantOffer
 *  net.minecraft.world.item.trading.MerchantOffers
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.pathfinder.PathFinder
 *  net.minecraft.world.level.pathfinder.WalkNodeEvaluator
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards.priest;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.SupportMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.FindSupportableTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.FocusOnTradingPlayerGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GenericDefendVillageTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.GustDefenseGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.HomeOwner;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.ReturnToHomeAtNightGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.RoamVillageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardSupportGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import io.redspace.ironsspellbooks.item.FurledMapItem;
import io.redspace.ironsspellbooks.player.AdditionalWanderingTrades;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.NotNull;

public class PriestEntity
extends NeutralWizard
implements VillagerDataHolder,
SupportMob,
HomeOwner,
IMerchantWizard {
    private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(PriestEntity.class, (EntityDataSerializer)EntityDataSerializers.VILLAGER_DATA);
    private static final EntityDataAccessor<Boolean> DATA_VILLAGER_UNHAPPY = SynchedEntityData.defineId(PriestEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    public GoalSelector supportTargetSelector;
    private int unhappyTimer;
    boolean shouldLookForPoi;
    LivingEntity supportTarget;
    BlockPos homePos;
    @org.jetbrains.annotations.Nullable
    private Player tradingPlayer;
    @org.jetbrains.annotations.Nullable
    protected MerchantOffers offers;
    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    private long lastRestockCheckDayTime;

    public PriestEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 15;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FocusOnTradingPlayerGoal<PriestEntity>(this));
        this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(0, (Goal)new OpenDoorGoal((Mob)this, true));
        this.goalSelector.addGoal(1, (Goal)new GustDefenseGoal(this));
        this.goalSelector.addGoal(2, new WizardSupportGoal<PriestEntity>(this, 1.25, 100, 180).setSpells(List.of(SpellRegistry.BLESSING_OF_LIFE_SPELL.get(), SpellRegistry.BLESSING_OF_LIFE_SPELL.get(), SpellRegistry.HEALING_CIRCLE_SPELL.get()), List.of(SpellRegistry.FORTIFY_SPELL.get())));
        this.goalSelector.addGoal(3, (Goal)new WizardAttackGoal(this, 1.25, 35, 70).setSpells(List.of(SpellRegistry.WISP_SPELL.get(), SpellRegistry.GUIDING_BOLT_SPELL.get()), List.of(SpellRegistry.GUST_SPELL.get()), List.of(), List.of(SpellRegistry.HEAL_SPELL.get())).setSpellQuality(0.3f, 0.5f).setDrinksPotions());
        this.goalSelector.addGoal(5, (Goal)new RoamVillageGoal(this, 30.0f, 1.0));
        this.goalSelector.addGoal(6, new ReturnToHomeAtNightGoal<PriestEntity>(this, 1.0));
        this.goalSelector.addGoal(7, (Goal)new PatrolNearLocationGoal(this, 30.0f, 1.0));
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
        this.goalSelector.addGoal(10, (Goal)new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.targetSelector.addGoal(2, (Goal)new GenericDefendVillageTargetGoal((Mob)this));
        this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isHostileTowards));
        this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, Mob.class, 5, false, false, mob -> mob instanceof Enemy && !(mob instanceof Creeper)));
        this.targetSelector.addGoal(5, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
        this.supportTargetSelector = new GoalSelector(this.level.getProfilerSupplier());
        this.supportTargetSelector.addGoal(0, new FindSupportableTargetGoal<PriestEntity>(this, LivingEntity.class, true, mob -> !this.isAngryAt((LivingEntity)mob) && mob.getHealth() * 1.25f < mob.getMaxHealth() && (mob.getType().is(ModTags.VILLAGE_ALLIES) || mob instanceof Player)));
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @org.jetbrains.annotations.Nullable SpawnGroupData pSpawnData) {
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        if (pReason == MobSpawnType.STRUCTURE) {
            this.shouldLookForPoi = true;
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)ItemRegistry.PRIEST_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)ItemRegistry.PRIEST_CHESTPLATE.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0f);
        this.setDropChance(EquipmentSlot.CHEST, 0.0f);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 24.0).add(AttributeRegistry.CAST_TIME_REDUCTION, 1.5).add(Attributes.MOVEMENT_SPEED, 0.23);
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new GroundPathNavigation(this, (Mob)this, pLevel){

            protected PathFinder createPathFinder(int pMaxVisitedNodes) {
                this.nodeEvaluator = new WalkNodeEvaluator();
                this.nodeEvaluator.setCanPassDoors(true);
                this.nodeEvaluator.setCanOpenDoors(true);
                return new PathFinder(this.nodeEvaluator, pMaxVisitedNodes);
            }
        };
    }

    @Override
    public boolean guardsBlocks() {
        return false;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return null;
        }
        return this.isTrading() ? SoundEvents.VILLAGER_TRADE : SoundEvents.VILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_VILLAGER_DATA, (Object)new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
        pBuilder.define(DATA_VILLAGER_UNHAPPY, (Object)false);
    }

    public void setVillagerData(VillagerData villagerdata) {
        villagerdata.setProfession(VillagerProfession.NONE);
        this.entityData.set(DATA_VILLAGER_DATA, (Object)villagerdata);
    }

    public boolean isUnhappy() {
        return (Boolean)this.entityData.get(DATA_VILLAGER_UNHAPPY);
    }

    @NotNull
    public VillagerData getVillagerData() {
        return (VillagerData)this.entityData.get(DATA_VILLAGER_DATA);
    }

    @Override
    @org.jetbrains.annotations.Nullable
    public LivingEntity getSupportTarget() {
        return this.supportTarget;
    }

    @Override
    public void setSupportTarget(LivingEntity target) {
        this.supportTarget = target;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.shouldLookForPoi) {
            Level level = this.level;
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                Optional optional1 = serverLevel.getPoiManager().find(poiTypeHolder -> poiTypeHolder.is(PoiTypes.MEETING), blockPos -> true, this.blockPosition(), 100, PoiManager.Occupancy.ANY);
                optional1.ifPresent(this::setHome);
            }
            this.shouldLookForPoi = false;
        }
        if (this.tickCount % 4 == 0 && this.tickCount > 1) {
            this.supportTargetSelector.tick();
        }
        if (this.tickCount % 60 == 0) {
            this.level.getEntities((Entity)this, this.getBoundingBox().inflate(this.getAttributeValue(Attributes.FOLLOW_RANGE)), entity -> entity instanceof Enemy && !(entity instanceof Creeper) && !(entity instanceof IMagicSummon) && !(entity instanceof TamableAnimal)).forEach(enemy -> {
                Mob mob;
                if (enemy instanceof Mob && (mob = (Mob)enemy).getTarget() == null && TargetingConditions.forCombat().test((LivingEntity)mob, (LivingEntity)this)) {
                    mob.setTarget((LivingEntity)this);
                }
            });
        }
        if (this.unhappyTimer > 0 && --this.unhappyTimer == 0) {
            this.entityData.set(DATA_VILLAGER_UNHAPPY, (Object)false);
        }
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        boolean preventTrade;
        boolean bl = preventTrade = this.isAggressive() || !this.level.isClientSide && this.getOffers().isEmpty();
        if (pHand == InteractionHand.MAIN_HAND && preventTrade && !this.level.isClientSide) {
            this.setUnhappy();
        }
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

    public void setUnhappy() {
        if (!this.level.isClientSide) {
            this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
            this.unhappyTimer = 20;
            this.entityData.set(DATA_VILLAGER_UNHAPPY, (Object)true);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.serializeHome(this, pCompound);
        this.serializeMerchant(pCompound, this.offers, this.lastRestockGameTime, this.numberOfRestocksToday);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.deserializeHome(this, pCompound);
        this.deserializeMerchant(pCompound, c -> {
            this.offers = c;
        });
    }

    @Override
    public Optional<SoundEvent> getAngerSound() {
        return Optional.of(SoundEvents.VILLAGER_NO);
    }

    @Override
    @org.jetbrains.annotations.Nullable
    public BlockPos getHome() {
        return this.homePos;
    }

    @Override
    public void setHome(BlockPos homePos) {
        this.homePos = homePos;
    }

    public void setTradingPlayer(@org.jetbrains.annotations.Nullable Player pTradingPlayer) {
        this.tradingPlayer = pTradingPlayer;
    }

    @org.jetbrains.annotations.Nullable
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 24), Optional.empty(), FurledMapItem.of(IronsSpellbooks.id("evoker_fort"), Component.translatable((String)"item.irons_spellbooks.evoker_fort_battle_plans")), 0, 1, 5, 10.0f));
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)ItemRegistry.GREATER_HEALING_POTION.get()), new ItemStack((ItemLike)Items.EMERALD, 18), 3, 0, 0.2f));
            this.offers.add((Object)new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, 6), Utils.setPotion(new ItemStack((ItemLike)Items.POTION), (Holder<Potion>)Potions.HEALING), 2, 0, 0.2f));
            this.offers.add((Object)new BibleTrade().getOffer((Entity)this, this.random));
            this.offers.removeIf(Objects::isNull);
            this.setLastRestockGameTime(this.level.getGameTime());
        }
        return this.offers;
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

    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    protected SoundEvent getTradeUpdatedSound(boolean pIsYesSound) {
        return pIsYesSound ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
    }

    private void startTrading(Player pPlayer) {
        this.setTradingPlayer(pPlayer);
        this.openTradingScreen(pPlayer, this.getDisplayName(), this.getVillagerData().getLevel());
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

    static class BibleTrade
    extends AdditionalWanderingTrades.SimpleTrade {
        private BibleTrade() {
            super((trader, random) -> {
                if (!trader.level.isClientSide) {
                    ItemStack cost = new ItemStack((ItemLike)ItemRegistry.TRANSLATED_ARCHEVOKER_LOGBOOK.get());
                    ItemStack forSale = new ItemStack((ItemLike)ItemRegistry.VILLAGER_SPELL_BOOK.get());
                    return new MerchantOffer(new ItemCost((ItemLike)cost.getItem(), cost.getCount()), forSale, 1, 5, 0.5f);
                }
                return null;
            });
        }
    }
}

