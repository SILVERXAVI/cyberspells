/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  top.theillusivec4.curios.api.CuriosApi
 *  top.theillusivec4.curios.api.SlotContext
 */
package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class TeleportationAmuletItem
extends SimpleDescriptiveCurio {
    private static final Component VANITY_DESCRIPTION = Component.translatable((String)"item.irons_spellbooks.teleportation_amulet.desc.alt").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC});

    public TeleportationAmuletItem(Item.Properties properties) {
        super(properties, Curios.NECKLACE_SLOT);
    }

    private void handleCurse(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity != null && !slotContext.entity().level.isClientSide && !this.canUse(entity)) {
            CuriosApi.getCuriosInventory((LivingEntity)slotContext.entity()).ifPresent(handler -> {
                ItemStack equippedStack = handler.getEquippedCurios().getStackInSlot(slotContext.index());
                if (ItemStack.matches((ItemStack)stack, (ItemStack)equippedStack)) {
                    handler.setEquippedCurio(Curios.NECKLACE_SLOT, slotContext.index(), ItemStack.EMPTY);
                    this.createItemEntity(slotContext.entity().level, stack, slotContext.entity().position());
                }
            });
        }
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, Item.TooltipContext tooltipContext, ItemStack stack) {
        Player player = MinecraftInstanceHelper.getPlayer();
        if (player != null && this.canUse((LivingEntity)player)) {
            super.getAttributesTooltip(tooltips, tooltipContext, stack);
        }
        tooltips.add(0, VANITY_DESCRIPTION);
        return tooltips;
    }

    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity().tickCount % 5 == 0) {
            this.handleCurse(slotContext, stack);
        }
    }

    private boolean canUse(LivingEntity livingEntity) {
        return livingEntity.getAttributeValue(AttributeRegistry.ENDER_SPELL_POWER) > 1.25;
    }

    private void createItemEntity(Level level, ItemStack stack, Vec3 center) {
        Vec3 target = center.add(new Vec3((double)((float)Utils.random.nextIntBetweenInclusive(4, 8) + Utils.random.nextFloat()), 0.0, 0.0).yRot(Utils.random.nextFloat() * ((float)Math.PI * 2)));
        Vec3 clipped = Utils.raycastForBlock(level, center.add(0.0, 0.5, 0.0), target.add(0.0, 0.5, 0.0), ClipContext.Fluid.NONE).getLocation();
        Vec3 placement = Utils.moveToRelativeGroundLevel(level, clipped, 5).add(0.0, 0.75, 0.0);
        ItemEntity item = new ItemEntity(level, placement.x, placement.y, placement.z, stack);
        level.addFreshEntity((Entity)item);
        MagicManager.spawnParticles(level, ParticleHelper.UNSTABLE_ENDER, placement.x, placement.y, placement.z, 20, 0.2, 0.2, 0.2, 0.2, false);
        level.playSound(null, BlockPos.containing((Position)placement), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
        level.playSound(null, BlockPos.containing((Position)center), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}

