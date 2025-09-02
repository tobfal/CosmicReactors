package at.tobfal.cosmicreactors.util;

public class PenroseReactorUtil {
    public static int getEnergyPerTick(int mass) {
        if (mass <= 0) {
            return 0;
        }

        return (int) Math.ceil(1e-9 * Math.pow(mass, 2));
    }

    public static int getMassLossPerTick(int mass) {
        if (mass <= 0) {
            return 0;
        }

        return (int) Math.ceil(getEnergyPerTick(mass) / 0.9);
    }
}
