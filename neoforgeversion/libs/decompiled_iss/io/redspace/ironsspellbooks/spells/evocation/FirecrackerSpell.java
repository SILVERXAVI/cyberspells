/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntImmutableList
 *  it.unimi.dsi.fastutil.ints.IntList
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.component.FireworkExplosion
 *  net.minecraft.world.item.component.FireworkExplosion$Shape
 *  net.minecraft.world.item.component.Fireworks
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 */
package io.redspace.ironsspellbooks.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.ExtendedFireworkRocket;
import it.unimi.dsi.fastutil.ints.IntImmutableList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import java.util.Random;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FirecrackerSpell
extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath((String)"irons_spellbooks", (String)"firecracker");
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(10).setCooldownSeconds(1.5).build();
    private static final int[] DYE_COLORS = new int[]{11546150, 6192150, 3949738, 8991416, 1481884, 15961002, 8439583, 16701501, 3847130, 13061821, 16351261, 0xF9FFFE};

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable((String)"ui.irons_spellbooks.damage", (Object[])new Object[]{Utils.stringTruncation(this.getSpellPower(spellLevel, (Entity)caster), 1)}));
    }

    public FirecrackerSpell() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 20;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
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
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 shootAngle = entity.getLookAngle().normalize();
        Vec3 spawn = Utils.raycastForEntity(world, (Entity)entity, this.getRange(spellLevel, entity), true).getLocation().subtract(shootAngle.scale(0.25));
        ExtendedFireworkRocket firework = new ExtendedFireworkRocket(world, this.randomFireworkRocket(), (Entity)entity, spawn.x, spawn.y, spawn.z, true, this.getDamage(spellLevel, entity));
        world.addFreshEntity((Entity)firework);
        firework.shoot(shootAngle.x, shootAngle.y, shootAngle.z, 0.0f, 0.0f);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private int getRange(int spellLevel, LivingEntity entity) {
        return 15 + (int)(this.getSpellPower(spellLevel, (Entity)entity) * 2.0f);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, (Entity)entity);
    }

    private ItemStack randomFireworkRocket() {
        Random random = new Random();
        ItemStack rocket = new ItemStack((ItemLike)Items.FIREWORK_ROCKET);
        int type = random.nextInt(3) * 2;
        if (random.nextFloat() < 0.08f) {
            type = 3;
        }
        FireworkExplosion.Shape shape = FireworkExplosion.Shape.values()[type];
        Fireworks fireworks = new Fireworks(-1, List.of(new FireworkExplosion(shape, (IntList)new IntImmutableList(this.randomColors()), (IntList)new IntImmutableList(new int[0]), random.nextInt(3) == 0, random.nextInt(3) == 0)));
        rocket.set(DataComponents.FIREWORKS, (Object)fireworks);
        return rocket;
    }

    private int[] randomColors() {
        int[] colors = new int[3];
        Random random = new Random();
        for (int i = 0; i < colors.length; ++i) {
            colors[i] = DYE_COLORS[random.nextInt(DYE_COLORS.length)];
        }
        return colors;
    }
}

