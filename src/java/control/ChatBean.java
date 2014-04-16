package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.*;


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
    
    /**
     * Permet de gérer la page de chat. Lors de l'arrivée sur cette page, si la liste d'attente n'éxiste aps elle est crée.
     */
    
    public ChatBean() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(servletContext.getAttribute("listeUtilisateursAttente") == null){
            servletContext.setAttribute("listeUtilisateursAttente", new ArrayList<Utilisateur>());
        }
        lastUpdate = new Date(0);
    }
    
    public Date getLastUpdate(){
        return lastUpdate;
    }
    
    public void setLastUpdate(Date lastUpdate){
        this.lastUpdate = lastUpdate;
    }
    
    public String getCorrespondant(){
        if (getChat().getEstDemarree())
        {
            if (getChat().getUtilisateur1().getPseudo().equals(utilisateurSession))
                return getChat().getUtilisateur2().getPseudo();
            else
                return getChat().getUtilisateur1().getPseudo();
        }
        return "";
    }
    /**
     * Cette fonction permet de lancer le chat en mode aleatoire
     * @return
     * @throws Exception 
     */
    public String chatAleatoire() throws Exception {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<Utilisateur> listeAttente = (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente") ;
        Utilisateur u1 = getUtilisateurSession() ;
        Utilisateur u2 = null ;
        
        //Normalement cela ne doit jamais ce produire
        if (u1 == null)
            return "index.xhtml" ;
        
        //Tout les chats de l'utilisateur doivent être fermé
        u1.closeAllChat () ;
        
        //on trouve un copain, si il y en a pas l'utilisateur attend
        u2 = obtenirChatteur (u1) ;
        if (u2 == null) {
            listeAttente.add(u1);
            return "profil.xhtml" ;
        }
        
        // on récupère ou on crée le chat
        obtenirChat (u1,u2) ;
        return "chat.xhtml" ;
    }
    
    public String chatCopain(Utilisateur ami) throws Exception {
        Utilisateur u1 = getUtilisateurSession() ;
        
        if ((u1 == null) || (ami == null))
            return "profil.xthtml" ;
        
        //si il est dans la liste d'attente le sortir, pareil pour ami
        removeFromWaitList (u1) ;
        removeFromWaitList (ami) ;
        
        //Tout les chats de l'utilisateur doivent être fermé
        u1.closeAllChat () ;
        
        //si on est deja dans un chat !
        if (u1.getSessionChatDemarree()!= null)
            return "chat.xhtml" ;
        
        obtenirChat(u1, ami) ;
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
        return u.getSessionChatDemarree() ;
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
    
    public void envoyerMessage () throws Exception {
        this.ut.begin();
        Utilisateur u = getUtilisateurSession() ;
        SessionChat chat = getChat();
        MessageChat msg = new MessageChat(message,u);
        this.em.persist(msg);
        chat.getMessages().add(msg);
        this.em.merge(chat);
        this.ut.commit();
        //return "chat.xhtml" ;
    }  
    
        /*public void envoyerMessage(ActionEvent evt) throws Exception 
    {         
        this.ut.begin();
        Utilisateur u = getUtilisateurSession() ;
        SessionChat chat = getChat();
        MessageChat msg = new MessageChat(message,u);
        this.em.persist(msg);
        chat.getMessages().add(msg);
        this.em.merge(chat);
        this.ut.commit();
    }*/

    private Utilisateur obtenirChatteur(Utilisateur u1) {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<Utilisateur> listeAttente = (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente") ;

        if (listeAttente.isEmpty())
            return null ;

        if (u1.equals(listeAttente.get(0))) {
             if (u1.equals(listeAttente.get(listeAttente.size()-1)))
                 return null ;
             return listeAttente.remove(listeAttente.size()-1) ;
        }
        return listeAttente.remove(0) ;

    }

    private SessionChat obtenirChat(Utilisateur u1, Utilisateur u2) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        SessionChat c = u1.recupererChat (u2) ;
        if (c == null) {
            this.ut.begin();
            c =  new SessionChat (u1,u2) ;
            c.setEstDemarree(true);
            this.em.persist(c);
            u1.ajouterChat(c);
            this.em.merge(u1);
            u2.ajouterChat(c);
            this.em.merge(u2);
            this.ut.commit();
        }
        c.setEstDemarree(true);
        return u1.getSessionChatDemarree();
    }

    private void removeFromWaitList(Utilisateur u1) {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<Utilisateur> listeAttente = (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente") ;
        
        listeAttente.remove(u1);
    }  
    
    public void quitterChat() throws Exception{
        this.ut.begin();
        this.utilisateurSession.getSessionChatDemarree().setEstDemarree(false);
        this.em.merge(this.utilisateurSession);
        this.ut.commit();
    }
}
