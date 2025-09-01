package at.tobfal.cosmicreactors.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.energy.EnergyStorage;

public class ModEnergyStorage extends EnergyStorage {
    public static final Codec<ModEnergyStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("capacity").forGetter(ModEnergyStorage::getMaxEnergyStored),
            Codec.INT.fieldOf("maxReceive").forGetter(ModEnergyStorage::getMaxReceive),
            Codec.INT.fieldOf("maxExtract").forGetter(ModEnergyStorage::getMaxExtract),
            Codec.INT.fieldOf("stored").forGetter(ModEnergyStorage::getEnergyStored)
    ).apply(instance, ModEnergyStorage::new));

    public ModEnergyStorage(int capacity, int maxReceive, int maxExtract, int stored) {
        super(capacity, maxReceive, maxExtract, stored);
    }

    public ModEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }
}
