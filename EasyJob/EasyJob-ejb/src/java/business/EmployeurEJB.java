/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import interfaces.EmployeurLocal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import persistence.Annonce;
import persistence.Candidat;
import persistence.CandidatureAnnonce;
import persistence.CandidatureSpontanee;
import persistence.Employeur;
import persistence.Entreprise;
import persistence.NotificationCandidat;
import persistence.NotificationCandidatureS;
import persistence.NotificationEmployeur;
import persistence.SuggestionAnnonce;
import persistence.SuggestionCandidat;

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

    @Override
    public boolean existeMail(String mail) {
        String query = "SELECT e FROM Employeur e WHERE upper(e.mail) = upper('" + mail + "')";
        Query q = em.createQuery(query);
        try {
            q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }
    
    @Override
    public void saveEmployeur(Employeur emp) {
        emp.getEntreprise().getEmployeurs().add(emp);
        em.merge(emp.getEntreprise());
        em.persist(emp);
    }

    @Override
    public void majEmployeur(Employeur emp) {
        em.merge(emp);
    }

    @Override
    public void removeEmployeur(Employeur employeur) {
        /* Suppression des suggestions que l'employeur a reçues */
        String query;
        
        query = "DELETE FROM SuggestionCandidat s WHERE s.destinataire.mail='"+employeur.getMail()+"'";
        Query requestQuery = em.createQuery(query);
        requestQuery.executeUpdate(); //OK TEST
        
        /* Suppression des suggestions que l'employeur a envoyées */
        query = "SELECT s FROM SuggestionCandidat s WHERE s.emetteur.mail='"+employeur.getMail()+"'";
        requestQuery = em.createQuery(query);
        List<SuggestionCandidat> lg = requestQuery.getResultList();
        for(SuggestionCandidat sa : lg) {
            removeSuggestionCandidat(sa, sa.getDestinataire());
        }//OK TEST
        
        /* Suppression des notifications que l'employeur a reçues */
        query = "DELETE FROM NotificationEmployeur n WHERE n.destinataire.mail='"+employeur.getMail()+"'";
        requestQuery = em.createQuery(query);
        requestQuery.executeUpdate(); //OK TEST
        
        /* Suppression des CandidaturesAnnonces relatives à une annonce créée par l'employeur */
        query = "SELECT ca FROM CandidatureAnnonce ca WHERE ca.annonce.employeur.mail ='"+employeur.getMail()+"'";
        requestQuery = em.createQuery(query);
        List<CandidatureAnnonce> lca = requestQuery.getResultList();
        for(CandidatureAnnonce ca : lca) {
            ca.getAnnonce().getCandidatures().remove(ca);
            em.merge(ca.getAnnonce());
            em.remove(em.merge(ca));
        }//OK TEST   
        
        /* Suppression des NotificationCandidat relatives à une annonce créée par l'employeur */
        query = "SELECT nc FROM NotificationCandidat nc WHERE nc.annonce.employeur.mail ='"+employeur.getMail()+"'";
        requestQuery = em.createQuery(query);
        List<NotificationCandidat> lnc = requestQuery.getResultList();
        for(NotificationCandidat nc : lnc) {
            nc.getDestinataire().getNotifications().remove(nc);
            em.merge(nc.getDestinataire());
            em.remove(em.merge(nc));
        }//OK TEST
        
        /* Suppression des SuggestionAnnonce relatives à une annonce créée par l'employeur */
        query = "SELECT sa FROM SuggestionAnnonce sa WHERE sa.annonce.employeur.mail ='"+employeur.getMail()+"'";
        requestQuery = em.createQuery(query);
        List<SuggestionAnnonce> lsa = requestQuery.getResultList();
        for(SuggestionAnnonce sa : lsa) {
            sa.getDestinataire().getSuggestions().remove(sa);
            em.merge(sa.getDestinataire());
            em.remove(em.merge(sa));
        }//OK TEST 
        
        /* Suppression des annonces que l'employeur a crées */
        query = "SELECT a FROM Annonce a WHERE a.employeur.mail='"+employeur.getMail()+"'";
        requestQuery = em.createQuery(query);
        List<Annonce> la = requestQuery.getResultList();
        for(Annonce sa : la) {
            removeAnnonce(sa, sa.getEmployeur());
        }//OK TEST
        
        /* suppression de l'employeur des listes d'employeurs des CandidatureSpontanee */
        query = "SELECT cs FROM CandidatureSpontanee cs";
        requestQuery = em.createQuery(query);
        List<CandidatureSpontanee> lcs = requestQuery.getResultList();
        Employeur emplo = null;
        for(CandidatureSpontanee cs : lcs) {
            for(Employeur emp : cs.getEmployeurs()) {
                if(emp.getMail() != null && employeur.getMail() != null && emp.getMail().equals(employeur.getMail())) {
                     emplo = emp;
                } 
            }
            if(emplo != null) {
                cs.getEmployeurs().remove(emplo);
                //cs.getCandidat().getNotifications().add(new Notification());
                emplo = null;
            }
            em.merge(cs);
        }//TEST OK
        
        /* suppression des NotificationCandidatureS de l'employeur */
        query = "DELETE FROM NotificationCandidatureS j WHERE j.destinataire.mail='"+employeur.getMail()+"'";
        requestQuery = em.createQuery(query);
        requestQuery.executeUpdate(); //OK TEST
        
        /* suppression de l'employeur de la liste de son entreprise */
        removeEmployeur(employeur.getEntreprise(), employeur); //OK TEST
    }
    
    @Override
    public void change(Employeur employeur) {
         em.merge(employeur.getEntreprise());
         em.merge(employeur);       
    }
    
    @Override
    public void removeEmployeur(Entreprise ent, Employeur emp) {
            ent.getEmployeurs().remove(emp);  
            em.remove(em.merge(emp));
    }
    
    @Override
    public void removeAnnonce(Annonce annonce, Employeur employeur) {
        employeur.getAnnonces().remove(annonce);
        em.merge(employeur);
        em.remove(em.merge(annonce));
    }
 
    @Override
    public void removeSuggestionCandidat(SuggestionCandidat suggestion, Employeur employeur) {
        employeur.getSuggestions().remove(suggestion);
        em.merge(employeur);
        em.remove(em.merge(suggestion));
    }

    @Override
    public void addNotificationEmp(Employeur emp, NotificationEmployeur notification) {
        em.persist(notification);
        emp.getNotifications().add(notification);
        em.merge(emp);
    }

    @Override
    public void removeNotificationEmp(Employeur emp, NotificationEmployeur notification) {
        emp.getNotifications().remove(notification);
        em.merge(emp);
        em.remove(em.merge(notification));
    }

    @Override
    public void addSuggestionCandidat(SuggestionCandidat sc, Employeur emp) {
        em.persist(sc);    
        emp.getSuggestions().add(sc);
        em.merge(emp);
    }

    @Override
    public List<SuggestionCandidat> getListeSuggestionC(Employeur emp) {
        String query = "SELECT s FROM SuggestionCandidat s WHERE s.destinataire.id = " + emp.getId();
        Query q = em.createQuery(query);
        return (List<SuggestionCandidat>)q.getResultList();
    }
    
    @Override
    public List<Employeur> rechercheEmployeur(int id) {
        String query = "SELECT e FROM Employeur e WHERE e.entreprise_id = " +id;
        Query q = em.createQuery(query);
        return (List<Employeur>)q.getResultList();
    }

    @Override
    public void remove(Entreprise e) {
        
        /* Suppression des NotificationCandidat relatives à une entreprise */
        String query = "SELECT nc FROM NotificationCandidat nc WHERE nc.entreprise.id ='"+e.getId()+"'";
        Query requestQuery = em.createQuery(query);
        List<NotificationCandidat> lnc = requestQuery.getResultList();
        for(NotificationCandidat nc : lnc) {
            nc.getDestinataire().getEntreprises().remove(e);
            em.merge(nc.getDestinataire());
            em.remove(em.merge(nc));
        }
        
        /* Suppression de l'entreprise des listes à suivre */
        query = "SELECT c FROM Candidat c";
        requestQuery = em.createQuery(query);
        List<Candidat> lc = requestQuery.getResultList();
        for(Candidat c : lc) {
            if(c.getEntreprises().contains(e)) {
                c.getEntreprises().remove(e);
            }
            em.merge(c);
        }
        
        em.remove(em.merge(e));
    }
        
    @Override
    public int getNbEmployeursInscrits() {
        String query = "SELECT e FROM Employeur e ";
        Query q = em.createQuery(query);
        try{
            return  q.getResultList().size();
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public void addNotificationCandidatureSpontanee(NotificationCandidatureS notif, Employeur emp) {
        em.persist(notif);
        emp.getNotificationsCS().add(notif);
        em.merge(emp);
    }

    @Override
    public void removeNotificationCandidatureSpontanee(NotificationCandidatureS notif, Employeur emp) {
        emp.getNotificationsCS().remove(notif);
        em.merge(emp);
        em.remove(em.merge(notif));
    }

    @Override
    public List<Employeur> getEmployeurByNom(String nom, String prenom) {
        String query = "SELECT e FROM Employeur e WHERE upper(e.nom)=upper('"+nom+"') AND upper(e.prenom)=upper('"+prenom+"')";
        Query q = em.createQuery(query);
        return q.getResultList();        
    }
    
    @Override
    public List<Annonce> listeAnnonceCree(Employeur e){
        String s="Select c from Annonce c where c.employeur.id='"+e.getId()+"'";
        Query rs=em.createQuery(s);
        return rs.getResultList();
    }

    @Override
    public boolean aSuggere(Employeur e, Candidat c, Employeur d) {
        String query = "SELECT s FROM SuggestionCandidat s WHERE s.candidat.id='" + c.getId() + "' AND s.emetteur.id='" + e.getId() + "' AND s.destinataire.id='" + d.getId() + "'";
        Query q = em.createQuery(query);
        try {
            q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }
    

}
