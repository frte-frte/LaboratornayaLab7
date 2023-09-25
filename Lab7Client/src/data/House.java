package data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class containing the information about the house
 */
public class House implements Serializable {
    /**
     * A field of the House class, which is name of the house
     * The value of the field is not null
     */
    private String name;
    /**
     * A field of the House class, which is age of the house
     * The field value is greater than 0
     */
    private Integer year;
    /**
     * A field of the House class, which is number of flats on floor in the house
     * The field value is greater than 0
     */
    private long numberOfFlatsOnFloor;

    /**
     * Constructor of the House class
     * @param name name of the house
     * @param year age of the house
     * @param numberOfFlatsOnFloor number of flats on floor in the house
     */
    public House(String name, Integer year, long numberOfFlatsOnFloor){
        this.name = name;
        this.year = year;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    /**
     * Function for getting the field value {@link House#name}
     * @return name of the house
     */
    public String getName(){
        return  name;
    }

    /**
     * Field value setting function {@link House#name}
     * @param name name of the house
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Function for getting the field value {@link House#year}
     * @return age of the house
     */
    public Integer getYear(){
        return year;
    }

    /**
     * Field value setting function {@link House#year}
     * @param year age of the house
     */
    public void setYear(Integer year){
        this.year = year;
    }

    /**
     * Function for getting the field value {@link House#numberOfFlatsOnFloor}
     * @return number of flats on floor in the house
     */
    public long getNumberOfFlatsOnFloor(){
        return numberOfFlatsOnFloor;
    }

    /**
     * Field value setting function {@link House#numberOfFlatsOnFloor}
     * @param numberOfFlatsOnFloor number of flats on floor in the house
     */
    public void setNumberOfFlatsOnFloor(long numberOfFlatsOnFloor){
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    @Override
    public String toString() {
        return "House{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", numberOfFlatsOnFloor=" + numberOfFlatsOnFloor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof House house)) return false;
        return Objects.equals(name, house.name) && Objects.equals(year, house.year)
                && Objects.equals(numberOfFlatsOnFloor, house.numberOfFlatsOnFloor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year, numberOfFlatsOnFloor);
    }
}
