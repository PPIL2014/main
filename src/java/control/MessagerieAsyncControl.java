/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
@Dependent
@ViewScoped
public class MessagerieAsyncControl {
    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    
    private Utilisateur expediteur;
    
    private Utilisateur destinataire;
    
    @ManagedProperty("#{sessionBean.utilisateur.getConversation()}")
    private Conversation conversation;
    
    private String contenu;
    
    @ManagedProperty("#{param.pseudo}")
    private String pseudo;
    
    /**
     * Creates a new instance of MessagerieAsyncControl
     */
    public MessagerieAsyncControl() {
    }
    
    @PostConstruct
    public void init(){
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("conv");
        System.err.println("id : "+id);
        conversation = getConversation(Long.parseLong(id));
        destinataire = conversation.getDestinataire();
        expediteur = conversation.getExpediteur();
    }
    
    public Collection<MessageConversation> getMessages(){
        return conversation.getMessages() ;
    }

    public Utilisateur getExpediteur() {
        return expediteur;
    }
    
    public void setExpediteur(Utilisateur expediteur) {
        this.expediteur = expediteur;
    }

    public Utilisateur getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(Utilisateur destinataire) {
        this.destinataire = destinataire;
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

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return pseudo;
    }
    
    /**
     * Methode permettant de récupérer une liste de messages privés (MessageConversation)
     * @return la liste des messages privés (MessageConversation)
     */
    public List<MessageConversation> getPrivateMessages() {
        return (List<MessageConversation>) conversation.getMessages();
    }
    
    /**
     * Méthode permettant de récupérer la conversation privée entre 2 utilisateurs
     * @return la (première et en théorie unique) conversation entre les deux utilisateurs
     */
    public Conversation getConversation(Long id) {
        try{
            
            if(id==null)
                return null;
            ut.begin();
            Conversation results =  (Conversation) em.find(Conversation.class, id);
            ut.commit();
            
            return results;
            
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
           
        return null;
    }
    
    public String send() {
        try {
            
            System.err.println("Message : "+contenu);
            MessageConversation m = new MessageConversation();
            m.setConversation(conversation);
            m.setContenu(contenu);
            m.setExpediteur(expediteur);
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
