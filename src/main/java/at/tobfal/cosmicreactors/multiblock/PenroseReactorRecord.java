package at.tobfal.cosmicreactors.multiblock;

import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import at.tobfal.cosmicreactors.energy.ModMassStorage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;

import java.util.List;
import java.util.UUID;

public record PenroseReactorRecord(UUID id, ModEnergyStorage energyStorage, ModMassStorage massStorage, List<BlockPos> memberPositions) {
    public static final Codec<PenroseReactorRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(PenroseReactorRecord::id),
            ModEnergyStorage.CODEC.fieldOf("energyStorage").forGetter(PenroseReactorRecord::energyStorage),
            ModMassStorage.CODEC.fieldOf("massStorage").forGetter(PenroseReactorRecord::massStorage),
            BlockPos.CODEC.listOf().fieldOf("memberPositions").forGetter(PenroseReactorRecord::memberPositions)
    ).apply(instance, PenroseReactorRecord::new));
}
