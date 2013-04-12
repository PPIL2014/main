/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.CandidatLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import persistence.Candidat;
import persistence.Employeur;

/**
 *
 * @author margaux
 */
@Named
@RequestScoped
public class CandidatManagedBean implements Serializable {
    
    private Candidat candidat;
    
    @Inject
    CandidatLocal candidatEJB;
    
    /**
     * vrai si les données personnelles sont affichées, faux sinon
     */
    private boolean afficherDonnees;
    
    @PostConstruct
    public void initialisation() {
        candidat = candidatEJB.getCandidatByMail("toto@toto.fr"); 
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        Candidat cand = rmb.getCandidat();
        if(cand!=null && !candidat.isConfidentialite()) {
            afficherDonnees = false;
        } else {
            afficherDonnees = true;
        }
    }

    /**
     * Creates a new instance of CandidatManagedBean
     */
    public CandidatManagedBean() {
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    /**
     * @return the afficherDonnees
     */
    public boolean isAfficherDonnees() {
        return afficherDonnees;
    }

    /**
     * @param afficherDonnees the afficherDonnees to set
     */
    public void setAfficherDonnees(boolean afficherDonnees) {
        this.afficherDonnees = afficherDonnees;
    }
    
}