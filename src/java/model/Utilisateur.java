package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Utilisateur implements Serializable {
    
    @Id
    private String pseudo;
    private Chat chat;
    private String avatar ;
    
    public Utilisateur(){
        
    }
    
    public Utilisateur(String pseudo,String avatar){
        this.pseudo = pseudo ;
        this.avatar = avatar ;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String image) {
        this.avatar = image;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.pseudo != null ? this.pseudo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the pseudo fields are not set
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.pseudo == null && other.pseudo != null) || (this.pseudo != null && !this.pseudo.equals(other.pseudo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Utilisateur[ pseudo=" + pseudo + " ]";
    }    
}