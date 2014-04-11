/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ul.dateroulette.model;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author thomas
 */
@Entity
public class Conversation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Utilisateur destinataire;
    @OneToOne
    private Utilisateur expediteur;
    /**
     * 
     * @element-type MessageConversation
     */
    @OneToMany
    private ArrayList<MessageConversation>  messages;
    
    public Utilisateur getDestinataire() {
        return destinataire;
    }
    public void setDestinataire(Utilisateur destinataire) {
        this.destinataire = destinataire;
    }
    public Utilisateur getExpediteur() {
        return expediteur;
    }
    public void setExpediteur(Utilisateur expediteur) {
        this.expediteur = expediteur;
    }
    public ArrayList<MessageConversation> getMessages() {
        return messages;
    }
    public void setMessages(ArrayList<MessageConversation> messages) {
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conversation)) {
            return false;
        }
        Conversation other = (Conversation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.Conversation[ id=" + id + " ]";
    }
    
}
