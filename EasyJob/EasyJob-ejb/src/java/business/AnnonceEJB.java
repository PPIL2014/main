/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import persistence.Annonce;

/**
 *
 * @author Marie
 */

@Stateless
@LocalBean
public class AnnonceEJB {
    
    @PersistenceContext(unitName = "EasyJob-PU")
    private EntityManager em;

    public Annonce getAnnonce(long id){
        Annonce a = em.find(Annonce.class, id);
        return a ;
    }
}
