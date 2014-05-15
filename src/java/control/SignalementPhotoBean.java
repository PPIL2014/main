/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;
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
import model.Image;
import model.SignalementImage;
import model.Utilisateur;

/**
 *
 * @author Yan
 */
@Named(value = "signalementPhotoBean")
@Dependent
public class SignalementPhotoBean {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    
    @Resource
    private UserTransaction ut;
    
    private Utilisateur user;
    
    //private Utilsiateur userPhoto;
    
    private Image photo;
    
    @ManagedProperty(value = "#{motif}")
    private String motif;
    
    //private String pseudoUserPhoto;
    
    /**
     * Creates a new instance of SignalementPhotoBean
     */
    public SignalementPhotoBean() {
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }
    
    public String signaler() {

       HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Utilisateur) session.getAttribute("user");
        System.out.println("user = "+user.getPseudo());
       //     user = em.find(Utilisateur.class, pseudo);
        System.out.println("photo = "+photo);
        System.out.println("motif = "+motif);
        
      
      /*  if (signalementsUtilisateurs.contains((Object)u2)) {
            System.err.println("Vous avez deja signaler cet utilisateur");
        } else {*/
            try {
                SignalementImage s = new SignalementImage();
                s.setMotif(motif);
                s.setEmetteur(user);
                s.setImageSignalee(photo);
                s.setEstTraitee(false);
                s.setDate(new Date());
                // user.getSignalementsImage().add(s);
                ut.begin();
                em.persist(s);
                em.merge(user);
                ut.commit();

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                e.printStackTrace();
            }

     //   }

        return "chat.xhtml";
    }
    
    public ArrayList<SignalementImage> consulterSignalementsImage(){
        try{
            ut.begin();
            Query q = em.createQuery("SELECT s FROM SignalementImage s WHERE s.estTraitee=:traite");
            q.setParameter("traite", Boolean.FALSE);
            List<SignalementImage> results = (List<SignalementImage>) q.getResultList();
            ut.commit();
            
            return (ArrayList<SignalementImage>) results;
            
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void traiterOk(SignalementImage s){
        try{
            ut.begin();
            s.setEstTraitee(true);
            em.merge(s);
            ut.commit();
            
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
    }
    
    public void traiterPasOk(SignalementImage s){
        try{
            ut.begin();
            s.setEstTraitee(true);
            em.merge(s);
            ut.commit();
            
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
    }
}
