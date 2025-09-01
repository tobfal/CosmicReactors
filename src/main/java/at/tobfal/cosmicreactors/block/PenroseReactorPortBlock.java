package at.tobfal.cosmicreactors.block;

import at.tobfal.cosmicreactors.block.entity.ITickableBlockEntity;
import at.tobfal.cosmicreactors.block.entity.PenroseReactorPortBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PenroseReactorPortBlock extends BasePenroseReactorBlock implements EntityBlock {
    public PenroseReactorPortBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PenroseReactorPortBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return ITickableBlockEntity.getTickerHelper(pLevel);
    }
}
