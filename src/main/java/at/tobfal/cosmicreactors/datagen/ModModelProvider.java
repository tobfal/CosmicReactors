package at.tobfal.cosmicreactors.datagen;

import at.tobfal.cosmicreactors.CosmicReactors;
import at.tobfal.cosmicreactors.init.ModBlocks;
import at.tobfal.cosmicreactors.init.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, CosmicReactors.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.DENSE_MATTER_PARTICLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.DENSE_MATTER_CLUMP.get(), ModelTemplates.FLAT_ITEM);

        blockModels.createTrivialCube(ModBlocks.PENROSE_REACTOR_CASING.get());
        blockModels.createTrivialBlock(
                ModBlocks.PENROSE_REACTOR_GLASS.get(),
                TexturedModel.CUBE.updateTemplate(t -> t.extend().renderType("minecraft:translucent").build())
        );
        blockModels.createTrivialCube(ModBlocks.PENROSE_REACTOR_PORT.get());
    }
}
