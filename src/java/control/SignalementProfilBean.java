/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import javax.annotation.Resource;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import model.Utilisateur;

/**
 *
 * @author ASUS
 */
@Named(value = "signalementProfilBean")
@ViewScoped
public class SignalementProfilBean {
   
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;

    @ManagedProperty(value="#{motif}")
    private String motif ;
    
    /**
     * Creates a new instance of SignalementProfilBean
     */
    public SignalementProfilBean() {
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }  
    
    /**
     * Cette methode est appellée lorque le client click sur quitter le chat afin de fermer la session chat en cours
    */
    
    public void quitterChat() throws Exception{
        this.ut.begin();
        Utilisateur u= getUtilisateurSession() ;
        u.getSessionChatDemarree().setEstDemarree(false);
        this.em.merge(u);
        this.ut.commit();
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur u = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return u ;
    }
    
    public String signaler() throws Exception {
        System.out.println(motif);
        quitterChat();
        return "profil.xhtml" ;
    }
}
