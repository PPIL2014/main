/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.EntrepriseLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import persistence.Entreprise;

/**
 *
 * @author Marc
 */
@Named
@RequestScoped
public class EntrepriseManagedBean implements Serializable {
    
               
    private Entreprise entreprise;
    
    @Inject
    EntrepriseLocal entrepriseEJB;
    
    public EntrepriseManagedBean() {               
    }
    
    @PostConstruct
    public void initialisation() {
        entreprise = entrepriseEJB.getEntrepriseByNom("Societe Generale"); 
    } 
    
    
    /*public String essai() {
        Entreprise entreprise2 = entrepriseEJB.getEntrepriseByNom("Societe Generale"); 
        System.out.println("+++++++"+entreprise2);
        return "vitrineEntreprise";
    }*/

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

    
}
