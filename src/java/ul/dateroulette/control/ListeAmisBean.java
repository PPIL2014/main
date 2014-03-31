package ul.dateroulette.control;

import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.Utilisateur;

@Named(value = "listeAmisBean")
@RequestScoped
public class ListeAmisBean {
    
    public ListeAmisBean() {
        
    }
    
    public String afficherAmis () {
        return "listeAmis.xhtml" ;
    }
    
    public ArrayList<Utilisateur> getListeAmis () {
        return getUtilisateurSession ().getListeAmis () ;
    }
    
    public boolean estConnecte (Utilisateur u) {
        return true ;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        return (Utilisateur) session.getAttribute("utilisateur") ;
    }
}
