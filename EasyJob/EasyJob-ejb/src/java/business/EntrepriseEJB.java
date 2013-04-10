/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import interfaces.EntrepriseLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import persistence.Candidat;
import persistence.Entreprise;

/**
 *
 * @author Marc
 */
@Stateless
public class EntrepriseEJB implements EntrepriseLocal {
    
    @PersistenceContext(unitName = "EasyJob-PU")
    private EntityManager em;

    public EntrepriseEJB() {
    }


    @Override
    public Entreprise getEntrepriseByNom(String nom){
        String query = "SELECT e FROM Entreprise e WHERE e.nomEntreprise = '" + nom + "'";
        Query q = em.createQuery(query);
        return (Entreprise) q.getSingleResult();
    }
}
