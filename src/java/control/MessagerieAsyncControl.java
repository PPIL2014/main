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

    @ManagedProperty(value ="#{session.user}")
    public Utilisateur expediteur;
    public Utilisateur destinataire;
    
    /**
     * Creates a new instance of MessagerieAsyncControl
     */
    public MessagerieAsyncControl() {
    }
    
    public ArrayList<MessageConversation> getMessages(){
        
        for(Conversation c : expediteur.getConversations()){
            if(c.getDestinataire().equals(destinataire)){
                return c.getMessages();
            }
        }
        
        return null;
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
    
    
    
}
