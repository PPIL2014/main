/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Conversation;
import model.MessageConversation;
import model.Utilisateur;

/**
 *
 * @author Faouz
 */
@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {
    
   private Utilisateur utilisateur;
   public Utilisateur dest = new Utilisateur();
   public Utilisateur dest2 = new Utilisateur();
   public  Utilisateur dest3 = new Utilisateur();
    public SessionBean() {
        utilisateur = new Utilisateur();
        utilisateur.setNom("Hamid");
        utilisateur.setPseudo("Toto");
        ArrayList<Conversation> conv = new ArrayList<Conversation>();
        
        
        Conversation un = new Conversation();
        ArrayList<MessageConversation> msg = new ArrayList<MessageConversation> ();
        MessageConversation msg1 = new MessageConversation();
        msg1.setDate(new Date());
        msg1.setContenu("Saaalut !");
        msg1.setId((long)1);
        msg.add(msg1);
        
        msg1 = new MessageConversation();
        msg1.setDate(new Date());
        msg1.setContenu("ça va?");
        msg1.setId((long)2);
        msg.add(msg1);
        
        
        un.setMessages(msg);
        dest.setPseudo("Micka");
        un.setDestinataire(dest);
        
        Conversation deux = new Conversation();
        deux.setMessages(msg);
        dest2.setPseudo("Mickey");
        deux.setDestinataire(dest2);
        
        Conversation trois = new Conversation();
       
        dest3.setPseudo("Mickael");
        trois.setDestinataire(dest3);
        trois.setMessages(msg);
        
        un.setExpediteur(utilisateur);
        deux.setExpediteur(utilisateur);
        trois.setExpediteur(utilisateur);
        
        
        conv.add(un);
        conv.add(deux);
        conv.add(trois);
        
        utilisateur.setConversations(conv);
    }
   
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
   
   
    
}
