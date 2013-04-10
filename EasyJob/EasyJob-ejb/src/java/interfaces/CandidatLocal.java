/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import javax.ejb.Local;
import persistence.Candidat;

/**
 *
 * @author celie
 */
@Local
public interface CandidatLocal {
    
    public Candidat getCandidatByMail(String mail); 
    
    public boolean loginCandidat(String login, String mdp);
    
}
