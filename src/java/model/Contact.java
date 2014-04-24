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
    private Boolean estBloque;
    private Boolean estAccepte;
    @OneToOne
    private Utilisateur estEnContactAvec;

    public Contact() {
        
    }
    
    public Contact(boolean estAccepte, boolean estBloque, Utilisateur contact){
        this.estAccepte = estAccepte;
        this.estBloque = estBloque;
        this.estEnContactAvec = contact;
    }
    
    public Boolean bloquer() {
        return null;
    }

    public Boolean debloquer() {
        return null;
    }

    public Boolean accepter() {
        return null;
    }

    public Boolean refuser() {
        return null;
    }

    public Boolean getEstBloque() {
        return estBloque;
    }

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public Boolean getEstAccepte() {
        return estAccepte;
    }

    public void setEstAccepte(Boolean estAccepte) {
        this.estAccepte = estAccepte;
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