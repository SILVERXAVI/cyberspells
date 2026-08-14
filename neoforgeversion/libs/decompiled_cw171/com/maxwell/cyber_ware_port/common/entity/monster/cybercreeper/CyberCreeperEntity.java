/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.monster.Creeper
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.Level
 */
package com.maxwell.cyber_ware_port.common.entity.monster.cybercreeper;

import com.maxwell.cyber_ware_port.common.entity.ICyberwareMob;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class CyberCreeperEntity
extends Creeper
implements ICyberwareMob {
    private boolean isCausingCustomExplosion = false;

    public CyberCreeperEntity(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    @Override
    public List<Item> getSpecialDrops() {
        return Arrays.asList((Item)ModItems.SOLARSKIN.get(), (Item)ModItems.SUBDERMAL_SPIKES.get(), (Item)ModItems.SYNTHETIC_SKIN.get(), (Item)ModItems.TARGETED_IMMUNOSUPPRESSANT.get());
    }

    public boolean isCausingCustomExplosion() {
        return this.isCausingCustomExplosion;
    }

    public void setCausingCustomExplosion(boolean isCausing) {
        this.isCausingCustomExplosion = isCausing;
    }
}

