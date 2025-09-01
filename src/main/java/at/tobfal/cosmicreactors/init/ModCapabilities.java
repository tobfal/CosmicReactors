package at.tobfal.cosmicreactors.init;

import at.tobfal.cosmicreactors.CosmicReactors;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = CosmicReactors.MODID)
public class ModCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.PENROSE_REACTOR_PORT.get(), (entity, side) -> entity);
    }
}
