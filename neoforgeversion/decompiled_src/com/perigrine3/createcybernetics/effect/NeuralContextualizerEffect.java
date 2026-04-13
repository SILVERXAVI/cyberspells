/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectCategory
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.AxeItem
 *  net.minecraft.world.item.HoeItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.PickaxeItem
 *  net.minecraft.world.item.ShovelItem
 *  net.minecraft.world.item.SwordItem
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.TieredItem
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package com.perigrine3.createcybernetics.effect;

import com.perigrine3.createcybernetics.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeuralContextualizerEffect
extends MobEffect {
    private static final int UPDATE_EVERY_TICKS = 2;
    private static final int SWAP_COOLDOWN_TICKS = 4;
    private static final double MAX_REACH_BLOCKS = 6.0;
    private static final TagKey<Block> CREATE_WRENCH_PICKUP = TagKey.create((ResourceKey)Registries.BLOCK, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"create", (String)"wrench_pickup"));
    private static final TagKey<Block> CREATE_WRENCHABLE = TagKey.create((ResourceKey)Registries.BLOCK, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"create", (String)"wrenchable"));
    private static final TagKey<Item> CREATE_WRENCHES = TagKey.create((ResourceKey)Registries.ITEM, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"create", (String)"wrenches"));
    private static final ResourceLocation CREATE_WRENCH_ID = ResourceLocation.fromNamespaceAndPath((String)"create", (String)"wrench");

    public NeuralContextualizerEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }

    private static boolean hasThisEffect(Player player) {
        return player.hasEffect(ModEffects.NEURAL_CONTEXTUALIZER_EFFECT);
    }

    public static void handleSwapHotbarPayload(ServerPlayer player, int slot) {
        if (player == null) {
            return;
        }
        if (!player.hasEffect(ModEffects.NEURAL_CONTEXTUALIZER_EFFECT)) {
            return;
        }
        if (slot < 0 || slot > 8) {
            return;
        }
        if (player.isSpectator()) {
            return;
        }
        player.getInventory().selected = slot;
        player.connection.send((Packet)new ClientboundSetCarriedItemPacket(slot));
        player.inventoryMenu.broadcastChanges();
    }

    @EventBusSubscriber(modid="createcybernetics", value={Dist.CLIENT}, bus=EventBusSubscriber.Bus.GAME)
    public static final class ClientLogic {
        private static int cooldown = 0;
        private static boolean lastHasEffect = false;
        private static int lastSentSlot = -1;

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            LivingEntity target;
            EntityHitResult ehr;
            Entity entity;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) {
                return;
            }
            LocalPlayer player = mc.player;
            boolean hasEffect = NeuralContextualizerEffect.hasThisEffect((Player)player);
            if (!hasEffect) {
                cooldown = 0;
                lastSentSlot = -1;
                lastHasEffect = false;
                return;
            }
            if (!lastHasEffect) {
                cooldown = 0;
                lastSentSlot = -1;
                lastHasEffect = true;
            }
            if (mc.level.getGameTime() % 2L != 0L) {
                return;
            }
            if (cooldown > 0) {
                --cooldown;
                return;
            }
            if (mc.screen != null) {
                return;
            }
            if (player.isUsingItem()) {
                return;
            }
            if (mc.options.keyShift.isDown()) {
                return;
            }
            HitResult hit = mc.hitResult;
            if (hit == null || hit.getType() == HitResult.Type.MISS) {
                return;
            }
            if (hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult) {
                BlockHitResult bhr = (BlockHitResult)hit;
                Vec3 eye = player.getEyePosition();
                Vec3 hitPos = bhr.getLocation();
                double maxSqr = 36.0;
                if (eye.distanceToSqr(hitPos) > maxSqr) {
                    return;
                }
            }
            int desiredSlot = -1;
            if (hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult) {
                int wrenchSlot;
                BlockHitResult bhr = (BlockHitResult)hit;
                BlockPos pos = bhr.getBlockPos();
                BlockState state = mc.level.getBlockState(pos);
                boolean isCreateBlock = "create".equals(BuiltInRegistries.BLOCK.getKey((Object)state.getBlock()).getNamespace());
                if ((state.is(CREATE_WRENCH_PICKUP) || state.is(CREATE_WRENCHABLE) || isCreateBlock) && (wrenchSlot = ClientLogic.findCreateWrenchHotbarSlot((Player)player)) != -1) {
                    desiredSlot = wrenchSlot;
                }
                if (desiredSlot == -1) {
                    desiredSlot = ClientLogic.findBestToolHotbarSlot((Player)player, state);
                }
            } else if (hit.getType() == HitResult.Type.ENTITY && hit instanceof EntityHitResult && (entity = (ehr = (EntityHitResult)hit).getEntity()) instanceof LivingEntity && (target = (LivingEntity)entity) instanceof Enemy) {
                desiredSlot = ClientLogic.findBestWeaponHotbarSlot((Player)player);
            }
            if (desiredSlot < 0 || desiredSlot > 8) {
                return;
            }
            int current = player.getInventory().selected;
            if (desiredSlot == current) {
                return;
            }
            if (desiredSlot == lastSentSlot) {
                return;
            }
            PacketDistributor.sendToServer((CustomPacketPayload)new SwapHotbarPayload(desiredSlot), (CustomPacketPayload[])new CustomPacketPayload[0]);
            lastSentSlot = desiredSlot;
            cooldown = 4;
        }

        private static int findCreateWrenchHotbarSlot(Player player) {
            for (int slot = 0; slot < 9; ++slot) {
                ItemStack stack = player.getInventory().getItem(slot);
                if (stack.isEmpty()) continue;
                if (stack.is(CREATE_WRENCHES)) {
                    return slot;
                }
                ResourceLocation key = BuiltInRegistries.ITEM.getKey((Object)stack.getItem());
                if (!CREATE_WRENCH_ID.equals((Object)key)) continue;
                return slot;
            }
            return -1;
        }

        private static int findBestToolHotbarSlot(Player player, BlockState state) {
            ToolFamily family = ClientLogic.toolFamilyFor(state);
            if (family == ToolFamily.NONE) {
                return -1;
            }
            int bestSlot = -1;
            float bestScore = 0.0f;
            for (int slot = 0; slot < 9; ++slot) {
                float speed;
                ItemStack stack = player.getInventory().getItem(slot);
                if (stack.isEmpty() || !family.matches(stack.getItem()) || (speed = stack.getDestroySpeed(state)) <= 1.0f) continue;
                float correctnessBonus = 0.0f;
                try {
                    if (stack.isCorrectToolForDrops(state)) {
                        correctnessBonus = 1000.0f;
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                float score = speed + correctnessBonus;
                if (!(score > bestScore)) continue;
                bestScore = score;
                bestSlot = slot;
            }
            return bestSlot;
        }

        private static int findBestWeaponHotbarSlot(Player player) {
            int swordSlot = ClientLogic.findBestTieredHotbarSlot(player, SwordItem.class);
            if (swordSlot != -1) {
                return swordSlot;
            }
            int axeSlot = ClientLogic.findBestTieredHotbarSlot(player, AxeItem.class);
            if (axeSlot != -1) {
                return axeSlot;
            }
            return -1;
        }

        private static int findBestTieredHotbarSlot(Player player, Class<? extends Item> clazz) {
            int bestSlot = -1;
            float bestScore = -3.4028235E38f;
            for (int slot = 0; slot < 9; ++slot) {
                Item item;
                ItemStack stack = player.getInventory().getItem(slot);
                if (stack.isEmpty() || !clazz.isInstance(item = stack.getItem())) continue;
                float score = 0.0f;
                if (item instanceof TieredItem) {
                    TieredItem tiered = (TieredItem)item;
                    Tier tier = tiered.getTier();
                    score += tier.getAttackDamageBonus() * 10.0f;
                    score += tier.getSpeed();
                    score += (float)tier.getUses() * 0.001f;
                }
                if (!(score > bestScore)) continue;
                bestScore = score;
                bestSlot = slot;
            }
            return bestSlot;
        }

        private static ToolFamily toolFamilyFor(BlockState state) {
            if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                return ToolFamily.PICKAXE;
            }
            if (state.is(BlockTags.MINEABLE_WITH_AXE)) {
                return ToolFamily.AXE;
            }
            if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                return ToolFamily.SHOVEL;
            }
            if (state.is(BlockTags.MINEABLE_WITH_HOE)) {
                return ToolFamily.HOE;
            }
            return ToolFamily.NONE;
        }

        private ClientLogic() {
        }

        private static enum ToolFamily {
            PICKAXE,
            AXE,
            SHOVEL,
            HOE,
            NONE;


            boolean matches(Item item) {
                return switch (this.ordinal()) {
                    default -> throw new MatchException(null, null);
                    case 0 -> item instanceof PickaxeItem;
                    case 1 -> item instanceof AxeItem;
                    case 2 -> item instanceof ShovelItem;
                    case 3 -> item instanceof HoeItem;
                    case 4 -> false;
                };
            }
        }
    }

    public record SwapHotbarPayload(int slot) implements CustomPacketPayload
    {
        public static final CustomPacketPayload.Type<SwapHotbarPayload> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"neural_swap_hotbar"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SwapHotbarPayload> STREAM_CODEC = StreamCodec.composite((StreamCodec)ByteBufCodecs.VAR_INT, SwapHotbarPayload::slot, SwapHotbarPayload::new);

        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}

