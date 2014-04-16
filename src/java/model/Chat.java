package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Chat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Utilisateur user1;
    private Utilisateur user2;
    private Collection<Message> listeMessages;
    
    public Chat(){
        
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getUser1() {
        return this.user1;
    }

    public void setUser1(Utilisateur user1) {
        this.user1 = user1;
    }

    public Utilisateur getUser2() {
        return this.user2;
    }

    public void setUser2(Utilisateur user2) {
        this.user2 = user2;
    }

    public Collection<Message> getListeMessages() {
        return this.listeMessages;
    }

    public void setListeMessages(Collection<Message> listeMessages) {
        this.listeMessages = listeMessages;
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
        if (!(object instanceof Chat)) {
            return false;
        }
        Chat other = (Chat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Chat[ id=" + id + " ]";
    }    
}