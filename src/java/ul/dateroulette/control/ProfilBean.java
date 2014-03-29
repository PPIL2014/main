package ul.dateroulette.control;

import java.util.ArrayList;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import ul.dateroulette.model.Utilisateur;

@ManagedBean
@RequestScoped
public class ProfilBean {   

    /**
     * Creates a new instance of ProfilBean
     */
    public ProfilBean() {
        
    }
    
    public String getPseudo () {
        return getUtilisateurSession().getPseudo() ;
    }
    
    public String getUrlAvatar () {
        return getUtilisateurSession().getAvatar().getUrl() ;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        return (Utilisateur)session.getAttribute("utilisateur");
    }
    
    public ArrayList<Utilisateur> getListeUtilisateurAttente () {
        ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente") ;
    }
}
