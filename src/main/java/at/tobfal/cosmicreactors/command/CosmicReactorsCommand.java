package at.tobfal.cosmicreactors.command;

import at.tobfal.cosmicreactors.multiblock.PenroseReactorAPI;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.Capabilities;

public class CosmicReactorsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("cosmicreactors")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("debug")
                                .then(Commands.literal("penrosereactors")
                                        .executes(ctx -> listReactors(ctx.getSource()))
                                )
                                .then(Commands.literal("energy")
                                        .then(Commands.literal("get")
                                                .then(Commands.argument("position", BlockPosArgument.blockPos())
                                                        .executes(ctx -> getEnergyAtPosition(ctx.getSource(), BlockPosArgument.getBlockPos(ctx, "position")))
                                                )
                                        )
                                        .then(Commands.literal("extract")
                                                .then(Commands.argument("position", BlockPosArgument.blockPos())
                                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                            .executes(ctx ->
                                                                    extractEnergyAtPosition(ctx.getSource(), BlockPosArgument.getBlockPos(ctx, "position"), IntegerArgumentType.getInteger(ctx, "amount")))
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }

    private static int listReactors(CommandSourceStack source) {
        ServerLevel level = source.getServer().overworld();
        var reactors = PenroseReactorAPI.getAllReactors(level);

        if (reactors.isEmpty()) {
            source.sendSuccess(() -> Component.literal("[CosmicReactors] No Penrose Reactors registered."), false);
        } else {
            source.sendSuccess(() -> Component.literal("[CosmicReactors] Registered Penrose Reactors:"), false);
            for (var entry : reactors.entrySet()) {
                var reactor = entry.getValue();
                source.sendSuccess(() -> Component.literal("- " + reactor.id().toString() + " --- " +  reactor.energyStorage().getEnergyStored()), false);
            }
        }
        return 1;
    }

    private static int getEnergyAtPosition(CommandSourceStack source, BlockPos pos) {
        ServerLevel level = source.getLevel();

        var energyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, Direction.DOWN);

        if (energyStorage == null) {
            source.sendFailure(Component.literal("[CosmicReactors] No energy storage found at " + pos + "."));
            return 1;
        }

        int energy = energyStorage.getEnergyStored();
        source.sendSuccess(() -> Component.literal("[CosmicReactors] Energy level: " + energy + " FE."), false);
        return 1;
    }

    private static int extractEnergyAtPosition(CommandSourceStack source, BlockPos pos, int energy) {
        ServerLevel level = source.getLevel();

        var energyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, Direction.DOWN);

        if (energyStorage == null) {
            source.sendFailure(Component.literal("[CosmicReactors] No energy storage found at " + pos + "."));
            return 1;
        }

        int extracted = energyStorage.extractEnergy(energy, false);
        source.sendSuccess(() -> Component.literal("[CosmicReactors] Extracted " + energy + " FE."), false);
        return 1;
    }
}
