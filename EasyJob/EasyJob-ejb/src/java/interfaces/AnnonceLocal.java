/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import javax.ejb.Local;
import persistence.Annonce;



/**
 *
 * @author celie
 */
@Local
public interface AnnonceLocal {
    
    public Annonce getAnnonceById(long id);
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
