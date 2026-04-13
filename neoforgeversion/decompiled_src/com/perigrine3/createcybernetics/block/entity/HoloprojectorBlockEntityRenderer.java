/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.RemotePlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package com.perigrine3.createcybernetics.block.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.perigrine3.createcybernetics.block.entity.HoloprojectorBlockEntity;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class HoloprojectorBlockEntityRenderer
implements BlockEntityRenderer<HoloprojectorBlockEntity> {
    private final Map<UUID, RemotePlayer> hologramPlayers = new HashMap<UUID, RemotePlayer>();
    private final Map<Long, CachedEntity> hologramEntities = new HashMap<Long, CachedEntity>();
    private static final Set<String> STRIP_KEYS = Set.of("UUID", "Pos", "Motion", "Rotation", "FallDistance", "Fire", "Air", "OnGround", "PortalCooldown", "Dimension", "Passengers", "Vehicle", "Leash", "Tags", "Brain", "HandItems", "HandDropChances", "ArmorItems", "ArmorDropChances", "Inventory", "Items", "Offers");

    public HoloprojectorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    public void render(HoloprojectorBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (be == null) {
            return;
        }
        if (be.getLevel() == null) {
            return;
        }
        switch (be.getMode()) {
            case ITEM: {
                this.renderItem(be, partialTick, poseStack, buffer, packedLight, packedOverlay);
                break;
            }
            case PLAYER: {
                this.renderPlayer(be, partialTick, poseStack, buffer, packedLight);
                break;
            }
            case ENTITY: {
                this.renderEntity(be, partialTick, poseStack, buffer, packedLight);
                break;
            }
        }
    }

    private void renderItem(HoloprojectorBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack stack = be.getProjectedStack();
        if (stack.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        float alpha = Mth.clamp((float)be.getItemAlpha(), (float)0.0f, (float)1.0f);
        if (alpha <= 0.001f) {
            return;
        }
        long time = be.getLevel().getGameTime();
        float rot = ((float)time + partialTick) * 4.0f;
        float bob = Mth.sin((float)(((float)time + partialTick) * 0.1f)) * 0.03f;
        ItemRenderer itemRenderer = mc.getItemRenderer();
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5 + (double)bob, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));
        float scale = 1.25f;
        poseStack.scale(scale, scale, scale);
        AlphaMultiBufferSource alphaBuffer = new AlphaMultiBufferSource(buffer, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, (MultiBufferSource)alphaBuffer, (Level)mc.level, 0);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private void renderPlayer(HoloprojectorBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        UUID uuid = be.getProjectedPlayerUuid();
        if (uuid == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return;
        }
        float alpha = Mth.clamp((float)be.getPlayerAlpha(), (float)0.0f, (float)1.0f);
        if (alpha <= 0.001f) {
            return;
        }
        String desiredName = be.getProjectedPlayerName() == null || be.getProjectedPlayerName().isBlank() ? "Hologram" : be.getProjectedPlayerName();
        RemotePlayer hologram = this.hologramPlayers.get(uuid);
        if (hologram == null || !desiredName.equals(hologram.getGameProfile().getName())) {
            hologram = new RemotePlayer(level, new GameProfile(uuid, desiredName));
            this.hologramPlayers.put(uuid, hologram);
        }
        HoloprojectorBlockEntityRenderer.forceOuterLayersOn((Player)hologram, (Player)mc.player);
        hologram.tickCount = (int)level.getGameTime();
        CompoundTag snap = be.getProjectedCyberwareSnapshot();
        CompoundTag pd = hologram.getPersistentData();
        if (snap != null && !snap.isEmpty()) {
            pd.putBoolean("cc_holo_snapshot", true);
            pd.put("cc_holo_snapshot_cyberware", (Tag)snap.copy());
        } else {
            pd.remove("cc_holo_snapshot");
            pd.remove("cc_holo_snapshot_cyberware");
        }
        long time = level.getGameTime();
        float yaw = ((float)time + partialTick) * 2.5f;
        yaw = Mth.wrapDegrees((float)yaw);
        hologram.setYRot(yaw);
        hologram.setXRot(0.0f);
        hologram.yRotO = yaw;
        hologram.xRotO = 0.0f;
        hologram.yBodyRot = yaw;
        hologram.yBodyRotO = yaw;
        hologram.yHeadRot = yaw;
        hologram.yHeadRotO = yaw;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        float bob = Mth.sin((float)(((float)time + partialTick) * 0.08f)) * 0.02f;
        poseStack.translate(0.0, (double)bob, 0.0);
        float scale = 0.9f;
        poseStack.scale(scale, scale, scale);
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        AlphaMultiBufferSource alphaBuffer = new AlphaMultiBufferSource(buffer, alpha);
        dispatcher.render((Entity)hologram, 0.0, 0.0, 0.0, yaw, partialTick, poseStack, (MultiBufferSource)alphaBuffer, packedLight);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private void renderEntity(HoloprojectorBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Entity hologram;
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) {
            return;
        }
        float alpha = Mth.clamp((float)be.getPlayerAlpha(), (float)0.0f, (float)1.0f);
        if (alpha <= 0.001f) {
            return;
        }
        String typeRaw = be.getProjectedEntityTypeId();
        if (typeRaw == null || typeRaw.isBlank()) {
            return;
        }
        ResourceLocation typeId = ResourceLocation.tryParse((String)typeRaw);
        if (typeId == null) {
            return;
        }
        EntityType type = (EntityType)BuiltInRegistries.ENTITY_TYPE.get(typeId);
        if (type == null) {
            return;
        }
        long key = be.getBlockPos().asLong();
        int payloadHash = 31 * typeRaw.hashCode() + (be.getProjectedEntityName() != null ? be.getProjectedEntityName().hashCode() : 0) + (be.getProjectedEntityNbt() != null ? be.getProjectedEntityNbt().hashCode() : 0);
        CachedEntity cached = this.hologramEntities.get(key);
        if (cached == null || cached.entity == null || cached.entity.level() != level || cached.type != type || cached.payloadHash != payloadHash) {
            Entity e = type.create((Level)level);
            if (e == null) {
                return;
            }
            e.setCustomName(null);
            e.setCustomNameVisible(false);
            CompoundTag nbt = be.getProjectedEntityNbt();
            if (nbt != null && !nbt.isEmpty()) {
                CompoundTag safe = nbt.copy();
                HoloprojectorBlockEntityRenderer.sanitizeEntityTagClient(safe);
                try {
                    e.load(safe);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            cached = new CachedEntity(e, type, payloadHash);
            this.hologramEntities.put(key, cached);
        }
        if ((hologram = cached.entity) == null) {
            return;
        }
        hologram.setCustomName(null);
        hologram.setCustomNameVisible(false);
        hologram.tickCount = (int)level.getGameTime();
        long time = level.getGameTime();
        float yaw = ((float)time + partialTick) * 2.5f;
        yaw = Mth.wrapDegrees((float)yaw);
        hologram.setYRot(yaw);
        hologram.yRotO = yaw;
        if (hologram instanceof LivingEntity) {
            LivingEntity le = (LivingEntity)hologram;
            le.yBodyRot = yaw;
            le.yBodyRotO = yaw;
            le.yHeadRot = yaw;
            le.yHeadRotO = yaw;
            le.setXRot(0.0f);
            le.xRotO = 0.0f;
        }
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        float bob = Mth.sin((float)(((float)time + partialTick) * 0.08f)) * 0.02f;
        poseStack.translate(0.0, (double)bob, 0.0);
        float scale = 0.9f;
        poseStack.scale(scale, scale, scale);
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        EntityHologramBufferSource alphaBuffer = new EntityHologramBufferSource(buffer, dispatcher, hologram, alpha);
        dispatcher.render(hologram, 0.0, 0.0, 0.0, yaw, partialTick, poseStack, (MultiBufferSource)alphaBuffer, packedLight);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private static void sanitizeEntityTagClient(CompoundTag tag) {
        for (String k : STRIP_KEYS) {
            tag.remove(k);
        }
    }

    private static void forceOuterLayersOn(Player hologram, @Nullable Player copyFrom) {
        try {
            Field f = null;
            Class<Player> cls = Player.class;
            for (String name : new String[]{"DATA_PLAYER_MODE_CUSTOMISATION", "DATA_PLAYER_MODE_CUSTOMIZATION", "DATA_PLAYER_MODE_CUSTOMISATION_ID", "DATA_PLAYER_MODE_CUSTOMIZATION_ID"}) {
                try {
                    f = cls.getDeclaredField(name);
                    break;
                }
                catch (NoSuchFieldException noSuchFieldException) {
                }
            }
            if (f == null) {
                return;
            }
            f.setAccessible(true);
            Object accessorObj = f.get(null);
            if (!(accessorObj instanceof EntityDataAccessor)) {
                return;
            }
            EntityDataAccessor accessor = (EntityDataAccessor)accessorObj;
            byte mask = 127;
            if (copyFrom != null) {
                try {
                    Object val = copyFrom.getEntityData().get(accessor);
                    if (val instanceof Byte) {
                        Byte b = (Byte)val;
                        mask = b;
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            hologram.getEntityData().set(accessor, (Object)mask);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public boolean shouldRenderOffScreen(HoloprojectorBlockEntity be) {
        return true;
    }

    private static final class AlphaMultiBufferSource
    implements MultiBufferSource {
        private final MultiBufferSource delegate;
        private final float alphaMul;

        private AlphaMultiBufferSource(MultiBufferSource delegate, float alphaMul) {
            this.delegate = delegate;
            this.alphaMul = alphaMul;
        }

        public VertexConsumer getBuffer(RenderType type) {
            RenderType mapped = AlphaMultiBufferSource.mapToTranslucentIfNeeded(type);
            return new AlphaVertexConsumer(this.delegate.getBuffer(mapped), this.alphaMul);
        }

        private static RenderType mapToTranslucentIfNeeded(RenderType type) {
            if (type == RenderType.solid() || type == RenderType.cutout() || type == RenderType.cutoutMipped() || type == RenderType.tripwire()) {
                return RenderType.translucent();
            }
            return type;
        }
    }

    private static final class CachedEntity {
        final Entity entity;
        final EntityType<?> type;
        final int payloadHash;

        private CachedEntity(Entity entity, EntityType<?> type, int payloadHash) {
            this.entity = entity;
            this.type = type;
            this.payloadHash = payloadHash;
        }
    }

    private static final class EntityHologramBufferSource
    implements MultiBufferSource {
        private final MultiBufferSource delegate;
        private final float alphaMul;
        @Nullable
        private final RenderType solid;
        @Nullable
        private final RenderType cutout;
        @Nullable
        private final RenderType cutoutNoCull;
        @Nullable
        private final RenderType cutoutNoCullZOffset;
        @Nullable
        private final RenderType translucent;

        private EntityHologramBufferSource(MultiBufferSource delegate, EntityRenderDispatcher dispatcher, Entity entity, float alphaMul) {
            ResourceLocation tex;
            this.delegate = delegate;
            this.alphaMul = alphaMul;
            try {
                tex = dispatcher.getRenderer(entity).getTextureLocation(entity);
            }
            catch (Throwable t) {
                tex = null;
            }
            if (tex != null) {
                this.solid = RenderType.entitySolid((ResourceLocation)tex);
                this.cutout = RenderType.entityCutout((ResourceLocation)tex);
                this.cutoutNoCull = RenderType.entityCutoutNoCull((ResourceLocation)tex);
                this.cutoutNoCullZOffset = RenderType.entityCutoutNoCullZOffset((ResourceLocation)tex);
                this.translucent = RenderType.entityTranslucent((ResourceLocation)tex);
            } else {
                this.solid = null;
                this.cutout = null;
                this.cutoutNoCull = null;
                this.cutoutNoCullZOffset = null;
                this.translucent = null;
            }
        }

        public VertexConsumer getBuffer(RenderType type) {
            RenderType mapped = this.mapEntityBasePassToTranslucent(type);
            return new AlphaVertexConsumer(this.delegate.getBuffer(mapped), this.alphaMul);
        }

        private RenderType mapEntityBasePassToTranslucent(RenderType type) {
            if (this.translucent == null) {
                return type;
            }
            if (type == this.solid || type == this.cutout || type == this.cutoutNoCull || type == this.cutoutNoCullZOffset) {
                return this.translucent;
            }
            return type;
        }
    }

    private static final class AlphaVertexConsumer
    implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float alphaMul;

        private AlphaVertexConsumer(VertexConsumer delegate, float alphaMul) {
            this.delegate = delegate;
            this.alphaMul = alphaMul;
        }

        public VertexConsumer addVertex(float x, float y, float z) {
            return this.delegate.addVertex(x, y, z);
        }

        public VertexConsumer setColor(int r, int g, int b, int a) {
            int a2 = Mth.clamp((int)((int)((float)a * this.alphaMul)), (int)0, (int)255);
            return this.delegate.setColor(r, g, b, a2);
        }

        public VertexConsumer setUv(float u, float v) {
            return this.delegate.setUv(u, v);
        }

        public VertexConsumer setUv1(int u, int v) {
            return this.delegate.setUv1(u, v);
        }

        public VertexConsumer setUv2(int u, int v) {
            return this.delegate.setUv2(u, v);
        }

        public VertexConsumer setNormal(float x, float y, float z) {
            return this.delegate.setNormal(x, y, z);
        }
    }
}

