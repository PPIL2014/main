/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession;
    }
    
    public List<MyBadge> verification() {
        if(this.getUtilisateurSession() == null) {
            return new ArrayList<>();
        } else {
            List<MyBadge> badgesResult = new ArrayList<>();
            Utilisateur utilisateur = this.getUtilisateurSession();
            
            Query query = em.createQuery("SELECT b FROM Badge b");
            List<Badge> badgesListe = (List<Badge>)query.getResultList();
            
            for(Badge badge : badgesListe) {
                if(badge.getField() == null || badge.getField().equals("")) {
                    
                } else {
                    
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
