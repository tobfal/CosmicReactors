package at.tobfal.cosmicreactors.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickableBlockEntity {
    static <T extends BlockEntity> BlockEntityTicker<T> getTickerHelper(Level pLevel) {
        return pLevel.isClientSide() ? null : (level0, pos0, state0, blockEntity) -> ((ITickableBlockEntity) blockEntity).tick(level0, pos0, state0);
    }

    void tick(Level level, BlockPos pos, BlockState state);
}
