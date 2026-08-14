/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Lighting
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.narration.NarrationElementOutput
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.model.Model
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.items.ItemStackHandler
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Matrix4f
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 */
package com.maxwell.cyber_ware_port.client.screen.robosurgeon;

import com.maxwell.cyber_ware_port.CyberWare;
import com.maxwell.cyber_ware_port.api.json.CyberwareAPI;
import com.maxwell.cyber_ware_port.client.model.PlayerInternalPartsModel;
import com.maxwell.cyber_ware_port.client.model.SkeletonDisplayModel;
import com.maxwell.cyber_ware_port.client.screen.robosurgeon.InstalledCyberwareScreen;
import com.maxwell.cyber_ware_port.common.block.robosurgeon.RobosurgeonBlockEntity;
import com.maxwell.cyber_ware_port.common.capability.CyberwareCapabilityProvider;
import com.maxwell.cyber_ware_port.common.capability.CyberwareUserData;
import com.maxwell.cyber_ware_port.common.container.RobosurgeonMenu;
import com.maxwell.cyber_ware_port.common.item.base.BodyPartType;
import com.maxwell.cyber_ware_port.common.item.base.ICyberware;
import com.maxwell.cyber_ware_port.common.network.SurgeryGhostTogglePacket;
import com.maxwell.cyber_ware_port.common.risk.SurgeryAlert;
import com.maxwell.cyber_ware_port.common.risk.SurgeryAnalyzer;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public class RobosurgeonScreen
extends AbstractContainerScreen<RobosurgeonMenu> {
    private static final ResourceLocation INTERNAL_PARTS_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/player_internal_part.png");
    private static final ResourceLocation SKELETON_TEXTURE = ResourceLocation.withDefaultNamespace((String)"textures/entity/skeleton/skeleton.png");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/surgery.png");
    private static final ResourceLocation MARKER_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/marker.png");
    private static final ResourceLocation RED_SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/red_slot.png");
    private static final ResourceLocation BLUE_SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/blue_slot.png");
    private static final ResourceLocation ALERT_ICON = ResourceLocation.fromNamespaceAndPath((String)"cyber_ware_port", (String)"textures/gui/risk_icons.png");
    private static final float ANIMATION_DURATION = 2000.0f;
    private static final int SLOT_SIZE = 18;
    private static final int SLOT_SPACING = 2;
    private static final int GUI_WIDTH = 175;
    private static final int TOP_HEIGHT = 131;
    private static final int BOTTOM_HEIGHT = 91;
    private static final int TEXTURE_INVENTORY_START_Y = 131;
    private static final float BASE_SCALE = 45.0f;
    private static final Field slotX;
    private static final Field slotY;
    private PlayerInternalPartsModel internalPartsModel;
    private BodyPart selectedPart = BodyPart.NONE;
    private TargetMarker selectedMarker = null;
    private SkeletonDisplayModel skeletonModel;
    private boolean isDraggingModel = false;
    private float viewRotation = 0.0f;
    private double dragStartX = 0.0;
    private float rotationStart = 0.0f;
    private boolean potentialDrag = false;
    private long startTime;
    private AbstractWidget installedListButton;
    private float currentScale = 45.0f;
    private float currentOffsetX = 0.0f;
    private float currentOffsetY = 0.0f;
    private boolean hideName = false;

    public RobosurgeonScreen(RobosurgeonMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super((AbstractContainerMenu)pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 175;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
        this.titleLabelY = 6;
    }

    public static void renderEntityWithRotation(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, float rotationYaw, LivingEntity pEntity) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate((float)pX, (float)pY, 50.0f);
        pGuiGraphics.pose().mulPose(new Matrix4f().scaling((float)pScale, (float)pScale, (float)(-pScale)));
        Quaternionf quaternionf = Axis.ZP.rotationDegrees(180.0f);
        Quaternionf rotation = Axis.YP.rotationDegrees(rotationYaw + 180.0f);
        quaternionf.mul((Quaternionfc)rotation);
        pGuiGraphics.pose().mulPose(quaternionf);
        float f2 = pEntity.yBodyRot;
        float f3 = pEntity.getYRot();
        float f4 = pEntity.getXRot();
        float f5 = pEntity.yHeadRotO;
        float f6 = pEntity.yHeadRot;
        pEntity.yBodyRot = 0.0f;
        pEntity.setYRot(0.0f);
        pEntity.setXRot(0.0f);
        pEntity.yHeadRot = 0.0f;
        pEntity.yHeadRotO = 0.0f;
        float originalLimbSwingAmount = pEntity.walkAnimation.speed();
        pEntity.walkAnimation.setSpeed(0.0f);
        pEntity.attackAnim = 0.0f;
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        rotation.conjugate();
        entityrenderdispatcher.overrideCameraOrientation(rotation);
        entityrenderdispatcher.setRenderShadow(false);
        entityrenderdispatcher.render((Entity)pEntity, 0.0, 0.0, 0.0, 0.0f, 1.0f, pGuiGraphics.pose(), (MultiBufferSource)pGuiGraphics.bufferSource(), 0xF000F0);
        pGuiGraphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        pEntity.yBodyRot = f2;
        pEntity.setYRot(f3);
        pEntity.setXRot(f4);
        pEntity.yHeadRotO = f5;
        pEntity.yHeadRot = f6;
        pEntity.walkAnimation.setSpeed(originalLimbSwingAmount);
        pGuiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    private static int[] slots(int start) {
        int[] slots = new int[9];
        for (int i = 0; i < 9; ++i) {
            slots[i] = start + i;
        }
        return slots;
    }

    public static void renderCustomModel(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, float rotationYaw, Model pModel, ResourceLocation texture) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate((float)pX, (float)pY, 50.0f);
        pGuiGraphics.pose().mulPose(new Matrix4f().scaling((float)pScale, (float)pScale, (float)(-pScale)));
        Quaternionf quaternionf = Axis.ZP.rotationDegrees(180.0f);
        Quaternionf rotation = Axis.YP.rotationDegrees(rotationYaw + 180.0f);
        quaternionf.mul((Quaternionfc)rotation);
        pGuiGraphics.pose().mulPose(quaternionf);
        Lighting.setupForEntityInInventory();
        VertexConsumer vertexConsumer = pGuiGraphics.bufferSource().getBuffer(pModel.renderType(texture));
        pModel.renderToBuffer(pGuiGraphics.pose(), vertexConsumer, 0xF000F0, OverlayTexture.NO_OVERLAY);
        pGuiGraphics.flush();
        pGuiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    private void setSlotPos(Slot slot, int x, int y) {
        try {
            slotX.set(slot, x);
            slotY.set(slot, y);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void init() {
        super.init();
        if (this.minecraft != null) {
            this.skeletonModel = new SkeletonDisplayModel(this.minecraft.getEntityModels().bakeLayer(SkeletonDisplayModel.LAYER_LOCATION));
            this.internalPartsModel = new PlayerInternalPartsModel(this.minecraft.getEntityModels().bakeLayer(PlayerInternalPartsModel.LAYER_LOCATION));
        }
        this.startTime = System.currentTimeMillis();
        int listBtnX = this.leftPos + 158;
        int listBtnY = this.topPos + 4;
        this.installedListButton = new AbstractWidget(listBtnX, listBtnY, 10, 10, (Component)Component.empty()){

            public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                guiGraphics.blit(TEXTURE, this.getX(), this.getY(), 176.0f, 122.0f, 10, 10, 256, 256);
                if (this.isHovered()) {
                    guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x50FFFFFF);
                    guiGraphics.renderTooltip(RobosurgeonScreen.this.font, (Component)Component.translatable((String)"gui.cyber_ware_port.button.view_installed"), mouseX, mouseY);
                }
            }

            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().setScreen((Screen)new InstalledCyberwareScreen((Screen)RobosurgeonScreen.this));
            }

            protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
                this.defaultButtonNarrationText(narrationElementOutput);
            }
        };
        this.addRenderableWidget((GuiEventListener)this.installedListButton);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        long elapsedTime = System.currentTimeMillis() - this.startTime;
        if ((float)elapsedTime < 2000.0f) {
            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }
        this.updateSlotPositions();
        if (!((RobosurgeonMenu)this.menu).getCarried().isEmpty()) {
            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }
        if ((pButton == 0 || pButton == 1) && this.selectedMarker != null) {
            Slot hoveredSlot = null;
            for (Slot slot : ((RobosurgeonMenu)this.menu).slots) {
                if (slot.x > 10000 || slot.y > 10000) continue;
                int slotLeft = this.leftPos + slot.x;
                int slotTop = this.topPos + slot.y;
                if (!(pMouseX >= (double)slotLeft) || !(pMouseX < (double)(slotLeft + 16)) || !(pMouseY >= (double)slotTop) || !(pMouseY < (double)(slotTop + 16))) continue;
                hoveredSlot = slot;
                break;
            }
            if (hoveredSlot != null && hoveredSlot.index < RobosurgeonBlockEntity.TOTAL_SLOTS) {
                if (hoveredSlot.hasItem() && ((Boolean)hoveredSlot.getItem().getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false)).booleanValue()) {
                    PacketDistributor.sendToServer((CustomPacketPayload)new SurgeryGhostTogglePacket(((RobosurgeonMenu)this.menu).blockEntity.getBlockPos(), hoveredSlot.index), (CustomPacketPayload[])new CustomPacketPayload[0]);
                    hoveredSlot.set(ItemStack.EMPTY);
                    Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value()), (float)1.0f));
                    return true;
                }
                if (!hoveredSlot.hasItem()) {
                    PacketDistributor.sendToServer((CustomPacketPayload)new SurgeryGhostTogglePacket(((RobosurgeonMenu)this.menu).blockEntity.getBlockPos(), hoveredSlot.index), (CustomPacketPayload[])new CustomPacketPayload[0]);
                    Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value()), (float)1.0f));
                    return true;
                }
            }
        }
        if (pButton == 0) {
            if (this.selectedPart != BodyPart.NONE && this.selectedMarker == null) {
                int modelCenterX = (int)((float)(this.leftPos + 88) + this.currentOffsetX);
                int modelCenterY = (int)((float)(this.topPos + 131 - 15) + this.currentOffsetY);
                if (this.selectedPart == BodyPart.INTERNAL) {
                    modelCenterX -= 48;
                    modelCenterY += 26;
                }
                float radRot = (float)Math.toRadians(this.viewRotation);
                float sin = (float)Math.sin(radRot);
                float cos = (float)Math.cos(radRot);
                float scaleFactor = this.currentScale * 0.065f;
                for (TargetMarker marker : this.selectedPart.markers) {
                    float screenOffsetX = marker.modelX() * cos - marker.modelZ() * sin;
                    int markerX = modelCenterX + (int)(screenOffsetX * scaleFactor) - 8;
                    int markerY = modelCenterY - (int)(marker.modelY() * scaleFactor) - 8;
                    if (!(pMouseX >= (double)markerX) || !(pMouseX < (double)(markerX + 16)) || !(pMouseY >= (double)markerY) || !(pMouseY < (double)(markerY + 16))) continue;
                    this.selectedMarker = marker;
                    Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value()), (float)1.2f));
                    return true;
                }
            }
            if (this.selectedPart == BodyPart.NONE) {
                int x = (this.width - this.imageWidth) / 2;
                int y = (this.height - this.imageHeight) / 2;
                int subBaseX = x + 40;
                int subBaseY = y + 131 - 55;
                if (pMouseX >= (double)subBaseX - 18.5 && pMouseX <= (double)subBaseX + 18.5 && pMouseY >= (double)subBaseY - 18.5 && pMouseY <= (double)subBaseY + 18.5) {
                    this.selectedPart = BodyPart.INTERNAL;
                    this.hideName = true;
                    Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value()), (float)1.0f));
                    return true;
                }
            }
            if (((RobosurgeonMenu)this.menu).getCarried().isEmpty() && this.getSlotUnderMouse() == null && pMouseX >= (double)this.leftPos && pMouseX < (double)(this.leftPos + this.imageWidth) && pMouseY >= (double)this.topPos && pMouseY < (double)(this.topPos + 131)) {
                this.potentialDrag = true;
                this.dragStartX = pMouseX;
                return true;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.potentialDrag && pButton == 0) {
            if (!this.isDraggingModel) {
                this.isDraggingModel = true;
                this.rotationStart = this.viewRotation;
            }
            this.viewRotation = this.rotationStart + (float)(pMouseX - this.dragStartX);
            return true;
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (pButton != 0) {
            return super.mouseReleased(pMouseX, pMouseY, pButton);
        }
        if (this.isDraggingModel) {
            this.isDraggingModel = false;
            this.potentialDrag = false;
            return true;
        }
        if (this.potentialDrag) {
            boolean partClicked = false;
            if (this.selectedPart == BodyPart.NONE) {
                int entityX = this.leftPos + 88;
                int entityY = this.topPos + 131 - 15;
                for (BodyPart part : BodyPart.values()) {
                    if (part == BodyPart.NONE || !(pMouseX >= (double)(entityX + part.hitX) - (double)part.hitW / 2.0) || !(pMouseX <= (double)(entityX + part.hitX) + (double)part.hitW / 2.0) || !(pMouseY >= (double)(entityY + part.hitY) - (double)part.hitH / 2.0) || !(pMouseY <= (double)(entityY + part.hitY) + (double)part.hitH / 2.0)) continue;
                    this.selectedPart = part;
                    this.hideName = true;
                    Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value()), (float)1.0f));
                    partClicked = true;
                    break;
                }
            }
            if (!partClicked && this.selectedPart != BodyPart.NONE) {
                this.selectedPart = BodyPart.NONE;
                this.selectedMarker = null;
                this.hideName = false;
            }
            this.potentialDrag = false;
            return true;
        }
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        SurgeryAlert alert;
        this.updateSlotPositions();
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        int maxTolerance = 100;
        if (this.minecraft != null && this.minecraft.player != null) {
            maxTolerance = ((CyberwareUserData)this.minecraft.player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).getMaxTolerance((LivingEntity)this.minecraft.player);
        }
        if ((alert = SurgeryAnalyzer.check((List<Slot>)((RobosurgeonMenu)this.menu).slots, maxTolerance)) != null) {
            int iconX = this.leftPos + 155;
            int iconY = this.topPos + 20;
            pGuiGraphics.blit(ALERT_ICON, iconX, iconY, 0.0f, 0.0f, 16, 16, 16, 16);
            if (pMouseX >= iconX && pMouseX < iconX + 16 && pMouseY >= iconY && pMouseY < iconY + 16) {
                pGuiGraphics.renderTooltip(this.font, alert.message(), pMouseX, pMouseY);
            }
        }
        if (this.selectedPart != BodyPart.NONE && this.selectedMarker == null) {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0.0f, 0.0f, 100.0f);
            int modelCenterX = (int)((float)(this.leftPos + 88) + this.currentOffsetX);
            int modelCenterY = (int)((float)(this.topPos + 131 - 15) + this.currentOffsetY);
            if (this.selectedPart == BodyPart.INTERNAL) {
                modelCenterX -= 48;
                modelCenterY += 26;
            }
            float radRot = (float)Math.toRadians(this.viewRotation);
            float sin = (float)Math.sin(radRot);
            float cos = (float)Math.cos(radRot);
            float scaleFactor = this.currentScale * 0.065f;
            RenderSystem.enableBlend();
            pGuiGraphics.setColor(1.0f, 1.0f, 1.0f, 0.8f);
            for (TargetMarker marker : this.selectedPart.markers) {
                float screenOffsetX = marker.modelX() * cos - marker.modelZ() * sin;
                int markerX = modelCenterX + (int)(screenOffsetX * scaleFactor) - 8;
                int markerY = modelCenterY - (int)(marker.modelY() * scaleFactor) - 8;
                pGuiGraphics.blit(MARKER_TEXTURE, markerX, markerY, 0.0f, 0.0f, 16, 16, 16, 16);
                if (pMouseX < markerX || pMouseX >= markerX + 16 || pMouseY < markerY || pMouseY >= markerY + 16) continue;
                pGuiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                pGuiGraphics.renderTooltip(this.font, marker.name(), pMouseX, pMouseY);
                pGuiGraphics.fill(markerX, markerY, markerX + 16, markerY + 16, 0x50FFFFFF);
                pGuiGraphics.setColor(1.0f, 1.0f, 1.0f, 0.8f);
            }
            pGuiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();
            pGuiGraphics.pose().popPose();
        }
        if (this.selectedMarker != null) {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0.0f, 0.0f, 100.0f);
            int slotCount = this.selectedMarker.relatedSlots().length;
            int uiWidth = 18 * slotCount + 2 * (slotCount - 1);
            int uiX = this.leftPos + (this.imageWidth - uiWidth) / 2;
            int installedY = this.topPos + 80;
            int stagingY = this.topPos + 105;
            RenderSystem.enableBlend();
            for (int i = 0; i < slotCount; ++i) {
                int slotX = uiX + i * 20;
                pGuiGraphics.blit(BLUE_SLOT_TEXTURE, slotX - 1, stagingY - 1, 0.0f, 0.0f, 18, 18, 18, 18);
                pGuiGraphics.blit(RED_SLOT_TEXTURE, slotX - 1, installedY - 2, 0.0f, 0.0f, 18, 18, 18, 18);
            }
            RenderSystem.disableBlend();
            if (this.minecraft != null && this.minecraft.player != null) {
                CyberwareUserData cyberware = (CyberwareUserData)this.minecraft.player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
                ItemStackHandler installed = cyberware.getInstalledCyberware();
                for (int i = 0; i < slotCount; ++i) {
                    int slotId = this.selectedMarker.relatedSlots()[i];
                    int itemX = uiX + i * 20;
                    if (slotId >= installed.getSlots()) continue;
                    ItemStack installedStack = installed.getStackInSlot(slotId);
                    pGuiGraphics.renderItem(installedStack, itemX, installedY - 1);
                    pGuiGraphics.renderItemDecorations(this.font, installedStack, itemX, installedY - 1);
                    if (!((RobosurgeonMenu)this.menu).getSlot(slotId).getItem().isEmpty() || installedStack.isEmpty()) continue;
                    pGuiGraphics.pose().pushPose();
                    pGuiGraphics.pose().translate(0.0f, 0.0f, 150.0f);
                    pGuiGraphics.renderItem(installedStack, itemX, stagingY);
                    pGuiGraphics.fill(itemX, stagingY, itemX + 16, stagingY + 16, Integer.MIN_VALUE);
                    pGuiGraphics.pose().popPose();
                }
            }
            this.renderGhostConflict(pGuiGraphics, slotCount, uiX, stagingY, installedY);
            pGuiGraphics.pose().popPose();
        }
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(@NotNull GuiGraphics g, float partial, int mouseX, int mouseY) {
        float currentRotation;
        long elapsed;
        float ease;
        int x = this.leftPos;
        int y = this.topPos;
        g.blit(TEXTURE, x, y, 0, 0, 175, 131);
        g.blit(TEXTURE, x, y + 131, 0, 131, 175, 91);
        int maxEssence = 100;
        int currentEssence = 0;
        CyberwareUserData data = null;
        if (this.minecraft.player != null) {
            data = (CyberwareUserData)this.minecraft.player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
            maxEssence = data.getMaxTolerance((LivingEntity)this.minecraft.player);
            currentEssence = data.getTolerance((LivingEntity)this.minecraft.player);
        }
        int barX = x + 5;
        int barY = y + 4;
        int barW = 8;
        int barH = 48;
        g.blit(TEXTURE, barX, barY, 211.0f, 61.0f, barW, barH, 256, 256);
        if (data != null) {
            int futureEssence = this.getProjectedFutureEssence(data);
            if (futureEssence < currentEssence) {
                flash = 0.5f + 0.3f * (float)Math.sin((double)((float)(System.currentTimeMillis() % 1000L) / 1000.0f * 2.0f) * Math.PI);
                g.setColor(1.0f, 0.6f, 0.0f, flash);
                this.drawEssenceBar(g, currentEssence, maxEssence, barX, barY, barW, barH);
                g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                this.drawEssenceBar(g, futureEssence, maxEssence, barX, barY, barW, barH);
            } else if (futureEssence > currentEssence) {
                flash = 0.5f + 0.3f * (float)Math.sin((double)((float)(System.currentTimeMillis() % 1000L) / 1000.0f * 2.0f) * Math.PI);
                g.setColor(0.2f, 1.0f, 0.2f, flash);
                this.drawEssenceBar(g, futureEssence, maxEssence, barX, barY, barW, barH);
                g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                this.drawEssenceBar(g, currentEssence, maxEssence, barX, barY, barW, barH);
            } else {
                this.drawEssenceBar(g, currentEssence, maxEssence, barX, barY, barW, barH);
            }
        }
        float spd = 0.05f;
        this.currentScale += ((this.selectedPart == BodyPart.NONE ? 45.0f : this.selectedPart.zoomScale) - this.currentScale) * spd;
        this.currentOffsetX += ((float)(this.selectedPart == BodyPart.NONE ? 0 : this.selectedPart.zoomOffsetX) - this.currentOffsetX) * spd;
        this.currentOffsetY += ((float)(this.selectedPart == BodyPart.NONE ? 0 : this.selectedPart.zoomOffsetY) - this.currentOffsetY) * spd;
        int drawX = (int)((float)(x + 88) + this.currentOffsetX);
        int drawY = (int)((float)(y + 131 - 15) + this.currentOffsetY);
        if (!this.hideName && this.minecraft.player != null) {
            String name = "_" + this.minecraft.player.getName().getString().toUpperCase();
            g.drawString(this.font, name, drawX - this.font.width(name) / 2, drawY, 65535, true);
        }
        float f = ease = (float)(elapsed = System.currentTimeMillis() - this.startTime) < 2000.0f ? 1.0f - (float)Math.pow(1.0f - Math.min((float)elapsed / 2000.0f, 1.0f), 3.0) : 1.0f;
        if ((float)elapsed < 2000.0f) {
            this.viewRotation = currentRotation = ease * 360.0f;
        } else {
            currentRotation = this.viewRotation;
        }
        if (this.internalPartsModel != null && this.selectedPart != BodyPart.ARM_LEFT && this.selectedPart != BodyPart.ARM_RIGHT && this.selectedPart != BodyPart.LEG_LEFT && this.selectedPart != BodyPart.LEG_RIGHT && this.selectedPart != BodyPart.HEAD && this.selectedPart != BodyPart.TORSO) {
            int subScale;
            int subX = this.selectedPart == BodyPart.INTERNAL ? drawX - 48 : x + 40;
            int subY = this.selectedPart == BodyPart.INTERNAL ? drawY : y + 131 - 21;
            int n = subScale = this.selectedPart == BodyPart.INTERNAL ? (int)this.currentScale : 40;
            if (this.selectedPart == BodyPart.NONE) {
                int boxX = subX - 18;
                int boxY = subY - 52;
                g.renderOutline(boxX, boxY, 37, 37, -16711681);
                g.fill(boxX + 28, boxY + 18, drawX, boxY + 19, -16711681);
            }
            for (int i = 0; i < 3; ++i) {
                this.internalPartsModel.setVisibleLayer(i);
                int renderY = subY + (this.selectedPart == BodyPart.NONE ? 17 : 0);
                RobosurgeonScreen.renderCustomModel(g, subX, renderY, subScale, currentRotation, this.internalPartsModel, INTERNAL_PARTS_TEXTURE);
            }
        }
        if (this.selectedPart != BodyPart.INTERNAL) {
            double guiScale = this.minecraft.getWindow().getGuiScale();
            int modelH = 115;
            int scan = (int)((float)modelH * ease);
            if (!this.isAnimating(elapsed)) {
                if (this.skeletonModel != null) {
                    RenderSystem.enableScissor((int)((int)((double)(x + 5) * guiScale)), (int)((int)((double)(this.height - (y + 131)) * guiScale)), (int)((int)(165.0 * guiScale)), (int)((int)(121.0 * guiScale)));
                    RobosurgeonScreen.renderCustomModel(g, drawX, drawY, (int)(this.currentScale * 0.933f) + 5, currentRotation, this.skeletonModel, SKELETON_TEXTURE);
                    RenderSystem.disableScissor();
                }
            } else if (this.minecraft.player != null) {
                int scX = (int)((double)(drawX - 50) * guiScale);
                int scW = (int)(100.0 * guiScale);
                int scFeet = (int)((double)(this.height - drawY) * guiScale);
                if (modelH - scan > 0) {
                    RenderSystem.enableScissor((int)scX, (int)scFeet, (int)scW, (int)((int)((double)(modelH - scan) * guiScale)));
                    RobosurgeonScreen.renderEntityWithRotation(g, drawX, drawY, 50, currentRotation, (LivingEntity)this.minecraft.player);
                    RenderSystem.disableScissor();
                }
                if (this.skeletonModel != null && scan > 0) {
                    RenderSystem.enableScissor((int)scX, (int)(scFeet + (int)((double)(modelH - scan) * guiScale)), (int)scW, (int)((int)((double)scan * guiScale)));
                    RobosurgeonScreen.renderCustomModel(g, drawX, drawY, 47, currentRotation, this.skeletonModel, SKELETON_TEXTURE);
                    RenderSystem.disableScissor();
                }
                g.blit(TEXTURE, drawX - 40, drawY - modelH + scan, 176, 110, 80, 1);
            }
        }
    }

    private boolean isAnimating(long elapsed) {
        return (float)elapsed < 2000.0f;
    }

    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        CyberwareUserData data = (CyberwareUserData)this.minecraft.player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get());
        int maxTolerance = data.getMaxTolerance((LivingEntity)this.minecraft.player);
        int currentCost = 0;
        for (int i = 0; i < RobosurgeonBlockEntity.TOTAL_SLOTS; ++i) {
            ItemStack stack = ((RobosurgeonMenu)this.menu).getSlot(i).getItem();
            ICyberware cw = CyberwareAPI.getCyberware(stack);
            if (cw == null) continue;
            currentCost += cw.getEssenceCost(stack) * stack.getCount();
        }
        int remaining = maxTolerance - currentCost;
        pGuiGraphics.drawString(this.font, remaining + " / " + maxTolerance, 18, 6, remaining < 0 ? 0xAA0000 : (remaining < 25 ? 0xFF5555 : 65535), true);
    }

    private void updateSlotPositions() {
        for (int i = 0; i < RobosurgeonBlockEntity.TOTAL_SLOTS; ++i) {
            if (i >= ((RobosurgeonMenu)this.menu).slots.size()) continue;
            this.setSlotPos((Slot)((RobosurgeonMenu)this.menu).slots.get(i), 20000, 20000);
        }
        if (this.selectedMarker != null) {
            int[] targets = this.selectedMarker.relatedSlots();
            int uiWidth = 18 * targets.length + 2 * (targets.length - 1);
            int uiX = (this.width - uiWidth) / 2;
            for (int i = 0; i < targets.length; ++i) {
                if (targets[i] >= ((RobosurgeonMenu)this.menu).slots.size()) continue;
                int slotXVal = uiX - this.leftPos + i * 20;
                int slotYVal = 105;
                this.setSlotPos((Slot)((RobosurgeonMenu)this.menu).slots.get(targets[i]), slotXVal, slotYVal);
            }
        }
    }

    private void renderGhostConflict(GuiGraphics g, int slotCount, int uiX, int stagingY, int installedY) {
        ItemStack carried = ((RobosurgeonMenu)this.menu).getCarried();
        if (carried.isEmpty()) {
            return;
        }
        ICyberware carriedCw = CyberwareAPI.getCyberware(carried);
        if (carriedCw == null) {
            return;
        }
        ItemStackHandler installed = ((CyberwareUserData)this.minecraft.player.getData((AttachmentType)CyberwareCapabilityProvider.CYBERWARE_DATA.get())).getInstalledCyberware();
        int[] related = this.selectedMarker.relatedSlots();
        for (int j = 0; j < slotCount; ++j) {
            ICyberware otherCw;
            boolean isStaging;
            int targetId = related[j];
            ItemStack other = ((RobosurgeonMenu)this.menu).getSlot(targetId).getItem();
            boolean bl = isStaging = !other.isEmpty();
            if (other.isEmpty() && targetId < installed.getSlots()) {
                other = installed.getStackInSlot(targetId);
            }
            if (other.isEmpty() || (otherCw = CyberwareAPI.getCyberware(other)) == null || (carriedCw.getBodyPartType(carried) == BodyPartType.NONE || carriedCw.getBodyPartType(carried) != otherCw.getBodyPartType(other)) && !carriedCw.isIncompatible(carried, other) && !otherCw.isIncompatible(other, carried)) continue;
            int x = uiX + j * 20 - 1;
            int y = isStaging ? stagingY - 1 : installedY - 1;
            g.pose().pushPose();
            g.pose().translate(0.0f, 0.0f, 400.0f);
            g.fill(x, y, x + 18, y + 18, -2130771968);
            g.renderOutline(x, y, 18, 18, -65536);
            g.drawString(this.font, "!", x + 7, y + 5, -65536, true);
            g.pose().popPose();
        }
    }

    private void drawEssenceBar(GuiGraphics g, int essence, int maxEssence, int x, int y, int w, int h) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int danger = (int)((float)maxEssence * 0.25f);
        int rH = (int)((float)h * ((float)Math.min(Math.max(0, essence), danger) / (float)maxEssence));
        int bH = (int)((float)h * ((float)Math.max(0, essence - danger) / (float)maxEssence));
        if (rH > 0) {
            g.blit(TEXTURE, x, y + (h - rH), w, rH, 220.0f, (float)(61 + (48 - rH)), w, rH, 256, 256);
        }
        if (bH > 0) {
            g.blit(TEXTURE, x, y + (h - rH - bH), w, bH, 176.0f, (float)(61 + (48 - (rH + bH))), w, bH, 256, 256);
        }
        RenderSystem.disableBlend();
    }

    private int getProjectedFutureEssence(CyberwareUserData data) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return 100;
        }
        ItemStackHandler playerBody = data.getInstalledCyberware();
        int futureCost = 0;
        for (int i = 0; i < RobosurgeonBlockEntity.TOTAL_SLOTS; ++i) {
            ItemStack tableStack = ((RobosurgeonMenu)this.menu).getSlot(i).getItem();
            ItemStack finalStack = tableStack.isEmpty() ? ItemStack.EMPTY : ((Boolean)tableStack.getOrDefault((DataComponentType)CyberWare.GHOST_COMPONENT.get(), (Object)false) != false ? playerBody.getStackInSlot(i) : tableStack);
            ICyberware cw = CyberwareAPI.getCyberware(finalStack);
            if (cw == null) continue;
            futureCost += cw.getEssenceCost(finalStack) * finalStack.getCount();
        }
        int maxTolerance = data.getMaxTolerance((LivingEntity)this.minecraft.player);
        return Math.max(0, maxTolerance - futureCost);
    }

    static {
        try {
            slotX = Slot.class.getDeclaredField("x");
            slotX.setAccessible(true);
            slotY = Slot.class.getDeclaredField("y");
            slotY.setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static enum BodyPart {
        HEAD(0, -80, 32, 32, 0, 160, 120.0f, List.of(new TargetMarker((Component)Component.literal((String)"Left Eye"), 2.0f, 25.5f, -3.4f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_EYES)), new TargetMarker((Component)Component.literal((String)"Right Eye"), -2.0f, 25.5f, -3.4f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_EYES)), new TargetMarker((Component)Component.literal((String)"Brain"), -0.13f, 27.56f, 1.52f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_BRAIN)))),
        TORSO(0, -54, 26, 32, 0, 120, 120.0f, List.of(new TargetMarker((Component)Component.literal((String)"Heart"), 0.0f, 21.0f, -0.5f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_HEART)), new TargetMarker((Component)Component.literal((String)"Left Lung"), 2.3f, 20.0f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_LUNGS)), new TargetMarker((Component)Component.literal((String)"Stomach"), 0.0f, 16.0f, -1.5f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_STOMACH)), new TargetMarker((Component)Component.literal((String)"Right Lung"), -2.3f, 20.0f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_LUNGS)))),
        ARM_LEFT(18, -54, 12, 34, -60, 120, 120.0f, List.of(new TargetMarker((Component)Component.literal((String)"Left Arm"), 4.7f, 21.0f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_ARMS)), new TargetMarker((Component)Component.literal((String)"Left Hand"), 5.8f, 14.0f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_HANDS)))),
        ARM_RIGHT(-18, -54, 12, 34, 60, 120, 120.0f, List.of(new TargetMarker((Component)Component.literal((String)"Right Arm"), -4.7f, 21.0f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_ARMS)), new TargetMarker((Component)Component.literal((String)"Right Hand"), -5.8f, 14.0f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_HANDS)))),
        LEG_LEFT(5, -19, 12, 38, -50, 20, 120.0f, List.of(new TargetMarker((Component)Component.literal((String)"Left Leg"), 2.0f, 10.0f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_LEGS)), new TargetMarker((Component)Component.literal((String)"Left Foot"), 2.1f, 3.9f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_BOOTS)))),
        LEG_RIGHT(-5, -19, 12, 38, 50, 20, 120.0f, List.of(new TargetMarker((Component)Component.literal((String)"Right Leg"), -2.0f, 10.0f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_LEGS)), new TargetMarker((Component)Component.literal((String)"Right Foot"), -2.1f, 3.9f, 0.0f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_BOOTS)))),
        INTERNAL(0, 0, 40, 50, 48, 130, 150.0f, List.of(new TargetMarker((Component)Component.literal((String)"Skin"), -3.0f, 24.6f, -5.5f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_SKIN)), new TargetMarker((Component)Component.literal((String)"Muscle"), 0.0f, 22.7f, -5.5f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_MUSCLE)), new TargetMarker((Component)Component.literal((String)"Bone"), 3.0f, 20.8f, -5.5f, RobosurgeonScreen.slots(RobosurgeonBlockEntity.SLOT_BONES)))),
        NONE(0, 0, 0, 0, 0, 0, 45.0f, List.of());

        final int hitX;
        final int hitY;
        final int hitW;
        final int hitH;
        final int zoomOffsetX;
        final int zoomOffsetY;
        final float zoomScale;
        final List<TargetMarker> markers;

        private BodyPart(int hX, int hY, int hW, int hH, int zX, int zY, float zS, List<TargetMarker> m) {
            this.hitX = hX;
            this.hitY = hY;
            this.hitW = hW;
            this.hitH = hH;
            this.zoomOffsetX = zX;
            this.zoomOffsetY = zY;
            this.zoomScale = zS;
            this.markers = m;
        }
    }

    public record TargetMarker(Component name, float modelX, float modelY, float modelZ, int[] relatedSlots) {
    }
}

