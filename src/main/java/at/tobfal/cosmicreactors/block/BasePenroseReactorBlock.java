package at.tobfal.cosmicreactors.block;

import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import at.tobfal.cosmicreactors.energy.ModMassStorage;
import at.tobfal.cosmicreactors.menu.PenroseReactorMenu;
import at.tobfal.cosmicreactors.multiblock.PenroseReactorAPI;
import at.tobfal.cosmicreactors.multiblock.PenroseReactorMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;

import java.util.UUID;

public class BasePenroseReactorBlock extends Block {
    public BasePenroseReactorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            PenroseReactorMultiblock.rebuildAt((ServerLevel) level, pos);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        if (!level.isClientSide) {
            PenroseReactorMultiblock.rebuildAt((ServerLevel) level, pos);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;
        ServerLevel serverLevel = (ServerLevel) level;
        UUID reactorId = PenroseReactorAPI.findReactorId(serverLevel, pos);
        if (reactorId == null) {
            return InteractionResult.PASS;
        }

        ContainerData containerData = new ContainerData() {
            @Override
            public int get(int index) {
                var reactor = PenroseReactorAPI.getRecord(serverLevel, reactorId);
                ModEnergyStorage energyStorage = reactor.energyStorage();
                ModMassStorage  massStorage = reactor.massStorage();
                return switch (index) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> energyStorage.getMaxEnergyStored();
                    case 2 -> massStorage.getMass();
                    case 3 -> massStorage.getCriticalMass();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        serverPlayer.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> new PenroseReactorMenu(id, inv, reactorId, containerData), Component.literal("Penrose Reactor")),
                (RegistryFriendlyByteBuf buf) -> buf.writeUUID(reactorId)
        );
        return InteractionResult.SUCCESS;
    }
}
