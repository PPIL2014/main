package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.*;
import model.SessionChat.* ;


@ManagedBean
@SessionScoped
public class ChatBean implements Serializable {
   
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;

    @ManagedProperty(value="#{message}")
    private String message;
    
    private SessionChat sessionChat;
    
    private Date lastUpdate;
    
    /**
     * Permet de gérer la page de chat. Lors de l'arrivée sur cette page, si la liste d'attente n'éxiste aps elle est crée.
     */
    
    public ChatBean() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(servletContext.getAttribute("listeUtilisateursAttente") == null){
            servletContext.setAttribute("listeUtilisateursAttente", new ArrayList<Utilisateur>());
        }
        if(servletContext.getAttribute("listeUtilisateursAttente60s") == null){
            servletContext.setAttribute("listeUtilisateursAttente60s", new ArrayList<Utilisateur>());
        }
        if(servletContext.getAttribute("listeUtilisateursAttenteAlea") == null){
            servletContext.setAttribute("listeUtilisateursAttenteAlea", new ArrayList<Utilisateur>());
        }
        if (servletContext.getAttribute("listeAffinite") == null) {
            servletContext.setAttribute("listeAffinite", new ListeAffinite());
        }
        
        lastUpdate = new Date(0);
    }
 
    public Date getLastUpdate(){
        return lastUpdate;
    }
    
    public void setLastUpdate(Date lastUpdate){
        this.lastUpdate = lastUpdate;
    }
    
    public String getMessage () {
        return "" ;
    }
    
    public void setMessage (String message) {
        this.message = message ;
    }

    public String getCorrespondant(){
        Utilisateur u = getUtilisateurSession() ;
        sessionChat = u.getSessionChatDemarree() ;
        
        if (sessionChat.getEstDemarree())
        {
            if (sessionChat.getUtilisateur1().equals(getUtilisateurSession ()))
                return sessionChat.getUtilisateur2().getPseudo();
            else
                return sessionChat.getUtilisateur1().getPseudo();
        }
        return "";
    }
    /**
     * Cette fonction permet de lancer le chat en mode aleatoire
     * @return
     * @throws Exception 
     */
    public String chatAffinite() throws Exception {
        return chat(SessionChat.Type.AFFNITE);
    }
    
    public String chat60s() throws Exception{
        return chat(SessionChat.Type.CHRONO);
    }
    
    public String chatAleatoire() throws Exception {
        return chat(SessionChat.Type.ALEATOIRE);
    }
    
    public String chat(SessionChat.Type type) throws Exception
    {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<Utilisateur> listeAttente = null;
        
        if (type == SessionChat.Type.AFFNITE) 
            listeAttente = (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente") ;
        else if (type == SessionChat.Type.CHRONO)
            listeAttente = (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente60s") ;
        else if (type == SessionChat.Type.ALEATOIRE)
            listeAttente = (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttenteAlea") ;
        
        
        Utilisateur u1 = getUtilisateurSession() ;
        Utilisateur u2 = null ;
                               
         
        //Normalement cela ne doit jamais ce produire
        if (u1 == null)
            return "index.xhtml" ;
        
        //Tout les chats de l'utilisateur doivent être fermé
        this.ut.begin();
        u1.closeAllChat();
        this.em.merge(u1);
        this.ut.commit();

        //est-ce que l'on a déja calculé les affinité pour ce chatteur ?
        //calculAffinite(u1) ;
        if (type != SessionChat.Type.ALEATOIRE)
            getListeAffinite().ajouterUtilisateur(u1);
        
        //on trouve un copain, si il y en a pas l'utilisateur attend
        u2 = obtenirChatteur (u1,type) ;
        if (u2 == null) {
            if (!listeAttente.contains(u1))
                listeAttente.add(u1);
            return "profil.xhtml" ;
        }
        
        
        // on récupère ou on crée le chat
        sessionChat = obtenirChat (u1,u2, type) ;
        listeAttente.remove(u1);
        listeAttente.remove(u2);
        this.ut.begin();
        sessionChat.setDebutSession(new Date());
        this.em.merge(sessionChat);
        this.ut.commit();
        return "chat.xhtml" ;        
    }
    
    public String chatCopain(Utilisateur ami) throws Exception {
        Utilisateur u1 = getUtilisateurSession() ;
        Utilisateur u2 = null;
        
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
        
        sessionChat = obtenirChat(u1, ami, SessionChat.Type.AMIS) ;
        u2 = ami ;
        ut.begin();
        SessionChat c = u1.recupererChat(u2) ;
        c.setType (Type.AMIS) ;
        em.merge(c);
        ut.commit();
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
    
    public ArrayList<Utilisateur> getListeUtilisateurAttente60s () {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente60s") ;
    }
    
    public ArrayList<Utilisateur> getListeUtilisateurAttenteAlea () {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttenteAlea") ;
    }
    
    public ListeAffinite getListeAffinite () {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ListeAffinite) servletContext.getAttribute("listeAffinite") ;
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
    
    private Utilisateur obtenirChatteur(Utilisateur u1, SessionChat.Type type) {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<Utilisateur> listeAttente = null;
        ArrayList<Utilisateur> listeCandidat = null;
        
        if (type == SessionChat.Type.AFFNITE)
            listeAttente = (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente") ;
        else if (type == SessionChat.Type.CHRONO)
            listeAttente = (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente60s") ;
        else if (type == SessionChat.Type.ALEATOIRE)
            listeAttente = (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttenteAlea") ;
        
        if (listeAttente.isEmpty())
            return null ;

        //60s ou affinite, on choisit la personne suivante de la même manière
        
        if (listeAttente.size() == 1 && (type != SessionChat.Type.ALEATOIRE)) {
            //pas assez de gens, on l'ajoute aussi
            return null ;
        }
        listeCandidat = (ArrayList<Utilisateur>) listeAttente.clone();
        //On supprime de la liste les personnes avec qui ont est déjà en contact
        for (Contact c : u1.getContacts())
        {
            listeCandidat.remove(c.getEstEnContactAvec());
        }
        /*if (type != SessionChat.Type.ALEATOIRE)
        {
            listeCandidat = (ArrayList<Utilisateur>) listeAttente.clone();
            // On cherche le dernier correspondant avec qui u1 à tchatter et on l'enleve de notre liste d'attente       
            Query q = em.createQuery("SELECT s FROM SessionChat s WHERE s.utilisateur1.pseudo = :pseudo OR s.utilisateur2.pseudo = :pseudo ORDER BY s.debutSession DESC");
            q.setParameter("pseudo", u1.getPseudo());
            List<SessionChat> listChat = q.getResultList();
            if (listChat.size() > 0)
            {
                SessionChat s = listChat.get(0);
                if (s.getUtilisateur1().equals(u1))
                    listeCandidat.remove(s.getUtilisateur2());
                else
                    listeCandidat.remove(s.getUtilisateur1());
            }
        }*/
        
        //On chercher le meilleur correspondant
        Utilisateur chatteur = null;
        if (type == SessionChat.Type.ALEATOIRE)        
            chatteur = getListeUtilisateurAttenteAlea().remove(0);  
        else
        {
            chatteur = getListeAffinite().getCorrespondant(u1, listeCandidat) ;       
           //listeAttente.remove(chatteur);
        }
        
        return chatteur ;
        
        /*if (u1.equals(listeAttente.get(0))) {
             if (u1.equals(listeAttente.get(listeAttente.size()-1)))
                 return null ;
             return listeAttente.remove(listeAttente.size()-1) ;
        }
        return listeAttente.remove(0) ;*/
        
    }

    private SessionChat obtenirChat(Utilisateur u1, Utilisateur u2, SessionChat.Type type) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        SessionChat c = u1.recupererChat (u2) ;
        if (c!=null)        
                c.setType(type);        
           
        this.ut.begin();
        if (c == null) {
            c =  new SessionChat (u1,u2,type) ;
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
        getListeUtilisateurAttente().remove(u1);
        getListeUtilisateurAttente60s().remove(u1);
        getListeUtilisateurAttenteAlea().remove(u1);
    }  
    
    /**
     * Cette methode est appellée lorque le client click sur quitter le chat afin de fermer la session chat en cours
    */
    
    public void quitterChat() throws Exception{
        this.ut.begin();
        Utilisateur u= getUtilisateurSession() ;
        u.getSessionChatDemarree().setEstDemarree(false);
        getListeAffinite().supprimerUtilisateur(u);
        this.em.merge(u);
        this.ut.commit();
    }
    
    /*
    les 3 prochaines methode sont appelé quand on subit un quittage !!
    il faut gérerl'ajout à la liste d'amis grace a u1 et u2 stocké dans le bean qui permettent de retrouver le chat
    */
    
    public String ajouterEtContinuer () throws Exception {
        this.ut.begin();
        Contact c = null;

        if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){
            c = new Contact(Contact.Type.ENATTENTE, this.sessionChat.getUtilisateur2());
            getUtilisateurSession().getContacts().add(c);

            Utilisateur u = em.find(Utilisateur.class, this.sessionChat.getUtilisateur2().getPseudo());
            Contact c2 = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur1());
            u.getContacts().add(c2);
            this.em.merge(u);
            this.em.merge(getUtilisateurSession());
            this.ut.commit();
        }
        else{
            c = new Contact(Contact.Type.ENATTENTE, this.sessionChat.getUtilisateur1());
            getUtilisateurSession().getContacts().add(c); 
            Utilisateur u = em.find(Utilisateur.class, this.sessionChat.getUtilisateur1().getPseudo());
            Contact c2 = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur2());
            u.getContacts().add(c2);
            this.em.merge(getUtilisateurSession());
            this.em.merge(u);
            this.ut.commit();
    }
     /*   if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){
       //     c = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur2());
        }else{
         //   c = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur1());
        }
        getUtilisateurSession().getContacts().add(c);
        this.em.merge(getUtilisateurSession());
        this.ut.commit();
       */
        
        if (sessionChat.getType() == SessionChat.Type.AFFNITE)
            return chatAffinite();
        else if (sessionChat.getType() == SessionChat.Type.CHRONO)
            return chat60s();
        else if (sessionChat.getType() == SessionChat.Type.ALEATOIRE)
            return chatAleatoire();
        else
            return "profil.xhtml";              
    }
    
    public String passerEtContinuer () throws Exception {
         if (sessionChat.getType() == SessionChat.Type.AFFNITE)
            return chatAffinite();
        else if (sessionChat.getType() == SessionChat.Type.CHRONO)
            return chat60s();
        else if (sessionChat.getType() == SessionChat.Type.ALEATOIRE)
            return chatAleatoire();
        else
            return "profil.xhtml";    
    }
    
    public String bloquerEtContinuer () throws Exception {
        this.ut.begin();
        Contact c = null;
        if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){
            c = new Contact(Contact.Type.BLOQUE, this.sessionChat.getUtilisateur2());
        }else{
            c = new Contact(Contact.Type.BLOQUE, this.sessionChat.getUtilisateur1());
        }
        getUtilisateurSession().getContacts().add(c);
        this.em.merge(getUtilisateurSession());
        this.ut.commit();
        if (sessionChat.getType() == SessionChat.Type.AFFNITE)
            return chatAffinite();
        else if (sessionChat.getType() == SessionChat.Type.CHRONO)
            return chat60s();
        else if (sessionChat.getType() == SessionChat.Type.ALEATOIRE)
            return chatAleatoire();
        else
            return "profil.xhtml";    
    }
    
    /*
    on quitte et on part
    */
    
    public String ajouterEtQuitter () throws Exception {
        this.ut.begin();
        Contact c = null;
        if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){             
            c = new Contact(Contact.Type.ENATTENTE, this.sessionChat.getUtilisateur2());
            getUtilisateurSession().getContacts().add(c);

            Utilisateur u = em.find(Utilisateur.class, this.sessionChat.getUtilisateur2().getPseudo());
            Contact c2 = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur1());
            u.getContacts().add(c2);
            this.em.merge(u);
            this.em.merge(getUtilisateurSession());
            this.ut.commit();  
        }
        else{
            c = new Contact(Contact.Type.ENATTENTE, this.sessionChat.getUtilisateur1());
            getUtilisateurSession().getContacts().add(c); 
            Utilisateur u = em.find(Utilisateur.class, this.sessionChat.getUtilisateur1().getPseudo());
            Contact c2 = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur2());
            u.getContacts().add(c2);
             this.em.merge(getUtilisateurSession());
            this.em.merge(u);
            this.ut.commit();
        }
    
        quitterChat();
        return "profil.xhtml" ;
    }
    
    public String rienEtQuitter () throws Exception {
        /*this.ut.begin();
        Contact c = null;
        if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){
            c = new Contact(Contact.Type.REFUSE, this.sessionChat.getUtilisateur2());
        }else{
            c = new Contact(Contact.Type.REFUSE, this.sessionChat.getUtilisateur1());
        }
        getUtilisateurSession().getContacts().add(c);
        this.em.merge(getUtilisateurSession());
        this.ut.commit();
        */
        quitterChat();
        return "profil.xhtml" ;
    }
    
    public String bloquerEtQuitter () throws Exception {
        this.ut.begin();
        Contact c = null;
        if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){
            c = new Contact(Contact.Type.BLOQUE, this.sessionChat.getUtilisateur2());
        }else{
            c = new Contact(Contact.Type.BLOQUE, this.sessionChat.getUtilisateur1());
        }
        getUtilisateurSession().getContacts().add(c);
        this.em.merge(getUtilisateurSession());
        this.ut.commit();
        quitterChat();
        return "profil.xhtml" ;
    }
    
    /*
    on fait suivant !
    */
    
    public String ajouterEtSuivantDemande () throws Exception {
        Contact c = null;
        this.ut.begin();
        
        
         /*if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){
        //    c = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur2());
        }else{
          //  c = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur1());
        }*/
        
        
        if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){
           
              
            c = new Contact(Contact.Type.ENATTENTE, this.sessionChat.getUtilisateur2());
            getUtilisateurSession().getContacts().add(c);

            Utilisateur u = em.find(Utilisateur.class, this.sessionChat.getUtilisateur2().getPseudo());
            Contact c2 = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur1());
            u.getContacts().add(c2);
            this.em.merge(u);
            this.em.merge(getUtilisateurSession());           
        }else{
            c = new Contact(Contact.Type.ENATTENTE, this.sessionChat.getUtilisateur1());
            getUtilisateurSession().getContacts().add(c); 
            Utilisateur u = em.find(Utilisateur.class, this.sessionChat.getUtilisateur1().getPseudo());
            Contact c2 = new Contact(Contact.Type.DEMANDE, this.sessionChat.getUtilisateur2());
            u.getContacts().add(c2);
            this.em.merge(getUtilisateurSession());
            this.em.merge(u);
        }
        
        getUtilisateurSession().getContacts().add(c);
        this.em.merge(getUtilisateurSession());
        this.ut.commit();
        quitterChat();
        if (sessionChat.getType() == SessionChat.Type.AFFNITE)
            return chatAffinite();
        else if (sessionChat.getType() == SessionChat.Type.CHRONO)
            return chat60s();
        else if (sessionChat.getType() == SessionChat.Type.ALEATOIRE)
            return chatAleatoire();
        else
            return "profil.xhtml";   
    }
    
    public String rienEtSuivantDemande () throws Exception {
        /*
        this.ut.begin();
        Contact c = null;
        if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){
            c = new Contact(Contact.Type.REFUSE, this.sessionChat.getUtilisateur2());
        }else{
            c = new Contact(Contact.Type.REFUSE, this.sessionChat.getUtilisateur1());
        }
        
        getUtilisateurSession().getContacts().add(c);
        this.em.merge(getUtilisateurSession());
        this.ut.commit();
        */
        quitterChat();
        if (sessionChat.getType() == SessionChat.Type.AFFNITE)
            return chatAffinite();
        else if (sessionChat.getType() == SessionChat.Type.CHRONO)
            return chat60s();
        else if (sessionChat.getType() == SessionChat.Type.ALEATOIRE)
            return chatAleatoire();
        else
            return "profil.xhtml";   
    }
    
    public String bloquerEtSuivantDemande () throws Exception {
        this.ut.begin();
        Contact c = null;
         if(getUtilisateurSession().getPseudo().equals(this.sessionChat.getUtilisateur1().getPseudo())){
            c = new Contact(Contact.Type.BLOQUE, this.sessionChat.getUtilisateur2());
        }else{
            c = new Contact(Contact.Type.BLOQUE, this.sessionChat.getUtilisateur1());
        }
        getUtilisateurSession().getContacts().add(c);
        this.em.merge(getUtilisateurSession());
        this.ut.commit();
        quitterChat();
        if (sessionChat.getType() == SessionChat.Type.AFFNITE)
            return chatAffinite();
        else if (sessionChat.getType() == SessionChat.Type.CHRONO)
            return chat60s();
        else if (sessionChat.getType() == SessionChat.Type.ALEATOIRE)
            return chatAleatoire();
        else
            return "profil.xhtml";   
    }
    
    
    //quand on ets ami et qu'on subit un quittage
  /*  public String sortirDuChat () {
        return "profil.xhtml" ;
    }
    
    public boolean getEstUnChatCopain () {
        // on remplit les utilisateur
        Utilisateur u1 = getUtilisateurSession() ;
        Utilisateur u2 = null;
        //SessionChat c = u1.getSessionChatDemarree() ;
        if (sessionChat != null) {
            if (sessionChat.getUtilisateur1().equals(u1)) {
                u2 = sessionChat.getUtilisateur2();
            } else {
                u2 = sessionChat.getUtilisateur1() ;
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
    }*/
    
    
    public SessionChat getSessionChat() {
        return sessionChat;
    }

    public void setSessionChat(SessionChat sessionChat) {
        this.sessionChat = sessionChat;
    }
    
    public void initSessionChat(ActionEvent evt){
        this.sessionChat = getUtilisateurSession().getSessionChatDemarree();
    }

}
