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
    
   public String deconnecter(){
        ArrayList<String> liste = getListeUtilisateurConnecte();
        liste.remove(this.getPseudo());
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        return "index.xhtml";
    }
   
   public boolean aChat () {
       Utilisateur u = getUtilisateurSession () ;
       return (u.getSessionChat() != null) ;
   }
    
}
