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
import model.SessionChat;
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
        return getUtilisateurSession().getAvatar() == null?"/resources/images/apercu.png":getUtilisateurSession().getAvatar().getUrl() ;
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
        if(servletContext.getAttribute("listeUtilisateursAttente") == null){
            servletContext.setAttribute("listeUtilisateursAttente", new ArrayList<Utilisateur>());
        }
        return (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente") ;
    }
    
    public ArrayList<Utilisateur> getListeUtilisateurAttente60s () {
        ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(servletContext.getAttribute("listeUtilisateursAttente60s") == null){
            servletContext.setAttribute("listeUtilisateursAttente60s", new ArrayList<Utilisateur>());
        }
        return (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente60s") ;
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
        ArrayList<Utilisateur> listeAttente60s = getListeUtilisateurAttente60s();
        Utilisateur u = getUtilisateurSession () ;
        return ( ((listeAttente.size() == 1) && (listeAttente.get(0).getPseudo().equals(u.getPseudo()))) ||
                    ((listeAttente60s.size() == 1) && (listeAttente60s.get(0).getPseudo().equals(u.getPseudo()))) ) ;
    }
  
    //Affiche un message à l'utilisateur si il est seul en attente. 
    public void addInfo() {  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Vous êtes seul(e) en attente !", "Vous serez redirigé(e) automatiquement lorsque quelqu'un d'autre arrivera !"));  
    }
}
