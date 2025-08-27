package at.tobfal.cosmicreactors.init;

import at.tobfal.cosmicreactors.CosmicReactors;
import at.tobfal.cosmicreactors.block.BasePulsarReactorBlock;
import at.tobfal.cosmicreactors.block.PulsarReactorPortBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CosmicReactors.MODID);

    // Pulsar Fragment Reactor
    public static final DeferredBlock<Block> PULSAR_REACTOR_CASING = registerBlock("pulsar_reactor_casing",
            BasePulsarReactorBlock::new);
    public static final DeferredBlock<Block> PULSAR_REACTOR_PORT = registerBlock("pulsar_reactor_port",
            PulsarReactorPortBlock::new);

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
