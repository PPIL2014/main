package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

@Entity
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Utilisateur estEnContactAvec;

    public Contact() {
        
    }
    
    public Contact(Type type, Utilisateur contact){
        /*this.estAccepte = estAccepte;
        this.estBloque = estBloque;*/
        this.type = type;
        this.estEnContactAvec = contact;
    }
    
    private Boolean estBloque;
    private Boolean estAccepte;

    public enum Type { ENATTENTE, DEMANDE, REFUSE, AMI, FAVORI, BLOQUE,BLOQUECHAT };
    
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public Utilisateur getEstEnContactAvec() {
        return estEnContactAvec;
    }

    public void setEstEnContactAvec(Utilisateur estEnContactAvec) {
        this.estEnContactAvec = estEnContactAvec;
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
        if (!(object instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.Contact[ id=" + id + " ]";
    }    
}
