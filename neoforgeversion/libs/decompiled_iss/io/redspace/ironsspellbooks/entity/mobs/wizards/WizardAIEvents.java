/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.advancements.AdvancementHolder
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.behavior.BehaviorUtils
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 */
package io.redspace.ironsspellbooks.entity.mobs.wizards;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.List;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class WizardAIEvents {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().is(ModTags.GUARDED_BY_WIZARDS)) {
            WizardAIEvents.angerNearbyWizards(event.getPlayer(), 3, false, true);
        }
    }

    @SubscribeEvent
    public static void onBlockUsed(PlayerInteractEvent.RightClickBlock event) {
        RandomizableContainerBlockEntity randomizableContainerBlockEntity;
        BlockEntity blockEntity;
        BlockState blockstate = event.getLevel().getBlockState(event.getHitVec().getBlockPos());
        if (blockstate.is(ModTags.GUARDED_BY_WIZARDS) && (!((blockEntity = event.getLevel().getBlockEntity(event.getPos())) instanceof RandomizableContainerBlockEntity) || (randomizableContainerBlockEntity = (RandomizableContainerBlockEntity)blockEntity).getLootTable() != null)) {
            WizardAIEvents.angerNearbyWizards(event.getEntity(), 1, false, true);
        }
    }

    public static void angerNearbyWizards(Player player, int angerLevel, boolean requireLineOfSight, boolean blockRelated) {
        if (player.getAbilities().instabuild) {
            return;
        }
        List list = player.level.getEntitiesOfClass(NeutralWizard.class, player.getBoundingBox().inflate(16.0));
        list.stream().filter(neutralWizard -> !(!neutralWizard.guardsBlocks() && blockRelated || requireLineOfSight && !BehaviorUtils.canSee((LivingEntity)neutralWizard, (LivingEntity)player))).forEach(neutralWizard -> {
            ServerPlayer serverPlayer;
            AdvancementHolder advancement;
            neutralWizard.increaseAngerLevel(player, angerLevel, true);
            neutralWizard.setPersistentAngerTarget(player.getUUID());
            if (blockRelated && player instanceof ServerPlayer && (advancement = (serverPlayer = (ServerPlayer)player).serverLevel().getServer().getAdvancements().get(IronsSpellbooks.id("irons_spellbooks/steal_from_wizard"))) != null) {
                serverPlayer.getAdvancements().award(advancement, "anger_wizard");
            }
        });
    }
}

