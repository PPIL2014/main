package control;

import java.util.ArrayList;
import javax.annotation.Resource;
import java.util.Collection;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import org.primefaces.context.RequestContext;
import model.Utilisateur;

@ManagedBean
@RequestScoped
public class ProfilBean {

    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    /**
     * Creates a new instance of ProfilBean
     */
    public ProfilBean() {
        
    }
    
    public String getPseudo () {
        return getUtilisateurSession().getPseudo() ;
    }
    
    public String getUrlAvatar () {
        return "";//getUtilisateurSession().getAvatar().getUrl() ;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession ;
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
        
       if (u.getSessionChat() == null)
            return ;
        
        Utilisateur u2 = u.getSessionChat().getUtilisateur2() ;
        
        //si le tcvhat est à l'envers
        if (u.equals(u2))
            u2 = u.getSessionChat().getUtilisateur1() ;
        
        ctx.addCallbackParam("copain", u2.getPseudo());
    }
    
    public String goChat () {
        Utilisateur u1 = getUtilisateurSession() ;
        
        //si on est deja dans un chat !
        if (u1.getSessionChat() != null)
            return "chat.xhtml" ;
        
        return "profil.xhtml" ;

    public Collection<Utilisateur> getListeUtilisateurAttente () {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (Collection<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente") ;
    }
}
