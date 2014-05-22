/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Badge;
import model.MyBadge;
import model.Utilisateur;

/**
 *
 * @author thomas
 */
@ManagedBean
@RequestScoped
public class BadgeBean implements Serializable{
    @PersistenceContext( unitName = "DateRoulettePU" )
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    public void initBadges() {
        Query query = em.createQuery("SELECT b FROM Badge b");
        List<Badge> badgesListe = (List<Badge>)query.getResultList();
        int[] counters = {10, 20, 30};
        String[] names = {"Plus de 10", "Plus de 20", "Plus de 30"};
        String[] tbn = {"Contact", "Contact", "Contact"};
        String[] fields = {"", "", ""};
        String[] fieldsPseudo = {"estEnContactAvec.pseudo", "estEnContactAvec.pseudo", "estEnContactAvec.pseudo"};
        Date[] startDates = {new Date(), new Date(), new Date()};
        Date[] endDates = {new Date(), new Date(), new Date()};
        if(badgesListe.isEmpty()) {
            try{
                ut.begin();
                for(int i = 0; i < counters.length; i++) {
                    Badge badge = new Badge();
                    badge.setCounter(counters[i]);
                    badge.setEndDate(endDates[i]);
                    badge.setField(fields[i]);
                    badge.setFieldPseudo(fieldsPseudo[i]);
                    badge.setName(names[i]);
                    badge.setStartDate(startDates[i]);
                    badge.setTableName(tbn[i]);
                    em.persist(badge);
                }
                ut.commit();
            } catch(NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                Logger.getLogger(BadgeBean.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    public Utilisateur getUtilisateurSession () {
        this.initBadges();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession;
    }
    
    public Collection<Badge> listeBadgesObtenus() {
        if(this.getUtilisateurSession() == null) {
            return new ArrayList<>();
        } else {
            List<Badge> badgesResult = new ArrayList<>();
            for(MyBadge mb : this.getUtilisateurSession().getBadges()) {
                badgesResult.add(mb.getBadge());
            }
            return badgesResult;
        }
    }
    
    public Collection<Badge> listeBadgesNonObtenus() {
        if(this.getUtilisateurSession() == null) {
            return new ArrayList<>();
        } else {
            List<Badge> badgesResult = new ArrayList<>();
            Utilisateur utilisateur = this.getUtilisateurSession();
            Query query = em.createQuery("SELECT b FROM Badge b");
            List<Badge> badgesListe = (List<Badge>)query.getResultList();
            boolean ajouter;
            for(Badge badge : badgesListe) {
                ajouter = true;
                for(MyBadge myBadge : utilisateur.getBadges()) {
                    if(myBadge.getBadge().getId() == badge.getId()) {
                        ajouter = false;
                        break;
                    } 
                }
                if(ajouter) {
                    badgesResult.add(badge);
                }
            }
            
            return badgesResult;
        }
    }
    
    public Collection<MyBadge> verification() {
        if(this.getUtilisateurSession() == null) {
            return new ArrayList<>();
        } else {
            List<MyBadge> badgesResult = new ArrayList<>();
            Utilisateur utilisateur = this.getUtilisateurSession();
            
            Query query = em.createQuery("SELECT b FROM Badge b");
            List<Badge> badgesListe = (List<Badge>)query.getResultList();
            Collection<Badge> lbno = listeBadgesNonObtenus();
            for(Badge badge : lbno) {
                if(badge.getField() == null || badge.getField().equals("")) {//Compter les tuples
                    query = em.createQuery("SELECT b FROM "+ badge.getTableName() +" b WHERE b." + badge.getFieldPseudo() + "=:pseudo");
                    query.setParameter("pseudo", utilisateur.getPseudo());
                    if(query.getResultList().size() >= badge.getCounter()) {
                        MyBadge mb = new MyBadge();
                        mb.setBadge(badge);
                        mb.setEstApparu(Boolean.FALSE);
                        mb.setUtilisateur(utilisateur);
                        utilisateur.getBadges().add(mb);
                    }
                } else {//Utiliser le champ indiqué dans getField pour compteur

                }
            }
            
            try {
                Collection<MyBadge> badges = utilisateur.getBadges();
                for(MyBadge mb : badges) {
                    if(! mb.isEstApparu()) {
                        badgesResult.add(mb);
                        mb.setEstApparu(Boolean.TRUE);
                    }
                }
                ut.begin();
                    em.merge(utilisateur);
                ut.commit();
            } catch (NotSupportedException | SystemException | RollbackException |
                    HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                Logger.getLogger(BadgeBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return badgesResult;
        }
    }
}
