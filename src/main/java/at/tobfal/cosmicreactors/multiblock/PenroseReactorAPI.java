package at.tobfal.cosmicreactors.multiblock;

import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PenroseReactorAPI {
    private PenroseReactorAPI() {}

    private static MultiblockSavedData getSavedData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(MultiblockSavedData.ID);
    }

    public static Map<UUID, PenroseReactorRecord> getAllReactors(ServerLevel level) {
        var savedData = getSavedData(level);
        return savedData.getPenroseReactors();
    }

    public static PenroseReactorRecord getOrCreateRecord(ServerLevel level, UUID id, ModEnergyStorage energyStorage, List<BlockPos> memberPositions) {
        var savedData = getSavedData(level);
        return savedData.getOrCreatePenroseReactor(id, energyStorage, memberPositions);
    }

    public static PenroseReactorRecord getRecord(ServerLevel level, UUID id) {
        var savedData = getSavedData(level);
        return savedData.getPenroseReactor(id);
    }

    public static void setEnergyStorage(ServerLevel level, UUID id, ModEnergyStorage energyStorage) {
        var savedData = getSavedData(level);
        savedData.setPenroseReactorEnergyStorage(id, energyStorage);
    }

    public static void removeRecord(ServerLevel level, UUID id) {
        var savedData = getSavedData(level);
        savedData.removePenroseReactor(id);
    }

    @Nullable
    public static UUID findReactorId(ServerLevel level, BlockPos pos) {
        var savedData = getSavedData(level);

        for (var record : savedData.getPenroseReactors().values()) {
            if (record.memberPositions().contains(pos)) {
                return record.id();
            }
        }

        return null;
    }
}
