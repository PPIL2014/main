/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ul.dateroulette.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String pseudo;
    private Boolean estBloque;
    private Boolean estAccepte;
    @OneToOne
    private Utilisateur estEnContactAvec;
    
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

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pseudo != null ? pseudo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the pseudo fields are not set
        if (!(object instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) object;
        if ((this.pseudo == null && other.pseudo != null) || (this.pseudo != null && !this.pseudo.equals(other.pseudo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.Contact[ pseudo=" + pseudo + " ]";
    }
    
}
