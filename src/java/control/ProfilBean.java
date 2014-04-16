/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.Collection;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import model.Image;
import model.Utilisateur;

/**
 *
 * @author arnould23u
 */
@ManagedBean
@RequestScoped
public class ProfilBean {

    /**
     * Creates a new instance of ProfilBean
     */
    public ProfilBean() {
        
    }
    
    public String getPseudo () {
        return getUtilisateurSession ().getPseudo() ;
    }
    
    public Image getAvatar () {
        return getUtilisateurSession ().getAvatar() ;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        return (Utilisateur) session.getAttribute("utilisateur");
    }
    
    public Collection<Utilisateur> getListeUtilisateurAttente () {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (Collection<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente") ;
    }
}
