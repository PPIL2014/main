/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ul.dateroulette.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author thomas
 */
@Entity
public class Signalement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    private String description;
    private Boolean estTraitee;
    private Boolean estEnCoursDeTraitement;
    private Boolean estEnAttente;
    @OneToOne
    private Utilisateur emetteur;
    
    public Boolean enCoursDeTraitement() {
        return null;
    }

    public Boolean traitee() {
        return null;
    }

    public Boolean enAttente() {
        return null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEstTraitee() {
        return estTraitee;
    }

    public void setEstTraitee(Boolean estTraitee) {
        this.estTraitee = estTraitee;
    }

    public Boolean getEstEnCoursDeTraitement() {
        return estEnCoursDeTraitement;
    }

    public void setEstEnCoursDeTraitement(Boolean estEnCoursDeTraitement) {
        this.estEnCoursDeTraitement = estEnCoursDeTraitement;
    }

    public Boolean getEstEnAttente() {
        return estEnAttente;
    }

    public void setEstEnAttente(Boolean estEnAttente) {
        this.estEnAttente = estEnAttente;
    }

    public Utilisateur getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(Utilisateur emetteur) {
        this.emetteur = emetteur;
    }

    public Long getId() {
        return id;
    }

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
        if (!(object instanceof Signalement)) {
            return false;
        }
        Signalement other = (Signalement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.Signalement[ id=" + id + " ]";
    }
    
}
