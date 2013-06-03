/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.AnnonceLocal;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author celie
 */
@Singleton
@Startup
public class SuppressionAnnonce {
    
    @Inject
    private AnnonceLocal annonceEJB;

    /**
     * Creates a new instance of SuppressionAnnonce
     */
    public SuppressionAnnonce() {
    }
    
    @PostConstruct
    public void intitialisation() {
        annonceEJB.supprimerAnnonce();
    }
}
