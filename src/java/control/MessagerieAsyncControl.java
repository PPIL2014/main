/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Conversation;
import model.MessageConversation;
import model.Utilisateur;

/**
 *
 * @author charles9u
 */
@ManagedBean
@Named(value = "messagerieAsyncControl")
@RequestScoped
public class MessagerieAsyncControl {
    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    
    @ManagedProperty(value="#{param.conv}")
    private String id;
    
    private Conversation conversation;
    
    private Utilisateur user;
    
    @ManagedProperty(value="#{contenu}")
    private String contenu;
    
    /**
     * Creates a new instance of MessagerieAsyncControl
     */
    public MessagerieAsyncControl() {
    }
    
    /*@PostConstruct
    public void init(){
        id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("conv");
        System.out.println("conv : "+FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("conv"));
        conversation = getConversation(Long.parseLong(id));
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Utilisateur) session.getAttribute("user");
        
    }*/
    
    public Utilisateur getDest(){
        if(conversation.getExpediteur().getPseudo().equals(user.getPseudo()))
            return conversation.getDestinataire();
        else
            return conversation.getExpediteur();
    }
    
    public Utilisateur getExp(){
        if(conversation.getExpediteur().getPseudo().equals(user.getPseudo()))
            return conversation.getExpediteur();
        else
            return conversation.getDestinataire();
    }
    
    public Collection<MessageConversation> getMessages(){
        return conversation.getMessages() ;
    }
    
    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Methode permettant de récupérer une liste de messages privés (MessageConversation)
     * @return la liste des messages privés (MessageConversation)
     */
    public List<MessageConversation> getPrivateMessages() {
        if(id==null || id.isEmpty())
            id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("conv");
        System.err.println("conv : "+FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("conv"));
         conversation = getConversation(Long.parseLong(id));
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Utilisateur) session.getAttribute("user");
        return (List<MessageConversation>) conversation.getMessages();
    }
    
    /**
     * Méthode permettant de récupérer la conversation privée entre 2 utilisateurs
     * @return la (première et en théorie unique) conversation entre les deux utilisateurs
     */
    public Conversation getConversation(Long id) {
            
        if(id==null)
            id = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("conv"));


        Conversation results =  (Conversation) em.find(Conversation.class, id);

        return results;
    }
    
    public String getConv(Utilisateur u){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Utilisateur) em.find(Utilisateur.class, (String) session.getAttribute("pseudoUtilisateur"));
        
        if(u==null)
            return "Messagerie.xhtml";
        
        Query q = em.createQuery("SELECT c FROM Conversation c "
                + "WHERE c.expediteur=:nous AND c.destinataire=:autre "
                + "OR c.destinataire=:n AND c.expediteur=:a");
        q.setParameter("nous", user);
        q.setParameter("autre", u);
        q.setParameter("n", user);
        q.setParameter("a", u);
        
        conversation = (Conversation) q.getSingleResult();
        
        if(conversation==null)
            return "Messagerie.xhtml";
        
        id=""+conversation.getId();
        System.err.println("id : "+id);
        return "MessagerieAsynchrone.xhtml";
    }
    
    public String send() {
        try {
            
            System.err.println("Message : "+contenu);
            MessageConversation m = new MessageConversation();
            m.setConversation(conversation);
            m.setContenu(contenu);
            System.err.println("user : "+user.getPseudo());
            m.setExpediteur(user);
            Date d = new Date();
            m.setDate(d);
            conversation.getMessages().add(m);
            ut.begin();
            em.persist(m);
            em.merge(conversation);
            ut.commit();
            return "MessagerieAsynchrone.xhtml"/*?faces-redirect=true&conv="+conversation.getId()*/;
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException 
                | HeuristicRollbackException | SecurityException | IllegalStateException e) {
        }
        
        return null;
    }
    
}
