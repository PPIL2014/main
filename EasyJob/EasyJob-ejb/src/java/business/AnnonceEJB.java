/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import interfaces.AnnonceLocal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import persistence.Adresse;
import persistence.Annonce;
import persistence.Candidat;
import persistence.CandidatureAnnonce;
import persistence.CandidatureSpontanee;
import persistence.Employeur;
import persistence.NotificationCandidat;
import persistence.NotificationCandidatureS;
import persistence.SuggestionAnnonce;

/**
 *
 * @author Marie
 */

@Stateless
public class AnnonceEJB implements AnnonceLocal{
    
    @PersistenceContext(unitName = "EasyJob-PU")
    private EntityManager em;

    
    @Override
    public Annonce getAnnonceById(long id){
        Annonce a = em.find(Annonce.class, id);
        return a ;
    }
    
    @Override
    public void creerCandidatureAnnonce(CandidatureAnnonce candidAnn, Annonce annonce) {
        em.persist(candidAnn);
        annonce.getCandidatures().add(candidAnn);
        em.merge(annonce);
    }
    
     @Override
    public void creerAnnonce(Annonce annonce, Employeur emp){
        em.persist(annonce);
        emp.getAnnonces().add(annonce);
        em.merge(emp);
    }

     /**
     * retourne une liste de nievau d'etude
     * @param niveau
     * @return 
     */
    public List<String> listeNiveauEtude(String niveau){
        ArrayList<String> liste=null;
        if(niveau!=null){
            /* on cree la liste */
            liste=new ArrayList<String>();
            liste.add("Aucun");
            liste.add("CAP");
            liste.add("BAC");
            for(int i=0;i<8;i++){
                liste.add("BAC+"+(i+1));
            }
            
            /* on retire les niveaux inferieurs à celui demandé*/
            while(!niveau.equalsIgnoreCase(liste.get(0))){
                liste.remove(0);
            }
        }
        return liste;
    }
    
    private String transformerListe(List<String> ls){
        StringBuilder sb=new StringBuilder("(");
        int j=0;
        while(j<ls.size()){
            sb.append("'"+ls.get(j)+"'");
            if(j!=ls.size()-1){
                sb.append(", ");
            }
            j++;
        }
        sb.append(")");
        return sb.toString();
    }
     
    @Override
    public List<Annonce> rechercheAnnonce(String contrat, String niveauEtude, String secteur, Date date, int experience, int salaire, String ville) {
        StringBuilder query=new StringBuilder("SELECT a FROM Annonce a WHERE ");
        if(contrat!=null&&(!contrat.equalsIgnoreCase("tous"))){
            query.append(" a.contrat LIKE '"+contrat+"' ");
        }
        
        if(niveauEtude!=null && niveauEtude.length()!=0 &&(!niveauEtude.equalsIgnoreCase("tous")) &&(!niveauEtude.equalsIgnoreCase("Aucun")) ){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" a.etudes IN "+transformerListe(listeNiveauEtude(niveauEtude))+" ");
        }
        
