/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ul.dateroulette.model;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author thomas
 */
@Entity
public class QuestionQCM extends Question implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer nombreChoixMax;
    private String type;

    /**
     * 
     * @element-type Choix
     */
    @OneToMany
    private ArrayList<Choix> choix;
    
    public Integer getNombreChoixMax() {
        return nombreChoixMax;
    }

    public void setNombreChoixMax(Integer nombreChoixMax) {
        this.nombreChoixMax = nombreChoixMax;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Choix> getChoix() {
        return choix;
    }

    public void setChoix(ArrayList<Choix> choix) {
        this.choix = choix;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionQCM)) {
            return false;
        }
        QuestionQCM other = (QuestionQCM) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.QuestionQCM[ id=" + id + " ]";
    }
    
}
