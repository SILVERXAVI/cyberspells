/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.Event
 *  net.neoforged.bus.api.ICancellableEvent
 */
package com.maxwell.cyber_ware_port.api.event;

import com.maxwell.cyber_ware_port.common.block.charger.ChargerBlockEntity;
import com.maxwell.cyber_ware_port.common.block.cwb.CyberwareWorkbenchBlockEntity;
import com.maxwell.cyber_ware_port.common.block.scanner.ScannerBlockEntity;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class CyberwareEvents {

    public static class Scan
    extends Event {
        private final ScannerBlockEntity tile;
        private final ItemStack inputStack;

        protected Scan(ScannerBlockEntity tile, ItemStack inputStack) {
            this.tile = tile;
            this.inputStack = inputStack;
        }

        public static class Complete
        extends Scan
        implements ICancellableEvent {
            private float chance;
            private boolean consumeItem;

            public Complete(ScannerBlockEntity tile, ItemStack inputStack, float defaultChance) {
                super(tile, inputStack);
                this.chance = defaultChance;
                this.consumeItem = true;
            }

            public float getChance() {
                return this.chance;
            }

            public void setChance(float chance) {
                this.chance = chance;
            }

            public boolean shouldConsumeItem() {
                return this.consumeItem;
            }

            public void setConsumeItem(boolean consume) {
                this.consumeItem = consume;
            }
        }
    }

    public static class Salvage
    extends Event {
        private final CyberwareWorkbenchBlockEntity tile;
        private final ItemStack inputStack;

        protected Salvage(CyberwareWorkbenchBlockEntity tile, ItemStack inputStack) {
            this.tile = tile;
            this.inputStack = inputStack;
        }

        public CyberwareWorkbenchBlockEntity getTile() {
            return this.tile;
        }

        public ItemStack getInputStack() {
            return this.inputStack;
        }

        public static class Post
        extends Salvage {
            private final List<ItemStack> outputs;

            public Post(CyberwareWorkbenchBlockEntity tile, ItemStack inputStack, List<ItemStack> outputs) {
                super(tile, inputStack);
                this.outputs = outputs;
            }

            public List<ItemStack> getOutputs() {
                return this.outputs;
            }
        }

        public static class Pre
        extends Salvage
        implements ICancellableEvent {
            private float blueprintChance;

            public Pre(CyberwareWorkbenchBlockEntity tile, ItemStack inputStack, float blueprintChance) {
                super(tile, inputStack);
                this.blueprintChance = blueprintChance;
            }

            public float getBlueprintChance() {
                return this.blueprintChance;
            }

            public void setBlueprintChance(float chance) {
                this.blueprintChance = chance;
            }
        }
    }

    public static class Recharge
    extends Event
    implements ICancellableEvent {
        private final LivingEntity entity;
        private final ChargerBlockEntity tile;
        private final boolean isDrainOperation;

        public Recharge(LivingEntity entity, ChargerBlockEntity tile, boolean isDrain) {
            this.entity = entity;
            this.tile = tile;
            this.isDrainOperation = isDrain;
        }

        public LivingEntity getEntity() {
            return this.entity;
        }

        public ChargerBlockEntity getTile() {
            return this.tile;
        }

        public boolean isDrainOperation() {
            return this.isDrainOperation;
        }
    }
}

