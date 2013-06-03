/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.CandidatLocal;
import interfaces.EntrepriseLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import persistence.Candidat;
import persistence.Employeur;
import persistence.Entreprise;

/**
 *
 * @author Marc
 */
@ManagedBean
@SessionScoped
public class EntrepriseManagedBean implements Serializable {
    
               
    private Entreprise entreprise;
    /**
     * vrai s'il y a des recruteurs à afficher, faux sinon
     */
    private boolean afficherRecruteur;
    
    @Inject
    private EntrepriseLocal entrepriseEJB;
    
    @Inject 
    private CandidatLocal candidatEJB;
    
    
    @PostConstruct
    public void initialisation() {
    } 
    
    public boolean aDeposerCand() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Candidat cand = ((RegisterManagedBean)fc.getExternalContext().getSessionMap().get("connexionBean")).getCandidat() ;
        return entrepriseEJB.aDeposerCand(entreprise, cand) ;
    }
    
    public boolean suivi() {
        Candidat cand = ((RegisterManagedBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connexionBean")).getCandidat() ;
        cand = candidatEJB.getCandidatByMail(cand.getMail());
        boolean b = cand.getEntreprises().contains(entreprise);
        return b;
    }
    
    public void consulterRecherche( ActionEvent e){
        String myAttribute = e.getComponent().getAttributes().get( "Mentreprise" ).toString();
        entreprise = entrepriseEJB.getEntrepriseById(Integer.parseInt(myAttribute));
        
        List<Employeur> emp = entreprise.getEmployeurs();
        afficherRecruteur = false;
        int i = 0;
        while(!afficherRecruteur && i<emp.size()) {
            afficherRecruteur = emp.get(i).isConfidentialite();
            i++;
        }

    }

    /**
     * @return the entreprise
     */
    public Entreprise getEntreprise() {
        return entreprise;
    }

    /**
     * @param entreprise the entreprise to set
     */
    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    /**
     * @return the afficherRecruteur
     */
    public boolean isAfficherRecruteur() {
        return afficherRecruteur;
    }

    /**
     * @param afficherRecruteur the afficherRecruteur to set
     */
    public void setAfficherRecruteur(boolean afficherRecruteur) {
        this.afficherRecruteur = afficherRecruteur;
    }

}
