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
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
import model.Contact;
import model.MyBadge;
import model.Utilisateur;

/**
 *
 * @author thomas
 */
@ManagedBean
@ViewScoped
public class BadgeBean implements Serializable{
    @PersistenceContext( unitName = "DateRoulettePU" )
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    private Collection<MyBadge> nouveauxBadges;

    public Collection<MyBadge> getNouveauxBadges() {
        return nouveauxBadges;
    }

    public void setNouveauxBadges(Collection<MyBadge> nouveauxBadges) {
        this.nouveauxBadges = nouveauxBadges;
    }
    
    @PostConstruct
    public void init() {
        this.initBadges();
        this.nouveauxBadges = this.verification();
        
        Utilisateur utilisateur = this.getUtilisateurSession();
        for(int i = 0; i < 5; i++) {
            Contact contact = new Contact();
            contact.setEstEnContactAvec(utilisateur);
            contact.setType(Contact.Type.AMI);
            utilisateur.getContacts().add(contact);
        }
        
        try {
            ut.begin();
                em.merge(utilisateur);
            ut.commit(); 
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            Logger.getLogger(BadgeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initBadges() {
        Query query = em.createQuery("SELECT b FROM Badge b");
        List<Badge> badgesListe = (List<Badge>)query.getResultList();
        Long[] id = {0l, 1l, 2l, 3l, 4l, 5l};
        int[] counters = {1, 10, 20, 30, 40, 50};
        String[] names = {"Vous avez ajouté votre premier contact", "Vous avez ajouté 10 contacts", "Vous avez ajouté 20 contacts", "Vous avez ajouté 30 contacts",
            "Vous avez ajouté 40 contacts", "Vous avez ajouté 50 contacts"};
        String[] tbn = {"Contact", "Contact", "Contact", "Contact", "Contact", "Contact"};
        String[] fields = {"", "", "", "", "", ""};
        String[] fieldsPseudo = {"estEnContactAvec.pseudo", "estEnContactAvec.pseudo", "estEnContactAvec.pseudo", "estEnContactAvec.pseudo", "estEnContactAvec.pseudo", "estEnContactAvec.pseudo"};
        Date[] startDates = {new Date(), new Date(), new Date(), new Date(), new Date(), new Date()};
        Date[] endDates = {new Date(), new Date(), new Date(), new Date(), new Date(), new Date()};
        if(badgesListe.isEmpty()) {
            try{
                ut.begin();
                for(int i = 0; i < counters.length; i++) {
                    Badge badge = new Badge();
                    badge.setId(id[i]);
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
    
    public void vu() {
        Utilisateur utilisateur = this.getUtilisateurSession();
        Collection<MyBadge> badges = utilisateur.getBadges();
        for(MyBadge mb : badges) {
            if(! mb.isEstApparu()) {
                mb.setEstApparu(Boolean.TRUE);
            }
        }
        try{
            ut.begin();
                em.merge(utilisateur);
            ut.commit();
        } catch (NotSupportedException | SystemException | RollbackException |
                HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            Logger.getLogger(BadgeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String mesBadges() {
        return "listeBadges.xhtml";
    }
}
