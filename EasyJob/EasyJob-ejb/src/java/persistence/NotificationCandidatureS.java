/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author celie
 */
@Entity
public class NotificationCandidatureS implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private CandidatureSpontanee candidature;
    
    @ManyToMany
    private List<Employeur> destinataires;
    
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
        if (!(object instanceof NotificationCandidatureS)) {
            return false;
        }
        NotificationCandidatureS other = (NotificationCandidatureS) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.NotificationCandidatureSpontanee[ id=" + id + " ]";
    }

    /**
     * @return the candidature
     */
    public CandidatureSpontanee getCandidature() {
        return candidature;
    }

    /**
     * @param candidature the candidature to set
     */
    public void setCandidature(CandidatureSpontanee candidature) {
        this.candidature = candidature;
    }

    /**
     * @return the destinataires
     */
    public List<Employeur> getDestinataires() {
        return destinataires;
    }

    /**
     * @param destinataires the destinataires to set
     */
    public void setDestinataires(List<Employeur> destinataires) {
        this.destinataires = destinataires;
    }


    
}
