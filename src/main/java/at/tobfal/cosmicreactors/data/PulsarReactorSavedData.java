package at.tobfal.cosmicreactors.data;

import at.tobfal.cosmicreactors.util.AabbCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class PulsarReactorSavedData extends SavedData {
    private final SavedData.Context ctx;
    private final Map<UUID, Record> records = new Object2ObjectOpenHashMap<>();

    public static final SavedDataType<PulsarReactorSavedData> ID = new SavedDataType<>(
            "cosmicreactors/pulsar_reactors",
            PulsarReactorSavedData::new,
            ctx -> RecordCodecBuilder.create(instance -> instance.group(
                    Codec.unboundedMap(UUIDUtil.CODEC, Record.CODEC)
                            .fieldOf("records").forGetter(savedData -> savedData.records)
            ).apply(instance, map -> new PulsarReactorSavedData(ctx, map)))
    );

    public PulsarReactorSavedData(SavedData.Context ctx) {
        this.ctx = ctx;
    }

    public PulsarReactorSavedData(SavedData.Context ctx, Map<UUID, Record> records) {
        this.ctx = ctx;
        this.records.putAll(records);
    }

    public static PulsarReactorSavedData get(MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        return overworld.getDataStorage().computeIfAbsent(ID);
    }

    public static PulsarReactorSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(ID);
    }

    public Set<UUID> getIds() {
        return Collections.unmodifiableSet(records.keySet());
    }

    public void put(UUID id, AABB box) {
        this.records.put(id, new Record(box));
        this.setDirty();
    }

    public Record get(UUID id) {
        return this.records.get(id);
    }

    public boolean remove(UUID id) {
        boolean existed = this.records.remove(id) != null;
        if (existed) this.setDirty();
        return existed;
    }

    public static final class Record {
        public AABB aabb;

        public Record(AABB aabb) {
            this.aabb = aabb;
        }

        public static final Codec<Record> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                AabbCodecs.AABB_CODEC.fieldOf("aabb").forGetter(record -> record.aabb)
        ).apply(instance, Record::new));
    }
}
