/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.SignalementUtilisateur;
import model.Utilisateur;

/**
 *
 * @author Yan
 */
@Named(value = "signalementUtilisateurBean")
@Dependent
public class SignalementUtilisateurBean {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    
    @Resource
    private UserTransaction ut;
    
    public Utilisateur user;
    
    @ManagedProperty(value = "#{description}")
    public String description;
    
    public SignalementUtilisateur signalement;
    
    /**
     * Creates a new instance of SignalementUtilisateurBean
     */
    public SignalementUtilisateurBean() {
    }
    
    public String signaler(){
        try{
            signalement = new SignalementUtilisateur();
            signalement.setDate(new Date());
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            signalement.setEmetteur((Utilisateur) session.getAttribute("user"));
            signalement.setEstTraitee(false);
            signalement.setUtilisateurSignale(user);
            ut.begin();
            em.persist(signalement);
            em.merge(user);
            ut.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            Logger.getLogger(SignalementUtilisateurBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
