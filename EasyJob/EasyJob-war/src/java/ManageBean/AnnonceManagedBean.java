/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import business.AnnonceEJB;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import persistence.Annonce;

/**
 *
 * @author Marie
 */

@Named
@RequestScoped
public class annonceManagedBean implements Serializable {
    
    @Inject
    AnnonceEJB annonceEJB;
    public Annonce getAnnonce(){
      //  FacesContext fc = FacesContext.getCurrentInstance() ;
      //  RegisterManageBean r = (RegisterManageBean)fc.getExternalContext().getSessionMap().get("connexion");
        //return AnnonceEJB.getAnnonce();
      // return new Annonce() ;
        
        return annonceEJB.getAnnonce(13) ;
    }
    
}
