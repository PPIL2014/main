/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.Collection;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    
    @ManagedProperty("#{sessionBean.utilisateur}")
    public Utilisateur expediteur;
    public Utilisateur destinataire;
    @ManagedProperty("#{sessionBean.utilisateur.getConversation()}")
    public Conversation conversation;
    public String message;
    @ManagedProperty("#{param.pseudo}")
    public String pseudo;
    
    /**
     * Creates a new instance of MessagerieAsyncControl
     */
    public MessagerieAsyncControl() {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return pseudo;
    }
    
    public String send() throws NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException{
        MessageConversation m = new MessageConversation() ;
        m.setContenu(message);
        m.setExpediteur(expediteur);
        conversation.getMessages().add(m) ;
        ut.begin();
        em.persist(m);
        ut.commit();
        
        return "MessagerieAsynchrone.xhtml?pseudo="+destinataire.getPseudo();
    }
    
}
