/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.EntrepriseLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import persistence.Employeur;
import persistence.Entreprise;

/**
 *
 * @author Marc
 */
@Named
@RequestScoped
public class EntrepriseManagedBean implements Serializable {
    
               
    private Entreprise entreprise;
    /**
     * vrai s'il y a des recruteurs à afficher, faux sinon
     */
    private boolean afficherRecruteur;
    
    @Inject
    EntrepriseLocal entrepriseEJB;
    
    public EntrepriseManagedBean() {               
    }
    
    @PostConstruct
    public void initialisation() {
        entreprise = entrepriseEJB.getEntrepriseByNom("La bonne place");
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
