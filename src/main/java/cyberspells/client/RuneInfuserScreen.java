package cyberspells.client;

import cyberspells.CyberSpellsMod;
import cyberspells.menu.RuneInfuserMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RuneInfuserScreen extends AbstractContainerScreen<RuneInfuserMenu> {
    public RuneInfuserScreen(RuneInfuserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Draw Main Background (Cyberpunk Dark)
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF141414); // Very Dark Gray

        // Premium Beveled Border Effect
        // Outer 1px Cyan Neon
        graphics.fill(x - 1, y - 1, x, y + imageHeight + 1, 0xFF00D1FF); // L
        graphics.fill(x + imageWidth, y - 1, x + imageWidth + 1, y + imageHeight + 1, 0xFF00D1FF); // R
        graphics.fill(x, y - 1, x + imageWidth, y, 0xFF00D1FF); // T
        graphics.fill(x, y + imageHeight, x + imageWidth, y + imageHeight + 1, 0xFF00D1FF); // B

        // Inner 2px Dark Border for depth
        graphics.fill(x + 1, y + 1, x + imageWidth - 1, y + imageHeight - 1, 0xFF222222);
        graphics.fill(x + 3, y + 3, x + imageWidth - 3, y + imageHeight - 3, 0xFF111111);

        // Header Section
        graphics.fill(x + 5, y + 5, x + imageWidth - 5, y + 18, 0xFF1A1A1A);
        graphics.fill(x + 5, y + 18, x + imageWidth - 5, y + 19, 0xFF00D1FF); // Cyan separator line

        // Draw Slots with Glowing effects
        // Input Slot (Cyberware)
        drawGlowingSlot(graphics, x + 80, y + 20, 0xFF00D1FF);

        // Rune Slots
        int activeSlots = 0; // Fixed: Default to 0 when no item is present
        net.minecraft.world.item.ItemStack inputStack = menu.getSlot(0).getItem();
        if (!inputStack.isEmpty() && inputStack.getItem() instanceof cyberspells.items.CyberRuneItem runeItem) {
            activeSlots = runeItem.getMaxSlots();
        }

        // Draw horizontal connector bus (only for active area)
        if (activeSlots > 0) {
            int leftmostX = 40 + 8;
            int rightmostX = 40 + ((activeSlots - 1) * 20) + 8;
            int hubX = 88;
            int startX = Math.min(leftmostX, hubX);
            int endX = Math.max(rightmostX, hubX);
            // Draw thicker horizontal line (2px)
            graphics.fill(x + startX, y + 38, x + endX + 1, y + 40, 0xFF00D1FF);
        }

        for (int i = 0; i < 5; i++) {
            int slotX = x + 40 + (i * 20);
            int slotY = y + 50;

            if (i < activeSlots) {
                // Active slot: Thicker Vertical drop (2px) and full glow
                graphics.fill(slotX + 7, y + 38, slotX + 9, slotY, 0xFF00D1FF);
                drawGlowingSlot(graphics, slotX, slotY, 0xFF0080FF);
            } else {
                // Inactive slot: Shaded/Grayed out
                drawSimpleSlot(graphics, slotX, slotY, 0xFF333333);
            }
        }

        // Output Slot
        drawGlowingSlot(graphics, x + 145, y + 35, 0xFFFF00D1); // Magenta glow for final result

        // --- PLAYER INVENTORY SLOTS ---
        // Main Inventory (3x9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotX = x + 8 + col * 18;
                int slotY = y + imageHeight - 84 + row * 18;
                drawSimpleSlot(graphics, slotX, slotY, 0xFF333333);
            }
        }

        // Hotbar (1x9)
        for (int col = 0; col < 9; col++) {
            int slotX = x + 8 + col * 18;
            int slotY = y + imageHeight - 24;
            drawSimpleSlot(graphics, slotX, slotY, 0xFF00D1FF); // Cyan for hotbar
        }
    }

    private void drawSimpleSlot(GuiGraphics graphics, int x, int y, int borderColor) {
        graphics.fill(x - 1, y - 1, x + 17, y + 17, borderColor);
        graphics.fill(x, y, x + 16, y + 16, 0xFF000000);
    }

    private void drawGlowingSlot(GuiGraphics graphics, int x, int y, int glowColor) {
        // Subtle outer glow
        graphics.fill(x - 1, y - 1, x + 17, y + 17, glowColor);
        // Inner black slot
        graphics.fill(x, y, x + 16, y + 16, 0xFF000000);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
