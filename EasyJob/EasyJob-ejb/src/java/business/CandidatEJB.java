/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import interfaces.CandidatLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public Candidat getCandidatByMail(String mail) {
        String query = "SELECT c FROM Candidat c WHERE c.mail = '" + mail + "'";
        Query q = em.createQuery(query);
        return (Candidat) q.getSingleResult();
    }

    @Override
    public boolean loginCandidat(String login, String mdp) {
        String query = "SELECT c FROM Candidat c WHERE c.mail='" + login + "' AND c.mdp='" + mdp + "'";
        Query q = em.createQuery(query);
        try {
            q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }
}
