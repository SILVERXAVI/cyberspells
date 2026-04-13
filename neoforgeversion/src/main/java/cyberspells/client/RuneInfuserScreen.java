package cyberspells.client;

import cyberspells.menu.RuneInfuserMenu;
import cyberspells.items.RuneHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

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

        // Draw Main Background
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF141414);

        // Neon Border
        graphics.fill(x - 1, y - 1, x, y + imageHeight + 1, 0xFF00D1FF);
        graphics.fill(x + imageWidth, y - 1, x + imageWidth + 1, y + imageHeight + 1, 0xFF00D1FF);
        graphics.fill(x, y - 1, x + imageWidth, y, 0xFF00D1FF);
        graphics.fill(x, y + imageHeight, x + imageWidth, y + imageHeight + 1, 0xFF00D1FF);

        // Header
        graphics.fill(x + 5, y + 5, x + imageWidth - 5, y + 18, 0xFF1A1A1A);
        graphics.fill(x + 5, y + 18, x + imageWidth - 5, y + 19, 0xFF00D1FF);

        // Input Slot
        drawGlowingSlot(graphics, x + 80, y + 20, 0xFF00D1FF);

        // Rune Slots Logic
        int activeSlots = 0;
        ItemStack inputStack = menu.getSlot(0).getItem();
        if (!inputStack.isEmpty() && inputStack.getItem() instanceof RuneHolder runeItem) {
            activeSlots = runeItem.getMaxRuneSlots();
        }

        // Connector lines
        if (activeSlots > 0) {
            int leftmostX = 40 + 8;
            int rightmostX = 40 + ((activeSlots - 1) * 20) + 8;
            graphics.fill(x + Math.min(leftmostX, 88), y + 38, x + Math.max(rightmostX, 88) + 1, y + 40, 0xFF00D1FF);
        }

        for (int i = 0; i < 5; i++) {
            int slotX = x + 40 + (i * 20);
            int slotY = y + 50;

            if (i < activeSlots) {
                graphics.fill(slotX + 7, y + 38, slotX + 9, slotY, 0xFF00D1FF);
                drawGlowingSlot(graphics, slotX, slotY, 0xFF0080FF);
            } else {
                drawSimpleSlot(graphics, slotX, slotY, 0xFF333333);
            }
        }

        // Output Slot
        drawGlowingSlot(graphics, x + 145, y + 35, 0xFFFF00D1);

        // Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSimpleSlot(graphics, x + 8 + col * 18, y + 84 + row * 18, 0xFF333333);
            }
        }
        for (int col = 0; col < 9; col++) {
            drawSimpleSlot(graphics, x + 8 + col * 18, y + imageHeight - 24, 0xFF00D1FF);
        }
    }

    private void drawSimpleSlot(GuiGraphics graphics, int x, int y, int borderColor) {
        graphics.fill(x - 1, y - 1, x + 17, y + 17, borderColor);
        graphics.fill(x, y, x + 16, y + 16, 0xFF000000);
    }

    private void drawGlowingSlot(GuiGraphics graphics, int x, int y, int glowColor) {
        graphics.fill(x - 1, y - 1, x + 17, y + 17, glowColor);
        graphics.fill(x, y, x + 16, y + 16, 0xFF000000);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
