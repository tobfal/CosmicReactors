package at.tobfal.cosmicreactors.block.entity;

import at.tobfal.cosmicreactors.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class PulsarReactorPortBlockEntity extends BlockEntity {
    private boolean formed;

    public PulsarReactorPortBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PULSAR_REACTOR_PORT.get(), pos, state);
    }

    public void setFormed(boolean formed) {
        if (this.formed != formed) {
            this.formed = formed;
            setChanged();
        }
    }

    public boolean isFormed() { return formed; }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("formed", formed);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.formed = input.getBooleanOr("formed", false);
    }
}
