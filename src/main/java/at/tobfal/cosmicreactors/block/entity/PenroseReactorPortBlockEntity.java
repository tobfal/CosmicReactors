package at.tobfal.cosmicreactors.block.entity;

import at.tobfal.cosmicreactors.init.ModBlockEntities;
import at.tobfal.cosmicreactors.multiblock.PenroseReactorAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.UUID;

public class PenroseReactorPortBlockEntity extends BlockEntity implements IEnergyStorage, ITickableBlockEntity {
    private boolean formed;
    private @Nullable UUID reactorId;

    public PenroseReactorPortBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PENROSE_REACTOR_PORT.get(), pos, state);
    }

    public void setFormed(boolean formed) {
        if (this.formed != formed) {
            this.formed = formed;
            setChanged();
        }
    }

    public boolean isFormed() {
        return formed;
    }

    @Nullable
    public UUID getReactorId() {
        return reactorId;
    }

    public void setReactorId(@Nullable UUID id) {
        this.reactorId = id;
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("formed", formed);
        output.storeNullable("reactorId", UUIDUtil.STRING_CODEC, reactorId);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.formed = input.getBooleanOr("formed", false);
        this.reactorId = input.read("reactorId", UUIDUtil.STRING_CODEC).orElse(null);
    }

    public boolean isReactorPart() {
        return isFormed() && reactorId != null;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        var level = getLevel();
        if (level == null || level.isClientSide() || !isFormed() || !isReactorPart()) {
            return 0;
        }

        ServerLevel serverLevel = (ServerLevel)level;
        var record = PenroseReactorAPI.getRecord(serverLevel, reactorId);
        if (record == null) {
            return 0;
        }

        var energyStorage = record.energyStorage();
        int received = energyStorage.receiveEnergy(toReceive, simulate);

        if (!simulate) {
            PenroseReactorAPI.setEnergyStorage(serverLevel, reactorId, energyStorage);
        }

        return received;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        var level = getLevel();
        if (level == null || level.isClientSide() || !isReactorPart()) {
            return 0;
        }

        ServerLevel serverLevel = (ServerLevel)level;
        var record = PenroseReactorAPI.getRecord(serverLevel, reactorId);
        if (record == null) {
            return 0;
        }

        var energyStorage = record.energyStorage();
        int extracted = energyStorage.extractEnergy(toExtract, simulate);

        if (!simulate) {
            PenroseReactorAPI.setEnergyStorage(serverLevel, reactorId, energyStorage);
        }

        return extracted;
    }

    @Override
    public int getEnergyStored() {
        var level = PenroseReactorPortBlockEntity.this.level;
        if (level == null || level.isClientSide() || !isReactorPart()) {
            return 0;
        }

        ServerLevel serverLevel = (ServerLevel)level;
        var record = PenroseReactorAPI.getRecord(serverLevel, reactorId);
        if (record == null) {
            return 0;
        }

        var energyStorage = record.energyStorage();
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        var level = PenroseReactorPortBlockEntity.this.level;
        if (level == null || level.isClientSide() || !isReactorPart()) {
            return 0;
        }

        ServerLevel serverLevel = (ServerLevel)level;
        var record = PenroseReactorAPI.getRecord(serverLevel, reactorId);
        if (record == null) {
            return 0;
        }

        var energyStorage = record.energyStorage();
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        var level = PenroseReactorPortBlockEntity.this.level;
        if (level == null || level.isClientSide() || !isReactorPart()) {
            return false;
        }

        ServerLevel serverLevel = (ServerLevel)level;
        var record = PenroseReactorAPI.getRecord(serverLevel, reactorId);
        if (record == null) {
            return false;
        }

        var energyStorage = record.energyStorage();
        return energyStorage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState state) {
        if (level.isClientSide() || !isReactorPart()) {
            return;
        }

        transferEnergy(level, blockPos, state, this);
    }

    private static void transferEnergy(Level level, BlockPos blockPos, BlockState state, PenroseReactorPortBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        for (Direction direction : Direction.values()) {
            BlockPos testPos = blockPos.relative(direction);
            BlockEntity testBlockEntity = level.getBlockEntity(testPos);
            if (testBlockEntity == null) {
                continue;
            }

            IEnergyStorage foreignEnergyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, testPos, level.getBlockState(testPos), testBlockEntity, direction.getOpposite());
            if (foreignEnergyStorage == null || !foreignEnergyStorage.canReceive()) {
                continue;
            }

            int toExtract = blockEntity.extractEnergy(blockEntity.getEnergyStored(), true);
            toExtract = foreignEnergyStorage.receiveEnergy(toExtract, false);
            if (toExtract <= 0) {
                continue;
            }

            blockEntity.extractEnergy(toExtract, false);
        }
    }
}
