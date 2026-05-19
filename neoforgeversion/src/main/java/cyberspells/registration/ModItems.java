package cyberspells.registration;

import cyberspells.CyberSpellsMod;
import cyberspells.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.fml.ModList;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CyberSpellsMod.MODID);

    public static final DeferredItem<Item> RUNE_INFUSER = ITEMS.register("rune_infuser",
            () -> new BlockItem(ModBlocks.RUNE_INFUSER.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        if (ModList.get().isLoaded("createcybernetics")) {
            CCItems.init();
        }
        if (ModList.get().isLoaded("cyber_ware_port")) {
            CWItems.init();
        }
        ITEMS.register(eventBus);
    }

    public static class CCItems {
        public static final DeferredItem<CyberRuneArmItem> RUNE_ARM_LEFT = ITEMS.register("rune_arm_left",
                () -> new CyberRuneArmItem(new Item.Properties().stacksTo(1), "rune_arm_left"));
        public static final DeferredItem<CyberRuneArmItem> RUNE_ARM_RIGHT = ITEMS.register("rune_arm_right",
                () -> new CyberRuneArmItem(new Item.Properties().stacksTo(1), "rune_arm_right"));
        public static final DeferredItem<CyberRuneLegItem> RUNE_LEG_LEFT = ITEMS.register("rune_leg_left",
                () -> new CyberRuneLegItem(new Item.Properties().stacksTo(1), "rune_leg_left"));
        public static final DeferredItem<CyberRuneLegItem> RUNE_LEG_RIGHT = ITEMS.register("rune_leg_right",
                () -> new CyberRuneLegItem(new Item.Properties().stacksTo(1), "rune_leg_right"));
        public static final DeferredItem<CyberRuneHeartItem> RUNE_HEART = ITEMS.register("rune_heart",
                () -> new CyberRuneHeartItem(new Item.Properties().stacksTo(1), "rune_heart"));

        static void init() {}
    }

    public static class CWItems {
        public static final DeferredItem<cyberspells.items.cyberware.CW_RuneArmItem> CW_RUNE_ARM_LEFT = ITEMS.register("cw_rune_arm_left",
                () -> new cyberspells.items.cyberware.CW_RuneArmItem(0, 72, "rune_arm_left"));
        public static final DeferredItem<cyberspells.items.cyberware.CW_RuneArmItem> CW_RUNE_ARM_RIGHT = ITEMS.register("cw_rune_arm_right",
                () -> new cyberspells.items.cyberware.CW_RuneArmItem(0, 72, "rune_arm_right"));
        public static final DeferredItem<cyberspells.items.cyberware.CW_RuneLegItem> CW_RUNE_LEG_LEFT = ITEMS.register("cw_rune_leg_left",
                () -> new cyberspells.items.cyberware.CW_RuneLegItem(0, 90, "rune_leg_left"));
        public static final DeferredItem<cyberspells.items.cyberware.CW_RuneLegItem> CW_RUNE_LEG_RIGHT = ITEMS.register("cw_rune_leg_right",
                () -> new cyberspells.items.cyberware.CW_RuneLegItem(0, 90, "rune_leg_right"));
        public static final DeferredItem<cyberspells.items.cyberware.CW_RuneHeartItem> CW_RUNE_HEART = ITEMS.register("cw_rune_heart",
                () -> new cyberspells.items.cyberware.CW_RuneHeartItem(0, 18, "rune_heart"));

        static void init() {}
    }
}
