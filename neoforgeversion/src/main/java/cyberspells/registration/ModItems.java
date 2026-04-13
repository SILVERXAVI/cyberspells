package cyberspells.registration;

import cyberspells.CyberSpellsMod;
import cyberspells.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItems {
        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CyberSpellsMod.MODID);

        public static final DeferredItem<Item> RUNE_INFUSER = ITEMS.register("rune_infuser",
                        () -> new BlockItem(ModBlocks.RUNE_INFUSER.get(), new Item.Properties()));

        // Cyberware Items with Rune Slots
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

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }
}
