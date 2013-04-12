/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.AnnonceLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
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
public class AnnonceManagedBean implements Serializable {
    
    private Annonce annonce;
    
    @Inject
    private AnnonceLocal annonceEJB;
    
    @PostConstruct
    public void initialisation() {
        annonce = annonceEJB.getAnnonceById(14);
        System.out.println("++++++++++++"+annonce);
    }

    /**
     * @return the annonce
     */
    public Annonce getAnnonce() {
        return annonce;
    }

    /**
     * @param annonce the annonce to set
     */
    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }


    
}
