package at.tobfal.cosmicreactors.command;

import at.tobfal.cosmicreactors.data.PulsarReactorSavedData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class CosmicReactorsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("cosmicreactors")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("debug")
                                .then(Commands.literal("pulsarreactors")
                                        .executes(ctx -> listReactors(ctx.getSource()))
                                )
                        )
        );
    }

    private static int listReactors(CommandSourceStack source) {
        ServerLevel level = source.getServer().overworld();
        PulsarReactorSavedData data = PulsarReactorSavedData.get(level);

        if (data.getIds().isEmpty()) {
            source.sendSuccess(() -> Component.literal("[CosmicReactors] No Pulsar Reactors registered."), false);
        } else {
            source.sendSuccess(() -> Component.literal("[CosmicReactors] Registered Pulsar Reactors:"), false);
            for (UUID id : data.getIds()) {
                source.sendSuccess(() -> Component.literal("- " + id.toString()), false);
            }
        }
        return 1;
    }
}
