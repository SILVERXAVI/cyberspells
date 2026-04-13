/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.DragonFireball
 *  net.minecraft.world.entity.projectile.ProjectileUtil
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Item$TooltipContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  net.neoforged.neoforge.network.registration.PayloadRegistrar
 */
package com.perigrine3.createcybernetics.item.cyberware;

import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class IgniphorusGlandItem
extends Item
implements ICyberwareItem {
    private final int humanityCost;
    private static final String NBT_LAST_SHOT_TICK = "cc_igniphorus_lastDragonFireballTick";
    private static final int COOLDOWN_TICKS = 100;

    public IgniphorusGlandItem(Item.Properties props, int humanityCost) {
        super(props);
        this.humanityCost = humanityCost;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add((Component)Component.translatable((String)"tooltip.createcybernetics.humanity", (Object[])new Object[]{this.humanityCost}).withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public int getHumanityCost() {
        return this.humanityCost;
    }

    @Override
    public Set<TagKey<Item>> requiresCyberwareTags(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of(ModTags.Items.LUNGS_ITEMS);
    }

    @Override
    public Set<CyberwareSlot> getSupportedSlots() {
        return Set.of(CyberwareSlot.LUNGS);
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
    public Set<Item> incompatibleCyberware(ItemStack installedStack, CyberwareSlot slot) {
        return Set.of((Item)ModItems.WETWARE_SCULKLUNGS.get(), (Item)ModItems.WETWARE_GUARDIANEYE.get());
    }

    @Override
    public void onInstalled(Player player) {
    }

    @Override
    public void onRemoved(Player player) {
    }

    @Override
    public void onTick(Player player) {
        ICyberwareItem.super.onTick(player);
    }

    private static void tryShoot(ServerPlayer player) {
        CompoundTag tag;
        long last;
        Vec3 look;
        Vec3 end;
        ServerLevel level = player.serverLevel();
        if (!player.isCrouching()) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null || !data.hasSpecificItem((Item)ModItems.WETWARE_FIREBREATHINGLUNGS.get(), CyberwareSlot.LUNGS)) {
            return;
        }
        double reach = 5.0;
        Vec3 start = player.getEyePosition();
        BlockHitResult blockHit = level.clip(new ClipContext(start, end = start.add((look = player.getLookAngle()).scale(5.0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)player));
        if (blockHit.getType() != HitResult.Type.MISS) {
            return;
        }
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult((Level)level, (Entity)player, (Vec3)start, (Vec3)end, (AABB)player.getBoundingBox().expandTowards(look.scale(5.0)).inflate(1.0), e -> e.isPickable() && e != player);
        if (entityHit != null) {
            return;
        }
        long now = level.getGameTime();
        if (now - (last = (tag = player.getPersistentData()).getLong(NBT_LAST_SHOT_TICK)) < 100L) {
            return;
        }
        tag.putLong(NBT_LAST_SHOT_TICK, now);
        Vec3 power = look.scale(5.0);
        DragonFireball fireball = new DragonFireball((Level)level, (LivingEntity)player, power);
        Vec3 spawnPos = start.add(look.scale(0.6));
        fireball.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, player.getYRot(), player.getXRot());
        level.addFreshEntity((Entity)fireball);
        level.levelEvent(null, 1017, player.blockPosition(), 0);
        player.swing(InteractionHand.MAIN_HAND, false);
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
    public static final class ClientInput {
        private static boolean wasUseDown = false;

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) {
                return;
            }
            if (mc.screen != null) {
                return;
            }
            boolean useDown = mc.options.keyUse.isDown();
            boolean risingEdge = useDown && !wasUseDown;
            wasUseDown = useDown;
            if (!risingEdge) {
                return;
            }
            if (!mc.player.isCrouching()) {
                return;
            }
            if (mc.hitResult == null) {
                return;
            }
            HitResult.Type type = mc.hitResult.getType();
            if (type != HitResult.Type.MISS && type != HitResult.Type.ENTITY && type != HitResult.Type.BLOCK) {
                return;
            }
            if (!mc.player.getMainHandItem().isEmpty() || !mc.player.getOffhandItem().isEmpty()) {
                return;
            }
            if (mc.getConnection() != null) {
                mc.getConnection().send((Packet)new ServerboundCustomPayloadPacket((CustomPacketPayload)new DragonBreathFireballPayload()));
            }
        }
    }

    @EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.MOD)
    public static final class NetworkRegistration {
        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar("1");
            registrar.playToServer(DragonBreathFireballPayload.TYPE, DragonBreathFireballPayload.STREAM_CODEC, NetworkRegistration::handle);
        }

        private static void handle(DragonBreathFireballPayload payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                Player patt0$temp = context.player();
                if (!(patt0$temp instanceof ServerPlayer)) {
                    return;
                }
                ServerPlayer sp = (ServerPlayer)patt0$temp;
                IgniphorusGlandItem.tryShoot(sp);
            });
        }
    }

    public record DragonBreathFireballPayload() implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<DragonBreathFireballPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"igniphorus_dragon_fireball"));
        public static final StreamCodec<ByteBuf, DragonBreathFireballPayload> STREAM_CODEC = StreamCodec.unit((Object)new DragonBreathFireballPayload());

        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}

