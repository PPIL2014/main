/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.CandidatLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import persistence.Candidat;

/**
 *
 * @author margaux
 */
@ManagedBean
@SessionScoped
public class CandidatManagedBean implements Serializable {
    
    private Candidat candidat;
    
    @Inject
    private CandidatLocal candidatEJB;
    
    /**
     * vrai si les données personnelles sont affichées, faux sinon
     */
    private boolean afficherDonnees;
    
    
    @PostConstruct
    public void initialisation() {
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        candidat = rmb.getCandidat();
        if(candidat!=null && !candidat.isConfidentialite()) {
            setAfficherDonnees(false);
        } else {
            setAfficherDonnees(true);
        }
    }
        
    public void consulterRecherche( ActionEvent e){
        try{
            String myAttribute = e.getComponent().getAttributes().get( "Mcandidat" ).toString();
            setCandidat(candidatEJB.getCandidatById(Integer.parseInt(myAttribute)));
            if(getCandidat()!=null && !candidat.isConfidentialite()) {
                setAfficherDonnees(false);
            } else {
                setAfficherDonnees(true);
            }
            
         } catch(Exception ex){
            ex.printStackTrace();
         }

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