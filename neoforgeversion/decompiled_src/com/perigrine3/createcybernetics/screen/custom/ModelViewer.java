/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  org.joml.Quaternionf
 *  org.joml.Vector3f
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.screen.custom.RobosurgeonScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModelViewer {
    private float rotation = 180.0f;
    private float spinVelocity = 0.0f;
    private float introScale = 0.0f;
    private int itemTick = 0;
    private final int itemDisplayTime = 20;
    private boolean dragging = false;
    private int lastMouseX = 0;
    private static final float FRICTION = 0.92f;
    private final ItemStack renderSkin = new ItemStack((ItemLike)ModItems.BODYPART_SKIN.get());
    private final ItemStack renderMuscle = new ItemStack((ItemLike)ModItems.BODYPART_MUSCLE.get());
    private final ItemStack renderBone = new ItemStack((ItemLike)Items.BONE);

    public void beginDrag(double mouseX) {
        this.dragging = true;
        this.lastMouseX = (int)mouseX;
    }

    public void endDrag() {
        this.dragging = false;
    }

    public void updateRotation(int mouseX) {
        if (this.dragging) {
            int dx = mouseX - this.lastMouseX;
            this.spinVelocity = (float)dx * 1.2f;
            this.rotation += this.spinVelocity;
            this.lastMouseX = mouseX;
        } else {
            this.spinVelocity *= 0.92f;
            this.rotation += this.spinVelocity;
            if (Math.abs(this.spinVelocity) < 0.1f) {
                this.rotation += 0.3f;
            }
        }
    }

    public Quaternionf getSpinQuaternion() {
        return new Quaternionf().rotateX((float)Math.toRadians(180.0)).rotateY((float)Math.toRadians(this.rotation));
    }

    public void triggerZoomReset() {
        this.introScale = 0.4f;
        this.spinVelocity = 2.0f;
    }

    public float getRotationPhase() {
        float phase = this.rotation % 360.0f / 360.0f;
        phase = Math.abs(2.0f * phase - 1.0f);
        return phase * (0.92f + phase * 0.05f);
    }

    public void render(GuiGraphics gui, int modelX, int modelY, int baseScale, Player player, RobosurgeonScreen.ViewMode viewMode) {
        gui.enableScissor(modelX - 78, modelY - 85, modelX + 72, modelY + 75);
        if (this.introScale < 1.0f) {
            this.introScale += (1.0f - this.introScale) * 0.1f;
            if (this.introScale > 1.0f) {
                this.introScale = 1.0f;
            }
        }
        float slideY = (1.0f - this.introScale) * 20.0f;
        Quaternionf spin = new Quaternionf().rotateX((float)Math.toRadians(180.0)).rotateY((float)Math.toRadians(this.rotation));
        float b1 = player.yBodyRot;
        float b2 = player.yBodyRotO;
        float h1 = player.yHeadRot;
        float h2 = player.yHeadRotO;
        float yaw = player.getYRot();
        float yawO = player.yRotO;
        float pitch = player.getXRot();
        float pitchO = player.xRotO;
        player.yBodyRotO = 180.0f;
        player.yBodyRot = 180.0f;
        player.yHeadRotO = 180.0f;
        player.yHeadRot = 180.0f;
        player.setYRot(180.0f);
        player.yRotO = 180.0f;
        player.setXRot(0.0f);
        player.xRotO = 0.0f;
        float entityScale = player.getScale();
        if (!Float.isFinite(entityScale) || entityScale <= 0.0f) {
            entityScale = 1.0f;
        }
        float counterScale = 1.0f / entityScale;
        float uiScale = this.introScale * counterScale;
        uiScale = Mth.clamp((float)uiScale, (float)1.0E-4f, (float)1000.0f);
        int entityRenderScale = baseScale;
        gui.pose().pushPose();
        gui.pose().translate((float)modelX, (float)modelY + slideY, 0.0f);
        gui.pose().scale(uiScale, uiScale, 1.0f);
        gui.pose().translate((float)(-modelX), (float)(-modelY), 0.0f);
        InventoryScreen.renderEntityInInventory((GuiGraphics)gui, (float)modelX, (float)modelY, (float)entityRenderScale, (Vector3f)new Vector3f(), (Quaternionf)spin, null, (LivingEntity)player);
        gui.pose().popPose();
        ++this.itemTick;
        ItemStack[] itemsToCycle = new ItemStack[]{this.renderSkin, this.renderMuscle, this.renderBone};
        int currentIndex = this.itemTick / 20 % itemsToCycle.length;
        ItemStack currentItem = itemsToCycle[currentIndex];
        gui.pose().pushPose();
        int itemX = modelX + 43;
        int itemY = modelY - 57;
        gui.pose().translate((float)itemX, (float)itemY, 100.0f);
        float itemScale = 1.75f;
        gui.pose().scale(itemScale, itemScale, 1.0f);
        gui.renderItem(currentItem, 0, 0);
        gui.pose().popPose();
        player.yBodyRot = b1;
        player.yBodyRotO = b2;
        player.yHeadRot = h1;
        player.yHeadRotO = h2;
        player.setYRot(yaw);
        player.yRotO = yawO;
        player.setXRot(pitch);
        player.xRotO = pitchO;
        gui.disableScissor();
    }
}

