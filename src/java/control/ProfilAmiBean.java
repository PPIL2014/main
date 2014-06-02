/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Utilisateur;

@ManagedBean
@RequestScoped
public class ProfilAmiBean {

    private Utilisateur ami;
    
    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;

    @ManagedProperty(value="#{param.pseudo}")
    private String pseudo;
    
    public Utilisateur getAmi() {
        return ami;
    }

    public void setAmi(Utilisateur ami) {
        this.ami = ami;
    }
    
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
        ami = em.find(Utilisateur.class, pseudo);
    }
    
    public String getUrlAvatar () {
        return getAmi().getAvatar() == null?"/resources/images/apercu.png":getAmi().getAvatar().getUrl() ;
    }
}
