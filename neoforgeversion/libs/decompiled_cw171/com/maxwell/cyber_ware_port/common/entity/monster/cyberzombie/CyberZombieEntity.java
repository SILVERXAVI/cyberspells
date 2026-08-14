/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.monster.Zombie
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.Level
 */
package com.maxwell.cyber_ware_port.common.entity.monster.cyberzombie;

import com.maxwell.cyber_ware_port.common.entity.ICyberwareMob;
import com.maxwell.cyber_ware_port.init.ModItems;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class CyberZombieEntity
extends Zombie
implements ICyberwareMob {
    public CyberZombieEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
        Objects.requireNonNull(this.getAttribute(Attributes.STEP_HEIGHT)).setBaseValue(2.0);
    }

    @Override
    public List<Item> getSpecialDrops() {
        return Arrays.asList((Item)ModItems.CYBER_ARM_LEFT.get(), (Item)ModItems.CYBER_ARM_RIGHT.get(), (Item)ModItems.CYBER_LEG_LEFT.get(), (Item)ModItems.CYBER_LEG_RIGHT.get(), (Item)ModItems.REINFORCED_FIST.get(), (Item)ModItems.DEPLOYABLE_WHEELS.get());
    }

    @Override
    public List<Item> getForbiddenDrops() {
        return Arrays.asList((Item)ModItems.RAPID_FIRE_FLYWHEEL.get(), (Item)ModItems.LINEAR_ACTUATORS.get(), (Item)ModItems.INTERNAL_DEFIBRILLATOR.get());
    }

    protected boolean isSunBurnTick() {
        return false;
    }
}

