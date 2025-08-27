package at.tobfal.cosmicreactors.datagen;

import at.tobfal.cosmicreactors.CosmicReactors;
import at.tobfal.cosmicreactors.init.ModBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, CosmicReactors.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(ModBlocks.PULSAR_REACTOR_CASING.get());
        blockModels.createTrivialCube(ModBlocks.PULSAR_REACTOR_PORT.get());
    }
}
