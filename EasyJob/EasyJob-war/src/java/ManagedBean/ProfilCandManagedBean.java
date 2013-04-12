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
    }
    
    @PostConstruct
    public void initialisation() {
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        if(rmb!=null) {
            candidat = rmb.getCandidat();
        } else {
            candidat = null;
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
   
    
}