package at.tobfal.cosmicreactors.multiblock;

import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.UUID;

public class PulsarReactorAPI {
    private PulsarReactorAPI() {}

    private static MultiblockSavedData getSavedData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(MultiblockSavedData.ID);
    }

    public static Map<UUID, PulsarReactorRecord> getAllReactors(ServerLevel level) {
        var savedData = getSavedData(level);
        return savedData.getPulsarReactors();
    }

    public static PulsarReactorRecord getOrCreateRecord(ServerLevel level, UUID id, ModEnergyStorage energyStorage) {
        var savedData = getSavedData(level);
        return savedData.getOrCreatePulsarReactor(id, energyStorage);
    }

    public static PulsarReactorRecord getRecord(ServerLevel level, UUID id) {
        var savedData = getSavedData(level);
        return savedData.getPulsarReactor(id);
    }

    public static void setEnergyStorage(ServerLevel level, UUID id, ModEnergyStorage energyStorage) {
        var savedData = getSavedData(level);
        savedData.setPulsarReactorEnergyStorage(id, energyStorage);
    }

    public static void removeRecord(ServerLevel level, UUID id) {
        var savedData = getSavedData(level);
        savedData.removePulsarReactor(id);
    }
}
