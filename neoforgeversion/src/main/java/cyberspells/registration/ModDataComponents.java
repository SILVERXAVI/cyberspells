package cyberspells.registration;

import cyberspells.CyberSpellsMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import java.util.List;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister
            .create(Registries.DATA_COMPONENT_TYPE, CyberSpellsMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<String>>> RUNES = DATA_COMPONENT_TYPES
            .register("runes", () -> DataComponentType.<List<String>>builder()
                    .persistent(Codec.list(Codec.STRING))
                    .networkSynchronized(ByteBufCodecs.stringUtf8(128).apply(ByteBufCodecs.list()))
                    .build());

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
