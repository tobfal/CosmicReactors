package at.tobfal.cosmicreactors.init;

import at.tobfal.cosmicreactors.CosmicReactors;
import at.tobfal.cosmicreactors.command.CosmicReactorsCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = CosmicReactors.MODID)
public class ModCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CosmicReactorsCommand.register(event.getDispatcher());
    }
}
