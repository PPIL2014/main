/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.EntrepriseLocal;
import java.io.Serializable;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import persistence.Candidat;
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
    private Entreprise entreprise;
    
    @Inject
    EntrepriseLocal entrepriseEJB;
    
    public ProfilEmpManagedBean(){
        
    }
    
    @PostConstruct
    public void initialisation() {
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManageBean rmb = (RegisterManageBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        System.out.println(rmb+"---------------------------------");
        System.out.println(rmb.getCandidat());
        GregorianCalendar date = new GregorianCalendar(1978, 0, 7);
        employeur = new Employeur("test","test","0395864899","1526975694102",true,entreprise);
        System.out.println("+++++++++++++++++++++");
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }
}