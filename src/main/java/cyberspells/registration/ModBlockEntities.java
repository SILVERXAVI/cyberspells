package cyberspells.registration;

import cyberspells.CyberSpellsMod;
import cyberspells.block.entity.RuneInfuserBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, CyberSpellsMod.MODID);

    public static final RegistryObject<BlockEntityType<RuneInfuserBlockEntity>> RUNE_INFUSER_BE = BLOCK_ENTITIES
            .register("rune_infuser_be", () -> BlockEntityType.Builder.of(RuneInfuserBlockEntity::new,
                    ModBlocks.RUNE_INFUSER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
