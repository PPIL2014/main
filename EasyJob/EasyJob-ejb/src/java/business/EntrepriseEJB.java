/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import interfaces.EntrepriseLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import persistence.Adresse;
import persistence.Candidat;
import persistence.CandidatureSpontanee;
import persistence.Employeur;
import persistence.Entreprise;
import persistence.NotificationCandidat;

/**
 *
 * @author Marc
 */

@Stateless
public class EntrepriseEJB implements EntrepriseLocal {
    
    @PersistenceContext(unitName = "EasyJob-PU")
    private EntityManager em;
    

    @Override
    public Entreprise getEntrepriseByNom(String nom){
        String query = "SELECT e FROM Entreprise e WHERE upper(e.nomEntreprise) LIKE upper('%" + nom + "%')";
        Query q = em.createQuery(query);
        return (Entreprise) q.getSingleResult();
    }

    @Override
    public void saveEntreprise(Entreprise ent) {
        em.persist(ent);
    }

    @Override
    public void removeEntreprise(Entreprise ent) {
        /* Suppression des notifications liées à l'entreprise */
        String query = "SELECT n FROM NotificationCandidat n WHERE n.entreprise.id='"+ent.getId()+"'";
        Query requestQuery = em.createQuery(query);
        List<NotificationCandidat> lg = requestQuery.getResultList();
        for(NotificationCandidat sa : lg) {
            removeNotification(sa, sa.getEntreprise());
        }
        /* suppression de l'entreprise */
        em.remove(em.merge(ent));
    }
    
    @Override
    public void change(Entreprise ent) {
        em.merge(ent);
    }
    
    @Override
    public void change(Adresse addr) {
        em.merge(addr);
    }
    
    private void removeNotification(NotificationCandidat sa, Entreprise ent) {
            sa.getDestinataire().getNotifications().remove(sa);
            em.merge(sa.getDestinataire());
            em.remove(em.merge(sa));
    }

    @Override
    public boolean existByName(String ent) {
        String query = "SELECT e FROM Entreprise e WHERE upper(e.nomEntreprise) = upper('" + ent + "')";
        Query q = em.createQuery(query);
        try {
            q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }
    
    @Override
    public int getNbEmployeurs(Entreprise e) {
        String query = "SELECT e FROM Employeur e WHERE e.entreprise.id = " + e.getId();
        Query q = em.createQuery(query) ;
        List<Employeur> ids = q.getResultList() ;
        return ids.size();
    }

    @Override
    public boolean aDeposerCand(Entreprise ent, Candidat cand) {
        if (cand == null) {
            return true ;
        }
        List<Employeur> employeurs = ent.getEmployeurs() ;
        String query = "select c.employeurs from CandidatureSpontanee c where c.candidat.id = "+cand.getId() ;
        Query q = em.createQuery(query) ;
        List<Employeur> ids = q.getResultList() ;
        if (ids.isEmpty()) {
            return false ;
        }
        List<Employeur> emp = new ArrayList<Employeur>() ;
        for (int i = 0 ; i < ids.size() ; i++) {
            if (employeurs.contains(ids.get(i))) {
                emp.add(ids.get(i)) ;
            }
        }
        return employeurs.equals(emp) ;
    }

    @Override
    public void creerCandidatureSpontanee(CandidatureSpontanee spont, Entreprise ent) {
        em.persist(spont);
        List<Employeur> emps = ent.getEmployeurs() ;
        Employeur e ;
        for (int i = 0 ; i < emps.size() ; i++) {
            e = emps.get(i);
            e.getCandidatures().add(spont) ;
            em.merge(e);
        }
    }
    
    @Override
    public List<Entreprise> rechercheEntreprise(String nomEntreprise, String domaine, String ville, String pays, String statut) {
        StringBuilder query=new StringBuilder("SELECT e FROM Entreprise e WHERE ");
        if(domaine!=null && !domaine.matches("Aucun") &&(!domaine.equalsIgnoreCase("tous")) ){
            query.append(" e.domaine LIKE '"+domaine+"' ");
        }
        if(nomEntreprise!=null && (!nomEntreprise.equalsIgnoreCase("tous")) ){
            if(query.length()>33){
                query.append(" AND ");
            }
            query.append(" UPPER(e.nomEntreprise) LIKE UPPER('%"+nomEntreprise+"%') ");
        }
        if(statut!=null &&(!statut.equalsIgnoreCase("tous")) ){
            if(query.length()>33){
                query.append(" AND ");
            }
            query.append(" e.statut LIKE '"+statut+"' ");
        }
        if(ville!=null && (!ville.equalsIgnoreCase("tous"))){
            if(query.length()>33){
                query.append(" AND ");
            }
            query.append(" e.adresse.id IN (SELECT b.id FROM Adresse b WHERE UPPER(b.ville) LIKE UPPER('%"+ville+"%')) ");
        }
        if(pays!=null && (!pays.equalsIgnoreCase("tous")) ){
            if(query.length()>33){
                query.append(" AND ");
            }
            query.append(" e.adresse.id IN (SELECT d.id FROM Adresse d WHERE UPPER(d.pays) LIKE UPPER('%"+pays+"%')) ");
        }

        /* si aucune condition n'est precise 
         * on enleve la clause WHERE
         */
        if(query.length()<=33){
            query.delete(query.length()-7, query.length());
        }
        query.append(" ORDER BY e.nomEntreprise ");
        Query q=em.createQuery(query.toString());
        return q.getResultList();
    }

    @Override
    public List<String> getAllVille() {
        String query = "SELECT DISTINCT a.ville FROM Adresse a ORDER BY a.ville";
        Query q = em.createQuery(query);
        return q.getResultList();
    }

    @Override
    public List<String> getAllPays() {
        String query = "SELECT DISTINCT a.pays FROM Adresse a ORDER BY a.pays";
        Query q = em.createQuery(query);
        return q.getResultList();
    }

    @Override
    public List<String> getAllNomEntreprise() {
        String query = "SELECT DISTINCT e.nomEntreprise FROM Entreprise e ORDER BY e.nomEntreprise";
        Query q = em.createQuery(query);
        return q.getResultList();
    }

    @Override
    public Entreprise getEntrepriseById(int id) {
        String query = "SELECT e FROM Entreprise e WHERE e.id = '" + id + "'";
        Query q = em.createQuery(query);
        return (Entreprise) q.getSingleResult();
    }
}
