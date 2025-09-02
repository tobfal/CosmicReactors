package at.tobfal.cosmicreactors.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

public class ModMassStorage {
    public static final Codec<ModMassStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("criticalMass").forGetter(ModMassStorage::getCriticalMass),
            Codec.INT.fieldOf("mass").forGetter(ModMassStorage::getMass)
    ).apply(instance, ModMassStorage::new));

    protected int mass;
    protected int criticalMass;

    public ModMassStorage() {
        this(-1);
    }

    public ModMassStorage(int criticalMass) {
        this(criticalMass, 0);
    }

    public ModMassStorage(int criticalMass, int mass) {
        this.mass = mass;
        this.criticalMass = criticalMass;
    }

    public int addMass(int toAdd, boolean simulate) {
        if (toAdd <= 0) {
            return 0;
        }

        int massAdded = criticalMass >= 0 ? Mth.clamp(this.criticalMass - this.mass, 0, toAdd) : toAdd;
        if (!simulate) {
            this.mass += massAdded;
        }

        return massAdded;
    }

    public int subtractMass(int toSubtract, boolean simulate) {
        if (toSubtract <= 0) {
            return 0;
        }

        int massSubtracted = Math.min(this.mass, toSubtract);
        if (!simulate)
            this.mass -= massSubtracted;
        return massSubtracted;
    }

    public int getMass() {
        return this.mass;
    }

    public int getCriticalMass() {
        return this.criticalMass;
    }
}
