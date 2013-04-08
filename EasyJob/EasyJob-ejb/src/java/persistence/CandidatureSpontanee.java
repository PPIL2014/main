/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author celie
 */
@Entity
public class CandidatureSpontanee implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private Candidat candidat;
    
    @ManyToMany
    private List<Employeur> employeurs;
    
    
    public CandidatureSpontanee() {
        this.employeurs = new ArrayList<Employeur>();
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
        if (!(object instanceof CandidatureSpontanee)) {
            return false;
        }
        CandidatureSpontanee other = (CandidatureSpontanee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistance.CandidatureSpontanee[ id=" + id + " ]";
    }

    /**
     * @return the candidat
     */
    public Candidat getCandidat() {
        return candidat;
    }

    /**
     * @param candidat the candidat to set
     */
    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    /**
     * @return the employeurs
     */
    public List<Employeur> getEmployeurs() {
        return employeurs;
    }

    /**
     * @param employeurs the employeurs to set
     */
    public void setEmployeurs(List<Employeur> employeurs) {
        this.employeurs = employeurs;
    }
    
}
