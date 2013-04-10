/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import javax.ejb.Local;
import persistence.Employeur;

/**
 *
 * @author celie
 */
@Local
public interface EmployeurLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public Employeur getEmployeurByMail(String mail); 
    
    public boolean loginEmployeur(String login, String mdp);
    
}
