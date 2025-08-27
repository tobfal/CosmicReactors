package at.tobfal.cosmicreactors;

import at.tobfal.cosmicreactors.init.ModBlockEntities;
import at.tobfal.cosmicreactors.init.ModBlocks;
import at.tobfal.cosmicreactors.init.ModItems;
import net.neoforged.fml.ModList;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CosmicReactors.MODID)
public class CosmicReactors {
    public static final String MODID = "cosmicreactors";
    public static final String MODNAME = ModList.get().getModFileById(MODID).moduleName();
    public static final String VERSION = ModList.get().getModFileById(MODID).versionString();

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public CosmicReactors(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
}
