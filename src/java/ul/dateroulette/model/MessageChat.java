package ul.dateroulette.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

@Entity
public class MessageChat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String contenu;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    @OneToOne
    private SessionChat sessionChat;
    @OneToOne
    private Utilisateur expediteur;
    
    public MessageChat(){
        
    }
    
    public MessageChat(String contenu, Utilisateur user){
        this.contenu = contenu;
        this.expediteur = user;
        this.date =  new Date();
    }
    
    public String getContenu() {
        return contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public SessionChat getSessionChat() {
        return sessionChat;
    }
    
    public void setSessionChat(SessionChat sessionChat) {
        this.sessionChat = sessionChat;
    }
    
    public Utilisateur getExpediteur() {
        return expediteur;
    }
    
    public void setExpediteur(Utilisateur expediteur) {
        this.expediteur = expediteur;
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
        if (!(object instanceof MessageChat)) {
            return false;
        }
        MessageChat other = (MessageChat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.MessageChat[ id=" + id + " ]";
    }    
}
