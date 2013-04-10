/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import interfaces.EmployeurLocal;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import persistence.Candidat;
import persistence.Employeur;

/**
 *
 * @author celie
 */
@Stateless
public class EmployeurEJB implements EmployeurLocal {
    
    @PersistenceContext(unitName = "EasyJob-PU")
    private EntityManager em;
    
    
    @Override
    public Employeur getEmployeurByMail(String mail) {
        
        String query = "SELECT e FROM Employeur e WHERE e.mail = '" + mail + "'";
        Query q = em.createQuery(query);
        return (Employeur) q.getSingleResult();
        
    }

    @Override
    public boolean loginEmployeur(String login, String mdp) {
        
        String query = "SELECT e FROM Employeur e WHERE e.mail='" + login + "' AND e.mdp='" + mdp + "'";
        Query q = em.createQuery(query);
        try {
            q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
