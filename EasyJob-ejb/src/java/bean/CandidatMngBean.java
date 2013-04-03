/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import interfaces.CandidatMng;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import persistence.Candidat;

/**
 *
 * @author celie
 */
@Stateless
public class CandidatMngBean implements CandidatMng{
    
    @PersistenceContext(unitName = "EasyJob-PU")
    private EntityManager em;

    @Override
    public Candidat getCandidat(String mail) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
