/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import java.io.Serializable;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import persistence.Candidat;

/**
 *
 * @author enis
 */

@Named(value="profilcandBean")
@RequestScoped
public class ProfilCandManagedBean implements Serializable{
    
    private Candidat candidat;
        
    public ProfilCandManagedBean(){        
        /*try {
        FacesContext fc = FacesContext.getCurrentInstance();
        //RegisterManageBean rmb = (RegisterManageBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        //System.out.println(rmb+"+++++++++++++++++++++");
        } catch (Exception ex) {
            System.out.println("erreru");
        }*/
        /*candidat = rmb.getCandidat();
        System.out.println("++++");
        System.out.println(candidat);*/
    }
    
    @PostConstruct
    public void initialisation() {
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManageBean rmb = (RegisterManageBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        System.out.println(rmb+"---------------------------------");
        System.out.println(rmb.getCandidat());
        GregorianCalendar date = new GregorianCalendar(1978, 0, 7);
        candidat = new Candidat("test","test","0395864899","1526975694102",date.getTime(),true);
        System.out.println("+++++++++++++++++++++");
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
   
    
}