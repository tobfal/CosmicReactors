package at.tobfal.cosmicreactors.init;

import at.tobfal.cosmicreactors.command.CosmicReactorsCommand;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommands {
    public static void registerCommands(RegisterCommandsEvent event) {
        CosmicReactorsCommand.register(event.getDispatcher());
    }
}
