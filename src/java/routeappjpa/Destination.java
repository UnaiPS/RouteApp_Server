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
 * @author 2dam
 */
@Entity
@Table(name="destination", schema="routedbjpa")
public class Destination extends Origin implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean visited;
    @OneToMany(mappedBy="destination")
    private Set<Route>routes;
}
