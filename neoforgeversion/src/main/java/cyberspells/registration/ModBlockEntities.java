package cyberspells.registration;

import cyberspells.CyberSpellsMod;
import cyberspells.block.entity.RuneInfuserBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CyberSpellsMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RuneInfuserBlockEntity>> RUNE_INFUSER_BE = BLOCK_ENTITIES
            .register("rune_infuser_be", () -> BlockEntityType.Builder.of(RuneInfuserBlockEntity::new,
                    ModBlocks.RUNE_INFUSER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
