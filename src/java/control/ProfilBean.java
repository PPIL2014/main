/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import model.Utilisateur;
import org.primefaces.context.RequestContext;

/**
 *
 * @author nicolas
 */
@ManagedBean
@RequestScoped
public class ProfilBean {
/**
     * Creates a new instance of ProfilBean
     */
    public ProfilBean() {
    }
    
     // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "DateRoulettePU" )
    private EntityManager em;

    private Utilisateur utilisateur;


    
    public String getPseudo () {
        return getUtilisateurSession().getPseudo() ;
    }
      
     public String getUrlAvatar () {
        return getUtilisateurSession() != null?"":System.getProperty("user.home")+"/dateImages/"+ getUtilisateurSession().getAvatar().getDescription() ;
    }
     

    
    public Utilisateur getUtilisateur(){
        return utilisateur;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession;
    }
    
    public ArrayList<Utilisateur> getListeUtilisateurAttente () {
        ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente") ;
    }
    
    public void chatInfo (ActionEvent evt) {
        RequestContext ctx = RequestContext.getCurrentInstance();
        
        Utilisateur u = getUtilisateurSession() ;
        ctx.addCallbackParam("ok", u!=null);
        
        if (u==null)
            return;
        
       if (u.getSessionChatDemarree() == null)
            return ;
        
        Utilisateur u2 = u.getSessionChatDemarree().getUtilisateur2() ;
        
        //si le tcvhat est à l'envers
        if (u.equals(u2))
            u2 = u.getSessionChatDemarree().getUtilisateur1() ;
        
        ctx.addCallbackParam("copain", u2.getPseudo());
    }
    
    public String goChat () {
        return "profil.xhtml" ;
    }
    
    public boolean getSeulEnAttente () {
        ArrayList<Utilisateur> listeAttente =  getListeUtilisateurAttente () ;
        Utilisateur u = getUtilisateurSession () ;
        return ((listeAttente.size() == 1) && (listeAttente.get(0).getPseudo().equals(u.getPseudo()))) ;
    }
  
    //Affiche un message à l'utilisateur si il est seul en attente. 
    public void addInfo() {  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Vous êtes seul(e) en attente !", "Vous serez redirigé(e) automatiquement lorsque quelqu'un d'autre arrivera !"));  
    }
}
