/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeappjpa;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jon Calvo Gaminde
 */
@Entity
@Table(name="origin", schema="routedbjpa")
public class Origin extends Coordinate implements Serializable {
    private static final long serialVersionUID = 1L;
    private String country;
    private String state;
    private String county;
    private String city;
    private String district;
    private String street;
    private String houseNumber;
    private Integer postalCode;
    @OneToMany(mappedBy="origin")
    private Set<Route>routes;
}
