/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.ArrayList;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
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
    @PersistenceUnit(unitName="DateRoulettePU")
    private EntityManagerFactory emf;
    @PersistenceContext
    private EntityManager em;
    private UserTransaction ut;

    

    @ManagedProperty(value ="#{session.user}")
    public Utilisateur expediteur;
    public Utilisateur destinataire;
    public Conversation conversation;
    public String message;
    
    /**
     * Creates a new instance of MessagerieAsyncControl
     */
    public MessagerieAsyncControl() {
    }
    
    public ArrayList<MessageConversation> getMessages(){
        return conversation.getMessages();
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String send() throws NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException{
        MessageConversation m = new MessageConversation() ;
        m.setContenu(message);
        m.setConversation(conversation);
        ut.begin();
            em.merge(m);
        ut.commit();
        
        return "MessagerieAsynchrone.xhtml";
    }
    
}
