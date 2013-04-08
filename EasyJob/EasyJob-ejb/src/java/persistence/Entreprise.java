/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author celie
 */
@Entity
public class Entreprise implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private String nomEntreprise;
    private String domaine;
    private String telephone;
    private String statut;
    private String siteWeb;
    private String description;
    private int nbEmployes;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Adresse adresse;
    
    @OneToMany(mappedBy="entreprise", cascade = CascadeType.MERGE)
    private List<Employeur> employeurs;

    
    public Entreprise() {
        this.employeurs = new ArrayList<Employeur>();
    }
    
    public Entreprise(String nom, String domaine, String tel, String statut, Adresse adr) {
        this.nomEntreprise = nom;
        this.domaine = domaine;
        this.telephone = tel;
        this.statut = statut;
        this.adresse = adr;
        this.employeurs = new ArrayList<Employeur>();
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getNomEntreprise() != null ? getNomEntreprise().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entreprise)) {
            return false;
        }
        Entreprise other = (Entreprise) object;
        if ((this.getNomEntreprise() == null && other.getNomEntreprise() != null) || (this.getNomEntreprise() != null && !this.nomEntreprise.equals(other.nomEntreprise))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistance.Entreprise[ id=" + getNomEntreprise() + " ]";
    }

    /**
     * @return the nomEntreprise
     */
    public String getNomEntreprise() {
        return nomEntreprise;
    }

    /**
     * @param nomEntreprise the nomEntreprise to set
     */
    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    /**
     * @return the domaine
     */
    public String getDomaine() {
        return domaine;
    }

    /**
     * @param domaine the domaine to set
     */
    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the statut
     */
    public String getStatut() {
        return statut;
    }

    /**
     * @param statut the statut to set
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * @return the siteWeb
     */
    public String getSiteWeb() {
        return siteWeb;
    }

    /**
     * @param siteWeb the siteWeb to set
     */
    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the nbEmployes
     */
    public int getNbEmployes() {
        return nbEmployes;
    }

    /**
     * @param nbEmployes the nbEmployes to set
     */
    public void setNbEmployes(int nbEmployes) {
        this.nbEmployes = nbEmployes;
    }

    /**
     * @return the adresse
     */
    public Adresse getAdresse() {
        return adresse;
    }

    /**
     * @param adresse the adresse to set
     */
    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    /**
     * @return the employeurs
     */
    public List<Employeur> getEmployeurs() {
        return employeurs;
    }

    /**
     * @param employeurs the employeurs to set
     */
    public void setEmployeurs(List<Employeur> employeurs) {
        this.employeurs = employeurs;
    }
    
}
