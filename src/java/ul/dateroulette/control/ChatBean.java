package ul.dateroulette.control;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import ul.dateroulette.model.MessageChat;
import ul.dateroulette.model.SessionChat;
import ul.dateroulette.model.Utilisateur;

@Named(value = "chatBean")
@RequestScoped
public class ChatBean implements Serializable {
   
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;

    @ManagedProperty(value="#{message}")
    private String message;
    
    @ManagedProperty(value="#{utilisateurSession}")
    private Utilisateur utilisateurSession ;
    
    public ChatBean() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(servletContext.getAttribute("listeUtilisateursAttente") == null){
            servletContext.setAttribute("listeUtilisateursAttente", new ArrayList<Utilisateur>());
        }
    }
    
    public String chat() throws Exception {
        ArrayList<Utilisateur> listeAttente = getListeUtilisateurAttente() ;
        Utilisateur u1 = getUtilisateurSession() ;
        Utilisateur u2 = null ;
        
        //si l'utilisateur est déja entrain de chater, on quitte la fonction !
        if (u1.getSessionChat() != null)
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
                this.ut.begin();
                SessionChat c = new SessionChat (u1,u2) ;
                this.em.persist(c);
                u1.setSessionChat(c);
                this.em.merge(u1);
                u2.setSessionChat(c);
                this.em.merge(u2);
                this.ut.commit();
            } else {
                return "profil.xhtml" ;
            }
            return "chat.xhtml" ;
        } else {
            //on ajoute l'utilisateur à la liste
            listeAttente.add(u1) ;
            return "profil.xhtml" ;
        }
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

    public SessionChat getChat() {
        //on recupere le chat de l'utilisateur en session
        Utilisateur u = getUtilisateurSession() ;
        return u.getSessionChat() ;
    }

    public void setChat(SessionChat chat) throws Exception {
        this.ut.begin();
        Utilisateur u = getUtilisateurSession() ;
        u.setSessionChat(chat);
        this.em.merge(u);
        this.ut.commit();
    }
    
    public ArrayList<Utilisateur> getListeUtilisateurAttente () {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente") ;
    }
    
    public String getMessage () {
        return "" ;
    }
    
    public void setMessage (String message) {
        this.message = message ;
    }
    
    public String envoyerMessage () throws Exception {
        this.ut.begin();
        Utilisateur u = getUtilisateurSession() ;
        SessionChat chat = getChat();
        MessageChat msg = new MessageChat(message,u);
        this.em.persist(msg);
        chat.getMessages().add(msg);
        this.em.merge(chat);
        this.ut.commit();
        return "chat.xhtml" ;
    }
}
