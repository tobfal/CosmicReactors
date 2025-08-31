package at.tobfal.cosmicreactors.multiblock;

import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record PulsarReactorRecord(UUID id, ModEnergyStorage energyStorage) {
    public static final Codec<PulsarReactorRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(PulsarReactorRecord::id),
            ModEnergyStorage.CODEC.fieldOf("energyStorage").forGetter(PulsarReactorRecord::energyStorage)
    ).apply(instance, PulsarReactorRecord::new));
}
