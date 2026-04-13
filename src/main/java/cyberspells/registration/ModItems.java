package cyberspells.registration;

import cyberspells.CyberSpellsMod;
import cyberspells.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        CyberSpellsMod.MODID);

        public static final RegistryObject<Item> RUNE_INFUSER = ITEMS.register("rune_infuser",
                        () -> new BlockItem(ModBlocks.RUNE_INFUSER.get(), new Item.Properties()));

        // Cyberware Items with Rune Slots
        public static final RegistryObject<Item> RUNE_ARM_LEFT = ITEMS.register("rune_arm_left",
                        () -> new CyberRuneArmItem(new Item.Properties().stacksTo(1), "arm_left"));
        public static final RegistryObject<Item> RUNE_ARM_RIGHT = ITEMS.register("rune_arm_right",
                        () -> new CyberRuneArmItem(new Item.Properties().stacksTo(1), "arm_right"));
        public static final RegistryObject<Item> RUNE_LEG_LEFT = ITEMS.register("rune_leg_left",
                        () -> new CyberRuneLegItem(new Item.Properties().stacksTo(1), "leg_left"));
        public static final RegistryObject<Item> RUNE_LEG_RIGHT = ITEMS.register("rune_leg_right",
                        () -> new CyberRuneLegItem(new Item.Properties().stacksTo(1), "leg_right"));
        public static final RegistryObject<Item> RUNE_HEART = ITEMS.register("rune_heart",
                        () -> new CyberRuneHeartItem(new Item.Properties().stacksTo(1), "heart"));

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }
}
