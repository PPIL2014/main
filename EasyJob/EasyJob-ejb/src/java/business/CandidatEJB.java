/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import interfaces.CandidatLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import persistence.Candidat;

/**
 *
 * @author celie
 */
@Stateless
public class CandidatEJB implements CandidatLocal{
    
    @PersistenceContext(unitName = "EasyJob-PU")
    private EntityManager em;

    @Override
    public Candidat getCandidat(String mail) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public boolean estCandidat(String login){
        return false;
    }
}
