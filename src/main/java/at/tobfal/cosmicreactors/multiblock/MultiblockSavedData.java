package at.tobfal.cosmicreactors.multiblock;

import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MultiblockSavedData extends SavedData {
    public static final Codec<MultiblockSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, PulsarReactorRecord.CODEC)
                    .fieldOf("pulsar_reactors")
                    .forGetter(savedData -> savedData.pulsarReactors)
    ).apply(instance, MultiblockSavedData::new));

    public static final SavedDataType<MultiblockSavedData> ID = new SavedDataType<>("cosmicreactors/multiblocks", MultiblockSavedData::new, CODEC);

    private final Map<UUID, PulsarReactorRecord> pulsarReactors;

    public MultiblockSavedData() {
        this.pulsarReactors = new HashMap<>();
    }

    public MultiblockSavedData(Map<UUID, PulsarReactorRecord> reactors) {
        this.pulsarReactors = new HashMap<>(reactors);
    }

    public Map<UUID, PulsarReactorRecord> getPulsarReactors() {
        return pulsarReactors;
    }

    public PulsarReactorRecord getOrCreatePulsarReactor(UUID id, ModEnergyStorage energyStorage) {
        var record = getPulsarReactor(id);
        if (record == null) {
            record = new PulsarReactorRecord(id, energyStorage);
            pulsarReactors.put(id, record);
            setDirty();
        }

        return record;
    }

    public PulsarReactorRecord getPulsarReactor(UUID id) {
        return pulsarReactors.get(id);
    }

    public void setPulsarReactorEnergyStorage(UUID id, ModEnergyStorage energyStorage) {
        PulsarReactorRecord record = pulsarReactors.get(id);
        if (record == null){
            return;
        }

        PulsarReactorRecord updatedRecord = new PulsarReactorRecord(record.id(), energyStorage);
        pulsarReactors.put(id, updatedRecord);
        setDirty();
    }

    public void removePulsarReactor(UUID id) {
        var r = getPulsarReactor(id);
        if (r == null) {
            return;
        }

        pulsarReactors.remove(id);
        setDirty();
    }
}
