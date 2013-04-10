/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import java.io.Serializable;
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
        try {
        FacesContext fc = FacesContext.getCurrentInstance();
        //RegisterManageBean rmb = (RegisterManageBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        //System.out.println(rmb+"+++++++++++++++++++++");
        } catch (Exception ex) {
            System.out.println("erreru");
        }
        /*candidat = rmb.getCandidat();
        System.out.println("++++");
        System.out.println(candidat);*/
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