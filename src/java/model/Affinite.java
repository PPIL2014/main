/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;

@Entity
public class Affinite implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private AffiniteId affiniteId;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCalc;

    private float affinite;

    public Affinite(){
        dateCalc = new Date(0);
    }
    
    public void calcAffinite()
    {
        affinite = (float)Math.random();
        dateCalc = new Date();
    }
            
        
    public Date getDateCalc() {
        return dateCalc;
    }

    public float getAffinite() {
        return affinite;
    }
    
    public AffiniteId getAffiniteId() {
        return affiniteId;
    }

    public void setAffiniteId(AffiniteId affiniteId) {
        this.affiniteId = affiniteId;
    }    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Affinite other = (Affinite) obj;
        if (!Objects.equals(this.affiniteId, other.affiniteId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.affiniteId);
        return hash;
    }
 
}
