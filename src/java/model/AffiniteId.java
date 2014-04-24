/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Embeddable
public class AffiniteId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pseudoU1;
    private String pseudoU2;
    public AffiniteId() {
    }
    public AffiniteId(String pseudoU1, String pseudoU2){
        this.pseudoU1 = pseudoU1;
        this.pseudoU2 = pseudoU2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.pseudoU1);
        hash = 97 * hash + Objects.hashCode(this.pseudoU2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AffiniteId other = (AffiniteId) obj;
        return true;
    }
    
    @Override
    public String toString() {
        return "model.AffiniteId[ id=" + pseudoU1+ ","+pseudoU2+" ]";
    }
    
}
