/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author thomas
 */
@Entity
public class SignalementUtilisateur extends Signalement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private Utilisateur utilisateurSignale;
    
    public Utilisateur getUtilisateurSignale() {
        return utilisateurSignale;
    }

    public void setUtilisateurSignale(Utilisateur utilisateurSignale) {
        this.utilisateurSignale = utilisateurSignale;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
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
       
        if(object == null){
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        SignalementUtilisateur other = (SignalementUtilisateur) object;

        return Objects.equals((Object)this.utilisateurSignale, (Object)other.utilisateurSignale) && Objects.equals((Object)this.getEmetteur(), (Object)other.getEmetteur());
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.SignalementUtilisateur[ id=" + id + " ]";
    }
    
}
