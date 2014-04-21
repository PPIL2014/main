package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import java.util.Collection;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import model.*;
import org.primefaces.context.RequestContext;


@ManagedBean
@ViewScoped
public class ChatBean implements Serializable {
   
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;

    @ManagedProperty(value="#{message}")
    private String message;
    
    @ManagedProperty(value="#{utilisateurSession}")
    private Utilisateur utilisateurSession ;
    
    private Date lastUpdate;
    
    public ChatBean() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(servletContext.getAttribute("listeUtilisateursAttente") == null){
            servletContext.setAttribute("listeUtilisateursAttente", new ArrayList<Utilisateur>());
        }
        
        lastUpdate = new Date(0);
    }
    
    public String chat() throws Exception {
        /*ArrayList<Utilisateur> listeAttente = getListeUtilisateurAttente() ;
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
        }*/
        return null;
    }
    
    public String chatCopain(Utilisateur ami) throws Exception {
        Utilisateur u1 = getUtilisateurSession() ;
        
        //si on est deja dans un chat !
        if (u1.getSessionChat() != null)
            return "chat.xhtml" ;
        //si l'ami est valide on chat
        if (ami != null) {
            this.ut.begin();
            SessionChat c = new SessionChat (u1,ami) ;
            this.em.persist(c);
            u1.setSessionChat(c);
            this.em.merge(u1);
            ami.setSessionChat(c);
            this.em.merge(ami);
            this.ut.commit();
        } else {
            return "listeAmis.xhtml" ;
        }
        return "chat.xhtml" ;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession ;
    }
    
    public void setUtilisateurSession (Utilisateur u) {
        utilisateurSession = u ;
    }

    public SessionChat getChat() {
        //on recupere le chat de l'utilisateur en session
        Utilisateur u = getUtilisateurSession() ;
        return u.getSessionChat() ;
    }
    
    public void envoyerMessage(ActionEvent evt) throws Exception 
    {         
        this.ut.begin();
        Utilisateur u = getUtilisateurSession() ;
        SessionChat chat = getChat();
        MessageChat msg = new MessageChat(message,u);
        this.em.persist(msg);
        chat.getMessages().add(msg);
        this.em.merge(chat);
        this.ut.commit();
    }

    public void setChat(SessionChat chat) throws Exception {
        this.ut.begin();
        Utilisateur u = getUtilisateurSession() ;
        u.setSessionChat(chat);
        this.em.merge(u);
        this.ut.commit();
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
    
    /*public String envoyerMessage () throws Exception {
        this.ut.begin();
        Utilisateur u = getUtilisateurSession() ;
        SessionChat chat = getChat();
        MessageChat msg = new MessageChat(message,u);
        this.em.persist(msg);
        chat.getMessages().add(msg);
        this.em.merge(chat);
        this.ut.commit();
        return "chat.xhtml" ;
    }*/
   
    
    public void firstUnreadMessage(ActionEvent evt) 
    { 
        RequestContext ctx = RequestContext.getCurrentInstance(); 
        MessageChat m = getChat().getFirstAfter(lastUpdate); 
        
        ctx.addCallbackParam("ok", m!=null); 
        if(m==null) 
            return; 
        lastUpdate = m.getDate(); 
        
        ctx.addCallbackParam("user", m.getExpediteur().getPseudo());
        ctx.addCallbackParam("dateSent", m.getDate().toString()); 
        ctx.addCallbackParam("text", m.getContenu()); 
    }
}