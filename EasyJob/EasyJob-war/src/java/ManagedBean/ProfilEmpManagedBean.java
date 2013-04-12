/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.EmployeurLocal;
import interfaces.EntrepriseLocal;
import java.io.Serializable;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import persistence.Adresse;
import persistence.Employeur;
import persistence.Entreprise;

/**
 *
 * @author enis
 */

@Named
@SessionScoped
public class ProfilEmpManagedBean implements Serializable{
    
    private Employeur employeur;
    
    @Inject
    EmployeurLocal employeurLocal;
    
    public ProfilEmpManagedBean(){
        
    }
    
    @PostConstruct
    public void initialisation() {
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        if(rmb!=null) {
            employeur = rmb.getEmployeur();
        } else {
            employeur = null;
        }

    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }
}