/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
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
import model.SignalementUtilisateur;
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
    
    private Image photo;
    
    //@ManagedProperty(value = "#{motif}")
    private String motif;
    
    private UIComponent boutonSignaler;
    
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

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public UIComponent getBoutonSignaler() {
        return boutonSignaler;
    }

    public void setBoutonSignaler(UIComponent boutonSignaler) {
        this.boutonSignaler = boutonSignaler;
    }
    
    public String signaler(long id) {
        System.err.println("idphoto : "+id);
        photo=em.find(Image.class, id);
        System.err.println("photo : "+photo.getNom());
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur"));
        System.out.println("user = "+user.getPseudo());
       //     user = em.find(Utilisateur.class, pseudo);
        System.out.println("photo = "+photo);
        motif = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("motif");
        System.out.println("motif = "+motif);
        
      
        Query jQuery = em.createQuery("Select s From SignalementImage s Where s.estTraitee = 0 AND s.emetteur = :u1 AND s.imageSignalee = :u2");
        jQuery.setParameter("u1", user);
        jQuery.setParameter("u2", photo);
        List<SignalementUtilisateur> liste = jQuery.getResultList();
        if (!liste.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(this.boutonSignaler.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous avez deja signalé cette photo !", null));
        } else {
            try {
                SignalementImage s = new SignalementImage();
                s.setMotif(motif);
                s.setEmetteur(user);
                s.setImageSignalee(photo);
                s.setEstTraitee(false);
                s.setDate(new Date());
                photo.setSignalee(Boolean.TRUE);
                // user.getSignalementsImage().add(s);
                ut.begin();
                em.persist(s);
                em.merge(photo);
                em.merge(user);
                ut.commit();

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                e.printStackTrace();
            }

        }

        return "afficherGalerie.xhtml";
    }
    
    public List<SignalementImage> consulterSignalementsImage(){
            
        Query q = em.createQuery("SELECT s FROM SignalementImage s WHERE s.estTraitee=:traite");
        q.setParameter("traite", Boolean.FALSE);
        List<SignalementImage> results = (List<SignalementImage>) q.getResultList();

        return results;
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
