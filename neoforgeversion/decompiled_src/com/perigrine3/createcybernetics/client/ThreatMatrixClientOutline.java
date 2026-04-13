/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.ints.IntIterator
 *  it.unimi.dsi.fastutil.ints.IntOpenHashSet
 *  it.unimi.dsi.fastutil.ints.IntSet
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.Font$DisplayMode
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.OutlineBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.Enemy
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 */
package com.perigrine3.createcybernetics.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.util.ModTags;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid="createcybernetics", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class ThreatMatrixClientOutline {
    private static final IntSet TARGET_IDS = new IntOpenHashSet();
    private static boolean active = false;

    private ThreatMatrixClientOutline() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (player == null || level == null) {
            return;
        }
        active = false;
        TARGET_IDS.clear();
        if (!player.hasData(ModAttachments.CYBERWARE)) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        InstalledLoc loc = ThreatMatrixClientOutline.findInstalledMatrix(data);
        if (loc == null) {
            return;
        }
        if (!loc.stack.is(ModTags.Items.TOGGLEABLE_CYBERWARE)) {
            return;
        }
        if (!data.isEnabled(loc.slot, loc.index)) {
            return;
        }
        active = true;
        int range = 32;
        AABB box = player.getBoundingBox().inflate((double)range);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, box)) {
            if (!e.isAlive() || !(e instanceof Enemy) || e.isInvisible()) continue;
            TARGET_IDS.add(e.getId());
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }
        if (!active || TARGET_IDS.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (player == null || level == null) {
            return;
        }
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        OutlineBufferSource outlines = mc.renderBuffers().outlineBufferSource();
        long gt = level.getGameTime();
        float p = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        double periodTicks = 40.0;
        double phase = (double)((float)gt + p) / periodTicks * (Math.PI * 2);
        double pulse01 = (Math.sin(phase) + 1.0) * 0.5;
        int rMin = 160;
        int rMax = 220;
        int r = (int)Math.round((double)rMin + (double)(rMax - rMin) * pulse01);
        outlines.setColor(r, 0, 0, 255);
        PoseStack poseStack = event.getPoseStack();
        float partial = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        IntIterator intIterator = TARGET_IDS.iterator();
        while (intIterator.hasNext()) {
            LivingEntity living;
            int id = (Integer)intIterator.next();
            Entity ent = level.getEntity(id);
            if (!(ent instanceof LivingEntity) || !(living = (LivingEntity)ent).isAlive()) continue;
            double x = living.getX() - cam.x;
            double y = living.getY() - cam.y;
            double z = living.getZ() - cam.z;
            dispatcher.render((Entity)living, x, y, z, living.getYRot(), partial, poseStack, (MultiBufferSource)outlines, 0xF000F0);
        }
        outlines.endOutlineBatch();
        ThreatMatrixClientOutline.renderNames(event, r);
    }

    private static void renderNames(RenderLevelStageEvent event, int red) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (player == null || level == null) {
            return;
        }
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        Font font = mc.font;
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();
        PoseStack poseStack = event.getPoseStack();
        IntIterator intIterator = TARGET_IDS.iterator();
        while (intIterator.hasNext()) {
            LivingEntity living;
            int id = (Integer)intIterator.next();
            Entity ent = level.getEntity(id);
            if (!(ent instanceof LivingEntity) || !(living = (LivingEntity)ent).isAlive()) continue;
            Component name = living.getDisplayName();
            double x = living.getX() - cam.x;
            double y = living.getY() + (double)living.getBbHeight() + 0.35 - cam.y;
            double z = living.getZ() - cam.z;
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            poseStack.mulPose(dispatcher.cameraOrientation());
            poseStack.scale(0.025f, -0.025f, 0.025f);
            int packedColor = 0xFF000000 | (red & 0xFF) << 16;
            float bgOpacity = mc.options.getBackgroundOpacity(0.25f);
            int bg = (int)(bgOpacity * 255.0f) << 24;
            float w = font.width((FormattedText)name);
            poseStack.translate(-w / 2.0f, 0.0f, 0.0f);
            font.drawInBatch(name, 0.0f, 0.0f, packedColor, false, poseStack.last().pose(), (MultiBufferSource)buf, Font.DisplayMode.SEE_THROUGH, bg, 0xF000F0);
            poseStack.popPose();
        }
        buf.endBatch();
    }

    private static InstalledLoc findInstalledMatrix(PlayerCyberwareData data) {
        InstalledCyberware[] arr = data.getAll().get((Object)CyberwareSlot.BRAIN);
        if (arr == null) {
            return null;
        }
        Item matrixItem = (Item)ModItems.BRAINUPGRADES_MATRIX.get();
        for (int i = 0; i < arr.length; ++i) {
            ItemStack st;
            InstalledCyberware cw = arr[i];
            if (cw == null || (st = cw.getItem()) == null || st.isEmpty() || st.getItem() != matrixItem) continue;
            return new InstalledLoc(CyberwareSlot.BRAIN, i, st);
        }
        return null;
    }

    private record InstalledLoc(CyberwareSlot slot, int index, ItemStack stack) {
    }
}

