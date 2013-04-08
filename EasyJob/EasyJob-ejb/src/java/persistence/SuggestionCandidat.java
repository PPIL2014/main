/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author celie
 */
@Entity
public class SuggestionCandidat implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private Candidat candidat;
    
    @OneToOne
    private Employeur emetteur;
    
    @ManyToOne
    private Employeur destinataire;
    
    
    public SuggestionCandidat() {
        
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
        if (!(object instanceof SuggestionCandidat)) {
            return false;
        }
        SuggestionCandidat other = (SuggestionCandidat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistance.SuggestionCandidat[ id=" + id + " ]";
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
     * @return the emetteur
     */
    public Employeur getEmetteur() {
        return emetteur;
    }

    /**
     * @param emetteur the emetteur to set
     */
    public void setEmetteur(Employeur emetteur) {
        this.emetteur = emetteur;
    }

    /**
     * @return the destinataire
     */
    public Employeur getDestinataire() {
        return destinataire;
    }

    /**
     * @param destinataire the destinataire to set
     */
    public void setDestinataire(Employeur destinataire) {
        this.destinataire = destinataire;
    }
    
}
