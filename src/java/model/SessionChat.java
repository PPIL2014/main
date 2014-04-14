/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
public class SessionChat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean estDemarree;
    @OneToOne
    private Utilisateur utilisateur1;
    @OneToOne
    private Utilisateur utilisateur2;
    /**
     * 
     * @element-type MessageChat
     */
    @OneToMany
    private List<MessageChat>  messages;

    public SessionChat() {
    }

    public SessionChat(Utilisateur user1, Utilisateur user2){
        this.utilisateur1 = user1;
        this.utilisateur2 = user2;
        this.messages = Collections.synchronizedList(new LinkedList());
    }

    
    public Boolean getEstDemarree() {
        return estDemarree;
    }

    public void setEstDemarree(Boolean estDemarree) {
        this.estDemarree = estDemarree;
    }

    public Utilisateur getUtilisateur1() {
        return utilisateur1;
    }

    public void setUtilisateur1(Utilisateur utilisateur1) {
        this.utilisateur1 = utilisateur1;
    }

    public Utilisateur getUtilisateur2() {
        return utilisateur2;
    }

    public void setUtilisateur2(Utilisateur utilisateur2) {
        this.utilisateur2 = utilisateur2;
    }

    public Collection<MessageChat> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageChat> messages) {
        this.messages = messages;
    }

    public Boolean demarrerSessionChat() {
        return null;
    }

    public Boolean terminerSessionChat() {
        return null;
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
        if (!(object instanceof SessionChat)) {
            return false;
        }
        SessionChat other = (SessionChat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.SessionChat[ id=" + id + " ]";
    }

    public MessageChat getFirstAfter(Date lastUpdate) {
        if(messages.isEmpty()) 
            return null; 
        if(lastUpdate == null) 
            return messages.get(0); 
        
        for(MessageChat m : messages) { 
            if(m.getDate().after(lastUpdate)) 
                return m; 
        } 
        
        return null; 
    }      

}
