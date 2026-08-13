/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.common.EventBusSubscriber$Bus
 *  net.neoforged.fml.loading.FMLLoader
 *  net.neoforged.neoforge.client.event.InputEvent$InteractionKeyMappingTriggered
 *  net.neoforged.neoforge.client.event.InputEvent$Key
 *  net.neoforged.neoforge.client.event.InputEvent$MouseButton$Pre
 *  net.neoforged.neoforge.client.event.InputEvent$MouseScrollingEvent
 *  net.neoforged.neoforge.network.PacketDistributor
 */
package io.redspace.ironsspellbooks.player;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay;
import io.redspace.ironsspellbooks.gui.overlays.SpellWheelOverlay;
import io.redspace.ironsspellbooks.network.casting.CastPacket;
import io.redspace.ironsspellbooks.network.casting.QuickCastPacket;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import io.redspace.ironsspellbooks.player.KeyMappings;
import io.redspace.ironsspellbooks.player.KeyState;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid="irons_spellbooks", bus=EventBusSubscriber.Bus.GAME, value={Dist.CLIENT})
public final class ClientInputEvents {
    private static final ArrayList<KeyState> KEY_STATES = new ArrayList();
    private static final KeyState SPELL_WHEEL_STATE = ClientInputEvents.register(KeyMappings.SPELL_WHEEL_KEYMAP);
    private static final KeyState SPELL_WHEEL_TOGGLE_STATE = ClientInputEvents.register(KeyMappings.SPELL_WHEEL_TOGGLE_KEYMAP);
    private static final KeyState SPELLBAR_MODIFIER_STATE = ClientInputEvents.register(KeyMappings.SPELLBAR_SCROLL_MODIFIER_KEYMAP);
    private static final KeyState SPELLBOOK_CAST_STATE = ClientInputEvents.register(KeyMappings.SPELLBOOK_CAST_ACTIVE_KEYMAP);
    private static final List<KeyState> QUICK_CAST_STATES = ClientInputEvents.registerQuickCast(KeyMappings.QUICK_CAST_MAPPINGS);
    private static int useKeyId = Integer.MIN_VALUE;
    public static boolean isUseKeyDown;
    public static boolean hasReleasedSinceCasting;
    public static boolean isShiftKeyDown;

    @SubscribeEvent
    public static void clientMouseScrolled(InputEvent.MouseScrollingEvent event) {
        SpellSelectionManager spellSelectionManager;
        Player player = MinecraftInstanceHelper.getPlayer();
        if (player == null) {
            return;
        }
        if (Minecraft.getInstance().screen == null && SPELLBAR_MODIFIER_STATE.isHeld() && (spellSelectionManager = ClientMagicData.getSpellSelectionManager()).getSpellCount() > 0) {
            int direction = Mth.clamp((int)((int)event.getScrollDeltaY()), (int)-1, (int)1);
            List<SpellSelectionManager.SelectionOption> spellbookSpells = spellSelectionManager.getAllSpells();
            int spellCount = spellbookSpells.size();
            int scrollIndex = Mth.clamp((int)spellSelectionManager.getSelectionIndex(), (int)0, (int)spellCount) - direction;
            int selectedIndex = (Mth.clamp((int)scrollIndex, (int)-1, (int)(spellCount + 1)) + spellCount) % spellCount;
            spellSelectionManager.makeSelection(selectedIndex);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onUseInput(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isUseItem()) {
            if (ClientSpellCastHelper.shouldSuppressRightClicks()) {
                event.setSwingHand(false);
                event.setCanceled(true);
            }
        } else if (event.isAttack() && ClientMagicData.isCasting()) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (!FMLLoader.isProduction() && event.getKey() == 329 && event.getAction() == 1) {
            IronsSpellbooks.LOGGER.debug("breakpoint");
        }
        ClientInputEvents.handleInputEvent(event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        ClientInputEvents.handleInputEvent(event.getButton(), event.getAction());
    }

    private static void handleInputEvent(int button, int action) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        ClientInputEvents.handleRightClickSuppression(button, action);
        if (button == 340) {
            isShiftKeyDown = action >= 1;
        }
        for (int i = 0; i < QUICK_CAST_STATES.size(); ++i) {
            if (!QUICK_CAST_STATES.get(i).wasPressed()) continue;
            PacketDistributor.sendToServer((CustomPacketPayload)new QuickCastPacket(i), (CustomPacketPayload[])new CustomPacketPayload[0]);
            break;
        }
        if (SPELLBOOK_CAST_STATE.wasPressed() && minecraft.screen == null) {
            PacketDistributor.sendToServer((CustomPacketPayload)new CastPacket(), (CustomPacketPayload[])new CustomPacketPayload[0]);
        }
        if (SPELL_WHEEL_STATE.wasPressed() && minecraft.screen == null) {
            SpellWheelOverlay.instance.open();
        }
        if (SPELL_WHEEL_STATE.wasReleased() && minecraft.screen == null && SpellWheelOverlay.instance.active) {
            SpellWheelOverlay.instance.close();
        }
        if (SPELL_WHEEL_TOGGLE_STATE.wasPressed() && minecraft.screen == null) {
            if (SpellWheelOverlay.instance.active) {
                SpellWheelOverlay.instance.close();
            } else {
                SpellWheelOverlay.instance.open();
            }
        }
        if (SPELLBAR_MODIFIER_STATE.isHeld() && ((ManaBarOverlay.Display)((Object)ClientConfigs.SPELL_BAR_DISPLAY.get())).equals((Object)ManaBarOverlay.Display.Contextual)) {
            SpellBarOverlay.fadeoutDelay = 40;
        }
        ClientInputEvents.update();
    }

    private static void handleRightClickSuppression(int button, int action) {
        if (useKeyId == Integer.MIN_VALUE) {
            useKeyId = Minecraft.getInstance().options.keyUse.getKey().getValue();
        }
        if (button == useKeyId) {
            if (action == 0) {
                ClientSpellCastHelper.setSuppressRightClicks(false);
                isUseKeyDown = false;
                hasReleasedSinceCasting = true;
            } else if (action == 1) {
                isUseKeyDown = true;
            }
        }
    }

    private static void update() {
        for (KeyState k : KEY_STATES) {
            k.update();
        }
    }

    private static KeyState register(KeyMapping key) {
        KeyState k = new KeyState(key);
        KEY_STATES.add(k);
        return k;
    }

    private static List<KeyState> registerQuickCast(List<KeyMapping> mappings) {
        ArrayList<KeyState> keyStates = new ArrayList<KeyState>();
        mappings.forEach(keyMapping -> {
            KeyState k = new KeyState((KeyMapping)keyMapping);
            KEY_STATES.add(k);
            keyStates.add(k);
        });
        return keyStates;
    }
}

