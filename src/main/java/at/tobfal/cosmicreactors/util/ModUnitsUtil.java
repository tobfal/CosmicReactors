package at.tobfal.cosmicreactors.util;

public class ModUnitsUtil {
    private static final String[] SUFFIXES = { "", "k", "M", "G", "T" };

    public static String format(int value, String baseUnit) {
        double d = value;
        int unit = 0;

        while (d >= 1000 && unit < SUFFIXES.length - 1) {
            d /= 1000.0;
            unit++;
        }

        if (unit == 0) {
            return String.format("%d %s", value, baseUnit);
        }

        return String.format("%.1f %s%s", d, SUFFIXES[unit], baseUnit);
    }
}
