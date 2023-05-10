package pro.verron.hyrule;

import java.util.Objects;

/**
 * This class represents an id with a given number of characters.
 * It is immutable.
 */
public final class Id
        implements Comparable<Id> {
    private final String format;
    private final int value;

    /**
     * This method will return an id with the given number of characters and the given value.
     *
     * @param nbChar the number of characters of the id
     * @param value  the value of the id
     * @throws AssertionError if the number of characters is not enough to write the value
     */
    public Id(int nbChar, int value) {
        if (nbChar < (int) (Math.log10(value) + 1))
            throw new AssertionError("The seed should be writable with the nb of character expected");
        this.value = value;
        this.format = "%" + '0' + nbChar + "d";
    }

    /**
     * This method will return an id with the given number of characters and the given value.
     *
     * @param nbChar the number of characters of the id
     * @param value  the value of the id
     * @return an id with the given number of characters and the given value
     */
    public static Id of(int nbChar, int value) {
        return new Id(nbChar, value);
    }

    /**
     * This method will return the representation of the id.
     *
     * @return the representation of the id
     */
    public String representation() {
        return String.format(format, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Id.class != o.getClass()) return false;
        Id id = (Id) o;
        return value == id.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(Id o) {
        return o == null ? -1 : this.value - o.value;
    }

    @Override
    public String toString() {
        return String.format("Id{seed=%d}", value);
    }
}
