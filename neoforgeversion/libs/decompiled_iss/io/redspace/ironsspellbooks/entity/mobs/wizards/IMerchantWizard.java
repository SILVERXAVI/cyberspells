/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.DynamicOps
 *  net.minecraft.Util
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtOps
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.item.trading.Merchant
 *  net.minecraft.world.item.trading.MerchantOffer
 *  net.minecraft.world.item.trading.MerchantOffers
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 *  org.slf4j.Logger
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards;

import com.mojang.serialization.DynamicOps;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public interface IMerchantWizard
extends Merchant {
    default public void serializeMerchant(CompoundTag pCompound, @Nullable MerchantOffers offers, long lastRestockGameTime, int numberOfRestocksToday) {
        if (offers != null && !offers.isEmpty()) {
            pCompound.put("Offers", (Tag)MerchantOffers.CODEC.encodeStart((DynamicOps)this.level().registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE), (Object)offers).getOrThrow());
        }
        pCompound.putLong("LastRestock", lastRestockGameTime);
        pCompound.putInt("RestocksToday", numberOfRestocksToday);
    }

    default public void deserializeMerchant(CompoundTag pCompound, Consumer<MerchantOffers> setOffers) {
        if (pCompound.contains("Offers")) {
            MerchantOffers.CODEC.parse((DynamicOps)this.level().registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE), (Object)pCompound.get("Offers")).resultOrPartial(Util.prefix((String)"Failed to load offers: ", arg_0 -> ((Logger)IronsSpellbooks.LOGGER).warn(arg_0))).ifPresent(setOffers);
        }
        this.setLastRestockGameTime(pCompound.getLong("LastRestock"));
        this.setRestocksToday(pCompound.getInt("RestocksToday"));
    }

    default public boolean isTrading() {
        return this.getTradingPlayer() != null;
    }

    default public boolean needsToRestock() {
        for (MerchantOffer merchantoffer : this.getOffers()) {
            if (!merchantoffer.needsRestock()) continue;
            return true;
        }
        return false;
    }

    default public boolean allowedToRestock() {
        return this.getRestocksToday() == 0 && this.level().getGameTime() > this.getLastRestockGameTime() + 2400L;
    }

    default public void stopTrading() {
        this.setTradingPlayer(null);
    }

    default public boolean shouldRestock() {
        long timeToNextRestock = this.getLastRestockGameTime() + 12000L;
        long currentGameTime = this.level().getGameTime();
        boolean hasDayElapsed = currentGameTime > timeToNextRestock;
        long currentDayTime = this.level().getDayTime();
        if (this.getLastRestockCheckDayTime() > 0L) {
            long currentDay = currentDayTime / 24000L;
            long lastRestockDay = this.getLastRestockCheckDayTime() / 24000L;
            hasDayElapsed |= currentDay > lastRestockDay;
        } else {
            this.setLastRestockCheckDayTime(currentDayTime);
        }
        if (hasDayElapsed) {
            this.setLastRestockCheckDayTime(currentDayTime);
            this.setRestocksToday(0);
        }
        return this.needsToRestock() && this.allowedToRestock();
    }

    default public void restock() {
        for (MerchantOffer offer : this.getOffers()) {
            offer.updateDemand();
            offer.resetUses();
        }
        this.setLastRestockGameTime(this.level().getGameTime());
        this.setRestocksToday(this.getRestocksToday() + 1);
    }

    public int getRestocksToday();

    public void setRestocksToday(int var1);

    public long getLastRestockGameTime();

    public void setLastRestockGameTime(long var1);

    public long getLastRestockCheckDayTime();

    public void setLastRestockCheckDayTime(long var1);

    public Level level();

    default public int getVillagerXp() {
        return 0;
    }

    default public void overrideXp(int pXp) {
    }

    default public boolean showProgressBar() {
        return false;
    }

    default public boolean isClientSide() {
        return this.level().isClientSide();
    }
}

