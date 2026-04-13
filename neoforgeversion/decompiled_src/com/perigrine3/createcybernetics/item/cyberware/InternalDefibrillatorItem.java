/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.neoforged.bus.api.EventPriority
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.event.entity.living.LivingDeathEvent
 *  net.neoforged.neoforge.network.PacketDistributor
 *  net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
 *  net.neoforged.neoforge.network.registration.PayloadRegistrar
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.mojang.serialization.Codec;
import com.perigrine3.createcybernetics.advancement.ModCriteria;
import com.perigrine3.createcybernetics.advancement.triggers.DeusExMachinaTrigger;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.surgery.DefaultOrgans;
import com.perigrine3.createcybernetics.item.ModItems;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class InternalDefibrillatorItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final int DEFIB_ENERGY_COST = 50;

    public InternalDefibrillatorItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.heartupgrades_defibrillator.energy").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<Item> requiresCyberware(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of((Item)ModItems.BODYPART_HEART.get());
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.HEART);
    }

    @Override
    public boolean replacesOrgan() {
        return false;
    }

    @Override
    public Set<CyberwareSlot> getReplacedOrgans() {
        return Set.of();
    }

    @Override
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
    }

    @Override
    public void onTick(Player player) {
    }

    @Override
    public boolean dropsOnDeath(ItemStack installedStack, CyberwareSlot slot) {
        return false;
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME)
    public static final class Events {
        @SubscribeEvent(priority=EventPriority.HIGHEST)
        public static void onLivingDeath(LivingDeathEvent event) {
            ItemStack display;
            LivingEntity livingEntity = event.getEntity();
            if (!(livingEntity instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer player = (ServerPlayer)livingEntity;
            if (player.level().isClientSide) {
                return;
            }
            if (event.isCanceled()) {
                return;
            }
            PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
            if (data == null) {
                return;
            }
            int[] idx = Events.findInstalledDefibIndex(data);
            if (idx == null) {
                return;
            }
            CyberwareSlot slot = CyberwareSlot.values()[idx[0]];
            InstalledCyberware inst = data.get(slot, idx[1]);
            ItemStack itemStack = display = inst != null && inst.getItem() != null && !inst.getItem().isEmpty() ? inst.getItem().copy() : ((Item)ModItems.HEARTUPGRADES_DEFIBRILLATOR.get()).getDefaultInstance();
            if (!data.tryConsumeEnergy(50)) {
                return;
            }
            if (!Events.tryTotemRevive(player)) {
                data.receiveEnergy((Player)player, 50);
                data.setDirty();
                player.syncData(ModAttachments.CYBERWARE);
                return;
            }
            PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)new DefibPopPayload(display), (CustomPacketPayload[])new CustomPacketPayload[0]);
            Events.removeInstalledDefib(data, idx[0], idx[1]);
            data.setDirty();
            player.syncData(ModAttachments.CYBERWARE);
            event.setCanceled(true);
        }

        private static boolean tryTotemRevive(ServerPlayer player) {
            if (player.getHealth() > 0.0f) {
                return false;
            }
            player.setHealth(1.0f);
            player.removeAllEffects();
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            ((ServerLevel)player.level()).sendParticles((ParticleOptions)ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY() + 1.0, player.getZ(), 60, 0.6, 0.8, 0.6, 0.15);
            player.level().playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
            ((DeusExMachinaTrigger)((Object)ModCriteria.DEUS_EX_MACHINA.get())).trigger(player);
            return true;
        }

        private static int[] findInstalledDefibIndex(PlayerCyberwareData data) {
            for (CyberwareSlot slot : CyberwareSlot.values()) {
                int size = slot.size;
                for (int i = 0; i < size; ++i) {
                    ItemStack st;
                    InstalledCyberware inst = data.get(slot, i);
                    if (inst == null || (st = inst.getItem()) == null || st.isEmpty() || !st.is((Item)ModItems.HEARTUPGRADES_DEFIBRILLATOR.get())) continue;
                    return new int[]{slot.ordinal(), i};
                }
            }
            return null;
        }

        private static void removeInstalledDefib(PlayerCyberwareData data, int slotOrdinal, int index) {
            CyberwareSlot slot = CyberwareSlot.values()[slotOrdinal];
            data.remove(slot, index);
            ItemStack def = DefaultOrgans.get(slot, index);
            if (def == null) {
                def = ItemStack.EMPTY;
            }
            if (!def.isEmpty()) {
                int humanity = 0;
                data.set(slot, index, new InstalledCyberware(def.copy(), slot, index, humanity));
            }
        }

        private Events() {
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD)
    public static final class NetworkRegistration {
        @SubscribeEvent
        public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar("1");
            registrar.playToClient(DefibPopPayload.TYPE, DefibPopPayload.STREAM_CODEC, (payload, context) -> context.enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                mc.gameRenderer.displayItemActivation(payload.stack());
            }));
        }

        private NetworkRegistration() {
        }
    }

    public record DefibPopPayload(ItemStack stack) implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<DefibPopPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"defib_pop"));
        public static final StreamCodec<RegistryFriendlyByteBuf, DefibPopPayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.fromCodecWithRegistries((Codec)ItemStack.CODEC), DefibPopPayload::stack, DefibPopPayload::new);

        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}

