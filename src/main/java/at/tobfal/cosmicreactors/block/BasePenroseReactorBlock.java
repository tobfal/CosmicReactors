package at.tobfal.cosmicreactors.block;

import at.tobfal.cosmicreactors.menu.PenroseReactorMenu;
import at.tobfal.cosmicreactors.multiblock.PenroseReactorMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;

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
        serverPlayer.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> new PenroseReactorMenu(id, inv),
                Component.literal("Penrose Reactor")
        ));

        return InteractionResult.SUCCESS;
    }
}
