/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.ArrayList;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import model.Utilisateur;

/**
 * Classe de messagerie asynchrone, permettant d'obtenir les resumes de messagerie : 
 * 3 lignes correspondant aux 3 derniers messages de la conversation.
 * @author Renaud
 */
@Named(value = "messagerieAsyncResumeMessages")
@Dependent
@ViewScoped
public class MessagerieAsyncResumeMessages {

    @ManagedProperty(value ="#{session.user}")
    public Utilisateur client;
    public Utilisateur[] destinataires;
    public int id;
    
    /**
     * Creates a new instance of MessagerieAsyncResumeMessages
     */
    public MessagerieAsyncResumeMessages() 
    {
        this.destinataires = (Utilisateur[]) client.getConversations().toArray();
        id =0;
    }
    
    public boolean hasNext()
    {
        return id<destinataires.length;
    }
    
    public ResumeMessages nextResumeMessage ()
    {
        if (!hasNext())
            return null;
       ResumeMessages rm = null;
        /*try {
            rm = new ResumeMessages(GestionMessagerie.getConversation(client, destinataires[id]));
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(MessagerieAsyncResumeMessages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(MessagerieAsyncResumeMessages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(MessagerieAsyncResumeMessages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(MessagerieAsyncResumeMessages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(MessagerieAsyncResumeMessages.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       id++;
       return rm;
    }
    
    public Collection<ResumeMessages> ListMessage()
    {
        Collection<ResumeMessages> liste = new ArrayList<ResumeMessages>();
        while (hasNext())
            liste.add(nextResumeMessage());
        return liste;
    }
    public Utilisateur getExpediteur() {
        return client;
    }

    public void setExpediteur(Utilisateur expediteur) {
        this.client = expediteur;
    }

    public Utilisateur[] getDestinataires() {
        return destinataires;
    }

    public void setDestinataires(Utilisateur[] destinataires) {
        this.destinataires = destinataires;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
