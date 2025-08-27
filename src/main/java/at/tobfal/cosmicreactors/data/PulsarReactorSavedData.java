package at.tobfal.cosmicreactors.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PulsarReactorSavedData extends SavedData {
    private final SavedData.Context ctx;
    private final LinkedHashSet<UUID> pulsarReactors = new LinkedHashSet<>();

    public static final SavedDataType<PulsarReactorSavedData> ID = new SavedDataType<>(
            "cosmicreactors/pulsar_reactors",
            PulsarReactorSavedData::new,
            ctx -> RecordCodecBuilder.create(instance -> instance.group(
                    RecordCodecBuilder.point(ctx),
                    Codec.list(UUIDUtil.CODEC).xmap(LinkedHashSet::new, List::copyOf)
                            .fieldOf("pulsarReactors").forGetter(savedData -> savedData.pulsarReactors)
            ).apply(instance, PulsarReactorSavedData::new))
    );

    public PulsarReactorSavedData(SavedData.Context ctx) {
        this.ctx = ctx;
    }

    public PulsarReactorSavedData(SavedData.Context ctx, LinkedHashSet<UUID> pulsarReactors) {
        this.ctx = ctx;
        this.pulsarReactors.addAll(pulsarReactors);
    }

    public boolean add(UUID id) {
        boolean added = pulsarReactors.add(id);
        if (added) {
            setDirty();
        }

        return added;
    }

    public boolean remove(UUID id) {
        boolean removed = pulsarReactors.remove(id);
        if (removed) {
            setDirty();
        }

        return removed;
    }

    public boolean contains(UUID id) {
        return pulsarReactors.contains(id);
    }

    public LinkedHashSet<UUID> all() {
        return new LinkedHashSet<>(pulsarReactors);
    }

    public static PulsarReactorSavedData get(MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        return overworld.getDataStorage().computeIfAbsent(ID);
    }

    public static PulsarReactorSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(ID);
    }
}
