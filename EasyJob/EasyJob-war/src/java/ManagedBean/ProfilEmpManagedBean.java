/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.EmployeurLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import persistence.Employeur;

/**
 *
 * @author enis
 */

@ManagedBean
public class ProfilEmpManagedBean implements Serializable{
    
    private Employeur employeur;
    
    @Inject
    EmployeurLocal employeurEJB;
    
    public ProfilEmpManagedBean(){
        
    }
    
    @PostConstruct
    public void initialisation() {
        /*FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManageBean rmb = (RegisterManageBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        System.out.println(rmb+"---------------------------------");
        System.out.println(rmb.getCandidat());
        GregorianCalendar date = new GregorianCalendar(1978, 0, 7);
        Entreprise entreprise = new Entreprise("Societe Generale","banque","0155687582","SARL", new Adresse("35 allée des Lilas","75000","Paris","France"));
        employeur = new Employeur("test","test","0395864899","1526975694102",true,entreprise);
        System.out.println("+++++++++++++++++++++");*/
        employeur = employeurEJB.getEmployeurByMail("laure@test.fr");
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }
}