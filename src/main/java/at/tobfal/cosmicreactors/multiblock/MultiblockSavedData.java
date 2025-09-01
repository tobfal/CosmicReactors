package at.tobfal.cosmicreactors.multiblock;

import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MultiblockSavedData extends SavedData {
    public static final Codec<MultiblockSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, PenroseReactorRecord.CODEC)
                    .fieldOf("penrose_reactors")
                    .forGetter(savedData -> savedData.penroseReactors)
    ).apply(instance, MultiblockSavedData::new));

    public static final SavedDataType<MultiblockSavedData> ID = new SavedDataType<>("cosmicreactors/multiblocks", MultiblockSavedData::new, CODEC);

    private final Map<UUID, PenroseReactorRecord> penroseReactors;

    public MultiblockSavedData() {
        this.penroseReactors = new HashMap<>();
    }

    public MultiblockSavedData(Map<UUID, PenroseReactorRecord> reactors) {
        this.penroseReactors = new HashMap<>(reactors);
    }

    public Map<UUID, PenroseReactorRecord> getPenroseReactors() {
        return penroseReactors;
    }

    public PenroseReactorRecord getOrCreatePenroseReactor(UUID id, ModEnergyStorage energyStorage) {
        var record = getPenroseReactor(id);
        if (record == null) {
            record = new PenroseReactorRecord(id, energyStorage);
            penroseReactors.put(id, record);
            setDirty();
        }

        return record;
    }

    public PenroseReactorRecord getPenroseReactor(UUID id) {
        return penroseReactors.get(id);
    }

    public void setPenroseReactorEnergyStorage(UUID id, ModEnergyStorage energyStorage) {
        PenroseReactorRecord record = penroseReactors.get(id);
        if (record == null){
            return;
        }

        PenroseReactorRecord updatedRecord = new PenroseReactorRecord(record.id(), energyStorage);
        penroseReactors.put(id, updatedRecord);
        setDirty();
    }

    public void removePenroseReactor(UUID id) {
        var r = getPenroseReactor(id);
        if (r == null) {
            return;
        }

        penroseReactors.remove(id);
        setDirty();
    }
}
