/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author thomas
 */
@Entity
public class Galerie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String nom;
    
    private Integer visibilite;
    @OneToOne
    private Utilisateur proprietaire;
    /**
     * 
     * @element-type Image
     */
    @OneToMany
    private Collection<Image>  images;
    
    public void ajouterImage(Image image) {
    }

    public void retirerImage(Image image) {
    }

    public Boolean rendreVisible() {
        return null;
    }

    public Boolean rendreInivisible() {
        return null;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getVisibilite() {
        return visibilite;
    }

    public void setVisibilite(Integer visibilite) {
        this.visibilite = visibilite;
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Collection<Image> getImages() {
        return images;
    }

    public void setImages(Collection<Image> images) {
        this.images = images;
    }    
}
