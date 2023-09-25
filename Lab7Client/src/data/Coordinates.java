package data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class containing the coordinates of the apartment
 */
public class Coordinates implements Serializable {
    /**
     * A field of the Coordinates class, which is X coordinate
     * The field value is greater than -970
     */
    private long x;
    /**
     * A field of the Coordinates class, which is Y coordinate
     * The maximum value of the field is 113
     */
    private int y;

    /**
     * Constructor of the Coordinates class
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Coordinates(long x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Function for getting the field value {@link Coordinates#x}
     * @return X coordinate
     */
    public Long getX() {
        return x;
    }

    /**
     * Field value setting function {@link Coordinates#x}
     * @param x X coordinate
     */
    public void setX(long x) {
        this.x = x;
    }

    /**
     * Function for getting the field value {@link Coordinates#y}
     * @return Y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Field value setting function {@link Coordinates#y}
     * @param y Y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" + "x = " + x + ", y = " + y + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates that)) return false;
        return Long.compare(that.x, x) == 0 && Integer.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}



