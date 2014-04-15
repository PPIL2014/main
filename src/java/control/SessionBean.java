package control;

import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.*;


@ManagedBean
@RequestScoped
public class SessionBean {
    
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
          
    public SessionBean() {
 
    }
    
     public String getPseudo () {
        return getUtilisateurSession ().getPseudo() ;
    }
     
     public Utilisateur getUtilisateurSession () {
       FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession ;
    }
    
    public ArrayList<String> getListeUtilisateurConnecte(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<String>) servletContext.getAttribute("listeUtilisateursConnecte");
    }
    
    public ArrayList<String> getListeAttente(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<String>) servletContext.getAttribute("listeUtilisateursAttente");
    }
    
   public String deconnecter() throws Exception{
        ut.begin();
        Utilisateur u = getUtilisateurSession();
        u.closeAllChat();
        em.merge(u);
        
        ArrayList<String> listeConnecte = getListeUtilisateurConnecte();
        //ArrayList<String> listeAttente = getListeAttente();
        listeConnecte.remove(this.getPseudo());
        //listeAttente.remove(this.getPseudo());
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        
        
        return "index.xhtml";
    }
   
   public boolean aChat () {
       Utilisateur u = getUtilisateurSession () ;
       return (u.getSessionChatDemarree()!= null) ;
   }
    
}
