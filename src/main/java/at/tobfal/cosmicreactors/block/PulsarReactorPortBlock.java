package at.tobfal.cosmicreactors.block;

import at.tobfal.cosmicreactors.block.entity.PulsarReactorPortBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PulsarReactorPortBlock extends BasePulsarReactorBlock implements EntityBlock {
    public PulsarReactorPortBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PulsarReactorPortBlockEntity(pos, state);
    }
}
