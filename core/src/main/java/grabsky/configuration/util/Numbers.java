package grabsky.configuration.util;

public final class Numbers {

    public static Short toShort(final String num) throws NumberFormatException {
        final long lon = Long.parseLong(num);
        if (lon > Short.MAX_VALUE) return Short.MAX_VALUE;
        if (lon < Short.MIN_VALUE) return Short.MIN_VALUE;
        return (short) lon;
    }

    public static Integer toInteger(final String num) throws NumberFormatException {
        final long lon = Long.parseLong(num);
        if (lon > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (lon < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        return (int) lon;
    }

    public static Float toFloat(final String num) throws NumberFormatException {
        final double doubl = Double.parseDouble(num);
        if (doubl > Float.MAX_VALUE) return Float.MAX_VALUE;
        if (doubl < Float.MIN_VALUE) return Float.MIN_VALUE;
        return (float) doubl;
    }
}
