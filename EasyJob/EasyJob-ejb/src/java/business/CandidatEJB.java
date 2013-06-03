/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import interfaces.CandidatLocal;
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
    public Candidat getCandidatById(int id) {
        String query = "SELECT c FROM Candidat c WHERE c.id = '" + id + "'";
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

    @Override
    public boolean existeMail(String mail) {
        String query = "SELECT c FROM Candidat c WHERE upper(c.mail) = upper('" + mail + "')";
        Query q = em.createQuery(query);
        try {
            q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }

    @Override
    public void saveCandidat(Candidat candidat) {
        em.persist(candidat);
    }

    @Override
    public void removeCandidat(Candidat candidat) {
        /* Suppression des suggestions que le candidat a reçues */
        String query = "DELETE FROM SuggestionAnnonce s WHERE s.destinataire.id='"+candidat.getId()+"'";
        Query requestQuery = em.createQuery(query);
        requestQuery.executeUpdate();
        
        /* Suppression des suggestions que le candidat a envoyées */
        query = "SELECT s FROM SuggestionAnnonce s WHERE s.emetteur.id='"+candidat.getId()+"'";
        requestQuery = em.createQuery(query);
        List<SuggestionAnnonce> lg = requestQuery.getResultList();
        for(SuggestionAnnonce sa : lg) {
            removeSuggestion(sa, sa.getDestinataire());
        }
        
        /* Suppression des suggestionCandidat */
        query = "SELECT s FROM SuggestionCandidat s WHERE s.candidat.id='"+candidat.getId()+"'";
        requestQuery = em.createQuery(query);
        List<SuggestionCandidat> lgc = requestQuery.getResultList();
        for(SuggestionCandidat sc : lgc) {
            sc.getDestinataire().getSuggestions().remove(sc);
            em.merge(sc.getDestinataire());
            em.remove(em.merge(sc));
        }
        
        /* Suppression des notifications que le candidat a reçu*/
        query = "DELETE FROM NotificationCandidat n WHERE n.destinataire.id='"+candidat.getId()+"'";
        requestQuery = em.createQuery(query);
        requestQuery.executeUpdate();
        
        /* Suppression des notifications relatives aux candidatureAnnonce du candidat*/
        query = "SELECT n FROM NotificationEmployeur n WHERE n.candidature.candidat.id='"+candidat.getId()+"'";
        requestQuery = em.createQuery(query);
        List<NotificationEmployeur> lne = requestQuery.getResultList();
        for(NotificationEmployeur ne : lne) {
            ne.getDestinataire().getNotifications().remove(ne);
            em.merge(ne.getDestinataire());
            em.remove(em.merge(ne));
        }        
         
        /*Suppression des candidatureAnnonce du candidat */
        query = "SELECT c FROM CandidatureAnnonce c WHERE c.candidat.id='"+candidat.getId()+"'";
        requestQuery = em.createQuery(query);
        List<CandidatureAnnonce> lca = requestQuery.getResultList();
        for(CandidatureAnnonce ca : lca) {
            ca.getAnnonce().getCandidatures().remove(ca);
            em.merge(ca.getAnnonce());
            em.remove(em.merge(ca));
        }
        
        /* Suppression des notifications relatives aux candidatureSpontannées du candidat*/
        query = "SELECT n FROM NotificationCandidatureS n WHERE n.candidature.candidat.id='"+candidat.getId()+"'";
        requestQuery = em.createQuery(query);
        List<NotificationCandidatureS> lnc = requestQuery.getResultList();
        for(NotificationCandidatureS ncs : lnc) {
            ncs.getDestinataire().getNotificationsCS().remove(ncs);
            em.merge(ncs.getDestinataire());                
            em.remove(em.merge(ncs));
        }
         
        /* Suppression des candidatureSpontannee du candidat */
        query = "SELECT c FROM CandidatureSpontanee c WHERE c.candidat.id='"+candidat.getId()+"'";
        requestQuery = em.createQuery(query);
        List<CandidatureSpontanee> lcs = requestQuery.getResultList();
        for(CandidatureSpontanee cs : lcs) {
            for(Employeur emp : cs.getEmployeurs()) {
                emp.getCandidatures().remove(cs);
                em.merge(emp);                
            }
            em.remove(em.merge(cs));
        }
        
        /* Suppression du candidat des listes de suivis */
        query = "SELECT e FROM Employeur e ";
        requestQuery = em.createQuery(query);
        List<Employeur> le = requestQuery.getResultList();
        for(Employeur e : le) {
            e.getCandidats().remove(candidat);
            em.merge(e);
        }
             
        em.remove(em.merge(candidat));
    }

    @Override
    public void majCandidat(Candidat c) {
        em.merge(c);
    }
    
    @Override
    public List<CandidatureAnnonce> getAnnonceParticipe(Candidat c){
        String s="Select c from CandidatureAnnonce c where c.candidat.id='"+c.getId()+"'";
        Query rs=em.createQuery(s);
        return rs.getResultList();
    }
    
    public String conversionNiveauEtude(String niveau){
        String sb="()";
        if(niveau.equals("Aucun")){
            sb="('Aucun','CAP','BAC','BAC+1','BAC+2','BAC+3','BAC+4','BAC+5','BAC+6','BAC+7','BAC+8')";
        }else if(niveau.equals("CAP")){
            sb="('CAP','BAC','BAC+1','BAC+2','BAC+3','BAC+4','BAC+5','BAC+6','BAC+7','BAC+8')";
        }else if(niveau.equals("BAC")){
            sb="('BAC','BAC+1','BAC+2','BAC+3','BAC+4','BAC+5','BAC+6','BAC+7','BAC+8')";
        }else if(niveau.equals("BAC+1")){
            sb="('BAC+1','BAC+2','BAC+3','BAC+4','BAC+5','BAC+6','BAC+7','BAC+8')";
        }else if(niveau.equals("BAC+2")){
            sb="('BAC+2','BAC+3','BAC+4','BAC+5','BAC+6','BAC+7','BAC+8')";
        }else if(niveau.equals("BAC+3")){
            sb="('BAC+3','BAC+4','BAC+5','BAC+6','BAC+7','BAC+8')";
        }else if(niveau.equals("BAC+4")){
            sb="('BAC+4','BAC+5','BAC+6','BAC+7','BAC+8')";
        }else if(niveau.equals("BAC+5")){
            sb="('BAC+5','BAC+6','BAC+7','BAC+8')";
        }else if(niveau.equals("BAC+6")){
            sb="('BAC+6','BAC+7','BAC+8')";
        }else if(niveau.equals("BAC+7")){
            sb="('BAC+7','BAC+8')";
        }else if(niveau.equals("BAC+8")){
            sb="('BAC+8')";
        }
        
        
        
        
        
        return sb;
    }
    
    @Override
    public List<Candidat> rechercheCandidat(String domaineEtudes, Integer nbExperiences, String niveauEtude, String ville, String pays, String nom, String prenom ) {
        StringBuilder query=new StringBuilder("SELECT c FROM Candidat c WHERE ");
        if(domaineEtudes!=null && !domaineEtudes.matches("selection")){
            query.append(" c.domaineEtudes LIKE '"+domaineEtudes+"' ");
        }
        if(niveauEtude!=null && !niveauEtude.matches("Aucun") && !niveauEtude.matches("selection")){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" c.niveauEtudes IN "+conversionNiveauEtude(niveauEtude)+" ");
        }
        if(nbExperiences!=null && nbExperiences!=0  ){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" c.nbExperiences >= "+nbExperiences+" ");
        }
        
        if(nom!=null && !nom.matches("Tous")){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" UPPER(c.nom) LIKE UPPER('%"+nom+"%') ");
        }
        if(prenom!=null && !prenom.matches("Tous")){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" UPPER(c.prenom) LIKE UPPER('%"+prenom+"%') ");
        }  
        if(ville!=null && !ville.matches("selection")){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" c.adresse.id IN (SELECT b.id FROM Adresse b WHERE UPPER(b.ville) LIKE UPPER('%"+ville+"%') )");
        }
        if(pays!=null && !pays.matches("selection")){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" c.adresse.id IN (SELECT d.id FROM Adresse d WHERE UPPER(d.pays) LIKE UPPER('%"+pays+"%') )");
        }

        /* si aucune condition n'est precise 
         * on enleve la clause WHERE
         */
        if(query.length()<=31){
            query.delete(query.length()-7, query.length());
        }
        query.append(" ORDER BY c.mail ");
        Query q = em.createQuery(query.toString());
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
    public List<Candidat> rechercheCandidatByDomaine(String domaine){
        String query = "SELECT DISTINCT c FROM Candidat c WHERE UPPER(c.domaineEtudes) LIKE UPPER('"+domaine+"')";
        Query q = em.createQuery(query);
        return q.getResultList();
    }

    @Override
    public List<String> getAllNomCandidat() {
        String query = "SELECT DISTINCT c.nom FROM Candidat c ORDER BY c.nom";
        Query q = em.createQuery(query);
        return q.getResultList();
    }

    @Override
    public List<String> getAllPrenomCandidat() {
        String query = "SELECT DISTINCT c.prenom FROM Candidat c ORDER BY c.prenom";
        Query q = em.createQuery(query);
        return q.getResultList();
    }

    @Override
    public void addNotification(Candidat candidat, NotificationCandidat notification) {
        em.persist(notification);
        candidat.getNotifications().add(notification);
        em.merge(candidat);
    }
    
    @Override
    public void removeNtification(Candidat candidat, NotificationCandidat notification) {
        candidat.getNotifications().remove(notification);
        em.merge(candidat);
        em.remove(em.merge(notification));
    }

    @Override
    public void addSuggestion(SuggestionAnnonce suggestion, Candidat candidat) {
        em.persist(suggestion);
        candidat.getSuggestions().add(suggestion);
        em.merge(candidat);
    }

    @Override
    public void removeSuggestion(SuggestionAnnonce suggestion, Candidat candidat) {
        candidat.getSuggestions().remove(suggestion);
        em.merge(candidat);
        em.remove(em.merge(suggestion));
    }

    @Override
    public List<Candidat> getCandidatByNom(String nom, String prenom) {
        String query = "SELECT c FROM Candidat c WHERE upper(c.nom)=upper('"+nom+"') AND upper(c.prenom)=upper('"+prenom+"')";
        Query q = em.createQuery(query);
        return q.getResultList();
    }

    @Override
    public int getNbCandidatsInscrits() {
        String query = "SELECT c FROM Candidat c ";
        Query q = em.createQuery(query);
        try{
            return  q.getResultList().size();
        }catch(Exception e){
            return 0;
        }
        
       
    }

    @Override
    public List<CandidatureSpontanee> getCandidatureSpontanee(Candidat c) {
        String query = "SELECT c FROM CandidatureSpontanee c WHERE c.candidat.id='"+c.getId()+"'";
        Query q = em.createQuery(query);
        return q.getResultList();
    }

    @Override
    public boolean aSuggere(Candidat e, Annonce a, Candidat d) {
        String query = "SELECT s FROM SuggestionAnnonce s WHERE s.annonce.id='" + a.getId() + "' AND s.emetteur.id='" + e.getId() + "' AND s.destinataire.id='" + d.getId() + "'";
        Query q = em.createQuery(query);
        try {
            q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }
}

