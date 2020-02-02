package routeappjpa;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The entity for the direction.
 *
 * @author Jon Calvo Gaminde
 */
@Entity
@Table(name="direction", schema="routesdb")
@NamedQueries({
    @NamedQuery(name="findDirectionsByType",
            query="SELECT d FROM Direction d WHERE d.coordinate.type = :type"
    ),
    @NamedQuery(name="findDirectionByCoordinate",
            query="SELECT d FROM Direction d WHERE d.coordinate = :coordinate"
    ),
    @NamedQuery(name="findDirectionsByRoute",
            query="SELECT d FROM Direction d WHERE d.coordinate IN (SELECT cr.coordinate FROM Coordinate_Route cr WHERE cr.id.routeId = :routeId)"
    )
})
@XmlRootElement
public class Direction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    private Coordinate coordinate;
    @NotNull
    private String name;
    private String country;
    private String state;
    private String county;
    private String city;
    private String district;
    private String street;
    private String houseNumber;
    private Integer postalCode;

    //Getters
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCounty() {
        return county;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    //Setters
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.coordinate);
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.country);
        hash = 89 * hash + Objects.hashCode(this.state);
        hash = 89 * hash + Objects.hashCode(this.county);
        hash = 89 * hash + Objects.hashCode(this.city);
        hash = 89 * hash + Objects.hashCode(this.district);
        hash = 89 * hash + Objects.hashCode(this.street);
        hash = 89 * hash + Objects.hashCode(this.houseNumber);
        hash = 89 * hash + Objects.hashCode(this.postalCode);
        return hash;
    }

    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Direction other = (Direction) obj;
        if (!Objects.equals(this.coordinate, other.coordinate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Direction " + name + "{country=" + country + ", state=" + state + ", county=" + county + ", city=" + city + ", district=" + district + ", street=" + street + ", houseNumber=" + houseNumber + ", postalCode=" + postalCode + '}';
    }
    
    
}
