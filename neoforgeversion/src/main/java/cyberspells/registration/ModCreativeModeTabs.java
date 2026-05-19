package cyberspells.registration;

import cyberspells.CyberSpellsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, CyberSpellsMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CYBER_ADDON_TAB = CREATIVE_MODE_TABS.register(
            "cyber_addon_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.RUNE_INFUSER.get()))
                    .title(Component.translatable("creativetab.cyber_addon_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.RUNE_INFUSER.get());
                        if (net.neoforged.fml.ModList.get().isLoaded("createcybernetics")) {
                            pOutput.accept(ModItems.CCItems.RUNE_ARM_LEFT.get());
                            pOutput.accept(ModItems.CCItems.RUNE_ARM_RIGHT.get());
                            pOutput.accept(ModItems.CCItems.RUNE_LEG_LEFT.get());
                            pOutput.accept(ModItems.CCItems.RUNE_LEG_RIGHT.get());
                            pOutput.accept(ModItems.CCItems.RUNE_HEART.get());
                        }
                        if (net.neoforged.fml.ModList.get().isLoaded("cyber_ware_port")) {
                            pOutput.accept(ModItems.CWItems.CW_RUNE_ARM_LEFT.get());
                            pOutput.accept(ModItems.CWItems.CW_RUNE_ARM_RIGHT.get());
                            pOutput.accept(ModItems.CWItems.CW_RUNE_LEG_LEFT.get());
                            pOutput.accept(ModItems.CWItems.CW_RUNE_LEG_RIGHT.get());
                            pOutput.accept(ModItems.CWItems.CW_RUNE_HEART.get());
                        }
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
