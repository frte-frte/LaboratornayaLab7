package data;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
public class Flat implements Comparable<Flat>, Serializable {
    private long id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private double area;
    private Long numberOfRooms;
    private Furnish furnish;
    private View view;
    private Transport transport;
    private House house;
    private String ownerLogin;

    public Flat(long id, String name, Coordinates coordinates, LocalDate creationDate, double area, Long numberOfRooms,
                Furnish furnish, View view, Transport transport, House house, String ownerLogin) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.house = house;
        this.ownerLogin = ownerLogin;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getArea() {
        return area;
    }

    public Long getNumberOfRooms() {
        return numberOfRooms;
    }

    public Furnish getFurnish() {
        return furnish;
    }

    public View getView() {
        return view;
    }

    public Transport getTransport() {
        return transport;
    }

    public House getHouse() {
        return house;
    }

    @Override
    public String toString() {
        return "Flat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", area=" + area +
                ", numberOfRooms=" + numberOfRooms +
                ", furnish=" + furnish +
                ", view=" + view +
                ", transport=" + transport +
                ", house=" + house +
                ", ownerLogin='" + ownerLogin + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flat flat)) return false;
        return id == flat.id && area == flat.area && numberOfRooms == flat.numberOfRooms && name.equals(flat.name)
                && coordinates.equals(flat.coordinates) && creationDate.equals(flat.creationDate)
                && furnish == flat.furnish && view == flat.view && transport == flat.transport
                && house.equals(flat.house) && ownerLogin.equals(flat.ownerLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, area, numberOfRooms, furnish, view, transport, house,
                ownerLogin);
    }

    @Override
    public int compareTo(Flat o) {
        return Long.compare(this.getId(), o.getId());
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }
}