        if(secteur!=null && secteur.length()!=0 &&(!secteur.equalsIgnoreCase("tous")) ){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" a.secteur LIKE '"+secteur+"' ");
        }
        
        if(date!=null){
            if(query.length()>31){
                query.append(" AND ");
            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
           
            query.append(" a.dateEmission >= '"+sdf.format(date)+"' ");
        }
        if(experience!=0){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" a.experience >= "+experience+" ");
        }
        if(salaire!=0){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" a.salaire >= "+salaire+" ");
        }
        if(ville!=null && ville.length()!=0 && (!ville.equalsIgnoreCase("tous") )){
            if(query.length()>31){
                query.append(" AND ");
            }
            query.append(" a.lieu.id IN (SELECT b.id FROM Adresse b WHERE b.ville LIKE '%"+ville+"%') ");
        }

        /* si aucune condition n'est precise 
         * on enleve la clause WHERE
         */
        if(query.length()<=31){
            query.delete(query.length()-7, query.length());
        }
        Query q=em.createQuery(query.toString());
        return q.getResultList();
    }

    @Override
    public boolean aPostuler(Annonce annonce, Candidat candidat) {
        if (candidat == null) {
            return true ;
        }
        String query = "select c.candidat.id from CandidatureAnnonce c where c.annonce.id = "+annonce.getId()+" and c.candidat.id = "+candidat.getId() ;
        Query q = em.createQuery(query) ;
        return !q.getResultList().isEmpty();
    }

    @Override
    public boolean estProprietaire(Annonce a,Employeur e){
        return a!=null&&e!=null&&e.equals(a.getEmployeur());
    }
        
    @Override
    public List<String> getAllVille() {
        String query = "SELECT DISTINCT a.ville FROM Adresse a ";
        Query q = em.createQuery(query);
        return q.getResultList();
    }
    
    @Override
    public void supprimerAnnonce(Annonce a){

        String query;
        Query requestQuery;
       
        //Suppression de toutes les NotificationEmployeur ou la CandidatureAnnonce est lié a mon annonce
        query="Delete from NotificationEmployeur n where n.candidature.annonce.id='"+a.getId()+"'";
        requestQuery=em.createQuery(query);
        requestQuery.executeUpdate();
        
        //Récupération de tous les candidats
        List<Candidat> listeCand=em.createQuery("Select c from Candidat c").getResultList();
        //Suppression des NotificationCandidat des candidats en rapport avec l'annonce
        for(Candidat c:listeCand){
            List<NotificationCandidat> listeNot=(List<NotificationCandidat>)em.createQuery("Select c from NotificationCandidat c where c.annonce.id='"+a.getId()+"'").getResultList();
            for(NotificationCandidat nc:listeNot){
                removeNotification(c, nc);
            }
            em.merge(c);
        }
        
        //Suppression des NotificationCandidat en rapport avec mon anonce
        query="Delete from NotificationCandidat n where n.annonce.id='"+a.getId()+"'";
        requestQuery=em.createQuery(query);
        requestQuery.executeUpdate();
        
        //Suppression des SuggestionAnnonce des candidats la SuggestionAnnonce en rapport avec mon annonce
        for(Candidat c:listeCand){
            List<SuggestionAnnonce> listeSug=em.createQuery("Select c from SuggestionAnnonce c where c.annonce.id='"+a.getId()+"'").getResultList();
            for(SuggestionAnnonce nc:listeSug){
                removeSuggestion(nc, c);
            }
            em.merge(c);
        }

        //Suppression des SuggestioinAnonce en rapport avec mon annonce
        query="Delete from SuggestionAnnonce n where n.annonce.id='"+a.getId()+"'";
        requestQuery=em.createQuery(query);
        requestQuery.executeUpdate();

        //Suppression des CandidatureAnnonce en rapport avec mon annonce
        query="Delete from CandidatureAnnonce n where n.annonce.id='"+a.getId()+"'";
        requestQuery=em.createQuery(query);
        requestQuery.executeUpdate();
        
        em.remove(em.merge(a));
    }

    @Override
    public void supprimerAnnonce() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
        Date d = new Date();
        String query = "SELECT a FROM Annonce a WHERE a.dateLimite<'"+sdf.format(d)+"'";
        Query q = em.createQuery(query);
        List<Annonce> la = q.getResultList();
        for(Annonce a : la) {
            supprimerAnnonce(a);
        }
    }

    @Override
    public int getNbAnnoncesPostees() {
        String query = "SELECT a FROM Annonce a ";
        Query q = em.createQuery(query);
        try{
            return  q.getResultList().size();
        }catch(Exception e){
            return 0;
        }               
    }
    
    @Override
    public void miseAJourAdresse(Adresse s){
        em.merge(s);
    }
    @Override
    public void miseAJourAnnonce(Annonce a){
        em.merge(a);
    }
    
    @Override
    public void enregistrerAdresse(Adresse a){
        em.persist(a);
    }

    public void removeNotification(Candidat candidat, NotificationCandidat notification) {
        candidat.getNotifications().remove(notification);
        em.merge(candidat);
        em.remove(em.merge(notification));
    }
    
    public void removeSuggestion(SuggestionAnnonce suggestion, Candidat candidat) {
        candidat.getSuggestions().remove(suggestion);
        em.merge(candidat);
        em.remove(em.merge(suggestion));
    }
    
    @Override
    public CandidatureAnnonce getCandidatureAnnonce(long id) {
        CandidatureAnnonce ca = em.find(CandidatureAnnonce.class, id);
        return ca ;
    }

    @Override
    public CandidatureSpontanee getCandidatureSpontanee(long id) {
        CandidatureSpontanee cs = em.find(CandidatureSpontanee.class, id) ;
        return cs ;
    }

    @Override
    public void supprimerCandidatureSpontanee(CandidatureSpontanee cs,Employeur e){
        em.merge(cs);
        int i=0;
        while(i<cs.getEmployeurs().size()&&cs.getEmployeurs().get(i).getId()!=e.getId()){
            i++;
        }
        cs.getEmployeurs().remove(i);
        em.merge(cs);
        if(cs.getEmployeurs().isEmpty()){
            int j=0;
            while(j<e.getCandidatures().size()&&e.getCandidatures().get(j).getId()!=cs.getId()){
             j++;
            }
            e.getCandidatures().remove(j);
            em.merge(e);
            Query q=em.createQuery("Select n from NotificationCandidatureS n where n.candidature.id="+cs.getId());
            List<NotificationCandidatureS> ls=q.getResultList();
            for(NotificationCandidatureS nn:ls){
                em.remove(em.merge(nn));
            }
            em.remove(em.merge(cs));
        }
    }
}
