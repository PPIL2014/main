package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
import model.MessageChat;
import model.SessionChat;
import model.Utilisateur;


@ManagedBean
@ViewScoped
public class ChatBean implements Serializable {
   
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;

    @ManagedProperty(value="#{message}")
    private String message;
    
    @ManagedProperty(value="#{u1}")
    private Utilisateur u1 ;
    
    @ManagedProperty(value="#{u2}")
    private Utilisateur u2 ;
    
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

    public Utilisateur getU1() {
        return u1;
    }

    public void setU1(Utilisateur u1) {
        this.u1 = u1;
    }

    public Utilisateur getU2() {
        return u2;
    }

    public void setU2(Utilisateur u2) {
        this.u2 = u2;
    }
    
    public Date getLastUpdate(){
        return lastUpdate;
    }
    
    public void setLastUpdate(Date lastUpdate){
        this.lastUpdate = lastUpdate;
    }

    public String getCorrespondant(){
        SessionChat c = getChat() ;
        
        if (c == null)
            return null ;
        
        if (c.getEstDemarree())
        {
            if (c.getUtilisateur1().equals(getUtilisateurSession ()))
                return c.getUtilisateur2().getPseudo();
            else
                return c.getUtilisateur1().getPseudo();
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
        u1 = getUtilisateurSession() ;
        u2 = null ;
         
        //Normalement cela ne doit jamais ce produire
        if (u1 == null)
            return "index.xhtml" ;
        
        //Tout les chats de l'utilisateur doivent être fermé
        u1.closeAllChat () ;

        //on trouve un copain, si il y en a pas l'utilisateur attend
        u2 = obtenirChatteur (u1) ;
        if (u2 == null) {
            if (!listeAttente.contains(u1))
                listeAttente.add(u1);
            return "profil.xhtml" ;
        }
        
        // on récupère ou on crée le chat
        obtenirChat (u1,u2) ;
        return "chat.xhtml" ;
    }
    
    public String chatCopain(Utilisateur ami) throws Exception {
        u1 = getUtilisateurSession() ;
        
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
        u2 = ami ;
        return "chat.xhtml" ;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur u = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return u ;
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
        if (!message.isEmpty())
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
        this.ut.begin();
        if (c == null) {
            c =  new SessionChat (u1,u2) ;
            c.setEstDemarree(true);
            this.em.persist(c);
            u1.ajouterChat(c);
            this.em.merge(u1);
            u2.ajouterChat(c);
            this.em.merge(u2);
        }
        c.setEstDemarree(true);
        this.em.merge(c);
        this.ut.commit();
        return u1.getSessionChatDemarree();
    }

    private void removeFromWaitList(Utilisateur u1) {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<Utilisateur> listeAttente = (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente") ;
        
        listeAttente.remove(u1);
    }  
    
    /**
     * Cette methode est appellée lorque le client click sur quitter le chat afin de fermer la session chat en cours
    */
    
    public void quitterChat() throws Exception{
        this.ut.begin();
        Utilisateur u= getUtilisateurSession() ;
        u.getSessionChatDemarree().setEstDemarree(false);
        this.em.merge(u);
        this.ut.commit();
    }
    
    /*
    les 3 prochaines methode sont appelé quand on subit un quittage !!
    il faut gérerl'ajout à la liste d'amis grace a u1 et u2 stocké dans le bean qui permettent de retrouver le chat
    */
    
    public String ajouterEtContinuer () throws Exception {
        return chatAleatoire() ;
    }
    
    public String passerEtContinuer () throws Exception {
        return chatAleatoire() ;
    }
    
    public String bloquerEtContinuer () throws Exception {
        return chatAleatoire() ;
    }
    
    /*
    on quitte et on part
    */
    
    public String ajouterEtQuitter () throws Exception {
        quitterChat();
        return "profil.xhtml" ;
    }
    
    public String rienEtQuitter () throws Exception {
        quitterChat();
        return "profil.xhtml" ;
    }
    
    public String bloquerEtQuitter () throws Exception {
        quitterChat();
        return "profil.xhtml" ;
    }
    
    /*
    on fait suivant !
    */
    
    public String ajouterEtSuivantDemande () throws Exception {
        quitterChat();
        return chatAleatoire() ;
    }
    
    public String rienEtSuivantDemande () throws Exception {
        quitterChat();
        return chatAleatoire() ;
    }
    
    public String bloquerEtSuivantDemande () throws Exception {
        quitterChat();
        return chatAleatoire() ;
    }
    
    
    //quand on ets ami et qu'on subit un quittage
    public String sortirDuChat () {
        return "profil.xhtml" ;
    }
    
    public boolean getEstUnChatCopain () {
        // on remplit les utilisateur
        u1 = getUtilisateurSession() ;
        SessionChat c = u1.getSessionChatDemarree() ;
        if (c != null) {
            if (c.getUtilisateur1().equals(u1)) {
                u2 = c.getUtilisateur2();
            } else {
                u2 = c.getUtilisateur1() ;
            }
        }
        
        if ((u1 == null) || (u2 == null)) {
            return false ;
        }
        
        return u1.estAmisAvec(u2) ;
    }
    
    public boolean getEstUnChatAlea () {
        System.out.println (getEstUnChatCopain()) ;
        return !getEstUnChatCopain() ;
    }
}
