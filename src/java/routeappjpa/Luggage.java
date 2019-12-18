/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routeappjpa;

import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 2dam
 */
@XmlRootElement(name = "responseList")
public class Luggage implements Serializable {
    private ArrayList<Object> luggage;

    /**
     * @return the luggage
     */
    public ArrayList<Object> getLuggage() {
        return luggage;
    }

    /**
     * @param luggage the luggage to set
     */
    public void setLuggage(ArrayList<Object> luggage) {
        this.luggage = luggage;
    }
}
