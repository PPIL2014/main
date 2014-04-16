package control;

import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import model.Chat;
import model.Message;
import model.Utilisateur;

@Named(value = "chatBean")
@RequestScoped
public class ChatBean implements Serializable {

    @ManagedProperty(value="#{message}")
    private String message;
    
    @ManagedProperty(value="#{utilisateurSession}")
    private Utilisateur utilisateurSession ;
    
    public ChatBean() {
 
    }
    
    public String chat() {
       /* Collection<Utilisateur> listeAttente = getListeUtilisateurAttente() ;
        Utilisateur u1 = getUtilisateurSession() ;
        Utilisateur u2 = null ;
        
        //si l'utilisateur est déja entrain de chater, on quitte la fonction !
        if (u1.getChat() != null)
            return "chat.xhtml" ;
        
        if (!listeAttente.isEmpty()) {
            if (u1.equals(listeAttente.get(0))) {
                 if (!u1.equals(listeAttente.get(listeAttente.size()-1))) {
                     // ON A UN AMI
                     u2 = listeAttente.remove(listeAttente.size()-1) ;
                 }
            } else {
                u2 = listeAttente.remove(0) ;
            }
            if (u2 != null) {
                Chat c = new Chat (u1,u2) ;
                u1.setChat(c) ;
                u2.setChat(c) ;
            } else {
                return "profil.xhtml" ;
            }
            return "chat.xhtml" ;
        } else {
            //on ajoute l'utilisateur à la liste
            listeAttente.add(u1) ;
            return "profil.xhtml" ;
        }*/
        return null;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        utilisateurSession = (Utilisateur) session.getAttribute("utilisateur") ;
        return utilisateurSession;
    }
    
    public void setUtilisateurSession (Utilisateur u) {
        utilisateurSession = u ;
    }

    public Chat getChat() {
        //on recupere le chat de l'utilisateur en session
        Utilisateur u = getUtilisateurSession() ;
        return u.getChat() ;
    }

    public void setChat(Chat chat) {
        Utilisateur u = getUtilisateurSession() ;
        u.setChat(chat);
    }
    
    public Collection<Utilisateur> getListeUtilisateurAttente () {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (Collection<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente") ;
    }
    
    public String getMessage () {
        return "" ;
    }
    
    public void setMessage (String message) {
        this.message = message ;
    }
    
    public String envoyerMessage () {
        Utilisateur u = getUtilisateurSession() ;
        getChat().getListeMessages().add(new Message(message,u));
        return "chat.xhtml" ;
    }
}