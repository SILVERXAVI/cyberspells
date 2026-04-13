/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.Skeleton
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  org.joml.Quaternionf
 *  org.joml.Vector3f
 */
package com.perigrine3.createcybernetics.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.perigrine3.createcybernetics.Config;
import com.perigrine3.createcybernetics.api.CyberwareSlot;
import com.perigrine3.createcybernetics.api.ICyberwareItem;
import com.perigrine3.createcybernetics.api.InstalledCyberware;
import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.surgery.RobosurgeonSlotMap;
import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.createcybernetics.item.ModItems;
import com.perigrine3.createcybernetics.screen.custom.MarkerManager;
import com.perigrine3.createcybernetics.screen.custom.ModelViewer;
import com.perigrine3.createcybernetics.screen.custom.RobosurgeonMenu;
import com.perigrine3.createcybernetics.screen.custom.RobosurgeonSlotItemHandler;
import com.perigrine3.createcybernetics.util.ModTags;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RobosurgeonScreen
extends AbstractContainerScreen<RobosurgeonMenu> {
    private ViewMode viewMode = ViewMode.FULL_BODY;
    private final ModelViewer modelViewer = new ModelViewer();
    private final MarkerManager markerManager = new MarkerManager(MARKER_ICON);
    private int typingTicks = 0;
    private String animatedTitle = "";
    private static final int TYPE_DELAY = 4;
    private final Skeleton skeletonPreview;
    private float modelFade;
    private final int backX = 4;
    private final int backY = 117;
    private final int backW = 20;
    private final int backH = 10;
    private static final int HUMANITY_BAR_WIDTH = 10;
    private static final int HUMANITY_BAR_HEIGHT = 75;
    private static final int WARNING_W = 12;
    private static final int WARNING_H = 12;
    private static final int WARNING_X = -15;
    private static final int WARNING_Y = 8;
    private final ItemStack renderSkin;
    private final ItemStack renderMuscle;
    private final ItemStack renderBone;
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/robosurgeon/robosurgeon_gui.png");
    private static final ResourceLocation MARKER_ICON = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/robosurgeon/robosurgeon_interface_marker.png");
    private static final ResourceLocation BACK_ICON = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/robosurgeon/robosurgeon_interface_backbutton.png");
    private static final ResourceLocation SLOT_ICON = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/robosurgeon/robosurgeon_interface_slot.png");
    private static final ResourceLocation REMOVALSLOT_ICON = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/robosurgeon/robosurgeon_interface_slotmarkedforremoval.png");
    private static final ResourceLocation STAGEDINSTALLSLOT_ICON = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/robosurgeon/robosurgeon_interface_stagedinstallslot.png");
    private static final ResourceLocation REMOVESLOT_ICON = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/robosurgeon/robosurgeon_interface_removeslot.png");
    private static final ResourceLocation SLOTHOVER_ICON = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/robosurgeon/robosurgeon_interface_slothover.png");
    private static final ResourceLocation WARNING_ICON = ResourceLocation.fromNamespaceAndPath((String)"createcybernetics", (String)"textures/gui/robosurgeon/robosurgeon_interface_warning.png");
    private static final int NEUROPOZYNE_HUMANITY_PER_LEVEL = 25;
    private final List<SlotBackground> slotBackgrounds;
    private final List<SlotView> slotViews;

    public RobosurgeonScreen(RobosurgeonMenu menu, Inventory playerInventory, Component title) {
        super((AbstractContainerMenu)menu, playerInventory, title);
        this.skeletonPreview = new Skeleton(EntityType.SKELETON, (Level)Minecraft.getInstance().level);
        this.modelFade = 0.0f;
        this.backX = 4;
        this.backY = 117;
        this.backW = 20;
        this.backH = 10;
        this.renderSkin = new ItemStack((ItemLike)ModItems.BODYPART_SKIN.get());
        this.renderMuscle = new ItemStack((ItemLike)ModItems.BODYPART_MUSCLE.get());
        this.renderBone = new ItemStack((ItemLike)Items.BONE);
        this.slotBackgrounds = new ArrayList<SlotBackground>();
        this.slotViews = new ArrayList<SlotView>();
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    protected void init() {
        super.init();
        this.topPos -= 10;
        this.modelViewer.triggerZoomReset();
        this.registerMarkers();
        this.registerSlotBackgrounds();
        this.registerSlotViews();
    }

    private void registerMarkers() {
        this.markerManager.clear();
        this.markerManager.add(new MarkerManager.Marker(-8, -78, ViewMode.FULL_BODY, ViewMode.HEAD, (Component)Component.translatable((String)"gui.marker.head"), false));
        this.markerManager.add(new MarkerManager.Marker(-8, -52, ViewMode.FULL_BODY, ViewMode.TORSO, (Component)Component.translatable((String)"gui.marker.torso"), false));
        this.markerManager.add(new MarkerManager.Marker(50, -52, ViewMode.FULL_BODY, ViewMode.SKIN, (Component)Component.translatable((String)"gui.marker.skin"), false));
        this.markerManager.add(new MarkerManager.Marker(9, -55, ViewMode.FULL_BODY, ViewMode.LARM, (Component)Component.translatable((String)"gui.marker.larm"), true));
        this.markerManager.add(new MarkerManager.Marker(-25, -55, ViewMode.FULL_BODY, ViewMode.RARM, (Component)Component.translatable((String)"gui.marker.rarm"), true));
        this.markerManager.add(new MarkerManager.Marker(-2, -28, ViewMode.FULL_BODY, ViewMode.LLEG, (Component)Component.translatable((String)"gui.marker.lleg"), true));
        this.markerManager.add(new MarkerManager.Marker(-14, -28, ViewMode.FULL_BODY, ViewMode.RLEG, (Component)Component.translatable((String)"gui.marker.rleg"), true));
        this.markerManager.add(new MarkerManager.Marker(-35, -210, ViewMode.HEAD, ViewMode.BRAIN, (Component)Component.translatable((String)"gui.marker.brain"), false));
        this.markerManager.add(new MarkerManager.Marker(-10, -197, ViewMode.HEAD, ViewMode.EYES, (Component)Component.translatable((String)"gui.marker.eyes"), false));
        this.markerManager.add(new MarkerManager.Marker(15, -197, ViewMode.HEAD, ViewMode.EYES, (Component)Component.translatable((String)"gui.marker.eyes"), false));
        this.markerManager.add(new MarkerManager.Marker(0, -185, ViewMode.TORSO, ViewMode.HEART, (Component)Component.translatable((String)"gui.marker.heart"), false));
        this.markerManager.add(new MarkerManager.Marker(-15, -170, ViewMode.TORSO, ViewMode.LUNGS, (Component)Component.translatable((String)"gui.marker.lungs"), false));
        this.markerManager.add(new MarkerManager.Marker(-5, -135, ViewMode.TORSO, ViewMode.ORGANS, (Component)Component.translatable((String)"gui.marker.organs"), false));
    }

    private void updateTypingAnimation() {
        String full = this.title.getString();
        if (this.animatedTitle.length() < full.length()) {
            ++this.typingTicks;
            if (this.typingTicks >= 4) {
                this.typingTicks = 0;
                this.animatedTitle = full.substring(0, this.animatedTitle.length() + 1);
            }
        }
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        float scale = 0.75f;
        int titleWidth = this.font.width(this.animatedTitle);
        int scaledX = (int)(((float)this.imageWidth - (float)titleWidth * scale) / 2.0f);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0f);
        guiGraphics.drawString(this.font, this.animatedTitle, (int)((float)scaledX / scale), (int)(6.0f / scale), 65318, false);
        guiGraphics.pose().popPose();
        float invScale = 0.85f;
        int labelY = this.imageHeight - 94 + 4;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(invScale, invScale, 1.0f);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, (int)(8.0f / invScale), (int)((float)labelY / invScale), 0x404040, false);
        guiGraphics.pose().popPose();
    }

    protected void renderBg(GuiGraphics guiGraphics, float p, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)GUI_TEXTURE);
        guiGraphics.blit(GUI_TEXTURE, this.leftPos, this.topPos, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 176, 222);
        this.drawHumanityBar(guiGraphics);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        for (Slot slot : ((RobosurgeonMenu)this.menu).slots) {
            RobosurgeonSlotItemHandler rsSlot;
            if (!(slot instanceof RobosurgeonSlotItemHandler) || !this.isSlotVisible((Slot)(rsSlot = (RobosurgeonSlotItemHandler)slot))) continue;
            int handlerIndex = rsSlot.getSlotIndex();
            int x = this.leftPos + rsSlot.x - 1;
            int y = this.topPos + rsSlot.y - 1;
            guiGraphics.blit(SLOT_ICON, x, y, 0.0f, 0.0f, 18, 18, 18, 18);
            if (((RobosurgeonMenu)this.menu).isMarkedForRemoval(handlerIndex)) {
                guiGraphics.setColor(1.0f, 1.0f, 1.0f, 0.6f);
                guiGraphics.blit(REMOVALSLOT_ICON, x, y, 0.0f, 0.0f, 18, 18, 18, 18);
                guiGraphics.blit(REMOVESLOT_ICON, x, y, 0.0f, 0.0f, 18, 18, 18, 18);
                guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                continue;
            }
            if (!((RobosurgeonMenu)this.menu).isStaged(handlerIndex)) continue;
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 0.6f);
            guiGraphics.blit(STAGEDINSTALLSLOT_ICON, x, y, 0.0f, 0.0f, 18, 18, 18, 18);
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RenderSystem.disableBlend();
    }

    private void drawHumanityBar(GuiGraphics gui) {
        LocalPlayer player = this.minecraft.player;
        if (player == null) {
            return;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return;
        }
        int humanity = this.calculatePreviewHumanity();
        int maxHumanity = this.getConfiguredBaseHumanity();
        maxHumanity = Math.max(1, maxHumanity);
        float raw = (float)humanity / (float)maxHumanity;
        float percent = Mth.clamp((float)raw, (float)0.0f, (float)1.0f);
        int x = this.leftPos + 10;
        int y = this.topPos + 30;
        gui.fill(x, y, x + 10, y + 75, -14671840);
        int filled = (int)(75.0f * percent);
        int color = this.getHumanityColor(percent);
        gui.fill(x, y + (75 - filled), x + 10, y + 75, color);
        String text = Integer.toString(humanity);
        float labelScale = 0.5f;
        int textW = this.font.width(text);
        int textX = x + 5 - Math.round((float)textW * labelScale / 2.0f);
        gui.pose().pushPose();
        gui.pose().scale(labelScale, labelScale, 1.0f);
        gui.drawString(this.minecraft.font, text, (int)((float)textX / labelScale), (int)((float)(y - 7) / labelScale), -13314581, false);
        gui.pose().popPose();
    }

    private int getConfiguredBaseHumanity() {
        return (Integer)Config.HUMANITY.get();
    }

    private int getNeuropozyneBonusClient(Player player) {
        for (MobEffectInstance inst : player.getActiveEffects()) {
            if (!inst.is(ModEffects.NEUROPOZYNE)) continue;
            return (inst.getAmplifier() + 1) * 25;
        }
        return 0;
    }

    private int getHumanityColor(float percent) {
        if (percent > 0.66f) {
            return -13959424;
        }
        if (percent > 0.25f) {
            return -22016;
        }
        return -65536;
    }

    private int calculatePreviewHumanity() {
        LocalPlayer player = this.minecraft.player;
        if (player == null) {
            return 100;
        }
        PlayerCyberwareData data = (PlayerCyberwareData)player.getData(ModAttachments.CYBERWARE);
        if (data == null) {
            return 100;
        }
        int humanity = data.getHumanityBase() + this.getNeuropozyneBonusClient((Player)player);
        ItemStack[] guiStacks = new ItemStack[65];
        for (Slot s : ((RobosurgeonMenu)this.menu).slots) {
            RobosurgeonSlotItemHandler rs;
            int idx;
            if (!(s instanceof RobosurgeonSlotItemHandler) || (idx = (rs = (RobosurgeonSlotItemHandler)s).getSlotIndex()) < 0 || idx >= guiStacks.length) continue;
            guiStacks[idx] = rs.getItem();
        }
        for (CyberwareSlot slotType : CyberwareSlot.values()) {
            for (int i = 0; i < slotType.size; ++i) {
                Item item;
                Item item2;
                ItemStack stagedStack;
                Item item3;
                ItemStack installedStack;
                int invIndex = RobosurgeonSlotMap.toInventoryIndex(slotType, i);
                if (invIndex < 0 || invIndex >= 65) continue;
                boolean staged = ((RobosurgeonMenu)this.menu).isStaged(invIndex);
                boolean marked = ((RobosurgeonMenu)this.menu).isMarkedForRemoval(invIndex);
                InstalledCyberware installed = data.get(slotType, i);
                ItemStack itemStack = installedStack = installed != null && installed.getItem() != null ? installed.getItem() : ItemStack.EMPTY;
                if (marked && !installedStack.isEmpty() && (item3 = installedStack.getItem()) instanceof ICyberwareItem) {
                    ICyberwareItem instItem = (ICyberwareItem)item3;
                    humanity += instItem.getHumanityCost();
                }
                if (!staged || (stagedStack = guiStacks[invIndex]).isEmpty() || !((item2 = stagedStack.getItem()) instanceof ICyberwareItem)) continue;
                ICyberwareItem stagedItem = (ICyberwareItem)item2;
                if (!marked && !installedStack.isEmpty() && (item = installedStack.getItem()) instanceof ICyberwareItem) {
                    ICyberwareItem instItem = (ICyberwareItem)item;
                    humanity += instItem.getHumanityCost();
                }
                humanity -= stagedItem.getHumanityCost();
            }
        }
        return humanity;
    }

    protected void renderSlot(GuiGraphics gui, Slot slot) {
        if (!this.isSlotVisible(slot)) {
            return;
        }
        super.renderSlot(gui, slot);
    }

    private void registerSlotBackgrounds() {
        this.slotBackgrounds.clear();
        this.slotBackgrounds.add(new SlotBackground(151, 110, ViewMode.BRAIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 92, ViewMode.BRAIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 74, ViewMode.BRAIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 56, ViewMode.BRAIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 38, ViewMode.BRAIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 110, ViewMode.EYES, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 92, ViewMode.EYES, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 74, ViewMode.EYES, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 56, ViewMode.EYES, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 38, ViewMode.EYES, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 110, ViewMode.HEART, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 92, ViewMode.HEART, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 74, ViewMode.HEART, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 56, ViewMode.HEART, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 38, ViewMode.HEART, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 20, ViewMode.HEART, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 110, ViewMode.LUNGS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 92, ViewMode.LUNGS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 74, ViewMode.LUNGS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 56, ViewMode.LUNGS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 38, ViewMode.LUNGS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 20, ViewMode.LUNGS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 110, ViewMode.ORGANS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 92, ViewMode.ORGANS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 74, ViewMode.ORGANS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 56, ViewMode.ORGANS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 38, ViewMode.ORGANS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(151, 20, ViewMode.ORGANS, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 110, ViewMode.RARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 92, ViewMode.RARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 74, ViewMode.RARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 56, ViewMode.RARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 38, ViewMode.RARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 20, ViewMode.RARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 110, ViewMode.LARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 92, ViewMode.LARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 74, ViewMode.LARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 56, ViewMode.LARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 38, ViewMode.LARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 20, ViewMode.LARM, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 110, ViewMode.RLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 92, ViewMode.RLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 74, ViewMode.RLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 56, ViewMode.RLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(43, 38, ViewMode.RLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 110, ViewMode.LLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 92, ViewMode.LLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 74, ViewMode.LLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 56, ViewMode.LLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(115, 38, ViewMode.LLEG, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(79, 110, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(79, 92, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(79, 74, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(79, 56, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(79, 38, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(106, 110, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(106, 92, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(106, 74, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(106, 56, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(106, 38, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(52, 110, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(52, 92, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(52, 74, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(52, 56, ViewMode.SKIN, SLOT_ICON));
        this.slotBackgrounds.add(new SlotBackground(52, 38, ViewMode.SKIN, SLOT_ICON));
    }

    private void drawSlotBackground(GuiGraphics gui, SlotBackground bg) {
        gui.blit(bg.texture, this.leftPos + bg.x, this.topPos + bg.y, 0.0f, 0.0f, 18, 18, 18, 18);
    }

    private boolean isSlotVisible(Slot slot) {
        if (!(slot instanceof RobosurgeonSlotItemHandler)) {
            return true;
        }
        RobosurgeonSlotItemHandler rsSlot = (RobosurgeonSlotItemHandler)slot;
        return this.isHandlerSlotVisible(rsSlot.getSlotIndex());
    }

    private void updateTeSlotActivity() {
        for (Slot slot : ((RobosurgeonMenu)this.menu).slots) {
            if (!(slot instanceof RobosurgeonSlotItemHandler)) continue;
            RobosurgeonSlotItemHandler rsSlot = (RobosurgeonSlotItemHandler)slot;
            int handlerIndex = rsSlot.getSlotIndex();
            boolean visible = this.isHandlerSlotVisible(handlerIndex);
            rsSlot.setActiveFlag(visible);
        }
    }

    private boolean isHandlerSlotVisible(int handlerIndex) {
        for (SlotView view : this.slotViews) {
            if (view.slotIndex != handlerIndex) continue;
            return this.matchesView(view.viewMode);
        }
        return true;
    }

    private boolean matchesView(ViewMode slotView) {
        ViewMode current = this.viewMode;
        while (true) {
            if (current == slotView) {
                return true;
            }
            if (current == current.parent) break;
            current = current.parent;
        }
        return false;
    }

    private void registerSlotViews() {
        this.slotViews.clear();
        this.slotViews.add(new SlotView(0, ViewMode.BRAIN));
        this.slotViews.add(new SlotView(1, ViewMode.BRAIN));
        this.slotViews.add(new SlotView(2, ViewMode.BRAIN));
        this.slotViews.add(new SlotView(3, ViewMode.BRAIN));
        this.slotViews.add(new SlotView(4, ViewMode.BRAIN));
        this.slotViews.add(new SlotView(5, ViewMode.EYES));
        this.slotViews.add(new SlotView(6, ViewMode.EYES));
        this.slotViews.add(new SlotView(7, ViewMode.EYES));
        this.slotViews.add(new SlotView(8, ViewMode.EYES));
        this.slotViews.add(new SlotView(9, ViewMode.EYES));
        this.slotViews.add(new SlotView(10, ViewMode.HEART));
        this.slotViews.add(new SlotView(11, ViewMode.HEART));
        this.slotViews.add(new SlotView(12, ViewMode.HEART));
        this.slotViews.add(new SlotView(13, ViewMode.HEART));
        this.slotViews.add(new SlotView(14, ViewMode.HEART));
        this.slotViews.add(new SlotView(15, ViewMode.HEART));
        this.slotViews.add(new SlotView(16, ViewMode.LUNGS));
        this.slotViews.add(new SlotView(17, ViewMode.LUNGS));
        this.slotViews.add(new SlotView(18, ViewMode.LUNGS));
        this.slotViews.add(new SlotView(19, ViewMode.LUNGS));
        this.slotViews.add(new SlotView(20, ViewMode.LUNGS));
        this.slotViews.add(new SlotView(21, ViewMode.LUNGS));
        this.slotViews.add(new SlotView(22, ViewMode.ORGANS));
        this.slotViews.add(new SlotView(23, ViewMode.ORGANS));
        this.slotViews.add(new SlotView(24, ViewMode.ORGANS));
        this.slotViews.add(new SlotView(25, ViewMode.ORGANS));
        this.slotViews.add(new SlotView(26, ViewMode.ORGANS));
        this.slotViews.add(new SlotView(27, ViewMode.ORGANS));
        this.slotViews.add(new SlotView(28, ViewMode.RARM));
        this.slotViews.add(new SlotView(29, ViewMode.RARM));
        this.slotViews.add(new SlotView(30, ViewMode.RARM));
        this.slotViews.add(new SlotView(31, ViewMode.RARM));
        this.slotViews.add(new SlotView(32, ViewMode.RARM));
        this.slotViews.add(new SlotView(33, ViewMode.RARM));
        this.slotViews.add(new SlotView(34, ViewMode.LARM));
        this.slotViews.add(new SlotView(35, ViewMode.LARM));
        this.slotViews.add(new SlotView(36, ViewMode.LARM));
        this.slotViews.add(new SlotView(37, ViewMode.LARM));
        this.slotViews.add(new SlotView(38, ViewMode.LARM));
        this.slotViews.add(new SlotView(39, ViewMode.LARM));
        this.slotViews.add(new SlotView(40, ViewMode.RLEG));
        this.slotViews.add(new SlotView(41, ViewMode.RLEG));
        this.slotViews.add(new SlotView(42, ViewMode.RLEG));
        this.slotViews.add(new SlotView(43, ViewMode.RLEG));
        this.slotViews.add(new SlotView(44, ViewMode.RLEG));
        this.slotViews.add(new SlotView(45, ViewMode.LLEG));
        this.slotViews.add(new SlotView(46, ViewMode.LLEG));
        this.slotViews.add(new SlotView(47, ViewMode.LLEG));
        this.slotViews.add(new SlotView(48, ViewMode.LLEG));
        this.slotViews.add(new SlotView(49, ViewMode.LLEG));
        this.slotViews.add(new SlotView(50, ViewMode.SKIN));
        this.slotViews.add(new SlotView(51, ViewMode.SKIN));
        this.slotViews.add(new SlotView(52, ViewMode.SKIN));
        this.slotViews.add(new SlotView(53, ViewMode.SKIN));
        this.slotViews.add(new SlotView(54, ViewMode.SKIN));
        this.slotViews.add(new SlotView(55, ViewMode.SKIN));
        this.slotViews.add(new SlotView(56, ViewMode.SKIN));
        this.slotViews.add(new SlotView(57, ViewMode.SKIN));
        this.slotViews.add(new SlotView(58, ViewMode.SKIN));
        this.slotViews.add(new SlotView(59, ViewMode.SKIN));
        this.slotViews.add(new SlotView(60, ViewMode.SKIN));
        this.slotViews.add(new SlotView(61, ViewMode.SKIN));
        this.slotViews.add(new SlotView(62, ViewMode.SKIN));
        this.slotViews.add(new SlotView(63, ViewMode.SKIN));
        this.slotViews.add(new SlotView(64, ViewMode.SKIN));
    }

    private ResourceLocation getSlotBackgroundTexture(Slot slot) {
        if (!(slot instanceof RobosurgeonSlotItemHandler)) {
            return SLOT_ICON;
        }
        RobosurgeonSlotItemHandler rsSlot = (RobosurgeonSlotItemHandler)slot;
        int teFirst = ((RobosurgeonMenu)this.menu).getTeInventoryFirstSlotIndex();
        int handlerIndex = slot.index - teFirst;
        if (((RobosurgeonMenu)this.menu).isMarkedForRemoval(handlerIndex)) {
            return REMOVALSLOT_ICON;
        }
        if (((RobosurgeonMenu)this.menu).isStaged(handlerIndex)) {
            return SLOTHOVER_ICON;
        }
        if (((RobosurgeonMenu)this.menu).isInstalled(handlerIndex)) {
            return SLOT_ICON;
        }
        return SLOT_ICON;
    }

    private void applyScissor(GuiGraphics gui) {
        int y1 = this.topPos + 15;
        int x1 = this.leftPos + 3;
        int x2 = this.leftPos + 173;
        int y2 = this.topPos + 128;
        gui.enableScissor(x1, y1, x2, y2);
    }

    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        boolean isSkin;
        boolean cropping;
        this.updateTeSlotActivity();
        int baseModelY = this.topPos + 105;
        this.updateTypingAnimation();
        this.renderBackground(gui, mouseX, mouseY, partialTick);
        super.render(gui, mouseX, mouseY, partialTick);
        if (this.viewMode.allowRotation) {
            this.modelViewer.updateRotation(mouseX);
        }
        int modelX = this.leftPos + 88 + this.viewMode.horizontalOffset;
        int modelY = baseModelY + this.viewMode.verticalOffset;
        boolean bl = cropping = this.viewMode != ViewMode.FULL_BODY;
        if (cropping) {
            this.applyScissor(gui);
        }
        boolean isHeadGroup = this.viewMode == ViewMode.HEAD || this.viewMode == ViewMode.BRAIN || this.viewMode == ViewMode.EYES;
        boolean isTorsoGroup = this.viewMode == ViewMode.TORSO || this.viewMode == ViewMode.HEART || this.viewMode == ViewMode.LUNGS || this.viewMode == ViewMode.ORGANS;
        boolean isLeftArm = this.viewMode == ViewMode.LARM;
        boolean isRightArm = this.viewMode == ViewMode.RARM;
        boolean isLeftLeg = this.viewMode == ViewMode.LLEG;
        boolean isRightLeg = this.viewMode == ViewMode.RLEG;
        boolean bl2 = isSkin = this.viewMode == ViewMode.SKIN;
        if (isHeadGroup) {
            this.modelFade = Math.min(this.modelFade + 0.06f, 1.0f);
            this.renderHeadModeFade(gui, modelX, modelY, this.viewMode.baseScale, this.modelFade);
        } else if (isTorsoGroup) {
            this.modelFade = Math.min(this.modelFade + 0.06f, 1.0f);
            this.renderTorsoModeFade(gui, modelX, modelY, this.viewMode.baseScale, this.modelFade);
        } else if (isRightArm) {
            this.modelFade = Math.min(this.modelFade + 0.06f, 1.0f);
            this.renderRightArmModeFade(gui, modelX, modelY, this.viewMode.baseScale, this.modelFade);
        } else if (isLeftArm) {
            this.modelFade = Math.min(this.modelFade + 0.06f, 1.0f);
            this.renderLeftArmModeFade(gui, modelX, modelY, this.viewMode.baseScale, this.modelFade);
        } else if (isLeftLeg) {
            this.modelFade = Math.min(this.modelFade + 0.06f, 1.0f);
            this.renderLeftLegModeFade(gui, modelX, modelY, this.viewMode.baseScale, this.modelFade);
        } else if (isRightLeg) {
            this.modelFade = Math.min(this.modelFade + 0.06f, 1.0f);
            this.renderRightLegModeFade(gui, modelX, modelY, this.viewMode.baseScale, this.modelFade);
        } else if (isSkin) {
            this.modelFade = Math.min(this.modelFade + 0.06f, 1.0f);
            this.renderSkinModeFade(gui, modelX, modelY, this.viewMode.baseScale, this.modelFade);
        } else {
            this.modelFade = Math.max(this.modelFade - 0.06f, 0.0f);
            this.modelViewer.render(gui, modelX, modelY, this.viewMode.baseScale, (Player)this.minecraft.player, this.viewMode);
        }
        if (cropping) {
            gui.disableScissor();
        }
        if (this.viewMode != ViewMode.FULL_BODY) {
            gui.pose().pushPose();
            gui.pose().translate(0.0f, 0.0f, 300.0f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            int bx = this.leftPos + 4;
            int by = this.topPos + 117;
            boolean hoveringBack = mouseX >= bx && mouseX <= bx + 20 && mouseY >= by && mouseY <= by + 10;
            float alpha = hoveringBack ? 1.0f : 0.35f;
            gui.setColor(1.0f, 1.0f, 1.0f, alpha);
            gui.blit(BACK_ICON, bx, by, 0.0f, 0.0f, 20, 10, 20, 10);
            gui.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();
            gui.pose().popPose();
        }
        this.markerManager.render(gui, modelX, modelY, mouseX, mouseY, this.viewMode, this.modelViewer.getRotationPhase(), this.font);
        this.renderRemovalWarning(gui, mouseX, mouseY);
        this.renderTooltip(gui, mouseX, mouseY);
    }

    private void renderHeadModeFade(GuiGraphics gui, int x, int y, int scale, float fade) {
        Quaternionf spin = new Quaternionf().rotateX((float)Math.toRadians(180.0)).rotateY((float)Math.toRadians(25.0));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        InventoryScreen.renderEntityInInventory((GuiGraphics)gui, (float)x, (float)y, (float)scale, (Vector3f)new Vector3f(), (Quaternionf)spin, null, (LivingEntity)this.skeletonPreview);
        RenderSystem.disableBlend();
    }

    private void renderTorsoModeFade(GuiGraphics gui, int x, int y, int scale, float fade) {
        Quaternionf spin = new Quaternionf().rotateX((float)Math.toRadians(180.0)).rotateY((float)Math.toRadians(25.0));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        InventoryScreen.renderEntityInInventory((GuiGraphics)gui, (float)x, (float)y, (float)scale, (Vector3f)new Vector3f(), (Quaternionf)spin, null, (LivingEntity)this.skeletonPreview);
        RenderSystem.disableBlend();
    }

    private void renderRightArmModeFade(GuiGraphics gui, int x, int y, int scale, float fade) {
        Quaternionf spin = new Quaternionf().rotateX((float)Math.toRadians(180.0)).rotateY((float)Math.toRadians(25.0));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        InventoryScreen.renderEntityInInventory((GuiGraphics)gui, (float)x, (float)y, (float)scale, (Vector3f)new Vector3f(), (Quaternionf)spin, null, (LivingEntity)this.skeletonPreview);
        RenderSystem.disableBlend();
    }

    private void renderLeftArmModeFade(GuiGraphics gui, int x, int y, int scale, float fade) {
        Quaternionf spin = new Quaternionf().rotateX((float)Math.toRadians(180.0)).rotateY((float)Math.toRadians(25.0));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        InventoryScreen.renderEntityInInventory((GuiGraphics)gui, (float)x, (float)y, (float)scale, (Vector3f)new Vector3f(), (Quaternionf)spin, null, (LivingEntity)this.skeletonPreview);
        RenderSystem.disableBlend();
    }

    private void renderRightLegModeFade(GuiGraphics gui, int x, int y, int scale, float fade) {
        Quaternionf spin = new Quaternionf().rotateX((float)Math.toRadians(180.0)).rotateY((float)Math.toRadians(25.0));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        InventoryScreen.renderEntityInInventory((GuiGraphics)gui, (float)x, (float)y, (float)scale, (Vector3f)new Vector3f(), (Quaternionf)spin, null, (LivingEntity)this.skeletonPreview);
        RenderSystem.disableBlend();
    }

    private void renderLeftLegModeFade(GuiGraphics gui, int x, int y, int scale, float fade) {
        Quaternionf spin = new Quaternionf().rotateX((float)Math.toRadians(180.0)).rotateY((float)Math.toRadians(25.0));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        InventoryScreen.renderEntityInInventory((GuiGraphics)gui, (float)x, (float)y, (float)scale, (Vector3f)new Vector3f(), (Quaternionf)spin, null, (LivingEntity)this.skeletonPreview);
        RenderSystem.disableBlend();
    }

    private void renderSkinModeFade(GuiGraphics gui, int x, int y, int scale, float fade) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        gui.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        gui.pose().pushPose();
        int baseX = x - 43;
        int baseY = y - 75;
        float itemScale = 1.5f;
        gui.pose().translate((float)baseX, (float)baseY, 100.0f);
        gui.pose().scale(itemScale, itemScale, 1.0f);
        gui.renderItem(this.renderSkin, 2, -10);
        gui.pose().translate(20.0f, 0.0f, 0.0f);
        gui.renderItem(this.renderMuscle, 0, -10);
        gui.pose().translate(20.0f, 0.0f, 0.0f);
        gui.renderItem(this.renderBone, -2, -10);
        gui.pose().popPose();
        RenderSystem.disableBlend();
    }

    private int countMarkedForRemovalTotal() {
        int count = 0;
        for (SlotView view : this.slotViews) {
            if (!((RobosurgeonMenu)this.menu).isMarkedForRemoval(view.slotIndex)) continue;
            ++count;
        }
        return count;
    }

    private int countMarkedForRemovalVisible() {
        int count = 0;
        for (SlotView view : this.slotViews) {
            if (!this.matchesView(view.viewMode) || !((RobosurgeonMenu)this.menu).isMarkedForRemoval(view.slotIndex)) continue;
            ++count;
        }
        return count;
    }

    private boolean hasDefaultOrganMarkedForRemoval() {
        for (Slot slot : ((RobosurgeonMenu)this.menu).slots) {
            ItemStack stack;
            RobosurgeonSlotItemHandler rsSlot;
            int handlerIndex;
            if (!(slot instanceof RobosurgeonSlotItemHandler) || !((RobosurgeonMenu)this.menu).isMarkedForRemoval(handlerIndex = (rsSlot = (RobosurgeonSlotItemHandler)slot).getSlotIndex()) || (stack = rsSlot.getItem()).isEmpty() || !stack.is(ModTags.Items.BODY_PARTS)) continue;
            return true;
        }
        return false;
    }

    private void renderRemovalWarning(GuiGraphics gui, int mouseX, int mouseY) {
        if (!this.hasDefaultOrganMarkedForRemoval()) {
            return;
        }
        int x = this.leftPos + -15;
        int y = this.topPos + 8;
        gui.pose().pushPose();
        gui.pose().translate(0.0f, 0.0f, 400.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        gui.blit(WARNING_ICON, x, y, 0.0f, 0.0f, 12, 12, 12, 12);
        boolean hovering = this.isMouseOverRect(x, y, 12, 12, mouseX, mouseY);
        if (hovering) {
            List<MutableComponent> tip = List.of(Component.translatable((String)"gui.warning.title").withStyle(ChatFormatting.RED), Component.translatable((String)"gui.warning.desc1").withStyle(ChatFormatting.RED), Component.translatable((String)"gui.warning.desc2").withStyle(ChatFormatting.RED));
            gui.renderComponentTooltip(this.font, tip, mouseX, mouseY);
        }
        RenderSystem.disableBlend();
        gui.pose().popPose();
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int modelY;
        int modelX;
        ViewMode clicked;
        if (button == 0 && this.viewMode != ViewMode.FULL_BODY) {
            int bx = this.leftPos + 4;
            int by = this.topPos + 117;
            if (mouseX >= (double)bx && mouseX <= (double)(bx + 20) && mouseY >= (double)by && mouseY <= (double)(by + 10)) {
                if (this.minecraft.player != null) {
                    this.minecraft.player.playNotifySound((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.MASTER, 1.0f, 1.0f);
                }
                this.viewMode = this.viewMode.parent != null ? this.viewMode.parent : ViewMode.FULL_BODY;
                this.modelViewer.triggerZoomReset();
                return true;
            }
        }
        if (button == 0) {
            this.modelViewer.beginDrag(mouseX);
        }
        if ((clicked = this.markerManager.tryClick(mouseX, mouseY, modelX = this.leftPos + 88, modelY = this.topPos + 105 + this.viewMode.verticalOffset, this.modelViewer.getRotationPhase(), this.viewMode)) != null) {
            this.viewMode = clicked;
            this.modelViewer.triggerZoomReset();
            return true;
        }
        Slot hovered = this.getSlotUnderMouse();
        if (hovered != null && !this.isSlotVisible(hovered)) {
            return false;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.modelViewer.endDrag();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isMouseOverSlot(Slot slot, double mouseX, double mouseY) {
        int x = this.leftPos + slot.x;
        int y = this.topPos + slot.y;
        return mouseX >= (double)x && mouseX < (double)(x + 16) && mouseY >= (double)y && mouseY < (double)(y + 16);
    }

    private boolean isMouseOverRect(int x, int y, int w, int h, int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
    }

    public static enum ViewMode {
        FULL_BODY(45, true, true, 0, 0),
        HEAD(FULL_BODY, 110, false, false, 150, 0),
        TORSO(FULL_BODY, 135, false, false, 110, 0),
        SKIN(FULL_BODY, 75, false, true, 0, 0),
        LARM(FULL_BODY, 130, true, true, 110, -40),
        RARM(FULL_BODY, 130, true, true, 110, 40),
        LLEG(FULL_BODY, 130, true, true, 20, 0),
        RLEG(FULL_BODY, 130, true, true, 20, 0),
        BRAIN(HEAD, 110, false, false, 150, 0),
        EYES(HEAD, 110, false, false, 150, 0),
        HEART(TORSO, 135, false, false, 110, 0),
        LUNGS(TORSO, 135, false, false, 110, 0),
        ORGANS(TORSO, 135, false, false, 110, 0);

        public final ViewMode parent;
        public final int baseScale;
        public final boolean allowRotation;
        public final boolean allowMarkerAnimation;
        public final int verticalOffset;
        public final int horizontalOffset;

        private ViewMode(int scale, boolean rotate, boolean animate, int offsetY, int offsetX) {
            this.parent = this;
            this.baseScale = scale;
            this.allowRotation = rotate;
            this.allowMarkerAnimation = animate;
            this.verticalOffset = offsetY;
            this.horizontalOffset = offsetX;
        }

        private ViewMode(ViewMode parent, int scale, boolean rotate, boolean animate, int offsetY, int offsetX) {
            this.parent = parent;
            this.baseScale = scale;
            this.allowRotation = rotate;
            this.allowMarkerAnimation = animate;
            this.verticalOffset = offsetY;
            this.horizontalOffset = offsetX;
        }
    }

    private record SlotBackground(int x, int y, ViewMode viewMode, ResourceLocation texture) {
    }

    private record SlotView(int slotIndex, ViewMode viewMode) {
    }
}

