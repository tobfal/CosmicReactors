package at.tobfal.cosmicreactors.init;

import at.tobfal.cosmicreactors.CosmicReactors;

import at.tobfal.cosmicreactors.block.entity.PulsarReactorPortBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CosmicReactors.MODID);

    public static final Supplier<BlockEntityType<PulsarReactorPortBlockEntity>> PULSAR_REACTOR_PORT = BLOCK_ENTITY_TYPES.register(
            "pulsar_reactor_port",
            () -> new BlockEntityType<>(
                PulsarReactorPortBlockEntity::new,
                false,
                ModBlocks.PULSAR_REACTOR_PORT.get()
            )
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
