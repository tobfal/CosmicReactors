package at.tobfal.cosmicreactors.multiblock;

import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record PenroseReactorRecord(UUID id, ModEnergyStorage energyStorage) {
    public static final Codec<PenroseReactorRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(PenroseReactorRecord::id),
            ModEnergyStorage.CODEC.fieldOf("energyStorage").forGetter(PenroseReactorRecord::energyStorage)
    ).apply(instance, PenroseReactorRecord::new));
}
