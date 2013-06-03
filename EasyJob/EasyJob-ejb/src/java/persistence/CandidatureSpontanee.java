/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

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
    
    private File cv ;
    private File lettre ;
    
    @OneToOne
    private Candidat candidat;
    
    @ManyToMany
    private List<Employeur> employeurs;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCreation;
    
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

    public File getCv() {
        return cv;
    }

    public void setCv(File cv) {
        this.cv = cv;
    }

    public File getLettre() {
        return lettre;
    }

    public void setLettre(File lettre) {
        this.lettre = lettre;
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

    /**
     * @return the dateCreation
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    /**
     * @param dateCreation the dateCreation to set
     */
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    
}
